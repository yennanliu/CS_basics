package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximum-performance-of-a-team/description/

import java.util.*;

/**
 * 1383. Maximum Performance of a Team
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two integers n and k and two integer arrays speed and efficiency both of length n. There are n engineers numbered from 1 to n. speed[i] and efficiency[i] represent the speed and efficiency of the ith engineer respectively.
 *
 * Choose at most k different engineers out of the n engineers to form a team with the maximum performance.
 *
 * The performance of a team is the sum of its engineers' speeds multiplied by the minimum efficiency among its engineers.
 *
 * Return the maximum performance of this team. Since the answer can be a huge number, return it modulo 109 + 7.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 2
 * Output: 60
 * Explanation:
 * We have the maximum performance of the team by selecting engineer 2 (with speed=10 and efficiency=4) and engineer 5 (with speed=5 and efficiency=7). That is, performance = (10 + 5) * min(4, 7) = 60.
 * Example 2:
 *
 * Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 3
 * Output: 68
 * Explanation:
 * This is the same example as the first but k = 3. We can select engineer 1, engineer 2 and engineer 5 to get the maximum performance of the team. That is, performance = (2 + 10 + 5) * min(5, 4, 7) = 68.
 * Example 3:
 *
 * Input: n = 6, speed = [2,10,3,1,5,8], efficiency = [5,4,3,9,7,2], k = 4
 * Output: 72
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 105
 * speed.length == n
 * efficiency.length == n
 * 1 <= speed[i] <= 105
 * 1 <= efficiency[i] <= 108
 *
 */
public class MaximumPerformanceOfAeam {

    // V0
//    public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
//
//    }

    // V0-1
    // IDEA: PQ (gemini)
    public int maxPerformance_0_1(int n, int[] speed, int[] efficiency, int k) {
        int[][] engineers = new int[n][2];
        for (int i = 0; i < n; i++) {
            engineers[i] = new int[] { efficiency[i], speed[i] };
        }

        // 1. Sort engineers by efficiency in descending order
        Arrays.sort(engineers, (a, b) -> b[0] - a[0]);

        // 2. Min-Heap to keep track of the highest speeds
        PriorityQueue<Integer> pq = new PriorityQueue<>(k);

        long totalSpeed = 0;
        long maxPerformance = 0;

        for (int[] engineer : engineers) {
            int currEff = engineer[0];
            int currSpeed = engineer[1];

            // Add current engineer to the team
            totalSpeed += currSpeed;
            pq.add(currSpeed);

            // If team size exceeds k, remove the engineer with the lowest speed
            if (pq.size() > k) {
                totalSpeed -= pq.poll();
            }

            // Calculate performance with current engineer as the efficiency bottleneck
            maxPerformance = Math.max(maxPerformance, totalSpeed * currEff);
        }

        // The problem asks for the result modulo 10^9 + 7
        return (int) (maxPerformance % 1000000007);
    }

    // V0-2
    // IDEA: PQ (gpt)
    public int maxPerformance_0_2(int n, int[] speed, int[] efficiency, int k) {
        final int MOD = 1_000_000_007;

        // Pair up engineers [efficiency, speed]
        int[][] engineers = new int[n][2];
        for (int i = 0; i < n; i++) {
            engineers[i] = new int[] { efficiency[i], speed[i] };
        }

        // Sort by efficiency in descending order
        Arrays.sort(engineers, (a, b) -> b[0] - a[0]);

        // Min-heap to keep 'k' largest speeds
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        long speedSum = 0;
        long maxPerf = 0;

        for (int[] eng : engineers) {
            int eff = eng[0];
            int spd = eng[1];

            // Add this speed
            minHeap.offer(spd);
            speedSum += spd;

            // If more than k, remove smallest speed
            if (minHeap.size() > k) {
                speedSum -= minHeap.poll();
            }

            // Performance = speed sum * current min efficiency
            long performance = speedSum * eff;
            maxPerf = Math.max(maxPerf, performance);
        }

        return (int) (maxPerf % MOD);
    }

    // V0-3
    // IDEA: PQ
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/64296433
    private static final int MOD = 1_000_000_007;
    public int maxPerformance_0_3(int n, int[] speed, int[] efficiency, int k) {
        int[][] p = new int[n][2];
        for (int i = 0; i < n; i++) {
            p[i][0] = efficiency[i];
            p[i][1] = speed[i];
        }
        Arrays.sort(p, (a, b) -> b[0] - a[0]);

        PriorityQueue<Integer> pq = new PriorityQueue<>();

        long sum = 0;
        long ans = 0;

        for (int i = 0; i < n; i++) {
            sum += p[i][1];
            pq.offer(p[i][1]);

            if (pq.size() > k) {
                sum -= pq.poll();
            }

            ans = Math.max(ans, sum * p[i][0]);
        }

        return (int) (ans % MOD);
    }

