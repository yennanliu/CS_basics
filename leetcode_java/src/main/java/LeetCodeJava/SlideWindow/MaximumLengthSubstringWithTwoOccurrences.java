package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-length-substring-with-two-occurrences/description/

import java.util.HashMap;
import java.util.Map;

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
    // IDEA: SLIDE WINDOW + HASHMAP
    /**
     * time = O(N)
     * space = O(K)
     */
    public int maximumLengthSubstring(String s) {
        // edge
        if (s.isEmpty()) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }

        int maxLen = 1;
        int l = 0;

        // { val : cnt }
        Map<String, Integer> map = new HashMap<>();
        for (int r = 0; r < s.length(); r++) {

            String rightVal = String.valueOf(s.charAt(r));
            // NOTE !! handle case if rightVal key NOT existed yet
            map.put(rightVal, map.getOrDefault(rightVal, 0) + 1);

            /**
             *  NOTE !!!
             *
             *   l < r condition
             *
             *   ---
             *
             *  Slide window pattern:
             *
             *  for(int r = 0; r < nums.len; r++){
             *      while(condition){
             *          // ...
             *          l += 1;
             *      }
             *      // ...
             *  }
             */
            while (!isValid(map) && l < r) {
                String leftVal = String.valueOf(s.charAt(l));
                // NOTE !! handle case if leftVal key NOT existed yet
                if (map.containsKey(leftVal)) {
                    map.put(leftVal, map.get(leftVal) - 1);
                    if (map.get(leftVal) == 0) {
                        map.remove(leftVal);
                    }
                }
                l += 1;
            }
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    private boolean isValid(Map<String, Integer> map) {
        if (map.isEmpty()) {
            return true;
        }
        for (int val : map.values()) {
            if (val > 2) {
                return false;
            }
        }

        return true;
    }

    // V0-1
    // IDEA: SLIDE WINDOW (gemini)
    /**
     * Logic:
     * 1. Use two pointers (l, r) to represent a sliding window.
     * 2. Use an int[26] array to track character frequencies.
     * 3. Expand 'r'. If the frequency of s[r] exceeds 2, shrink 'l'
     * until the frequency of s[r] is back to 2.
     * * Time Complexity: O(N) - Each pointer moves at most N times.
     * Space Complexity: O(1) - Fixed size frequency array.
     */
    /**
     * time = O(N)
     * space = O(K)
     */
    public int maximumLengthSubstring_0_1(String s) {
        int n = s.length();
        if (n <= 2)
            return n;

        int[] counts = new int[26];
        int maxLen = 0;
        int l = 0;

        /**
         *  NOTE !!!
         *
         *  Slide window pattern:
         *
         *  for(int r = 0; r < nums.len; r++){
         *      while(condition){
         *          // ...
         *          l += 1;
         *      }
         *      // ...
         *  }
         */
        for (int r = 0; r < n; r++) {
            int rightIdx = s.charAt(r) - 'a';
            counts[rightIdx]++;

            /** NOTE !!!
             *
             *  the critical optimized idea:
             *
             *   -> ONLY check the `right pointer` idx,
             *     to see if the sub string is still valid (max cnt <= 2)
             *
             */
            // Instead of checking the whole map, only check the character
            // we just added. If it's > 2, the window is invalid.
            while (counts[rightIdx] > 2) {
                int leftIdx = s.charAt(l) - 'a';
                counts[leftIdx]--;
                l++;
            }

            // Calculate current window length
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }


    // V1
    // IDEA:  Sliding Window
    // https://leetcode.com/problems/maximum-length-substring-with-two-occurrences/solutions/4916854/easy-video-solution-brute-force-optimal-xul8w/
    /**
     * time = O(N)
     * space = O(K)
     */
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
    /**
     * time = O(N)
     * space = O(K)
     */
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
    /**
     * time = O(N)
     * space = O(K)
     */
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

    // V4
    // IDEA: SLIDE WINDOW
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/64243726
    /**
     * time = O(N)
     * space = O(K)
     */
    public int maximumLengthSubstring_4(String s) {
        int[] cnt = new int[26];
        int ans = 0;
        int l = 0;

        for (int i = 0; i < s.length(); i++) {
            cnt[s.charAt(i) - 'a']++;

            while (cnt[s.charAt(i) - 'a'] > 2) {
                cnt[s.charAt(l) - 'a']--;
                l++;
            }

            ans = Math.max(ans, i - l + 1);
        }

        return ans;
    }



}
