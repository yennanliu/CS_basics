package LeetCodeJava.Greedy;

// https://leetcode.com/problems/remove-duplicate-letters/description/

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 316. Remove Duplicate Letters
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s, remove duplicate letters so that every letter appears once and only once. You must make sure your result is the smallest in lexicographical order among all possible results.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "bcabc"
 * Output: "abc"
 * Example 2:
 *
 * Input: s = "cbacdcbc"
 * Output: "acdb"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s consists of lowercase English letters.
 *
 *
 * Note: This question is the same as 1081: https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/
 *
 */
public class RemoveDuplicateLetters {

  /**
   *  NOTE
   *
   *  Lexicographically Smaller
   *
   * A string a is lexicographically smaller than a
   * string b if in the first position where a and b differ,
   * string a has a letter that appears earlier in the alphabet
   * than the corresponding letter in b.
   * If the first min(a.length, b.length) characters do not differ,
   * then the shorter string is the lexicographically smaller one.
   *
   */

  // V0
  //    public String removeDuplicateLetters(String s) {
  //
  //    }

  // V0-1
  // IDEA: STACK (fixed by gpt)
  // Time: O(n) — one pass over the string and each character is pushed/popped at most once.
  // Space: O(1) — constant space for 26 characters (seen, freq, stack)
  /**
   * 📌 Example Walkthrough
   *
   * Input: "cbacdcbc"
   * 	1.	'c' → Stack: ["c"]
   * 	2.	'b' < 'c' and 'c' still appears → pop 'c', push 'b'
   * 	3.	'a' < 'b' → pop 'b', push 'a'
   * 	4.	'c' > 'a' → push 'c'
   * 	5.	'd' > 'c' → push 'd'
   * 	6.	'c' already seen → skip
   * 	7.	'b' > 'd' → push 'b'
   * 	8.	'c' > 'b' → push 'c'
   *
   * Final stack: ['a', 'c', 'd', 'b']
   * Lexicographically smallest valid string: "acdb"
   *
   */
  public String removeDuplicateLetters_0_1(String s) {
      if (s == null || s.length() == 0) {
          return "";
      }

    /**
     *  •	freq: array to count how many times each letter appears in s.
     * 	•	We use c - 'a' to map each character to index 0–25 ('a' to 'z').
     * 	•	This helps us later determine if we can remove a character and see it again later.
     */
    int[] freq = new int[26]; // frequency of each character
      for (char c : s.toCharArray()) {
          freq[c - 'a']++;
      }

    /**
     * 	•	Tracks which characters have already been added to the result.
     * 	•	This ensures we only include each character once.
     *
     *
     * 	NOTE !!! sean is a `boolean` array
     */
    boolean[] seen = new boolean[26]; // whether character is in stack/result

    /** NOTE !!!
     *
     *  we init stack here
     *
     *
     *  •	This stack is used to build the final result.
     * 	•	We’ll maintain characters in order and manipulate
     * 	    the top to maintain lexicographical order.
     */
    Stack<Character> stack = new Stack<>();

    /**
     * 	•	Iterate through the string one character at a time.
     * 	•	Since we’ve now processed c, decrement its frequency count.
     */
    for (char c : s.toCharArray()) {
          freq[c - 'a']--; // reduce frequency, since we're processing this char

        /**
         * 	•	If we’ve already added this character to the result,
         * 	    skip it — we only want one occurrence of each letter.
         */
          if (seen[c - 'a']) {
              continue; // already added, skip
          }

      /** NOTE !!!
       *
       * Now we’re checking:
       *
       * 	•	Is the stack NOT empty?
       *
       * 	•	Is the current character c lexicographically
       *      	smaller than the character at the top of the stack?
       *
       * 	•	Does the character at the top of the stack still
       *     	appear later (i.e., its freq > 0)?
       *
       * If yes to all, we can:
       *
       * 	•	pop it from the result,
       *
       * 	•	and add it later again in a better
       *     	position (lexicographically smaller order).
       */
      // remove characters that are bigger than current AND appear later again
      while (!stack.isEmpty() && c < stack.peek() && freq[stack.peek() - 'a'] > 0) {
              /**
               *
               * 	Remove the character from the stack,
               * 	and mark it as not seen so it can be added again later.
               */
              char removed = stack.pop();
              seen[removed - 'a'] = false;
          }

      /**
       * 	•	Push the current character c to the stack,
       * 	•	And mark it as seen (i.e., already in the result).
       */
      stack.push(c);
      seen[c - 'a'] = true;
      }

      // build result from stack
      StringBuilder sb = new StringBuilder();
      for (char c : stack) {
          sb.append(c);
      }

      return sb.toString();
  }

  // V1
  // IDEA: STACK
  // https://leetcode.com/problems/remove-duplicate-letters/solutions/76762/java-on-solution-using-stack-with-detail-z3nb/
  public String removeDuplicateLetters_1(String s) {
        Stack<Character> stack = new Stack<>();
        int[] count = new int[26];
        char[] arr = s.toCharArray();
        for (char c : arr) {
            count[c - 'a']++;
        }
        boolean[] visited = new boolean[26];
        for (char c : arr) {
            count[c - 'a']--;
            if (visited[c - 'a']) {
                continue;
            }
            while (!stack.isEmpty() && stack.peek() > c && count[stack.peek() - 'a'] > 0) {
                visited[stack.peek() - 'a'] = false;
                stack.pop();
            }
            stack.push(c);
            visited[c - 'a'] = true;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : stack) {
            sb.append(c);
        }
        return sb.toString();
    }

    // V2
    // https://leetcode.com/problems/remove-duplicate-letters/solutions/76766/easy-to-understand-iterative-java-soluti-bgeo/
    public String removeDuplicateLetters_2(String s) {
        if (s == null || s.length() <= 1)
            return s;

        Map<Character, Integer> lastPosMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            lastPosMap.put(s.charAt(i), i);
        }

        char[] result = new char[lastPosMap.size()];
        int begin = 0, end = findMinLastPos(lastPosMap);

        for (int i = 0; i < result.length; i++) {
            char minChar = 'z' + 1;
            for (int k = begin; k <= end; k++) {
                if (lastPosMap.containsKey(s.charAt(k)) && s.charAt(k) < minChar) {
                    minChar = s.charAt(k);
                    begin = k + 1;
                }
            }

            result[i] = minChar;
            if (i == result.length - 1)
                break;

            lastPosMap.remove(minChar);
            if (s.charAt(end) == minChar)
                end = findMinLastPos(lastPosMap);
        }

        return new String(result);
    }

    private int findMinLastPos(Map<Character, Integer> lastPosMap) {
        if (lastPosMap == null || lastPosMap.isEmpty())
            return -1;
        int minLastPos = Integer.MAX_VALUE;
        for (int lastPos : lastPosMap.values()) {
            minLastPos = Math.min(minLastPos, lastPos);
        }
        return minLastPos;
    }

    // V3
    // https://leetcode.com/problems/remove-duplicate-letters/solutions/76768/a-short-on-recursive-greedy-solution-by-z4daq/
    // IDEA: RECURSIVE
    public String removeDuplicateLetters_3(String s) {
        int[] cnt = new int[26];
        int pos = 0; // the position for the smallest s[i]
        for (int i = 0; i < s.length(); i++)
            cnt[s.charAt(i) - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(pos))
                pos = i;
            if (--cnt[s.charAt(i) - 'a'] == 0)
                break;
        }
        return s.length() == 0 ? ""
                : s.charAt(pos) + removeDuplicateLetters_3(s.substring(pos + 1).replaceAll("" + s.charAt(pos), ""));
    }

    // V3


}
