# Architecture — milestone 0.2.0

## Goals

Milestone 0.2 separates the simulation core from ScalaFX and from specific infrastructure implementations. The core can
now run deterministically in tests, a command-line process or the desktop visualizer.

## Dependency direction

```text
app ───────────────► simulation ─────────► model/math
 │                     │  │
 │                     │  ├─────────────► behavior
 │                     │  ├─────────────► neighbor
 │                     │  └─────────────► boundary
 │
 └──────────────────► rendering ─────────► model
                          │
                          └──────────────► ScalaFX
```

The domain and simulation packages never import ScalaFX.

## Core state

`SimulationState` is immutable. Every call to `SimulationEngine.step` reads the same snapshot and produces a complete
new state. There are no partially updated neighbors.

## Vector model

Breeze is removed. `Vector2` is deliberately small and domain-specific. It supports magnitude, normalization, limiting,
dot products and distance operations needed by flocking.

## Steering

`SteeringBehavior` is a pure strategy. `Separation`, `Alignment`, and `Cohesion` implement the classic rules.
`CompositeSteeringBehavior` applies explicit weights without coupling the engine to concrete behaviors.

## Neighbor search

`NeighborSearch` is the seam for milestone 0.3. `NaiveNeighborSearch` scans the flock and remains the correctness and
performance baseline.

## Boundaries

`BoundaryPolicy` removes screen-edge logic from the engine. `WrapAroundBoundary` preserves the toroidal screen behavior
from the baseline implementation.

## Rendering

`Renderer` consumes `SimulationState`; it cannot influence physics. `ScalaFxRenderer` is the only class in the rendering
path that knows JavaFX/ScalaFX primitives.

## Determinism

`RandomSimulationInitializer` is seeded from `SimulationConfig`. Same config + same seed + same number of steps produces
an identical trajectory.

## Intentionally deferred

- spatial hash / uniform grid,
- quadtree,
- JMH,
- true toroidal neighbor distance across opposite screen edges,
- obstacles and predators,
- interactive control panel,
- parallel simulation,
- structure-of-arrays storage,
- Scala 3 migration.
