# Reynolds Boids Visualization

A Scala/ScalaFX implementation of Craig Reynolds' classic flocking model.

Milestone **0.3.0** is the first performance-oriented release. It keeps the immutable simulation architecture introduced
in 0.2, fixes the JavaFX runtime dependency discovered during real GUI execution, adds a uniform-grid spatial index, and
introduces reproducible JMH benchmarks comparing the new index with the original O(n²) reference implementation.

## Highlights of 0.3.0

- fixed ScalaFX runtime startup by adding the required `javafx-media` module,
- retained Spotless/Scalafmt as a real release gate rather than disabling formatting checks,
- changed neighbor discovery to build one `NeighborIndex` per immutable simulation tick,
- retained `NaiveNeighborSearch` as the semantic and performance baseline,
- added `UniformGridNeighborSearch` with exact Euclidean post-filtering,
- made the uniform grid the default simulation strategy,
- added `--neighbor naive|grid` to headless execution,
- added JMH 1.37 benchmarks through the Gradle JMH plugin,
- JSON benchmark output is written to `benchmark/results/jmh-0.3.0.json`,
- added naive-vs-grid equivalence regression tests,
- added a JavaFX runtime-classpath regression test for `javafx-media`,
- CI compiles the JMH source set in a dedicated no-configuration-cache step.

## Requirements

- JDK 21+,
- Gradle 9.8.0, or a generated Gradle Wrapper.

The Java toolchain is pinned to JDK 21. The Gradle process itself may run on a newer supported JDK.

## Build

```bash
gradle spotlessApply
gradle clean build
```

If a wrapper is present:

```bash
./gradlew spotlessApply
./gradlew clean build
```

## Run the ScalaFX visualization

```bash
gradle run
```

The desktop application uses `UniformGridNeighborSearch` by default.

## Headless simulation

The configured smoke task runs the grid implementation:

```bash
gradle runHeadless
```

The matching baseline scenario is:

```bash
gradle runHeadlessNaive
```

For direct execution with a selected search strategy, use the main class through your IDE or Gradle application
classpath with these supported arguments:

```text
--boids <positive integer>
--steps <non-negative integer>
--seed <long>
--neighbor naive|grid
```

The two strategies intentionally use the same steering rules. The difference is only how candidate neighbors are found.

## Spatial indexing

Milestone 0.3 changes the neighbor contract from "scan a flock per query" to "build one index per flock snapshot":

```text
SimulationState(t)
      |
      v
NeighborSearch.index(...)
      |
      v
NeighborIndex
      |
      +--> neighborsOf(boid 1)
      +--> neighborsOf(boid 2)
      +--> ...
      +--> neighborsOf(boid n)
      |
      v
SimulationState(t + 1)
```

### Naive baseline

`NaiveNeighborSearch` retains the complete flock and scans it for every query. A full simulation tick therefore remains
O(n²) in the number of boids.

### Uniform grid

`UniformGridNeighborSearch` partitions the 2D world into square cells. Each query visits only cells intersecting its
radius and then performs the same exact squared-distance predicate as the naive implementation. With approximately
uniform density and a local perception radius, candidate work is expected to grow much more slowly than a full scan.

The grid does **not** yet implement cross-edge toroidal neighbor distances. This preserves the Euclidean neighborhood
semantics of 0.2 while the existing `WrapAroundBoundary` still wraps positions after integration.

## JMH benchmarks

The project uses the Gradle JMH plugin and JMH 1.37.

Run:

```bash
gradle --no-configuration-cache jmh
```

or:

```bash
./benchmark.sh
```

Results are written as JSON to:

```text
benchmark/results/jmh-0.3.0.json
```

The suite contains:

- `NeighborSearchBenchmark` — prepared-index query cost,
- `SimulationStepBenchmark` — complete immutable simulation tick including index construction.

Default boid counts are:

```text
100
1000
5000
```

See [docs/BENCHMARKS.md](docs/BENCHMARKS.md) before interpreting results.

## Formatting

Apply formatting before building a release:

```bash
gradle spotlessApply
```

Then verify:

```bash
gradle spotlessCheck
```

CI performs checks only; it never rewrites source files.

## Architecture

```text
                            SimulationEngine
                                  |
                    +-------------+-------------+
                    |                           |
                    v                           v
             SteeringBehavior             BoundaryPolicy
                    |
                    v
              NeighborSearch
                    |
                    v
               NeighborIndex
                    |
          +---------+---------+
          |                   |
          v                   v
       Naive              UniformGrid

                            SimulationState
                                  |
                      +-----------+-----------+
                      |                       |
                      v                       v
               Headless runner              Renderer
                                              |
                                              v
                                       ScalaFxRenderer
```

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for details.

## Project structure

```text
src/main/scala/io/codeswarm/boids/
├── app/
├── behavior/
├── boundary/
├── math/
├── model/
├── neighbor/
│   ├── NeighborSearch.scala
│   ├── NeighborSearchStrategy.scala
│   ├── NaiveNeighborSearch.scala
│   └── UniformGridNeighborSearch.scala
├── rendering/
└── simulation/

src/jmh/java/io/codeswarm/boids/benchmark/
├── NeighborSearchBenchmark.java
└── SimulationStepBenchmark.java

benchmark/
├── benchmark-contract-0.3.0.json
└── results/
```

## Documentation

- [Algorithm](docs/ALGORITHM.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Benchmarks](docs/BENCHMARKS.md)
- [Implementation tasks](docs/IMPLEMENTATION_TASKS.md)
- [Migration from 0.2](docs/MIGRATION_FROM_0.2.0.md)
- [Headless mode](docs/HEADLESS_MODE.md)
- [Roadmap](docs/ROADMAP.md)
- [Release checklist](docs/RELEASE_CHECKLIST.md)
- [Changelog](CHANGELOG.md)

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).
