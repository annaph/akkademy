package com.akkademy

import com.akkademy.StateContainerTypes.RequestQueue
import com.akkademy.messages.Connected
import com.akkademy.messages.Flush
import com.akkademy.messages.Request

import akka.actor.FSM

sealed trait State
case object DisconnectedState extends State
case object ConnectedState extends State
case object ConnectedAndPendingState extends State

object StateContainerTypes {
  type RequestQueue = List[Request]
}

class FSMClientActor(address: String) extends FSM[State, RequestQueue] {
  private val remoteDb = context.system.actorSelection(address)

  startWith(DisconnectedState, List[Request]())

  when(DisconnectedState) {
    case Event(x: Request, container: RequestQueue) =>
      remoteDb ! new Connected
      stay using (container :+ x)
    case Event(_: Connected, container: RequestQueue) =>
      container match {
        case Nil =>
          goto(ConnectedState)
        case _ =>
          goto(ConnectedAndPendingState)
      }
    case x =>
      println("uhh didn't quite get that: " + x)
      stay
  }

  when(ConnectedState) {
    case Event(x: Request, container: RequestQueue) =>
      goto(ConnectedAndPendingState) using (container :+ x)
  }

  when(ConnectedAndPendingState) {
    case Event(x: Request, container: RequestQueue) =>
      stay using (container :+ x)
    case Event(_: Flush, container: RequestQueue) =>
      remoteDb ! container
      goto(ConnectedState) using Nil
  }

  initialize()
}
