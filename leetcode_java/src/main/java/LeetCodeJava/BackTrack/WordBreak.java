package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-break/description/
/**
 * 139. Word Break
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.
 *
 * Note that the same word in the dictionary may be reused multiple times in the segmentation.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "leetcode", wordDict = ["leet","code"]
 * Output: true
 * Explanation: Return true because "leetcode" can be segmented as "leet code".
 * Example 2:
 *
 * Input: s = "applepenapple", wordDict = ["apple","pen"]
 * Output: true
 * Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
 * Note that you are allowed to reuse a dictionary word.
 * Example 3:
 *
 * Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 300
 * 1 <= wordDict.length <= 1000
 * 1 <= wordDict[i].length <= 20
 * s and wordDict[i] consist of only lowercase English letters.
 * All the strings of wordDict are unique.
 */
import java.util.*;

public class WordBreak {

    // V0
    // IDEA : BFS (Queue<Integer> queue) (fixed by GPT)
    // time: O(N^2 * M), space: O(N)
    public boolean wordBreak(String s, List<String> wordDict) {
        // Convert wordDict to a HashSet for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordDict);

        /** NOTE !!!
         *
         *  the QUEUE stores indices where words `end`
         */
        // BFS queue (stores indices where words end)
        Queue<Integer> queue = new LinkedList<>();

        /** NOTE !!!
         *
         *  we record the first idx (idx=0)
         *  as the start point, so in the BFS,
         *  we can move forward based on the init idx
         */
        queue.add(0); // Start from index 0

        // Track visited indices to avoid reprocessing
        /** NOTE !!!
         *
         * we use `visited` to avoid duplicated visiting
         */
        Set<Integer> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            int start = queue.poll();

            /** NOTE !!!
             *
             * Skip if already visited
             */
            if (visited.contains(start)) {
                continue;
            }
            visited.add(start);

            /** NOTE !!!
             *
             *  loop over all words in the dictionary
             */
            // Try all words in the dictionary
            for (String word : wordSet) {

                // NOTE !!! end if `the actual length of `building string`
                // (not idx, so below we check if already found a solution
                // via `end = start + word.length()`
                int end = start + word.length();
                /** NOTE !!!
                 *
                 *  `s.substring(start, end).equals(word)` condition
                 *   -> can avoid `not valid word` added to queue
                 *      , so avoid no-needed visit in while loop
                 *      , so BFS efficiency is improved
                 */
                if (end <= s.length() && s.substring(start, end).equals(word)) {
                    // NOTE !!! below
                    if (end == s.length()) {
                        return true; // Successfully segmented
                    }
                    queue.add(end); // Add new starting index
                }
            }
        }
        return false;
    }

    // V0-0-1
    // IDEA: BFS (loop over idx) (fixed by gemini)
    public boolean wordBreak_0_0_1(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict); // Faster lookups
        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[s.length() + 1];

        q.add(0);
        visited[0] = true;

        while (!q.isEmpty()) {
            int start = q.poll();

            // If we reached the end of the string
            if (start == s.length()) {
                return true;
            }

            // Try all possible end positions for the next word
            for (int end = start + 1; end <= s.length(); end++) {
                if (visited[end])
                    continue; // Optimization: skip if index already reached

                if (wordSet.contains(s.substring(start, end))) {
                    q.add(end);
                    visited[end] = true;
                }
            }
        }

        return false;
    }


    // V0-0-2
    // IDEA: BFS (gpt)
    public boolean wordBreak_0_0_2(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[s.length()];

        q.add(0); // start index

        while (!q.isEmpty()) {
            int start = q.poll();

            if (visited[start]) continue;
            visited[start] = true;

            for (String word : dict) {
                int end = start + word.length();

                if (end > s.length()) continue;

                if (s.substring(start, end).equals(word)) {
                    if (end == s.length()) {
                        return true;
                    }
                    q.add(end);
                }
            }
        }
        return false;
    }

    // V0-0-3
    // IDEA: BOTTOM UP DP (gemini)
    public boolean wordBreak_0_0_3(String s, List<String> wordDict) {
        int n = s.length();
        // Convert list to set for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordDict);

        // dp[i] means the prefix s[0...i-1] can be segmented
        boolean[] dp = new boolean[n + 1];

        // Base case: empty string can be segmented
        dp[0] = true;

        // i is the end of the current substring we are checking
        for (int i = 1; i <= n; i++) {
            // j is the potential "split point"
            for (int j = 0; j < i; j++) {
                /* * If the prefix s[0...j] is valid (dp[j] is true)
                 * AND the remaining part s[j...i] is in the dictionary
                 */
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // Found a valid way to segment up to i, move to i+1
                }
            }
        }

        return dp[n];
    }

    // V0-0-4
    // IDEA: TOP DOWN DP (gemini)
    public boolean wordBreak_0_0_4(String s, List<String> wordDict) {
        return solve(s, new HashSet<>(wordDict), 0, new Boolean[s.length()]);
    }

    private boolean solve(String s, Set<String> set, int start, Boolean[] memo) {
        if (start == s.length())
            return true;
        if (memo[start] != null)
            return memo[start];

        for (int end = start + 1; end <= s.length(); end++) {
            if (set.contains(s.substring(start, end)) && solve(s, set, end, memo)) {
                return memo[start] = true;
            }
        }
        return memo[start] = false;
    }



    // V0-1
    // IDEA : BFS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Backtracking/word-break.py
    // time: O(N^2 * M), space: O(N)
    public boolean wordBreak_0_1(String s, List<String> wordDict) {
        if (s == null || s.length() == 0 || wordDict == null || wordDict.size() == 0) {
            return false;
        }

        Deque<Integer> q = new ArrayDeque<>();
        q.offer(0);
        boolean[] visited = new boolean[s.length()];

        while (!q.isEmpty()) {
            int i = q.poll();
            if (!visited[i]) {
                for (int j = i + 1; j <= s.length(); j++) {
                    if (wordDict.contains(s.substring(i, j))) {
                        if (j == s.length()) {
                            return true;
                        }
                        q.offer(j);
                    }
                }
                visited[i] = true;
            }
        }

        return false;
    }

    // V0-2
    // IDEA : BACKTRACK (modified via GPT)
    // time: O(N^2 * M), space: O(N)
    public boolean wordBreak_0_2(String s, List<String> wordDict) {
        return check(s, new HashSet<>(wordDict), 0, new Boolean[s.length()]);
    }

    private boolean check(String s, Set<String> wordSet, int start, Boolean[] memo) {
        if (start == s.length()) {
            return true;
        }
        if (memo[start] != null) {
            return memo[start];
        }

        for (int end = start + 1; end <= s.length(); end++) {
            if (wordSet.contains(s.substring(start, end)) && check(s, wordSet, end, memo)) {
                return memo[start] = true;
            }
        }
        return memo[start] = false;
    }

    // V0-3
    // IDEA : BACKTRACK
    // time: O(N^2 * M), space: O(N)
    Boolean[] memo_;

    public boolean wordBreak_0_3(String s, List<String> wordDict) {
        memo_ = new Boolean[s.length()];
        return help(s, wordDict, 0);
    }

    private boolean help(String s, List<String> wordDict, int start) {
        if (start == s.length()) {
            return true;
        }
        if (memo_[start] != null) {
            return memo_[start];
        }
        for (int end = start + 1; end <= s.length(); end++) {
            if (wordDict.contains(s.substring(start, end)) && help(s, wordDict, end)) {
                memo_[start] = true;
                return true;
            }
        }
        memo_[start] = false;
        return false;
    }

    // V0-4
    // IDEA : BACKTRACK + MEMORIZATION (improve efficiency) (gpt)
    // time: O(N^2 * M), space: O(N * M)
    /**
     *
     * 1) Memoization:
     *
     *  Use a Map<String, Boolean> called memo to store whether a given substring can be segmented using the word dictionary.
     *  This avoids re-computation for the same substring multiple times.
     *
     * 2) Base Case:
     *
     *  If the string s is empty, return true because an empty string is considered to be fully segmented.
     *
     * 3) Check Memo:
     *
     *  Before performing any computations, check if the result for the current substring s is already computed and stored in memo.
     *
     * 4) Iterate Through Word Dictionary:
     *
     *  For each word in the dictionary, check if the current substring s starts with that word.
     *  If it does, recursively call canBuild on the remaining substring (suffix).
     *  If the recursive call returns true, store the result in memo and return true.
     *  Store False in Memo:
     *
     * If none of the words match or lead to a solution, store false in memo for the current substring s.
     * This approach enhances the efficiency of the backtracking algorithm by ensuring that each substring is processed only once, reducing the overall time complexity significantly.
     *
     */
    private Map<String, Boolean> memo = new HashMap<>();

    public boolean wordBreak_0_4(String s, List<String> wordDict) {
        return canBuild(s, wordDict);
    }

    private boolean canBuild(String s, List<String> wordDict) {
        if (s.isEmpty()) {
            return true;
        }

        if (memo.containsKey(s)) {
            return memo.get(s);
        }

        for (String word : wordDict) {
            if (s.startsWith(word)) {
                String suffix = s.substring(word.length());
                if (canBuild(suffix, wordDict)) {
                    memo.put(s, true);
                    return true;
                }
            }
        }

        memo.put(s, false);
        return false;
    }


    // V0-5
    // IDEA : DP (modified by GPT)
    // time: O(N^2 * M), space: O(N)
    /**
     *
     * To improve the efficiency of your wordBreak implementation,
     * we should switch from a backtracking approach to a dynamic programming (DP) approach.
     * The backtracking approach can lead to a lot of repeated work
     * and can be very slow for larger inputs,
     * whereas the DP approach will allow us to efficiently
     * check if the word can be segmented.
     *
     * Here is the improved version using dynamic programming:
     *
     */
    public boolean wordBreak_0_5(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true; // empty string is always breakable

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[s.length()];
    }

    // V0-6
    // IDEA: BFS (Queue<String> q ) (fixed by gpt)
    // time: O(N^3), space: O(N)
    public boolean wordBreak_0_6(String s, List<String> wordDict) {
        if (s == null || s.isEmpty())
            return true;
        if (wordDict.contains(s))
            return true;
        if (wordDict.isEmpty())
            return false;

        Queue<String> q = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Set<String> wordSet = new HashSet<>(wordDict); // O(1) lookup

        q.add(""); // Start with an empty string

        while (!q.isEmpty()) {
            String cur = q.poll();

            if (cur.equals(s))
                return true; // Found valid segmentation

            for (String w : wordSet) {
        /**
         *   NOTE !!! instead of `cur += w`,
         *       -> we create a new string (String newStr = cur + w;)
         *
         *
         *  Reason :
         *
         *    You should not use cur = cur + w;
         *    directly inside the loop
         *    -> because it modifies cur incorrectly for `future iterations`.
         *
         *
         *   ❌ Why cur = cur + w; is Wrong?
         *
         * 	1.	Modifies cur Instead of Creating a New Substring
         * 	    •	cur is the base substring that is expanded.
         * 	        If modified directly,
         * 	        future iterations will operate on an incorrect version.
         *
         * 	2.	Causes Incorrect Substring Expansions
         * 	    •	The queue (q) should always store valid prefixes,
         * 	        but modifying cur leads to unintended values.
         *
         * 	3.	Breaks BFS Iteration
         * 	    •	BFS should explore all possible words from
         * 	        the current prefix. But if cur is modified, subsequent iterations use the wrong string.
         *
         */
        String newStr = cur + w; // Form a new substring

        if (s.startsWith(newStr) && !visited.contains(newStr)) {
                    visited.add(newStr);
                    q.add(newStr);
                }
            }
        }

        return false;
    }

    // V0-7
    // IDEA: BACKTRACK (fixed by gpt) (TLE, not efficient)
    // time: O(N^2 * M), space: O(N)
    public boolean wordBreak_0_7(String s, List<String> wordDict) {
        // edge cases
        if (s == null || s.length() == 0) {
            return true;
        }
        if (wordDict == null) {
            return false;
        }
        if (wordDict.contains(s)) {
            return true;
        }

        // Backtrack without modifying idx directly
        return findWordHelper(s, wordDict, 0);
    }

    public boolean findWordHelper(String s, List<String> wordDict, int idx) {
        // Found a solution
        if (idx == s.length()) {
            return true;
        }

        // Try every word in the dictionary
        for (String word : wordDict) {
            int end = idx + word.length();
            // Make sure we don't go out of bounds and word matches
            if (end <= s.length() && word.equals(s.substring(idx, end))) {
                if (findWordHelper(s, wordDict, end)) {
                    return true; // Found a valid path
                }
            }
        }

        // No valid word break found
        return false;
    }

    // V0-8
    // IDEA: DP (gemini)
    /**

     * ### 🚀 Comparison: BFS vs. DP
     *
     * While you used BFS,
     * this problem is most famously solved with **1D Dynamic Programming**.
     * The logic is nearly identical, but it avoids the overhead of a Queue.
     *
     * **Why use DP here?**
     *
     * * **BFS:** Focuses on "Where can I go from here?" (Forward-looking).
     *
     * * **DP:** Focuses on "Is the current position reachable
     * from any previous valid spot?" (Backward-looking).
     *
     * ---
     *
     * ### 🔑 Logic Breakdown
     *
     * * **The `visited` array:**
     *     This is your most important fix.
     *     In a string like `s = "aaaaaaaaa"` and `wordDict = ["a", "aa", "aaa"]`,
     *     there are dozens of ways to reach the middle of the string.
     *     Without `visited`, your queue would explode in size (Exponential Complexity).
     *     With it, you only process each index once.
     *
     *
     * * **The Target:**
     *     You want to see if index `s.length()` is reachable.
     *     By using `start` indices, you avoid the "off-by-one" errors
     *     common when working with `length() - 1`.
     *
     */
    public boolean wordBreak_0_8(String s, List<String> wordDict) {
        // 1. Use a HashSet for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordDict);

        /**  NOTE !!
         *
         * dp[i] means the first i characters of s can be segmented
         *
         */
        // 2. dp[i] means the first i characters of s can be segmented
        boolean[] dp = new boolean[s.length() + 1];

        // 3. Base case: an empty string is always segmentable
        dp[0] = true;

        // 4. Fill the DP array
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                // If the prefix s[0...j] is valid AND the suffix s[j...i] is a word
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // Move to the next i once we find one valid split
                }
            }
        }

        return dp[s.length()];
    }


    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/word-break/
    // time: O(N^2 * M), space: O(N * M)
    public boolean wordBreak_1(String s, List<String> wordDict) {
        Map<String, Boolean> memo = new HashMap<>();
        Set<String> wordSet = new HashSet<>(wordDict);
        return dfs(s, wordSet, memo);
    }

    private boolean dfs(String s, Set<String> wordSet, Map<String, Boolean> memo) {
        if (memo.containsKey(s)) return memo.get(s);
        if (wordSet.contains(s)) return true;
        for (int i = 1; i < s.length(); i++) {
            String prefix = s.substring(0, i);
            if (wordSet.contains(prefix) && dfs(s.substring(i), wordSet, memo)) {
                memo.put(s, true);
                return true;
            }
        }
        memo.put(s, false);
        return false;
    }

    // V2
    // IDEA : MEMORIZATION
    // https://leetcode.com/problems/word-break/solutions/3687504/recursion-memoization-tabulation/
