package com.akkademy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import com.akkademy.messages.ArticleBody
import com.akkademy.messages.GetRequest
import com.akkademy.messages.HttpResponse
import com.akkademy.messages.ParseArticle
import com.akkademy.messages.ParseHtmlArticle
import com.akkademy.messages.SetRequest

import akka.actor.Actor
import akka.pattern.ask
import akka.util.Timeout

class AskDemoArticleParser(
    cacheActorPath: String,
    httpClientActorPath: String,
    articleParserActorPath: String,
    implicit val timeout: Timeout) extends Actor {

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  override def receive = {
    case ParseArticle(uri) =>
      val senderRef = sender()
      val cacheResult = cacheActor ? GetRequest(uri)

      val result = cacheResult.recoverWith {
        case _: Exception =>
          val rawResult = httpClientActor ? uri

          rawResult flatMap {
            case HttpResponse(htmlString) =>
              articleParserActor ? ParseHtmlArticle(uri, htmlString)
            case _ =>
              Future.failed(new Exception("unknown response"))
          }

      }

      result.onComplete {
        case Success(x: String) =>
          println("cached result!")
          senderRef ! x
        case Success(ArticleBody(uri, body)) =>
          cacheActor ! SetRequest(uri, body)
          senderRef ! body
        case Failure(t) =>
          senderRef ! akka.actor.Status.Failure(t)
        case x =>
          println("unknown message! " + x)
      }
  }

}
