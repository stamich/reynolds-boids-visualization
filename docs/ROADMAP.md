# Roadmap

## 0.1
Original visualization baseline.

## 0.1.1
Stabilization, tests, documentation and licensing.

## 0.2
Clean simulation architecture, own `Vector2`, Gradle, headless mode and renderer abstraction.

## 0.3
Uniform-grid spatial indexing, naive/grid equivalence and JMH JSON benchmarks.

## 0.4 — current
Interactive simulation laboratory: complete wrapper, Start/Pause/Restart, live parameter tuning, colorful boids, metrics, grid overlay, fixed-density benchmarks and allocation profiling.

## 0.5 — proposed
Optimize the next measured bottleneck using 0.4 allocation/density evidence. Candidates include reduced temporary collections, reusable buffers and internal array/SoA representations while preserving the public immutable model.

## 0.6+
Advanced steering behaviors, selected-boid diagnostics, obstacles/predators, optional parallelism and larger-scale experiments. QuadTree is added only if measured workloads justify it.
