package io.codeswarm.boids.neighbor

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId, FlockConfig, SimulationConfig}
import io.codeswarm.boids.simulation.RandomSimulationInitializer
import org.junit.jupiter.api.Assertions.{assertEquals, assertThrows}
import org.junit.jupiter.api.Test

/** Correctness tests for the milestone-0.3 uniform-grid spatial index. */
final class UniformGridNeighborSearchTest {

  /** Verifies that grid queries produce exactly the same neighbors as the naive reference implementation. */
  @Test
  def matchesNaiveSearchForDeterministicFlock(): Unit = {
    val base = SimulationConfig.Default
    val config = base.copy(flock = FlockConfig(boidCount = 500, maxSpeed = 4.0, maxForce = 0.08), seed = 123L)
    val state = new RandomSimulationInitializer().initialize(config)
    val naive = new NaiveNeighborSearch().index(state.boids, config.world)
    val grid = new UniformGridNeighborSearch(config.behavior.perceptionRadius).index(state.boids, config.world)

    state.boids.foreach { boid =>
      val expected = naive.neighborsOf(boid, config.behavior.perceptionRadius).map(_.id).sortBy(_.value)
      val actual = grid.neighborsOf(boid, config.behavior.perceptionRadius).map(_.id).sortBy(_.value)
      assertEquals(expected, actual)
    }
  }

  /** Verifies queries spanning more than one cell in every direction. */
  @Test
  def supportsRadiusLargerThanCellSize(): Unit = {
    val world = baseWorld
    val query = Boid(BoidId(0L), Vector2(50.0, 50.0), Vector2.Zero)
    val near = Boid(BoidId(1L), Vector2(72.0, 50.0), Vector2.Zero)
    val outside = Boid(BoidId(2L), Vector2(80.0, 50.0), Vector2.Zero)
    val index = new UniformGridNeighborSearch(10.0).index(Vector(query, near, outside), world)

    assertEquals(Vector(near), index.neighborsOf(query, 25.0))
  }

  /** Rejects invalid cell sizes at construction time. */
  @Test
  def rejectsInvalidCellSize(): Unit = {
    assertThrows(classOf[IllegalArgumentException], () => new UniformGridNeighborSearch(0.0))
  }

  private def baseWorld = SimulationConfig.Default.world
}
