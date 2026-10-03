package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Focused tests for the classic Reynolds steering components. */
final class BehaviorTest {
  private val context = SteeringContext(maxSpeed = 4.0, maxForce = 0.5)

  @Test
  def separationPointsAwayFromCloseNeighbor(): Unit = {
    val boid = Boid(BoidId(1), Vector2(0.0, 0.0), Vector2.Zero)
    val other = Boid(BoidId(2), Vector2(1.0, 0.0), Vector2.Zero)
    val force = new Separation(10.0).force(boid, Vector(other), context)
    assertTrue(force.x < 0.0)
    assertEquals(0.0, force.y, 1e-12)
  }

  @Test
  def alignmentSteersTowardAverageVelocity(): Unit = {
    val boid = Boid(BoidId(1), Vector2.Zero, Vector2(0.0, 1.0))
    val neighbors = Vector(
      Boid(BoidId(2), Vector2(1.0, 0.0), Vector2(2.0, 0.0)),
      Boid(BoidId(3), Vector2(2.0, 0.0), Vector2(2.0, 0.0))
    )
    val force = new Alignment().force(boid, neighbors, context)
    assertTrue(force.x > 0.0)
  }

  @Test
  def cohesionSteersTowardCenterOfMass(): Unit = {
    val boid = Boid(BoidId(1), Vector2.Zero, Vector2.Zero)
    val neighbors = Vector(
      Boid(BoidId(2), Vector2(10.0, 0.0), Vector2.Zero),
      Boid(BoidId(3), Vector2(20.0, 0.0), Vector2.Zero)
    )
    val force = new Cohesion().force(boid, neighbors, context)
    assertTrue(force.x > 0.0)
    assertEquals(0.0, force.y, 1e-12)
  }

  @Test
  def noNeighborsProduceZeroForAlignmentAndCohesion(): Unit = {
    val boid = Boid(BoidId(1), Vector2.Zero, Vector2.Zero)
    assertEquals(Vector2.Zero, new Alignment().force(boid, Vector.empty, context))
    assertEquals(Vector2.Zero, new Cohesion().force(boid, Vector.empty, context))
  }
}
