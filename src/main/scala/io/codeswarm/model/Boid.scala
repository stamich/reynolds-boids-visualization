package io.codeswarm.model

import io.codeswarm.algorithm.BoidBehavior
import io.codeswarm.graphics.Vector2D

case class Boid(
                 position: Vector2D,
                 velocity: Vector2D,
                 maxSpeed: Double = 2.5,
                 maxForce: Double = 0.75,
                 perceptionRadius: Double = 200.0
               ) {
  // Function to update boids with target attraction to the cursor
  def updateBoids(boids: Seq[Boid], target: Vector2D): Seq[Boid] = {
    boids.map { boid =>
      val behavior = new BoidBehavior(boid)
      behavior.update(boids, target)
    }
  }
}
