# pre-commit-jira

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/batovpasha/pre-commit-jira/tree/main.svg?style=shield)](https://dl.circleci.com/status-badge/redirect/gh/batovpasha/pre-commit-jira/tree/main)

Collection of pre-commit hooks for JIRA workflow.

## Prerequisites

- [pre-commit](https://pre-commit.com/)
- [Docker](https://www.docker.com/). Docker daemon should be enabled whenever you make a commit and
  want to use [pre-commit-jira](https://github.com/batovpasha/pre-commit-jira) hooks

## Installation

1. Put the following configuration into your `.pre-commit-config.yaml` file:

    ```yaml
    repos:
      - repo: https://github.com/batovpasha/pre-commit-jira
        rev: v1.0.0
        hooks:
          - id: add-jira-ticket
    ```

2. Update the hooks to latest version:

    ```shell
    pre-commit autoupdate
    ```

3. Install the hooks:

    ```shell
    pre-commit install --hook-type commit-msg
    ```

4. Start using. Example:

    ```shell
    git checkout -b feature/TICKET-123-some-feature
    git commit --allow-empty -m "feat: add new feature"

    # The next command will show the latest commit message
    git log -1 --pretty=%B # => feat: TICKET-123 add new feature
    ```

## Testing

### Testing Prerequisites

- Everything from usage [prerequisites](#prerequisites)
- [Leiningen](https://leiningen.org/)
- [Clojure](https://clojure.org/)

### Unit tests (without coverage)

```shell
lein test
```

### Unit tests (with coverage)

```shell
lein cloverage
# Open the coverage report ./target/coverage/index.html
open ./target/coverage/index.html     # MacOS
xdg-open ./target/coverage/index.html # Linux
```

### End-to-End tests

```shell
./e2e-test.sh || rm -rf /tmp/pre-commit-jira-test
```

## Development

### Development Prerequisites

The same as for [testing](#testing-prerequisites) + need to set up dev pre-commit hooks:

```shell
pre-commit install --install-hooks
```

### Run hooks locally

#### add-jira-ticket

1. (Optional) Edit the commit-msg.txt whatever you want
2. Run the hook:

    ```shell
    pre-commit try-repo . add-jira-ticket --verbose --all-files \
      --commit-msg-filename ./commit-msg.txt \
      --hook-stage commit-msg
    ```

### Lint

```shell
lein clj-kondo
```

### Format

```shell
lein zprint src/**/*.clj test/**/*.clj project.clj
```
