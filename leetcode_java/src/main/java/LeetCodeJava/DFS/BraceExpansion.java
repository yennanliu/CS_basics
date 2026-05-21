package LeetCodeJava.DFS;

// https://leetcode.com/problems/brace-expansion/description/
// https://leetcode.ca/2018-11-21-1087-Brace-Expansion/

import java.util.*;

/**
 * 1087. Brace Expansion
 * Description
 * You are given a string s representing a list of words. Each letter in the word has one or more options.
 *
 * If there is one option, the letter is represented as is.
 * If there is more than one option, then curly braces delimit the options. For example, "{a,b,c}" represents options ["a", "b", "c"].
 * For example, if s = "a{b,c}", the first character is always 'a', but the second character can be 'b' or 'c'. The original list is ["ab", "ac"].
 *
 * Return all words that can be formed in this manner, sorted in lexicographical order.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "{a,b}c{d,e}f"
 * Output: ["acdf","acef","bcdf","bcef"]
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: ["abcd"]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * s consists of curly brackets '{}', commas ',', and lowercase English letters.
 * s is guaranteed to be a valid input.
 * There are no nested curly brackets.
 * All characters inside a pair of consecutive opening and ending curly brackets are different.
 *
 */
public class BraceExpansion {

    // V0
    // IDEA: BFS + PREFIX SUM !!!!
    // TODO : implement, validate
    public String[] expand(String s) {
        // Step 1: Parse the string into distinct groups of options
        List<List<String>> groups = new ArrayList<>();
        int i = 0;
        int n = s.length();

        while (i < n) {
            List<String> currentGroup = new ArrayList<>();
            char ch = s.charAt(i);

            if (ch == '{') {
                i++; // Skip '{'
                // Collect all characters inside this specific bracket pair
                while (s.charAt(i) != '}') {
                    if (s.charAt(i) != ',') {
                        currentGroup.add(String.valueOf(s.charAt(i)));
                    }
                    i++;
                }
                i++; // Skip '}'
            } else {
                // Regular character outside brackets forms a group of size 1
                currentGroup.add(String.valueOf(ch));
                i++;
            }

            // Sort each group alphabetically to ensure lexicographical order later
            Collections.sort(currentGroup);
            groups.add(currentGroup);
        }

        /** NOTE !!
         *
         *   BFS + prefix
         *
         */
        // Step 2: Perform BFS to combine the groups layer by layer
        Queue<String> queue = new LinkedList<>();
        queue.add(""); // Start with an empty base prefix string

        // Process groups one by one
        for (List<String> group : groups) {
            int currentQueueSize = queue.size();

            // Empty the current layer and append every option from the new group
            for (int k = 0; k < currentQueueSize; k++) {
                String currentPrefix = queue.poll();
                for (String option : group) {
                    queue.add(currentPrefix + option);
                }
            }
        }

        // Step 3: Convert the final accumulated layer in the queue into a String array
        String[] res = new String[queue.size()];
        int idx = 0;
        while (!queue.isEmpty()) {
            res[idx++] = queue.poll();
        }

        return res;
    }


    // V0-0-1
    // IDEA: CLASS BFS + PREFIX (GEMINI)
    // A classic BFS State container to track the string prefix
    // alongside its current depth/layer in the decision tree.
    /** NOTE !!!
     *
     *  we define a helper class
     *  for storing prefix, and group idx info
     */
    private static class State {
        String prefix;
        int groupIndex;

        State(String prefix, int groupIndex) {
            this.prefix = prefix;
            this.groupIndex = groupIndex;
        }
    }

