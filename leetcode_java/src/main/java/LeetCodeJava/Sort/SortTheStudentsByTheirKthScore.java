package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-the-students-by-their-kth-score/

import java.util.Arrays;

/**
 *  2545. Sort the Students by Their Kth Score
 *  Medium
 *
 *  There is a class with m students and n exams. You are given a 0-indexed
 *  m x n integer matrix score, where each row represents one student and
 *  score[i][j] denotes the score the ith student got in the jth exam. The matrix
 *  score contains distinct integers only.
 *
 *  You are also given an integer k. Sort the students (i.e., the rows of the
 *  matrix) by their scores in the kth (0-indexed) exam from the highest to the
 *  lowest.
 *
 *  Return the matrix after sorting it.
 *
 *  Example 1:
 *    Input: score = [[10,6,9,1],[7,5,11,2],[4,8,3,15]], k = 2
 *    Output: [[7,5,11,2],[10,6,9,1],[4,8,3,15]]
 *
 *  Example 2:
 *    Input: score = [[3,4],[5,6]], k = 0
 *    Output: [[5,6],[3,4]]
 *
 *  Constraints:
 *    m == score.length, n == score[i].length
 *    1 <= m, n <= 250
 *    1 <= score[i][j] <= 10^5
 *    score consists of distinct integers.
 *    0 <= k < n
 */
public class SortTheStudentsByTheirKthScore {

    // V0
    // IDEA: SORT THE ROW REFERENCES BY THE KEY COLUMN, DESCENDING
    //       rows move as whole units and only column k decides the order, so
    //       sorting int[][] with a comparator on row[k] permutes the row
    //       POINTERS (no row content is copied).
    //       all values are distinct -> no ties, so stability never matters.
    /**
     * time = O(m log m)   // comparisons are O(1); row moves are pointer moves
     * space = O(1) extra  // in-place on the given array
     */
    public int[][] sortTheStudents(int[][] score, int k) {
        Arrays.sort(score, (a, b) -> Integer.compare(b[k], a[k]));
        return score;
    }
}
