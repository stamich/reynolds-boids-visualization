# Desktop UI — milestone 0.4

## Layout

```text
+--------------------------------------+--------------------+
|                                      | Simulation controls|
|                                      | Start Pause Restart|
|             Canvas                   | status / restart   |
|                                      | Boids *            |
|                                      | Seed *             |
|                                      | Speed / force      |
|                                      | Radii / weights    |
|                                      | Boid size          |
|                                      | Show spatial grid  |
|                                      | Metrics            |
+--------------------------------------+--------------------+
```

`*` means the parameter is restart-required.

## Lifecycle

- `Ready -> Start -> Running`
- `Running -> Pause -> Paused`
- `Paused -> Start -> Running`
- `Restart -> Ready`, tick 0, deterministic initialization using the current seed/configuration.

Pause does not alter `SimulationState`. Restart does not invent a new seed.

## Live parameters

Applied on subsequent ticks without resetting the flock:

- max speed,
- max force,
- perception radius,
- separation radius,
- separation/alignment/cohesion weights,
- boid size,
- grid overlay visibility.

## Restart-required parameters

- boid count,
- seed.

The panel displays a restart-required message after either changes.

## Numeric controls

Each tunable numeric parameter combines a slider with a text field. Invalid text restores the previous committed value. Values outside the supported slider range are clamped before the callback is invoked.

## Color model

Color is not part of `Boid`. The default renderer uses deterministic ID-based hues, so the same boid keeps the same color across frames and deterministic restarts.
