package io.codeswarm.algorithm

import io.codeswarm.graphics.Vector2D
import io.codeswarm.model.Boid

class BoidBehavior(boid: Boid) {

  // Main function to apply all behaviors, with an added target position (cursor)
  def update(boids: Seq[Boid], target: Vector2D): Boid = {
    val acceleration = applyBehaviors(boids, target)
    val newVelocity = (boid.velocity + acceleration).normalize * boid.maxSpeed
    val newPosition = boid.position + newVelocity
    boid.copy(position = newPosition, velocity = newVelocity)
  }

  // Apply alignment, cohesion, separation, and target following
  private def applyBehaviors(boids: Seq[Boid], target: Vector2D): Vector2D = {
    val alignment = align(boids) * 1.0
    val cohesion = cohere(boids) * 1.0
    val separation = separate(boids) * 1.5
    val targetAttraction = steerToward(target) * 0.8
    alignment + cohesion + separation + targetAttraction
  }

  // Target attraction behavior: steer towards a specified target (e.g., cursor)
  private def steerToward(target: Vector2D): Vector2D = {
    val desired = (target - boid.position).normalize * boid.maxSpeed
    limitForce(desired - boid.velocity)
  }

  // Alignment behavior: steer towards average velocity of nearby boids
  def align(boids: Seq[Boid]): Vector2D = {
    val (steering, total) = boids.foldLeft((Vector2D(0, 0), 0)) {
      case ((sum, count), other) if other != boid && distance(other) < boid.perceptionRadius =>
        (sum + other.velocity, count + 1)
      case (acc, _) => acc
    }
    checkVector(total, steering)
  }

  // Cohesion behavior: steer towards average position of nearby boids
  def cohere(boids: Seq[Boid]): Vector2D = {
    val (steering, total) = boids.foldLeft((Vector2D(0, 0), 0)) {
      case ((sum, count), other) if other != boid && distance(other) < boid.perceptionRadius =>
        (sum + other.position, count + 1)
      case (acc, _) => acc
    }
    if (total > 0) limitForce(((steering / total) - boid.position).normalize * boid.maxSpeed - boid.velocity)
    else Vector2D(0, 0)
  }

  // Separation behavior: steer away from nearby boids to avoid crowding
  def separate(boids: Seq[Boid]): Vector2D = {
    val (steering, total) = boids.foldLeft((Vector2D(0, 0), 0)) {
      case ((sum, count), other) if other != boid && distance(other) < boid.perceptionRadius =>
        (sum + (boid.position - other.position) / distance(other), count + 1)
      case (acc, _) => acc
    }
    checkVector(total, steering)
  }

  // Helper function to calculate distance between two boids
  def distance(other: Boid): Double = {
    math.sqrt(math.pow(boid.position.x - other.position.x, 2) + math.pow(boid.position.y - other.position.y, 2))
  }

  // Limit the force to boid's maxForce
  private def limitForce(force: Vector2D): Vector2D = {
    if (force.magnitude > boid.maxForce) force.normalize * boid.maxForce else force
  }

  private def checkVector(total: Int, steering: Vector2D): Vector2D = {
    if (total > 0) limitForce((steering / total).normalize * boid.maxSpeed - boid.velocity)
    else Vector2D(0, 0)
  }
}
