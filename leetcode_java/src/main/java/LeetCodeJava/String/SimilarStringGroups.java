package LeetCodeJava.String;

// https://leetcode.com/problems/similar-string-groups/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    // V1
    // IDEA: BFS COMPONENT COUNTING (no union-find)
    /**
     *  Build the similarity relation implicitly and walk the components with BFS,
     *  counting how many traversals it takes to cover every word.
     *
     *  Same O(n^2 m) as V0, but the components are produced by a traversal, which
     *  makes it easy to also RETURN the groups rather than just their count.
     *
     *  time  = O(n^2 * m)
     *  space = O(n)
     */
    public int numSimilarGroups_1(String[] strs) {
        int n = strs.length;
        boolean[] seen = new boolean[n];
        int groups = 0;

        for (int i = 0; i < n; i++) {
            if (seen[i]) {
                continue;
            }
            groups += 1;
            Deque<Integer> q = new ArrayDeque<>();
            q.offer(i);
            seen[i] = true;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v = 0; v < n; v++) {
                    if (!seen[v] && similarPair(strs[u], strs[v])) {
                        seen[v] = true;
                        q.offer(v);
                    }
                }
            }
        }
        return groups;
    }

    private boolean similarPair(String a, String b) {
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i) && ++diff > 2) {
                return false;
            }
        }
        return true;
    }

    // V2
    // IDEA: GENERATE THE NEIGHBOURS instead of testing every pair
    /**
     *  For each word, generate all m*(m-1)/2 strings reachable by ONE swap and look
     *  them up in a map from word -> index.
     *
     *  -> O(n * m^2) instead of O(n^2 * m). When the words are short and numerous
     *     (m = 20, n = 300 here) that is the better side of the trade; when the
     *     words are long it is worse. Picking between them is the whole point.
     *
     *  time  = O(n * m^2)
     *  space = O(n)
     */
    public int numSimilarGroups_2(String[] strs) {
        int n = strs.length;
        int m = strs[0].length();

        Map<String, List<Integer>> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.computeIfAbsent(strs[i], k -> new ArrayList<>()).add(i);
        }

        int[] par = new int[n];
        for (int i = 0; i < n; i++) {
            par[i] = i;
        }
        int groups = n;

        for (int i = 0; i < n; i++) {
            char[] c = strs[i].toCharArray();
            // identical words are similar with 0 differences
            for (int dup : index.getOrDefault(strs[i], Collections.emptyList())) {
                if (dup != i && join(par, dup, i)) {
                    groups -= 1;
                }
            }
            for (int x = 0; x < m; x++) {
                for (int y = x + 1; y < m; y++) {
                    if (c[x] == c[y]) {
                        continue;
                    }
                    char t = c[x];
                    c[x] = c[y];
                    c[y] = t;
                    for (int j : index.getOrDefault(new String(c), Collections.emptyList())) {
                        if (join(par, i, j)) {
                            groups -= 1;
                        }
                    }
                    t = c[x];
                    c[x] = c[y];
                    c[y] = t;
                }
            }
        }
        return groups;
    }

    private int root(int[] par, int x) {
        while (par[x] != x) {
            par[x] = par[par[x]];
            x = par[x];
        }
        return x;
    }

    private boolean join(int[] par, int a, int b) {
        int ra = root(par, a);
        int rb = root(par, b);
        if (ra == rb) {
            return false;
        }
        par[ra] = rb;
        return true;
    }

    // V3
    // IDEA: RECURSIVE DFS OVER AN EXPLICIT ADJACENCY LIST
    /**
     *  Materialise the similarity graph as adjacency lists first, then count the
     *  components with a recursive DFS.
     *
     *  Costs O(n^2) memory for the edges, but the GRAPH becomes an object you can
     *  inspect -- which is what the problem statement actually describes.
     *
     *  time  = O(n^2 * m)
     *  space = O(n^2)
     */
    public int numSimilarGroups_3(String[] strs) {
        int n = strs.length;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (similarPair(strs[i], strs[j])) {
                    adj.get(i).add(j);
                    adj.get(j).add(i);
                }
            }
        }

        boolean[] seen = new boolean[n];
        int groups = 0;
        for (int i = 0; i < n; i++) {
            if (!seen[i]) {
                groups += 1;
                dfsGroup(adj, seen, i);
            }
        }
        return groups;
    }

    private void dfsGroup(List<List<Integer>> adj, boolean[] seen, int u) {
        seen[u] = true;
        for (int v : adj.get(u)) {
            if (!seen[v]) {
                dfsGroup(adj, seen, v);
            }
        }
    }

}
