# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

CS_basics is a comprehensive computer science fundamentals repository containing algorithmic problems, data structures, system design patterns, and interview preparation materials. The codebase spans multiple programming languages and focuses on LeetCode problems, system design, and CS concepts.

## Directory Structure

- `leetcode_java/` - Java implementations of LeetCode problems (~508 files)
  - Maven project with JUnit testing setup
  - Organized into: AlgorithmJava, DataStructure, dev, LeetCodeJava packages
- `leetcode_python/` - Python implementations of LeetCode problems (~826 files)
  - Organized by algorithm patterns (Array, Backtracking, Binary_Search, etc.)
- `leetcode_SQL/` - SQL query solutions (~166 files)
- `leetcode_scala/` - Scala implementations
- `algorithm/` - Algorithm implementations across multiple languages (C, Java, JS, Python, SQL)
- `data_structure/` - Data structure implementations (Java, JS, Python, Scala)
- `system_design/` - System design patterns, templates, and case studies
- `doc/` - Documentation, cheat sheets, interview resources, and study materials
- `ref_code/` - Reference code examples
- `script/` - Utility scripts
- `site/` - GitHub Pages build tooling
  - `build-site.js` - Builds HTML pages from markdown docs
  - `build-leetcode.js` - Generates LeetCode JSON data for the LC Explorer
  - `style.css` - Site stylesheet
  - `package.json` / `package-lock.json` - Node.js dependencies (markdown-it, highlight.js)
  - Run scripts from the project root: `node site/build-site.js`

## Build and Test Commands

### Java (leetcode_java/)
```bash
# Build (if Maven is available)
cd leetcode_java
mvn compile

# Run tests (if Maven is available)
mvn test

# Run specific test class
mvn test -Dtest=ClassName
```

Note: Maven may not be available in all environments. The project uses Java 8 compatibility with JUnit 5 for testing.

### Python
No specific build requirements. Python files can be executed directly:
```bash
python3 path/to/solution.py
```

## Code Organization Patterns

### LeetCode Problems
- **Java**: Problems are organized into packages by algorithm type (AlgorithmJava, DataStructure, LeetCodeJava)
- **Python**: Problems are organized into directories by algorithm patterns (Array, Backtracking, Binary_Search, etc.)
- **SQL**: Query solutions organized by problem number/type

### System Design
- Template-based approach with `00_template.md` as the base structure
- Real-world case studies (Netflix, Twitter, Uber, etc.)
- Design patterns organized by system type

## Key Resources and References

The repository extensively references:
- LeetCode problem classifications and patterns
- Algorithm complexity charts and Big-O references
- Interview preparation materials (Blind 75, Grind 75, Grind 169)
- System design fundamentals and case studies

## Utility Scripts

See [`doc/utility-scripts.md`](doc/utility-scripts.md) for full usage of all scripts in `script/`.

## Development Notes

- Code follows language-specific conventions
- Problems often include multiple solution approaches
- System design includes both theoretical concepts and practical implementations
- Documentation emphasizes interview preparation and pattern recognition
- Use `data/progress.md` to track daily practice progress with AI-suggested related problems

---

## Cheatsheet Style Guide

Cheatsheets live in `doc/cheatsheet/`. [`doc/cheatsheet/00_template.md`](doc/cheatsheet/00_template.md) is the authoritative structure — read it before creating or restructuring a cheatsheet.

### Which skeleton

Two skeletons are in use. **Pick by size, not by topic**, and never mix them in one file:

| | **Skeleton A — short doc** | **Skeleton B — reference doc** |
|---|---|---|
| Use when | < ~800 lines; one technique | > ~800 lines; a data structure or pattern family |
| Shape | `0) Concept` → `1) General form` → `2) LC Example` | `Overview` → `Problem Categories` → `Templates & Algorithms` → `LC Examples` → `Problems by Pattern` → `Pattern Selection Strategy` → `Summary` |
| Examples | `kadane_algorithm.md`, `n_sum.md`, `iterator.md` | `heap.md`, `dp.md`, `bfs.md`, `graph.md` |

If a Skeleton A doc grows past ~800 lines, convert it to B rather than appending B's sections to the end.

### Mandatory header

Every cheatsheet opens with the H1, then a **Scope** block, then `## LeetCode Problem Lists`:

```markdown
# <Topic Name>

> **Scope** — <what this file owns, and what it deliberately does not>.
> **See also**: [other.md](./other.md) — <why you'd go there>.
```

The Scope line is required whenever another cheatsheet covers adjacent ground. It is what stops two files from silently growing into the same doc.

### Formatting Rules

- **Bold** key terms: `**Pattern**`, `**Key Idea**`, `**Recurrence**`
- Category headers: `#### **Category Name**`
- Code blocks: **always** tag the language — `java`, `python`, or `text` for ASCII traces, diagrams and program output. Never a bare ` ``` `.
- Complexity: inside code as first comment — `// time = O(...), space = O(...)`
- Images: `<p align="center"><img src="../pic/filename.png"></p>`
- Priority markers: `⭐⭐⭐⭐⭐` for critical/frequently-tested patterns
- Heading levels never skip (`h2` → `h3`, never `h2` → `h4`)
- State each LC number **once** per heading — not `... (LC 347) — LC 347`
- Complexity is stated **once** in the header: either the `## Time Complexity` table *or* a Key Properties bullet, never both

### Code Conventions

- Open with `// IDEA: brief description` (Java) or `# IDEA: ...` (Python)
- Provide both Java and Python implementations when applicable
- Label each block: `// java` / `# python`
- Include `// LC <number> - Problem Name` above the class/function
- **One canonical solution per problem.** A second variant needs a stated reason (different complexity, different language idiom, distinct trick) — not just a different spelling of the same loop.

### Anti-patterns (these caused the Aug 2026 cleanup — see [`doc/cheatsheet-review-2026-08.md`](doc/cheatsheet-review-2026-08.md))

- ❌ An `LC Examples` section appended to the end that re-solves problems already solved by templates above
- ❌ Catch-all sections (`Missing Google Patterns`) instead of filing new material under the pattern it belongs to
- ❌ Duplicate heading text under the same parent (`Summary`, `Core Idea`) — qualify them
- ❌ Splitting one topic across two files without a Scope line saying which owns what

### Common Section Patterns

| Pattern | Use |
|---------|-----|
| Quick Decision Table | At section start — maps goal → template → examples |
| Template Comparison Table | Side-by-side comparison of loop conditions / update rules |
| Similar Problems Table | Group related LC numbers with key differences |
| Visual Trace | ASCII walkthrough of algorithm steps (tag it ` ```text `) |
| Decision Matrix | `Minimize vs Maximize`, `Memoization vs Tabulation`, etc. |

### Overview Section (for larger docs)

```markdown
### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
- **Core Idea**: ...
- **When to Use**: ...

### References
- [Name](url)
```

---

## Adding Time/Space Complexity Javadoc Comments

For the full guide, see [`doc/add-time-space-guide.md`](doc/add-time-space-guide.md). Quick start: `/add-time-space <DirectoryName>`.