# Roadmap

## 0.1

Original Reynolds boids visualization prototype.

## 0.1.1

Stabilized baseline: dependency refresh, tests, documentation, Apache-2.0 and GitHub Actions.

## 0.2.0 — architecture and Gradle migration

- Gradle Kotlin DSL,
- dedicated `Vector2`,
- immutable `SimulationState`,
- steering behavior hierarchy,
- neighbor-search abstraction,
- boundary abstraction,
- deterministic initialization,
- headless engine/runner,
- renderer abstraction,
- JUnit 5 tests.

## 0.3.0 — spatial indexing and JMH

Implemented:

- `NeighborIndex` prepared once per simulation tick,
- `NaiveNeighborSearch` baseline,
- `UniformGridNeighborSearch`,
- selectable `naive` / `grid` headless strategies,
- JMH 1.37 integration,
- prepared-query and complete-step benchmarks,
- JSON benchmark output,
- correctness equivalence tests,
- JavaFX `media` runtime dependency fix and regression test.

## 0.4 — interactive visualization

Planned:

- live sliders for behavior weights and radii,
- pause/resume/single-step/reset,
- selected-boid debug view,
- velocity and steering vectors,
- neighbor/perception visualization,
- spatial-grid overlay,
- trails and FPS/step-time overlay.

## 0.5 — advanced environment

Planned:

- obstacles,
- attractors and repulsors,
- seek/flee/arrive behaviors,
- optional predators.

## Later milestones

- multi-species systems,
- flow/flock metrics and replay,
- true toroidal neighbor metric,
- quadtree comparison,
- parallel/SoA experiments,
- Scala 3 evaluation,
- optional 3D/GPU experiments.