    // V1
    // IDEA: PQ
    // https://leetcode.com/problems/maximum-performance-of-a-team/solutions/2559857/java-easy-solution-with-explanation-99-f-3wul/
    public int maxPerformance_1(int n, int[] speed, int[] efficiency, int k) {
        int[][] players = new int[n][2];
        for (int i = 0; i < n; i++) {
            players[i][0] = efficiency[i];
            players[i][1] = speed[i];
        }
        // Sort the players based on efficiency in decreasing order, as for each iteration, we'll consider only players with higher efficiency.
        Arrays.sort(players, (p1, p2) -> (p2[0] - p1[0]));

        // Priority-Queue to maintain players with highest relative speeds with efficiencies greater than the one under iteration.
        PriorityQueue<Integer> speedQueue = new PriorityQueue<>(k);
        long teamPerformance = 0, teamSpeed = 0;

        for (int i = 0; i < n; i++) {
            // This is because a team can have atmost `k` players.
            if (speedQueue.size() >= k) {
                teamSpeed -= speedQueue.remove();
            }
            speedQueue.add(players[i][1]);
            teamSpeed += players[i][1];

            teamPerformance = Math.max(teamPerformance, teamSpeed * players[i][0]);
        }
        return (int) (teamPerformance % 1000000007);
    }


    // V2
    // IDEA: PQ
    // https://leetcode.com/problems/maximum-performance-of-a-team/solutions/1252316/js-python-java-c-easy-priority-queue-sol-aa78/
    public int maxPerformance_2(int n, int[] speed, int[] efficiency, int k) {
        int[][] ord = new int[n][2];
        for (int i = 0; i < n; i++)
            ord[i] = new int[] { efficiency[i], speed[i] };
        Arrays.sort(ord, (a, b) -> Integer.compare(b[0], a[0]));
        PriorityQueue<Integer> sppq = new PriorityQueue<>();
        long totalSpeed = 0, best = 0;
        for (int[] pair : ord) {
            int spd = pair[1];
            sppq.add(spd);
            if (sppq.size() <= k)
                totalSpeed += spd;
            else
                totalSpeed += spd - sppq.poll();
            best = Math.max(best, totalSpeed * pair[0]);
        }
        return (int) (best % 1000000007);
    }


    // V3
    // https://leetcode.com/problems/maximum-performance-of-a-team/solutions/2562547/java-commented-by-sourin_bruh-718l/
    // Combining the data of both the given arrays so as to make it easier to do comparisons
    private class Engineer {
        private int speed;
        private int efficiency;

        public Engineer(int speed, int efficiency) {
            this.speed = speed;
            this.efficiency = efficiency;
        }
    }

    public int maxPerformance_3(int n, int[] speed, int[] efficiency, int k) {
        List<Engineer> engg = new ArrayList<>(); // Will store all the engineers
        for (int i = 0; i < n; i++) {
            engg.add(new Engineer(speed[i], efficiency[i]));
        }

        // sorting the engg list in descending order of their efficiency
        Collections.sort(engg, (a, b) -> b.efficiency - a.efficiency);
        // This priority queue (min heap) will contain engineers chosen to form a team to get the performance
        // min heap will keep slowest engineer at the top, faster ones stay longer in the team
        Queue<Engineer> currTeam = new PriorityQueue<>((a, b) -> a.speed - b.speed);
        long teamSpeed = 0, maxPerformance = 0;
        for (Engineer newHire : engg) {
            if (currTeam.size() == k) {
                Engineer slowGuy = currTeam.poll(); // remove the engineer with slowest speed
                teamSpeed -= slowGuy.speed; // deducting the removed engineer's speed from the total speed
            }

            currTeam.add(newHire); // adding the new engineer to the team (engg list)
            teamSpeed += newHire.speed; // adding the new engineer's speed to the total speed
            long newPerformance = teamSpeed * (long) newHire.efficiency; // getting the current performance
            maxPerformance = Math.max(maxPerformance, newPerformance); // updating the max performance if greater
        }

        return (int) (maxPerformance % 1000000007); // returning the way asked to in the problem statement
    }



}
