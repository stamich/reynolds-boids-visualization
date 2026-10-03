import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.scala.ScalaCompile
import org.gradle.api.tasks.wrapper.Wrapper

plugins {
    scala
    application
    id("com.diffplug.spotless") version "8.10.3"
    id("me.champeau.jmh") version "0.7.3"
}

group = "io.codeswarm"
version = "0.3.0"

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

val javaFxModules = listOf("base", "graphics", "controls", "media")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

scala {
    scalaVersion = scalaLibraryVersion
}

dependencies {
    implementation("org.scala-lang:scala-library:$scalaLibraryVersion")
    implementation("org.scalafx:scalafx_2.13:$scalaFxVersion")

    javaFxModules.forEach { module ->
        implementation("org.openjfx:javafx-$module:$javaFxVersion:$javaFxPlatform")
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
    description = "Runs a deterministic headless simulation smoke test with the uniform-grid neighbor search."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("io.codeswarm.boids.app.HeadlessApplication")
    args("--boids", "250", "--steps", "500", "--seed", "42", "--neighbor", "grid")
}

tasks.register<JavaExec>("runHeadlessNaive") {
    group = "application"
    description = "Runs the same deterministic headless smoke scenario with the naive neighbor search."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("io.codeswarm.boids.app.HeadlessApplication")
    args("--boids", "250", "--steps", "500", "--seed", "42", "--neighbor", "naive")
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

jmh {
    jmhVersion = "1.37"
    warmupIterations = 3
    iterations = 5
    fork = 2
    timeOnIteration = "1s"
    warmup = "1s"
    resultFormat = "JSON"
    resultsFile = file("benchmark/results/jmh-0.3.0.json")
    duplicateClassesStrategy = DuplicatesStrategy.WARN
}

tasks.named<Jar>("jmhJar") {
    exclude("module-info.class")
    exclude("META-INF/versions/**/module-info.class")
}

tasks.register("benchmark") {
    group = "benchmark"
    description = "Runs the milestone 0.3 JMH benchmark suite."
    dependsOn("jmh")
}

tasks.check {
    dependsOn("spotlessCheck")
}

tasks.wrapper {
    gradleVersion = "9.8.0"
    distributionType = Wrapper.DistributionType.BIN
}
