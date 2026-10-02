# Release Checklist — 0.1.1

## Build

- [ ] `sbt clean compile`
- [ ] `sbt test`
- [ ] `sbt scalafmtCheckAll scalafmtSbtCheck`
- [ ] `sbt package`

## Manual smoke test

- [ ] `sbt run` opens the ScalaFX window
- [ ] boids are visible
- [ ] boids move continuously
- [ ] flocking behavior is visible
- [ ] boids wrap correctly at every edge
- [ ] application closes cleanly

## Documentation

- [ ] README describes version 0.1.1
- [ ] ARCHITECTURE documents the actual 0.1.1 structure
- [ ] ALGORITHM documents the three Reynolds rules
- [ ] ROADMAP separates future work from the stabilization release
- [ ] CHANGELOG contains 0.1 and 0.1.1 entries
- [ ] LICENSE contains Apache License 2.0

## CI

- [ ] GitHub Actions workflow passes on the default branch
