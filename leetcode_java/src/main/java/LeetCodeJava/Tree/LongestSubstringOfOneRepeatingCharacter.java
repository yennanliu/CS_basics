package LeetCodeJava.Tree;

// https://leetcode.com/problems/longest-substring-of-one-repeating-character/description/

/**
 * 2213. Longest Substring of One Repeating Character
 * Hard
 *
 * You are given a 0-indexed string s. You are also given a 0-indexed string queryCharacters of length k and a 0-indexed array of integer indices queryIndices of length k, both of which are used to describe k queries.
 *
 * The ith query updates the character in s at index queryIndices[i] to the character queryCharacters[i].
 *
 * Return an array lengths of length k where lengths[i] is the length of the longest substring of s consisting of only one repeating character after the ith query is performed.
 *
 *
 * Example 1:
 *
 * Input: s = "babacc", queryCharacters = "bcb", queryIndices = [1,3,3]
 * Output: [3,3,4]
 * Explanation:
 * - 1st query updates s = "bbbacc". The longest substring consisting of one repeating character is "bbb" with length 3.
 * - 2nd query updates s = "bbbccc".
 *   The longest substring consisting of one repeating character can be "bbb" or "ccc" with length 3.
 * - 3rd query updates s = "bbbbcc".
 *   The longest substring consisting of one repeating character is "bbbb" with length 4.
 * Thus, we return [3,3,4].
 *
 * Example 2:
 *
 * Input: s = "abyzz", queryCharacters = "aa", queryIndices = [2,1]
 * Output: [2,3]
 * Explanation:
 * - 1st query updates s = "abazz". The longest substring consisting of one repeating character is "zz" with length 2.
 * - 2nd query updates s = "aaazz". The longest substring consisting of one repeating character is "aaa" with length 3.
 * Thus, we return [2,3].
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters.
 * k == queryCharacters.length == queryIndices.length
 * 1 <= k <= 10^5
 * queryCharacters consists of lowercase English letters.
 * 0 <= queryIndices[i] < s.length
 *
 */
public class LongestSubstringOfOneRepeatingCharacter {

    // the current string, and per node: run from its left edge, run into its right edge, best run inside
    private char[] chars;
    private int[] prefix;
    private int[] suffix;
    private int[] best;

    // V0
    // IDEA: SEGMENT TREE — EACH NODE KEEPS ITS PREFIX RUN, SUFFIX RUN AND BEST RUN
    //       two children join across the middle only when left's last char == right's first char:
    //         best = max(left.best, right.best, left.suffix + right.prefix)
    //       a prefix (suffix) extends into the other child when its own child is one single run.
    //       each query is a point update; the answer is the root's best.
    /**
     * time = O((N + K) * log N)
     * space = O(N)
     */
    public int[] longestRepeating(String s, String queryCharacters, int[] queryIndices) {
        int n = s.length();
        chars = s.toCharArray();
        prefix = new int[4 * n];
        suffix = new int[4 * n];
        best = new int[4 * n];
        build(0, 0, n - 1);

        int[] answer = new int[queryIndices.length];
        for (int q = 0; q < queryIndices.length; q++) {
            chars[queryIndices[q]] = queryCharacters.charAt(q);
            update(0, 0, n - 1, queryIndices[q]);
            answer[q] = best[0];
        }
        return answer;
    }

    /** Builds the node covering chars[lo..hi]. */
    private void build(int node, int lo, int hi) {
        if (lo == hi) {
            setLeaf(node);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        build(2 * node + 1, lo, mid);
        build(2 * node + 2, mid + 1, hi);
        pull(node, lo, mid, hi);
    }

    /** Re-evaluates the leaf for index and every node above it. */
    private void update(int node, int lo, int hi, int index) {
        if (lo == hi) {
            setLeaf(node);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        if (index <= mid) {
            update(2 * node + 1, lo, mid, index);
        } else {
            update(2 * node + 2, mid + 1, hi, index);
        }
        pull(node, lo, mid, hi);
    }

    /** A single character is a run of length 1. */
    private void setLeaf(int node) {
        prefix[node] = 1;
        suffix[node] = 1;
        best[node] = 1;
    }

    /** Merges the two children of node; the boundary is between chars[mid] and chars[mid + 1]. */
    private void pull(int node, int lo, int mid, int hi) {
        int left = 2 * node + 1;
        int right = 2 * node + 2;
        int leftSize = mid - lo + 1;
        int rightSize = hi - mid;
        boolean joins = chars[mid] == chars[mid + 1];

        prefix[node] = prefix[left];
        // the whole left half is one run and it continues into the right half
        if (joins && prefix[left] == leftSize) {
            prefix[node] += prefix[right];
        }

        suffix[node] = suffix[right];
        // the whole right half is one run and it continues back into the left half
        if (joins && suffix[right] == rightSize) {
            suffix[node] += suffix[left];
        }

        best[node] = Math.max(best[left], best[right]);
        if (joins) {
            // a run crossing the middle
            best[node] = Math.max(best[node], suffix[left] + prefix[right]);
        }
    }
}
