package LeetCodeJava.String;

// https://leetcode.com/problems/palindromic-substrings/

public class PalindromicSubstrings {

    // V0
    // IDEA : 2 POINTER + for loop + odd / even length handling (gpt) (LC 005)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/palindromic-substrings.py#L59
    public int countSubstrings(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            // odd
            ans += helper(s, i, i);
            // even
            ans += helper(s, i, i + 1);
        }
        return ans;
    }

    private int helper(String s, int l, int r) {
        int ans = 0;
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
            ans++;
        }
        return ans;
    }


    // V0'
    // IDEA : BRUTE FORCE
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/palindromic-substrings.py
    public int countSubstrings_0(String s) {
        int cnt = 0;
        if (s == null || s.length() == 0){
            return cnt;
        }
        // two pointer
        //   a  b  c
        //   i
        //   j
        // i as left pointer, j as right pointer
        for (int i = 0; i < s.length(); i++){
            for (int j = i+1; j < s.length() + 1; j++){
                if (isPalindromic(s.substring(i, j))){
                    cnt += 1;
                }
            }
        }
        return cnt;
    }

    private boolean isPalindromic(String x){
        //System.out.println(x);
        if (x.length() == 0){
            return true;
        }
        if (x.length() == 1){
            return true;
        }
        int l = 0;
        int r = x.length()-1;
        while (r >= l){
            if (x.charAt(l) != x.charAt(r)){
                return false;
            }
            r -= 1;
            l += 1;
        }
        return true;
    }

    // V0'''
    // IDEA : 2 pointers with all cases covered (gpt)
    public int countSubstrings_0_1(String s) {
        int n = s.length();
        int res = 0;

        // Expand around center for all possible centers
        for (int center = 0; center < 2 * n - 1; center++) {
            int left = center / 2;
            int right = left + center % 2;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                res++;
                left--;
                right++;
            }
        }

        return res;
    }

    // V1
    // https://leetcode.com/problems/palindromic-substrings/solutions/4705208/beats-100-easy-to-understand-c-java-python-solution/
    public int check(String s, int i, int j) {
        int ans = 0;
        while (i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
            ans++;
            i--;
            j++;
        }
        return ans;
    }

    public int countSubstrings_1(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            ans += check(s, i, i);
            ans += check(s, i, i + 1);
        }
        return ans;
    }

    // V2
    // https://leetcode.com/problems/palindromic-substrings/solutions/4667296/short-and-easy-method-3ms-please-upvote/
    public int solve(String s, int i, int j){
        if(i<0 || j==s.length()) return 0;
        if(s.charAt(i) == s.charAt(j)) return solve(s, i-1, j+1) + 1;
        else return 0;
    }
    public int countSubstrings_2(String s) {
        int max = 0;
        for(int i=0; i<s.length(); i++){
            max += solve(s,i,i) + solve(s,i,i+1);
        }
        return max;
    }

}
