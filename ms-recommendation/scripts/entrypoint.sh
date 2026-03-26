#!/usr/bin/env bash
set -euo pipefail

python manage.py consumer_orders & CONSUMER_PID=$!

gunicorn core.wsgi:application \
  --bind 0.0.0.0:${PORT:-8000} \
  --workers ${GUNICORN_WORKERS:-2} \
  --timeout ${GUNICORN_TIMEOUT:-120} \
  --log-level info &
  WEB_PID=$!

terminate() {
  echo "Terminating processes..."
  kill -TERM "$CONSUMER_PID" "$WEB_PID" 2>/dev/null || true
  wait "$CONSUMER_PID" "$WEB_PID" 2>/dev/null || true
}

trap terminate SIGTERM SIGINT

wait -n "$CONSUMER_PID" "$WEB_PID"
EXIT_CODE=$?
echo "Process exited with code $EXIT_CODE"
terminate
exit $EXIT_CODE