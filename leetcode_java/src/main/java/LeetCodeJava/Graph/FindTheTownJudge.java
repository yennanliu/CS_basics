package LeetCodeJava.Graph;

// https://leetcode.com/problems/find-the-town-judge/description/
/**
 * 997. Find the Town Judge
 * Easy
 * Topics
 * Companies
 * In a town, there are n people labeled from 1 to n. There is a rumor that one of these people is secretly the town judge.
 *
 * If the town judge exists, then:
 *
 * The town judge trusts nobody.
 * Everybody (except for the town judge) trusts the town judge.
 * There is exactly one person that satisfies properties 1 and 2.
 * You are given an array trust where trust[i] = [ai, bi] representing that the person labeled ai trusts the person labeled bi. If a trust relationship does not exist in trust array, then such a trust relationship does not exist.
 *
 * Return the label of the town judge if the town judge exists and can be identified, or return -1 otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2, trust = [[1,2]]
 * Output: 2
 * Example 2:
 *
 * Input: n = 3, trust = [[1,3],[2,3]]
 * Output: 3
 * Example 3:
 *
 * Input: n = 3, trust = [[1,3],[2,3],[3,1]]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 * 0 <= trust.length <= 104
 * trust[i].length == 2
 * All the pairs of trust are unique.
 * ai != bi
 * 1 <= ai, bi <= n
 *
 *
 */
public class FindTheTownJudge {

    // V0
//    public int findJudge(int n, int[][] trust) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (fixed by gpt)
    public int findJudge_0_1(int n, int[][] trust) {
        // Edge case: If there's only one person, they are the judge
        if (n == 1) {
            return 1;
        }

        // Initialize arrays to track in-degree and out-degree
        int[] inDegree = new int[n + 1];
        int[] outDegree = new int[n + 1];

        // Process the trust relationships
        for (int[] relation : trust) {
            int truster = relation[0];
            int trustee = relation[1];
            outDegree[truster]++;
            inDegree[trustee]++;
        }

        // Find the person who satisfies the judge condition
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == n - 1 && outDegree[i] == 0) {
                return i;
            }
        }

        // No judge found
        return -1;
    }

    // V1
    // https://www.youtube.com/watch?v=QiGaxdUINJ8

    // V2
    // https://leetcode.com/problems/find-the-town-judge/solutions/3088104/day-23-easiest-beginner-friendly-solutio-7fbp/
    public int findJudge_2(int n, int[][] trust) {
        if (trust.length == 0 && n == 1)
            return 1;
        int[] count = new int[n + 1];
        for (int[] person : trust) {
            count[person[0]]--;
            count[person[1]]++;
        }

        for (int person = 0; person < count.length; person++) {
            if (count[person] == n - 1)
                return person;
        }
        return -1;
    }

    // V3
    // https://leetcode.com/problems/find-the-town-judge/solutions/4764873/beats-98-users-cjavapythonjavascript-exp-lk99/
    public int findJudge_3(int N, int[][] trust) {
        int[] in = new int[N + 1];
        int[] out = new int[N + 1];
        for (int[] a : trust) {
            out[a[0]]++;
            in[a[1]]++;
        }
        for (int i = 1; i <= N; ++i) {
            if (in[i] == N - 1 && out[i] == 0)
                return i;
        }
        return -1;
    }


}
