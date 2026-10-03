package io.codeswarm.boids.benchmark;

import io.codeswarm.boids.model.Boid;
import io.codeswarm.boids.model.SimulationConfig;
import io.codeswarm.boids.model.SimulationState;
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

/** Explores grid-cell sizes relative to perception radius for a dense 5000-boid world. */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CellSizeBenchmark {
  /** Trial state for one cell-size multiplier. */
  @State(Scope.Benchmark)
  public static class BenchmarkState {
    @Param({"0.5", "1.0", "1.5", "2.0"})
    public double cellSizeFactor;

    private SimulationConfig config;
    private SimulationState state;
    private NeighborIndex index;

    /** Builds one prepared grid outside measured iterations. */
    @Setup(Level.Trial)
    public void setup() {
      config = BenchmarkFixtures.config(5000);
      state = BenchmarkFixtures.state(config);
      double cellSize = config.behavior().perceptionRadius() * cellSizeFactor;
      index = new UniformGridNeighborSearch(cellSize).index(state.boids(), config.world());
    }
  }

  /** Queries every boid using the selected grid cell size. */
  @Benchmark
  public int queryAll(BenchmarkState benchmarkState) {
    int total = 0;
    Iterator<Boid> iterator = benchmarkState.state.boids().iterator();
    while (iterator.hasNext()) {
      total +=
          benchmarkState.index
              .neighborsOf(iterator.next(), benchmarkState.config.behavior().perceptionRadius())
              .size();
    }
    return total;
  }
}
