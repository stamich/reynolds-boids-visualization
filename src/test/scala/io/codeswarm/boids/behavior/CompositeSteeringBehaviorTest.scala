package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Tests weighted behavior composition independently of the simulation engine. */
final class CompositeSteeringBehaviorTest {

  private final class ConstantBehavior(value: Vector2) extends SteeringBehavior {
    override def force(boid: Boid, neighbors: IndexedSeq[Boid], context: SteeringContext): Vector2 = value
  }

  @Test
  def weightsAreAppliedAndSummed(): Unit = {
    val composite = new CompositeSteeringBehavior(
      Vector(
        WeightedBehavior(new ConstantBehavior(Vector2(1.0, 0.0)), 2.0),
        WeightedBehavior(new ConstantBehavior(Vector2(0.0, 1.0)), 3.0)
      )
    )
    val boid = Boid(BoidId(1), Vector2.Zero, Vector2.Zero)
    val result = composite.force(boid, Vector.empty, SteeringContext(4.0, 1.0))
    assertEquals(Vector2(2.0, 3.0), result)
  }
}
