# Architecture — Milestone 0.1.1

## Goal

Milestone 0.1.1 is a stabilization release, not a redesign. It preserves the original ScalaFX-based visualization and classic Reynolds behavior while making responsibilities easier to understand and test.

## Packages

### `io.codeswarm.boids.app`

Contains the desktop application entry point and ScalaFX rendering loop.

### `io.codeswarm.boids.model`

Contains small data models:

- `Boid` — position, velocity and acceleration,
- `SimulationConfig` — validated simulation constants.

### `io.codeswarm.boids.simulation`

Contains baseline simulation logic:

- `BoidBehavior` — separation, alignment and cohesion,
- `BoidSimulationStep` — one-frame state transition and world wrapping,
- `BoidFactory` — initial flock construction.

### `io.codeswarm.boids.util`

Contains Breeze-related numerical helpers in `VectorOperations`.

## Runtime flow

```text
BoidSimulation (ScalaFX)
        │
        ├── BoidFactory.randomFlock
        │
        ▼
current IndexedSeq[Boid]
        │
        ▼
BoidSimulationStep.next
        │
        ├── BoidBehavior.separation
        ├── BoidBehavior.alignment
        └── BoidBehavior.cohesion
        │
        ▼
next IndexedSeq[Boid]
        │
        ▼
ScalaFX Canvas rendering
```

## Deliberate limitations

The following are intentionally left unchanged or deferred:

- neighbor search remains O(n²),
- Breeze remains the vector representation,
- the visualization is still hosted directly by the application,
- there is no generic renderer interface,
- there is no `SpatialIndex`,
- there is no JMH module,
- there are no predators, obstacles or additional steering behaviors.

These limitations make 0.1.1 useful as a reference point for later architectural and performance work.
