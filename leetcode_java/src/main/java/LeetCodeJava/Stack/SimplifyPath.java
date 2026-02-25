package LeetCodeJava.Stack;

// https://leetcode.com/problems/simplify-path/
// https://leetcode.cn/problems/simplify-path/

import java.util.*;

/**
 * 71. Simplify Path
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an absolute path for a Unix-style file system, which always begins with a slash '/'. Your task is to transform this absolute path into its simplified canonical path.
 *
 * The rules of a Unix-style file system are as follows:
 *
 * A single period '.' represents the current directory.
 * A double period '..' represents the previous/parent directory.
 * Multiple consecutive slashes such as '//' and '///' are treated as a single slash '/'.
 * Any sequence of periods that does not match the rules above should be treated as a valid directory or file name. For example, '...' and '....' are valid directory or file names.
 * The simplified canonical path should follow these rules:
 *
 * The path must start with a single slash '/'.
 * Directories within the path must be separated by exactly one slash '/'.
 * The path must not end with a slash '/', unless it is the root directory.
 * The path must not have any single or double periods ('.' and '..') used to denote current or parent directories.
 * Return the simplified canonical path.
 *
 *
 *
 * Example 1:
 *
 * Input: path = "/home/"
 *
 * Output: "/home"
 *
 * Explanation:
 *
 * The trailing slash should be removed.
 *
 * Example 2:
 *
 * Input: path = "/home//foo/"
 *
 * Output: "/home/foo"
 *
 * Explanation:
 *
 * Multiple consecutive slashes are replaced by a single one.
 *
 * Example 3:
 *
 * Input: path = "/home/user/Documents/../Pictures"
 *
 * Output: "/home/user/Pictures"
 *
 * Explanation:
 *
 * A double period ".." refers to the directory up a level (the parent directory).
 *
 * Example 4:
 *
 * Input: path = "/../"
 *
 * Output: "/"
 *
 * Explanation:
 *
 * Going one level up from the root directory is not possible.
 *
 * Example 5:
 *
 * Input: path = "/.../a/../b/c/../d/./"
 *
 * Output: "/.../b/d"
 *
 * Explanation:
 *
 * "..." is a valid name for a directory in this problem.
 *
 *
 *
 * Constraints:
 *
 * 1 <= path.length <= 3000
 * path consists of English letters, digits, period '.', slash '/' or '_'.
 * path is a valid absolute Unix path.
 *
 */
public class SimplifyPath {

    // V0
    // IDEA: Deque (gemini)
    public String simplifyPath(String path) {
        // 1. Clean up edge cases/logic: split("/") handles empty segments and slashes
        Deque<String> deque = new ArrayDeque<>();

        for (String p : path.split("/")) {
            if (p.equals("..")) {
                if (!deque.isEmpty()) {
                    deque.pollLast(); // Go up one directory
                }
            } else if (p.equals(".") || p.isEmpty()) {
                continue; // Ignore current directory or empty splits
            } else {
                deque.addLast(p); // Valid folder name
            }
        }

        /** NOTE !!!
         *
         *  edge case: if deque is empty, return "/" directly
         */
        // 2. If the deque is empty, the simplified path is just the root "/"
        if (deque.isEmpty()) {
            return "/";
        }

        // 3. Build the path using pollFirst to maintain correct order
        StringBuilder sb = new StringBuilder();
        while (!deque.isEmpty()) {
            /** NOTE !!!
             *
             *  the append order
             */
            sb.append("/"); // Prepend the slash
            sb.append(deque.pollFirst());
        }

        return sb.toString();
    }


