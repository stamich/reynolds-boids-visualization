# Headless mode

Milestone 0.2 can execute the complete flocking model without initializing JavaFX.

## Purpose

Headless execution supports:

- deterministic regression tests,
- CI smoke tests,
- future benchmarks,
- data/metric generation without rendering overhead.

## Gradle smoke task

```bash
./gradlew runHeadless
```

The task executes 250 boids for 500 ticks with seed 42.

## Output

The application prints:

```text
tick=500
boids=250
averageSpeed=<value>
finite=true
```

No JavaFX window or graphics toolkit is initialized.
