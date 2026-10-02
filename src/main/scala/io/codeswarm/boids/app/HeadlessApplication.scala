package io.codeswarm.boids.app

import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.simulation.{RandomSimulationInitializer, SimulationComponents, SimulationRunner}

/** Command-line entry point that executes the simulation without JavaFX rendering. */
object HeadlessApplication {

  /** Parses a deliberately small CLI, executes the requested number of ticks and prints reproducible summary metrics.
    */
  def main(args: Array[String]): Unit = {
    val options = Arguments.parse(args.toList)
    val base = SimulationConfig.Default
    val config = base.copy(
      flock = base.flock.copy(boidCount = options.boidCount),
      seed = options.seed
    )

    val initializer = new RandomSimulationInitializer
    val runner = new SimulationRunner(SimulationComponents.engine(config))
    val finalState = runner.run(initializer.initialize(config), options.steps)

    val averageSpeed = finalState.boids.map(_.velocity.magnitude).sum / finalState.boids.size.toDouble
    println(s"tick=${finalState.tick}")
    println(s"boids=${finalState.boids.size}")
    println(f"averageSpeed=$averageSpeed%.6f")
    println(s"finite=${finalState.boids.forall(b => b.position.isFinite && b.velocity.isFinite)}")
  }

  /** Parsed command-line options. */
  private final case class Arguments(boidCount: Int = 200, steps: Int = 1000, seed: Long = 42L)

  /** Minimal parser kept dependency-free for the small milestone-0.2 headless surface. */
  private object Arguments {
    def parse(args: List[String]): Arguments = {
      @annotation.tailrec
      def loop(remaining: List[String], current: Arguments): Arguments = remaining match {
        case Nil                        => current
        case "--boids" :: value :: tail => loop(tail, current.copy(boidCount = positiveInt("boids", value)))
        case "--steps" :: value :: tail => loop(tail, current.copy(steps = nonNegativeInt("steps", value)))
        case "--seed" :: value :: tail  => loop(tail, current.copy(seed = value.toLong))
        case unknown :: _ => throw new IllegalArgumentException(s"unknown or incomplete argument: $unknown")
      }
      loop(args, Arguments())
    }

    private def positiveInt(name: String, value: String): Int = {
      val parsed = value.toInt
      require(parsed > 0, s"$name must be positive")
      parsed
    }

    private def nonNegativeInt(name: String, value: String): Int = {
      val parsed = value.toInt
      require(parsed >= 0, s"$name must be non-negative")
      parsed
    }
  }
}
