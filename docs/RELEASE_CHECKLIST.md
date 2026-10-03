# Release checklist

Run from a clean checkout.

```bash
./gradlew --version
./gradlew wrapper
./gradlew spotlessApply
./gradlew spotlessCheck
./gradlew clean test
./gradlew check
./gradlew runHeadless
./gradlew --no-configuration-cache jmhClasses
./gradlew assembleDist
```

Manual GUI smoke test:

```bash
./gradlew run
```

Verify:

- window opens without JavaFX linkage errors,
- Start advances ticks,
- Pause freezes ticks,
- Start resumes,
- Restart returns to deterministic tick zero,
- live sliders/text fields affect behavior,
- boid count/seed show restart-required state,
- boids retain stable individual colors,
- grid overlay toggles correctly,
- metrics update without exceptions.

Before packaging:

- run `./gradlew spotlessCheck`,
- confirm `gradle/wrapper/` is present,
- confirm version is `0.4.0`,
- update README, Changelog and docs,
- ensure benchmark output files are not accidentally committed unless intentionally retained.
