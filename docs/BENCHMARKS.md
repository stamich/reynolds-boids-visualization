# Benchmarks — milestone 0.4

## Why the suite changed

Milestone 0.3 showed that Uniform Grid significantly improves neighbor lookup and full-step performance as flock size grows, but fixed world dimensions also increase density. Milestone 0.4 therefore separates density growth from population growth and adds allocation profiling.


## 0.3 measured baseline that motivates 0.4

The raw 0.3 reference run is retained as `benchmark/baselines/jmh-0.3.0.json`. It measured:

| Boids | Naive neighbor queries | Grid neighbor queries | Naive full step | Grid full step |
|---:|---:|---:|---:|---:|
| 100 | 0.0420 ms | 0.02195 ms | 0.0584 ms | 0.0569 ms |
| 1,000 | 4.324 ms | 1.413 ms | 5.165 ms | 2.263 ms |
| 5,000 | 131.35 ms | 31.26 ms | 145.52 ms | 46.71 ms |

At 5,000 boids the prepared grid improved neighbor lookup by about 4.2x and the complete simulation step by about 3.1x. The 0.4 benchmark additions are designed to explain the remaining scaling cost rather than introducing another spatial data structure prematurely.

## Benchmark families

### Fixed world

`NeighborSearchBenchmark` and `SimulationStepBenchmark` preserve the 0.3 workload style: world size remains 1200x800 while boid count grows. This measures increasingly dense flocks.

### Fixed density

`FixedDensityBenchmark` scales world area approximately in direct proportion to boid count. The 1000-boid 1200x800 world is the density baseline. This better exposes the algorithmic scaling of Uniform Grid when average local density remains approximately constant.

### Cell size

`CellSizeBenchmark` evaluates `0.5x`, `1.0x`, `1.5x` and `2.0x` perception radius for a 5000-boid fixed world. It measures whether the default `cellSize = perceptionRadius` remains near the empirical optimum.

### GC/allocation

`benchmark-gc.sh` runs full-step benchmark families with JMH's GC profiler. Primary metrics of interest are allocation rate, normalized bytes/op, GC count and GC time.

## Commands

```bash
./benchmark.sh
./benchmark-gc.sh
```

Standard JSON:

```text
benchmark/results/jmh-0.4.0.json
```

GC JSON:

```text
benchmark/results/jmh-0.4.0-gc.json
```

## Measurement rules

- Do not compare IDE wall-clock timing directly with JMH.
- Keep JDK, power/governor state and benchmark parameters documented when comparing milestones.
- GUI spatial metrics are disabled in benchmark fixtures.
- A speedup claim should compare equivalent benchmark methods and world/density semantics.
