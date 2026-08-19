package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximum-average-pass-ratio/

import java.util.PriorityQueue;

/**
 *  1792. Maximum Average Pass Ratio
 *  Medium
 *
 *  There is a school that has classes of students and each class will be having a
 *  final exam. You are given a 2D integer array classes, where
 *  classes[i] = [pass_i, total_i]. In the ith class there are total_i students but
 *  only pass_i of them will pass the exam.
 *
 *  You are also given an integer extraStudents. There are another extraStudents
 *  brilliant students that are guaranteed to pass the exam of any class they are
 *  assigned to. You want to assign each of the extraStudents students to a class in
 *  a way that maximizes the average pass ratio across all the classes.
 *
 *  Return the maximum possible average pass ratio after assigning the extraStudents
 *  students. Answers within 10^-5 of the actual answer will be accepted.
 *
 *  Example 1:
 *    Input: classes = [[1,2],[3,5],[2,2]], extraStudents = 2
 *    Output: 0.78333
 *    Explanation: assign both extra students to the first class ->
 *                 (3/4 + 3/5 + 2/2) / 3 = 0.78333
 *
 *  Example 2:
 *    Input: classes = [[2,4],[3,9],[4,5],[2,10]], extraStudents = 4
 *    Output: 0.53485
 *
 *  Constraints:
 *    1 <= classes.length <= 10^5
 *    classes[i].length == 2
 *    1 <= pass_i <= total_i <= 10^5
 *    1 <= extraStudents <= 10^5
 */
public class MaximumAveragePassRatio {

    // V0
    // IDEA: MAX HEAP ON THE MARGINAL GAIN
    //       putting one extra student into class (a, b) raises its ratio by
    //           gain = (a+1)/(b+1) - a/b
    //       this gain STRICTLY DECREASES as the class grows, so handing each
    //       student to the class with the currently largest gain is optimal
    //       (exchange argument on the per-class decreasing gain sequence).
    /**
     * time = O((n + k) log n)
     * space = O(n)
     */
    public double maxAverageRatio(int[][] classes, int extraStudents) {
        // heap of {gain, pass, total}, largest gain first
        PriorityQueue<double[]> pq = new PriorityQueue<>((x, y) -> Double.compare(y[0], x[0]));
        for (int[] c : classes) {
            pq.add(new double[]{gain(c[0], c[1]), c[0], c[1]});
        }

        for (int i = 0; i < extraStudents; i++) {
            double[] cur = pq.poll();
            double a = cur[1] + 1;
            double b = cur[2] + 1;
            pq.add(new double[]{gain(a, b), a, b});
        }

        double sum = 0.0;
        while (!pq.isEmpty()) {
            double[] cur = pq.poll();
            sum += cur[1] / cur[2];
        }
        return sum / classes.length;
    }

    private double gain(double a, double b) {
        return (a + 1) / (b + 1) - a / b;
    }
}
