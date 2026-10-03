package io.codeswarm.boids.neighbor

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId, WorldConfig}
import org.junit.jupiter.api.Assertions.{assertEquals, assertFalse, assertTrue}
import org.junit.jupiter.api.Test

/** Unit tests for the linear-scan neighborhood baseline. */
final class NaiveNeighborSearchTest {

  private val world = WorldConfig(100.0, 100.0)

  /** Verifies strict-radius filtering and query-boid exclusion. */
  @Test
  def filtersByRadiusAndExcludesSelf(): Unit = {
    val query = Boid(BoidId(0L), Vector2(10.0, 10.0), Vector2.Zero)
    val near = Boid(BoidId(1L), Vector2(12.0, 10.0), Vector2.Zero)
    val far = Boid(BoidId(2L), Vector2(30.0, 10.0), Vector2.Zero)
    val index = new NaiveNeighborSearch().index(Vector(query, near, far), world)
    val neighbors = index.neighborsOf(query, 5.0)

    assertEquals(Vector(near), neighbors)
    assertFalse(neighbors.contains(query))
    assertTrue(neighbors.forall(_.id != query.id))
  }
}
