package LeetCodeJava.Math;

// https://leetcode.com/problems/valid-square/description/

import java.util.Arrays;

/**
 * 593. Valid Square Attempted Medium Topics Companies Given the coordinates of four points in 2D
 * space p1, p2, p3 and p4, return true if the four points construct a square.
 *
 * <p>The coordinate of a point pi is represented as [xi, yi]. The input is not given in any order.
 *
 * <p>A valid square has four equal sides with positive length and four equal angles (90-degree
 * angles).
 *
 * <p>Example 1:
 *
 * <p>Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,1] Output: true Example 2:
 *
 * <p>Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,12] Output: false Example 3:
 *
 * <p>Input: p1 = [1,0], p2 = [-1,0], p3 = [0,1], p4 = [0,-1] Output: true
 *
 * <p>Constraints:
 *
 * <p>p1.length == p2.length == p3.length == p4.length == 2 -104 <= xi, yi <= 104
 */
public class ValidSquare {

  // V0
  // TODO : implement
  //    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
  //
  //    }

  // V1
  // https://leetcode.com/problems/valid-square/editorial/

  // IDEA : BRUTE FORCE
  /**
   * time = O(N)
   * space = O(1)
   */
  public double dist(int[] p1, int[] p2) {
    return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
  }


  /**
   * time = O(N)
   * space = O(1)
   */
  public boolean check(int[] p1, int[] p2, int[] p3, int[] p4) {
    return dist(p1, p2) > 0
        && dist(p1, p2) == dist(p2, p3)
        && dist(p2, p3) == dist(p3, p4)
        && dist(p3, p4) == dist(p4, p1)
        && dist(p1, p3) == dist(p2, p4);
  }


  /**
   * time = O(N)
   * space = O(1)
   */
  public boolean validSquare_1(int[] p1, int[] p2, int[] p3, int[] p4) {
    int[][] p = {p1, p2, p3, p4};
    return checkAllPermutations(p, 0);
  }

  boolean checkAllPermutations(int[][] p, int l) {
    if (l == 4) {
      return check(p[0], p[1], p[2], p[3]);
    } else {
      boolean res = false;
      for (int i = l; i < 4; i++) {
        swap(p, l, i);
        res |= checkAllPermutations(p, l + 1);
        swap(p, l, i);
      }
      return res;
    }
  }


  /**
   * time = O(N)
   * space = O(1)
   */
  public void swap(int[][] p, int x, int y) {
    int[] temp = p[x];
    p[x] = p[y];
    p[y] = temp;
  }

  // V2
  // https://leetcode.com/problems/valid-square/editorial/

  // IDEA : Using Sorting
  /**
   * time = O(N)
   * space = O(1)
   */
  public double dist_2(int[] p1, int[] p2) {
    return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
  }


  /**
   * time = O(N)
   * space = O(1)
   */
  public boolean validSquare_2(int[] p1, int[] p2, int[] p3, int[] p4) {
    int[][] p = {p1, p2, p3, p4};
    Arrays.sort(p, (l1, l2) -> l2[0] == l1[0] ? l1[1] - l2[1] : l1[0] - l2[0]);
    return dist_2(p[0], p[1]) != 0
        && dist_2(p[0], p[1]) == dist_2(p[1], p[3])
        && dist_2(p[1], p[3]) == dist_2(p[3], p[2])
        && dist_2(p[3], p[2]) == dist_2(p[2], p[0])
        && dist_2(p[0], p[3]) == dist_2(p[1], p[2]);
  }

  // V3
  // https://leetcode.com/problems/valid-square/editorial/

  // IDEA : Checking every case
  /**
   * time = O(N)
   * space = O(1)
   */
  public double dist_3(int[] p1, int[] p2) {
    return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
  }


  /**
   * time = O(N)
   * space = O(1)
   */
  public boolean check_3(int[] p1, int[] p2, int[] p3, int[] p4) {
    return dist_3(p1, p2) > 0
        && dist_3(p1, p2) == dist_3(p2, p3)
        && dist_3(p2, p3) == dist_3(p3, p4)
        && dist_3(p3, p4) == dist_3(p4, p1)
        && dist_3(p1, p3) == dist_3(p2, p4);
  }


  /**
   * time = O(N)
   * space = O(1)
   */
  public boolean validSquare_3(int[] p1, int[] p2, int[] p3, int[] p4) {
    return check_3(p1, p2, p3, p4) || check_3(p1, p3, p2, p4) || check_3(p1, p2, p4, p3);
  }
}
