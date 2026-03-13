package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/shortest-palindrome/description/
/**
 *  214. Shortest Palindrome
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given a string s. You can convert s to a palindrome by adding characters in front of it.
 *
 * Return the shortest palindrome you can find by performing this transformation.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aacecaaa"
 * Output: "aaacecaaa"
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: "dcbabcd"
 *
 *
 * Constraints:
 *
 * 0 <= s.length <= 5 * 104
 * s consists of lowercase English letters only.
 */
public class ShortestPalindrome {

    // V0
//    public String shortestPalindrome(String s) {
//
//    }

    /** NOTE !!!
     *
     *  why we CAN'T use `left, right pointer` approach ?
     *
     *   (left = 0, right = s.len() - 1)
     *
     *   -------------------
     *
     *   Explanation (gpt):
     *
     *   You **can use a two-pointer idea**, but the way your original code uses `left` and `right` **cannot guarantee correctness or efficiency** for **LeetCode 214 Shortest Palindrome**. Let's break down **why it fails**.
     *
     * ---
     *
     * # 1️⃣ Your algorithm changes the string while scanning
     *
     * This is the biggest issue.
     *
     * You do:
     *
     * ```java
     * s = s.substring(0, l-1) + right + s.substring(l, r);
     * ```
     *
     * This **modifies the string while the pointers are still based on the old string**.
     *
     * Example:
     *
     * ```
     * s = "abcd"
     * ```
     *
     * Your algorithm might try to insert characters while `l` and `r` still assume the original indices. After modification:
     *
     * * indices shift
     * * pointer positions become invalid
     * * comparisons become meaningless
     *
     * So the pointer logic **breaks immediately**.
     *
     * ---
     *
     * # 2️⃣ The problem is not a normal palindrome check
     *
     * Typical two-pointer palindrome problems (like **LeetCode 125 Valid Palindrome**) work because:
     *
     * ```
     * compare left ↔ right
     * ```
     *
     * But **LC 214** is different:
     *
     * > You can only **add characters to the FRONT**.
     *
     * So the goal is actually:
     *
     * ```
     * find the longest PALINDROME PREFIX
     * ```
     *
     * Example:
     *
     * ```
     * s = "abcd"
     * ```
     *
     * Possible results:
     *
     * ```
     * dcbabcd
     * ```
     *
     * Notice:
     *
     * ```
     * abcd   ❌ not palindrome
     * abc    ❌
     * ab     ❌
     * a      ✅ longest prefix palindrome
     * ```
     *
     * Two pointers comparing ends **cannot detect this prefix property correctly**.
     *
     * ---
     *
     * # 3️⃣ Your `isPalindrome(s)` check is too expensive
     *
     * Inside your loop you do:
     *
     * ```java
     * if(isPalindrome(s))
     * ```
     *
     * Your complexity becomes roughly:
     *
     * ```
     * O(n) loop
     *   × O(n) palindrome check
     * ```
     *
     * Total:
     *
     * ```
     * O(n²)
     * ```
     *
     * And because you **modify the string**, it can even degrade further.
     *
     * ---
     *
     * # 4️⃣ Two pointers cannot decide where to insert
     *
     * Consider:
     *
     * ```
     * s = "aacecaaa"
     * ```
     *
     * Correct answer:
     *
     * ```
     * aaacecaaa
     * ```
     *
     * Two pointers see:
     *
     * ```
     * a a c e c a a a
     * ^             ^
     * ```
     *
     * But they **cannot determine which prefix is the maximal palindrome**.
     *
     * The real solution must find:
     *
     * ```
     * longest prefix palindrome
     * ```
     *
     * which requires **global prefix information**, not just local pointer comparison.
     *
     * ---
     *
     * # 5️⃣ Counterexample where your logic breaks
     *
     * Example:
     *
     * ```
     * s = "abbacd"
     * ```
     *
     * Correct result:
     *
     * ```
     * dcabbacd
     * ```
     *
     * Two pointers:
     *
     * ```
     * a b b a c d
     * ^         ^
     * ```
     *
     * Mismatch occurs but **you cannot know how many characters to prepend**.
     *
     * ---
     *
     * # 6️⃣ What the correct algorithm actually does
     *
     * The key step is:
     *
     * ```
     * scan from right → find longest prefix palindrome
     * ```
     *
     * Example:
     *
     * ```
     * s = aacecaaa
     * ```
     *
     * Compare mirrored characters:
     *
     * ```
     * i ↓
     * j ↑
     * ```
     *
     * When characters match → extend palindrome prefix.
     *
     * ---
     *
     * # 7️⃣ Two pointers CAN be used, but differently
     *
     * A valid two-pointer-like solution is:
     *
     * ```java
     * int j = 0;
     * for(int i = s.length()-1; i >= 0; i--){
     *     if(s.charAt(i) == s.charAt(j)){
     *         j++;
     *     }
     * }
     * ```
     *
     * This works because it finds the **longest prefix palindrome**.
     *
     * ---
     *
     * ✅ **Summary**
     *
     * Your original approach fails because:
     *
     * 1. It **mutates the string while iterating**
     * 2. It **checks full palindrome instead of prefix**
     * 3. Two pointers **cannot determine insertion length**
     * 4. Pointer indices **break after modification**
     *
     * ---
     *
     * 💡 **Interview insight**
     *
     * For **LeetCode 214 Shortest Palindrome**, interviewers expect one of these:
     *
     * | Approach                 | Complexity      |
     * | ------------------------ | --------------- |
     * | Brute force prefix check | O(n²)           |
     * | Two-pointer prefix scan  | O(n²) worst     |
     * | KMP trick                | **O(n)** (best) |
     *
     * ---
     *
     */

