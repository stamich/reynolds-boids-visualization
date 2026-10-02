package io.codeswarm.boids.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Tests validation of simulation configuration. */
final class SimulationConfigSpec extends AnyFunSuite with Matchers {

  test("default configuration is valid") {
    noException should be thrownBy SimulationConfig.Default
  }

  test("configuration rejects a non-positive boid count") {
    an[IllegalArgumentException] should be thrownBy SimulationConfig(boidCount = 0)
  }

  test("configuration rejects non-positive dimensions") {
    an[IllegalArgumentException] should be thrownBy SimulationConfig(width = 0.0)
    an[IllegalArgumentException] should be thrownBy SimulationConfig(height = -1.0)
  }
}
