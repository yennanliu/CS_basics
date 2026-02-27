package LeetCodeJava.HashTable;

// https://leetcode.com/problems/palindrome-pairs/description/

import java.util.*;

/**
 * 336. Palindrome Pairs
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed array of unique strings words.
 *
 * A palindrome pair is a pair of integers (i, j) such that:
 *
 * 0 <= i, j < words.length,
 * i != j, and
 * words[i] + words[j] (the concatenation of the two strings) is a palindrome.
 * Return an array of all the palindrome pairs of words.
 *
 * You must write an algorithm with O(sum of words[i].length) runtime complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["abcd","dcba","lls","s","sssll"]
 * Output: [[0,1],[1,0],[3,2],[2,4]]
 * Explanation: The palindromes are ["abcddcba","dcbaabcd","slls","llssssll"]
 * Example 2:
 *
 * Input: words = ["bat","tab","cat"]
 * Output: [[0,1],[1,0]]
 * Explanation: The palindromes are ["battab","tabbat"]
 * Example 3:
 *
 * Input: words = ["a",""]
 * Output: [[0,1],[1,0]]
 * Explanation: The palindromes are ["a","a"]
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 5000
 * 0 <= words[i].length <= 300
 * words[i] consists of lowercase English letters.
 *
 */
public class PalindromePairs {

    // V0
//    public List<List<Integer>> palindromePairs(String[] words) {
//
//    }


    // V0-0-3
    // IDEA: BRUTE FORCE (TLE) (gemini)
    public List<List<Integer>> palindromePairs_0_0_3(String[] words) {
        List<List<Integer>> res = new ArrayList<>();
        if (words == null || words.length == 0)
            return res;

        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {

                // Test Combination 1: words[i] + words[j]
                String s1 = words[i] + words[j];
                if (isPalindrome_0_0_3(s1)) {
                    res.add(Arrays.asList(i, j));
                }

                // Test Combination 2: words[j] + words[i]
                String s2 = words[j] + words[i];
                if (isPalindrome_0_0_3(s2)) {
                    res.add(Arrays.asList(j, i));
                }
            }
        }

        // Optional: Sort result by first index
        res.sort((a, b) -> a.get(0).compareTo(b.get(0)));