    // V0-0-1
    // IDEA: BRUTE FORCE (TLE) (gemini)
    /**  NOTE !!!  Dry run demo (a bit tricky)
     *
     *  input = "aacecaaa"
     *
     *   -> end = 6 (via isPalindrome)
     *   -> suffix = "a"
     *   -> res = suffix.reverse() + input
     *          = "a" + "aacecaaa"
     *          = "aaacecaaa"
     *
     * -----------------
     *
     *  -> dry run
     *
     *
     * ### Why start from the right?
     *
     * We start `i` at `n - 1` because we are greedy.
     * We want to find the **largest** possible chunk
     * at the front that is already a palindrome.
     *
     * 1. If the loop finds a palindrome at `i = 7`, we add nothing to the front.
     * 2. If it finds one at `i = 6` (like in our example), we only have to add **one** character to the front.
     * 3. If it doesn't find one until `i = 0`, we have to reverse and add **almost the whole string**.
     *
     * ### Let's trace "aacecaaa" with your loop:
     *
     * * **Iteration 1: `i = 7**`
     * * Check `isPalindrome(s, 0, 7)` $\rightarrow$ `"aacecaaa"`
     * * Result: **False** (Index 2 is 'c', Index 5 is 'a').
     *
     *
     * * **Iteration 2: `i = 6**`
     * * Check `isPalindrome(s, 0, 6)` $\rightarrow$ `"aacecaa"`
     * * Result: **True**!
     * * `end = 6`.
     * * **Break.**
     *
     *
     *
     * ### Now, where is the suffix?
     *
     * The suffix is everything **after** our `end` index.
     *
     * ```java
     * // Our string: a a c e c a a [a]
     * // Indices:    0 1 2 3 4 5 6  7
     * //                          ^
     * //                         end
     *
     * String suffix = s.substring(end + 1); // substring(7)
     * // suffix is "a"
     *
     * ```
     *
     * ---
     *
     * ### 💡 Summary of the logic
     *
     * * The **loop** finds the "Anchor" (the biggest palindrome we already have at the start).
     * * The **suffix** is everything that was "left out" of that anchor.
     * * We reverse the "left out" part and put it at the front to balance the scales.
     *
     */
    public String shortestPalindrome_0_0_1(String s) {
        int n = s.length();
        if (n <= 1)
            return s;

        // 1. Find the longest prefix that is a palindrome
        // We start from the end and check [0...i]
        /**
         *  NOTE !!!
         *
         *   We start from the `end` and check `[0...i]`
         *
         */
        int end = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (isPalindrome(s, 0, i)) {
                end = i;
                break;
            }
        }

