#!/bin/bash

set -e

PRE_COMMIT_JIRA_VERSION="v0.1.0"

mkdir -p /tmp/pre-commit-jira-test
cd /tmp/pre-commit-jira-test

# git init > /dev/null 2>&1
git init

cat > .pre-commit-config.yaml <<EOF
repos:
  - repo: https://github.com/batovpasha/pre-commit-jira
    rev: ${PRE_COMMIT_JIRA_VERSION}
    hooks:
      - id: add-jira-ticket
EOF
# pre-commit install --hook-type commit-msg > /dev/null 2>&1
pre-commit install --hook-type commit-msg

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
# git checkout -b feature/TICKET-123-some-feature > /dev/null 2>&1
git checkout -b feature/TICKET-123-some-feature
# git commit --allow-empty -m "feat: add new feature" > /dev/null 2>&1
git commit --allow-empty -m "feat: add new feature"
COMMIT_MSG=$(git log -1 --pretty=%B)
assert_equals "feat: TICKET-123 add new feature" "$COMMIT_MSG" "should prepend JIRA ticket from branch to commit message"

rm -rf /tmp/pre-commit-jira-test