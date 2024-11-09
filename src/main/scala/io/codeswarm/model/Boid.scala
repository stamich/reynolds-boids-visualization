package io.codeswarm.model

import io.codeswarm.graphics.Vector2D

case class Boid(
                 position: Vector2D,
                 velocity: Vector2D,
                 maxSpeed: Double = 2.5,
                 maxForce: Double = 0.75,
                 perceptionRadius: Double = 200.0
               )
