package io.codeswarm.graphics

import org.scalatest.funsuite.AnyFunSuite

class Vector2DTest extends AnyFunSuite {

  test("Addition of two vectors should produce the correct result") {
    val v1 = Vector2D(2.0, 3.0)
    val v2 = Vector2D(4.0, 1.0)
    assert(v1 + v2 == Vector2D(6.0, 4.0))
  }

  test("Subtraction of two vectors should produce the correct result") {
    val v1 = Vector2D(5.0, 7.0)
    val v2 = Vector2D(3.0, 2.0)
    assert(v1 - v2 == Vector2D(2.0, 5.0))
  }

  test("Scalar multiplication should produce the correct result") {
    val v = Vector2D(1.0, -2.0)
    val scalar = 3.0
    assert(v * scalar == Vector2D(3.0, -6.0))
  }

  test("Scalar division should produce the correct result") {
    val v = Vector2D(4.0, 8.0)
    val scalar = 2.0
    assert(v / scalar == Vector2D(2.0, 4.0))
  }

  test("Magnitude should calculate the correct length of the vector") {
    val v = Vector2D(3.0, 4.0)
    assert(v.magnitude == 5.0) // 3-4-5 triangle
  }

  test("Normalization should produce a vector of unit length") {
    val v = Vector2D(3.0, 4.0)
    val normalized = v.normalize
    assert(math.abs(normalized.magnitude - 1.0) < 1e-9, "Magnitude should be close to 1.0")
    assert(normalized == Vector2D(0.6, 0.8)) // Normalized form of (3, 4)
  }

  test("Normalization of a zero vector should return the zero vector") {
    val v = Vector2D(0.0, 0.0)
    assert(v.normalize == v)
  }
}
