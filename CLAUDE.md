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

---

## Adding Time/Space Complexity Javadoc Comments

### Quick Start (Using Skill)

The easiest way to add complexity comments is using the `/add-time-space` skill:

```bash
/add-time-space BinarySearch
/add-time-space DynamicProgramming
```

The skill is located at `.claude/skills/add-time-space.yaml` and automates the entire process including:
- Running the Python script
- Manual processing if needed
- Verification
- Git commit

### Overview

A standardized process for adding time and space complexity documentation to Java LeetCode solutions. This transforms inline complexity comments into proper Javadoc format for better documentation and IDE integration.

### Transformation Pattern

**Before:**
```java
// V0
// IDEA: Two Pointers
// time: O(N), space: O(1)
public int[] twoSum(int[] nums, int target) {
```

**After:**
```java
// V0
// IDEA: Two Pointers
/**
 * time = O(N)
 * space = O(1)
 */
public int[] twoSum(int[] nums, int target) {
```

### Automated Script

Create a Python script for batch processing files with inline complexity comments:

```python
#!/usr/bin/env python3
import re
import sys
import os

def add_javadoc_complexity(file_path):
    """Add Javadoc complexity comments to Java methods."""

    with open(file_path, 'r') as f:
        content = f.read()

    original_content = content

    # Pattern to match inline complexity comments
    pattern = r'(\s*)(//[^\n]*\n)(\s*)(//\s*time:\s*O\([^)]+\),?\s*space:\s*O\([^)]+\))\s*\n(\s*public\s+)'

    def replacement(match):
        indent = match.group(1)
        comment_line = match.group(2)
        next_indent = match.group(3)
        complexity_line = match.group(4)
        public_keyword = match.group(5)

        # Extract time and space complexity
        time_match = re.search(r'time:\s*O\(([^)]+)\)', complexity_line)
        space_match = re.search(r'space:\s*O\(([^)]+)\)', complexity_line)

        if time_match and space_match:
            time_complexity = time_match.group(1)
            space_complexity = space_match.group(1)

            javadoc = f'''{indent}{comment_line}{next_indent}/**
{next_indent} * time = O({time_complexity})
{next_indent} * space = O({space_complexity})
{next_indent} */
{next_indent}{public_keyword}'''
            return javadoc

        return match.group(0)

    # Apply the replacement
    content = re.sub(pattern, replacement, content)

    # Only write if content changed
    if content != original_content:
        with open(file_path, 'w') as f:
            f.write(content)
        return True
    return False

if __name__ == "__main__":
    files_to_process = sys.argv[1:]

    modified_count = 0
    for file_path in files_to_process:
        if os.path.exists(file_path):
            if add_javadoc_complexity(file_path):
                print(f"Modified: {os.path.basename(file_path)}")
                modified_count += 1

    print(f"\nTotal files modified: {modified_count}")
```

### Usage Instructions

#### 1. Using the Skill (Recommended)

```bash
/add-time-space BinarySearch
/add-time-space DynamicProgramming
```

#### 2. Process a Single Directory (Manual)

```bash
# The script is located at script/add_javadoc_complexity.py

# Process all Java files in a directory
python3 script/add_javadoc_complexity.py \
  leetcode_java/src/main/java/LeetCodeJava/Array/*.java

# Or specific files
python3 script/add_javadoc_complexity.py \
  leetcode_java/src/main/java/LeetCodeJava/Array/TwoSum.java \
  leetcode_java/src/main/java/LeetCodeJava/Array/ThreeSum.java
```

#### 2. Manual Processing for Files Without Inline Comments

For files that don't have the `// time: O(...), space: O(...)` pattern, manually analyze and add:

```java
// V0
// IDEA: HashMap
/**
 * time = O(N)
 * space = O(N)
 */
public int[] twoSum(int[] nums, int target) {
```

**Common Complexity Patterns:**

| Algorithm Type | Time Complexity | Space Complexity |
|----------------|----------------|------------------|
| Single Loop | O(N) | O(1) |
| Nested Loops | O(N²) | O(1) |
| Sorting | O(N log N) | O(1) or O(N) |
| Binary Search | O(log N) | O(1) |
| HashMap/HashSet | O(N) | O(N) |
| Backtracking (Permutations) | O(N!) | O(N) |
| Backtracking (Subsets) | O(N * 2^N) | O(N * 2^N) |
| DFS/BFS | O(V + E) | O(V) |

#### 3. Commit Changes in Batches

```bash
# Process files in batches and commit
git add leetcode_java/src/main/java/LeetCodeJava/Array/*.java
git commit -m "Add time/space complexity Javadoc comments to Array solutions (Batch 1)

- Converted inline comments to Javadoc format
- Files: TwoSum.java, ThreeSum.java, ...

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>"
```

### Process Summary (Completed Examples)

