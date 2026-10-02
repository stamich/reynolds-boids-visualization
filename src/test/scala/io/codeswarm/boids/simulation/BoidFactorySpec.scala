package io.codeswarm.boids.simulation

import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.util.VectorOperations
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

/** Tests reproducible creation of the initial flock. */
final class BoidFactorySpec extends AnyFunSuite with Matchers {

  test("factory creates requested number of boids inside bounds") {
    val config = SimulationConfig(width = 100.0, height = 80.0, boidCount = 20)
    val flock = BoidFactory.randomFlock(config, new Random(42L))

    flock should have size 20
    flock.foreach { boid =>
      boid.position(0) should (be >= 0.0 and be < config.width)
      boid.position(1) should (be >= 0.0 and be < config.height)
      VectorOperations.magnitude(boid.velocity) should be <= config.maxSpeed
    }
  }

  test("factory is reproducible when given the same seed") {
    val config = SimulationConfig(boidCount = 5)
    BoidFactory.randomFlock(config, new Random(7L)) shouldBe BoidFactory.randomFlock(config, new Random(7L))
  }
}
