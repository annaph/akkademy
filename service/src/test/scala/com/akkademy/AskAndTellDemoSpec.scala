package com.akkademy

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import org.scalatest.FunSpec
import org.scalatest.Matchers

import com.akkademy.messages.GetRequest
import com.akkademy.messages.HttpResponse
import com.akkademy.messages.ParseArticle
import com.akkademy.messages.SetRequest

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Status.Failure
import akka.pattern.ask
import akka.testkit.TestProbe
import akka.util.Timeout
import de.l3s.boilerpipe.extractors.ArticleExtractor
import com.typesafe.config.ConfigFactory

class AskAndTellDemoSpec extends FunSpec with Matchers {

  implicit val system = ActorSystem("akkademyService-test", ConfigFactory.empty)
  implicit val timeout = Timeout(10 seconds)

  describe("ask demo") {
    val cacheProbe = TestProbe()
    val httpClientProbe = TestProbe()
    val articleParserActor = system.actorOf(Props(classOf[ParsingActor]))
    val askDemoActor = system.actorOf(Props(
      classOf[AskDemoArticleParser],
      cacheProbe.ref.path.toString,
      httpClientProbe.ref.path.toString,
      articleParserActor.path.toString,
      timeout))

    it("should provide parsed article") {
      val f = askDemoActor ? ParseArticle("http://www.google.com")

      cacheProbe.expectMsgType[GetRequest]
      cacheProbe.reply(Failure(new Exception("no cache")))

      httpClientProbe.expectMsgType[String]
      httpClientProbe.reply(HttpResponse(Articles.article1))

      cacheProbe.expectMsgType[SetRequest]

      val parsedArticle = Await.result(f, 10 seconds)

      parsedArticle.toString should include("I’ve been writing a lot in emacs lately")
      parsedArticle.toString should not include ("<body>")
    }

    it("should provide cached article") {
      val f = askDemoActor ? ParseArticle("http://www.google.com")

      cacheProbe.expectMsgType[GetRequest]
      cacheProbe.reply(ArticleExtractor.INSTANCE.getText(Articles.article1))

      val parsedArticle = Await.result(f, 10 seconds)

      parsedArticle.toString should include("I’ve been writing a lot in emacs lately")
      parsedArticle.toString should not include ("<body>")
    }

  }

  describe("tell demo") {
    val cacheProbe = TestProbe()
    val httpClientProbe = TestProbe()
    val articleParserActor = system.actorOf(Props(classOf[ParsingActor]))
    val tellDemoActor = system.actorOf(Props(
      classOf[TellDemoArticleParser],
      cacheProbe.ref.path.toString,
      httpClientProbe.ref.path.toString,
      articleParserActor.path.toString,
      timeout))

    it("should provide parsed article") {
      val f = tellDemoActor ? ParseArticle("http://www.google.com")

      cacheProbe.expectMsgType[GetRequest]
      cacheProbe.reply(new Exception("no cache"))

      httpClientProbe.expectMsgType[String]
      httpClientProbe.reply(HttpResponse(Articles.article1))

      cacheProbe.expectMsgType[SetRequest]

      val parsedArticle = Await.result(f, 10 seconds)

      parsedArticle.toString should include("I’ve been writing a lot in emacs lately")
      parsedArticle.toString should not include ("<body>")
    }

    it("should provide cached article") {
      val f = tellDemoActor ? ParseArticle("http://www.google.com")

      cacheProbe.expectMsgType[GetRequest]
      cacheProbe.reply(ArticleExtractor.INSTANCE.getText(Articles.article1))

      val parsedArticle = Await.result(f, 10 seconds)

      parsedArticle.toString should include("I’ve been writing a lot in emacs lately")
      parsedArticle.toString should not include ("<body>")
    }

  }

}
