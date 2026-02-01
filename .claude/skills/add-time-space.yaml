name: add-time-space
description: Add standardized Javadoc time and space complexity comments to LeetCode Java solutions
instructions: |
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
     - For files without inline comments, analyze the code and add Javadoc manually:
       - Single loop: O(N) time
       - Nested loops: O(N²) time
       - Sorting: O(N log N) time
       - Binary search: O(log N) time
       - Backtracking: O(2^N) or O(N!) depending on problem
       - New array of size N: O(N) space
       - Only variables: O(1) space

  4. **Verify Changes**:
     ```bash
     git diff leetcode_java/src/main/java/LeetCodeJava/[DIRECTORY]/
     ```
     - Confirm only comments changed, no logic modified

  5. **Commit Changes**:
     ```bash
     git add leetcode_java/src/main/java/LeetCodeJava/[DIRECTORY]/
     git commit -m "Add time/space complexity Javadoc comments to LeetCode Java [DIRECTORY] solutions"
     ```

  6. **Report Results**:
     - Number of files processed
     - Number of files that needed manual processing
     - Any files skipped and why
     - Git commit hash

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
