package io.codeswarm.algorithm

import io.codeswarm.algorithm.BoidBehavior
import io.codeswarm.graphics.Vector2D
import io.codeswarm.model.Boid
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoidBehaviorTest extends AnyFunSuite with Matchers {

  test("update should update position and velocity correctly") {
    val boid = Boid(
      position = Vector2D(0, 0),
      velocity = Vector2D(1, 0),
      maxSpeed = 2.0,
      maxForce = 0.1,
      perceptionRadius = 50.0
    )
    val target = Vector2D(10, 10)
    val otherBoids = Seq(
      Boid(Vector2D(10, 10), Vector2D(1, 1), 2.0, 0.1, 50.0),
      Boid(Vector2D(-10, -10), Vector2D(-1, -1), 2.0, 0.1, 50.0)
    )

    val behavior = new BoidBehavior(boid)
    val updatedBoid = behavior.update(otherBoids, target)

    // Example assertion: Ensure the updated boid has moved towards the target
    updatedBoid.position.x should be > boid.position.x
    updatedBoid.position.y should be > boid.position.y
  }

  test("align should steer boid towards average velocity of nearby boids") {
    val boid = Boid(Vector2D(0, 0), Vector2D(1, 0), 2.0, 0.1, 50.0)
    val otherBoids = Seq(
      Boid(Vector2D(10, 10), Vector2D(1, 1), 2.0, 0.1, 50.0),
      Boid(Vector2D(-10, -10), Vector2D(-1, -1), 2.0, 0.1, 50.0)
    )

    val behavior = new BoidBehavior(boid)
    val alignment = behavior.align(otherBoids)

    // Example assertion: Check that alignment is non-zero
    alignment.magnitude should be > 0.0
  }

  test("cohere should steer boid towards average position of nearby boids") {
    val boid = Boid(Vector2D(0, 0), Vector2D(1, 0), 2.0, 0.1, 50.0)
    val otherBoids = Seq(
      Boid(Vector2D(10, 10), Vector2D(1, 1), 2.0, 0.1, 50.0),
      Boid(Vector2D(-10, -10), Vector2D(-1, -1), 2.0, 0.1, 50.0)
    )

    val behavior = new BoidBehavior(boid)
    val cohesion = behavior.cohere(otherBoids)

    // Example assertion: Cohesion should be non-zero
    cohesion.magnitude should be > 0.0
  }

  test("separate should steer boid away from nearby boids") {
    val boid = Boid(Vector2D(0, 0), Vector2D(1, 0), 2.0, 0.1, 50.0)
    val otherBoids = Seq(
      Boid(Vector2D(5, 5), Vector2D(1, 1), 2.0, 0.1, 50.0),
      Boid(Vector2D(-5, -5), Vector2D(-1, -1), 2.0, 0.1, 50.0)
    )

    val behavior = new BoidBehavior(boid)
    val separation = behavior.separate(otherBoids)

    // Example assertion: Separation should be non-zero
    separation.magnitude should be > 0.0
  }

  test("steerToward should produce a valid steering vector") {
    val boid = Boid(Vector2D(0, 0), Vector2D(1, 0), 2.0, 0.1, 50.0)
    val target = Vector2D(10, 10)

    val behavior = new BoidBehavior(boid)
    val steering = behavior.steerToward(target)

    // Example assertion: Steering should be non-zero and limited by maxForce
    steering.magnitude should be > 0.0
    steering.magnitude should be <= boid.maxForce
  }
}
