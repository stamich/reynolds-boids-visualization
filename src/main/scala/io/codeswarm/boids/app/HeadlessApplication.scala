package io.codeswarm.boids.app

import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.neighbor.NeighborSearchStrategy
import io.codeswarm.boids.simulation.{RandomSimulationInitializer, SimulationComponents, SimulationRunner}

/** Command-line entry point that executes the simulation without JavaFX rendering. */
object HeadlessApplication {

  /** Parses CLI options, executes ticks and prints reproducible summary metrics. */
  def main(args: Array[String]): Unit = {
    val options = Arguments.parse(args.toList)
    val base = SimulationConfig.Default
    val config = base.copy(flock = base.flock.copy(boidCount = options.boidCount), seed = options.seed)
    val initializer = new RandomSimulationInitializer
    val strategy = NeighborSearchStrategy.parse(options.neighborStrategy)
    val runner = new SimulationRunner(SimulationComponents.engine(config, strategy))
    val finalState = runner.run(initializer.initialize(config), options.steps)
    val averageSpeed = finalState.boids.map(_.velocity.magnitude).sum / finalState.boids.size.toDouble
    println(s"tick=${finalState.tick}")
    println(s"boids=${finalState.boids.size}")
    println(s"neighborSearch=${strategy.name}")
    println(f"averageSpeed=$averageSpeed%.6f")
    println(s"finite=${finalState.boids.forall(b => b.position.isFinite && b.velocity.isFinite)}")
  }

  /** Parsed command-line options. */
  private final case class Arguments(
      boidCount: Int = 200,
      steps: Int = 1000,
      seed: Long = 42L,
      neighborStrategy: String = NeighborSearchStrategy.UniformGrid.name
  )

  /** Minimal dependency-free parser for the headless surface. */
  private object Arguments {

    /** Parses supported command-line arguments. */
    def parse(args: List[String]): Arguments = {
      @annotation.tailrec
      def loop(remaining: List[String], current: Arguments): Arguments = remaining match {
        case Nil => current
        case "--boids" :: value :: tail => loop(tail, current.copy(boidCount = positiveInt("boids", value)))
        case "--steps" :: value :: tail => loop(tail, current.copy(steps = nonNegativeInt("steps", value)))
        case "--seed" :: value :: tail => loop(tail, current.copy(seed = value.toLong))
        case "--neighbor" :: value :: tail => loop(tail, current.copy(neighborStrategy = value))
        case unknown :: _ => throw new IllegalArgumentException(s"unknown or incomplete argument: $unknown")
      }
      loop(args, Arguments())
    }

    /** Parses a strictly positive integer option. */
    private def positiveInt(name: String, value: String): Int = { val parsed = value.toInt; require(parsed > 0, s"$name must be positive"); parsed }

    /** Parses a non-negative integer option. */
    private def nonNegativeInt(name: String, value: String): Int = { val parsed = value.toInt; require(parsed >= 0, s"$name must be non-negative"); parsed }
  }
}
