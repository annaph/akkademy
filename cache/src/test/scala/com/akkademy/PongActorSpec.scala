package com.akkademy

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout

class PongActorSpec extends FunSpecLike with Matchers {

  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem()
  val pongActor = system.actorOf(Props(classOf[PongActor]))

  describe("Pong actor") {
    it("should respond with Pong") {
      val future = askActor("Ping")
      val result = Await.result(future.mapTo[String], 1 second)

      assert(result == "Pong")
    }

    it("should fail on unknown message") {
      val future = askActor("unknown message")

      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }

  def askActor(message: String): Future[String] =
    (pongActor ? message).mapTo[String]

}
