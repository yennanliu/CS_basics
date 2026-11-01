package LeetCodeJava.Graph;

// https://leetcode.com/problems/lexicographically-smallest-equivalent-string/description/

/**
 * 1061. Lexicographically Smallest Equivalent String
 * Medium
 *
 * You are given two strings of the same length s1 and s2 and a string baseStr.
 *
 * We say s1[i] and s2[i] are equivalent characters.
 *
 * For example, if s1 = "abc" and s2 = "cde", then we have 'a' == 'c', 'b' == 'd', and 'c' == 'e'.
 *
 * Equivalent characters follow the usual rules of any equivalence relation:
 *
 * Reflexivity: 'a' == 'a'.
 * Symmetry: 'a' == 'b' implies 'b' == 'a'.
 * Transitivity: 'a' == 'b' and 'b' == 'c' implies 'a' == 'c'.
 *
 * For example, given the equivalency information from s1 = "abc" and s2 = "cde",
 * "acd" and "aab" are equivalent strings of baseStr = "eed", and "aab" is the
 * lexicographically smallest equivalent string of baseStr.
 *
 * Return the lexicographically smallest equivalent string of baseStr by using
 * the equivalency information from s1 and s2.
 *
 *
 * Example 1:
 *
 * Input: s1 = "parker", s2 = "morris", baseStr = "parser"
 * Output: "makkek"
 * Explanation: Based on the equivalency information in s1 and s2, we can group their characters as [m,p], [a,o], [k,r,s], [e,i].
 * The characters in each group are equivalent and sorted in lexicographical order.
 * So the answer is "makkek".
 *
 * Example 2:
 *
 * Input: s1 = "hello", s2 = "world", baseStr = "hold"
 * Output: "hdld"
 * Explanation: Based on the equivalency information in s1 and s2, we can group their characters as [h,w], [d,e,o], [l,r].
 * So only the second letter 'o' in baseStr is changed to 'd', the answer is "hdld".
 *
 * Example 3:
 *
 * Input: s1 = "leetcode", s2 = "programs", baseStr = "sourcecode"
 * Output: "aauaaaaada"
 * Explanation: We group the equivalent characters in s1 and s2 as [a,o,e,r,s,c], [l,p], [g,t] and [d,m], thus all letters in baseStr except 'u' and 'd' are transformed to 'a', the answer is "aauaaaaada".
 *
 *
 * Constraints:
 *
 * 1 <= s1.length, s2.length, baseStr <= 1000
 * s1.length == s2.length
 * s1, s2, and baseStr consist of lowercase English letters.
 *
 */
public class LexicographicallySmallestEquivalentString {

    // V0
    // IDEA: UNION FIND OVER 26 LETTERS, THE ROOT OF A GROUP IS ALWAYS ITS SMALLEST LETTER
    //       when merging, point the bigger root at the smaller root, so find(c) returns
    //       the lexicographically smallest character equivalent to c.
    /**
     * time = O((N + M) * α(26)), N = s1.length(), M = baseStr.length()
     * space = O(26)
     */
    public String smallestEquivalentString(String s1, String s2, String baseStr) {
        int[] parent = new int[26];
        for (int i = 0; i < 26; i++) {
            parent[i] = i;
        }

        // s1[i] and s2[i] are equivalent -> merge their groups
        for (int i = 0; i < s1.length(); i++) {
            int rootA = find(parent, s1.charAt(i) - 'a');
            int rootB = find(parent, s2.charAt(i) - 'a');
            if (rootA == rootB) {
                continue;
            }
            // NOTE !!! always keep the SMALLER letter as the root
            if (rootA < rootB) {
                parent[rootB] = rootA;
            } else {
                parent[rootA] = rootB;
            }
        }

        // replace every char with the smallest letter of its group
        StringBuilder sb = new StringBuilder();
        for (char c : baseStr.toCharArray()) {
            sb.append((char) ('a' + find(parent, c - 'a')));
        }
        return sb.toString();
    }

    /** Returns the root of x, halving the path on the way up. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];   // path compression
            x = parent[x];
        }
        return x;
    }
}
