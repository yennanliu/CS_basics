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

## Utility Scripts

The `script/` directory contains utility scripts for managing and analyzing LeetCode practice data:

### categorize_lc_by_type.py
Categorizes LeetCode problems from `data/progress.md` by problem type (Array, DP, Graph, etc.) based on the directory structure in `leetcode_python/` and `leetcode_java/`.

**Usage:**
```bash
# Default: 2025-2026
python3 script/categorize_lc_by_type.py

# Specify custom year range
python3 script/categorize_lc_by_type.py 2024 2025
```

**Output:**
- Generates `data/LC_Practice_{year_start}_{year_end}_By_Category.md`
- Includes table of contents, problem counts per category, and summary statistics
- Categorizes ~80%+ of problems based on existing codebase structure

### Other Scripts
- `get_again_problems.sh` - Extract problems to review again
- `get_company_LC.sh` - Get company-specific LeetCode problems
- `get_lc_per_rating.py` - Filter problems by difficulty rating
- `get_must_problems.sh` - Extract must-do problems
- `get_review_list.py` - Generate review lists
- `list_leetcode_solutions_by_type.sh` - List solutions by type

## Development Notes

- Code follows language-specific conventions
- Problems often include multiple solution approaches
- System design includes both theoretical concepts and practical implementations
- Documentation emphasizes interview preparation and pattern recognition
- Use `data/progress.md` to track daily practice progress with AI-suggested related problems