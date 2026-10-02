package io.codeswarm.boids.simulation

import breeze.linalg.DenseVector
import io.codeswarm.boids.model.{Boid, SimulationConfig}
import io.codeswarm.boids.util.VectorOperations
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Regression tests for one-frame simulation updates. */
final class BoidSimulationStepSpec extends AnyFunSuite with Matchers {

  private val config = SimulationConfig(width = 100.0, height = 80.0, boidCount = 2, maxSpeed = 4.0)

  test("wrap keeps coordinates inside the world") {
    BoidSimulationStep.wrap(DenseVector(101.0, -1.0), config) shouldBe DenseVector(1.0, 79.0)
  }

  test("next keeps velocity bounded and all values finite") {
    val flock = Vector(
      Boid(DenseVector(10.0, 10.0), DenseVector(8.0, 0.0)),
      Boid(DenseVector(20.0, 10.0), DenseVector(0.0, 8.0))
    )

    val next = BoidSimulationStep.next(flock, config)

    next.foreach { boid =>
      VectorOperations.magnitude(boid.velocity) should be <= config.maxSpeed
      VectorOperations.isFinite(boid.position) shouldBe true
      VectorOperations.isFinite(boid.velocity) shouldBe true
    }
  }

  test("a deterministic initial state remains valid for many steps") {
    val initial = Vector(
      Boid(DenseVector(10.0, 10.0), DenseVector(1.0, 0.0)),
      Boid(DenseVector(20.0, 20.0), DenseVector(0.0, 1.0)),
      Boid(DenseVector(30.0, 15.0), DenseVector(-1.0, 0.5))
    )

    val result = (1 to 100).foldLeft(initial: IndexedSeq[Boid]) { (state, _) =>
      BoidSimulationStep.next(state, config.copy(boidCount = state.size))
    }

    result.foreach { boid =>
      VectorOperations.isFinite(boid.position) shouldBe true
      VectorOperations.isFinite(boid.velocity) shouldBe true
      VectorOperations.magnitude(boid.velocity) should be <= config.maxSpeed
      boid.position(0) should (be >= 0.0 and be < config.width)
      boid.position(1) should (be >= 0.0 and be < config.height)
    }
  }
}
