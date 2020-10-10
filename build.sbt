import sbtassembly.AssemblyPlugin.defaultUniversalScript

name := "worksets"

version := "0.1"

scalaVersion := "0.27.0-RC1"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "com.lihaoyi" %% "fansi" % "0.2.7"
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"

val circeVersion = "0.14.0-M1"

libraryDependencies := libraryDependencies.value.map(_.withDottyCompat(scalaVersion.value))

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.2" % "test",
  ("org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % "test")
    .intransitive()
    .withDottyCompat(scalaVersion.value),
  ("org.scalacheck" %% "scalacheck" % "1.14.3" % "test").withDottyCompat(scalaVersion.value)
)


libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion).map(_.withDottyCompat(scalaVersion.value))

libraryDependencies += "org.jline" % "jline-reader" % "3.16.0"


scalacOptions ++= Seq("-language:implicitConversions", "-source:3.0")

mainClass in assembly := Some("worksets.Main")
test in assembly := {}
assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = false)))
assemblyJarName in assembly := s"${name.value}-${version.value}"

