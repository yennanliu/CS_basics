package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-break/description/

import java.util.*;

public class WordBreak {

    // V0
    // IDEA : BACKTRACK
    // TODO: fix below
//    public boolean wordBreak(String s, List<String> wordDict) {
//        String cur = "";
//        return this.check(s, wordDict, cur, 0);
//    }
//
//    private Boolean check(String s, List<String> wordDict, String cur, int idx){
//        if (cur == s){
//            return true;
//        }
//        if (cur.length() > s.length()){
//            return false;
//        }
//        for (int i = idx; i < wordDict.size(); i++){
//            Integer curLen = cur.length();
//            String word = wordDict.get(i);
//            cur += word;
//            this.check(s, wordDict, cur, i);
//            // undo
//            cur = cur.substring(0, curLen-1);
//        }
//        return false;
//    }

    // V0
    // IDEA : BFS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Backtracking/word-break.py
    public boolean wordBreak(String s, List<String> wordDict) {
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

    // V0
    // IDEA : BACKTRACK (modified via GPT)
    public boolean wordBreak_(String s, List<String> wordDict) {
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

    // V0''
    // IDEA : BACKTRACK
    Boolean[] memo_;

    public boolean wordBreak_0(String s, List<String> wordDict) {
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

    // V0''''
    // IDEA : BACKTRACK + MEMORIZATION (improve efficiency) (gpt)
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

    public boolean wordBreak_0_2(String s, List<String> wordDict) {
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


    // V0''''
    // IDEA : DP (modified by GPT)
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
    public boolean wordBreak_0_3(String s, List<String> wordDict) {
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

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/word-break/
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
