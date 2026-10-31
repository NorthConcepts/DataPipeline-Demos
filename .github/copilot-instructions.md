# Copilot instructions for DataPipeline-Demos

Instructions for GitHub Copilot, Claude Code and other agents working in **DataPipeline-Demos**:
the public repository of demo applications built on the DataPipeline library. Each top-level
folder is a self-contained Maven or Gradle project (`README.md` lists them); there is no shared
build.

## Coding conventions

- Every field and method declares `public`, `protected` or `private`, never package-private (no
  modifier), not even for a helper that a sibling class or a test needs: make it `public`, or
  `protected` when only subclasses use it. In an enum, a method that the constant bodies call
  must be `protected` or `public`, because a `private` one is not reachable from a constant body.
- Keep each demo minimal and readable; it is published as example code.

## Change discipline

- Branch names, commit messages, and pull request titles start with the Jira ticket
  (e.g. `DP-6048 Update demos to JDK 21`).
- Keep commits small and easy to review; do not reformat unrelated files.
