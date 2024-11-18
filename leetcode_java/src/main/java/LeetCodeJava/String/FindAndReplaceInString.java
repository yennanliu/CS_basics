package LeetCodeJava.String;

// https://leetcode.com/problems/find-and-replace-in-string/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 833. Find And Replace in String
 * Medium
 * Topics
 * Companies
 * You are given a 0-indexed string s that you must perform k replacement operations on. The replacement operations are given as three 0-indexed parallel arrays, indices, sources, and targets, all of length k.
 * <p>
 * To complete the ith replacement operation:
 * <p>
 * Check if the substring sources[i] occurs at index indices[i] in the original string s.
 * If it does not occur, do nothing.
 * Otherwise if it does occur, replace that substring with targets[i].
 * For example, if s = "abcd", indices[i] = 0, sources[i] = "ab", and targets[i] = "eee", then the result of this replacement will be "eeecd".
 * <p>
 * All replacement operations must occur simultaneously, meaning the replacement operations should not affect the indexing of each other. The testcases will be generated such that the replacements will not overlap.
 * <p>
 * For example, a testcase with s = "abc", indices = [0, 1], and sources = ["ab","bc"] will not be generated because the "ab" and "bc" replacements overlap.
 * Return the resulting string after performing all replacement operations on s.
 * <p>
 * A substring is a contiguous sequence of characters in a string.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * Input: s = "abcd", indices = [0, 2], sources = ["a", "cd"], targets = ["eee", "ffff"]
 * Output: "eeebffff"
 * Explanation:
 * "a" occurs at index 0 in s, so we replace it with "eee".
 * "cd" occurs at index 2 in s, so we replace it with "ffff".
 * Example 2:
 * <p>
 * <p>
 * Input: s = "abcd", indices = [0, 2], sources = ["ab","ec"], targets = ["eee","ffff"]
 * Output: "eeecd"
 * Explanation:
 * "ab" occurs at index 0 in s, so we replace it with "eee".
 * "ec" does not occur at index 2 in s, so we do nothing.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= s.length <= 1000
 * k == indices.length == sources.length == targets.length
 * 1 <= k <= 100
 * 0 <= indexes[i] < s.length
 * 1 <= sources[i].length, targets[i].length <= 50
 * s consists of only lowercase English letters.
 * sources[i] and targets[i] consist of only lowercase English letters.
 */
/**
 *  NOTE !!!
 *
 *  -> The testcases will be generated such that the replacements will not overlap.
 *  (All replacement operations must occur simultaneously,
 *   meaning the replacement operations should not affect the
 *   indexing of each other. The testcases will be generated
 *   such that the replacements will not overlap.)
 *
 *   -> e.g. case like below will NOT happen:
 *
 *   s = "abc", indices = [0, 1], and sources = ["ab","bc"]
 *
 *   -> so, it's NO NEEDED that our code to handle scenario as above
 *
 */
public class FindAndReplaceInString {

    // V0
    // TODO : implement
//    public String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
//
//    }

