lazy val commonSettings = Seq(
	organization := "com.akkademy",
	scalaVersion := "2.12.1",
	
	scalacOptions ++= Seq(
		"-unchecked",
		"-deprecation",
		"-feature",
		"-language:postfixOps")
)

lazy val commonDependencies = Seq(
	libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.4.16")
)

lazy val messages = (project in file("messages"))
	.settings(commonSettings)
	.settings(commonDependencies)

lazy val cache = (project in file("cache"))
  .settings(commonSettings)
  .settings(commonDependencies)
  .dependsOn(messages)

lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(commonDependencies)
  .dependsOn(messages)
  .dependsOn(cache)

lazy val root = (project in file("."))
	.aggregate(messages, cache, client)
	.settings(commonSettings)
	.settings(
		name := AkkademyBuild.prefixName + "root"
	)
