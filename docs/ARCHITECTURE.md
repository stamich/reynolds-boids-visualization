# Architecture — milestone 0.4

## Goals

Milestone 0.4 keeps the deterministic simulation core independent from ScalaFX while adding an application/controller layer for interactive operation and observability.

```text
+----------------------------- ScalaFX -----------------------------+
| SimulationView   SimulationControlPanel   MetricsPanel            |
|       |                    |                    ^                  |
|       |                    v                    |                  |
|       +------------ SimulationController ------+                  |
+-----------------------------|-------------------------------------+
                              v
                    SimulationEngine
                 /         |          \
        SteeringBehavior NeighborSearch BoundaryPolicy
                              |
                         NeighborIndex
                       /               \
                    Naive          UniformGrid
                                      |
                                SpatialMetrics*
                              (* optional only)
                              |
                              v
                    SimulationState(t+1)
                              |
                    +---------+---------+
                    |                   |
               ScalaFxRenderer      headless/JMH
                    |
             BoidColorStrategy
```

## Dependency rules

1. `model`, `math`, `behavior`, `neighbor`, `boundary` and `simulation` do not depend on ScalaFX.
2. `SimulationState` contains only deterministic domain state: tick and boids.
3. Lifecycle (`Ready/Running/Paused`), UI values and frame timings belong to `ui`/`metrics`.
4. Colors are renderer concerns, selected by `BoidColorStrategy`; `Boid` has no color field.
5. Spatial diagnostics are optional and can be disabled for benchmark hot paths.

## SimulationController

`SimulationController` owns the mutable application shell around immutable domain values:

- current `SimulationConfig`,
- current `SimulationState`,
- current `SimulationStatus`,
- current engine wiring,
- restart-required marker,
- latest `RuntimeMetrics`.

Live parameter changes replace the immutable config and rebuild lightweight engine wiring without resetting state. `boidCount` and `seed` only take effect after `restart()`.

## Uniform Grid instrumentation

`UniformGridNeighborSearch` accepts `metricsEnabled`. When disabled, it creates the same non-instrumented index path used by JMH. When enabled, the prepared index records:

- occupied cells,
- average/max boids per occupied cell,
- candidates inspected per query,
- accepted neighbors per query.

This avoids contaminating performance measurements with GUI telemetry.

## Rendering

`ScalaFxRenderer` draws velocity-oriented triangles and delegates color choice to `BoidColorStrategy`. `IdBasedColorStrategy` maps stable IDs to well-separated HSB hues. Grid overlay rendering is separate from simulation indexing.

## Build reproducibility

The archive contains wrapper scripts, properties and a transparent bootstrap JAR. The bootstrap downloads the checksum-pinned Gradle 9.8.0 distribution and launches it. Running `./gradlew wrapper` replaces the bootstrap artifacts with Gradle's official generated wrapper files.
