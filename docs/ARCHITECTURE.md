# Architecture — Milestone 0.3.0

## Goals

Milestone 0.3 keeps the domain/rendering separation from 0.2 and changes the expensive neighborhood boundary so a
spatial data structure can be built once for each immutable flock snapshot.

The simulation core remains independent of ScalaFX.

## Component view

```text
BoidApplication / HeadlessApplication
                |
                v
        SimulationComponents
                |
                v
        DefaultSimulationEngine
          /         |          \
         /          |           \
        v            v            v
SteeringBehavior NeighborSearch BoundaryPolicy
                      |
                      v
                 NeighborIndex
                   /      \
                  /        \
                 v          v
              Naive    UniformGrid

SimulationState -------------------------------------> Renderer
                                                           |
                                                           v
                                                    ScalaFxRenderer
```

## Immutable tick model

A tick is evaluated from one immutable state:

```text
state(t)
   |
   +--> build NeighborIndex once
   |
   +--> query neighbors for every boid
   |
   +--> evaluate weighted steering
   |
   +--> integrate velocity and position
   |
   v
state(t + 1)
```

No boid can observe another boid that has already been updated during the same tick.

## NeighborSearch and NeighborIndex

`NeighborSearch` is responsible for preparation:

```scala
def index(flock: IndexedSeq[Boid], world: WorldConfig): NeighborIndex
```

`NeighborIndex` is responsible for repeated queries:

```scala
def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid]
```

This split is important. Building a grid inside every `neighborsOf` call would still make the algorithm expensive and
would hide index-construction cost from simulation-step benchmarks.

## NaiveNeighborSearch

The naive implementation stores the immutable flock and scans every boid for each query. It is retained for:

- semantic correctness comparison,
- performance baseline,
- regression diagnosis,
- small-flock scenarios where simplicity may be sufficient.

## UniformGridNeighborSearch

The grid maps world positions to integer cells:

```text
cellX = floor(x / cellSize)
cellY = floor(y / cellSize)
```

For a query radius `r`, it examines cells within:

```text
ceil(r / cellSize)
```

cells in each direction. Candidates then pass through the same strict squared-Euclidean-distance predicate used by the
naive baseline.

The default cell size equals `perceptionRadius`, which means the standard full-perception query normally examines a 3x3
cell neighborhood.

## Boundary semantics

`WrapAroundBoundary` still maps positions back into the rectangular world after integration. Neighbor search in 0.3 is
still Euclidean, not minimum-image/toroidal. Therefore boids near opposite world edges are not yet treated as close
neighbors.

A true toroidal metric is intentionally deferred so the performance change can be compared against 0.2 semantics.

## Rendering

`Renderer` receives immutable states only. `ScalaFxRenderer` knows about JavaFX/ScalaFX; the simulation core does not.

Milestone 0.3 explicitly includes `javafx-media` because the selected ScalaFX version loads JavaFX media types while
initializing canvas-related classes.

## Benchmark boundary

JMH benchmark sources live outside production sources in `src/jmh/java` and depend on the production `main` source set.
The benchmark suite measures both isolated neighborhood queries and complete simulation ticks.
