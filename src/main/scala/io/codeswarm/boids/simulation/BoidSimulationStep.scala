package io.codeswarm.boids.simulation

import breeze.linalg.DenseVector
import io.codeswarm.boids.model.{Boid, SimulationConfig}
import io.codeswarm.boids.util.VectorOperations

/** Advances the baseline boids simulation by one frame.
  *
  * All steering forces are computed from the same input flock before any updated state is returned. This avoids a
  * frame-order dependency where boids processed later would otherwise observe already-updated neighbors.
  */
object BoidSimulationStep {

  /** Produces the next immutable flock state.
    *
    * @param flock
    *   current boids
    * @param config
    *   simulation parameters
    * @return
    *   updated boids after one simulation step
    */
  def next(flock: IndexedSeq[Boid], config: SimulationConfig): IndexedSeq[Boid] =
    flock.map { boid =>
      val acceleration = BoidBehavior.flockingForce(boid, flock, config)
      val nextVelocity = VectorOperations.limit(boid.velocity + acceleration, config.maxSpeed)
      val nextPosition = wrap(boid.position + nextVelocity, config)
      Boid(nextPosition, nextVelocity, DenseVector.zeros[Double](2))
    }

  /** Wraps a position around the rectangular world, producing toroidal screen boundaries. */
  def wrap(position: DenseVector[Double], config: SimulationConfig): DenseVector[Double] = {
    val x = wrapCoordinate(position(0), config.width)
    val y = wrapCoordinate(position(1), config.height)
    DenseVector(x, y)
  }

  private def wrapCoordinate(value: Double, upperBound: Double): Double = {
    val remainder = value % upperBound
    if (remainder < 0.0) remainder + upperBound else remainder
  }
}
