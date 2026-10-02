package io.codeswarm.boids.model

/** Strongly typed identifier of a boid.
  *
  * The identifier lets neighbor-search implementations reliably exclude the query boid without depending on object
  * identity or complete value equality.
  *
  * @param value
  *   stable numeric identifier within one simulation
  */
final case class BoidId(value: Long) extends AnyVal
