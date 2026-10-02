package io.codeswarm.boids.app

import io.codeswarm.boids.model.{Boid, SimulationConfig}
import io.codeswarm.boids.simulation.{BoidFactory, BoidSimulationStep}
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color

/** ScalaFX entry point for the Reynolds boids visualization.
  *
  * Milestone 0.1.1 keeps the renderer deliberately compact and close to the original prototype. Rendering and
  * simulation will be separated behind explicit interfaces in milestone 0.2.
  */
object BoidSimulation extends JFXApp3 {

  private val config = SimulationConfig.Default
  private var flock: IndexedSeq[Boid] = IndexedSeq.empty

  /** Creates the application window and starts the animation timer. */
  override def start(): Unit = {
    flock = BoidFactory.randomFlock(config)

    val canvas = new Canvas(config.width, config.height)
    val graphics = canvas.graphicsContext2D

    stage = new JFXApp3.PrimaryStage {
      title = "Reynolds Boids Visualization 0.1.1"
      scene = new Scene(config.width, config.height) {
        content = canvas
      }
    }

    val timer = AnimationTimer { _ =>
      flock = BoidSimulationStep.next(flock, config)
      render(graphics, flock)
    }
    timer.start()
  }

  /** Draws the current flock as oriented triangles. */
  private def render(graphics: scalafx.scene.canvas.GraphicsContext, boids: IndexedSeq[Boid]): Unit = {
    graphics.fill = Color.rgb(16, 20, 28)
    graphics.fillRect(0.0, 0.0, config.width, config.height)
    graphics.fill = Color.rgb(126, 211, 255)

    boids.foreach { boid =>
      val x = boid.position(0)
      val y = boid.position(1)
      val angle = math.atan2(boid.velocity(1), boid.velocity(0))
      val size = config.boidSize

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
