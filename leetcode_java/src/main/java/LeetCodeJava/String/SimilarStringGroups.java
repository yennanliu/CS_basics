package LeetCodeJava.String;

// https://leetcode.com/problems/similar-string-groups/description/
/**
 * 839. Similar String Groups
 * Hard
 *
 * Two strings, X and Y, are considered similar if either they are identical or we can
 * make them equivalent by swapping at most two letters (in distinct positions) within
 * the string X.
 *
 * For example, "tars" and "rats" are similar (swapping at positions 0 and 2), and
 * "rats" and "arts" are similar, but "star" is not similar to "tars", "rats", or "arts".
 *
 * Together, these form two connected groups by similarity: {"tars", "rats", "arts"}
 * and {"star"}. Notice that "tars" and "arts" are in the same group even though they
 * are not similar. Formally, each group is such that a word is in the group if and
 * only if it is similar to at least one other word in the group.
 *
 * We are given a list strs of strings where every string in strs is an anagram of
 * every other string in strs. How many groups are there?
 *
 *
 * Example 1:
 *
 * Input: strs = ["tars","rats","arts","star"]
 * Output: 2
 *
 * Example 2:
 *
 * Input: strs = ["omv","ovm"]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= strs.length <= 300
 * 1 <= strs[i].length <= 300
 * strs[i] consists of lowercase letters only.
 * All words in strs have the same length and are anagrams of each other.
 *
 */
public class SimilarStringGroups {

    // V0
    // IDEA: UNION FIND
    /**
     *   Since ALL words are ANAGRAMS of each other, two words are `similar`
     *   exactly when they differ in 0 or 2 positions.
     *
     *   NOTE !!! exactly 1 difference is IMPOSSIBLE for anagrams (if one position
     *            differs, some other position must differ too), and >= 3 needs more
     *            than one swap -> that is why the early exit at diff > 2 is safe.
     *
     *   Compare every pair, union the similar ones, and the answer is the number
     *   of connected COMPONENTS.
     *
     *   time  = O(n^2 * m)   // n = strs.length, m = strs[0].length
     *   space = O(n)
     */

    private int[] parent;

    public int numSimilarGroups(String[] strs) {
        int n = strs.length;

        this.parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int groups = n;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (similar(strs[i], strs[j])) {
                    int ri = find(i);
                    int rj = find(j);
                    if (ri != rj) {
                        parent[ri] = rj;
                        groups -= 1;
                    }
                }
            }
        }

        return groups;
    }

    private boolean similar(String a, String b) {
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                diff += 1;
                // early exit: 3+ mismatches can never be ONE swap
                if (diff > 2) {
                    return false;
                }
            }
        }
        return true;
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path halving
            x = parent[x];
        }
        return x;
    }

}
