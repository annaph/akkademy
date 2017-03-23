package com.akkademy

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

import com.akkademy.messages.SetRequest
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Status
import akka.testkit.TestActorRef
import akka.testkit.TestProbe

class AkkademyCacheSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem("akkademyCache-test", ConfigFactory.empty)

  describe("akkademyCache") {
    describe("givenSetRequest") {
      val testProbe = TestProbe()

      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyCache)
        actorRef ! SetRequest("key", "value", testProbe.ref)

        val akkademyCache = actorRef.underlyingActor
        akkademyCache.map.get("key") should equal(Some("value"))

        testProbe expectMsg Status.Success
      }
    }
  }

  describe("given List[SetRequest]") {
    it("should place key/values int mpa") {
      val testProbe = TestProbe()

      val actorRef = TestActorRef(new AkkademyCache)
      actorRef ! List(
        SetRequest("key1", "value1", testProbe.ref),
        SetRequest("key2", "value2", testProbe.ref))

      val akkademyCache = actorRef.underlyingActor
      akkademyCache.map.get("key1") should equal(Some("value1"))
      akkademyCache.map.get("key2") should equal(Some("value2"))

      testProbe expectMsg Status.Success
      testProbe expectMsg Status.Success
    }
  }

}