    public String[] expand_0_0_1(String s) {
        // Step 1: Parse the string into distinct sorted groups (Same as before)
        List<List<String>> groups = new ArrayList<>();
        int i = 0;
        int n = s.length();
        while (i < n) {
            List<String> currentGroup = new ArrayList<>();
            char ch = s.charAt(i);
            if (ch == '{') {
                i++;
                while (s.charAt(i) != '}') {
                    if (s.charAt(i) != ',') {
                        currentGroup.add(String.valueOf(s.charAt(i)));
                    }
                    i++;
                }
                i++;
            } else {
                currentGroup.add(String.valueOf(ch));
                i++;
            }
            Collections.sort(currentGroup);
            groups.add(currentGroup);
        }

        // List to hold our final complete combinations
        List<String> resultList = new ArrayList<>();

        // Step 2: Canonical BFS using a state-tracking Queue
        Queue<State> queue = new LinkedList<>();

        // Push the root node: empty prefix starting at group index 0
        queue.add(new State("", 0));

        // The core BFS loop driven entirely by queue emptiness
        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Base Case / Leaf Node: If we have processed all groups,
            // the prefix is complete and can be added directly to results.
            if (current.groupIndex == groups.size()) {
                resultList.add(current.prefix);
                continue;
            }

            // Get the current layer's choices
            List<String> currentGroup = groups.get(current.groupIndex);

            // Expand neighbors (generate children) and push them back into the queue
            for (String option : currentGroup) {
                queue.add(new State(current.prefix + option, current.groupIndex + 1));
            }
        }

