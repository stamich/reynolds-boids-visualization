# Changelog

All notable changes to this project are documented here.

## [0.4.0] - 2026-10-03

### Added

- Complete Gradle Wrapper surface: `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.properties` and wrapper bootstrap JAR/source.
- Gradle 9.8.0 binary-distribution SHA-256 verification.
- `SimulationStatus` (`Ready`, `Running`, `Paused`).
- `SimulationController` for lifecycle, deterministic restart and live configuration updates.
- `SimulationControlPanel` with Start, Pause and Restart actions.
- Reusable `DoubleParameterControl` and `IntParameterControl` slider/text-field controls.
- Runtime controls for flock limits, Reynolds radii/weights and boid rendering size.
- Restart-required handling for boid count and seed.
- `MetricsPanel` with FPS, tick, simulation-step time, rendering time, speed and grid diagnostics.
- `RuntimeMetrics` and `SpatialMetrics`.
- Optional instrumented uniform-grid index with candidate, neighbor and cell-occupancy metrics.
- `BoidColorStrategy`, deterministic `IdBasedColorStrategy` and diagnostic `SpeedBasedColorStrategy`.
- `GridOverlayRenderer` and Show Grid UI option.
- Fixed-density JMH benchmark.
- Uniform-grid cell-size JMH benchmark.
- GC/allocation benchmark script using JMH `-prof gc`.
- Controller, metrics, color and Gradle-wrapper regression tests.
- `docs/UI.md` and `docs/MIGRATION_FROM_0.3.0.md`.

### Changed

- Desktop application is now composed from `SimulationView`, `SimulationControlPanel`, `MetricsPanel` and `SimulationController`.
- Boids are rendered as stable individual colors instead of one global color.
- Uniform-grid search can optionally expose diagnostics; instrumentation is disabled in benchmark fixtures.
- CI uses `./gradlew` instead of relying on a preinstalled Gradle executable.
- Benchmark contract extended with fixed-world, fixed-density, cell-size and GC/allocation families.
- Release workflow requires wrapper, formatter, tests, headless smoke test and GUI smoke test.

### Preserved

- Immutable `SimulationState` snapshots.
- Separation, alignment and cohesion semantics.
- Uniform Grid as the default neighbor strategy.
- Naive neighbor lookup as correctness/performance baseline.
- Snapshot update semantics: all boids read `state(t)` and publish `state(t+1)` together.
- Deterministic initialization by seed.

### Deferred

- QuadTree and KD-tree indexing.
- Structure-of-Arrays simulation representation.
- Parallel simulation stepping.
- Toroidal minimum-image neighbor distance.
- Predator/obstacle/food behaviors.
- Boid selection and per-boid steering-vector debug rendering.
- Scala 3 migration and GPU acceleration.

## [0.3.0] - 2026-10-03

- Added `NeighborIndex`, `UniformGridNeighborSearch`, strategy selection and JMH benchmarks.
- Added JSON benchmark output and correctness equivalence tests.
- Added `javafx-media` and GUI runtime dependency regression coverage.
- Measured large speedups for Uniform Grid at 1,000 and 5,000 boids.

## [0.2.0]

- Replaced Breeze with immutable `Vector2`.
- Introduced `SimulationState`, `SimulationEngine`, steering behavior composition, boundary policy, headless mode and renderer abstraction.
- Migrated build from sbt to Gradle Kotlin DSL.

## [0.1.1]

- Stabilized and documented the original implementation.
- Added Apache License 2.0, tests, CI and formatting.

## [0.1]

Initial Reynolds Boids visualization baseline.
