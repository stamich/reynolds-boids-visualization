#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"
./gradlew --no-configuration-cache jmhJar
JAR="$(find build/libs -maxdepth 1 -name '*-jmh.jar' -print -quit)"
if [[ -z "$JAR" ]]; then
  echo "JMH jar not found" >&2
  exit 1
fi
java -jar "$JAR" 'SimulationStepBenchmark|FixedDensityBenchmark' -prof gc -rf json -rff benchmark/results/jmh-0.4.0-gc.json
printf 'GC JMH JSON: %s\n' "$ROOT/benchmark/results/jmh-0.4.0-gc.json"
