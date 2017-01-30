package com.akkademy

import scala.collection.mutable.HashMap

import com.akkademy.messages.{ GetRequest, SetRequest, KeyNotFoundException }

import akka.actor.Actor
import akka.actor.Status
import akka.event.Logging

class AkkademyDb extends Actor {

  val map = HashMap.empty[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) =>
      log.info(s"received SetRequest - key: ${key} value: ${value}")
      map.put(key, value)
      sender() ! Status.Success
    case GetRequest(key) =>
      log.info(s"received GetRequest - key: ${key}")
      map.get(key) match {
        case None =>
          sender() ! Status.Failure(KeyNotFoundException(key))
        case Some(x) =>
          sender() ! x
      }
    case o =>
      log.info(s"received unknown message: ${0}")
      sender() ! Status.Failure(new ClassNotFoundException)
  }

}
