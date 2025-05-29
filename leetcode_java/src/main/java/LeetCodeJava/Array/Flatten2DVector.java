package LeetCodeJava.Array;

// https://leetcode.com/problems/flatten-2d-vector/
// https://leetcode.ca/all/251.html

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    // V0-1
    // TODO: fix & validate below
//    class Vector2D{
//
//        List<Integer> collected;
//        int layer;
//        int pointer;
//
//        public Vector2D(int[][] vec) {
//            collected = new ArrayList<>();
//            layer = 0;
//            pointer = 0;
//
//
//            // flatten op
//            for(int i = 0; i < vec.length; i++){
//                for(int x: vec[i]){
//                    this.collected.add(x);
//                }
//            }
//
//        }
//
//        public int next() {
//            if(this.collected.isEmpty()){
//                return -1;
//            }
//            if(pointer > this.collected.size()){
//                return -1;
//            }
//            pointer += 1;
//            return this.collected.get(pointer - 1);
//        }
//
//        public boolean hasNext() {
//
//            return this.pointer < this.collected.size();
//        }
//
//        private void forward() {
//        }

    // V0-2
    // IDEA: (fixed by gpt)
    // TODO: fix & validate below
    class Vector2D_0_2 {
        private List<Integer> collected;
        private int pointer;

        public Vector2D_0_2(int[][] vec) {
            collected = new ArrayList<>();
            pointer = 0;

            for (int[] row : vec) {
                for (int val : row) {
                    collected.add(val);
                }
            }
        }

        public int next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            return collected.get(pointer++);
        }

        public boolean hasNext() {
            return pointer < collected.size();
        }
    }

    // V0-3
    // IDEA: (fixed by gpt)
    // TODO: fix & validate below
    class Vector2D_0_3 {
        private int[][] vec;
        private int row;
        private int col;

        public Vector2D_0_3(int[][] vec) {
            this.vec = vec;
            this.row = 0;
            this.col = 0;
            advanceToNext();
        }

        public int next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            int val = vec[row][col];
            col++;
            advanceToNext();
            return val;
        }

        public boolean hasNext() {
            return row < vec.length;
        }

        // Skip empty rows
        private void advanceToNext() {
            while (row < vec.length && col >= vec[row].length) {
                row++;
                col = 0;
            }
        }
    }

    
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
