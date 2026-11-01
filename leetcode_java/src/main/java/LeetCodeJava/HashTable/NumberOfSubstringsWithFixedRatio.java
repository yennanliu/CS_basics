package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-substrings-with-fixed-ratio/description/
// https://leetcode.ca/2022-09-23-2489-Number-of-Substrings-With-Fixed-Ratio/

import java.util.*;

/**
 * 2489. Number of Substrings With Fixed Ratio
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given a binary string s, and two integers num1 and num2. num1 and num2 are coprime numbers.
 *
 * A ratio substring is a substring of s where the ratio between the number of 0's and the number of 1's in the substring is exactly num1 : num2. For example, if num1 = 2 and num2 = 3, then "01011" and "1110000111" are ratio substrings, while "11000" is not.
 *
 * Return the number of non-empty ratio substrings of s.
 *
 * Note that:
 *
 * A substring is a contiguous sequence of characters within a string.
 * Two values x and y are coprime if gcd(x, y) == 1, where gcd(x, y) is the greatest common divisor of x and y.
 *
 *
 * Example 1:
 *
 * Input: s = "0110011", num1 = 1, num2 = 2
 * Output: 4
 * Explanation: There exist 4 non-empty ratio substrings.
 * - The substring s[0..2]: "0110011". It contains one 0 and two 1's. The ratio is 1 : 2.
 * - The substring s[1..4]: "0110011". It contains one 0 and two 1's. The ratio is 1 : 2.
 * - The substring s[4..6]: "0110011". It contains one 0 and two 1's. The ratio is 1 : 2.
 * - The substring s[1..6]: "0110011". It contains two 0's and four 1's. The ratio is 2 : 4 == 1 : 2.
 * It can be shown that there are no more ratio substrings.
 *
 * Example 2:
 *
 * Input: s = "10101", num1 = 3, num2 = 1
 * Output: 0
 * Explanation: There is no ratio substrings of s. We return 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 1 <= num1, num2 <= 10^6
 * gcd(num1, num2) == 1
 * s consists only of '0' and '1' characters.
 *
 */
public class NumberOfSubstringsWithFixedRatio {

    // V0
    // IDEA: PREFIX KEY num2 * zeros - num1 * ones; EQUAL KEYS BOUND A RATIO SUBSTRING (HASH MAP)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long fixedRatio(String s, int num1, int num2) {
        // zeros : ones == num1 : num2  <=>  num2 * zeros - num1 * ones == 0,
        // so substring (i, j] qualifies exactly when key[i] == key[j]
        Map<Long, Integer> keyCount = new HashMap<>();
        // the empty prefix
        keyCount.put(0L, 1);

        // key can reach 10^6 * 10^5, so it needs a long
        long key = 0;
        long result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                key += num2;
            } else {
                key -= num1;
            }
            // every earlier prefix with the same key ends a ratio substring here
            int earlier = keyCount.getOrDefault(key, 0);
            result += earlier;
            keyCount.put(key, earlier + 1);
        }
        return result;
    }
}
