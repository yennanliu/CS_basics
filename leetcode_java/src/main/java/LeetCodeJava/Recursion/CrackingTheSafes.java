package LeetCodeJava.Recursion;

// https://leetcode.com/problems/cracking-the-safe/

import java.util.HashSet;
import java.util.Set;

/**
 *
 * 753. Cracking the Safe
 * Hard
 * Topics
 * Companies
 * Hint
 * There is a safe protected by a password. The password is a sequence of n digits where each digit can be in the range [0, k - 1].
 *
 * The safe has a peculiar way of checking the password. When you enter in a sequence, it checks the most recent n digits that were entered each time you type a digit.
 *
 * For example, the correct password is "345" and you enter in "012345":
 * After typing 0, the most recent 3 digits is "0", which is incorrect.
 * After typing 1, the most recent 3 digits is "01", which is incorrect.
 * After typing 2, the most recent 3 digits is "012", which is incorrect.
 * After typing 3, the most recent 3 digits is "123", which is incorrect.
 * After typing 4, the most recent 3 digits is "234", which is incorrect.
 * After typing 5, the most recent 3 digits is "345", which is correct and the safe unlocks.
 * Return any string of minimum length that will unlock the safe at some point of entering it.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 1, k = 2
 * Output: "10"
 * Explanation: The password is a single digit, so enter each digit. "01" would also unlock the safe.
 * Example 2:
 *
 * Input: n = 2, k = 2
 * Output: "01100"
 * Explanation: For each possible password:
 * - "00" is typed in starting from the 4th digit.
 * - "01" is typed in starting from the 1st digit.
 * - "10" is typed in starting from the 3rd digit.
 * - "11" is typed in starting from the 2nd digit.
 * Thus "01100" will unlock the safe. "10011", and "11001" would also unlock the safe.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 4
 * 1 <= k <= 10
 * 1 <= kn <= 4096
 */


public class CrackingTheSafes {

    // V0
    // TODO : implement below
//    public String crackSafe(int n, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/cracking-the-safe/solutions/314906/java-dfs-not-a-fast-solution-but-easy-to-understand-with-explanation/
    public String crackSafe_1(int n, int k) {
        Set<String> visited = new HashSet<String>();
        //*start from string "00.."
        String res = "";
        for(int j = 0; j < n; j++){
            res+=0;
        }
        //*calculate target length, which is k^n+n-1
        int total = 1;
        for(int i = 0; i < n; i++){
            total *= k;
        }
        total += n-1;
        //*run DFS
        res=DFS(res, n, k, visited, total);
        return res;
    }
    private String DFS(String res, int n, int k, Set<String> visited, int total){
        int len = res.length();
        visited.add(res.substring(len-n, len));
        for(int i = 0; i < k; i++){
            if(!visited.contains(res.substring(len-n+1, len)+i)){
                String tmp = DFS(res+i, n, k, visited, total);
                //*if length of result is less than total length, remove substring from visited and continue loop, else we are done! break the loop!
                if(tmp.length() == total){
                    res = tmp;
                    break;
                }
                visited.remove(res.substring(len-n+1, len)+i);
            }
        }
        return res;
    }

    // V2
    // IDEA : DFS + BACKTRACK (gpt)
    // https://zxi.mytechroad.com/blog/graph/leetcode-753-cracking-the-safe/
    public String crackSafe_2(int n, int k) {
        int totalLen = (int) Math.pow(k, n) + n - 1;
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            ans.append('0');
        }
        Set<String> visited = new HashSet<>();
        visited.add(ans.toString());

        if (dfs(ans, totalLen, n, k, visited)) {
            return ans.toString();
        }
        return "";
    }

    private boolean dfs(StringBuilder ans, int totalLen, int n, int k, Set<String> visited) {
        if (ans.length() == totalLen) {
            return true;
        }

        String node = ans.substring(ans.length() - n + 1);
        for (char c = '0'; c < '0' + k; c++) {
            String next = node + c;
            if (!visited.contains(next)) {
                ans.append(c);
                visited.add(next);
                if (dfs(ans, totalLen, n, k, visited)) {
                    return true;
                }
                visited.remove(next);
                ans.deleteCharAt(ans.length() - 1);
            }
        }

        return false;
    }

    // V2-1
    // IDEA : DFS + BACKTRACK
    // https://zxi.mytechroad.com/blog/graph/leetcode-753-cracking-the-safe/
    public String crackSafe_2_1(int n, int k) {
        int totalLen = (int) Math.pow(k, n) + n - 1;
        StringBuilder node = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            node.append('0');
        }
        StringBuilder ans = new StringBuilder();
        Set<String> visited = new HashSet<>();
        dfs_2_1(node.toString(), k, visited, ans);
        return ans.toString() + node;
    }

    private void dfs_2_1(String node, int k, Set<String> visited, StringBuilder ans) {
        for (char c = '0'; c < '0' + k; c++) {
            String next = node + c;
            if (visited.contains(next)) continue;
            visited.add(next);
            dfs_2_1(next.substring(1), k, visited, ans);
            ans.append(c);
        }
    }

}