        // Step 3: Convert the populated results list to a String array
        return resultList.toArray(new String[0]);
    }



    // V0-1
    // IDEA: BFS + prefix (GPT)
    // TODO: validate
    public String[] expand_0_1(String s) {

        // ----------------------------------------
        // Queue for BFS
        // ----------------------------------------
        Queue<String> queue = new LinkedList<>();

        // Start with empty string
        queue.offer("");

        int i = 0;

        // ----------------------------------------
        // Process input string
        // ----------------------------------------
        while (i < s.length()) {

            // Current choices at this level
            List<String> choices = new ArrayList<>();

            // ------------------------------------
            // Case 1:
            // Brace group
            //
            // Example:
            // {a,b,c}
            // ------------------------------------
            if (s.charAt(i) == '{') {

                i++; // skip '{'

                while (s.charAt(i) != '}') {

                    char ch = s.charAt(i);

                    // Ignore commas
                    if (ch != ',') {
                        choices.add(String.valueOf(ch));
                    }

                    i++;
                }

                // skip '}'
                i++;
            }

            // ------------------------------------
            // Case 2:
            // Normal character
            // ------------------------------------
            else {

                choices.add(String.valueOf(s.charAt(i)));

                i++;
            }

            // ------------------------------------
            // BFS Expansion
            // ------------------------------------

            // Current queue size
            int size = queue.size();

            // Expand all existing strings
            for (int k = 0; k < size; k++) {

                // Current partial string
                String cur = queue.poll();

                // Append every possible choice
                for (String choice : choices) {

                    queue.offer(cur + choice);
                }
            }
        }

        // ----------------------------------------
        // Convert queue -> list
        // ----------------------------------------
        List<String> res = new ArrayList<>(queue);

        // Sort lexicographically
        Collections.sort(res);

        return res.toArray(new String[0]);
    }


    // V0-1-1
    // IDEA: BFS + prefix (GEMINI)
    public String[] expand_0_1_1(String s) {
        // Step 1: Parse the string into distinct groups of options
        List<List<String>> groups = new ArrayList<>();
        int i = 0;
        int n = s.length();

        while (i < n) {
            List<String> currentGroup = new ArrayList<>();
            char ch = s.charAt(i);

            if (ch == '{') {
                i++; // Skip '{'
                // Collect all characters inside this specific bracket pair
                while (s.charAt(i) != '}') {
                    if (s.charAt(i) != ',') {
                        currentGroup.add(String.valueOf(s.charAt(i)));
                    }
                    i++;
                }
                i++; // Skip '}'
            } else {
                // Regular character outside brackets forms a group of size 1
                currentGroup.add(String.valueOf(ch));
                i++;
            }

            // Sort each group alphabetically to ensure lexicographical order later
            Collections.sort(currentGroup);
            groups.add(currentGroup);
        }

        // Step 2: Perform BFS to combine the groups layer by layer
        Queue<String> queue = new LinkedList<>();
        queue.add(""); // Start with an empty base prefix string

        // Process groups one by one
        for (List<String> group : groups) {
            int currentQueueSize = queue.size();

            // Empty the current layer and append every option from the new group
            for (int k = 0; k < currentQueueSize; k++) {
                String currentPrefix = queue.poll();
                for (String option : group) {
                    queue.add(currentPrefix + option);
                }
            }
        }

        // Step 3: Convert the final accumulated layer in the queue into a String array
        String[] res = new String[queue.size()];
        int idx = 0;
        while (!queue.isEmpty()) {
            res[idx++] = queue.poll();
        }

        return res;
    }



    // V0-2
    // IDEA: BACKTRACK (gemini)
    // TODO: validate
    public String[] expand_0_2(String s) {
        // Step 1: Parse the string into groups of choices
        List<List<String>> groups = new ArrayList<>();
        int i = 0;
        int n = s.length();

        while (i < n) {
            List<String> currentGroup = new ArrayList<>();
            if (s.charAt(i) == '{') {
                i++; // Skip '{'
                // Gather all characters inside the brackets
                while (s.charAt(i) != '}') {
                    if (s.charAt(i) != ',') {
                        currentGroup.add(String.valueOf(s.charAt(i)));
                    }
                    i++;
                }
                i++; // Skip '}'
            } else {
                // It's a regular single character outside brackets
                currentGroup.add(String.valueOf(s.charAt(i)));
                i++;
            }
            // Sort each group alphabetically to guarantee lexicographical output order
            Collections.sort(currentGroup);
            groups.add(currentGroup);
        }

        // Step 2: Use Backtracking to generate all combinations
        List<String> resultList = new ArrayList<>();
        backtrack(groups, 0, new StringBuilder(), resultList);

        // Convert the result list to a primitive String array
        return resultList.toArray(new String[0]);
    }

    private void backtrack(List<List<String>> groups, int index, StringBuilder current, List<String> resultList) {
        // Base case: If we've processed all groups, we've formed a complete string
        if (index == groups.size()) {
            resultList.add(current.toString());
            return;
        }

        // Try every option available in the current group
        List<String> options = groups.get(index);
        for (String option : options) {
            current.append(option);                     // Choose
            backtrack(groups, index + 1, current, resultList); // Explore
            current.deleteCharAt(current.length() - 1); // Unchoose (Backtrack)
        }
    }


    // V0-5
    // IDEA: (GPT)
    // TODO : implement, validate
    public String[] expand_0_5(String s) {

        // ----------------------------------------
        // res stores current expansion results
        // Start with empty string
        // ----------------------------------------
        List<String> res = new ArrayList<>();
        res.add("");

        int i = 0;

        // ----------------------------------------
        // Process the input string
        // ----------------------------------------
        while (i < s.length()) {

            List<String> choices = new ArrayList<>();

            // ------------------------------------
            // Case 1:
            // Brace expansion
            //
            // Example:
            // {a,b,c}
            // ------------------------------------
            if (s.charAt(i) == '{') {

                i++; // skip '{'

                // Collect all characters inside brace
                while (s.charAt(i) != '}') {

                    char ch = s.charAt(i);

                    // Ignore commas
                    if (ch != ',') {
                        choices.add(String.valueOf(ch));
                    }

                    i++;
                }

                // skip '}'
                i++;
            }

            // ------------------------------------
            // Case 2:
            // Normal character
            // ------------------------------------
            else {

                choices.add(String.valueOf(s.charAt(i)));

                i++;
            }

            // ------------------------------------
            // Build Cartesian Product
            //
            // Example:
            //
            // res = ["a", "b"]
            // choices = ["c", "d"]
            //
            // =>
            // ["ac", "ad", "bc", "bd"]
            // ------------------------------------
            List<String> next = new ArrayList<>();

            for (String prefix : res) {

                for (String choice : choices) {

                    next.add(prefix + choice);
                }
            }

            // Update result
            res = next;
        }

        // ----------------------------------------
        // Sort lexicographically
        // Required by problem
        // ----------------------------------------
        Collections.sort(res);

        // Convert List -> String[]
        return res.toArray(new String[0]);
    }


    // V1
    // IDEA : DFS
    // https://leetcode.ca/2018-11-21-1087-Brace-Expansion/
    private List<String> ans;
    private List<String[]> items;

    /**
     * time = O(N * M)
     * space = O(N * M)
     */
    public String[] expand_1(String s) {
        ans = new ArrayList<>();
        items = new ArrayList<>();
        convert(s);

        System.out.println(">>> items = ");
        /**
         *
         * NOTE !!!
         *
         *  exp 1:
         *
         *  Input: s = "{a,b}c{d,e}f"
         *  Output: ["acdf","acef","bcdf","bcef"]
         *
         *  so,
         * >>> items =
         * [a, b]
         * [c]
         * [d, e]
         * [f]
         */
        for (String[] item : items){
            System.out.println(Arrays.toString(item));
        }

        dfs(0, new ArrayList<>());
        Collections.sort(ans);
        return ans.toArray(new String[0]);
    }

    private void convert(String s) {
        if ("".equals(s)) {
            return;
        }
        if (s.charAt(0) == '{') {
            int j = s.indexOf("}");
            items.add(s.substring(1, j).split(","));
            convert(s.substring(j + 1));
        } else {
            int j = s.indexOf("{");
            if (j != -1) {
                items.add(s.substring(0, j).split(","));
                convert(s.substring(j));
            } else {
                items.add(s.split(","));
            }
        }
    }


    // backtrack
    /**
     * NOTE !!!
     *
     *  once we get items, then the problem
     *  is transformed as "how to get all collections
     *  from each item from items", a classic backtrack pattern
     *
     *  dfs(int i, List<String> t)
     *  i is "starting index" we use it and "undo" to collect
     *  all item from items
     */
    private void dfs(int i, List<String> t) {
        if (i == items.size()) {
            ans.add(String.join("", t));
            return;
        }
        for (String c : items.get(i)) {
            t.add(c);
            dfs(i + 1, t);
            // undo
            t.remove(t.size() - 1);
        }
    }



    // V2
    // IDEA : DFS
    // https://walkccc.me/LeetCode/problems/1087/#__tabbed_1_2
    /**
     * time = O(N * M)
     * space = O(N * M)
     */
    public String[] expand_2(String s) {
        List<String> ans = new ArrayList<>();

        dfs_2(s, 0, new StringBuilder(), ans);
        Collections.sort(ans);
        return ans.toArray(new String[0]);
    }

    private void dfs_2(final String s, int i, StringBuilder sb, List<String> ans) {
        if (i == s.length()) {
            ans.add(sb.toString());
            return;
        }
        if (s.charAt(i) == '{') {
            final int nextRightBraceIndex = s.indexOf("}", i);
            for (final String c : s.substring(i + 1, nextRightBraceIndex).split(",")) {
                sb.append(c);
                dfs_2(s, nextRightBraceIndex + 1, sb, ans);
                sb.deleteCharAt(sb.length() - 1);
            }
        } else { // s[i] != '{'
            sb.append(s.charAt(i));
            dfs_2(s, i + 1, sb, ans);
            sb.deleteCharAt(sb.length() - 1);
        }
    }



    // V3
    // IDEA : DFS
    // https://blog.csdn.net/qq_46105170/article/details/108840420



    // V4
    // IDEA : DFS
    // https://blog.csdn.net/qq_21201267/article/details/107541722




    // testing
//    public static void main(String[] args) {
//        BraceExpansion be = new BraceExpansion();
//        String input =  "{a,b}c{d,e}f";
//        String[] res = be.expand(input);
//        for(String x : Arrays.asList(res)){
//            System.out.println(x);
//        }
//    }




}
