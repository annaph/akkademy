name := "akkademaid"
organization := "com.akkademaid"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.1"

scalacOptions += "-unchecked"
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
	"com.akkademy-db" %% "akkademy-db" % "0.0.1-SNAPSHOT",
	"com.typesafe.akka" %% "akka-actor" % "2.4.16",
	"com.syncthemall" % "boilerpipe" % "1.2.2",
	"com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test")
