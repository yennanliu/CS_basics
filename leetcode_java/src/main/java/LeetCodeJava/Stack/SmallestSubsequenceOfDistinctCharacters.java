package LeetCodeJava.Stack;

// https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/description/

import java.util.Stack;

/**
 *  1081. Smallest Subsequence of Distinct Characters
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a string s, return the lexicographically smallest subsequence of s that contains all the distinct characters of s exactly once.
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
 * 1 <= s.length <= 1000
 * s consists of lowercase English letters.
 *
 *
 * Note: This question is the same as 316: https://leetcode.com/problems/remove-duplicate-letters/
 *
 *
 */
public class SmallestSubsequenceOfDistinctCharacters {

    /**
     * Note: This question is the same
     * as 316: https://leetcode.com/problems/remove-duplicate-letters/
     */

    // V0
//    public String smallestSubsequence(String s) {
//
//    }


    // V1


    // V2
    // IDEA: STACK
    // https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/solutions/7659799/greedy-stack-approach-to-get-lexicograph-ikmt/
    public String smallestSubsequence_2(String s) {
        int[] freq = new int[26];
        boolean[] seen = new boolean[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        Stack<Character> st = new Stack<>();
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            freq[index]--;
            if (seen[index])
                continue;
            while (!st.isEmpty() && st.peek() > c && freq[st.peek() - 'a'] > 0) {
                seen[st.pop() - 'a'] = false;
            }
            st.push(c);
            seen[index] = true;
        }
        StringBuilder str = new StringBuilder();
        for (char c : st) {
            str.append(c);
        }

        return str.toString();

    }


    // V3
    // IDEA: STACK
    // https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/solutions/889488/java-solution-using-stack-by-credit_card-qbak/
    public String smallestSubsequence_3(String s) {
        //character frequency
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        //to keep track of visited character so that we don't use them if present in answer
        boolean[] visited = new boolean[26];

        //Stack store resulting characters
        Stack<Character> stack = new Stack<>();
        //traverse through string and add characters
        for (char c : s.toCharArray()) {
            //dcrement the frequncy of character since we are using it in answer
            //!!! We have decrement the character frequncy before checking it is visited
            count[c - 'a']--;

            //if already present in tstack we dont need the character
            if (visited[c - 'a']) {
                continue;
            }

            //traverse through the stack and check for larger characters
            //if found and it is not the last position then pop from stack
            //Eg: bcabc => if stack has bc, now a<b and curr b is not the last one
            //if not in last position come out of loop and add curr character to stack
            while (!stack.isEmpty() && c < stack.peek() && count[stack.peek() - 'a'] > 0) {
                //make the current character available for next operations
                visited[stack.pop() - 'a'] = false;
            }
            //add curr charatcer to string
            stack.push(c);
            //mark it as vistied
            visited[c - 'a'] = true;
        }

        //Now characters are in reverse order in stack
        //Use StringBuilder instead of String for storing result
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }

        return sb.reverse().toString();

    }



    // V4




}
