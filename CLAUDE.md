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

## Development Notes

- Code follows language-specific conventions
- Problems often include multiple solution approaches
- System design includes both theoretical concepts and practical implementations
- Documentation emphasizes interview preparation and pattern recognition