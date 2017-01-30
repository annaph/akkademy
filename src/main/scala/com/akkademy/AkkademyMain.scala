package com.akkademy

import akka.actor.ActorSystem
import akka.actor.Props

object Main extends App {
  val system = ActorSystem("akkademy")

  system.actorOf(Props(classOf[AkkademyDb]), "akkademy-db")
  system.actorOf(Props(classOf[PongActor]), "pong")
}
