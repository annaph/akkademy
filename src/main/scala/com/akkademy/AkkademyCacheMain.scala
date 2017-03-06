package com.akkademy

import java.io.File

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props

object Main extends App {

  val configFile: Try[String] = Try {
    getClass.getClassLoader.getResource("akkademy-cache.conf").getFile
  }
  val config: Try[Config] = configFile map { f =>
    ConfigFactory.parseFile(new File(f))
  }

  config match {
    case Success(c) =>
      val system = ActorSystem("akkademyCache")

      system.actorOf(Props(classOf[AkkademyCache]), "akkademy-db")
      system.actorOf(Props(classOf[PongActor]), "pong")
    case Failure(e) =>
      println("Error starting actor system")
      e.printStackTrace()
  }

}
