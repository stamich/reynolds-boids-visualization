# Benchmarks — Milestone 0.3.0

## Why JMH

JMH is used because JVM microbenchmarks are easily distorted by warmup, JIT compilation, dead-code elimination and
fork-specific state. JMH provides controlled warmup, measurement iterations and separate JVM forks.

The Gradle build uses `me.champeau.jmh` 0.7.3 and JMH 1.37.

## Run

```bash
gradle --no-configuration-cache jmh
```

or:

```bash
./benchmark.sh
```

The JSON result file is:

```text
benchmark/results/jmh-0.3.0.json
```

The benchmark contract is documented in:

```text
benchmark/benchmark-contract-0.3.0.json
```

## Benchmark families

### NeighborSearchBenchmark

This benchmark prepares both indexes during JMH setup and then queries every boid. It isolates repeated query cost from
index-construction cost.

Measured methods:

```text
naiveQueries
uniformGridQueries
```

### SimulationStepBenchmark

This benchmark executes one complete immutable simulation tick. It includes:

- building the selected index once,
- all neighborhood queries,
- separation/alignment/cohesion,
- integration and boundary handling,
- creation of the next immutable state.

Measured methods:

```text
naiveStep
uniformGridStep
```

## Workload sizes

Default parameter values:

```text
100
1000
5000 boids
```

Use identical machine/JVM settings when comparing results across commits or milestones.

## Default harness configuration

```text
warmup iterations: 3
measurement iterations: 5
warmup time: 1 s
measurement time: 1 s
forks: 2
result format: JSON
```

## Interpretation

Do not compare a single cold run or IDE timing against JMH results. For regression analysis, compare:

- the same JDK major/update when possible,
- the same CPU governor/power profile,
- the same boid counts,
- the same world and perception configuration,
- the same benchmark method and JMH settings.

The query benchmark intentionally excludes grid-build time. The full-step benchmark includes it. Both are needed to
understand the tradeoff.

## CI

CI compiles the JMH source set through a dedicated `jmhClasses` step with configuration cache disabled, but it does not treat noisy shared-runner timing as a performance gate. Performance runs should be executed on controlled hardware.
