package LeetCodeJava.String;

// https://leetcode.com/problems/find-substring-with-given-hash-value/description/

/**
 * 2156. Find Substring With Given Hash Value
 * Hard
 *
 * The hash of a 0-indexed string s of length k, given integers p and m, is computed using the following function:
 *
 * hash(s, p, m) = (val(s[0]) * p^0 + val(s[1]) * p^1 + ... + val(s[k-1]) * p^(k-1)) mod m.
 *
 * Where val(s[i]) represents the index of s[i] in the alphabet from val('a') = 1 to val('z') = 26.
 *
 * You are given a string s and the integers power, modulo, k, and hashValue. Return sub, the first substring of s of length k such that hash(sub, power, modulo) == hashValue.
 *
 * The test cases will be generated such that an answer always exists.
 *
 * A substring is a contiguous non-empty sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "leetcode", power = 7, modulo = 20, k = 2, hashValue = 0
 * Output: "ee"
 * Explanation: The hash of "ee" can be computed to be hash("ee", 7, 20) = (5 * 1 + 5 * 7) mod 20 = 40 mod 20 = 0.
 * "ee" is the first substring of length 2 with hashValue 0. Hence, we return "ee".
 *
 * Example 2:
 *
 * Input: s = "fbxzaad", power = 31, modulo = 100, k = 3, hashValue = 32
 * Output: "fbx"
 * Explanation: The hash of "fbx" can be computed to be hash("fbx", 31, 100) = (6 * 1 + 2 * 31 + 24 * 961) mod 100 = 23132 mod 100 = 32.
 * "fbx" is the first substring of length 3 with hashValue 32. Hence, we return "fbx".
 * Note that "bxz" also has a hash of 32 but it is a later substring.
 *
 *
 * Constraints:
 *
 * 1 <= k <= s.length <= 2 * 10^4
 * 1 <= power, modulo <= 10^9
 * 0 <= hashValue < modulo
 * s consists of lowercase English letters only.
 * The test cases are generated such that an answer always exists.
 *
 */
public class FindSubstringWithGivenHashValue {

    // V0
    // IDEA: ROLLING HASH, SLIDING RIGHT-TO-LEFT SO NO MODULAR INVERSE IS NEEDED
    //       moving the window from [i+1, i+k] to [i, i+k-1]:
    //       h = h * power - val(s[i+k]) * power^k + val(s[i])   (all mod modulo)
    /**
     * time = O(N)
     * space = O(1)
     */
    public String subStrHash(String s, int power, int modulo, int k, int hashValue) {
        int n = s.length();
        long mod = modulo;
        long p = power % mod;

        // power^k mod modulo: the weight that falls off the right end of the window
        long topWeight = 1;
        for (int i = 0; i < k; i++) {
            topWeight = topWeight * p % mod;
        }

        // hash of the last window s[n-k .. n-1], Horner's rule from the right
        // so the leftmost character ends up with weight power^0
        long hash = 0;
        for (int i = n - 1; i >= n - k; i--) {
            hash = (hash * p + charValue(s, i)) % mod;
        }
        int start = (hash == hashValue) ? n - k : -1;

        // slide leftwards; the last match found is the earliest index
        for (int i = n - k - 1; i >= 0; i--) {
            long dropped = charValue(s, i + k) * topWeight % mod;
            // add mod before subtracting so the value never goes negative
            hash = (hash * p % mod - dropped + mod + charValue(s, i)) % mod;
            if (hash == hashValue) {
                start = i;
            }
        }
        return s.substring(start, start + k);
    }

    /** val('a') = 1, ..., val('z') = 26. */
    private long charValue(String s, int index) {
        return s.charAt(index) - 'a' + 1;
    }
}
