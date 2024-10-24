package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-matching-subsequences/description/

import java.util.*;

/**
 * 792. Number of Matching Subsequences
 * Medium
 * Topics
 * Companies
 * Given a string s and an array of strings words, return the number of words[i] that is a subsequence of s.
 * <p>
 * A subsequence of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.
 * <p>
 * For example, "ace" is a subsequence of "abcde".
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: s = "abcde", words = ["a","bb","acd","ace"]
 * Output: 3
 * Explanation: There are three strings in words that are a subsequence of s: "a", "acd", "ace".
 * Example 2:
 * <p>
 * Input: s = "dsahjpjauf", words = ["ahjpjau","ja","ahbwzgqnuk","tnmlanowax"]
 * Output: 2
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= s.length <= 5 * 104
 * 1 <= words.length <= 5000
 * 1 <= words[i].length <= 50
 * and words[i] consist of only lowercase English letters.
 */
public class NumberOfMatchingSubsequences {

    // V0
//    public int numMatchingSubseq(String s, String[] words) {
//
//    }

    // V1
    // IDEA : HASH MAP + 2 POINTERS
    // https://leetcode.com/problems/number-of-matching-subsequences/solutions/2306416/java-easy-solution-97-faster-code/
    public int numMatchingSubseq_1(String s, String[] words) {

        Map<String, Integer> map = new HashMap<>();
        for (String str : words) {
            map.put(str, map.getOrDefault(str, 0) + 1);
        }

        int ans = 0;
        char ch[] = s.toCharArray();

        for (String str : map.keySet()) {

            char temp[] = str.toCharArray();
            int i = 0;
            int j = 0;

            while (i < ch.length && j < temp.length) {
                if (ch[i] == temp[j]) {
                    i++;
                    j++;
                } else {
                    i++;
                }
            }

            if (j == temp.length) {
                ans += map.get(str);
            }

        }

        return ans;
    }

    // V2
    // https://leetcode.com/problems/number-of-matching-subsequences/solutions/1097868/simple-java-solution-used-a-map-for-last-7-test-cases/
    public static int numMatchingSubseq_2(String S, String[] words) {
        int result = 0;

        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        for (String word : words) {
            if (!map.containsKey(word)) {
                if (isSubSequence(word, S, word.length(), S.length()) || word.length() == 0) {
                    result++;
                    map.put(word, true);
                } else {
                    map.put(word, false);
                }
            } else {
                if (map.get(word)) {
                    result++;
                }
            }

        }

        return result;
    }

    static boolean isSubSequence(String str1, String str2, int m, int n) {
        int j = 0;

        // Traverse str2 and str1, and compare
        // current character of str2 with first
        // unmatched char of str1, if matched
        // then move ahead in str1
        for (int i = 0; i < n && j < m; i++)
            if (str1.charAt(j) == str2.charAt(i))
                j++;

        // If all characters of str1 were found
        // in str2
        return (j == m);
    }

    // V3
    // https://leetcode.com/problems/number-of-matching-subsequences/solutions/2306285/java-easy-solution-using-hashmap/
    public int numMatchingSubseq_3(String s, String[] words) {
        Map<Character, Queue<String>> mp = new HashMap<>();
        int ans = 0;

        for (int i = 0; i < s.length(); ++i)
            mp.putIfAbsent(s.charAt(i), new LinkedList<>());

        for (String word : words) {
            char startCh = word.charAt(0);
            if (mp.containsKey(startCh))
                mp.get(startCh).offer(word);
        }

        for (int i = 0; i < s.length(); ++i) {
            char startCh = s.charAt(i);
            Queue<String> que = mp.get(startCh);
            int size = que.size();
            for (int j = 0; j < size; ++j) {
                String str = que.poll();
                if (str.substring(1).length() == 0)
                    ans++;
                else if (mp.containsKey(str.charAt(1)))
                    mp.get(str.charAt(1)).add(str.substring(1));
            }
        }

        return ans;
    }

    // V4
    // https://leetcode.com/problems/number-of-matching-subsequences/solutions/1289458/easy-to-understand-java-solution-explanation-with-comments-with-string-functions/
    /* Approach: For every word, check if it is subsequence of input string */
    public int numMatchingSubseq_4(String s, String[] words) {

        String inputString = s;
        int count = 0;

        // Check for every words in array

        for (String word : words) {

            // Check if word is subsequence of input string

            if (checkSubsequence(word, inputString)) {
                count = count + 1;
            }

        }

        return count;
    }


    /* Helper function to check if given word is sub sequence of given input string */

    private boolean checkSubsequence(String word, String inputString) {

        int prevCharIndex = 0;   // It will store the index of input String where previous char was found

        /*  So, the curr character should be found after this index for maintaining subsequence order  */

        for (char ch : word.toCharArray()) {

            int index = inputString.indexOf(ch, prevCharIndex);   // search for char after prev char found index

            // If index == -1 means char not found, else found at index i.

            if (index == -1) {
                return false;
            }

            prevCharIndex = index + 1;   // set the prevCharIndex to current found char index + 1 for next search

            // We do index + 1 as maybe duplicate elements consider this same index twice, so increment by 1.

        }

        return true;   // Every chars traversed and found, return true.

    }


    // V5
    // IDEA : list + queue (gpt)
    public int numMatchingSubseq_5(String s, String[] words) {
        // Create an array of 26 queues (one for each letter)
        List<Queue<String>> waiting = new ArrayList<>(26);
        for (int i = 0; i < 26; i++) {
            waiting.add(new LinkedList<>());
        }

        // Add each word to the appropriate queue based on its first letter
        for (String word : words) {
            char firstChar = word.charAt(0);
            waiting.get(firstChar - 'a').offer(word);
        }

        // Process the string `s`
        int res = 0;
        for (char c : s.toCharArray()) {
            Queue<String> queue = waiting.get(c - 'a');
            int size = queue.size(); // Only process the current size of the queue
            while (size-- > 0) {
                String word = queue.poll();
                if (word.length() == 1) {
                    // If the word has only one letter and it's matched, it's a subsequence
                    res++;
                } else {
                    // Move the remaining part of the word to the next appropriate queue
                    String remaining = word.substring(1);
                    waiting.get(remaining.charAt(0) - 'a').offer(remaining);
                }
            }
        }

        return res;
    }

    // V2
}
