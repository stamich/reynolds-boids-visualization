# Headless mode

The simulation core does not require JavaFX.

The standard smoke task uses the uniform-grid strategy:

```bash
gradle runHeadless
```

The matching naive baseline task is:

```bash
gradle runHeadlessNaive
```

Configured arguments are:

```text
--boids 250 --steps 500 --seed 42 --neighbor grid
```

Supported strategies:

```text
naive
  Reference full-flock linear scan.

grid
  Uniform-grid spatial index; default in milestone 0.3.
```

Headless output includes:

```text
tick
boids
neighborSearch
averageSpeed
finite
```

A fixed seed produces reproducible initialization and deterministic execution for the same strategy and configuration.
