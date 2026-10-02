# Migration from 0.1.1 to 0.2.0

## Build

| 0.1.1 | 0.2.0 |
|---|---|
| `build.sbt` | `build.gradle.kts` |
| `project/plugins.sbt` | Spotless plugin in Gradle |
| `sbt test` | `gradle test` |
| `sbt run` | `gradle run` |
| `sbt scalafmtCheckAll` | `gradle spotlessCheck` |

## Source mapping

| 0.1.1 | 0.2.0 |
|---|---|
| Breeze `DenseVector[Double]` | `math.Vector2` |
| `model.Boid` with acceleration | `model.Boid` with `BoidId`, position, velocity |
| flat `SimulationConfig` | grouped immutable configuration case classes |
| `simulation.BoidBehavior` | `behavior.Separation`, `Alignment`, `Cohesion` |
| hard-coded weighted sum | `CompositeSteeringBehavior` |
| neighbor scan inside behaviors | `NeighborSearch` / `NaiveNeighborSearch` |
| wrap helper in simulation step | `BoundaryPolicy` / `WrapAroundBoundary` |
| `BoidFactory` | `SimulationInitializer` |
| `BoidSimulationStep` | `SimulationEngine` |
| rendering in `BoidSimulation` | `Renderer` / `ScalaFxRenderer` |
| GUI-only execution | GUI plus headless `SimulationRunner` |

## Behavior compatibility

Milestone 0.2 intentionally keeps the same three Reynolds behaviors and the same default physical constants as 0.1.1.
The architecture changes substantially, but the release does not add predators, obstacles, spatial indexing or alternate
flocking rules.

The principal semantic improvement is explicit snapshot execution: all boids observe state `t` and are published
together as state `t+1`.
