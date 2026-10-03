# Reynolds Boids Visualization 0.4.0

Interactive Scala/ScalaFX implementation of Craig Reynolds' classic Boids model. Milestone 0.4 builds on the spatial-indexing/JMH work from 0.3 and turns the project into an interactive simulation laboratory with runtime controls, colorful boids, diagnostics and reproducible Gradle-based builds.

## Milestone 0.4 highlights

- complete Gradle Wrapper surface (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`),
- Start / Pause / Restart controls,
- slider + editable field controls for simulation parameters,
- deterministic restart by seed,
- colorful, velocity-oriented boid triangles,
- optional uniform-grid overlay,
- live FPS, simulation, rendering and spatial-index metrics,
- fixed-world, fixed-density and grid-cell-size JMH benchmarks,
- optional JMH GC/allocation profiling,
- JavaFX `media` runtime dependency retained and regression-tested,
- expanded unit/regression tests and documentation.

## Stack

- JDK toolchain 21
- Scala 2.13.18
- ScalaFX 21.0.0-R32
- JavaFX 21.0.8
- Gradle 9.8.0
- JUnit Jupiter 5.13.4
- Spotless + Scalafmt 3.10.7
- JMH 1.37

## Build

The repository ships a self-contained Gradle wrapper bootstrap. On a normal networked workstation the first invocation downloads and verifies the Gradle 9.8.0 binary distribution.

```bash
./gradlew clean build
```

To replace the transparent bootstrap JAR with Gradle's official generated wrapper files, run once:

```bash
./gradlew wrapper
```

Then commit the generated wrapper files if this project is stored in Git.

## Run the desktop application

```bash
./gradlew run
```

The right-side control panel exposes:

- Start / Pause / Restart,
- boid count (restart required),
- seed (restart required),
- max speed and max force,
- perception and separation radii,
- separation / alignment / cohesion weights,
- boid size,
- spatial-grid overlay.

Live simulation values are applied without resetting the flock. Boid count and seed are intentionally restart-required so initialization remains deterministic.

## Headless mode

```bash
./gradlew runHeadless
./gradlew runHeadlessNaive
```

The commands execute deterministic smoke scenarios with uniform-grid and naive neighbor lookup respectively.

## Formatting

```bash
./gradlew spotlessApply
./gradlew spotlessCheck
```

CI checks formatting; it never modifies source files automatically.

## Tests

```bash
./gradlew test
```

Tests cover vector math, classic Reynolds steering, grid-vs-naive equivalence, boundary behavior, deterministic initialization, controller lifecycle/configuration updates, spatial metrics, color stability, JavaFX runtime dependencies and wrapper completeness.

## Benchmarks

Standard JMH suite:

```bash
./benchmark.sh
```

GC/allocation profiling:

```bash
./benchmark-gc.sh
```

Results are written under `benchmark/results/`. See `docs/BENCHMARKS.md`.

## Architecture

```text
ScalaFX UI
  SimulationControlPanel
  SimulationView
  MetricsPanel
          |
          v
SimulationController
  status + immutable config + immutable state
          |
          v
SimulationEngine
  SteeringBehavior
  NeighborSearch -> NeighborIndex
  BoundaryPolicy
          |
          v
SimulationState(t + 1)
          |
          +--> ScalaFxRenderer -> BoidColorStrategy
          +--> runtime/spatial metrics
```

The simulation domain remains independent of ScalaFX. UI state, colors and frame timings are not stored in `Boid` or `SimulationState`.

## Documentation

- `docs/ARCHITECTURE.md`
- `docs/ALGORITHM.md`
- `docs/UI.md`
- `docs/BENCHMARKS.md`
- `docs/HEADLESS_MODE.md`
- `docs/IMPLEMENTATION_TASKS.md`
- `docs/MIGRATION_FROM_0.3.0.md`
- `docs/RELEASE_CHECKLIST.md`
- `docs/ROADMAP.md`

## License

Apache License 2.0. See `LICENSE` and `NOTICE`.
