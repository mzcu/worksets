import sbtassembly.AssemblyPlugin.defaultUniversalScript

name := "worksets"

version := "0.1"

scalaVersion := "3.1.0"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0"
libraryDependencies += "com.lihaoyi" %% "fansi" % "0.3.0" exclude("com.lihaoyi", "sourcecode_3")
libraryDependencies += ("com.lihaoyi" % "scalatags" % "0.11.0").cross(CrossVersion.for3Use2_13)
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" % "circe-core",
  "io.circe" % "circe-parser"
).map(_ % circeVersion).map(_.cross(CrossVersion.for3Use2_13))

libraryDependencies += "org.jline" % "jline-reader" % "3.21.0"
libraryDependencies += "com.mitchtalmadge" % "ascii-data" % "1.4.0"

scalacOptions ++= Seq("-language:implicitConversions", "-source:3.0")


assembly / mainClass := Some("worksets.Main")
assembly / test := {}
assembly / assemblyPrependShellScript := Some(defaultUniversalScript(shebang = false))
assembly / assemblyJarName := s"${name.value}-${version.value}"