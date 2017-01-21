name := """akkademy-db"""

version := "1.0"

scalaVersion := "2.12.1"

scalacOptions += "-unchecked"
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.4.16",
	"com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

