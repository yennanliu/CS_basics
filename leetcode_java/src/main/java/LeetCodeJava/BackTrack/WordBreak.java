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

        // BFS queue (stores indices where words end)
        Queue<Integer> queue = new LinkedList<>();
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
