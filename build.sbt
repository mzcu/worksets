
name := "worksets"

version := "0.1"

scalaVersion := "2.13.3"

wartremoverErrors ++= Warts.unsafe.filterNot(_ == Wart.DefaultArguments)

libraryDependencies += "com.lihaoyi" % "ammonite" % "2.2.0" cross CrossVersion.full
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"

val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)