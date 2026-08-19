package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-duplicate-file-in-system/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  609. Find Duplicate File in System
 *  Medium
 *
 *  Given a list paths of directory info, including the directory path, and all the
 *  files with contents in this directory, return all the duplicate files in the file
 *  system in terms of their paths. You may return the answer in any order.
 *
 *  A group of duplicate files consists of at least two files that have the same content.
 *
 *  A single directory info string in the input list has the following format:
 *   "root/d1/d2/.../dm f1.txt(f1_content) f2.txt(f2_content) ... fn.txt(fn_content)"
 *
 *  The output is a list of groups of duplicate file paths, of the form
 *   "directory_path/file_name.txt"
 *
 *  Example 1:
 *  Input: paths = ["root/a 1.txt(abcd) 2.txt(efgh)","root/c 3.txt(abcd)",
 *                  "root/c/d 4.txt(efgh)","root 4.txt(efgh)"]
 *  Output: [["root/a/2.txt","root/c/d/4.txt","root/4.txt"],
 *           ["root/a/1.txt","root/c/3.txt"]]
 *
 *  Constraints:
 *  1 <= paths.length <= 2 * 10^4
 *  1 <= paths[i].length <= 3000
 */
public class FindDuplicateFileInSystem {

    // V0
    // IDEA: HASHMAP content -> list of full paths, keep groups with size > 1
    /**
     * time = O(n * x)   // n = # of files, x = avg string length
     * space = O(n * x)
     */
    public List<List<String>> findDuplicate(String[] paths) {
        Map<String, List<String>> contentToPaths = new HashMap<>();

        for (String path : paths) {
            if (path == null || path.trim().isEmpty()) {
                continue;
            }
            String[] parts = path.split(" ");
            String dir = parts[0];
            for (int i = 1; i < parts.length; i++) {
                String file = parts[i];
                int open = file.indexOf('(');
                if (open < 0) {
                    continue;
                }
                String name = file.substring(0, open);
                // strip the trailing ')'
                String content = file.substring(open + 1, file.length() - 1);

                List<String> lst = contentToPaths.get(content);
                if (lst == null) {
                    lst = new ArrayList<>();
                    contentToPaths.put(content, lst);
                }
                lst.add(dir + "/" + name);
            }
        }

        List<List<String>> res = new ArrayList<>();
        for (List<String> group : contentToPaths.values()) {
            if (group.size() > 1) {
                res.add(group);
            }
        }
        return res;
    }
}
