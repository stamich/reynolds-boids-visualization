package io.codeswarm.boids.rendering.scalafx

import io.codeswarm.boids.model.WorldConfig
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

/** Draws uniform-grid cell boundaries for spatial-index diagnostics. */
final class GridOverlayRenderer(graphics: GraphicsContext) {

  /** Draws cell boundaries over the simulation world. */
  def render(world: WorldConfig, cellSize: Double): Unit = {
    if (cellSize > 0.0) {
      graphics.stroke = Color.rgb(255, 255, 255, 0.10)
      graphics.lineWidth = 1.0
      var x = 0.0
      while (x <= world.width) {
        graphics.strokeLine(x, 0.0, x, world.height)
        x += cellSize
      }
      var y = 0.0
      while (y <= world.height) {
        graphics.strokeLine(0.0, y, world.width, y)
        y += cellSize
      }
    }
  }
}
