package com.akkademy

import scala.concurrent.duration.DurationInt

import org.scalatest.FunSpecLike
import org.scalatest.Matchers

import com.akkademy.messages.Flush
import com.akkademy.messages.SetRequest
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Status
import akka.testkit.TestActorRef
import akka.testkit.TestProbe
import akka.util.Timeout

class FSMClientActorSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem("akkademyFSMClient-test", ConfigFactory.empty)
  implicit val timeout = Timeout(5 seconds)

  val dbRef = TestActorRef[AkkademyCache]
  val db = dbRef.underlyingActor
  val testProbe = TestProbe()

  describe("FSMClientActor") {
    val fsmClientActor = TestActorRef[FSMClientActor](Props.create(classOf[FSMClientActor], dbRef.path.toString))
    val fsmClient = fsmClientActor.underlyingActor

    it("should transition from Disconnected to ConnectedAndPending when getting a msg") {
      fsmClient.stateName should equal(DisconnectedState)
      fsmClientActor ! SetRequest("key", "value", testProbe.ref)
      fsmClient.stateName should equal(ConnectedAndPendingState)
      db.map get "key" should equal(None)
    }

    it("should transition from ConnectedAndPending to Connected when flushing") {
      fsmClient.stateName should equal(ConnectedAndPendingState)
      fsmClientActor ! Flush()
      fsmClient.stateName should equal(ConnectedState)
      db.map get "key" should equal(Some("value"))
      testProbe.expectMsg(Status.Success)
    }
  }

}
