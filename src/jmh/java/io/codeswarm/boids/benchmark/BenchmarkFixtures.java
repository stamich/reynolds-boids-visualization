package io.codeswarm.boids.benchmark;

import io.codeswarm.boids.model.BehaviorConfig;
import io.codeswarm.boids.model.DiagnosticsConfig;
import io.codeswarm.boids.model.FlockConfig;
import io.codeswarm.boids.model.RenderConfig;
import io.codeswarm.boids.model.SimulationConfig;
import io.codeswarm.boids.model.SimulationState;
import io.codeswarm.boids.model.WorldConfig;
import io.codeswarm.boids.simulation.RandomSimulationInitializer;

/** Shared deterministic fixtures used by milestone-0.4 JMH benchmarks. */
final class BenchmarkFixtures {
  private BenchmarkFixtures() {}

  /** Creates the standard fixed-world configuration with diagnostics disabled. */
  static SimulationConfig config(int boidCount) {
    return config(boidCount, 1200.0, 800.0);
  }

  /** Creates a benchmark configuration for explicit world dimensions. */
  static SimulationConfig config(int boidCount, double width, double height) {
    return new SimulationConfig(
        new WorldConfig(width, height),
        new FlockConfig(boidCount, 4.0, 0.08),
        new BehaviorConfig(70.0, 25.0, 1.5, 1.0, 1.0),
        new RenderConfig(7.0, false),
        new DiagnosticsConfig(false),
        42L);
  }

  /** Scales world area in direct proportion to boid count, preserving the 1000-boid baseline density. */
  static SimulationConfig fixedDensityConfig(int boidCount) {
    double scale = Math.sqrt(boidCount / 1000.0);
    return config(boidCount, 1200.0 * scale, 800.0 * scale);
  }

  /** Creates deterministic tick zero for a benchmark configuration. */
  static SimulationState state(SimulationConfig config) {
    return new RandomSimulationInitializer().initialize(config);
  }
}
