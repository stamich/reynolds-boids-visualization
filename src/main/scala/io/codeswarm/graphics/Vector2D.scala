package io.codeswarm.graphics

case class Vector2D(x: Double, y: Double) {
  def +(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)
  def -(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)
  def *(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)
  def /(scalar: Double): Vector2D = Vector2D(x / scalar, y / scalar)
  def magnitude: Double = math.sqrt(x * x + y * y)
  def normalize: Vector2D = if (magnitude > 0) this / magnitude else this
}
