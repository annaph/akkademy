package com.akkademy

import scala.collection.mutable.HashMap

import com.akkademy.messages.SetRequest

import akka.actor.Actor
import akka.event.Logging

class AkkademyDb extends Actor {

  val map = HashMap.empty[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) =>
      log.info(s"received SetRequest - key: ${key} value: ${value}")
      map.put(key, value)
    case o =>
      log.info(s"received unknown message: ${0}")
  }

}