        return res;
    }

    private boolean isPalindrome_0_0_3(String x) {
        int l = 0;
        int r = x.length() - 1;
        while (l < r) {
            if (x.charAt(l++) != x.charAt(r--)) {
                return false;
            }
        }
        return true; // Corrected: return true if loop finishes
    }


    // V0-1
    // IDEA: HASHMAP (fixed by gemini)
    /**
     * Finds all unique pairs of indices (i, j) such that the concatenation
     * words[i] + words[j] is a palindrome.
     * * @param words The array of strings.
     * @return A list of lists, where each inner list is a pair of indices.
     */
    public List<List<Integer>> palindromePairs_0_1(String[] words) {
        List<List<Integer>> res = new ArrayList<>();

        // 1. Map to store word -> index for O(1) lookup
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }

        // 2. Iterate through each word and check all possible splits
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            // Split 'word' into two parts: prefix (s1) and suffix (s2)
            // s1 = word.substring(0, j)
            // s2 = word.substring(j)
            // The split point 'j' goes from 0 to word.length() (inclusive)
            for (int j = 0; j <= word.length(); j++) {
                String s1 = word.substring(0, j);
                String s2 = word.substring(j);

                /** Case 1: w1 = s1, w2 = reverse(s2). We want w1 + w2 to be a palindrome. */
                // Case 1: w1 = s1, w2 = reverse(s2). We want w1 + w2 to be a palindrome.
                // words[i] + words[k] = s1 + s2 + reverse(s2)
                // For this to be a palindrome, s1 must be equal to reverse(s2).
                //
                /** Check 1.1: If s1 is a palindrome (or empty) AND reverse(s2) is in the map */
                //   - word: s2 + s1
                //   - candidate pair: words[k] + words[i]
                //   - words[k] must be reverse(s1) for the total to be a palindrome.
                //   - Example: word[i]="s-e-e-s" (s2="s-e-e-s", s1=""), reverse(s1)="" exists at k. Result: "" + "s-e-e-s"
                //   - The correct logic is simpler:

                /**  Check 1.2: If s1 is a palindrome, check if reverse(s2) exists in the map */
                // If s1 is a palindrome, we need words[k] to be reverse(s2) such that
                // words[i] + words[k] = s1 + s2 + reverse(s2) is a palindrome.
                if (isPalindrome_0_1(s1)) {
                    String s2Reversed = reverseStr_0_1(s2);
                    if (map.containsKey(s2Reversed)) {
                        int k = map.get(s2Reversed);
                        // Prevent adding the same word twice (e.g., words[i] + words[i])
                        if (i != k) {
                            // words[k] (reverse(s2)) + words[i] (s1 + s2)
                            // Concatenation: reverse(s2) + s1 + s2.
                            // Since s1 is a palindrome, this forms a palindrome.

                            // res.add(List.of(k, i));
                            // fix for java 8
                            res.add(Arrays.asList(k, i));
                        }
                    }
                }

                /** Case 2: w1 = reverse(s1), w2 = s2. We want w1 + w2 to be a palindrome.  */
                // words[k] + words[i] = reverse(s1) + s1 + s2
                // For this to be a palindrome, s2 must be a palindrome.

                /** Check 2.1: If s2 is a palindrome, check if reverse(s1) exists in the map */
                // Note: s2 must not be empty. If j=0, s2=word, s1="". This case is covered by Case 1 with j=word.length() (s1=word, s2="").
                if (s2.length() != 0 && isPalindrome_0_1(s2)) {
                    String s1Reversed = reverseStr_0_1(s1);
                    if (map.containsKey(s1Reversed)) {
                        int k = map.get(s1Reversed);
                        // Prevent adding the same word twice and avoid duplicates already covered in Case 1.
                        // For the pair (i, k), if i < k, it will be covered in Case 1 when we process word[i].
                        // Since we already added (k, i) in Case 1, we only need to add (i, k) here.
                        // The main reason is to avoid duplicates, the split logic ensures we find all pairs.
                        if (i != k) {
                            // words[i] (s1 + s2) + words[k] (reverse(s1))
                            // Concatenation: s1 + s2 + reverse(s1).
                            // Since s2 is a palindrome, this forms a palindrome.

                            //res.add(List.of(i, k));
                            // fix for java 8
                            res.add(Arrays.asList(i, k));
                        }
                    }
                }
            }
        }

        return res;
    }

    /**
     * Helper to reverse a string.
     */
    private String reverseStr_0_1(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Helper to check if a string is a palindrome.
     */
    private boolean isPalindrome_0_1(String str) {
        int l = 0;
        int r = str.length() - 1;
        while (l < r) {
            if (str.charAt(l) != str.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    // V0-2
    // IDEA: HASHMAP +  prefix - suffix palindrome check (fixed by gpt)
    public List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> res = new ArrayList<>();
        if (words == null || words.length == 0)
            return res;

        // 1️⃣ Build a map from reversed word -> index
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(new StringBuilder(words[i]).reverse().toString(), i);
        }

        // 2️⃣ For each word, check palindrome pairs
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            /**  NOTE !!! trick: prefix - suffix palindrome check
             *
             *  - That double loop with prefix/suffix splitting is the core trick for solving LeetCode 336.
             *    Palindrome Pairs efficiently.
             *
             *  1. we go through each word in word list
             *  2. with EACH word, we do below check
             *     - 2-1: sample `prefix` from 0 till end of word,
             *            and check if the `palindrome of prefix` ever EXISTS in the hashmap
             *            -> if yes, means we can find the `pair of palindrome` from word list
             *
             *     - 2-2: sample `suffix` from 0 till end of word
             *            and check if the `palindrome of suffix` ever EXISTS in the hashmap
             *            -> if yes, means we can find the `pair of palindrome` from word list
             *
             *
             *  ------------------------
             *
             *
             *   For any two words A and B,
             *     → A + B is a palindrome iff
             *      one part of A is already a palindrome, and the other part of
             *      A matches the reverse of B.
             *
             *   So for each word, we split it into every possible prefix | suffix, and:
             *
             * 	  •	If the prefix is a palindrome, then to make the whole string palindrome,
             * 	    we need a word that equals reverse(suffix) in front of it.
             *
             * 	  •	If the suffix is a palindrome, then to make the whole string palindrome,
             * 	     we need a word that equals reverse(prefix) after it.
             *
             *
             *  ------------------------
             *
             *  📝 Why two cases?
             *
             *   We have to handle both:
             * 	    •	Case A: reversed(suffix) + word
             *           This handles situations like ["dcba", "abcd"] → abcd has
             *           palindrome prefix, so you prepend dcba.
             * 	    •	Case B: word + reversed(prefix)
             *          This handles situations like ["abcd", "dcba"] → abcd
             *          has palindrome suffix, so you append dcba.
             *
             * If we only did one direction, we’d miss half of the valid pairs.
             *
             *  ------------------------
             *
             *   Example:
             *
             *   Take word = "abcd", reversed map contains "dcba" -> 1.
             *   Split:
             * 	    •	"" | "abcd" → prefix palindrome, look up "abcd" → no match.
             * 	    •	"a" | "bcd" → prefix palindrome (a), lookup reversed suffix "dcb" → no match.
             * 	    •	"ab" | "cd" → no palindrome.
             * 	    •	"abc" | "d" → no palindrome.
             * 	    •	"abcd" | "" → suffix palindrome (empty), lookup reversed prefix, "dcba" → found index 1. → [0,1].
             *
             *
             *   Similarly, "dcba" finds [1,0].
             *
             */
            // Check all possible splits: prefix | suffix
            for (int cut = 0; cut <= word.length(); cut++) {
                String prefix = word.substring(0, cut);
                String suffix = word.substring(cut);

                // Case A: prefix is palindrome, look for reversed suffix
                if (isPalindrome_0_2(prefix)) {
                    Integer j = map.get(suffix);
                    if (j != null && j != i) {
                        // reversed(suffix) + word forms palindrome
                        res.add(Arrays.asList(j, i));
                    }
                }

                // Case B: suffix is palindrome, look for reversed prefix
                // avoid double counting when cut == word.length()
                if (cut != word.length() && isPalindrome_0_2(suffix)) {
                    Integer j = map.get(prefix);
                    if (j != null && j != i) {
                        // word + reversed(prefix) forms palindrome
                        res.add(Arrays.asList(i, j));
                    }
                }
            }
        }

        return res;
    }

    private boolean isPalindrome_0_2(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--))
                return false;
        }
        return true;
    }


    // V1
    // https://leetcode.com/problems/palindrome-pairs/solutions/79210/the-easy-to-unserstand-java-solution-by-09k79/
    public List<List<Integer>> palindromePairs_1(String[] words) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (words == null || words.length == 0) {
            return res;
        }
        //build the map save the key-val pairs: String - idx
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }

        //special cases: "" can be combine with any palindrome string
        if (map.containsKey("")) {
            int blankIdx = map.get("");
            for (int i = 0; i < words.length; i++) {
                if (isPalindrome(words[i])) {
                    if (i == blankIdx)
                        continue;
                    res.add(Arrays.asList(blankIdx, i));
                    res.add(Arrays.asList(i, blankIdx));
                }
            }
        }

        //find all string and reverse string pairs
        for (int i = 0; i < words.length; i++) {
            String cur_r = reverseStr(words[i]);
            if (map.containsKey(cur_r)) {
                int found = map.get(cur_r);
                if (found == i)
                    continue;
                res.add(Arrays.asList(i, found));
            }
        }

        //find the pair s1, s2 that
        //case1 : s1[0:cut] is palindrome and s1[cut+1:] = reverse(s2) => (s2, s1)
        //case2 : s1[cut+1:] is palindrome and s1[0:cut] = reverse(s2) => (s1, s2)
        for (int i = 0; i < words.length; i++) {
            String cur = words[i];
            for (int cut = 1; cut < cur.length(); cut++) {
                if (isPalindrome(cur.substring(0, cut))) {
                    String cut_r = reverseStr(cur.substring(cut));
                    if (map.containsKey(cut_r)) {
                        int found = map.get(cut_r);
                        if (found == i)
                            continue;
                        res.add(Arrays.asList(found, i));
                    }
                }
                if (isPalindrome(cur.substring(cut))) {
                    String cut_r = reverseStr(cur.substring(0, cut));
                    if (map.containsKey(cut_r)) {
                        int found = map.get(cut_r);
                        if (found == i)
                            continue;
                        res.add(Arrays.asList(i, found));
                    }
                }
            }
        }

        return res;
    }
    /**
     * time = O(N)
     * space = O(N)
     */
    public String reverseStr(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i <= j) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    // V2
    // IDEA: HASH MAP
    // https://leetcode.com/problems/palindrome-pairs/solutions/1269310/js-python-java-c-easy-map-matching-solut-ln8m/
    public List<List<Integer>> palindromePairs_2(String[] words) {
        Map<String, Integer> wmap = new HashMap<>();
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < words.length; i++)
            wmap.put(words[i], i);
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("")) {
                for (int j = 0; j < words.length; j++) {
                    String w = words[j];
                    if (isPal(w, 0, w.length()-1) && j != i) {

                        //ans.add(List.of(i, j));
                        //ans.add(List.of(j, i));
                        // fix for java 8
                        ans.add(Arrays.asList(i, j));
                        ans.add(Arrays.asList(j, i));
                    }
                }
                continue;
            }
            StringBuilder sb = new StringBuilder(words[i]);
            sb.reverse();
            String bw = sb.toString();
            if (wmap.containsKey(bw)) {
                int res = wmap.get(bw);
                // fix for java 8
                //if (res != i) ans.add(List.of(i, res));
                if (res != i){
                    ans.add(Arrays.asList(i, res));
                }
            }
            for (int j = 1; j < bw.length(); j++) {
                if (isPal(bw, 0, j-1)) {
                    String s = bw.substring(j);
//                    if (wmap.containsKey(s))
//                        ans.add(List.of(i, wmap.get(s)));
                    if (wmap.containsKey(s)){
                        //ans.add(List.of(i, wmap.get(s)));
                        ans.add(Arrays.asList(i, wmap.get(s)));
                    }
                }
                if (isPal(bw, j, bw.length()-1)) {
                    String s = bw.substring(0,j);
//                    if (wmap.containsKey(s))
//                        ans.add(List.of(wmap.get(s), i));
                    if (wmap.containsKey(s)){
                        ans.add(Arrays.asList(wmap.get(s), i));
                    }

                }
            }
        }
        return ans;
    }

    private boolean isPal(String word, int i, int j) {
        while (i < j)
            if (word.charAt(i++) != word.charAt(j--)) return false;
        return true;
    }

    // V3
    // https://leetcode.com/problems/palindrome-pairs/solutions/2585678/java-easy-solution-detailed-explanation-5jd4s/
    public List<List<Integer>> palindromePairs_3(String[] words) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (words == null || words.length == 0) {
            return res;
        }
        //build the map save the key-val pairs: String - idx
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }

        //special cases: "" can be combine with any palindrome string
        if (map.containsKey("")) {
            int blankIdx = map.get("");
            for (int i = 0; i < words.length; i++) {
                if (isPalindrome_3(words[i])) {
                    if (i == blankIdx)
                        continue;
                    res.add(Arrays.asList(blankIdx, i));
                    res.add(Arrays.asList(i, blankIdx));
                }
            }
        }

        //find all string and reverse string pairs
        for (int i = 0; i < words.length; i++) {
            String cur_r = reverseStr_3(words[i]);
            if (map.containsKey(cur_r)) {
                int found = map.get(cur_r);
                if (found == i)
                    continue;
                res.add(Arrays.asList(i, found));
            }
        }

        //find the pair s1, s2 that
        //case1 : s1[0:cut] is palindrome and s1[cut+1:] = reverse(s2) => (s2, s1)
        //case2 : s1[cut+1:] is palindrome and s1[0:cut] = reverse(s2) => (s1, s2)
        for (int i = 0; i < words.length; i++) {
            String cur = words[i];
            for (int cut = 1; cut < cur.length(); cut++) {
                if (isPalindrome_3(cur.substring(0, cut))) {
                    String cut_r = reverseStr_3(cur.substring(cut));
                    if (map.containsKey(cut_r)) {
                        int found = map.get(cut_r);
                        if (found == i)
                            continue;
                        res.add(Arrays.asList(found, i));
                    }
                }
                if (isPalindrome_3(cur.substring(cut))) {
                    String cut_r = reverseStr_3(cur.substring(0, cut));
                    if (map.containsKey(cut_r)) {
                        int found = map.get(cut_r);
                        if (found == i)
                            continue;
                        res.add(Arrays.asList(i, found));
                    }
                }
            }
        }

        return res;
    }
    /**
     * time = O(N)
     * space = O(N)
     */
    public String reverseStr_3(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome_3(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i <= j) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }




}
