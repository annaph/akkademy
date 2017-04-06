name := AkkademyBuild.prefixName + "-service"
version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.syncthemall" % "boilerpipe" % "1.2.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test")
