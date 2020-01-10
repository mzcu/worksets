
name := "worksets"

version := "0.1"

scalaVersion := "2.12.10"

wartremoverErrors ++= Warts.unsafe.filterNot(_ == Wart.DefaultArguments)



// Core project settings
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"



// UI project settings
lazy val ui: Project = (project in file("worksets-ui"))
  .settings(
    name := "worksets-ui",
    libraryDependencies ++= Seq(
      "com.thoughtworks.binding" %%% "dom" % "latest.release"
    ),
    scalaJSUseMainModuleInitializer := false,
    scalaJSUseMainModuleInitializer in Test := false,
    crossPaths := false,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  ).enablePlugins(ScalaJSPlugin)

