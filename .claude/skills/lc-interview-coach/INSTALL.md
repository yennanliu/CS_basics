# Installing the LC Interview Coach

The skill is four plain markdown files with no dependencies, so any coding agent that can be
given a system prompt can run it. `SKILL.md` is the whole coach; the three files under
`references/` are loaded on demand.

```text
lc-interview-coach/
├── SKILL.md                    # the coach — modes, review loop, output contract
└── references/
    ├── rubric.md               # four signals, level anchors, self-score card
    ├── patterns.md             # cue / invariant / target / classic bug per pattern
    └── talk-track.md           # what to say in the room, phase by phase
```

## Claude Code

Already installed in this repo at `.claude/skills/lc-interview-coach/`. It loads by
description when you ask for a review or a mock, or invoke it by name:

```text
/lc-interview-coach review leetcode_python/Sliding_Window/sliding_window_maximum.py
```

For every repo, copy the directory to `~/.claude/skills/lc-interview-coach/`.

## Claude (claude.ai / desktop)

Settings → Capabilities → Skills → **Upload skill**, with the directory zipped:

```bash
cd .claude/skills && zip -r lc-interview-coach.zip lc-interview-coach
```

The YAML frontmatter (`name`, `description`) is what the model matches against, so leave it
intact.

## Codex

Append a pointer to `AGENTS.md` at the repo root — Codex reads it automatically:

```markdown
## Interview coaching
When asked to review a solution, run a mock interview, teach a pattern, or analyse a
bottleneck, follow `.claude/skills/lc-interview-coach/SKILL.md` and its `references/`.
```

## Gemini CLI

Same shape, in `GEMINI.md`. Or point at it per session:

```bash
gemini -p "Act as the coach defined in .claude/skills/lc-interview-coach/SKILL.md. Review @solution.py"
```

## Cursor / Windsurf / other IDE agents

Add a rule file (`.cursor/rules/interview-coach.mdc`, or the editor's equivalent) whose body
is the pointer above. Keep the pointer, not a copy — one source of truth means a fix to
`SKILL.md` reaches every agent at once.

## Anything else

Paste `SKILL.md` in as the system prompt. It is self-contained; the `references/` files are
optional depth, and the coach works without them (with shallower pattern recall).
