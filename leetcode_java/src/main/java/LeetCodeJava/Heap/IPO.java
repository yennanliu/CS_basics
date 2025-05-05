package LeetCodeJava.Heap;

// https://leetcode.com/problems/ipo/description/

import java.util.*;

/**
 * 502. IPO
 * Hard
 * Topics
 * Companies
 * Suppose LeetCode will start its IPO soon. In order to sell a good price of its shares to Venture Capital, LeetCode would like to work on some projects to increase its capital before the IPO. Since it has limited resources, it can only finish at most k distinct projects before the IPO. Help LeetCode design the best way to maximize its total capital after finishing at most k distinct projects.
 *
 * You are given n projects where the ith project has a pure profit profits[i] and a minimum capital of capital[i] is needed to start it.
 *
 * Initially, you have w capital. When you finish a project, you will obtain its pure profit and the profit will be added to your total capital.
 *
 * Pick a list of at most k distinct projects from given projects to maximize your final capital, and return the final maximized capital.
 *
 * The answer is guaranteed to fit in a 32-bit signed integer.
 *
 *
 *
 * Example 1:
 *
 * Input: k = 2, w = 0, profits = [1,2,3], capital = [0,1,1]
 * Output: 4
 * Explanation: Since your initial capital is 0, you can only start the project indexed 0.
 * After finishing it you will obtain profit 1 and your capital becomes 1.
 * With capital 1, you can either start the project indexed 1 or the project indexed 2.
 * Since you can choose at most 2 projects, you need to finish the project indexed 2 to get the maximum capital.
 * Therefore, output the final maximized capital, which is 0 + 1 + 3 = 4.
 * Example 2:
 *
 * Input: k = 3, w = 0, profits = [1,2,3], capital = [0,1,2]
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= k <= 105
 * 0 <= w <= 109
 * n == profits.length
 * n == capital.length
 * 1 <= n <= 105
 * 0 <= profits[i] <= 104
 * 0 <= capital[i] <= 109
 *
 */
public class IPO {

    // V0
//    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
//
//    }

    // V0-1
    // IDEA: 2 PQ + GREEDY (fixed by gpt)
    /**
     *  NOTE !!!  we use 2 PQ for this problem
     *
     *  1. capital PQ : Min-heap ordered by capital
     *
     *     - structure : { [capital, profit] }  // <---- NOTE this !!!!
     *
     *  2. profit PQ: ax-heap ordered by profit
     *
     *     - structure : { profit }
     *
     *
     */
    public int findMaximizedCapital_0_1(int k, int w, int[] profits, int[] capital) {
        if (k <= 0 || profits == null || capital == null ||
                profits.length != capital.length || profits.length == 0) {
            return w;
        }

        // Min-heap ordered by capital
        //  - structure : { [capital, profit] }  // <---- NOTE this !!!!
        PriorityQueue<int[]> capitalPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // Max-heap ordered by profit
        // - structure : { profit }
        PriorityQueue<Integer> profitPQ = new PriorityQueue<>((a, b) -> b - a);

        // NOTE !!! below !!! we fill all [capital, profit] to capitalPQ first
        // Fill capitalPQ with [capital, profit] pairs
        for (int i = 0; i < profits.length; i++) {
            capitalPQ.offer(new int[] { capital[i], profits[i] });
        }

        // Perform up to k selections
        for (int i = 0; i < k; i++) {

            // NOTE !!! we use `while` loop to move all `affordable` profit to queue
            // Move all projects that are affordable into profitPQ
            /**
             *  NOTE !!!
             *
             *   we use `while` but not `if` for below
             *
             *   Reason :
             *
             *
             * > **No, you should not replace the `while` with an `if`** in this case,
             *    unless you're okay with **missing valid profitable projects**.
             *
             * ---
             *
             * ### 🔍 Why `while` is necessary
             *
             * The `capitalPQ` contains **multiple projects**, sorted by the required capital.
             * You're trying to **move all projects you can currently afford (capital ≤ `w`)**
             * into the `profitPQ`.
             *
             * If you write:
             *
             * ```java
             * if (!capitalPQ.isEmpty() && capitalPQ.peek()[0] <= w) {
             *     profitPQ.offer(capitalPQ.poll()[1]);
             * }
             * ```
             *
             * ...then **only one** eligible project (the one at the top of the min-heap)
             * is considered per iteration, even if **many** are affordable.
             *
             * ---
             *
             * ### ✅ Why `while` is correct
             *
             * The correct approach is:
             *
             * ```java
             * while (!capitalPQ.isEmpty() && capitalPQ.peek()[0] <= w) {
             *     profitPQ.offer(capitalPQ.poll()[1]);
             * }
             * ```
             *
             * This ensures **all** currently affordable projects
             * are available for you to choose the **most profitable one** from (`profitPQ.poll()`).
             *
             * ---
             *
             * ### 🔧 Example to illustrate
             *
             * Imagine:
             *
             * * `capital = [0, 1, 2]`
             * * `profits = [1, 2, 3]`
             * * `w = 2`
             * * `k = 1`
             *
             * Using `if`:
             *
             * * You only add the **first** eligible project (`capital = 0`) → profit = 1.
             * * Miss out on better ones (`capital = 1 or 2`, profit = 2 or 3). !!!!! (NOTE THIS)
             *
             * Using `while`:
             *
             * * You add **all** (`capital ≤ 2`) → all profits (1, 2, 3) go into `profitPQ`.
             * * You then pick the best (profit = 3).
             *
             * ---
             *
             * ### ✅ Summary
             *
             * Use `while` to **load all currently affordable options**, not just one.
             * `if` would result in **suboptimal or incorrect results**.
             *
             *
             */
            while (!capitalPQ.isEmpty() && capitalPQ.peek()[0] <= w) {
                profitPQ.offer(capitalPQ.poll()[1]);
            }

            // If no affordable projects, stop
            if (profitPQ.isEmpty())
                break;

            // Choose the most profitable affordable project
            w += profitPQ.poll();
        }

        return w;
    }

