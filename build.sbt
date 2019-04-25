name := "flight"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += Resolver.jcenterRepo

val akka = "com.typesafe.akka"
val scalaTestVersion = "3.0.5"

val akkaVersion = "2.5.22"

val akkaDependencies = Seq(

  akka % "akka-actor_2.12" % akkaVersion,
  akka % "akka-stream_2.12" % akkaVersion,
  akka %% "akka-slf4j" % akkaVersion,
)

val akkaHttpVersion = "10.1.7"

val akkaHttpDependencies = Seq(
  akka % "akka-http_2.12" % akkaHttpVersion,
  akka %% "akka-http-spray-json" % akkaHttpVersion,
  akka %% "akka-http-testkit" % akkaHttpVersion
)

val logback = "ch.qos.logback"
val logbackVersion = "1.2.3"

val loggingDependencies = Seq(
  logback % "logback-classic" % logbackVersion
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % scalaTestVersion % "test",
  "org.json4s" %% "json4s-jackson" % "3.2.11",
)
libraryDependencies ++= akkaDependencies
libraryDependencies ++= akkaHttpDependencies
libraryDependencies ++= loggingDependencies