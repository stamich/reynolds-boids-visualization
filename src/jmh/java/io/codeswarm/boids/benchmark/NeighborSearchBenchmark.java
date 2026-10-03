package io.codeswarm.boids.benchmark;

import io.codeswarm.boids.model.Boid;
import io.codeswarm.boids.model.SimulationConfig;
import io.codeswarm.boids.model.SimulationState;
import io.codeswarm.boids.neighbor.NaiveNeighborSearch;
import io.codeswarm.boids.neighbor.NeighborIndex;
import io.codeswarm.boids.neighbor.UniformGridNeighborSearch;

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
import scala.collection.Iterator;

/**
 * JMH comparison of prepared naive and uniform-grid neighborhood queries.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class NeighborSearchBenchmark {

    /**
     * Benchmark state reused for all query measurements within one trial.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"100", "1000", "5000"})
        public int boidCount;

        private SimulationConfig config;
        private SimulationState state;
        private NeighborIndex naiveIndex;
        private NeighborIndex gridIndex;

        /**
         * Builds one deterministic flock and both prepared indexes outside measurement iterations.
         */
        @Setup(Level.Trial)
        public void setup() {
            config = BenchmarkFixtures.config(boidCount);
            state = BenchmarkFixtures.state(config);
            naiveIndex = new NaiveNeighborSearch().index(state.boids(), config.world());
            gridIndex =
                    new UniformGridNeighborSearch(config.behavior().perceptionRadius())
                            .index(state.boids(), config.world());
        }
    }

    /**
     * Queries every boid using the O(n) per-query reference scan.
     */
    @Benchmark
    public int naiveQueries(BenchmarkState benchmarkState) {
        return countNeighbors(benchmarkState.state, benchmarkState.naiveIndex, benchmarkState.config);
    }

    /**
     * Queries every boid using the uniform-grid candidate set.
     */
    @Benchmark
    public int uniformGridQueries(BenchmarkState benchmarkState) {
        return countNeighbors(benchmarkState.state, benchmarkState.gridIndex, benchmarkState.config);
    }

    /**
     * Executes the complete prepared-index query workload and returns a consumed aggregate count.
     */
    private static int countNeighbors(
            SimulationState state, NeighborIndex index, SimulationConfig config) {
        int total = 0;
        Iterator<Boid> iterator = state.boids().iterator();
        while (iterator.hasNext()) {
            total += index.neighborsOf(iterator.next(), config.behavior().perceptionRadius()).size();
        }
        return total;
    }
}
