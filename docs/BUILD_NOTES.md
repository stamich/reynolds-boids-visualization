# Build notes

## Gradle Wrapper bootstrap

Milestone 0.4 must be immediately buildable through `./gradlew`, but the isolated environment used to assemble this source archive had no DNS access and could not fetch Gradle's official `gradle-wrapper.jar`.

The archive therefore contains a small, source-visible bootstrap JAR at `gradle/wrapper/gradle-wrapper.jar`. Its source is retained under `gradle/wrapper/bootstrap-src/`. It performs only these tasks:

1. read `gradle-wrapper.properties`,
2. download the configured Gradle 9.8.0 binary ZIP,
3. verify `distributionSha256Sum`,
4. unpack it under the user's Gradle wrapper cache,
5. launch the downloaded Gradle executable.

On a normal workstation run:

```bash
./gradlew wrapper
```

Gradle then regenerates the conventional official wrapper scripts/JAR. The project build itself does not compile or depend on the bootstrap source.

The configured Gradle 9.8.0 binary distribution checksum is:

```text
bafd5ce9cfaea0fbccfdc8439a1ac42fbd4cd9c89dc9a988228d8a2639a58e6c
```
