package io.codeswarm.boids.neighbor

import org.junit.jupiter.api.Assertions.{assertEquals, assertThrows}
import org.junit.jupiter.api.Test

/** Tests stable strategy names used by headless execution and benchmarks. */
final class NeighborSearchStrategyTest {

  /** Parses both supported strategy names. */
  @Test
  def parsesSupportedStrategies(): Unit = {
    assertEquals(NeighborSearchStrategy.Naive, NeighborSearchStrategy.parse("naive"))
    assertEquals(NeighborSearchStrategy.UniformGrid, NeighborSearchStrategy.parse("GRID"))
  }

  /** Rejects unknown strategy names. */
  @Test
  def rejectsUnknownStrategy(): Unit =
    assertThrows(classOf[IllegalArgumentException], () => NeighborSearchStrategy.parse("quadtree"))
}
