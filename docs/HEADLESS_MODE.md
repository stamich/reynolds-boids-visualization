# Headless mode

The headless entry point runs the same deterministic simulation core without JavaFX rendering.

```bash
./gradlew runHeadless
./gradlew runHeadlessNaive
```

Direct execution supports:

```text
--boids <positive-int>
--steps <non-negative-int>
--seed <long>
--neighbor grid|naive
```

Example:

```bash
./gradlew run --args='--boids 1000 --steps 10000 --seed 42 --neighbor grid'
```

The Gradle `run` task is reserved for the GUI main class, so custom headless arguments are normally best executed by adding/using a dedicated `JavaExec` task or invoking the built distribution directly.
