# Changelog

All notable changes to this project are documented here.

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
- `HEADLESS_MODE.md` and rewritten architecture/implementation documentation.

### Changed

- Build system migrated from sbt to Gradle.
- Simulation core no longer depends on ScalaFX.
- All boids are updated from the same immutable input snapshot.
- Rendering state is no longer mixed with simulation rules.
- Configuration is split into focused immutable case classes.
- Neighbor distance filtering uses squared distances where possible.
- GitHub Actions now executes pinned Gradle 9.8.0 tasks.

### Removed

- `build.sbt` and `project/` sbt metadata.
- `sbt-scalafmt`.
- Breeze dependency.
- Breeze `DenseVector` representation.
- `VectorOperations` utility object.
- Monolithic `BoidBehavior` object.
- Persisted acceleration field from `Boid`.
- Simulation logic from the ScalaFX application.

### Intentionally deferred

- spatial indexing,
- JMH benchmarks,
- true cross-boundary toroidal neighbor metric,
- interactive controls and debug overlays,
- obstacles/predators,
- parallel simulation and SoA storage,
- Scala 3 migration.

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
