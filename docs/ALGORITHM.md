# Reynolds Boids Algorithm — milestone 0.2.0

## State update

Every logical tick computes:

```text
SimulationState(t)
      │
      ├─ find visible neighbors
      ├─ compute weighted steering
      ├─ velocity(t+1) = limit(velocity(t) + steering, maxSpeed)
      └─ position(t+1) = boundary(position(t) + velocity(t+1))
      │
      ▼
SimulationState(t+1)
```

All boids read only `SimulationState(t)`.

## Separation

For neighbors inside the separation radius, compute a direction away from the neighbor and weight it inversely by
actual distance. The average contribution is converted into a desired velocity and then a limited steering force.

## Alignment

Average visible-neighbor velocities, normalize the result to `maxSpeed`, subtract current velocity, and limit to
`maxForce`.

## Cohesion

Compute the mean visible-neighbor position and steer toward this local center of mass.

## Weighted composition

```text
F = ws * Fseparation + wa * Falignment + wc * Fcohesion
```

Weights are configuration values and the engine knows only the resulting `SteeringBehavior` abstraction.

## Neighbor complexity

`NaiveNeighborSearch` checks every boid for every query boid. A complete step therefore remains O(n²). This is
intentional: milestone 0.3 will compare spatially indexed implementations against this baseline.

## Numerical details

Distance filters use squared distance when possible to avoid unnecessary square roots. `Vector2.normalized` handles the
zero vector without NaN/Infinity, and speed/force limiting preserve direction.
