# Roadmap

## 0.1 — Original baseline

Initial Scala/ScalaFX Reynolds boids visualization.

## 0.1.1 — Stabilization

- modernize Scala/JDK/JavaFX/ScalaFX toolchain,
- centralize baseline configuration,
- improve numerical safety and code documentation,
- extend unit/regression tests,
- add Scalafmt,
- migrate CI to GitHub Actions,
- add Apache License 2.0,
- document architecture and algorithm.

## 0.2 — Architecture

Planned:

- separate simulation engine from ScalaFX rendering,
- introduce renderer-neutral simulation state,
- replace Breeze in the hot path with a lightweight `Vector2`,
- formalize steering behaviors and boundary policies,
- deterministic seeded simulation configuration,
- headless simulation mode.

## 0.3 — Spatial indexing and benchmarks

Planned:

- `SpatialIndex` abstraction,
- naive reference implementation,
- uniform grid/spatial hash,
- quadtree experiment,
- JMH benchmark module,
- JSON benchmark results,
- regression comparison between milestones.

## 0.4 — Interactive visualization

Planned:

- live parameter controls,
- pause/resume/single-step,
- debug vectors,
- perception-radius visualization,
- neighbor highlighting,
- trails and simulation metrics.

## Later milestones

Candidates include obstacles, attractors/repulsors, predators, multiple species, richer metrics, parallel update strategies, structure-of-arrays experiments, Scala 3 migration and eventually 3D/GPU experiments.
