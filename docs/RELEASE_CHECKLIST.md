# Release checklist

## Source quality

- [ ] `gradle spotlessApply`
- [ ] inspect the formatting diff
- [ ] `gradle spotlessCheck`
- [ ] no generated build output is committed

## Build and tests

- [ ] `gradle clean test`
- [ ] `gradle check`
- [ ] neighbor naive/grid equivalence tests pass
- [ ] JavaFX media runtime dependency regression test passes
- [ ] `gradle assembleDist`

## Runtime smoke tests

- [ ] `gradle runHeadless`
- [ ] headless output reports `neighborSearch=grid`
- [ ] `gradle runHeadlessNaive`
- [ ] `gradle run` opens the ScalaFX window
- [ ] no `NoClassDefFoundError` for JavaFX media classes
- [ ] boids move continuously and remain inside the world

## Benchmarks

- [ ] `gradle jmhClasses`
- [ ] controlled-machine release run: `gradle jmh`
- [ ] `benchmark/results/jmh-0.3.0.json` is valid JSON
- [ ] benchmark environment is recorded with release notes when performance numbers are published

## Documentation

- [ ] README matches actual commands and default strategy
- [ ] ARCHITECTURE describes prepared indexes
- [ ] BENCHMARKS describes measurement boundaries
- [ ] ROADMAP is updated
- [ ] CHANGELOG is updated

## Packaging

- [ ] version is `0.3.0`
- [ ] LICENSE and NOTICE are present
- [ ] archive contains no `build/`, `.gradle/` or IDE output
- [ ] archive filename is `reynolds-boids-visualization-0.3.0.zip`
