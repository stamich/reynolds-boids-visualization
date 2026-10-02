# Reynolds Boids Visualization

A two-dimensional Scala/ScalaFX visualization of Craig Reynolds' classic boids flocking model.

Milestone **0.1.1** is a stabilization release based on the original 0.1 implementation. It keeps the classic three-rule simulation intact while modernizing the build, documenting the code, strengthening tests and moving CI to GitHub Actions.

## Features

- classic Reynolds flocking rules:
  - **separation** — avoid crowding nearby boids,
  - **alignment** — steer toward the average local heading,
  - **cohesion** — steer toward the local center of mass,
- toroidal screen wrapping,
- ScalaFX visualization,
- centralized simulation parameters,
- deterministic factory support for tests,
- ScalaTest regression and behavior tests,
- Scalafmt formatting checks,
- GitHub Actions CI.

## Technology

- JDK 21
- Scala 2.13.18
- ScalaFX 21.0.0-R32
- JavaFX 21.0.8
- Breeze 2.1.0
- ScalaTest 3.2.19
- sbt 1.11.7

ScalaFX remains the UI layer and Breeze remains the vector representation in 0.1.1. Replacing Breeze with a lightweight `Vector2`, separating the renderer from the simulation engine and introducing a `SpatialIndex` are intentionally postponed to later milestones so 0.1.1 remains a reliable baseline.

## Running

Requirements:

- JDK 21
- sbt

```bash
sbt clean test
sbt run
```

## Formatting

Check formatting:

```bash
sbt scalafmtCheckAll scalafmtSbtCheck
```

Format the project:

```bash
sbt scalafmtAll scalafmtSbt
```

## Project structure

```text
src/main/scala/io/codeswarm/boids/
├── app/
│   └── BoidSimulation.scala
├── model/
│   ├── Boid.scala
│   └── SimulationConfig.scala
├── simulation/
│   ├── BoidBehavior.scala
│   ├── BoidFactory.scala
│   └── BoidSimulationStep.scala
└── util/
    └── VectorOperations.scala
```

The 0.1.1 structure is deliberately compact. The simulation and renderer are not yet split into separate modules because that refactor is planned for 0.2.

## Algorithm

For each boid, the simulation computes three steering contributions:

```text
steering =
    separation * separationWeight +
    alignment  * alignmentWeight +
    cohesion   * cohesionWeight
```

The current implementation scans the complete flock when finding neighbors. That gives a simple and easy-to-review **O(n²)** baseline that later spatial-index implementations can be benchmarked against.

See [docs/ALGORITHM.md](docs/ALGORITHM.md) for details.

## Documentation

- [Algorithm](docs/ALGORITHM.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Release checklist](docs/RELEASE_CHECKLIST.md)
- [Implementation tasks](docs/IMPLEMENTATION_TASKS.md)
- [Changelog](CHANGELOG.md)

## CI

`.github/workflows/ci.yml` checks formatting, compiles the code, runs the test suite and builds the package on JDK 21.

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).
