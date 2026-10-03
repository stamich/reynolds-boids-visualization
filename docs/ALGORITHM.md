# Reynolds Boids algorithm — milestone 0.4

Milestone 0.4 does not change the three classic steering rules. It changes how parameters are controlled and how the simulation is observed.

## Separation

Nearby boids inside `separationRadius` contribute repulsive steering. Closer boids contribute more strongly through inverse-distance weighting. The result is converted into a steering vector and limited by `maxForce`.

## Alignment

Neighbors inside `perceptionRadius` contribute their velocities. Their mean heading becomes the desired velocity, from which current velocity is subtracted and steering is limited by `maxForce`.

## Cohesion

The average position of neighbors inside `perceptionRadius` forms a local center of mass. The boid steers toward that point.

## Weighted composition

```text
steering = separation * separationWeight
         + alignment  * alignmentWeight
         + cohesion   * cohesionWeight
```

All weights can be changed live in milestone 0.4. A live change creates a new immutable configuration and lightweight engine wiring; it does not mutate existing boids or reset `SimulationState`.

## Snapshot semantics

Every tick still follows:

```text
state(t)
  -> prepare NeighborIndex once
  -> query every boid against the same state(t)
  -> compute all next boids
  -> publish state(t+1)
```

No boid observes a partially updated flock.

## Uniform Grid

World coordinates map to square cells. The default cell size equals `perceptionRadius`. A query scans only cells intersecting the requested radius and always performs an exact squared-Euclidean-distance check before accepting a neighbor.

0.4 can instrument the grid to expose candidate and occupancy metrics. Instrumentation does not alter neighbor semantics and is disabled in JMH fixtures.
