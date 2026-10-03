package io.codeswarm.boids.ui

/** Application-level lifecycle of the desktop simulation. */
sealed trait SimulationStatus

/** Lifecycle states used by the Start/Pause/Restart controls. */
object SimulationStatus {
  case object Ready extends SimulationStatus
  case object Running extends SimulationStatus
  case object Paused extends SimulationStatus
}
