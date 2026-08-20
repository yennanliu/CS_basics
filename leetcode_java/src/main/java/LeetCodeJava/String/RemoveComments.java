package LeetCodeJava.String;

import java.util.ArrayList;
import java.util.List;

// https://leetcode.com/problems/remove-comments/description/
// https://leetcode.ca/all/722.html
public class RemoveComments {

    // V0
    // IDEA: LINE BY LINE PARSING + `inBlock` STATE MACHINE
    /**
     *  Key points:
     *
     *   1) keep ONE global `inBlock` flag, since a block comment
     *      can span multiple lines
     *
     *   2) when NOT in a block comment:
     *       - "/*" -> enter block comment (skip 2 chars)
     *       - "//" -> drop the REST of the line (break)
     *       - otherwise -> keep the char
     *
     *   3) when IN a block comment:
     *       - only look for the closing "*"+"/" (skip 2 chars),
     *         everything else (including "//") is ignored
     *
     *   4) we ONLY flush the buffer when we are NOT inside a block comment
     *      at the end of a line (so `int a = 1; /* comment
     *      still a comment * / + 2;` gets joined into ONE line)
     *
     *   5) a line that becomes EMPTY after removal is dropped
     */
    /**
     * time = O(N)  (N = total number of chars over all lines)
     * space = O(N)
     */
    public List<String> removeComments(String[] source) {
        List<String> res = new ArrayList<>();
        if (source == null || source.length == 0) {
            return res;
        }

        boolean inBlock = false;
        StringBuilder sb = new StringBuilder();

        for (String line : source) {
            int i = 0;
            int len = line.length();

            /** NOTE !!!
             *
             *  ONLY reset the buffer if we are NOT continuing
             *  an `unfinished` block comment
             */
            if (!inBlock) {
                sb.setLength(0);
            }

            while (i < len) {
                if (inBlock) {
                    // looking for the closing "*/"
                    if (i + 1 < len && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlock = false;
                        i += 2;
                    } else {
                        i += 1;
                    }
                } else {
                    // "/*" -> block comment starts
                    if (i + 1 < len && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlock = true;
                        i += 2;
                    }
                    // "//" -> line comment, drop the rest of this line
                    else if (i + 1 < len && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break;
                    }
                    // normal char
                    else {
                        sb.append(line.charAt(i));
                        i += 1;
                    }
                }
            }

            /** NOTE !!!
             *
             *  only flush when the block comment is closed,
             *  and skip the `empty` line
             */
            if (!inBlock && sb.length() > 0) {
                res.add(sb.toString());
                sb.setLength(0);
            }
        }

        return res;
    }

    // V1
    // IDEA : PARSING
    // https://leetcode.com/problems/remove-comments/editorial/
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<String> removeComments_1(String[] source) {
        boolean inBlock = false;
        StringBuilder newline = new StringBuilder();
        List<String> ans = new ArrayList();
        for (String line: source) {
            int i = 0;
            char[] chars = line.toCharArray();
            if (!inBlock) {
                newline = new StringBuilder();
            }
            while (i < line.length()) {
                if (!inBlock && i+1 < line.length() && chars[i] == '/' && chars[i+1] == '*') {
                    inBlock = true;
                    i++;
                } else if (inBlock && i+1 < line.length() && chars[i] == '*' && chars[i+1] == '/') {
                    inBlock = false;
                    i++;
                } else if (!inBlock && i+1 < line.length() && chars[i] == '/' && chars[i+1] == '/') {
                    break;
                } else if (!inBlock) {
                    newline.append(chars[i]);
                }
                i++;
            }
            if (!inBlock && newline.length() > 0) {
                ans.add(new String(newline));
            }
        }
        return ans;
    }


    // V2
    // https://leetcode.ca/2017-11-21-722-Remove-Comments/
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<String> removeComments_2(String[] source) {
        List<String> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean blockComment = false;
        for (String s : source) {
            int m = s.length();
            for (int i = 0; i < m; ++i) {
                if (blockComment) {
                    if (i + 1 < m && s.charAt(i) == '*' && s.charAt(i + 1) == '/') {
                        blockComment = false;
                        ++i;
                    }
                } else {
                    if (i + 1 < m && s.charAt(i) == '/' && s.charAt(i + 1) == '*') {
                        blockComment = true;
                        ++i;
                    } else if (i + 1 < m && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                        break;
                    } else {
                        sb.append(s.charAt(i));
                    }
                }
            }
            if (!blockComment && sb.length() > 0) {
                ans.add(sb.toString());
                sb.setLength(0);
            }
        }
        return ans;
    }

}
