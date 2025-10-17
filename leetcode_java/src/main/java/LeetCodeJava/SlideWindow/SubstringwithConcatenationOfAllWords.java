package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/substring-with-concatenation-of-all-words/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  30. Substring with Concatenation of All Words
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given a string s and an array of strings words. All the strings of words are of the same length.
 *
 * A concatenated string is a string that exactly contains all the strings of any permutation of words concatenated.
 *
 * For example, if words = ["ab","cd","ef"], then "abcdef", "abefcd", "cdabef", "cdefab", "efabcd", and "efcdab" are all concatenated strings. "acdbef" is not a concatenated string because it is not the concatenation of any permutation of words.
 * Return an array of the starting indices of all the concatenated substrings in s. You can return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "barfoothefoobarman", words = ["foo","bar"]
 *
 * Output: [0,9]
 *
 * Explanation:
 *
 * The substring starting at 0 is "barfoo". It is the concatenation of ["bar","foo"] which is a permutation of words.
 * The substring starting at 9 is "foobar". It is the concatenation of ["foo","bar"] which is a permutation of words.
 *
 * Example 2:
 *
 * Input: s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"]
 *
 * Output: []
 *
 * Explanation:
 *
 * There is no concatenated substring.
 *
 * Example 3:
 *
 * Input: s = "barfoofoobarthefoobarman", words = ["bar","foo","the"]
 *
 * Output: [6,9,12]
 *
 * Explanation:
 *
 * The substring starting at 6 is "foobarthe". It is the concatenation of ["foo","bar","the"].
 * The substring starting at 9 is "barthefoo". It is the concatenation of ["bar","the","foo"].
 * The substring starting at 12 is "thefoobar". It is the concatenation of ["the","foo","bar"].
 *
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * 1 <= words.length <= 5000
 * 1 <= words[i].length <= 30
 * s and words[i] consist of lowercase English letters.
 */
public class SubstringwithConcatenationOfAllWords {

    // V0
//    public List<Integer> findSubstring(String s, String[] words) {
//
//    }

    // V1
    // https://leetcode.ca/2015-12-30-30-Substring-with-Concatenation-of-All-Words/
    public List<Integer> findSubstring_1(String s, String[] words) {
        Map<String, Integer> cnt = new HashMap<>();
        for (String w : words) {
            cnt.merge(w, 1, Integer::sum);
        }
        int m = s.length(), n = words.length;
        int k = words[0].length();
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < k; ++i) {
            Map<String, Integer> cnt1 = new HashMap<>();
            int l = i, r = i;
            int t = 0;
            while (r + k <= m) {
                String w = s.substring(r, r + k);
                r += k;
                if (!cnt.containsKey(w)) {
                    cnt1.clear();
                    l = r;
                    t = 0;
                    continue;
                }
                cnt1.merge(w, 1, Integer::sum);
                ++t;
                while (cnt1.get(w) > cnt.get(w)) {
                    String remove = s.substring(l, l + k);
                    l += k;
                    cnt1.merge(remove, -1, Integer::sum);
                    --t;
                }
                if (t == n) {
                    ans.add(l);
                }
            }
        }
        return ans;
    }


    // V2
    // https://leetcode.com/problems/substring-with-concatenation-of-all-words/solutions/7099022/if-not-understood-block-me-by-amyy_1256-1g7w/
    public List<Integer> findSubstring_2(String s, String[] words) {
        List<Integer> ans = new ArrayList<>();

        if (words.length == 0 || s.length() == 0) {
            return ans;
        }

        int wordSize = words[0].length();
        int wordCount = words.length;
        int N = s.length();

        HashMap<String, Integer> originalCount = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            originalCount.put(words[i], originalCount.getOrDefault(words[i], 0) + 1);
        }

        for (int offset = 0; offset < wordSize; offset++) {
            HashMap<String, Integer> currentCount = new HashMap<>();
            int start = offset;
            int count = 0;
            for (int end = offset; end + wordSize <= N; end += wordSize) {
                String currWord = s.substring(end, end + wordSize);
                if (originalCount.containsKey(currWord)) {
                    currentCount.put(currWord, currentCount.getOrDefault(currWord, 0) + 1);
                    count++;

                    while (currentCount.get(currWord) > originalCount.get(currWord)) {
                        String startWord = s.substring(start, start + wordSize);
                        currentCount.put(startWord, currentCount.get(startWord) - 1);
                        start += wordSize;
                        count--;
                    }

                    if (count == wordCount) {
                        ans.add(start);
                    }

                } else {
                    count = 0;
                    start = end + wordSize;
                    currentCount.clear();
                }
            }

        }
        return ans;
    }

    // V3
    // IDEA: HASH MAP + SLIDE WINDOW
    // https://leetcode.com/problems/substring-with-concatenation-of-all-words/solutions/6613074/easy-explanation-of-sliding-window-hashm-8qiy/
    public List<Integer> findSubstring_3(String s, String[] words) {
        List<Integer> ans = new ArrayList<>();

        if (words.length == 0 || s.length() == 0) {
            return ans;
        }

        int wordSize = words[0].length();
        int wordCount = words.length;
        int N = s.length();

        HashMap<String, Integer> originalCount = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            originalCount.put(words[i], originalCount.getOrDefault(words[i], 0) + 1);
        }

        for (int offset = 0; offset < wordSize; offset++) {
            HashMap<String, Integer> currentCount = new HashMap<>();
            int start = offset;
            int count = 0;
            for (int end = offset; end + wordSize <= N; end += wordSize) {
                String currWord = s.substring(end, end + wordSize);
                if (originalCount.containsKey(currWord)) {
                    currentCount.put(currWord, currentCount.getOrDefault(currWord, 0) + 1);
                    count++;

                    while (currentCount.get(currWord) > originalCount.get(currWord)) {
                        String startWord = s.substring(start, start + wordSize);
                        currentCount.put(startWord, currentCount.get(startWord) - 1);
                        start += wordSize;
                        count--;
                    }

                    if (count == wordCount) {
                        ans.add(start);
                    }

                } else {
                    count = 0;
                    start = end + wordSize;
                    currentCount.clear();
                }
            }

        }
        return ans;
    }


    // V4
    // IDEA: 2 HASH MAP (TLE)
    // https://leetcode.com/problems/substring-with-concatenation-of-all-words/solutions/13658/easy-two-map-solution-cjava-by-jianchao-wlsli/
    public List<Integer> findSubstring_4(String s, String[] words) {
        final Map<String, Integer> counts = new HashMap<>();
        for (final String word : words) {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }
        final List<Integer> indexes = new ArrayList<>();
        final int n = s.length(), num = words.length, len = words[0].length();
        for (int i = 0; i < n - num * len + 1; i++) {
            final Map<String, Integer> seen = new HashMap<>();
            int j = 0;
            while (j < num) {
                final String word = s.substring(i + j * len, i + (j + 1) * len);
                if (counts.containsKey(word)) {
                    seen.put(word, seen.getOrDefault(word, 0) + 1);
                    if (seen.get(word) > counts.getOrDefault(word, 0)) {
                        break;
                    }
                } else {
                    break;
                }
                j++;
            }
            if (j == num) {
                indexes.add(i);
            }
        }
        return indexes;
    }



}
