package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/palindrome-partitioning/
/**
 *  131. Palindrome Partitioning
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a string s, partition s such that every substring of the partition is a palindrome. Return all possible palindrome partitioning of s.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aab"
 * Output: [["a","a","b"],["aa","b"]]
 * Example 2:
 *
 * Input: s = "a"
 * Output: [["a"]]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 16
 * s contains only lowercase English letters.
 *
 */
import java.util.ArrayList;
import java.util.List;

public class PalindromePartitioning {

    // V0
    // IDEA : BACKTRACK
//    public List<List<String>> partition(String s) {
//
//    }

    // V0-1
    // IDEA: BACKTRACK + start_idx (fixed by gpt)
    // time: O(N * 2^N), space: O(N * 2^N)
    List<List<String>> partitionRes = new ArrayList<>();

    public List<List<String>> partition_0_1(String s) {
        if (s == null || s.isEmpty()) {
            return new ArrayList<>();
        }

        backtrack(s, 0, new ArrayList<>());
        return partitionRes;
    }

    private void backtrack(String s, int start, List<String> currentList) {
    /**
     *
     * 	•	This is the base case of the recursion.
     *
     * 	•	It means: "If we've reached the end of the string,
     * 	     then the current list of substrings (currentList)
     * 	     forms a valid full partition of s into palindromes."
     *
     * 	•	-> So we add a copy of currentList into
     * 	       the final result list partitionRes.
     */
    /**
     *  - Why start == s.length()?
     *
     * 	•	Because start is the index from which
     * 	    we're currently trying to partition.
     *
     * 	•	If start == s.length(), it means we've
     * 	     used up all characters in s, and currentList is now a full,
     * 	     valid partition.
     */
    if (start == s.length()) {
            partitionRes.add(new ArrayList<>(currentList));
            return;
        }

   /**
    *  NOTE !!!
    *
    *   1) we loop from `start + 1` to `s.length()`
    *   2) get sub string via s.substring(a, b)
    *   3) check if current sub string is Palindrome
    *       - if yes,
    *          - add sub string to current cache
    *          - recursive call backtrack
    *          - undo cache add
    */
   for (int end = start + 1; end <= s.length(); end++) {
            String sub = s.substring(start, end);
            if (isPalindrome(sub)) {
                currentList.add(sub);
                backtrack(s, end, currentList);
                currentList.remove(currentList.size() - 1); // undo
            }
        }

    }

    // helper func check if a string is `Palindrome`
    public boolean isPalindrome(String x) {
        int l = 0;
        int r = x.length() - 1;
        while (r > l) {
            if (x.charAt(l) != x.charAt(r)) {
                return false;
            }
            r--;
            l++;
        }
        return true;
    }

    // V1-1
    // https://neetcode.io/problems/palindrome-partitioning
    // IDEA: Backtracking (Pick / Not Pick)
    // time: O(N * 2^N), space: O(N * 2^N)
    private List<List<String>> res = new ArrayList<>();
    private List<String> part = new ArrayList<>();

    public List<List<String>> partition_1_1(String s) {
        dfs(0, 0, s);
        return res;
    }

    private void dfs(int j, int i, String s) {
        if (i >= s.length()) {
            if (i == j) {
                res.add(new ArrayList<>(part));
            }
            return;
        }

        if (isPali(s, j, i)) {
            part.add(s.substring(j, i + 1));
            dfs(i + 1, i + 1, s);
            part.remove(part.size() - 1);
        }

        dfs(j, i + 1, s);
    }

    private boolean isPali(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }


    // V1-2
    // https://neetcode.io/problems/palindrome-partitioning
    // IDEA: Backtracking
    // time: O(N * 2^N), space: O(N * 2^N)
    public List<List<String>> partition_1_2(String s) {
        List<List<String>> res = new ArrayList<>();
        List<String> part = new ArrayList<>();
        dfs_1_2(0, s, part, res);
        return res;
    }

    private void dfs_1_2(int i, String s, List<String> part, List<List<String>> res) {
        if (i >= s.length()) {
            res.add(new ArrayList<>(part));
            return;
        }
        for (int j = i; j < s.length(); j++) {
            if (isPali_1_2(s, i, j)) {
                part.add(s.substring(i, j + 1));
                dfs_1_2(j + 1, s, part, res);
                part.remove(part.size() - 1);
            }
        }
    }

    private boolean isPali_1_2(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }


    // V1-3
    // https://neetcode.io/problems/palindrome-partitioning
    // IDEA: Backtracking (DP)
    // time: O(N^2 + N * 2^N), space: O(N^2 + N * 2^N)
    boolean[][] dp;
    public List<List<String>> partition_1_3(String s) {
        int n = s.length();
        dp = new boolean[n][n];
        for (int l = 1; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                dp[i][i + l - 1] = (s.charAt(i) == s.charAt(i + l - 1) &&
                        (i + 1 > (i + l - 2) ||
                                dp[i + 1][i + l - 2]));
            }
        }

        List<List<String>> res = new ArrayList<>();
        List<String> part = new ArrayList<>();
        dfs(0, s, part, res);
        return res;
    }

    private void dfs(int i, String s, List<String> part, List<List<String>> res) {
        if (i >= s.length()) {
            res.add(new ArrayList<>(part));
            return;
        }
        for (int j = i; j < s.length(); j++) {
            if (dp[i][j]) {
                part.add(s.substring(i, j + 1));
                dfs(j + 1, s, part, res);
                part.remove(part.size() - 1);
            }
        }
    }

    // V4
    // IDEA: BACKTRACK
    // https://leetcode.com/problems/palindrome-partitioning/editorial/
    // time: O(N * 2^N), space: O(N * 2^N)
    public List<List<String>> partition_4(String s) {
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

    // V5
    // IDEA: BACKTRACK + DP
    // https://leetcode.com/problems/palindrome-partitioning/editorial/
    // time: O(N^2 + N * 2^N), space: O(N^2 + N * 2^N)
    public List<List<String>> partition_5(String s) {
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
