package io.codeswarm.graphics

import scalafx.scene.paint.Color

import scala.util.Random

class ColorHandler {
  // Function to generate a random color
  def randomColor(): Color = {
    Color(
      Random.nextDouble(), // Red
      Random.nextDouble(), // Green
      Random.nextDouble(), // Blue
      1.0                  // Opacity
    )
  }
}
