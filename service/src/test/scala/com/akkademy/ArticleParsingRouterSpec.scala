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
import akka.routing.BalancingPool
import akka.util.Timeout

class ArticleParsingRouterSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem("akkademyServiceRouter-test", ConfigFactory.empty)
  implicit val timeout = Timeout(12 seconds)

  describe("ArticleParsing actor with Router") {
    val workerRouter: ActorRef =
      system.actorOf(
        Props.create(classOf[ArticleParsingActor]).
          withRouter(new BalancingPool(7)), "balancing-pool-router")

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