    // V0-0-1
    // IDEA: Deque (GPT)
    public String simplifyPath_0_0_1(String path) {

        if (path == null || path.length() == 0) {
            return "/";
        }

        Deque<String> deque = new ArrayDeque<>();

        for (String p : path.split("/")) {

            if (p.equals("..")) {
                if (!deque.isEmpty()) {
                    deque.pollLast();
                }
            } else if (p.equals(".") || p.isEmpty()) {
                continue;
            } else {
                deque.addLast(p);
            }
        }

        // build result
        StringBuilder sb = new StringBuilder();

        for (String dir : deque) {
            sb.append("/").append(dir);
        }

        return sb.length() == 0 ? "/" : sb.toString();
    }


    // V0-1
    // IDEA: STACK (fixed by gpt)
    /**
     * time = O(1)
     * space = O(1)
     */
    public String simplifyPath_0_1(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }

        Stack<String> st = new Stack<>();

        for (String dir : path.split("/")) {
            if (dir.equals("..")) {
                if (!st.isEmpty()) {
                    st.pop();
                }
            }
            /** NOTE !!!
             *
             *  -> we DON'T add `null element` to stack
             *  -> !dir.isEmpty()
             */
            else if (!dir.equals(".") && !dir.isEmpty()) {
                st.push(dir);
            }
        }

        StringBuilder sb = new StringBuilder();
        /**
         * NOTE !!!
         *
         *  we can loop over element in stack directly (old -> new element ordering)
         *  via `for (String dir : st)`
         *
         *  -> so NO NEED to transform to
         *     list / other intermedia data structure
         *
         */
        for (String dir : st) {
            sb.append("/").append(dir);
        }

        return sb.length() == 0 ? "/" : sb.toString();
    }

    // V1
    // (needcode)
    // https://www.youtube.com/watch?v=qYlHrAKJfyA

    // V2
    // IDEA: DEQEUE + SET
    // https://leetcode.com/problems/simplify-path/solutions/25686/java-10-lines-solution-with-stack-by-shp-u6t2/
    /**
     * time = O(1)
     * space = O(1)
     */
    public String simplifyPath_2(String path) {
        Deque<String> stack = new LinkedList<>();
        Set<String> skip = new HashSet<>(Arrays.asList("..", ".", ""));
        for (String dir : path.split("/")) {
            if (dir.equals("..") && !stack.isEmpty())
                stack.pop();
            else if (!skip.contains(dir))
                stack.push(dir);
        }
        String res = "";
        for (String dir : stack)
            res = "/" + dir + res;
        return res.isEmpty() ? "/" : res;
    }

    // V3
    // IDEA: STACK
    // https://leetcode.com/problems/simplify-path/solutions/3407361/easy-solutions-in-java-python-and-c-look-0a1r/
    /**
     * time = O(1)
     * space = O(1)
     */
    public String simplifyPath_3(String path) {
        Stack<String> stack = new Stack<>(); // create a stack to keep track of directories
        String[] directories = path.split("/"); // split the path by slash '/'
        for (String dir : directories) { // iterate over the directories
            if (dir.equals(".") || dir.isEmpty()) { // ignore the current directory '.' and empty directories
                continue;
            } else if (dir.equals("..")) { // go one level up for double period '..'
                if (!stack.isEmpty()) { // if stack is not empty, pop the top element
                    stack.pop();
                }
            } else { // for any other directory, push it to the stack
                stack.push(dir);
            }
        }
        return "/" + String.join("/", stack); // join the directories in the stack with slash '/' and add a slash at the beginning
    }

    // V4
    // IDEA: STACK
    // https://leetcode.com/problems/simplify-path/solutions/6210383/video-stack-solution-by-niits-wg7g/
    /**
     * time = O(1)
     * space = O(1)
     */
    public String simplifyPath_4(String path) {
        String[] components = path.split("/");
        Stack<String> st = new Stack<>();

        for (String comp : components) {
            if (comp.equals("") || comp.equals(".")) {
                continue;
            }

            if (comp.equals("..")) {
                if (!st.isEmpty()) {
                    st.pop();
                }
            } else {
                st.push(comp);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) {
            sb.insert(0, "/" + st.pop());
        }

        return sb.length() == 0 ? "/" : sb.toString();
    }




}
