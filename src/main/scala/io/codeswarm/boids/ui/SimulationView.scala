package io.codeswarm.boids.ui

import io.codeswarm.boids.model.WorldConfig
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.StackPane

/** Canvas container for the simulation visualization. */
final class SimulationView(world: WorldConfig) extends StackPane {

  /** Canvas whose coordinate system matches simulation world coordinates. */
  val canvas: Canvas = new Canvas(world.width, world.height)
  children = Seq(canvas)
}
