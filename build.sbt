ThisBuild / organization := "io.codeswarm"
ThisBuild / version := "0.1.1"
ThisBuild / scalaVersion := "2.13.18"

lazy val javafxVersion = "21.0.8"
lazy val scalaFxVersion = "21.0.0-R32"
lazy val breezeVersion = "2.1.0"
lazy val scalaTestVersion = "3.2.19"

lazy val javafxPlatform: String = {
  val os = System.getProperty("os.name", "").toLowerCase
  val arch = System.getProperty("os.arch", "").toLowerCase

  if (os.contains("win")) "win"
  else if (os.contains("mac")) {
    if (arch.contains("aarch64") || arch.contains("arm64")) "mac-aarch64" else "mac"
  } else if (arch.contains("aarch64") || arch.contains("arm64")) "linux-aarch64"
  else "linux"
}

lazy val root = (project in file("."))
  .settings(
    name := "reynolds-boids-visualization",
    Compile / mainClass := Some("io.codeswarm.boids.app.BoidSimulation"),
    fork := true,
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafx" % scalaFxVersion,
      "org.openjfx" % "javafx-base" % javafxVersion classifier javafxPlatform,
      "org.openjfx" % "javafx-graphics" % javafxVersion classifier javafxPlatform,
      "org.openjfx" % "javafx-controls" % javafxVersion classifier javafxPlatform,
      "org.scalanlp" %% "breeze" % breezeVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint"
    ),
    Test / parallelExecution := false
  )
