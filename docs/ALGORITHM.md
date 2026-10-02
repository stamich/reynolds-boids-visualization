# Reynolds Boids Algorithm

## Overview

The boids model produces flocking behavior from local interactions rather than from a central controller. Every boid responds only to nearby members of the flock.

Milestone 0.1.1 implements the three canonical steering rules.

## Separation

Separation prevents crowding. For every neighbor inside `separationRadius`, the boid computes a vector pointing away from that neighbor. Closer neighbors receive greater influence through inverse-distance weighting.

Conceptually:

```text
separation(i) = Σ normalize(position(i) - position(j)) / distance(i, j)
```

The resulting desired velocity is converted to a steering force and limited by `maxForce`.

## Alignment

Alignment tends to make nearby boids travel in the same direction.

```text
averageVelocity = average(velocity(neighbors))
```

The average velocity is normalized to `maxSpeed`, the current velocity is subtracted, and the resulting steering force is limited by `maxForce`.

## Cohesion

Cohesion moves a boid toward the local center of mass.

```text
center = average(position(neighbors))
desired = center - position(i)
```

The desired direction is converted to a velocity and then to a limited steering force.

## Combined force

```text
force =
  separation * separationWeight +
  alignment  * alignmentWeight +
  cohesion   * cohesionWeight
```

## Position update

For each frame:

```text
velocity(t+1) = limit(velocity(t) + force, maxSpeed)
position(t+1) = wrap(position(t) + velocity(t+1))
```

All boids are calculated from the same previous-frame state before the next flock is returned.

## Complexity

Neighbor lookup in milestone 0.1.1 is intentionally naive: every boid scans the flock.

```text
N boids × N candidate neighbors = O(N²)
```

This provides a clear reference implementation for the spatial-index/JMH milestone planned later.
