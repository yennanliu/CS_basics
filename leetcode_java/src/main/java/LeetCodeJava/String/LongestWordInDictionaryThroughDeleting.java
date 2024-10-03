package LeetCodeJava.String;

// https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/description/

import java.util.*;

/**
 * 524. Longest Word in Dictionary through Deleting
 *
 * Medium
 * Topics
 * Companies
 * Given a string s and a string array dictionary, return the longest string in the dictionary that can be formed by deleting some of the given string characters. If there is more than one possible result, return the longest word with the smallest lexicographical order. If there is no possible result, return the empty string.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
 * Output: "apple"
 * Example 2:
 *
 * Input: s = "abpcplea", dictionary = ["a","b","c"]
 * Output: "a"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * 1 <= dictionary.length <= 1000
 * 1 <= dictionary[i].length <= 1000
 * s and dictionary[i] consist of lowercase English letters.
 *
 */
public class LongestWordInDictionaryThroughDeleting {

    // V0
    // TODO: implement
    public String findLongestWord(String s, List<String> dictionary) {
        if (dictionary.size() == 0 && s != null){
            return "";
        }

        List<String> collected = new ArrayList<>();
        // check
        for (String item : dictionary){
            //Map<String, Integer> curMap = this.getElementCount(item);
            if (canForm(item, s)){
                collected.add(item);
            }
        }
        System.out.println(">>> collected = " + collected);

        if (collected.size()==0){
            return "";
        }

        Collections.sort(collected, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                /**
                 * // First compare by length
                 * // NOTE !! inverse order, e.g. longest str at first
                 */
                int lengthComparison = Integer.compare(o2.length(), o1.length());
                /**
                 *  // If lengths are equal, compare lexicographically
                 *  // NOTE !!! if lengths are the same, we compare  lexicographically
                 */
                if (lengthComparison == 0) {
                    return o1.compareTo(o2); // lexicographical order
                }
                return lengthComparison; // sort by length
            }
        });

        System.out.println(">>> (sorted) collected = " + collected);
        return collected.get(0);
    }

    /**
     *  NOTE !!!
     *
     *   x : str in dict
     *   y : s
     *
     *   ref : isSubsequence_3(str, s)
     */
    private boolean canForm(String x, String y){
        int j = 0;
        for (int i = 0; i < y.length() && j < x.length(); i++)
            if (x.charAt(j) == y.charAt(i))
                j++;
        return j == x.length();
    }


    // V1_1
    // IDEA : Brute Force
    // https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/editorial/
    public String findLongestWord_1(String s, List< String > d) {
        HashSet< String > set = new HashSet < > (d);
        List < String > l = new ArrayList< >();
        generate(s, "", 0, l);
        String max_str = "";
        for (String str: l) {
            if (set.contains(str))
                if (str.length() > max_str.length() || (str.length() == max_str.length() && str.compareTo(max_str) < 0))
                    max_str = str;
        }
        return max_str;
    }
    public void generate(String s, String str, int i, List < String > l) {
        if (i == s.length())
            l.add(str);
        else {
            generate(s, str + s.charAt(i), i + 1, l);
            generate(s, str, i + 1, l);
        }
    }

    // V1_2
    // IDEA : Iterative Brute Force
    // https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/editorial/
    public String findLongestWord_1_2(String s, List < String > d) {
        HashSet < String > set = new HashSet < > (d);
        List < String > l = new ArrayList < > ();
        for (int i = 0; i < (1 << s.length()); i++) {
            String t = "";
            for (int j = 0; j < s.length(); j++) {
                if (((i >> j) & 1) != 0)
                    t += s.charAt(j);
            }
            l.add(t);
        }
        String max_str = "";
        for (String str: l) {
            if (set.contains(str))
                if (str.length() > max_str.length() || (str.length() == max_str.length() && str.compareTo(max_str) < 0))
                    max_str = str;
        }
        return max_str;
    }

    // V1_3
    // IDEA : Sorting and Checking Subsequence
    // https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/editorial/
    public boolean isSubsequence_3(String x, String y) {
        int j = 0;
        for (int i = 0; i < y.length() && j < x.length(); i++)
            if (x.charAt(j) == y.charAt(i))
                j++;
        return j == x.length();
    }
    public String findLongestWord_1_3(String s, List < String > d) {
        Collections.sort(d, new Comparator< String >() {
            public int compare(String s1, String s2) {
                return s2.length() != s1.length() ? s2.length() - s1.length() : s1.compareTo(s2);
            }
        });
        for (String str: d) {
            if (isSubsequence_3(str, s))
                return str;
        }
        return "";
    }

    // V1_4
    // IDEA : Without Sorting
    // https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/editorial/
    public boolean isSubsequence_4(String x, String y) {
        int j = 0;
        for (int i = 0; i < y.length() && j < x.length(); i++)
            if (x.charAt(j) == y.charAt(i))
                j++;
        return j == x.length();
    }
    public String findLongestWord_1_4(String s, List < String > d) {
        String max_str = "";
        for (String str: d) {
            if (isSubsequence_4(str, s)) {
                if (str.length() > max_str.length() || (str.length() == max_str.length() && str.compareTo(max_str) < 0))
                    max_str = str;
            }
        }
        return max_str;
    }

    // V2
}
