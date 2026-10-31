# CLAUDE.md

Guidance for Claude Code / agents working in **DataPipeline-Demos**.

## Canonical guidance

The rules for this repository live in one file,
[`.github/copilot-instructions.md`](.github/copilot-instructions.md): GitHub
Copilot reads it directly, and Claude Code loads it together with this file
through the import below, so a rule written there once reaches both. Keep this
file for guidance that only applies to Claude.

@.github/copilot-instructions.md

## Pull request reviews

When a session reviews a pull request, rename it `PR #<number> review (<PR title>)`
as soon as the PR's title is known, for example
`PR #1166 review (DP-6102-Design and implement row filtering)`; the title already
starts with the Jira ticket. In the Claude desktop app, use the `set_session_title`
session tool; the title is what the sidebar shows, so this is how review sessions
are told apart.
