package LeetCodeJava.String;

// https://leetcode.com/problems/reverse-only-letters/description/

import java.util.Stack;

/**
 * 917. Reverse Only Letters
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a string s, reverse the string according to the following rules:
 *
 * All the characters that are not English letters remain in the same position.
 * All the English letters (lowercase or uppercase) should be reversed.
 * Return s after reversing it.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "ab-cd"
 * Output: "dc-ba"
 * Example 2:
 *
 * Input: s = "a-bC-dEf-ghIj"
 * Output: "j-Ih-gfE-dCba"
 * Example 3:
 *
 * Input: s = "Test1ng-Leet=code-Q!"
 * Output: "Qedo1ct-eeLg=ntse-T!"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists of characters with ASCII values in the range [33, 122].
 * s does not contain '\"' or '\\'.
 *
 *
 *
 */
public class ReverseOnlyLetters {

    // V0
//    public String reverseOnlyLetters(String s) {
//
//    }

    // V0-1
    // IDEA: 2 POINTERS (gemini)
    public String reverseOnlyLetters_0_1(String s) {
        // 1. Convert to char array for easy swapping
        char[] arr = s.toCharArray();
        int l = 0;
        int r = s.length() - 1;

        while (l < r) {
            /** NOTE !!!
             *
             *   1. check if r is still > l
             *   2. check if cur char is alphabet
             *
             */
            // 2. Move left pointer until it hits a letter
            while (l < r && !Character.isLetter(arr[l])) {
                l++;
            }
            /** NOTE !!!
             *
             *   1. check if r is still > l
             *   2. check if cur char is alphabet
             *
             */
            // 3. Move right pointer until it hits a letter
            while (l < r && !Character.isLetter(arr[r])) {
                r--;
            }

            // 4. Swap the letters
            char tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;

            // 5. Move pointers inward
            l++;
            r--;
        }

        // 6. Convert char array back to String
        return new String(arr);
    }

    // V0-2
    // IDEA: 2 POINTERS (gpt)
    public String reverseOnlyLetters_0_2(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }

        char[] arr = s.toCharArray();
        int l = 0;
        int r = arr.length - 1;

        while (l < r) {
            if (!Character.isLetter(arr[l])) {
                l++;
            } else if (!Character.isLetter(arr[r])) {
                r--;
            } else {
                char temp = arr[l];
                arr[l] = arr[r];
                arr[r] = temp;
                l++;
                r--;
            }
        }

        return new String(arr);
    }



    // V1-1
    // IDEA: Stack of Letters + stack (FILO)
    /**  IDEA
     *
     *  1. loop over string
     *  2. have a stack, save `alphabet` in stack
     *  3. loop over string again
     *    - for NON `alphabet`, we append as its original order
     *    - for  `alphabet`, we  pop in inverse order. (stack : FILO)
     *
     *
     */
    // https://leetcode.com/problems/reverse-only-letters/editorial/
    public String reverseOnlyLetters_1_1(String S) {

        // NOTE !!! stack: FILO
        Stack<Character> letters = new Stack();
        for (char c: S.toCharArray())
            if (Character.isLetter(c))
                letters.push(c);

        StringBuilder ans = new StringBuilder();
        for (char c: S.toCharArray()) {
            if (Character.isLetter(c))
                ans.append(letters.pop());
            else
                ans.append(c);
        }

        return ans.toString();
    }

    // V1-2
    // IDEA: Reverse Pointer
    // https://leetcode.com/problems/reverse-only-letters/editorial/
    public String reverseOnlyLetters_1_2(String S) {
        StringBuilder ans = new StringBuilder();
        int j = S.length() - 1;
        for (int i = 0; i < S.length(); ++i) {
            if (Character.isLetter(S.charAt(i))) {
                while (!Character.isLetter(S.charAt(j)))
                    j--;
                ans.append(S.charAt(j--));
            } else {
                ans.append(S.charAt(i));
            }
        }

        return ans.toString();
    }


    // V2



}
