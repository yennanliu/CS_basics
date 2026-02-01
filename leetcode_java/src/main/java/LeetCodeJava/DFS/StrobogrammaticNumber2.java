package LeetCodeJava.DFS;

// https://leetcode.com/problems/strobogrammatic-number-ii/description/
// https://leetcode.ca/all/247.html

import java.util.*;

/**
 * 247. Strobogrammatic Number II
 * A strobogrammatic number is a number that looks the same when rotated 180 degrees (looked at upside down).
 *
 * Find all strobogrammatic numbers that are of length = n.
 *
 * Example:
 *
 * Input:  n = 2
 * Output: ["11","69","88","96"]
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Cisco Facebook Google
 * Problem Solution
 * 247-Strobogrammatic-Number-II
 *
 */
public class StrobogrammaticNumber2 {

    // V0
    // TODO: validate and fix
//    List<String> res = new ArrayList<>();
//    public List<String> findStrobogrammatic_1_1(int n) {
//        for(int i = 0; i < n; i++){
//            HashSet<String> cache = new HashSet<>();
//            StringBuilder sb = new StringBuilder();
//            findNumbers(n, sb, cache);
//        }
//        return res;
//    }
//
//    private void findNumbers(int n, StringBuilder sb, Set<String> cache){
//
//        String[] digits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
//        String str = sb.toString();
//        if (str.length() == n){
//            if (isStrobogrammatic(str)){
//                if(!cache.contains(str)){
//                    cache.add(str);
//                    res.add(str);
//                }
//            }
//            return;
//        }
//
//        if (str.length() > n){
//            return;
//        }
//
//        for (String x: digits){
//            findNumbers(n, sb.append(x), cache);
//        }
//    }
//
//    private boolean isStrobogrammatic(String x){
//        Map<String, String> symmertricMapping = new HashMap<>();
//        symmertricMapping.put("0", "0");
//        symmertricMapping.put("1", "1");
//        symmertricMapping.put("8", "8");
//        symmertricMapping.put("6", "9");
//        symmertricMapping.put("9", "6");
//        int l = 0;
//        int r = x.length() - 1;
//        while (r > l){
////            if (x.charAt(l) != x.charAt(r)){
////                return false;
////            }
//            String left = String.valueOf(x.charAt(l));
//            String right = String.valueOf(x.charAt(r));
//            if (!symmertricMapping.get(left).equals(right)){
//                return false;
//            }
//            r -= 1;
//            l += 1;
//        }
//
//        return true;
//    }

    // V1-1
    // https://leetcode.ca/2016-08-03-247-Strobogrammatic-Number-II/
    List<String> singleDigitList = new ArrayList<>(Arrays.asList("0", "1", "8")); // not char[], because List can direct return as result
    char[][] digitPair = { {'1', '1'}, {'8', '8'}, {'6', '9'}, {'9', '6'} }; // except '0', a special case

    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> findStrobogrammatic_1_1(int n) {
        return dfs(n, n);
    }

    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> dfs(int k, int n) {
        if (k <= 0) {
            return new ArrayList<String>(Arrays.asList(""));
        }
        if (k == 1) {
            return singleDigitList;
        }

        List<String> subList = dfs(k - 2, n);
        List<String> result = new ArrayList<>();

        for (String str : subList) {
            if (k != n) { // @note: cannot start with 0
                result.add("0" + str + "0");
            }
            for (char[] aDigitPair : digitPair) {
                result.add(aDigitPair[0] + str + aDigitPair[1]);
            }
        }

        return result;
    }

    // V1-2
    // https://leetcode.ca/2016-08-03-247-Strobogrammatic-Number-II/
    private static final int[][] PAIRS = { {1, 1}, {8, 8}, {6, 9}, {9, 6}};
    private int n;

    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> findStrobogrammatic_1_2(int n) {
        this.n = n;
        return dfs(n);
    }

    private List<String> dfs(int u) {
        if (u == 0) {
            return Collections.singletonList("");
        }
        if (u == 1) {
            return Arrays.asList("0", "1", "8");
        }
        List<String> ans = new ArrayList<>();
        for (String v : dfs(u - 2)) {
            for ( int[] p : PAIRS) {
                ans.add(p[0] + v + p[1]);
            }
            if (u != n) {
                ans.add(0 + v + 0);
            }
        }
        return ans;
    }

    // V2-1
    // IDEA : DFS (gpt)
    // TODO: validate the code
    List<String> res = new ArrayList<>();
    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> findStrobogrammatic_2_1(int n) {
        // Use valid strobogrammatic pairs only
        char[][] pairs = new char[][] {
                {'0', '0'}, {'1', '1'}, {'8', '8'},
                {'6', '9'}, {'9', '6'}
        };

        findNumbers(n, new char[n], 0, n - 1, pairs);
        return res;
    }

    private void findNumbers(int n, char[] current, int left, int right, char[][] pairs) {
        if (left > right) {
            // Base case: Valid strobogrammatic number
            res.add(new String(current));
            return;
        }

        for (char[] pair : pairs) {
            // Avoid leading zero unless the number is of length 1
            if (left == 0 && pair[0] == '0' && n > 1) {
                continue;
            }

            // Place the pair
            current[left] = pair[0];
            current[right] = pair[1];

            // Recursive call
            findNumbers(n, current, left + 1, right - 1, pairs);
        }
    }

}
