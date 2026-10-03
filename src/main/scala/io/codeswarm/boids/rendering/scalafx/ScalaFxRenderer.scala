package io.codeswarm.boids.rendering.scalafx

import io.codeswarm.boids.model.{RenderConfig, SimulationState, WorldConfig}
import io.codeswarm.boids.rendering.Renderer
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/** ScalaFX canvas renderer for immutable simulation snapshots.
  *
  * @param graphics
  *   target canvas graphics context
  * @param world
  *   world dimensions used to clear the canvas
  * @param colorStrategy
  *   strategy used to color individual boids
  */
final class ScalaFxRenderer(
    graphics: GraphicsContext,
    world: WorldConfig,
    colorStrategy: BoidColorStrategy = new IdBasedColorStrategy
) extends Renderer {
  private val gridOverlay = new GridOverlayRenderer(graphics)
  private var renderConfig = RenderConfig()
  private var gridCellSize = 70.0

  /** Updates renderer-only settings without modifying simulation state. */
  def updateConfig(config: RenderConfig, cellSize: Double): Unit = {
    renderConfig = config
    gridCellSize = cellSize
  }

  /** Clears the canvas and draws every boid as a colored oriented triangle. */
  override def render(state: SimulationState): Unit = {
    graphics.fill = Color.rgb(16, 20, 28)
    graphics.fillRect(0.0, 0.0, world.width, world.height)
    if (renderConfig.showGrid) gridOverlay.render(world, gridCellSize)

    state.boids.foreach { boid =>
      val angle = math.atan2(boid.velocity.y, boid.velocity.x)
      val size = renderConfig.boidSize
      val x = boid.position.x
      val y = boid.position.y
      val noseX = x + math.cos(angle) * size
      val noseY = y + math.sin(angle) * size
      val leftX = x + math.cos(angle + 2.5) * size
      val leftY = y + math.sin(angle + 2.5) * size
      val rightX = x + math.cos(angle - 2.5) * size
      val rightY = y + math.sin(angle - 2.5) * size
      graphics.fill = colorStrategy.colorFor(boid)
      graphics.fillPolygon(Array(noseX, leftX, rightX), Array(noseY, leftY, rightY), 3)
    }
  }
}
