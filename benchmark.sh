#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"
./gradlew spotlessCheck test
./gradlew --no-configuration-cache jmh
printf 'JMH JSON: %s\n' "$ROOT/benchmark/results/jmh-0.4.0.json"
