package io.codeswarm.boids.neighbor

/** Selects the neighborhood implementation used by a simulation engine. */
sealed trait NeighborSearchStrategy {

  /** Stable command-line/configuration name. */
  def name: String
}

/** Supported milestone-0.3 neighborhood strategies. */
object NeighborSearchStrategy {

  /** Full-flock linear scan retained as the correctness baseline. */
  case object Naive extends NeighborSearchStrategy {
    override val name: String = "naive"
  }

  /** Uniform-grid spatial partitioning used as the milestone-0.3 default. */
  case object UniformGrid extends NeighborSearchStrategy {
    override val name: String = "grid"
  }

  /** Parses a stable command-line strategy name.
    *
    * @throws IllegalArgumentException
    *   when `value` does not identify a supported strategy
    */
  def parse(value: String): NeighborSearchStrategy =
    value.trim.toLowerCase match {
      case "naive" => Naive
      case "grid" => UniformGrid
      case other => throw new IllegalArgumentException(s"unsupported neighbor search strategy: $other")
    }
}
