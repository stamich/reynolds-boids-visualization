package io.codeswarm.boids.util

import breeze.linalg.DenseVector
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Unit tests for numerical helpers used by the boids simulation. */
final class VectorOperationsSpec extends AnyFunSuite with Matchers {

  test("magnitude computes Euclidean length") {
    VectorOperations.magnitude(DenseVector(3.0, 4.0)) shouldBe 5.0 +- 1e-12
  }

  test("normalized returns a unit vector") {
    val result = VectorOperations.normalized(DenseVector(3.0, 4.0))
    result(0) shouldBe 0.6 +- 1e-12
    result(1) shouldBe 0.8 +- 1e-12
  }

  test("normalized leaves zero vector finite") {
    val result = VectorOperations.normalized(DenseVector(0.0, 0.0))
    result shouldBe DenseVector(0.0, 0.0)
    VectorOperations.isFinite(result) shouldBe true
  }

  test("limit caps vector magnitude") {
    VectorOperations.magnitude(VectorOperations.limit(DenseVector(6.0, 8.0), 5.0)) shouldBe 5.0 +- 1e-12
  }

  test("limit keeps vectors already inside the limit unchanged") {
    VectorOperations.limit(DenseVector(1.0, 2.0), 5.0) shouldBe DenseVector(1.0, 2.0)
  }

  test("distance computes Euclidean distance") {
    VectorOperations.distance(DenseVector(1.0, 1.0), DenseVector(4.0, 5.0)) shouldBe 5.0 +- 1e-12
  }
}
