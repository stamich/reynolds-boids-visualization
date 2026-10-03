# Reynolds Boids Visualization

A Scala/ScalaFX implementation of Craig Reynolds' classic flocking model.

Milestone **0.2.0** is the first architectural release after the stabilized 0.1.1 baseline. It preserves the classic
separation/alignment/cohesion behavior while decoupling simulation, rendering and infrastructure concerns.

## Highlights of 0.2.0

- migrated build from **sbt to Gradle 9.8.0 / Kotlin DSL**,
- replaced Breeze with a dedicated immutable `Vector2`,
- introduced immutable `SimulationState`,
- separated steering rules behind `SteeringBehavior`,
- introduced weighted behavior composition,
- separated neighbor discovery behind `NeighborSearch`,
- retained `NaiveNeighborSearch` as the O(n²) reference baseline,
- separated world boundaries behind `BoundaryPolicy`,
- deterministic initialization through `SimulationConfig.seed`,
- introduced `SimulationEngine` and `SimulationRunner`,
- added renderer abstraction and a ScalaFX implementation,
- added a dependency-free headless execution mode,
- migrated tests to JUnit 5 for native Gradle test discovery,
- formatting is enforced by Spotless + Scalafmt,
- CI builds and tests through GitHub Actions.

## Requirements

- JDK 21+ to compile and run the application,
- Gradle 9.8.0 (the build contains a pinned `wrapper` task; run `gradle wrapper` once if you want local wrapper scripts).

Gradle 9.8.0 can itself run on current JDKs including Java 22; compilation is pinned to a JDK 21 toolchain.

## Build

```bash
gradle clean build
```

## Run the ScalaFX visualization

```bash
gradle run
```

## Headless simulation

```bash
gradle runHeadless
```

or pass custom arguments directly to the headless main class:

```bash
gradle classes
gradle -q runHeadless
```

The built-in smoke task uses:

```text
--boids 250 --steps 500 --seed 42
```

The CLI entry point supports:

```text
--boids <positive integer>
--steps <non-negative integer>
--seed <long>
```

## Formatting

Check formatting:

```bash
gradle spotlessCheck
```

Apply formatting:

```bash
gradle spotlessApply
```

## Architecture

```text
                     ┌────────────────────┐
                     │  BoidApplication   │
                     └─────────┬──────────┘
                               │
                     ┌─────────▼──────────┐
                     │ SimulationEngine   │
                     └──────┬───────┬─────┘
                            │       │
                 ┌──────────▼──┐ ┌──▼──────────┐
                 │  Steering   │ │ Boundary    │
                 │  Behavior   │ │ Policy      │
                 └──────┬──────┘ └─────────────┘
                        │
                 ┌──────▼──────────┐
                 │ NeighborSearch  │
                 └─────────────────┘

                     SimulationState
                          │
                ┌─────────┴─────────┐
                │                   │
          Headless runner        Renderer
                                    │
                              ScalaFxRenderer
```

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for details.

## Project structure

```text
src/main/scala/io/codeswarm/boids/
├── app/
│   ├── BoidApplication.scala
│   └── HeadlessApplication.scala
├── behavior/
│   ├── SteeringBehavior.scala
│   ├── SteeringMath.scala
│   ├── Separation.scala
│   ├── Alignment.scala
│   ├── Cohesion.scala
│   └── CompositeSteeringBehavior.scala
├── boundary/
│   ├── BoundaryPolicy.scala
│   └── WrapAroundBoundary.scala
├── math/
│   └── Vector2.scala
├── model/
│   ├── Boid.scala
│   ├── BoidId.scala
│   ├── SimulationConfig.scala
│   └── SimulationState.scala
├── neighbor/
│   ├── NeighborSearch.scala
│   └── NaiveNeighborSearch.scala
├── rendering/
│   ├── Renderer.scala
│   └── scalafx/ScalaFxRenderer.scala
└── simulation/
    ├── SimulationInitializer.scala
    ├── SimulationEngine.scala
    ├── SimulationRunner.scala
    └── SimulationComponents.scala
```

## Algorithm

The three classic Reynolds rules remain unchanged conceptually:

```text
steering =
    separation * separationWeight +
    alignment  * alignmentWeight  +
    cohesion   * cohesionWeight
```

The complete flock is still scanned to find neighbors. The O(n²) behavior is intentional in 0.2 because milestone 0.3
will introduce spatial indexing and JMH benchmarks against this reference implementation.

## Documentation

- [Algorithm](docs/ALGORITHM.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Headless mode](docs/HEADLESS_MODE.md)
- [Roadmap](docs/ROADMAP.md)
- [Release checklist](docs/RELEASE_CHECKLIST.md)
- [Changelog](CHANGELOG.md)

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).
