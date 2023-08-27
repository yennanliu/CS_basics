package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/palindrome-partitioning/

import java.util.ArrayList;
import java.util.List;

public class PalindromePartitioning {

    // V0
    // IDEA : BACKTRACK
//    List<List<String>> ans = new ArrayList<>();
//    public List<List<String>> partition(String s) {
//
//        if (s.length()==1){
//            ArrayList<String> tmp = new ArrayList<>();
//            tmp.add(s);
//            this.ans.add(tmp);
//        }
//
//        return this.ans;
//    }
//
//    private void _help(String s, List<String> cur){
//
//    }
//
//    private boolean isPalindrome(List<String> input){
//        int mid = -1;
//        if (input.size() % 2 == 1){
//            mid = input.size() / 2;
//        }else{
//            mid = input.size() / 2 - 1;
//        }
//
//        for (int i = 0; i < mid; i++){
//
//            int left = input[i];
//            int rig
//
//        }
//        return true;
//    }

    // V1
    // IDEA: BACKTRACK
    // https://leetcode.com/problems/palindrome-partitioning/editorial/
    public List<List<String>> partition_1(String s) {
        /** NOTE : we can init result, pass it to method, modify it, and return as ans */
        List<List<String>> result = new ArrayList<List<String>>();
        dfs_1(0, result, new ArrayList<String>(), s);
        return result;
    }

    void dfs_1(int start, List<List<String>> result, List<String> currentList, String s) {
        if (start >= s.length()) result.add(new ArrayList<String>(currentList));
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome(s, start, end)) {
                // add current substring in the currentList
                currentList.add(s.substring(start, end + 1));
                dfs_1(end + 1, result, currentList, s);
                // backtrack and remove the current substring from currentList
                currentList.remove(currentList.size() - 1);
            }
        }
    }

    boolean isPalindrome(String s, int low, int high) {
        while (low < high) {
            if (s.charAt(low++) != s.charAt(high--)) return false;
        }
        return true;
    }

    // V2
    // IDEA: BACKTRACK + DP
    // https://leetcode.com/problems/palindrome-partitioning/editorial/
    public List<List<String>> partition_2(String s) {
        int len = s.length();
        boolean[][] dp = new boolean[len][len];
        List<List<String>> result = new ArrayList<>();
        dfs_2(result, s, 0, new ArrayList<>(), dp);
        return result;
    }

    void dfs_2(List<List<String>> result, String s, int start, List<String> currentList, boolean[][] dp) {
        if (start >= s.length()) result.add(new ArrayList<>(currentList));
        for (int end = start; end < s.length(); end++) {
            if (s.charAt(start) == s.charAt(end) && (end - start <= 2 || dp[start + 1][end - 1])) {
                dp[start][end] = true;
                currentList.add(s.substring(start, end + 1));
                dfs_2(result, s, end + 1, currentList, dp);
                currentList.remove(currentList.size() - 1);
            }
        }
    }

}
