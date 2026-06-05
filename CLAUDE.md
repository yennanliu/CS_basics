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

Cheatsheets live in `doc/cheatsheet/`. Use [`doc/cheatsheet/00_template.md`](doc/cheatsheet/00_template.md) as the base structure.

### File Structure

```
# Algorithm Name

## Overview                    ← Key Properties, When to Use, References
## 0) Concept                  ← Types, categories, mental model
### 0-1) Types
### 0-2) Pattern
## 1) General form             ← Templates with code
### 1-1) Basic OP
## 2) LC Example               ← Concrete LeetCode solutions
```

### Formatting Rules

- **Bold** key terms: `**Pattern**`, `**Key Idea**`, `**Recurrence**`
- Category headers: `#### **Category Name**`
- Code blocks: always tag language (`java`, `python`)
- Complexity: inside code as first comment — `// time = O(...), space = O(...)`
- Images: `<p align="center"><img src="../pic/filename.png"></p>`
- Priority markers: `⭐⭐⭐⭐⭐` for critical/frequently-tested patterns

### Code Conventions

- Open with `// IDEA: brief description` (Java) or `# IDEA: ...` (Python)
- Provide both Java and Python implementations when applicable
- Label each block: `// java` / `# python`
- Include `// LC <number> - Problem Name` above the class/function

### Common Section Patterns

| Pattern | Use |
|---------|-----|
| Quick Decision Table | At section start — maps goal → template → examples |
| Template Comparison Table | Side-by-side comparison of loop conditions / update rules |
| Similar Problems Table | Group related LC numbers with key differences |
| Visual Trace | ASCII walkthrough of algorithm steps on a concrete example |
| Decision Matrix | `Minimize vs Maximize`, `Memoization vs Tabulation`, etc. |

### Overview Section (for larger docs)

```markdown
### Key Properties
- **Time Complexity**: O(...)
- **Space Complexity**: O(...)
- **Core Idea**: ...
- **When to Use**: ...

### References
- [Name](url)
```

---

## Adding Time/Space Complexity Javadoc Comments

For the full guide, see [`doc/add-time-space-guide.md`](doc/add-time-space-guide.md). Quick start: `/add-time-space <DirectoryName>`.