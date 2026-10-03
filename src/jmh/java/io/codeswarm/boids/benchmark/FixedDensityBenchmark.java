package io.codeswarm.boids.benchmark;

import io.codeswarm.boids.model.SimulationConfig;
import io.codeswarm.boids.model.SimulationState;
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

/** Measures grid scaling while keeping boid density approximately constant. */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FixedDensityBenchmark {
  /** Trial state for one flock size. */
  @State(Scope.Benchmark)
  public static class BenchmarkState {
    @Param({"1000", "5000", "10000"})
    public int boidCount;

    private SimulationState state;
    private SimulationEngine engine;

    /** Creates a larger world as flock size grows so average density remains approximately constant. */
    @Setup(Level.Trial)
    public void setup() {
      SimulationConfig config = BenchmarkFixtures.fixedDensityConfig(boidCount);
      state = BenchmarkFixtures.state(config);
      engine =
          SimulationComponents$.MODULE$.engine(
              config, new UniformGridNeighborSearch(config.behavior().perceptionRadius()));
    }
  }

  /** Measures one full simulation step under fixed-density scaling. */
  @Benchmark
  public SimulationState uniformGridStep(BenchmarkState benchmarkState) {
    return benchmarkState.engine.step(benchmarkState.state);
  }
}
