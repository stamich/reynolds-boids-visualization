package io.codeswarm.boids.simulation

import breeze.linalg.DenseVector
import io.codeswarm.boids.model.{Boid, SimulationConfig}
import io.codeswarm.boids.util.VectorOperations

/** Implements the three classic local steering rules used by Craig Reynolds' boids model.
 *
 * The implementation intentionally performs a direct scan of all boids. This is the baseline O(n²) neighbor lookup
 * retained for milestone 0.1.1. Spatial partitioning and performance-oriented data structures belong to a later
 * milestone so their impact can be benchmarked against this reference implementation.
 */
object BoidBehavior {

  /** Computes short-range repulsion from nearby boids.
   *
   * Closer neighbors contribute more strongly than distant neighbors. The returned steering force is limited by the
   * configured maximum force.
   */
  def separation(boid: Boid, flock: IndexedSeq[Boid], config: SimulationConfig): DenseVector[Double] = {
    val contributions = flock.iterator
      .filterNot(_ eq boid)
      .flatMap { other =>
        val distance = VectorOperations.distance(boid.position, other.position)
        if (distance > 0.0 && distance < config.separationRadius) {
          Some(VectorOperations.normalized(boid.position - other.position) / distance)
        } else None
      }
      .toVector

    steerFromAverage(contributions, boid.velocity, config)
  }

  /** Computes steering toward the average velocity of visible neighbors. */
  def alignment(boid: Boid, flock: IndexedSeq[Boid], config: SimulationConfig): DenseVector[Double] = {
    val velocities = neighbors(boid, flock, config.perceptionRadius).map(_.velocity)
    steerFromAverage(velocities, boid.velocity, config)
  }

  /** Computes steering toward the average position (local center of mass) of visible neighbors. */
  def cohesion(boid: Boid, flock: IndexedSeq[Boid], config: SimulationConfig): DenseVector[Double] = {
    val visibleNeighbors = neighbors(boid, flock, config.perceptionRadius)
    if (visibleNeighbors.isEmpty) zero
    else {
      val center = average(visibleNeighbors.map(_.position))
      steerToward(boid, center, config)
    }
  }

  /** Combines separation, alignment and cohesion using the configured weights. */
  def flockingForce(boid: Boid, flock: IndexedSeq[Boid], config: SimulationConfig): DenseVector[Double] =
    separation(boid, flock, config) * config.separationWeight +
      alignment(boid, flock, config) * config.alignmentWeight +
      cohesion(boid, flock, config) * config.cohesionWeight

  private def neighbors(boid: Boid, flock: IndexedSeq[Boid], radius: Double): IndexedSeq[Boid] =
    flock.filter { other =>
      (other ne boid) && {
        val d = VectorOperations.distance(boid.position, other.position)
        d > 0.0 && d < radius
      }
    }

  private def steerToward(boid: Boid, target: DenseVector[Double], config: SimulationConfig): DenseVector[Double] = {
    val desiredDirection = target - boid.position
    if (VectorOperations.magnitude(desiredDirection) == 0.0) zero
    else {
      val desiredVelocity = VectorOperations.normalized(desiredDirection) * config.maxSpeed
      VectorOperations.limit(desiredVelocity - boid.velocity, config.maxForce)
    }
  }

  private def steerFromAverage(
                                vectors: IndexedSeq[DenseVector[Double]],
                                currentVelocity: DenseVector[Double],
                                config: SimulationConfig
                              ): DenseVector[Double] = {
    if (vectors.isEmpty) zero
    else {
      val mean = average(vectors)
      if (VectorOperations.magnitude(mean) == 0.0) zero
      else {
        val desiredVelocity = VectorOperations.normalized(mean) * config.maxSpeed
        VectorOperations.limit(desiredVelocity - currentVelocity, config.maxForce)
      }
    }
  }

  private def average(vectors: IndexedSeq[DenseVector[Double]]): DenseVector[Double] =
    vectors.foldLeft(zero)(_ + _) / vectors.size.toDouble

  private def zero: DenseVector[Double] = DenseVector.zeros[Double](2)
}
