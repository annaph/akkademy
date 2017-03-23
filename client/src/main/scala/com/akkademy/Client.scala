package com.akkademy

import scala.concurrent.duration.DurationInt

import com.akkademy.messages.GetRequest
import com.akkademy.messages.SetRequest

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.util.Timeout

class Client(remoteAddress: String) {
  implicit val timeout = Timeout(20 seconds)
  implicit val system = ActorSystem.create("LocalSystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key: String, value: Object) =
    remoteDb ? SetRequest(key, value)

  def get(key: String) =
    remoteDb ? GetRequest(key)

}