#### Array Directory (57 files)
- **Total files**: 57
- **Processed**: 56 files (1 file was commented-out code)
- **Batches**: 9 batches
- **Methods**: ~300+ methods documented

#### BackTrack Directory (18 files)
- **Total files**: 18
- **Processed**: 18 files
- **Batches**: 1 batch
- **Methods**: ~80+ methods documented

### Verification Steps

After processing, verify the changes:

```bash
# 1. Check git diff
git diff leetcode_java/src/main/java/LeetCodeJava/Array/

# 2. Count files modified
git status --short leetcode_java/src/main/java/LeetCodeJava/Array/*.java | wc -l

# 3. Sample a few files to verify formatting
head -50 leetcode_java/src/main/java/LeetCodeJava/Array/TwoSum.java

# 4. Compile if Maven is available (optional)
cd leetcode_java && mvn compile -q
```

### Creating a Reusable Command/Skill

To make this repeatable, create a shell script:

```bash
#!/bin/bash
# add_complexity_docs.sh - Add Javadoc complexity comments to Java files

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PYTHON_SCRIPT="/tmp/add_javadoc_complexity.py"

# Create Python script if it doesn't exist
if [ ! -f "$PYTHON_SCRIPT" ]; then
    cat > "$PYTHON_SCRIPT" << 'PYEOF'
#!/usr/bin/env python3
import re
import sys
import os

def add_javadoc_complexity(file_path):
    """Add Javadoc complexity comments to Java methods."""

    with open(file_path, 'r') as f:
        content = f.read()

    original_content = content

    # Pattern to match inline complexity comments
    pattern = r'(\s*)(//[^\n]*\n)(\s*)(//\s*time:\s*O\([^)]+\),?\s*space:\s*O\([^)]+\))\s*\n(\s*public\s+)'

    def replacement(match):
        indent = match.group(1)
        comment_line = match.group(2)
        next_indent = match.group(3)
        complexity_line = match.group(4)
        public_keyword = match.group(5)

        # Extract time and space complexity
        time_match = re.search(r'time:\s*O\(([^)]+)\)', complexity_line)
        space_match = re.search(r'space:\s*O\(([^)]+)\)', complexity_line)

        if time_match and space_match:
            time_complexity = time_match.group(1)
            space_complexity = space_match.group(1)

            javadoc = f'''{indent}{comment_line}{next_indent}/**
{next_indent} * time = O({time_complexity})
{next_indent} * space = O({space_complexity})
{next_indent} */
{next_indent}{public_keyword}'''
            return javadoc

        return match.group(0)

    # Apply the replacement
    content = re.sub(pattern, replacement, content)

    # Only write if content changed
    if content != original_content:
        with open(file_path, 'w') as f:
            f.write(content)
        return True
    return False

if __name__ == "__main__":
    files_to_process = sys.argv[1:]

    modified_count = 0
    for file_path in files_to_process:
        if os.path.exists(file_path):
            if add_javadoc_complexity(file_path):
                print(f"Modified: {os.path.basename(file_path)}")
                modified_count += 1

    print(f"\nTotal files modified: {modified_count}")
PYEOF
    chmod +x "$PYTHON_SCRIPT"
fi

# Usage
if [ $# -eq 0 ]; then
    echo "Usage: $0 <directory_name>"
    echo "Example: $0 Array"
    echo "Example: $0 BackTrack"
    exit 1
fi

DIRECTORY="$1"
TARGET_PATH="leetcode_java/src/main/java/LeetCodeJava/$DIRECTORY"

if [ ! -d "$TARGET_PATH" ]; then
    echo "Error: Directory $TARGET_PATH does not exist"
    exit 1
fi

echo "Processing $DIRECTORY directory..."
python3 "$PYTHON_SCRIPT" "$TARGET_PATH"/*.java

echo ""
echo "Review changes with: git diff $TARGET_PATH/"
echo "Commit with: git add $TARGET_PATH/*.java && git commit -m 'Add complexity docs to $DIRECTORY'"
```

**Usage:**
```bash
chmod +x script/add_complexity_docs.sh
./script/add_complexity_docs.sh Array
./script/add_complexity_docs.sh BackTrack
./script/add_complexity_docs.sh BinarySearch
```

### Future Directories to Process

- `LeetCodeJava/BinarySearch/`
- `LeetCodeJava/BinaryTree/`
- `LeetCodeJava/DataStructure/`
- `LeetCodeJava/DynamicProgramming/`
- `LeetCodeJava/Graph/`
- `LeetCodeJava/LinkedList/`
- `LeetCodeJava/String/`
- `LeetCodeJava/TwoPointers/`
- And others as needed

### Notes

- Always review automated changes before committing
- Some files may need manual adjustment for complex complexity expressions
- Merge complexity at the top of existing Javadoc blocks
- Use batched commits for better git history organization