package LeetCodeJava.Math;

// https://leetcode.com/problems/detect-squares/description/

import java.util.*;

/**
 * Your DetectSquares object will be instantiated and called as such:
 * DetectSquares obj = new DetectSquares();
 * obj.add(point);
 * int param_2 = obj.count(point);
 */

public class DetectSquares {

    // V0
    // IDEA : MATH
    // TODO : fix
//    class DetectSquares_0 {
//
//        List<List<Integer>> points;
//        int count;
//
//        public DetectSquares_0() {
//            this.points = new ArrayList<>();
//            this.count = 0;
//        }
//
//        public void add(int[] point) {
//            List<Integer> newPoint = new ArrayList<>();
//            newPoint.add(point[0]);
//            newPoint.add(point[1]);
//            this.points.add(newPoint);
//        }
//
//        public int count(int[] point) {
//            if (this.points.size() <= 3){
//                return 0;
//            }
//            // get possible Squares count
//
//            return this.count;
//        }
//
//        public boolean isValidSquares(List<List<Integer>> points, int[] point){
//            int distx = getDistance(points.get(0), Arrays.asList(point));
//        }
//
//        public int getDistance(int x1, int y1, int x2, int y2){
//            int diff1 = x1 - x2;
//            int diff2 = y1 - y2;
//            return diff1 * diff1 + diff2 * diff2;
//        }
//
//    }

    // V1
    // https://github.com/neetcode-gh/leetcode/blob/main/java/2013-detect-squares.java
    // https://leetcode.com/submissions/detail/761120641/
    class DetectSquares_1 {

        private Integer[][] matrix;

        public DetectSquares_1() {
            matrix = new Integer[1001][1001];
        }

        public void add(int[] point) {
            if (matrix[point[0]][point[1]] == null) {
                matrix[point[0]][point[1]] = 1;
            } else {
                matrix[point[0]][point[1]] = matrix[point[0]][point[1]] + 1;
            }
        }

        public int count(int[] point) {
            int currentSquareCount = 0;
            int currentPointCount = 1;
            int startRow = point[0];
            int startCol = point[1];
            int curRow = point[0];
            int curCol = point[1];

            while (curRow != 0 && curCol != 0) {
                curRow--;
                curCol--;
                if (
                        matrix[curRow][curCol] != null &&
                                matrix[startRow][curCol] != null &&
                                matrix[curRow][startCol] != null
                ) {
                    currentSquareCount =
                            currentSquareCount +
                                    (
                                            currentPointCount *
                                                    matrix[curRow][curCol] *
                                                    matrix[startRow][curCol] *
                                                    matrix[curRow][startCol]
                                    );
                }
            }

            curRow = point[0];
            curCol = point[1];
            while (curRow != 1000 && curCol != 1000) {
                curRow++;
                curCol++;
                if (
                        matrix[curRow][curCol] != null &&
                                matrix[startRow][curCol] != null &&
                                matrix[curRow][startCol] != null
                ) {
                    currentSquareCount =
                            currentSquareCount +
                                    (
                                            currentPointCount *
                                                    matrix[curRow][curCol] *
                                                    matrix[startRow][curCol] *
                                                    matrix[curRow][startCol]
                                    );
                }
            }

            curRow = point[0];
            curCol = point[1];
            while (curRow != 0 && curCol != 1000) {
                curRow--;
                curCol++;
                if (
                        matrix[curRow][curCol] != null &&
                                matrix[startRow][curCol] != null &&
                                matrix[curRow][startCol] != null
                ) {
                    currentSquareCount =
                            currentSquareCount +
                                    (
                                            currentPointCount *
                                                    matrix[curRow][curCol] *
                                                    matrix[startRow][curCol] *
                                                    matrix[curRow][startCol]
                                    );
                }
            }

            curRow = point[0];
            curCol = point[1];
            while (curRow != 1000 && curCol != 0) {
                curRow++;
                curCol--;
                if (
                        matrix[curRow][curCol] != null &&
                                matrix[startRow][curCol] != null &&
                                matrix[curRow][startCol] != null
                ) {
                    currentSquareCount =
                            currentSquareCount +
                                    (
                                            currentPointCount *
                                                    matrix[curRow][curCol] *
                                                    matrix[startRow][curCol] *
                                                    matrix[curRow][startCol]
                                    );
                }
            }

            return currentSquareCount;
        }
    }

    // V2
    // https://leetcode.com/problems/detect-squares/solutions/1472167/java-clean-solution-with-list-and-hashmap/
    class DetectSquares_2 {
        List<int[]> coordinates;
        Map<String, Integer> counts;

        public DetectSquares_2() {
            coordinates = new ArrayList<>();
            counts = new HashMap<>();
        }

        public void add(int[] point) {
            coordinates.add(point);
            String key = point[0] + "@" + point[1];
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        public int count(int[] point) {
            int sum = 0, px = point[0], py = point[1];
            for (int[] coordinate : coordinates) {
                int x = coordinate[0], y = coordinate[1];
                if (px == x || py == y || (Math.abs(px - x) != Math.abs(py - y)))
                    continue;
                sum += counts.getOrDefault(x + "@" + py, 0) * counts.getOrDefault(px + "@" + y, 0);
            }

            return sum;
        }
    }


}
