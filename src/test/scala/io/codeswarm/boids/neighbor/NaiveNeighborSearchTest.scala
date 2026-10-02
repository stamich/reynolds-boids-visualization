package io.codeswarm.boids.neighbor

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Correctness tests for the reference O(n²) neighbor lookup. */
final class NaiveNeighborSearchTest {

  @Test def findsOnlyNeighborsInsideRadiusAndExcludesSelf(): Unit = {
    val query = Boid(BoidId(1), Vector2.Zero, Vector2.Zero)
    val inside = Boid(BoidId(2), Vector2(3.0, 4.0), Vector2.Zero)
    val outside = Boid(BoidId(3), Vector2(30.0, 40.0), Vector2.Zero)
    val result = new NaiveNeighborSearch().neighborsOf(query, Vector(query, inside, outside), 10.0)
    assertEquals(Vector(inside), result)
  }
}
