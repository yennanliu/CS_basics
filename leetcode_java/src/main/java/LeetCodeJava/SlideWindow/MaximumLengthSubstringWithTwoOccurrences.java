package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-length-substring-with-two-occurrences/description/
/**
 * 3090. Maximum Length Substring With Two Occurrences
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a string s, return the maximum length of a substring such that it contains at most two occurrences of each character.
 *
 *
 * Example 1:
 *
 * Input: s = "bcbbbcba"
 *
 * Output: 4
 *
 * Explanation:
 *
 * The following substring has a length of 4 and contains at most two occurrences of each character: "bcbbbcba".
 * Example 2:
 *
 * Input: s = "aaaa"
 *
 * Output: 2
 *
 * Explanation:
 *
 * The following substring has a length of 2 and contains at most two occurrences of each character: "aaaa".
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 100
 * s consists only of lowercase English letters.
 *
 */
public class MaximumLengthSubstringWithTwoOccurrences {

    // V0
//    public int maximumLengthSubstring(String s) {
//
//    }

    // V1
    // IDEA:  Sliding Window
    // https://leetcode.com/problems/maximum-length-substring-with-two-occurrences/solutions/4916854/easy-video-solution-brute-force-optimal-xul8w/
    public int maximumLengthSubstring_1(String s) {
        int n = s.length();
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            int[] arr = new int[26];
            for (int j = i; j < n; j++) {
                if (++arr[s.charAt(j) - 'a'] == 3)
                    break;
                maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }

    // V2
    // IDEA: ARRAY
    // https://leetcode.com/problems/maximum-length-substring-with-two-occurrences/solutions/4916763/simple-java-solution-by-siddhant_1602-i3dd/
    public int maximumLengthSubstring_2(String s) {
        int a[] = new int[26];
        int maxi = 0, i = 0, i1 = 0;
        for (i = 0, i1 = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            a[c - 'a']++;
            int count = 0;
            for (int j = 0; j < 26; j++) {
                if (a[j] > 2) {
                    count++;
                }
            }
            if (count == 1) {
                maxi = Math.max(maxi, i - i1);
                //System.out.println(maxi);
                while (count != 0) {
                    count = 0;
                    a[s.charAt(i1) - 'a']--;
                    i1++;
                    for (int j = 0; j < 26; j++) {
                        if (a[j] > 2) {
                            count++;
                        }
                    }
                }
            }
        }
        return Math.max(maxi, (i - i1));
    }


    // V3
    // IDEA:  Sliding Window
    public int maximumLengthSubstring_3(String s) {
        int ans = 0;
        int[] freq = new int[26];
        for (int i = 0, ii = 0; i < s.length(); ++i) {
            ++freq[s.charAt(i) - 97];
            while (freq[s.charAt(i) - 97] == 3)
                --freq[s.charAt(ii++) - 97];
            ans = Math.max(ans, i - ii + 1);
        }
        return ans;
    }


}
