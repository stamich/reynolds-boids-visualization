# Algorithm — Milestone 0.3.0

## Reynolds steering rules

The behavioral model remains the classic weighted combination:

```text
steering =
    separation * separationWeight +
    alignment  * alignmentWeight  +
    cohesion   * cohesionWeight
```

Milestone 0.3 changes how neighbors are discovered, not the steering equations.

## Separation

For neighbors inside `separationRadius`, the boid accumulates vectors pointing away from neighbors with stronger
contribution at shorter distance. The average desired direction is converted into a limited steering force.

## Alignment

The boid computes the average velocity of visible neighbors and steers its velocity toward that local average heading.

## Cohesion

The boid computes the center of mass of visible neighbors and steers toward that point.

## Neighborhood predicate

Both the naive and grid implementations use the same strict predicate:

```text
other.id != boid.id
0 < distanceSquared(boid, other) < radius²
```

Using squared distance avoids an unnecessary square root during filtering.

## Naive neighborhood search

For every boid, every other boid is considered. A complete tick is therefore O(n²).

## Uniform-grid neighborhood search

### Build phase

Each boid is assigned to one square cell:

```text
cellX = floor(position.x / cellSize)
cellY = floor(position.y / cellSize)
```

Building the map is O(n).

### Query phase

The query computes the number of cells intersecting the requested radius:

```text
extent = ceil(radius / cellSize)
```

Only buckets in that local rectangular cell window are visited. Each candidate still undergoes the exact distance
predicate, so the grid changes candidate selection only and does not approximate the Reynolds radius.

### Expected behavior

If density remains approximately bounded while world area and flock size scale together, local candidate work can stay
close to constant per boid. In the fixed-size desktop world, density rises with flock size, so the practical complexity
is workload-dependent; JMH results should therefore be treated as measured evidence rather than assuming ideal O(n).

## Integration

For each boid:

```text
newVelocity = limit(oldVelocity + steering, maxSpeed)
newPosition = boundary(oldPosition + newVelocity)
```

All updates read from `SimulationState(t)` and are published together as `SimulationState(t + 1)`.
