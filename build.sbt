ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaBoids"
  )

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "15.0.1-R21",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)
