package io.codeswarm.boids.simulation

import breeze.linalg.DenseVector
import io.codeswarm.boids.model.{Boid, SimulationConfig}
import io.codeswarm.boids.util.VectorOperations
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Tests the three Reynolds steering rules independently. */
final class BoidBehaviorSpec extends AnyFunSuite with Matchers {

  private val config = SimulationConfig(
    boidCount = 2,
    perceptionRadius = 100.0,
    separationRadius = 30.0,
    maxSpeed = 4.0,
    maxForce = 0.5
  )

  test("separation points away from a close neighbor") {
    val subject = Boid(DenseVector(10.0, 10.0), DenseVector(1.0, 0.0))
    val neighbor = Boid(DenseVector(20.0, 10.0), DenseVector(1.0, 0.0))

    val force = BoidBehavior.separation(subject, Vector(subject, neighbor), config)

    force(0) should be < 0.0
    VectorOperations.isFinite(force) shouldBe true
  }

  test("alignment steers toward neighbors' heading") {
    val subject = Boid(DenseVector(10.0, 10.0), DenseVector(1.0, 0.0))
    val neighbor = Boid(DenseVector(20.0, 10.0), DenseVector(0.0, 2.0))

    val force = BoidBehavior.alignment(subject, Vector(subject, neighbor), config)

    force(1) should be > 0.0
  }

  test("cohesion steers toward local center of mass") {
    val subject = Boid(DenseVector(10.0, 10.0), DenseVector(0.0, 1.0))
    val neighbor = Boid(DenseVector(40.0, 10.0), DenseVector(0.0, 1.0))

    val force = BoidBehavior.cohesion(subject, Vector(subject, neighbor), config)

    force(0) should be > 0.0
  }

  test("all rules return zero when there are no neighbors") {
    val subject = Boid(DenseVector(10.0, 10.0), DenseVector(1.0, 0.0))
    val flock = Vector(subject)

    BoidBehavior.separation(subject, flock, config) shouldBe DenseVector(0.0, 0.0)
    BoidBehavior.alignment(subject, flock, config) shouldBe DenseVector(0.0, 0.0)
    BoidBehavior.cohesion(subject, flock, config) shouldBe DenseVector(0.0, 0.0)
  }
}
