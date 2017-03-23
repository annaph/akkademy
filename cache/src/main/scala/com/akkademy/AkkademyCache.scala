package com.akkademy

import scala.collection.mutable.HashMap

import com.akkademy.messages.Connected
import com.akkademy.messages.GetRequest
import com.akkademy.messages.KeyNotFoundException
import com.akkademy.messages.SetRequest

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Status
import akka.event.Logging

class AkkademyCache extends Actor {

  val map = HashMap.empty[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case x: Connected =>
      sender() ! x
    case SetRequest(key, value, senderRef) =>
      handleSetRequest(key, value, senderRef)
    case GetRequest(key, senderRef) =>
      handleGetRequest(key, senderRef)
    case x: List[_] =>
      x foreach {
        case SetRequest(key, value, senderRef) =>
          handleSetRequest(key, value, senderRef)
        case GetRequest(key, senderRef) =>
          handleGetRequest(key, senderRef)
      }
    case o =>
      log.info(s"received unknown message: ${0}")
      sender() ! Status.Failure(new ClassNotFoundException)
  }

  def handleSetRequest(key: String, value: Object, senderRef: ActorRef): Unit = {
    log.info(s"received SetRequest - key: ${key} value: ${value}")
    map.put(key, value)
    senderRef ! Status.Success
  }

  def handleGetRequest(key: String, senderRef: ActorRef): Unit = {
    log.info(s"received GetRequest - key: ${key}")
    map.get(key) match {
      case None =>
        senderRef ! Status.Failure(KeyNotFoundException(key))
      case Some(x) =>
        senderRef ! x
    }
  }

}
