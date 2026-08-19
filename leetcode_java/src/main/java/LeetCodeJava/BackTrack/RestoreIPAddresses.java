package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/restore-ip-addresses/

import java.util.*;

/**
 *  93. Restore IP Addresses
 *  Medium
 *
 *  A valid IP address consists of exactly four integers separated by single
 *  dots. Each integer is between 0 and 255 (inclusive) and cannot have leading
 *  zeros.
 *   - For example, "0.1.2.201" and "192.168.1.1" are valid IP addresses, but
 *     "0.011.255.245", "192.168.1.312" and "192.168@1.1" are not.
 *
 *  Given a string s containing only digits, return all possible valid IP
 *  addresses that can be formed by inserting dots into s. You are not allowed
 *  to reorder or remove any digits.
 *
 *  Example 1:
 *   Input: s = "25525511135"
 *   Output: ["255.255.11.135","255.255.111.35"]
 *
 *  Example 2:
 *   Input: s = "101023"
 *   Output: ["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 *
 *  Constraints:
 *   1 <= s.length <= 20
 *   s consists of digits only.
 */
public class RestoreIPAddresses {

    // V0
    // IDEA: backtracking, at each step take 1..3 digits as one segment
    /**
     * time = O(1) (bounded: at most 3^4 splits)
     * space = O(1)
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.length() < 4 || s.length() > 12) {
            return res;
        }
        dfs(s, 0, new ArrayList<String>(), res);
        return res;
    }

    private void dfs(String s, int start, List<String> path, List<String> res) {
        if (path.size() == 4) {
            if (start == s.length()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < path.size(); i++) {
                    if (i > 0) {
                        sb.append(".");
                    }
                    sb.append(path.get(i));
                }
                res.add(sb.toString());
            }
            return;
        }

        for (int len = 1; len <= 3; len++) {
            if (start + len > s.length()) {
                break;
            }
            String seg = s.substring(start, start + len);
            if (!isValid(seg)) {
                continue;
            }
            path.add(seg);
            dfs(s, start + len, path, res);
            path.remove(path.size() - 1);
        }
    }

    private boolean isValid(String seg) {
        // no leading zero (except "0" itself)
        if (seg.length() > 1 && seg.charAt(0) == '0') {
            return false;
        }
        return Integer.parseInt(seg) <= 255;
    }
}