    // V0-1
    // IDEA : MAP + startsWith (fixed by GPT)
    public String findReplaceString_0_1(String s, int[] indices, String[] sources, String[] targets) {
        // Map to store valid replacement indices and their respective replacement info
        Map<Integer, Integer> map = new HashMap<>();

        // Collect all validated replacements
        for (int i = 0; i < indices.length; i++) {
            if (s.startsWith(sources[i], indices[i])) {
                map.put(indices[i], i);
            }
        }

        // Construct the updated string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            if (map.containsKey(i)) {
                int replacementIndex = map.get(i);
                sb.append(targets[replacementIndex]); // Add the replacement
                /**
                 *  NOTE !!!
                 *
                 *   update index with replacement string length
                 *
                 *   -> NOTE !!! we use "source" length (with replacementIndex)
                 *               (but NOT target length)
                 */
                i += sources[replacementIndex].length(); // Skip the replaced substring
            } else {
                sb.append(s.charAt(i)); // Add the original character
                /**
                 *  NOTE !!!
                 *
                 *   move index 1 to right (i = i +1)
                 */
                i++;
            }
        }

        return sb.toString();
    }

    // V1
    // IDEA : HASHMAP
    // https://leetcode.com/problems/find-and-replace-in-string/submissions/1454170265/
    public String findReplaceString_1(String s, int[] indices, String[] sources, String[] targets) {
        /**
         * 	•	Purpose:
         * 	    •	Create a map where the key is the index in the string s where a valid replacement can occur, and the value is the index in the sources and targets arrays.
         *
         * 	•	Logic:
         * 	    •	Loop through each replacement instruction (indices, sources, targets).
         * 	    •	Check if sources[i] exists as a substring of s starting at indices[i]:
         * 	    •	Use s.startsWith(sources[i], indices[i]) to verify this condition.
         * 	    •	If the condition is true, store the index (indices[i]) in the map with its corresponding sources/targets index (i).
         *
         * 	•	Result:
         * 	    •	The map will only contain valid replacement indices, ensuring that only valid replacements are performed.
         *
         */
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < indices.length; i++) {
            if (s.startsWith(sources[i], indices[i])) {
                map.put(indices[i], i);
            }
        }

        /**
         * 	•	Purpose:
         * 	    •	Construct the final string using a StringBuilder, processing each character of s sequentially.
         *
         * 	•	Logic:
         * 	    •	Case 1: If the current index i is not in the map, it means no replacement starts at this index.
         * 	        •	Append the character at s[i] to the StringBuilder and move to the next character (i++).
         * 	    •	Case 2: If the current index i is in the map, it means a valid replacement starts here.
         * 	        •	Retrieve the corresponding index from map.get(i) and:
         * 	                •	Append the replacement string (targets[map.get(i)]) to the StringBuilder.
         * 	                •	Skip over the characters in the source substring (i += sources[map.get(i)].length()).
         *
         *
         * 	•	Why Use StringBuilder:
         * 	    •	Strings in Java are immutable, so using StringBuilder allows efficient in-place concatenation.
         *
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            if (!map.containsKey(i)) {
                sb.append(s.charAt(i));
                i++;
            } else { //replace chars
                sb.append(targets[map.get(i)]);
                i += sources[map.get(i)].length();
            }
        }
        return sb.toString();
    }

    // V2
    // IDEA : HASHMAP (gpt)
    /**
     * 	1.	Sorting the Indices:
     * 	    •	The replacements must not interfere with each other. For example, if you replace earlier substrings, it can shift the later indices.
     * 	    •	To avoid this issue, the indices are processed in descending order.
     *
     * 	2.	String Replacement:
     * 	    •	For each replacement index i, check if the substring starting at indices[i] in s matches sources[i] using s.startsWith(source, start).
     * 	    •	If it matches, replace the substring from start to start + source.length() with targets[i].
     *
     * 	3.	StringBuilder for Efficient Modifications:
     * 	    •	Use StringBuilder to perform in-place modifications on the string, which is more efficient than creating new strings repeatedly.
     *
     */
    public String findReplaceString_2(String s, int[] indices, String[] sources, String[] targets) {
        // Create an array of indices and sort them in descending order
        /**
         *
         *  - To avoid affecting the indices of unprocessed replacements, we process the replacements in reverse order of their indices.
         * 	- Sorting the indices ensures that replacements starting from the largest index occur first.
         *
         *
         * 	Why Reverse Order:
         * 	    •	For example, consider s = "abcd", indices = [0, 2], sources = ["a", "cd"], and targets = ["eee", "ffff"].
         *          If we replace "a" (at index 0) first, it would shift the position of "cd", causing the second replacement to fail.
         *          Replacing "cd" first avoids this issue.
         */
        Integer[] sortedIndices = new Integer[indices.length];
        for (int i = 0; i < indices.length; i++) {
            sortedIndices[i] = i;
        }
        Arrays.sort(sortedIndices, (a, b) -> Integer.compare(indices[b], indices[a]));

        // Perform replacements
        /**
         * 	•	Using StringBuilder:
         * 	    •	Strings in Java are immutable, meaning every modification creates a new string.
         * 	    •	Using StringBuilder allows us to modify the string efficiently in-place.
         *
         * 	•	Checking Substring Match:
         * 	    •	The method s.startsWith(source, start) checks whether the substring at index start matches the source string.
         * 	    •	If it matches, we proceed to replace it with the target.
         *
         * 	•	Replacing Substrings:
         * 	    •	The method sb.replace(start, start + source.length(), target) replaces the substring from start to start + source.length() with the target.
         */
        StringBuilder sb = new StringBuilder(s);
        for (int idx : sortedIndices) {
            int start = indices[idx];
            String source = sources[idx];
            String target = targets[idx];

            // Check if `source` exists at `start`
            if (s.startsWith(source, start)) {
                sb.replace(start, start + source.length(), target);
            }
        }

        return sb.toString();
    }


    // V3
    // TODO : replacer `Pair` in code
    // https://leetcode.com/problems/find-and-replace-in-string/submissions/1454169549/
//    public String findReplaceString_3(String s, int[] indices, String[] sources, String[] targets) {
//        Map<Integer , Pair> replacements = new TreeMap<>();
//        StringBuilder res = new StringBuilder();
//
//        for(int i = 0 ; i < indices.length ; i++)
//            if(s.substring(indices[i]).startsWith(sources[i]))
//                replacements.put(indices[i] , new Pair(targets[i] , sources[i].length()-1));
//
//        for(int i = 0 ; i < s.length() ; i++){
//            if(replacements.containsKey(i)){
//                Pair p = replacements.get(i);
//                res.append(p.getKey());
//                i += (int)p.getValue();
//            }
//            else
//                res.append(s.charAt(i));
//        }
//
//        return res.toString();
//    }

    // V3
    // https://blog.csdn.net/qq_37821701/article/details/125737152

}
