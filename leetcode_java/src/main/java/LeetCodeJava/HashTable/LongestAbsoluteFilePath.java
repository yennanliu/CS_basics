package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-absolute-file-path/

import java.util.*;

/**
 *  388. Longest Absolute File Path
 *  Medium
 *
 *  Suppose we have a file system that stores both files and directories,
 *  serialized as a single string where '\n' separates entries and the number of
 *  leading '\t' gives the depth, e.g.
 *  "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext"
 *
 *  Return the length of the longest absolute path to a FILE in the abstracted
 *  file system (directories joined by '/'). If there is no file, return 0.
 *
 *  Example 1:
 *  Input: input = "dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"
 *  Output: 20   ("dir/subdir2/file.ext")
 *
 *  Example 2:
 *  Input: input = "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext"
 *  Output: 32   ("dir/subdir2/subsubdir2/file2.ext")
 *
 *  Example 3:
 *  Input: input = "a"
 *  Output: 0
 *
 *  Constraints:
 *   - 1 <= input.length <= 10^4
 *   - input may contain letters, '\n', '\t', '.', ' ' and digits.
 */
public class LongestAbsoluteFilePath {

    // V0
    // IDEA: HASHMAP (depth -> accumulated path length)
    //       pathLen[d] = length of the path (including the trailing '/') of the
    //       directory chain at depth d, so a file at depth d has length
    //       pathLen[d] + len(name)
    /**
     * time = O(n)
     * space = O(d)   # d = max depth
     */
    public int lengthLongestPath(String input) {

        if (input == null || input.length() == 0) {
            return 0;
        }

        Map<Integer, Integer> pathLen = new HashMap<>();
        pathLen.put(0, 0);

        int res = 0;

        for (String line : input.split("\n")) {

            // count leading '\t' -> depth
            int depth = 0;
            while (depth < line.length() && line.charAt(depth) == '\t') {
                depth++;
            }

            String name = line.substring(depth);

            if (name.indexOf('.') >= 0) {
                // it is a file
                int base = pathLen.containsKey(depth) ? pathLen.get(depth) : 0;
                res = Math.max(res, base + name.length());
            } else {
                /**
                 *  NOTE !!!
                 *
                 *  it is a directory -> record the length for its CHILDREN (depth + 1),
                 *  + 1 for the '/' separator
                 */
                int base = pathLen.containsKey(depth) ? pathLen.get(depth) : 0;
                pathLen.put(depth + 1, base + name.length() + 1);
            }
        }

        return res;
    }
}
