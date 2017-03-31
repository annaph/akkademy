package com.akkademy

import com.akkademy.messages.Request

import akka.actor.Actor

class SimpleClientActor(remoteAddress: String) extends Actor {
  private val remoteDb = context.system.actorSelection(remoteAddress)

  override def receive = {
    case x: Request =>
      remoteDb forward x
  }

}
