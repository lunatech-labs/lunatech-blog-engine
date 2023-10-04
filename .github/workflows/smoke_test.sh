#!/usr/bin/env bash
set -euo pipefail

TRY_COUNT=30
sbt stage

cd target/universal/stage

APPLICATION_SECRET="9w/DBOjdTE?65hO5IzGu_XL5^AiK^>iUZgIF?mYVUbPNjNU[ver>zw[V@zlbb0wu" access_token=$GITHUB_TOKEN bin/lunatech-blog-engine &

PID=$!
trap "kill $! 2> /dev/null" EXIT

is_running() {
  kill -0 $PID 2> /dev/null
}

is_responding() {
  curl localhost:9000 -s -o /dev/null
}

for (( count=0; count < $TRY_COUNT; count++ )); do
  if ! is_running; then
    echo "::error::The application stopped before it could serve a page."
    exit 1
  fi

  if is_responding; then
    exit 0
  fi

  sleep 3
done

echo "::error::Timeout: the application did not serve a page in time."
exit 2
