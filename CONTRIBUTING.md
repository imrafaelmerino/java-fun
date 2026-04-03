# Contributing to java-fun

Thanks for contributing to `java-fun`.

## Development Requirements

- Java 21+
- Maven 3.6.3+

## Local Test Workflow

- Fast suite (default): `mvn -q test`
- Fast suite + coverage gates: `mvn -q test jacoco:report jacoco:check`
- Statistical/distribution suite only: `mvn -q -Pstats test`
- Statistical suite with verbose diagnostics:
  `mvn -q -Pstats -DJAVAFUN.STATS.VERBOSE=true test`

By default, tests tagged with `@Tag("stats")` are excluded and only run with the `stats` profile.

## Test Naming Conventions

- Test classes end with `Test`.
- Test methods follow `should...When...`.
- Use package-private visibility for JUnit 5 test classes and methods unless there is a strong reason to make them public.

## Statistical Test Conventions

- Mark expensive distribution checks with `@Tag("stats")`.
- Keep sample sizes and tolerances explicit in constants.
- Prefer deterministic seeds in assertions involving generated-value distributions.

## API Evolution Conventions

- Prefer explicit method names over ambiguous aliases.
- When changing public API behavior, update:
  - Javadoc
  - `docs/README.md`
  - `docs/CHANGELOG.md`

## Pull Request Checklist

- [ ] Tests pass locally (`mvn -q test`)
- [ ] If statistical behavior changed, stats suite was executed (`mvn -q -Pstats test`)
- [ ] Javadoc and README examples reflect the current API
- [ ] Changelog updated for user-visible changes
