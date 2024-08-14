ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.4.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .settings(
    name := "junit5-jupiter-starter-sbt",
    libraryDependencies ++= Seq(
      "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
      "org.junit.jupiter" % "junit-jupiter" % "5.11.0" % Test,
      "org.junit.platform" % "junit-platform-launcher" % "1.11.0" % Test,
    ),
    testOptions += Tests.Argument(jupiterTestFramework, "--display-mode=tree")
  )
