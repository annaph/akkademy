package com.akkademy

import com.akkademy.messages.Connected
import com.akkademy.messages.Disconnected
import com.akkademy.messages.Request

import akka.actor.Actor
import akka.actor.Stash

class HotswapClientActor(remoteAddress: String) extends Actor with Stash {
  private val remoteDb = context.system.actorSelection(remoteAddress)

  override def receive = {
    case _: Request =>
      remoteDb ! new Connected
      stash
    case _: Connected =>
      unstashAll
      context.become(online)
  }

  def online: Receive = {
    case x: Request =>
      remoteDb forward x
    case _: Disconnected =>
      context.unbecome()
  }

}
