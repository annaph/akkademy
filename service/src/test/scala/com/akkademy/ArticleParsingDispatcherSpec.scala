package com.akkademy

import scala.concurrent.Await
import scala.concurrent.Promise
import scala.concurrent.duration.DurationInt

import org.scalatest.FunSpec
import org.scalatest.Matchers

import com.akkademy.TestHelper.TestCameoActor
import com.akkademy.messages.ParseHtmlArticle
import com.typesafe.config.ConfigFactory

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinGroup
import akka.util.Timeout

class ArticleParsingDispatcherSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem("akkademyServiceDispatcher-test", ConfigFactory.parseString(TestHelper.dispatcherConfig))
  implicit val timeout = Timeout(12 seconds)

  describe("ArticleParsing actor with Dispatcher") {
    val actors: IndexedSeq[ActorRef] = (0 to 7) map { _ =>
      system.actorOf(
        Props.create(classOf[ArticleParsingActor]).
          withDispatcher("akka.actor.article-parsing-dispatcher"))
    }

    val actorPaths: List[String] =
      (actors map { _.path.toStringWithoutAddress }).toList
    val workerRouter: ActorRef = system.actorOf(
      RoundRobinGroup(actorPaths).props,
      "workerRouter")

    it("should do work concurrently") {
      val p: Promise[String] = Promise()
      val cameoActor: ActorRef =
        system.actorOf(Props.create(classOf[TestCameoActor], p))

      (0 to 2000) foreach { x =>
        workerRouter ! (new ParseHtmlArticle("http://some-link.com/", TestHelper.file, cameoActor))
      }

      Await.ready(p.future, 20 seconds)
    }
  }

}
