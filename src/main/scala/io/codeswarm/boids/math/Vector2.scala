package io.codeswarm.boids.math

/** Immutable two-dimensional vector used by the simulation core.
  *
  * Milestone 0.2 removes the general-purpose Breeze dependency from the simulation hot path. `Vector2` intentionally exposes only the operations required by
  * the boids model, keeping the domain model small and allocation behavior explicit.
  *
  * @param x
  *   horizontal component
  * @param y
  *   vertical component
  */
final case class Vector2(x: Double, y: Double) {

  /** Adds another vector component-wise. */
  def +(other: Vector2): Vector2 = Vector2(x + other.x, y + other.y)

  /** Subtracts another vector component-wise. */
  def -(other: Vector2): Vector2 = Vector2(x - other.x, y - other.y)

  /** Multiplies this vector by a scalar. */
  def *(scalar: Double): Vector2 = Vector2(x * scalar, y * scalar)

  /** Divides this vector by a non-zero scalar.
    *
    * @throws IllegalArgumentException
    *   when `scalar` equals zero
    */
  def /(scalar: Double): Vector2 = {
    require(scalar != 0.0, "scalar must be non-zero")
    Vector2(x / scalar, y / scalar)
  }

  /** Returns the squared Euclidean magnitude without computing a square root. */
  def magnitudeSquared: Double = x * x + y * y

  /** Returns the Euclidean magnitude. */
  def magnitude: Double = math.sqrt(magnitudeSquared)

  /** Returns a unit vector in the same direction or `Vector2.Zero` for a zero vector. */
  def normalized: Vector2 = {
    val length = magnitude
    if (length == 0.0) Vector2.Zero else this / length
  }

  /** Limits this vector to at most `maximum` magnitude while preserving direction.
    *
    * @throws IllegalArgumentException
    *   when `maximum` is negative
    */
  def limit(maximum: Double): Vector2 = {
    require(maximum >= 0.0, "maximum must be non-negative")
    if (magnitudeSquared > maximum * maximum && magnitudeSquared > 0.0) normalized * maximum else this
  }

  /** Returns the dot product with another vector. */
  def dot(other: Vector2): Double = x * other.x + y * other.y

  /** Returns the squared Euclidean distance to another vector. */
  def distanceSquaredTo(other: Vector2): Double = (this - other).magnitudeSquared

  /** Returns the Euclidean distance to another vector. */
  def distanceTo(other: Vector2): Double = math.sqrt(distanceSquaredTo(other))

  /** Returns true when both components are finite numbers. */
  def isFinite: Boolean = java.lang.Double.isFinite(x) && java.lang.Double.isFinite(y)

  /** Returns true when both components are exactly zero. */
  def isZero: Boolean = x == 0.0 && y == 0.0
}

/** Constructors and constants for [[Vector2]]. */
object Vector2 {

  /** Zero vector used for absent steering contributions. */
  val Zero: Vector2 = Vector2(0.0, 0.0)

  /** Creates a unit vector from an angle in radians. */
  def fromAngle(angle: Double): Vector2 = Vector2(math.cos(angle), math.sin(angle))
}
