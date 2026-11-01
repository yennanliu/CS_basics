package LeetCodeJava.Graph;

// https://leetcode.com/problems/find-all-people-with-secret/description/

import java.util.*;

/**
 * 2092. Find All People With Secret
 * Hard
 *
 * You are given an integer n indicating there are n people numbered from 0 to n - 1. You are also given a
 * 0-indexed 2D integer array meetings where meetings[i] = [xi, yi, timei] indicates that person xi and person
 * yi have a meeting at timei. A person may attend multiple meetings at the same time. Finally, you are given
 * an integer firstPerson.
 *
 * Person 0 has a secret and initially shares the secret with a person firstPerson at time 0. This secret can
 * then be shared by any person who knows it to any other person they meet at the same time. More formally,
 * for every meeting, if a person xi has the secret at timei, then they will share the secret with person yi,
 * and vice versa.
 *
 * The secrets are shared instantaneously. That is, a person may receive the secret and share it with people
 * in other meetings within the same time frame.
 *
 * Return a list of all the people that have the secret after all the meetings have taken place. You may
 * return the answer in any order.
 *
 *
 * Example 1:
 *
 * Input: n = 6, meetings = [[1,2,5],[2,3,8],[1,5,10]], firstPerson = 1
 * Output: [0,1,2,3,5]
 *
 * Example 2:
 *
 * Input: n = 4, meetings = [[3,1,3],[1,2,2],[0,3,3]], firstPerson = 3
 * Output: [0,1,3]
 *
 * Example 3:
 *
 * Input: n = 5, meetings = [[3,4,2],[1,2,1],[2,3,1]], firstPerson = 1
 * Output: [0,1,2,3,4]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= meetings.length <= 10^5
 * meetings[i].length == 3
 * 0 <= xi, yi <= n - 1
 * xi != yi
 * 1 <= timei <= 10^5
 * 1 <= firstPerson <= n - 1
 *
 */
public class FindAllPeopleWithSecret {

    // V0
    // IDEA: UNION FIND PROCESSED IN TIME GROUPS, WITH A ROLLBACK PER GROUP
    // meetings at one timestamp happen simultaneously, so union all of them before deciding
    // who knows the secret (that lets it hop 1 -> 2 -> 3 within one frame). Then every person
    // in the group whose component does not contain person 0 is reset to a singleton: a
    // meeting among people who do not know the secret must not leak into later groups.
    /**
     * time = O(M log M + M * alpha(N))
     * space = O(N + M)
     */
    public List<Integer> findAllPeople(int n, int[][] meetings, int firstPerson) {
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        // time 0: person 0 tells firstPerson
        union(parent, firstPerson, 0);

        int[][] sorted = meetings.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[2], b[2]));

        int start = 0;
        while (start < sorted.length) {
            // [start, end) is one group of meetings sharing the same time
            int end = start;
            while (end < sorted.length && sorted[end][2] == sorted[start][2]) {
                end++;
            }

            for (int i = start; i < end; i++) {
                union(parent, sorted[i][0], sorted[i][1]);
            }

            // roll back everyone in this group who did not end up with person 0
            int secretRoot = find(parent, 0);
            for (int i = start; i < end; i++) {
                for (int k = 0; k < 2; k++) {
                    int person = sorted[i][k];
                    if (find(parent, person) != secretRoot) {
                        parent[person] = person;
                    }
                }
            }
            start = end;
        }

        int secretRoot = find(parent, 0);
        List<Integer> result = new ArrayList<>();
        for (int person = 0; person < n; person++) {
            if (find(parent, person) == secretRoot) {
                result.add(person);
            }
        }
        return result;
    }

    /** Returns the root of x, compressing the path on the way. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /** Merges the sets of a and b. */
    private void union(int[] parent, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA != rootB) {
            parent[rootA] = rootB;
        }
    }
}