        // 2. The part that is NOT part of the prefix palindrome
        // needs to be reversed and added to the front.
        /**
         *  NOTE !!!
         *
         *   We get suffix (sub-string) with begin idx =  `end + 1`
         *
         */
        String suffix = s.substring(end + 1);
        StringBuilder sb = new StringBuilder(suffix);
        return sb.reverse().toString() + s;
    }

    private boolean isPalindrome(String x, int l, int r) {
        while (l < r) {
            if (x.charAt(l) != x.charAt(r))
                return false;
            l++;
            r--;
        }
        return true;
    }


    // V0-1
    // IDEA: Find the longest prefix that is a palindrome + 2 POINTERS (gpt)
    /**  NOTE !!!
     *
     *  core idea:
     *    Find the longest prefix that is a palindrome.
     *
     *      -> Then add the reverse of the
     *         remaining suffix to the front.
     */
    public String shortestPalindrome_0_1(String s) {

        if (s == null || s.length() <= 1) {
            return s;
        }

        int j = 0;

        /** NOTE !!!
         *
         * start from right hand side (s.length() - 1)
         *
         */
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
        }

        // already palindrome
        if (j == s.length()) {
            return s;
        }

        String suffix = s.substring(j);

        /** NOTE !!!
         *
         * how we build prefix
         *
         */
        String prefix = new StringBuilder(suffix).reverse().toString();

        /** NOTE !!!
         *
         * how we prepare final res
         *
         */
        return prefix + shortestPalindrome_0_1(s.substring(0, j)) + suffix;
    }


    // V0-2
    // IDEA: PREFIX (gemini)
    public String shortestPalindrome_0_2(String s) {
        int n = s.length();
        if (n <= 1)
            return s;

        // 1. Find the longest palindromic prefix
        // We use a pointer 'j' to see how much of the start of 's' matches the end of 's'
        int j = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
        }

        // 2. If j reached the end, the whole string is already a palindrome
        if (j == n)
            return s;

        // 3. Everything from index j to the end is the "non-palindrome" part
        String suffix = s.substring(j);
        String reverseSuffix = new StringBuilder(suffix).reverse().toString();

        // 4. Recurse on the prefix part to find the shortest within that,
        // then sandwich the current non-palindrome part around it.
        return reverseSuffix + shortestPalindrome_0_2(s.substring(0, j)) + suffix;
    }



    // V0-4
    // IDEA: 2 POINTER AND palindrome check (gemini) (TLE)
    public String shortestPalindrome_0_4(String s) {
        // 1. Edge Case
        if (s == null || s.length() <= 1) {
            return s;
        }

        int n = s.length();
        // 2. We use 'r' to find the largest prefix [0...r] that is a palindrome
        // We start from the very end and move backwards
        int r = n - 1;

        while (r >= 0) {
            // If the current substring from 0 to r is a palindrome, we found our anchor!
            if (isPalindrome_0_4(s, 0, r)) {
                break;
            }
            r--;
        }

        // 3. Everything from index (r + 1) to the end is the "non-palindrome" part
        // We need to reverse this part and put it at the very beginning
        String suffix = s.substring(r + 1);
        StringBuilder sb = new StringBuilder(suffix);
        String reversedSuffix = sb.reverse().toString();

        return reversedSuffix + s;
    }

    // Optimized isPalindrome to use pointers instead of creating new substrings
    private boolean isPalindrome_0_4(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }



    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_1(String s) {
        int length = s.length();
        String reversedString = new StringBuilder(s).reverse().toString();

        // Iterate through the string to find the longest palindromic prefix
        for (int i = 0; i < length; i++) {
            if (s.substring(0, length - i).equals(reversedString.substring(i))) {
                return new StringBuilder(reversedString.substring(0, i))
                        .append(s)
                        .toString();
            }
        }
        return "";
    }


    // V1-2
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_2(String s) {
        int length = s.length();
        if (length == 0) {
            return s;
        }

        // Find the longest palindromic prefix
        int left = 0;
        for (int right = length - 1; right >= 0; right--) {
            if (s.charAt(right) == s.charAt(left)) {
                left++;
            }
        }

        // If the whole string is a palindrome, return the original string
        if (left == length) {
            return s;
        }

        // Extract the suffix that is not part of the palindromic prefix
        String nonPalindromeSuffix = s.substring(left);
        StringBuilder reverseSuffix = new StringBuilder(
                nonPalindromeSuffix
        ).reverse();

        // Form the shortest palindrome by prepending the reversed suffix
        return reverseSuffix
                .append(shortestPalindrome_1_2(s.substring(0, left)))
                .append(nonPalindromeSuffix)
                .toString();
    }


    // V1-3
    // IDEA: KMP (Knuth-Morris-Pratt) ALGO
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_3(String s) {
        String reversedString = new StringBuilder(s).reverse().toString();
        String combinedString = s + "#" + reversedString;
        int[] prefixTable = buildPrefixTable(combinedString);

        int palindromeLength = prefixTable[combinedString.length() - 1];
        StringBuilder suffix = new StringBuilder(
                s.substring(palindromeLength)).reverse();
        return suffix.append(s).toString();
    }

    private int[] buildPrefixTable(String s) {
        int[] prefixTable = new int[s.length()];
        int length = 0;
        for (int i = 1; i < s.length(); i++) {
            while (length > 0 && s.charAt(i) != s.charAt(length)) {
                length = prefixTable[length - 1];
            }
            if (s.charAt(i) == s.charAt(length)) {
                length++;
            }
            prefixTable[i] = length;
        }
        return prefixTable;
    }


    // V1-4
    // IDEA: Rolling Hash Based Algorithm
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_4(String s) {
        long hashBase = 29;
        long modValue = (long) 1e9 + 7;
        long forwardHash = 0, reverseHash = 0, powerValue = 1;
        int palindromeEndIndex = -1;

        // Calculate rolling hashes and find the longest palindromic prefix
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);

            // Update forward hash
            forwardHash = (forwardHash * hashBase + (currentChar - 'a' + 1)) %
                    modValue;

            // Update reverse hash
            reverseHash = (reverseHash + (currentChar - 'a' + 1) * powerValue) %
                    modValue;
            powerValue = (powerValue * hashBase) % modValue;

            // If forward and reverse hashes match, update palindrome end index
            if (forwardHash == reverseHash) {
                palindromeEndIndex = i;
            }
        }

        // Find the remaining suffix after the longest palindromic prefix
        String suffix = s.substring(palindromeEndIndex + 1);
        // Reverse the remaining suffix
        StringBuilder reversedSuffix = new StringBuilder(suffix).reverse();

        // Prepend the reversed suffix to the original string and return the result
        return reversedSuffix.append(s).toString();
    }


    // V1-5
    // IDEA: Manacher's Algorithm
    // https://leetcode.com/problems/shortest-palindrome/editorial/

    public String shortestPalindrome_1_5(String s) {
        // Return early if the string is null or empty
        if (s == null || s.length() == 0) {
            return s;
        }

        // Preprocess the string to handle palindromes uniformly
        String modifiedString = preprocessString(s);
        int[] palindromeRadiusArray = new int[modifiedString.length()];
        int center = 0, rightBoundary = 0;
        int maxPalindromeLength = 0;

        // Iterate through each character in the modified string
        for (int i = 1; i < modifiedString.length() - 1; i++) {
            int mirrorIndex = 2 * center - i;

            // Use previously computed values to avoid redundant calculations
            if (rightBoundary > i) {
                palindromeRadiusArray[i] = Math.min(
                        rightBoundary - i,
                        palindromeRadiusArray[mirrorIndex]);
            }

            // Expand around the current center while characters match
            while (modifiedString.charAt(i + 1 + palindromeRadiusArray[i]) == modifiedString
                    .charAt(i - 1 - palindromeRadiusArray[i])) {
                palindromeRadiusArray[i]++;
            }

            // Update the center and right boundary if the palindrome extends beyond the current boundary
            if (i + palindromeRadiusArray[i] > rightBoundary) {
                center = i;
                rightBoundary = i + palindromeRadiusArray[i];
            }

            // Update the maximum length of palindrome starting at the beginning
            if (i - palindromeRadiusArray[i] == 1) {
                maxPalindromeLength = Math.max(
                        maxPalindromeLength,
                        palindromeRadiusArray[i]);
            }
        }

        // Construct the shortest palindrome by reversing the suffix and prepending it to the original string
        StringBuilder suffix = new StringBuilder(
                s.substring(maxPalindromeLength)).reverse();
        return suffix.append(s).toString();
    }

    private String preprocessString(String s) {
        // Add boundaries and separators to handle palindromes uniformly
        StringBuilder sb = new StringBuilder("^");
        for (char c : s.toCharArray()) {
            sb.append("#").append(c);
        }
        return sb.append("#$").toString();
    }


    // V2



    // V3




}
