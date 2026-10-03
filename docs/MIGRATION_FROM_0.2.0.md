# Migration from 0.2.0 to 0.3.0

## Runtime dependency fix

0.2.0 declared JavaFX base, graphics and controls but omitted `javafx-media`. The selected ScalaFX version references
`javafx.scene.media.MediaException`, which caused GUI startup to fail with `NoClassDefFoundError`.

0.3.0 adds:

```text
javafx-media
```

and a regression test verifies that the required nested media exception type is present on the runtime classpath.

## NeighborSearch API

0.2.0:

```scala
def neighborsOf(boid: Boid, flock: IndexedSeq[Boid], radius: Double): IndexedSeq[Boid]
```

0.3.0:

```scala
def index(flock: IndexedSeq[Boid], world: WorldConfig): NeighborIndex
```

followed by repeated calls to:

```scala
def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid]
```

This is a source-level API change, appropriate while the project is pre-1.0. It allows expensive preparation work to be
performed once per immutable tick instead of once per boid.

## Default strategy

0.2 used only `NaiveNeighborSearch`.

0.3 keeps it but makes `UniformGridNeighborSearch` the default wiring. Headless runs can select either strategy through
`--neighbor naive|grid`.

## Benchmark source set

0.3 adds `src/jmh/java` and the Gradle JMH plugin. Benchmarks are not production runtime dependencies.
