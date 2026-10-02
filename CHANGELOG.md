# Changelog

All notable changes to this project are documented in this file.

The format follows the principles of Keep a Changelog and the project uses milestone-oriented semantic versioning.

## [0.1.1] - 2026-10-02

### Added

- Apache License 2.0 and NOTICE file.
- `SimulationConfig` for validated baseline parameters.
- `BoidFactory` with injectable randomness for reproducible tests.
- `BoidSimulationStep` to make a one-frame update directly testable.
- `VectorOperations` safety helpers for normalization, limiting and finite-value checks.
- ScalaTest coverage for vector operations, Reynolds steering rules, configuration, initial flock generation and multi-step simulation regression.
- Scalafmt configuration and sbt plugin.
- GitHub Actions CI pipeline for formatting, compile, tests and packaging.
- `docs/ALGORITHM.md`, `docs/ARCHITECTURE.md`, `docs/ROADMAP.md` and `docs/RELEASE_CHECKLIST.md`.
- Scaladoc for public classes, objects and methods where it improves understanding.

### Changed

- Project version set to `0.1.1`.
- Scala updated to 2.13.18.
- Runtime target standardized on JDK 21.
- ScalaFX updated to 21.0.0-R32.
- JavaFX updated to 21.0.8 and reduced to the required `base`, `graphics` and `controls` modules.
- Build metadata and compiler warnings modernized.
- README rewritten to document build, architecture, algorithm, tests, CI and roadmap.
- Simulation update computes every next boid from the same previous-frame flock to avoid order-dependent state updates.

### Removed

- Dependence on GitLab CI in favor of `.github/workflows/ci.yml`.
- Unused JavaFX FXML, media, Swing and Web modules from the proposed build.

### Intentionally deferred

- dedicated `Vector2` replacement for Breeze,
- renderer/simulation module split,
- `SpatialIndex` and spatial partitioning,
- JMH benchmarks,
- advanced steering rules, obstacles and predators,
- Scala 3 migration.

## [0.1]

### Added

- Initial Reynolds boids visualization.
- ScalaFX desktop rendering.
- Separation, alignment and cohesion behavior.
