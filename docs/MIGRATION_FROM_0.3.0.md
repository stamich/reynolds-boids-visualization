# Migration from 0.3.0 to 0.4.0

## Build

0.3 required an external Gradle installation when wrapper files were missing. 0.4 ships `gradlew`, `gradlew.bat` and `gradle/wrapper/*`. Use `./gradlew ...` in documentation, CI and local workflows.

## Desktop application

0.3 advanced continuously from application startup. 0.4 starts in `Ready` and advances only after Start. Pause freezes the current snapshot; Restart deterministically reinitializes it.

## Rendering

0.3 used one global boid color. 0.4 uses renderer-level `BoidColorStrategy`; default colors are deterministic by `BoidId`.

## Metrics

0.4 adds optional spatial instrumentation and runtime frame/step metrics. Benchmark fixtures keep instrumentation disabled.

## New benchmark dimensions

- fixed-density scaling,
- cell-size sensitivity,
- GC/allocation profiling.
