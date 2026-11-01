package LeetCodeJava.Math;

// https://leetcode.com/problems/type-of-triangle/description/

import java.util.Arrays;

/**
 * 3024. Type of Triangle
 * Easy
 *
 * You are given a 0-indexed integer array nums of size 3 which can form the sides of a triangle.
 *
 * A triangle is called equilateral if it has all sides of equal length.
 * A triangle is called isosceles if it has exactly two sides of equal length.
 * A triangle is called scalene if all its sides are of different lengths.
 *
 * Return a string representing the type of triangle that can be formed or "none" if it cannot form a triangle.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,3,3]
 * Output: "equilateral"
 *
 * Example 2:
 *
 * Input: nums = [3,4,5]
 * Output: "scalene"
 *
 *
 * Constraints:
 *
 * nums.length == 3
 * 1 <= nums[i] <= 100
 *
 */
public class TypeOfTriangle {

    // V0
    // IDEA: SORT FIRST -- with a <= b <= c, a + b > c alone decides whether a triangle exists
    /**
     * time = O(1)
     * space = O(1)
     */
    public String triangleType(int[] nums) {
        // sort a copy so the caller's array is untouched
        int[] sides = Arrays.copyOf(nums, 3);
        Arrays.sort(sides);
        int a = sides[0];
        int b = sides[1];
        int c = sides[2];

        if (a + b <= c) {
            return "none";
        }
        if (a == b && b == c) {
            return "equilateral";
        }
        // sorted, so equal sides must be neighbours
        if (a == b || b == c) {
            return "isosceles";
        }
        return "scalene";
    }
}
