package LeetCodeJava.String;

import java.util.ArrayList;
import java.util.List;

// https://leetcode.com/problems/remove-comments/description/
// https://leetcode.ca/all/722.html
/**
 * 722. Remove Comments
 * Medium
 *
 * Given a C++ program, remove comments from it. The program source is an array of strings source where source[i] is the i^th line of the source code. This represents the result of splitting the original source code string by the newline character '\n'.
 *
 * In C++, there are two types of comments, line comments, and block comments.
 *
 * The string "//" denotes a line comment, which represents that it and the rest of the characters to the right of it in the same line should be ignored.
 * The string "/*" denotes a block comment, which represents that all characters until the next (non-overlapping) occurrence of "*&#47;" should be ignored. (Here, occurrences happen in reading order: line by line from left to right.) To be clear, the string "/*&#47;" does not yet end the block comment, as the ending would be overlapping the beginning.
 *
 * The first effective comment takes precedence over others.
 *
 * For example, if the string "//" occurs in a block comment, it is ignored.
 * Similarly, if the string "/*" occurs in a line or block comment, it is also ignored.
 *
 * If a certain line of code is empty after removing comments, you must not output that line: each string in the answer list will be non-empty.
 *
 * There will be no control characters, single quote, or double quote characters.
 *
 * For example, source = "string s = "/* Not a comment. *&#47;";" will not be a test case.
 *
 * Also, nothing else such as defines or macros will interfere with the comments.
 *
 * It is guaranteed that every open block comment will eventually be closed, so "/*" outside of a line or block comment always starts a new comment.
 *
 * Finally, implicit newline characters can be deleted by block comments. Please see the examples below for details.
 *
 * After removing the comments from the source code, return the source code in the same format.
 *
 * Example 1:
 *
 * Input: source = ["/*Test program *&#47;", "int main()", "{ ", "  // variable declaration ", "int a, b, c;", "/* This is a test", "   multiline  ", "   comment for ", "   testing *&#47;", "a = b + c;", "}"]
 * Output: ["int main()","{ ","  ","int a, b, c;","a = b + c;","}"]
 * Explanation: The line by line code is visualized as below:
 * /*Test program *&#47;
 * int main()
 * {
 *   // variable declaration
 * int a, b, c;
 * /* This is a test
 *    multiline
 *    comment for
 *    testing *&#47;
 * a = b + c;
 * }
 * The string /* denotes a block comment, including line 1 and lines 6-9. The string // denotes line 4 as comments.
 * The line by line output code is visualized as below:
 * int main()
 * {
 *
 * int a, b, c;
 * a = b + c;
 * }
 *
 * Example 2:
 *
 * Input: source = ["a/*comment", "line", "more_comment*&#47;b"]
 * Output: ["ab"]
 * Explanation: The original source string is "a/*comment\nline\nmore_comment*&#47;b", where we have bolded the newline characters.  After deletion, the implicit newline characters are deleted, leaving the string "ab", which when delimited by newline characters becomes ["ab"].
 *
 * Constraints:
 *
 * 1 <= source.length <= 100
 * 0 <= source[i].length <= 80
 * source[i] consists of printable ASCII characters.
 * Every open block comment is eventually closed.
 * There are no single-quote or double-quote in the input.
 *
 */
public class RemoveComments {

    // V0
    // TODO : implement
//    public List<String> removeComments(String[] source) {
//
//    }

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
