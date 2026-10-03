package io.codeswarm.boids.benchmark;

import io.codeswarm.boids.model.SimulationConfig;
import io.codeswarm.boids.model.SimulationState;
import io.codeswarm.boids.neighbor.NaiveNeighborSearch;
import io.codeswarm.boids.neighbor.UniformGridNeighborSearch;
import io.codeswarm.boids.simulation.SimulationComponents$;
import io.codeswarm.boids.simulation.SimulationEngine;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * End-to-end JMH comparison of one immutable simulation tick.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SimulationStepBenchmark {

    /**
     * Deterministic state shared by the two engine variants.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"100", "1000", "5000"})
        public int boidCount;

        private SimulationState state;
        private SimulationEngine naiveEngine;
        private SimulationEngine gridEngine;

        /**
         * Creates equal engines differing only in neighborhood strategy.
         */
        @Setup(Level.Trial)
        public void setup() {
            SimulationConfig config = BenchmarkFixtures.config(boidCount);
            state = BenchmarkFixtures.state(config);
            naiveEngine =
                    SimulationComponents$.MODULE$.engine(config, new NaiveNeighborSearch());
            gridEngine =
                    SimulationComponents$.MODULE$.engine(
                            config, new UniformGridNeighborSearch(config.behavior().perceptionRadius()));
        }
    }

    /**
     * Measures one simulation tick with the full-flock linear scan.
     */
    @Benchmark
    public SimulationState naiveStep(BenchmarkState benchmarkState) {
        return benchmarkState.naiveEngine.step(benchmarkState.state);
    }

    /**
     * Measures one simulation tick with a grid built once for the tick.
     */
    @Benchmark
    public SimulationState uniformGridStep(BenchmarkState benchmarkState) {
        return benchmarkState.gridEngine.step(benchmarkState.state);
    }
}
