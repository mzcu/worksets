
name := "worksets"

version := "0.1"

scalaVersion := "2.13.3"

wartremoverErrors ++= Warts.unsafe.filterNot(_ == Wart.DefaultArguments)


libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"

val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)