import org.gradle.api.tasks.scala.ScalaCompile
import org.gradle.api.tasks.wrapper.Wrapper

plugins {
    scala
    application
    id("com.diffplug.spotless") version "8.10.3"
}

group = "io.codeswarm"
version = "0.2.0"

repositories {
    mavenCentral()
}

val scalaLibraryVersion = "2.13.18"
val scalaFxVersion = "21.0.0-R32"
val javaFxVersion = "21.0.8"
val junitVersion = "5.13.4"

val javaFxPlatform =
    run {
        val os = System.getProperty("os.name", "").lowercase()
        val arch = System.getProperty("os.arch", "").lowercase()
        when {
            os.contains("win") -> "win"
            os.contains("mac") && (arch.contains("aarch64") || arch.contains("arm64")) -> "mac-aarch64"
            os.contains("mac") -> "mac"
            arch.contains("aarch64") || arch.contains("arm64") -> "linux-aarch64"
            else -> "linux"
        }
    }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

scala {
    scalaVersion = scalaLibraryVersion
}

val javaFxModules =
    listOf(
        "base",
        "graphics",
        "controls",
        "media",
    )

dependencies {
    implementation("org.scala-lang:scala-library:$scalaLibraryVersion")
    implementation("org.scalafx:scalafx_2.13:$scalaFxVersion")

    javaFxModules.forEach { module ->
        implementation(
            "org.openjfx:javafx-$module:$javaFxVersion:$javaFxPlatform",
        )
    }

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("io.codeswarm.boids.app.BoidApplication")
}

tasks.withType<ScalaCompile>().configureEach {
    scalaCompileOptions.additionalParameters =
        listOf(
            "-deprecation",
            "-feature",
            "-unchecked",
            "-Xlint",
        )
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<JavaExec>("runHeadless") {
    group = "application"
    description = "Runs a deterministic headless simulation smoke test."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("io.codeswarm.boids.app.HeadlessApplication")
    args("--boids", "250", "--steps", "500", "--seed", "42")
}

spotless {
    scala {
        target("src/**/*.scala")
        scalafmt("3.10.7").configFile(".scalafmt.conf")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

tasks.check {
    dependsOn("spotlessCheck")
}

tasks.wrapper {
    gradleVersion = "9.8.0"
    distributionType = Wrapper.DistributionType.BIN
}
