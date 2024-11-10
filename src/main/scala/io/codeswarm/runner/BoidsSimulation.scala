package io.codeswarm.runner

import io.codeswarm.algorithm.BoidBehavior
import io.codeswarm.graphics.Vector2D
import io.codeswarm.model.Boid
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Polygon}

import scala.util.Random

object BoidsSimulation extends JFXApp {

  // Function to generate a random color
  def randomColor(): Color = {
    Color(
      Random.nextDouble(), // Red
      Random.nextDouble(), // Green
      Random.nextDouble(), // Blue
      1.0                   // Opacity
    )
  }

  // Initial boid setup
  private val initialBoids: Seq[Boid] = (1 to 250).map { _ =>
    Boid(
      Vector2D(Random.nextDouble() * 1280, Random.nextDouble() * 960),
      Vector2D(Random.nextDouble() * 2 - 1, Random.nextDouble() * 2 - 1)
    )
  }

  // Function to update boids with target attraction to the cursor
  private def updateBoids(boids: Seq[Boid], target: Vector2D): Seq[Boid] = {
    boids.map { boid =>
      val behavior = new BoidBehavior(boid)
      behavior.update(boids, target)
    }
  }

  stage = new JFXApp.PrimaryStage {
    title = "Boids Simulation"
    scene = new Scene(1280, 960) {
      fill = Color.Black

      // Create triangle shapes for boids
      val boidShapes: Seq[Polygon] = initialBoids.map { _ =>
        new Polygon {
          points.addAll(
            0.0, -10.0,  // Top point of the triangle
            -5.0, 5.0,   // Bottom left point of the triangle
            5.0, 5.0     // Bottom right point of the triangle
          )
          fill = randomColor()
        }
      }

      content = boidShapes

      // Variable to hold the current cursor position
      var cursorPosition: Vector2D = Vector2D(640, 480)

      // Update cursor position on mouse move
      onMouseMoved = (event) => {
        cursorPosition = Vector2D(event.getX, event.getY)
      }

      // Animation loop
      var boids: Seq[Boid] = initialBoids
      val timer: AnimationTimer = AnimationTimer { _ =>
        // Update boids with the current cursor position as the target
        boids = updateBoids(boids, cursorPosition)

        // Update each shape position and rotation based on the new boid data
        boidShapes.zip(boids).foreach { case (shape, boid) =>
          shape.layoutX = boid.position.x
          shape.layoutY = boid.position.y

          // Rotate shape based on velocity direction
          val angle = Math.toDegrees(math.atan2(boid.velocity.y, boid.velocity.x)) + 90
          shape.rotate = angle
        }
      }
      timer.start()
    }
  }
}