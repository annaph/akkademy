package com.akkademy

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

import com.akkademy.messages.SetRequest

import akka.actor.ActorSystem
import akka.testkit.TestActorRef

class AkkademyCacheSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem()

  describe("akkademyCache") {
    describe("givenSetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyCache)
        actorRef ! SetRequest("key", "value")
        val akkademyCache = actorRef.underlyingActor
        akkademyCache.map.get("key") should equal(Some("value"))
      }
    }
  }

}
