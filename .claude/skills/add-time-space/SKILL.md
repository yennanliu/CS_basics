---
name: add-time-space
description: Add standardized Javadoc time and space complexity comments to LeetCode Java solutions. Use when a directory under leetcode_java/ needs its `time = O(...)` / `space = O(...)` annotations added, normalised from inline comments, or filled in by hand where the script could not infer them.
allowed-tools: Read, Glob, Grep, Edit, Bash
---

# Add Time/Space Complexity

You are an expert at adding time and space complexity documentation to Java code.

When the user runs `/add-time-space [DIRECTORY]`, follow these steps:

1. **Validate Input**:
   - Directory should be a subdirectory name under `leetcode_java/src/main/java/LeetCodeJava/`
   - Common directories: Array, BackTrack, BinarySearch, BinaryTree, DataStructure, DynamicProgramming, Graph, LinkedList, String, TwoPointers
   - If no directory specified, ask the user which directory to process

2. **Run the Python Script**:
   ```bash
   python3 script/add_javadoc_complexity.py leetcode_java/src/main/java/LeetCodeJava/[DIRECTORY]/*.java
   ```

3. **Manual Processing (if needed)**:
   - Check for any files that weren't processed automatically
   - For files without inline comments, work out the complexity from the code and add the
     Javadoc by hand. Name the line that sets each bound, and count the recursion stack
     and the output as space where they apply.

4. **Verify Changes**:
   ```bash
   git diff leetcode_java/src/main/java/LeetCodeJava/[DIRECTORY]/
   ```
   - Confirm only comments changed, no logic modified

5. **Stop at the diff.** Don't commit or push unless asked.

6. **Report Results**:
   - Number of files processed
   - Number of files that needed manual processing
   - Any files skipped and why
   - The `git diff --stat` for the directory

**Transformation Pattern**:
```java
// BEFORE:
// time: O(N), space: O(1)
public int method() {

// AFTER:
/**
 * time = O(N)
 * space = O(1)
 */
public int method() {
```

**Important**:
- NEVER modify actual code logic, only add/update comments
- Preserve all existing IDEA comments and problem descriptions
- If existing Javadoc exists, merge complexity at the top
- Use format `time = O(...)` and `space = O(...)` (equals sign, not colon)
