package io.codeswarm.algorithm

import io.codeswarm.graphics.Vector2D
import io.codeswarm.model.Boid
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoidBehaviorTest extends AnyFunSuite with Matchers {

  // Helper function to create a simple boid with default settings
  def createBoid(x: Double, y: Double, vx: Double, vy: Double): Boid =
    Boid(Vector2D(x, y), Vector2D(vx, vy))

  test("Boid alignment with neighbors should steer towards average velocity") {
    val boid = createBoid(50, 50, 1, 1)
    val neighbors = Seq(
      createBoid(60, 60, 2, 2),
      createBoid(40, 40, 2, 2)
    )
    val behavior = new BoidBehavior(boid)
    val alignmentForce = behavior.align(neighbors)

    alignmentForce.x should (be > 0.0 and be <= boid.maxSpeed)
    alignmentForce.y should (be > 0.0 and be <= boid.maxSpeed)
  }

  test("Boid cohesion with neighbors should steer towards average position") {
    val boid = createBoid(50, 50, 1, 1)
    val neighbors = Seq(
      createBoid(60, 60, 0, 0),
      createBoid(40, 40, 0, 0)
    )
    val behavior = new BoidBehavior(boid)
    val cohesionForce = behavior.cohere(neighbors)

    cohesionForce.x should (be > 0.0 and be <= boid.maxSpeed)
    cohesionForce.y should (be > 0.0 and be <= boid.maxSpeed)
  }

  test("Boid separation should steer away from close neighbors") {
    val boid = createBoid(50, 50, 1, 1)
    val closeNeighbors = Seq(
      createBoid(52, 52, 0, 0),
      createBoid(48, 48, 0, 0)
    )
    val behavior = new BoidBehavior(boid)
    val separationForce = behavior.separate(closeNeighbors)

    separationForce.x should (be < 0.0 or be <= boid.maxSpeed)
    separationForce.y should (be < 0.0 or be <= boid.maxSpeed)
  }

  test("Boid should update with combined behaviors") {
    val boid = createBoid(50, 50, 1, 1)
    val neighbors = Seq(
      createBoid(60, 60, 2, 2),
      createBoid(40, 40, 2, 2),
      createBoid(52, 52, 0, 0),
      createBoid(48, 48, 0, 0)
    )
    val behavior = new BoidBehavior(boid)
    val updatedBoid = behavior.update(neighbors)

    updatedBoid.position should not equal boid.position // Position should change
    updatedBoid.velocity should not equal boid.velocity // Velocity should change
    updatedBoid.position.x should be >= 0.0
    updatedBoid.position.y should be >= 0.0
  }

  test("Boid updates are immutable") {
    val boid = createBoid(50, 50, 1, 1)
    val neighbors = Seq(
      createBoid(60, 60, 2, 2),
      createBoid(40, 40, 2, 2)
    )
    val behavior = new BoidBehavior(boid)
    val updatedBoid = behavior.update(neighbors)

    // Ensure that the original boid has not changed
    boid.position should equal(Vector2D(50, 50))
    boid.velocity should equal(Vector2D(1, 1))
  }

  test("Boid distance calculation between two boids is correct") {
    val boid = createBoid(50, 50, 1, 1)
    val otherBoid = createBoid(60, 60, 0, 0)
    val behavior = new BoidBehavior(boid)

    val dist = behavior.distance(otherBoid)
    dist shouldBe math.sqrt(200)  // sqrt((60-50)^2 + (60-50)^2)
  }

  test("Alignment, cohesion, and separation forces are within maxForce limit") {
    val boid = createBoid(50, 50, 1, 1)
    val neighbors = Seq(
      createBoid(60, 60, 2, 2),
      createBoid(40, 40, 0, 0),
      createBoid(55, 55, 1, 1)
    )
    val behavior = new BoidBehavior(boid)

    val alignment = behavior.align(neighbors)
    val cohesion = behavior.cohere(neighbors)
    val separation = behavior.separate(neighbors)

    alignment.magnitude should be <= boid.maxForce
    cohesion.magnitude should be <= boid.maxForce
    separation.magnitude should be <= boid.maxForce
  }
}
