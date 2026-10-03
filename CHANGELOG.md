# Changelog

All notable changes to this project are documented here.

## [0.3.0] - 2026-10-02

### Added

- `NeighborIndex` abstraction prepared once per immutable simulation tick.
- `UniformGridNeighborSearch` spatial partitioning implementation.
- `NeighborSearchStrategy` ADT with `naive` and `grid` stable names.
- Headless `--neighbor naive|grid` selection and selected-strategy output.
- JMH Gradle plugin 0.7.3 integration using JMH 1.37.
- `NeighborSearchBenchmark` for prepared-query comparison.
- `SimulationStepBenchmark` for complete tick comparison including index construction.
- JSON benchmark output contract at `benchmark/results/jmh-0.3.0.json`.
- `benchmark/benchmark-contract-0.3.0.json` and `benchmark.sh`.
- Uniform-grid correctness tests against the naive implementation.
- Engine-level naive/grid equivalence regression test.
- JavaFX runtime regression test for `javafx.scene.media.MediaException$Type`.
- `BENCHMARKS.md` and migration documentation from 0.2.

### Changed

- `NeighborSearch` now prepares a `NeighborIndex` instead of scanning a flock directly in every query call.
- `DefaultSimulationEngine` builds one neighbor index per tick and reuses it for every boid.
- `SimulationComponents.engine(config)` now uses uniform-grid search by default.
- CI compiles JMH benchmark sources in a dedicated no-configuration-cache step after formatting and tests.
- README, architecture, algorithm, headless and roadmap documentation updated for the performance milestone.
- Scalafmt configuration no longer uses rewrite rules that caused source packaging to diverge from the checked format.

### Fixed

- Added `javafx-media` to the runtime dependencies, fixing the 0.2 GUI startup failure:
  `NoClassDefFoundError: javafx/scene/media/MediaException$Type`.
- Release documentation now requires `spotlessApply` before `spotlessCheck` and packaging.
- Benchmark and production wiring share the same simulation components rather than duplicating steering logic.

### Performance scope

- `NaiveNeighborSearch` remains the O(n²) semantic baseline.
- `UniformGridNeighborSearch` reduces candidate scanning through square-cell spatial partitioning.
- JMH measures both prepared-index query cost and full simulation-step cost.
- No benchmark numbers are hard-coded in the release because results are hardware/JVM dependent.

### Intentionally deferred

- quadtree comparison,
- minimum-image/toroidal neighbor metric,
- parallel simulation,
- SoA storage,
- interactive controls and grid overlay,
- obstacles and predators,
- Scala 3 migration.

## [0.2.0] - 2026-10-02

### Added

- Gradle Kotlin DSL build with Gradle 9.8.0 pinned in CI and the wrapper task.
- Dedicated immutable `Vector2` simulation type.
- Strong `BoidId` identity type.
- Immutable `SimulationState` snapshots.
- Grouped world/flock/behavior/render configuration types.
- `SteeringBehavior` abstraction with independent Separation, Alignment and Cohesion implementations.
- `CompositeSteeringBehavior` and weighted behavior composition.
- `NeighborSearch` abstraction and reference `NaiveNeighborSearch` implementation.
- `BoundaryPolicy` and modulo-based `WrapAroundBoundary`.
- Deterministic `SimulationInitializer` driven by configuration seed.
- `SimulationEngine`, `DefaultSimulationEngine` and `SimulationRunner`.
- Centralized `SimulationComponents` wiring.
- Renderer abstraction and `ScalaFxRenderer`.
- Headless application and Gradle `runHeadless` task.
- JUnit 5 test suite for direct Gradle test discovery.
- Spotless + Scalafmt formatting checks.
- Headless CI smoke test.

### Changed

- Build system migrated from sbt to Gradle.
- Simulation core no longer depends on ScalaFX.
- All boids are updated from the same immutable input snapshot.
- Rendering state is no longer mixed with simulation rules.
- Configuration is split into focused immutable case classes.

### Known issues corrected in 0.3.0

- source archive had not been fully normalized by Spotless before packaging,
- `javafx-media` was missing from runtime dependencies and caused GUI startup failure.

## [0.1.1] - 2026-10-02

### Added

- Apache License 2.0.
- GitHub Actions CI.
- Scalafmt integration.
- stronger baseline unit tests and documentation.

### Changed

- stabilized the original implementation on Scala 2.13.18 / JDK 21-era dependencies.
- centralized baseline simulation constants.

## [0.1]

Initial public Reynolds Boids ScalaFX visualization.
