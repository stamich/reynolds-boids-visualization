package io.codeswarm.boids.ui

import io.codeswarm.boids.model.SimulationConfig
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Tests lifecycle and configuration semantics of the desktop controller without starting JavaFX. */
final class SimulationControllerTest {

  @Test
  def startPauseResumePreservesState(): Unit = {
    val controller = new SimulationController(SimulationConfig.Desktop)
    assertEquals(SimulationStatus.Ready, controller.status)
    controller.start()
    controller.advance()
    val tickAfterStart = controller.state.tick
    assertEquals(1L, tickAfterStart)
    controller.pause()
    controller.advance()
    assertEquals(tickAfterStart, controller.state.tick)
    controller.start()
    controller.advance()
    assertEquals(tickAfterStart + 1L, controller.state.tick)
  }

  @Test
  def restartUsesCurrentSeedAndReturnsToTickZero(): Unit = {
    val controller = new SimulationController(SimulationConfig.Desktop)
    val original = controller.state
    controller.start()
    (1 to 20).foreach(_ => controller.advance())
    controller.restart()
    assertEquals(0L, controller.state.tick)
    assertEquals(original, controller.state)
    assertEquals(SimulationStatus.Ready, controller.status)
  }

  @Test
  def boidCountAndSeedAreRestartRequired(): Unit = {
    val controller = new SimulationController(SimulationConfig.Desktop)
    controller.updateBoidCount(321)
    controller.updateSeed(99L)
    assertTrue(controller.restartRequired)
    assertNotEquals(321, controller.state.boids.size)
    controller.restart()
    assertEquals(321, controller.state.boids.size)
    assertFalse(controller.restartRequired)
  }

  @Test
  def liveBehaviorUpdateDoesNotResetTick(): Unit = {
    val controller = new SimulationController(SimulationConfig.Desktop)
    controller.start()
    controller.advance()
    controller.updateAlignmentWeight(2.5)
    assertEquals(1L, controller.state.tick)
    assertEquals(2.5, controller.config.behavior.alignmentWeight, 1e-12)
    assertFalse(controller.restartRequired)
  }

  @Test
  def separationRadiusCannotExceedPerceptionRadius(): Unit = {
    val controller = new SimulationController(SimulationConfig.Desktop)
    controller.updatePerceptionRadius(20.0)
    assertTrue(controller.config.behavior.separationRadius <= 20.0)
    controller.updateSeparationRadius(100.0)
    assertEquals(20.0, controller.config.behavior.separationRadius, 1e-12)
  }
}
