#!/bin/bash

set -e

if ! command -v pre-commit > /dev/null 2>&1; then
  echo "\033[0;31m" \
    "Please, install pre-commit to run e2e tests:" \
    "https://pre-commit.com/"

  exit 1
fi

PRE_COMMIT_JIRA_VERSION="v0.1.0"
TMP_TEST_DIR="/tmp/pre-commit-jira-test"

mkdir -p "${TMP_TEST_DIR}"
cd "${TMP_TEST_DIR}"

git init >/dev/null

cat >.pre-commit-config.yaml <<EOF
repos:
  - repo: https://github.com/batovpasha/pre-commit-jira
    rev: ${PRE_COMMIT_JIRA_VERSION}
    hooks:
      - id: add-jira-ticket
EOF
pre-commit install --hook-type commit-msg >/dev/null

assert_equals() {
  local expected="$1"
  local actual="$2"
  local message="$3"

  if [ "$expected" == "$actual" ]; then
    echo "PASS: $message"
  else
    echo "FAIL: $message - Expected: '$expected', Got: '$actual'"
    exit 1
  fi
}

# Test cases
TEST_CASE_1="should prepend JIRA ticket from branch to commit message"
git checkout -b feature/TICKET-123-some-feature >/dev/null
git commit --allow-empty -m "feat: add new feature" >/dev/null
COMMIT_MSG=$(git log -1 --pretty=%B)
assert_equals "feat: TICKET-123 add new feature" "${COMMIT_MSG}" "${TEST_CASE_1}"

rm -rf "${TMP_TEST_DIR}"
