package io.codeswarm.boids.util

import breeze.linalg.{DenseVector, norm}

/** Small collection of safe vector operations used by the boids implementation.
  *
  * Breeze is intentionally retained in milestone 0.1.1 to avoid changing the baseline representation. Replacing it with
  * a dedicated lightweight `Vector2` type is reserved for milestone 0.2, where it can be measured and reviewed as an
  * explicit architectural change.
  */
object VectorOperations {

  /** Returns the Euclidean magnitude of a vector. */
  def magnitude(vector: DenseVector[Double]): Double = norm(vector)

  /** Returns a normalized copy of `vector` or a zero vector when its magnitude is zero. */
  def normalized(vector: DenseVector[Double]): DenseVector[Double] = {
    val currentMagnitude = magnitude(vector)
    if (currentMagnitude == 0.0) DenseVector.zeros[Double](vector.length)
    else vector / currentMagnitude
  }

  /** Limits the magnitude of `vector` to `maximum` while preserving its direction.
    *
    * @throws IllegalArgumentException
    *   when `maximum` is negative
    */
  def limit(vector: DenseVector[Double], maximum: Double): DenseVector[Double] = {
    require(maximum >= 0.0, "maximum must be non-negative")
    val currentMagnitude = magnitude(vector)
    if (currentMagnitude > maximum && currentMagnitude > 0.0) normalized(vector) * maximum else vector.copy
  }

  /** Computes Euclidean distance between two vectors. */
  def distance(left: DenseVector[Double], right: DenseVector[Double]): Double = magnitude(left - right)

  /** Returns true if all vector components are finite numbers. */
  def isFinite(vector: DenseVector[Double]): Boolean = vector.toArray.forall(java.lang.Double.isFinite)
}
