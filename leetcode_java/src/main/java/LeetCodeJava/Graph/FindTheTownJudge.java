package LeetCodeJava.Graph;

// https://leetcode.com/problems/find-the-town-judge/description/

import java.util.Arrays;

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
    // IDEA: ARRAY OP (fixed by gemini)
    /**
     * Logic:
     * - A judge gains 1 point for every person who trusts them.
     * - A judge loses 1 point for every person they trust.
     * - The judge must have exactly n - 1 points.
     */
    /**
     * time = O(E)
     * space = O(N)
     */
    public int findJudge(int n, int[][] trust) {
        // If there is only one person and no trust relationship, they are the judge
        if (n == 1 && trust.length == 0)
            return 1;

        // Array to store net trust score for each person (1 to n)
        int[] netTrust = new int[n + 1];

        for (int[] relation : trust) {
            int a = relation[0]; // Truster
            int b = relation[1]; // Trustee

            // a trusts b, so a cannot be judge
            netTrust[a]--;
            // b is trusted by a, b is a candidate
            netTrust[b]++;
        }

        for (int i = 1; i <= n; i++) {
            // The judge is trusted by everyone (n-1) and trusts no one (0)
            if (netTrust[i] == n - 1) {
                return i;
            }
        }

        return -1;
    }

    // V0-0-0-1
    // IDEA: TRUST, TRUSTED ARRAY
    /**
     * time = O(E)
     * space = O(N)
     */
    public int findJudge_0_0_0_1(int n, int[][] trust) {
        // edge
        if (n == 1 && trust.length == 0) {
            return 1;
        }

        // ???
        // how many ppl the cur ppl (ppl with idx) trust
        int[] trustArr = new int[n + 1];
        // how many ppl the cur ppl (ppl with idx) is trusted
        int[] trustedArr = new int[n + 1];

        for (int[] t : trust) {
            /**
             *      *    - trust[i] = [ai, bi]
             *      *       -> ai trusts bi
             */
            int ai = t[0];
            int bi = t[1];

            // ??
            trustArr[ai] += 1;
            trustedArr[bi] += 1;
        }

        for (int i = 1; i < n + 1; i++) {
            if (trustArr[i] == 0 && trustedArr[i] == n - 1) {
                return i;
            }
        }

        return -1;
    }


    // V0-0-1
    // IDEA: 2 ARRAY (fixed by gpt)
    /**
     * time = O(E)
     * space = O(N)
     */
    /** NOTE: how do we handle `exactly ONLY 1 judge` in below code ?
     *
     *  e.g. `There is exactly one person that satisfies properties 1 and 2`
     *
     * ---
     *
     *
     * That is an excellent question that gets to the core of why the **Net Degree** approach is so efficient for this problem\!
     *
     * The short answer is: **Yes, the code implicitly checks and satisfies the uniqueness requirement without needing an explicit counter.**
     *
     * Here is the explanation:
     *
     * ### 1\. The Judge Condition is Unique
     *
     * The criteria for the Town Judge is that their **Net Degree must be exactly $N-1$**.
     *
     * Let's assume there are two different people, Person $X$ and Person $Y$, who both satisfy the Judge conditions (Net Degree $= N-1$).
     *
     *   * **Condition 1:** Judge $X$ must be trusted by everyone else. This includes Person $Y$.
     *       * $Y \to X$ (Y trusts X)
     *   * **Condition 2:** Judge $Y$ must be trusted by everyone else. This includes Person $X$.
     *       * $X \to Y$ (X trusts Y)
     *
     * If $X$ and $Y$ trust each other ($X \to Y$ and $Y \to X$):
     *
     *   * **$X$'s Out-Degree:** $X$ trusts $Y$, so $X$ has an Out-Degree of at least 1.
     *   * **$X$'s Net Degree:** $X$'s Net Degree would be $(N-1) - (\text{Out-Degree} \ge 1) \le N-2$.
     *   * **Conclusion:** If $X$ trusts $Y$, $X$'s net degree cannot be $N-1$. This violates the condition for being the Judge.
     *
     * Therefore, it is **impossible** for two distinct individuals to both satisfy the Judge conditions simultaneously.
     *
     * ### 2\. How the Code Handles Uniqueness
     *
     * The fixed code iterates through the `degree` array and returns the first person it finds whose `degree[i]` is equal to `N - 1`:
     *
     * ```java
     * // ...
     * for (int i = 1; i <= n; i++) {
     *     if (degree[i] == n - 1) {
     *         return i; // Returns the first (and only possible) match.
     *     }
     * }
     * return -1;
     * ```
     *
     * Since the logic proves that **at most one** person can possibly satisfy the Net Degree of $N-1$, the moment the `if (degree[i] == n - 1)` condition is met, we know we have found the unique Judge, and we can immediately return their ID. If the loop completes without finding anyone, we return $-1$.
     *
     */
    public int findJudge_0_0_1(int n, int[][] trust) {
        // edge
        // NOTE !!! edge case below
        if (n == 1) {
            return 1;
        }

        // people the person (idx) trusts
        // e.g.  idx --- trust --> person
        int[] toTrust = new int[n + 1];

        // people who trusts the person (idx)
        // e.g.  person --- trust --> idx
        int[] trusted = new int[n + 1];

        for (int[] t : trust) {
            // NOTE !!!
            // [ai, bi]:  `ai` trusts `bi`
            int ai = t[0];
            int bi = t[1];

            toTrust[ai] += 1;
            trusted[bi] += 1;
        }

        // NOTE !!! via below, we can print `array value` in java
        System.out.println(">>> toTrust = " + Arrays.toString(toTrust));
        System.out.println(">>> trusted = " + Arrays.toString(trusted));

        for (int j = 0; j < toTrust.length; j++) {
            if (toTrust[j] == 0 && trusted[j] == n - 1) {
                return j;
            }
        }

        return -1;
    }

    // V0-0-2
    // IDEA: HA2 ARRAY (fixed by gemini)
    /**
     * Finds the Town Judge by tracking the net degree of trust for each person.
     * * Time Complexity: O(E + N), where E is the number of trust relationships.
     * Space Complexity: O(N) for the degree array.
     */
    /**
     * time = O(E + N)
     * space = O(N)
     */
    public int findJudge_0_0_2(int n, int[][] trust) {

        // Edge case: If N=1 and no trust relationships, person 1 is trivially the Judge.
        if (n == 1 && trust.length == 0) {
            return 1;
        }

        // degree[i] tracks the net trust degree for person i (using 1-based indexing).
        // Size N+1 is used for people 1 to N.
        // A person is the Judge if degree[i] == N - 1.
        int[] degree = new int[n + 1];

        // --- Step 1: Calculate Net Trust Degree ---
        for (int[] t : trust) {
            int a = t[0]; // Person A (trusts -> Out-degree)
            int b = t[1]; // Person B (is trusted -> In-degree)

            // Condition 2 Check: A trusts someone, so A cannot be the Judge.
            degree[a]--;

            // Condition 1 Check: B is trusted by someone.
            degree[b]++;
        }

        // --- Step 2: Check for the Judge Condition ---
        // Iterate through all people from 1 to N.
        for (int i = 1; i <= n; i++) {
            // Check if the net degree equals N - 1.
            // (N-1) is the only score that satisfies both:
            //   In-degree (N-1) + Out-degree (0) = N-1.
            if (degree[i] == n - 1) {
                return i;
            }
        }

        // If no one meets the degree requirement, no Judge exists.
        return -1;
    }

    // V0-1
    // IDEA: BRUTE FORCE (fixed by gpt)
    /**
     * 3. **Simplified Logic:**
     *    - Used an `inDegree` and `outDegree` approach to efficiently determine the judge.
     *    - Avoided unnecessary `candidates` list manipulation, which was overly complicated.
     *
     */
    /**
     * time = O(E)
     * space = O(N)
     */
    public int findJudge_0_1(int n, int[][] trust) {
        // Edge case: If there's only one person, they are the judge
        if (n == 1) {
            return 1;
        }

        // Initialize arrays to track in-degree and out-degree
        /**
         *
         * 2. **In-Degree and Out-Degree Arrays:**
         *    - `inDegree[i]`: Tracks how many people trust person `i`.
         *    - `outDegree[i]`: Tracks how many people person `i` trusts.
         *
         */
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
            /**
             *
             * 3. **Judge Condition:**
             *    - A person `i` is the judge if:
             *      - `inDegree[i] == n - 1` (trusted by everyone except themselves).
             *      - `outDegree[i] == 0` (does not trust anyone).
             *
             */
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
    /**
     * time = O(E)
     * space = O(N)
     */
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
    /**
     * time = O(E)
     * space = O(N)
     */
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
