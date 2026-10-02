# Release checklist — 0.2.0

Run from a clean checkout:

```bash
gradle --version
gradle clean spotlessCheck test
gradle runHeadless
gradle assembleDist
```

Manual desktop smoke test:

```bash
gradle run
```

Verify:

- the window title reports 0.2.0,
- boids move and flock,
- separation/alignment/cohesion remain visually plausible,
- screen wrapping works,
- no NaN/Infinity behavior is visible,
- `runHeadless` reports `finite=true`,
- tests and formatting checks are green,
- CI uses only Gradle commands,
- no `build.sbt`, `project/plugins.sbt` or Breeze dependency remains.
