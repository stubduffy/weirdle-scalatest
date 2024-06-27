import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "weirdle-scalatest",
      libraryDependencies ++= Seq(
      "org.scalatestplus" %% "selenium-4-21" % "3.2.19.0" % "test",
      "org.scalatest" %% "scalatest-flatspec" % "3.2.19" % "test",
      "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.19" % "test",
      "com.google.guava" % "guava" % "30.1.1-jre")
  )

