package io.codeswarm.boids.benchmark;

import io.codeswarm.boids.model.BehaviorConfig;
import io.codeswarm.boids.model.FlockConfig;
import io.codeswarm.boids.model.RenderConfig;
import io.codeswarm.boids.model.SimulationConfig;
import io.codeswarm.boids.model.SimulationState;
import io.codeswarm.boids.model.WorldConfig;
import io.codeswarm.boids.simulation.RandomSimulationInitializer;

/**
 * Shared deterministic fixtures used by milestone-0.3 JMH benchmarks.
 */
final class BenchmarkFixtures {
    private BenchmarkFixtures() {
    }

    /**
     * Creates the standard desktop configuration with a benchmark-specific flock size.
     */
    static SimulationConfig config(int boidCount) {
        return new SimulationConfig(
                new WorldConfig(1200.0, 800.0),
                new FlockConfig(boidCount, 4.0, 0.08),
                new BehaviorConfig(70.0, 25.0, 1.5, 1.0, 1.0),
                new RenderConfig(7.0),
                42L);
    }

    /**
     * Creates the deterministic tick-zero state for a benchmark configuration.
     */
    static SimulationState state(SimulationConfig config) {
        return new RandomSimulationInitializer().initialize(config);
    }
}
