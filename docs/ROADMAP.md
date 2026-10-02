# Roadmap

## 0.1

Original Reynolds boids visualization prototype.

## 0.1.1

Stabilized baseline: dependency refresh, tests, documentation, Apache-2.0 and GitHub Actions.

## 0.2.0 — architecture and Gradle migration

- Gradle Kotlin DSL + Wrapper,
- dedicated `Vector2`,
- immutable `SimulationState`,
- steering strategy hierarchy,
- neighbor-search abstraction,
- boundary abstraction,
- deterministic initialization,
- headless engine/runner,
- renderer abstraction,
- JUnit 5 tests.

## 0.3 — spatial indexing and benchmarks

Planned:

- `UniformGridNeighborSearch`,
- optional quadtree comparison,
- JMH module,
- `Naive` vs `UniformGrid` performance comparison,
- JSON benchmark output,
- workload sizes from hundreds to tens of thousands of boids,
- allocation/GC observations.

## 0.4 — interactive visualization

Planned:

- live sliders for behavior weights/radii,
- pause/resume/single-step/reset,
- selected-boid debug view,
- velocity/force vectors,
- neighbor/perception visualization,
- trails and FPS/step-time overlay.

## Later milestones

- obstacles, attractors and repulsors,
- predators and multi-species systems,
- simulation metrics and replay,
- parallel/SoA experiments,
- Scala 3 evaluation,
- optional 3D/GPU experiments.
