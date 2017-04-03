package com.akkademy

import scala.concurrent.duration.DurationInt

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

import com.akkademy.messages.GetRequest
import com.akkademy.messages.SetRequest
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Status
import akka.testkit.TestActorRef
import akka.testkit.TestProbe
import akka.util.Timeout

class HotswapClientActorSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem("akkademySimpleClient-test", ConfigFactory.empty)
  implicit val timeout = Timeout(5 seconds)

  describe("HotswapClientActor") {
    it("should set a value") {
      val dbRef = TestActorRef[AkkademyCache]
      val db = dbRef.underlyingActor
      val clientRef = TestActorRef(Props.create(classOf[HotswapClientActor], dbRef.path.toString))
      val probe = TestProbe()

      clientRef ! new SetRequest("key", "value", probe.ref)

      probe.expectMsg(Status.Success)
      db.map.get("key") should equal(Some("value"))
    }

    it("should get a value") {
      val dbRef = TestActorRef[AkkademyCache]
      val db = dbRef.underlyingActor
      db.map.put("key", "value")
      val clientRef = TestActorRef(Props.create(classOf[HotswapClientActor], dbRef.path.toString))
      val probe = TestProbe()

      clientRef ! new GetRequest("key", probe.ref)

      probe.expectMsg("value")
    }
  }

}
