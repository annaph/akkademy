package com.akkademy

import java.util.concurrent.TimeoutException

import com.akkademy.messages.GetRequest
import com.akkademy.messages.SetRequest

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Status.Failure
import akka.util.Timeout

class TellDemoArticleParser(
    cacheActorPath: String,
    httpClientActorPath: String,
    articleParserActorPath: String,
    implicit val timeout: Timeout) extends Actor {

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  implicit val es = context.dispatcher

  override def receive = {
    case ParseArticle(uri) =>
      val extraActor = buildExtraActor(sender(), uri)

      cacheActor tell (GetRequest(uri), extraActor)
      httpClientActor tell (uri, extraActor)

      context.system.scheduler.scheduleOnce(timeout.duration, extraActor, "timeout")
  }

  private def buildExtraActor(senderRef: ActorRef, uri: String): ActorRef = {
    return context.actorOf(Props(new Actor {
      override def receive = {
        case "timeout" =>
          senderRef ! Failure(new TimeoutException("timeout!"))
          context.stop(self)
        case body: String =>
          senderRef ! body
          context.stop(self)
        case HttpResponse(htmlString) =>
          articleParserActor ! ParseHtmlArticle(uri, htmlString)
        case ArticleBody(uri, body) =>
          cacheActor ! SetRequest(uri, body)
          senderRef ! body
          context.stop(self)
        case t =>
          println("ignoring msg: " + t.getClass)
      }
    }))
  }

}
