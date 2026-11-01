package LeetCodeJava.Graph;

// https://leetcode.com/problems/frequencies-of-shortest-supersequences/description/

import java.util.*;

/**
 * 3435. Frequencies of Shortest Supersequences
 * Hard
 *
 * You are given an array of strings words. Find all shortest common supersequences
 * (SCS) of words that are not permutations of each other.
 *
 * A shortest common supersequence is a string of minimum length that contains each
 * string in words as a subsequence.
 *
 * Return a 2D array of integers freqs that represent all the SCSs. Each freqs[i]
 * is an array of size 26, representing the frequency of each letter in the
 * lowercase English alphabet for a single SCS. You may return the frequency arrays
 * in any order.
 *
 * Example 1:
 *
 * Input: words = ["ab","ba"]
 *
 * Output: [[1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]
 *
 * Explanation:
 *
 * The two SCSs are "aba" and "bab". The output is the letter frequencies for each
 * one.
 *
 * Example 2:
 *
 * Input: words = ["aa","ac"]
 *
 * Output: [[2,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]
 *
 * Explanation:
 *
 * The two SCSs are "aac" and "aca". Since they are permutations of each other,
 * keep only "aac".
 *
 * Example 3:
 *
 * Input: words = ["aa","bb","cc"]
 *
 * Output: [[2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]
 *
 * Explanation:
 *
 * "aabbcc" and all its permutations are SCSs.
 *
 * Constraints:
 *
 * 1 <= words.length <= 256
 * words[i].length == 2
 * All strings in words will altogether be composed of no more than 16 unique
 * lowercase letters.
 * All strings in words are unique.
 *
 */
public class FrequenciesOfShortestSupersequences {

    // V0
    // IDEA: MINIMUM FEEDBACK VERTEX SET VIA SUBSET DP (letters <= 16)
    //  - each word "ab" is a directed edge a -> b ("aa" is a self-loop)
    //  - giving the letter set S two copies (front + back) and every other letter one copy
    //    works iff the graph on the single-copy letters is acyclic (topological order between
    //    the copies); so we want every minimum-size S whose complement is acyclic
    //  - acyclic[m] = some vertex of m has no incoming edge from inside m, and m without it
    //    is acyclic (a self-loop vertex is never such a source, so it is forced into S)
    /**
     * time = O(2^V * V)
     * space = O(2^V)
     */
    public List<List<Integer>> supersequences(String[] words) {
        // collect the distinct letters, in alphabetical order
        boolean[] present = new boolean[26];
        for (String word : words) {
            present[word.charAt(0) - 'a'] = true;
            present[word.charAt(1) - 'a'] = true;
        }
        int[] letterAt = new int[26];      // vertex index -> letter
        int[] indexOf = new int[26];       // letter -> vertex index
        int vertexCount = 0;
        for (int letter = 0; letter < 26; letter++) {
            if (present[letter]) {
                indexOf[letter] = vertexCount;
                letterAt[vertexCount] = letter;
                vertexCount++;
            }
        }
        int full = (1 << vertexCount) - 1;

        // incoming[v] = bitmask of vertices u with an edge u -> v
        int[] incoming = new int[vertexCount];
        for (String word : words) {
            int from = indexOf[word.charAt(0) - 'a'];
            int to = indexOf[word.charAt(1) - 'a'];
            incoming[to] |= (1 << from);
        }

        // acyclic[mask] = the subgraph induced on mask has no cycle
        boolean[] acyclic = new boolean[full + 1];
        acyclic[0] = true;
        for (int mask = 1; mask <= full; mask++) {
            for (int v = 0; v < vertexCount; v++) {
                int bit = 1 << v;
                if ((mask & bit) == 0) {
                    continue;
                }
                // v is a source inside mask, and the rest is acyclic
                if ((incoming[v] & mask) == 0 && acyclic[mask ^ bit]) {
                    acyclic[mask] = true;
                    break;
                }
            }
        }

        // every minimum-size doubled set whose complement is acyclic
        int bestSize = vertexCount + 1;
        List<Integer> chosen = new ArrayList<>();
        for (int doubled = 0; doubled <= full; doubled++) {
            int size = Integer.bitCount(doubled);
            if (size > bestSize || !acyclic[full ^ doubled]) {
                continue;
            }
            if (size < bestSize) {
                bestSize = size;
                chosen.clear();
            }
            chosen.add(doubled);
        }

        List<List<Integer>> answer = new ArrayList<>();
        for (int doubled : chosen) {
            Integer[] frequency = new Integer[26];
            Arrays.fill(frequency, 0);
            for (int v = 0; v < vertexCount; v++) {
                boolean isDoubled = (doubled & (1 << v)) != 0;
                frequency[letterAt[v]] = isDoubled ? 2 : 1;
            }
            answer.add(Arrays.asList(frequency));
        }
        return answer;
    }
}