    // V1
    // IDEA: GREEDY + PQ
    // https://www.youtube.com/watch?app=desktop&v=1IUzNJ6TPEM
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0502-ipo.java
    /**
     *  IDEA :
     *
     *  This greedy approach works because:
     *
     *   - You always pick the most profitable project you
     *     can afford at each step.
     *
     *   - Efficiently maintains project choices using two heaps.
     *
     */
    public int findMaximizedCapital_1(int k, int w, int[] profits, int[] capital) {
        // Max-heap for profits of affordable projects
        /**
         *
         * Why max heap?
         *
         * Because you want to
         * always pick the most profitable project
         * from the ones you can afford.
         */
        Queue<Integer> maxProfit = new PriorityQueue<>(Comparator.reverseOrder());

        // Min-heap for (capital, profit) pairs
        /**
         *
         *  -  This is a `MIN` heap that keeps all projects sorted
         *     by their `required capital`.
         *
         *  -  Why? So you can efficiently find all projects that are now
         *     affordable when your capital (w) increases.
         *
         *
         *  -> minCapital  : [ capital_i, profit_i ]
         *
         *  -> `a[0]` is capital
         */
        Queue<int[]> minCapital = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        /**
         *
         * Prepares the minCapital heap by storing every project
         * as a pair: [required capital, profit].
         */
        for (int i = 0; i < capital.length; i++) {
            minCapital.add(new int[] { capital[i], profits[i] });
        }

        /**
         *
         *  - For each round:
         *
         *    1. Find all projects you can afford now (capital[i] <= w) →
         *       move them from minCapital → maxProfit
         *
         *    2. If no projects are affordable (maxProfit is empty),
         *       you're stuck — break early.
         *
         *    3. Else, pick the project with maximum profit,
         *       and increase your capital by that profit.
         *
         *    4. Repeat.
         *
         */
        for (int i = 0; i < k; i++) {
            // Add all affordable projects to the maxProfit heap
            while (!minCapital.isEmpty() && minCapital.peek()[0] <= w) {
                int[] project = minCapital.poll();
                maxProfit.add(project[1]);
            }

            // If there are no affordable projects, break
            if (maxProfit.isEmpty()) {
                break;
            }

            // Select the project with the maximum profit
            w += maxProfit.poll();
        }

        /**
         *
         * After at most k projects, return the maximum capital you achieved.
         */
        return w;
    }


    // V2
    // https://leetcode.com/problems/ipo/solutions/5315041/easy-to-understand-fast-maxheap-sorting-5sie7/
    // Defining the Project class within the Solution class
    private static class Project {
        int capital;
        int profit;

        Project(int capital, int profit) {
            this.capital = capital;
            this.profit = profit;
        }
    }

    public int findMaximizedCapital_2(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        List<Project> projects = new ArrayList<>();

        // Creating list of projects with capital and profits
        for (int i = 0; i < n; i++) {
            projects.add(new Project(capital[i], profits[i]));
        }

        // Sorting projects by capital required
        Collections.sort(projects, (a, b) -> a.capital - b.capital);

        // Max-heap to store profits (using a min-heap with inverted values)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((x, y) -> y - x);
        int i = 0;

        // Main loop to select up to k projects
        for (int j = 0; j < k; j++) {
            // Add all profitable projects that we can afford
            while (i < n && projects.get(i).capital <= w) {
                maxHeap.add(projects.get(i).profit);
                i++;
            }

            // If no projects can be funded, break out of the loop
            if (maxHeap.isEmpty()) {
                break;
            }

            // Otherwise, take the project with the maximum profit
            w += maxHeap.poll();
        }

        return w;
    }

    // V3
    // https://leetcode.com/problems/ipo/solutions/3219987/day-54-c-priority_queue-easiest-beginner-m55e/
    public int findMaximizedCapital_3(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        int[][] projects = new int[n][2];
        for (int i = 0; i < n; i++) {
            projects[i][0] = capital[i];
            projects[i][1] = profits[i];
        }
        Arrays.sort(projects, (a, b) -> Integer.compare(a[0], b[0]));
        int i = 0;
        PriorityQueue<Integer> maximizeCapital = new PriorityQueue<>(Collections.reverseOrder());
        while (k-- > 0) {
            while (i < n && projects[i][0] <= w) {
                maximizeCapital.offer(projects[i][1]);
                i++;
            }
            if (maximizeCapital.isEmpty()) {
                break;
            }
            w += maximizeCapital.poll();
        }
        return w;
    }

    // V4
    // https://leetcode.com/problems/ipo/solutions/6630069/simple-easy-to-understand-by-kdhakal-ysv5/
    class Project2 {
        private int capital;
        private int profit;

        Project2(int capital, int profit) {
            this.capital = capital;
            this.profit = profit;
        }
    }

    public int findMaximizedCapital_4(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        List<Project2> projects = new ArrayList<>();

        for (int i = 0; i < n; i++)
            projects.add(new Project2(capital[i], profits[i]));

        //sorting projects by capital
        Collections.sort(projects, (a, b) -> a.capital - b.capital);

        //max heap
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);

        for (int i = 0, j = 0; i < k; i++) {
            while (j < n && projects.get(j).capital <= w) {
                heap.add(projects.get(j).profit);
                j++;
            }

            if (heap.isEmpty())
                break;

            w += heap.poll();
        }

        return w;
    }


}
