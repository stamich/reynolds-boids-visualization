package io.codeswarm.boids.model

import breeze.linalg.DenseVector

/** State of a single boid in the two-dimensional simulation.
 *
 * The state remains intentionally small in milestone 0.1.1: position, velocity and the accumulated acceleration for
 * the current frame. A future milestone may separate immutable simulation state from rendering state, but doing so
 * here would turn a stabilization release into an architectural rewrite.
 *
 * @param position
 * current x/y position
 * @param velocity
 * current x/y velocity
 * @param acceleration
 * accumulated x/y acceleration for the current simulation step
 */
final case class Boid(
                       position: DenseVector[Double],
                       velocity: DenseVector[Double],
                       acceleration: DenseVector[Double] = DenseVector.zeros[Double](2)
                     ) {
  require(position.length == 2, "position must be a two-dimensional vector")
  require(velocity.length == 2, "velocity must be a two-dimensional vector")
  require(acceleration.length == 2, "acceleration must be a two-dimensional vector")
}
