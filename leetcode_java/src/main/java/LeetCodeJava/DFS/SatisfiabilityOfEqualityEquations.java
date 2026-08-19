package LeetCodeJava.DFS;

// https://leetcode.com/problems/satisfiability-of-equality-equations/

/**
 *  990. Satisfiability of Equality Equations
 *  Medium
 *
 *  You are given an array of strings equations that represent relationships between variables
 *  where each string equations[i] is of length 4 and takes one of two different forms:
 *  "xi==yi" or "xi!=yi". Here, xi and yi are lowercase letters (not necessarily different)
 *  that represent one-letter variable names.
 *
 *  Return true if it is possible to assign integers to variable names so as to satisfy all the
 *  given equations, or false otherwise.
 *
 *  Example 1:
 *  Input: equations = ["a==b","b!=a"]
 *  Output: false
 *
 *  Example 2:
 *  Input: equations = ["b==a","a==b"]
 *  Output: true
 *
 *  Constraints:
 *  1 <= equations.length <= 500
 *  equations[i].length == 4
 *  equations[i][0] is a lowercase letter.
 *  equations[i][1] is either '=' or '!'.
 *  equations[i][2] is '='.
 *  equations[i][3] is a lowercase letter.
 */
public class SatisfiabilityOfEqualityEquations {

    // V0
    // IDEA: UNION FIND - first union every "==" pair, then verify no "!=" pair shares a root
    /**
     * time = O(n * a(26))
     * space = O(26)
     */
    public boolean equationsPossible(String[] equations) {
        int[] parent = new int[26];
        for (int i = 0; i < 26; i++) {
            parent[i] = i;
        }

        // pass 1 : merge all equalities
        for (String eq : equations) {
            if (eq.charAt(1) == '=') {
                int a = eq.charAt(0) - 'a';
                int b = eq.charAt(3) - 'a';
                union(parent, a, b);
            }
        }

        // pass 2 : any inequality inside the same group breaks it
        for (String eq : equations) {
            if (eq.charAt(1) == '!') {
                int a = eq.charAt(0) - 'a';
                int b = eq.charAt(3) - 'a';
                if (find(parent, a) == find(parent, b)) {
                    return false;
                }
            }
        }
        return true;
    }

    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path compression
            x = parent[x];
        }
        return x;
    }

    private void union(int[] parent, int a, int b) {
        int ra = find(parent, a);
        int rb = find(parent, b);
        if (ra != rb) {
            parent[ra] = rb;
        }
    }
}
