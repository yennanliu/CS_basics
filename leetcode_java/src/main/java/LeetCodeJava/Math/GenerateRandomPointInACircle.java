package LeetCodeJava.Math;

// https://leetcode.com/problems/generate-random-point-in-a-circle/

import java.util.Random;

/**
 *  478. Generate Random Point in a Circle
 *  Medium
 *
 *  Given the radius and the position of the center of a circle, implement the function
 *  randPoint which generates a uniform random point inside the circle.
 *
 *  Implement the Solution class:
 *    - Solution(double radius, double x_center, double y_center) initializes the object
 *      with the radius of the circle radius and the position of the center (x_center, y_center).
 *    - randPoint() returns a random point inside the circle. A point on the circumference
 *      of the circle is considered to be in the circle. The answer is returned as an array
 *      [x, y].
 *
 *  Example 1:
 *    Input: ["Solution", "randPoint", "randPoint", "randPoint"]
 *           [[1.0, 0.0, 0.0], [], [], []]
 *    Output: [null, [-0.02493, -0.38077], [0.82314, 0.38945], [0.36572, 0.17248]]
 *
 *  Constraints:
 *    0 < radius <= 10^8
 *    -10^7 <= x_center, y_center <= 10^7
 *    At most 3 * 10^4 calls will be made to randPoint.
 */
public class GenerateRandomPointInACircle {

    private final double radius;
    private final double xCenter;
    private final double yCenter;
    private final Random rand;

    public GenerateRandomPointInACircle(double radius, double x_center, double y_center) {
        this.radius = radius;
        this.xCenter = x_center;
        this.yCenter = y_center;
        this.rand = new Random();
    }

    // V0
    // IDEA: sample the radius as r = R * sqrt(u) so the points are uniform over the AREA
    //       (a plain uniform r would over-sample the center), then a uniform angle.
    /**
     * time = O(1)
     * space = O(1)
     */
    public double[] randPoint() {
        double r = this.radius * java.lang.Math.sqrt(this.rand.nextDouble());
        double theta = 2 * java.lang.Math.PI * this.rand.nextDouble();
        return new double[] { this.xCenter + r * java.lang.Math.cos(theta),
                              this.yCenter + r * java.lang.Math.sin(theta) };
    }

    // V1
    // IDEA: rejection sampling - draw a point in the bounding square, retry until inside
    /**
     * time = O(1) expected (accept probability = pi/4)
     * space = O(1)
     */
    public double[] randPoint_1() {
        while (true) {
            double x = (this.rand.nextDouble() * 2 - 1) * this.radius;
            double y = (this.rand.nextDouble() * 2 - 1) * this.radius;
            if (x * x + y * y <= this.radius * this.radius) {
                return new double[] { this.xCenter + x, this.yCenter + y };
            }
        }
    }
}
