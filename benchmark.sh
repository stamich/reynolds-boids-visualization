#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

if [[ -x ./gradlew ]]; then
  GRADLE=(./gradlew)
elif command -v gradle >/dev/null 2>&1; then
  GRADLE=(gradle)
else
  echo "Gradle is required. Generate the wrapper with: gradle wrapper" >&2
  exit 1
fi

mkdir -p benchmark/results
"${GRADLE[@]}" --no-daemon spotlessCheck test
"${GRADLE[@]}" --no-daemon --no-configuration-cache jmh

echo "JMH results: benchmark/results/jmh-0.3.0.json"
