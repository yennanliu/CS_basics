package LeetCodeJava.Array;

// https://leetcode.com/problems/flatten-2d-vector/
// https://leetcode.ca/all/251.html
/**
 * 251. Flatten 2D Vector
 * Design and implement an iterator to flatten a 2d vector. It should support the following operations: next and hasNext.
 *
 *
 *
 * Example:
 *
 * Vector2D iterator = new Vector2D([[1,2],[3],[4]]);
 *
 * iterator.next(); // return 1
 * iterator.next(); // return 2
 * iterator.next(); // return 3
 * iterator.hasNext(); // return true
 * iterator.hasNext(); // return true
 * iterator.next(); // return 4
 * iterator.hasNext(); // return false
 *
 *
 * Notes:
 *
 * Please remember to RESET your class variables declared in Vector2D, as static/class variables are persisted across multiple test cases. Please see here for more details.
 * You may assume that next() call will always be valid, that is, there will be at least a next element in the 2d vector when next() is called.
 *
 *
 * Follow up:
 *
 * As an added challenge, try to code it using only iterators in C++ or iterators in Java.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Airbnb Amazon Apple Facebook Google Lyft Salesforce Twitter Uber Zenefits
 *
 *
 *
 *
 */
public class Flatten2DVector {

    // V0
//    class Vector2D{
//        public Vector2D(int[][] vec) {
//        }
//
//        public int next() {
//        }
//
//        public boolean hasNext() {
//        }
//
//        private void forward() {
//        }
//    }

    // V1
    // https://leetcode.ca/2016-08-07-251-Flatten-2D-Vector/
    class Vector2D_1 {
        private int i;
        private int j;
        private int[][] vec;

        public Vector2D_1(int[][] vec) {
            this.vec = vec;
        }

        public int next() {
            forward();
            return vec[i][j++];
        }

        public boolean hasNext() {
            forward();
            return i < vec.length;
        }

        private void forward() {
            while (i < vec.length && j >= vec[i].length) {
                ++i;
                j = 0;
            }
        }
    }


    // V2

}
