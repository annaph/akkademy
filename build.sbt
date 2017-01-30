name := "akkademy-db"
organization := "com.akkademy-db"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.1"

scalacOptions += "-unchecked"
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.4.16",
	"com.typesafe.akka" %% "akka-remote" % "2.4.16",
	"com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test")

mappings in (Compile, packageBin) ~= { _.filter {
	case (_, name) =>
		name != "application.conf"
	}
}

