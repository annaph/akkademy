package com.akkademy

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

import com.akkademy.messages.SetRequest

import akka.actor.ActorSystem
import akka.testkit.TestActorRef

class AkkademyDbSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem()

  describe("akkademyDb") {
    describe("givenSetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! SetRequest("key", "value")
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.map.get("key") should equal(Some("value"))
      }
    }
  }

}