//    Boolean[] memo;
//    public boolean wordBreak_2(String s, List<String> wordDict) {
//        memo=new Boolean[s.length()];
//        return _wordBreak(s,wordDict,0);
//    }
//    public boolean _wordBreak(String s, List<String> wordDict, int si) {
//        if(si>=s.length()){
//            return true;
//        }
//        if(memo[si]!=null) return memo[si];
//        for(int i=si; i<s.length(); i++){
//            if(wordDict.contains(s.substring(si,i+1))){
//                if(_wordBreak(s,wordDict,i+1)){
//                    return memo[si] = true;
//                }
//            }
//        }
//        return memo[si] = false;
//    }

    // V3
    // IDEA : Tabulation
    // https://leetcode.com/problems/word-break/solutions/3687504/recursion-memoization-tabulation/
    // time: O(N^3), space: O(N)
    public boolean wordBreak_3(String s, List<String> wordDict) {
        boolean[] dp=new boolean[s.length()+1];
        dp[dp.length-1]=true;
        for(int i=dp.length-2; i>=0; i--){
            for(int j=i; j<s.length() ; j++){
                if(wordDict.contains(s.substring(i,j+1))){
                    if(dp[j+1]){
                        dp[i] = true;
                    }
                }
            }
        }
        return dp[0];
    }



}
