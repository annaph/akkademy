package com.akkademy

import akka.actor.Actor
import akka.actor.Status.Failure

class PongActor extends Actor {

  override def receive = {
    case "Ping" =>
      sender ! "Pong"
    case _ =>
      sender ! Failure(new Exception("unknown message"))
  }

}
