package LeetCodeJava.Math;

// https://leetcode.com/problems/counting-bits/
/**
 * 338. Counting Bits
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an integer n, return an array ans of length n + 1 such that for each i (0 <= i <= n), ans[i] is the number of 1's in the binary representation of i.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: [0,1,1]
 * Explanation:
 * 0 --> 0
 * 1 --> 1
 * 2 --> 10
 * Example 2:
 *
 * Input: n = 5
 * Output: [0,1,1,2,1,2]
 * Explanation:
 * 0 --> 0
 * 1 --> 1
 * 2 --> 10
 * 3 --> 11
 * 4 --> 100
 * 5 --> 101
 *
 *
 * Constraints:
 *
 * 0 <= n <= 105
 *
 *
 * Follow up:
 *
 * It is very easy to come up with a solution with a runtime of O(n log n). Can you do it in linear time O(n) and possibly in a single pass?
 * Can you do it without using any built-in function (i.e., like __builtin_popcount in C++)?
 *
 */
public class CountingBits {

    // V0
    // IDEA : MATH
    public int[] countBits(int n) {
        /** NOTE !!! we init ans as below */
        int[] ans = new int[n+1];
        for (int x = 0; x < n+1; x++){
            // https://stackoverflow.com/questions/2406432/converting-an-int-to-a-binary-string-representation-in-java
            String binary = Integer.toBinaryString(x);
            ans[x] = countElement(binary, '1');
        }

        return ans;
    }

    // https://www.baeldung.com/java-count-chars
    private int countElement(String s, char x){
        int count = 0;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == x){
                count += 1;
            }
        }
        return count;
    }

    // V0-1
    // IDEA: BRUTE FORCE
    public int[] countBits_0_1(int n) {
        // edge
        if (n == 0) {
            return new int[] { 0 };
        }
        if (n == 1) {
            return new int[] { 0, 1 };
        }
        if (n == 2) {
            return new int[] { 0, 1, 1 };
        }

        int[] res = new int[n + 1]; //???
        // ????
        for (int i = 0; i < n + 1; i++) {
            String binary = Integer.toBinaryString(i);
            System.out.println(">>> i = " + i +
                    ", binary = " + binary);
            res[i] = getOneCnt(binary);
        }

        return res;
    }

    private int getOneCnt(String input) {
        int cnt = 0;
        for (String x : input.split("")) {
            if (x.equals("1")) {
                cnt += 1;
            }
        }
        return cnt;
    }

    // V0-2
    // IDEA: BIT DP (gemini)
    public int[] countBits_0_2(int n) {
        int[] res = new int[n + 1];

        // We start from 1 because res[0] is already 0
        for (int i = 1; i <= n; i++) {
            // i >> 1 is the same as i / 2
            // i & 1 is the same as i % 2 (checks if the last bit is 1)
            res[i] = res[i >> 1] + (i & 1);
        }

        return res;
    }
    

    // V0-3
    // IDEA : java default
    public int[] countBits_0_3(int n) {

        if (n == 0){
            return new int[]{0};
        }

        int[] res = new int[n+1];

        for (int i = 0; i < n+1; i++){
            /**
             *  Integer.bitCount
             *
             *  -> java default get number of "1" from binary representation of a 10 based integer
             *
             *  -> e.g.
             *      Integer.bitCount(0) = 0
             *      Integer.bitCount(1) = 1
             *      Integer.bitCount(2) = 1
             *      Integer.bitCount(3) = 2
             *
             *  Ref
             *      - https://blog.csdn.net/weixin_42092787/article/details/106607426
             */
            int cnt = Integer.bitCount(i);
            res[i] = cnt;
        }
        return res;
    }

    // V1
    // https://leetcode.com/problems/counting-bits/solutions/3987279/java-solution-using-right-shift-operator/
    public int countOnes(int n) {
        int count = 0;
        while(n != 0) {
            count += (n&1);
            n = n>>1;
        }
        return count;
    }
    public int[] countBits_1(int n) {
        int ans[] = new int[n+1];
        ans[0] = 0;
        for(int i=1; i<ans.length; i++) {
            ans[i] = countOnes(i);
        }
        return ans;
    }

    // V2
    // IDEA : BIT OP
    // https://leetcode.com/problems/counting-bits/solutions/79539/three-line-java-solution/
    public int[] countBits_2(int num) {
        int[] f = new int[num + 1];
        for (int i=1; i<=num; i++) f[i] = f[i >> 1] + (i & 1);
        return f;
    }



}
