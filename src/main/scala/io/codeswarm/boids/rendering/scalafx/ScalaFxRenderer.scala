package io.codeswarm.boids.rendering.scalafx

import io.codeswarm.boids.model.{RenderConfig, SimulationState, WorldConfig}
import io.codeswarm.boids.rendering.Renderer
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/** ScalaFX canvas renderer for the immutable simulation model.
  *
  * This class contains no simulation rules and never mutates `SimulationState`.
  *
  * @param graphics
  *   target canvas graphics context
  * @param world
  *   world dimensions used to clear the canvas
  * @param config
  *   rendering-only configuration
  */
final class ScalaFxRenderer(graphics: GraphicsContext, world: WorldConfig, config: RenderConfig) extends Renderer {

  /** Clears the canvas and draws every boid as an oriented triangle. */
  override def render(state: SimulationState): Unit = {
    graphics.fill = Color.rgb(16, 20, 28)
    graphics.fillRect(0.0, 0.0, world.width, world.height)
    graphics.fill = Color.rgb(126, 211, 255)

    state.boids.foreach { boid =>
      val angle = math.atan2(boid.velocity.y, boid.velocity.x)
      val size = config.boidSize
      val x = boid.position.x
      val y = boid.position.y

      val noseX = x + math.cos(angle) * size
      val noseY = y + math.sin(angle) * size
      val leftX = x + math.cos(angle + 2.5) * size
      val leftY = y + math.sin(angle + 2.5) * size
      val rightX = x + math.cos(angle - 2.5) * size
      val rightY = y + math.sin(angle - 2.5) * size

      graphics.fillPolygon(Array(noseX, leftX, rightX), Array(noseY, leftY, rightY), 3)
    }
  }
}
