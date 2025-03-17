package LeetCodeJava.Math;

import java.util.*;


// https://leetcode.com/problems/detect-squares/description/
// https://leetcode.cn/problems/detect-squares/
/**
 * 2013. Detect Squares
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given a stream of points on the X-Y plane. Design an algorithm that:
 *
 * Adds new points from the stream into a data structure. Duplicate points are allowed and should be treated as different points.
 * Given a query point, counts the number of ways to choose three points from the data structure such that the three points and the query point form an axis-aligned square with positive area.
 * An axis-aligned square is a square whose edges are all the same length and are either parallel or perpendicular to the x-axis and y-axis.
 *
 * Implement the DetectSquares class:
 *
 * DetectSquares() Initializes the object with an empty data structure.
 * void add(int[] point) Adds a new point point = [x, y] to the data structure.
 * int count(int[] point) Counts the number of ways to form axis-aligned squares with point point = [x, y] as described above.
 *
 *
 * Example 1:
 *
 *
 * Input
 * ["DetectSquares", "add", "add", "add", "count", "count", "add", "count"]
 * [[], [[3, 10]], [[11, 2]], [[3, 2]], [[11, 10]], [[14, 8]], [[11, 2]], [[11, 10]]]
 * Output
 * [null, null, null, null, 1, 0, null, 2]
 *
 * Explanation
 * DetectSquares detectSquares = new DetectSquares();
 * detectSquares.add([3, 10]);
 * detectSquares.add([11, 2]);
 * detectSquares.add([3, 2]);
 * detectSquares.count([11, 10]); // return 1. You can choose:
 *                                //   - The first, second, and third points
 * detectSquares.count([14, 8]);  // return 0. The query point cannot form a square with any points in the data structure.
 * detectSquares.add([11, 2]);    // Adding duplicate points is allowed.
 * detectSquares.count([11, 10]); // return 2. You can choose:
 *                                //   - The first, second, and third points
 *                                //   - The first, third, and fourth points
 *
 *
 * Constraints:
 *
 * point.length == 2
 * 0 <= x, y <= 1000
 * At most 3000 calls in total will be made to add and count.
 *
 *
 *
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

    // V1-1
    // https://neetcode.io/problems/count-squares
    // IDEA: HASHMAP -1
    public class CountSquares_1_1 {
        private Map<List<Integer>, Integer> ptsCount;
        private List<List<Integer>> pts;

        public CountSquares_1_1() {
            ptsCount = new HashMap<>();
            pts = new ArrayList<>();
        }

        public void add(int[] point) {
            List<Integer> p = Arrays.asList(point[0], point[1]);
            ptsCount.put(p, ptsCount.getOrDefault(p, 0) + 1);
            pts.add(p);
        }

        public int count(int[] point) {
            int res = 0;
            int px = point[0], py = point[1];
            for (List<Integer> pt : pts) {
                int x = pt.get(0), y = pt.get(1);
                if (Math.abs(py - y) != Math.abs(px - x) || x == px || y == py) {
                    continue;
                }
                res += ptsCount.getOrDefault(Arrays.asList(x, py), 0) *
                        ptsCount.getOrDefault(Arrays.asList(px, y), 0);
            }
            return res;
        }
    }

    // V1-2
    // https://neetcode.io/problems/count-squares
    // IDEA: HASHMAP -2
    public class CountSquares_1_2 {
        private Map<Integer, Map<Integer, Integer>> ptsCount;

        public CountSquares_1_2() {
            ptsCount = new HashMap<>();
        }

        public void add(int[] point) {
            int x = point[0], y = point[1];
            ptsCount.putIfAbsent(x, new HashMap<>());
            ptsCount.get(x).put(y, ptsCount.get(x).getOrDefault(y, 0) + 1);
        }

        public int count(int[] point) {
            int res = 0, x1 = point[0], y1 = point[1];

            if (!ptsCount.containsKey(x1)) return res;

            for (int y2 : ptsCount.get(x1).keySet()) {
                int side = y2 - y1;
                if (side == 0) continue;

                int x3 = x1 + side, x4 = x1 - side;
                res += ptsCount.get(x1).get(y2) *
                        ptsCount.getOrDefault(x3, new HashMap<>()).getOrDefault(y1, 0) *
                        ptsCount.getOrDefault(x3, new HashMap<>()).getOrDefault(y2, 0);

                res += ptsCount.get(x1).get(y2) *
                        ptsCount.getOrDefault(x4, new HashMap<>()).getOrDefault(y1, 0) *
                        ptsCount.getOrDefault(x4, new HashMap<>()).getOrDefault(y2, 0);
            }

            return res;
        }
    }

    // V2
    // https://github.com/neetcode-gh/leetcode/blob/main/java/2013-detect-squares.java
    // https://leetcode.com/submissions/detail/761120641/
    class DetectSquares_2 {

        private Integer[][] matrix;

        public DetectSquares_2() {
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

    // V3
    // https://leetcode.com/problems/detect-squares/solutions/1472167/java-clean-solution-with-list-and-hashmap/
    class DetectSquares_3 {
        List<int[]> coordinates;
        Map<String, Integer> counts;

        public DetectSquares_3() {
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

    // V4
    // https://leetcode.ca/2021-09-27-2013-Detect-Squares/
//    class DetectSquares_4 {
//        unordered_map<int, int> cnt;
//        unordered_set<int> xs;
//        inline int key(int x, int y) {
//            return 10000 * x + y;
//        }
//        inline int count(int x, int y) {
//            int k = key(x, y);
//            return cnt.count(k) ? cnt[k] : 0;
//        }
//        public:
//        DetectSquares_4() {}
//        void add(vector<int> p) {
//            cnt[key(p[0], p[1])]++;
//            xs.insert(p[0]);
//        }
//        int count(vector<int> p) {
//            int x = p[0], y = p[1], ans = 0;
//            for (int x1 : xs) {
//                if (x1 == x) continue;
//                int c = count(x1, y), d = Math.abs(x - x1), y1 = y - d, y2 = y + d;
//                if (c == 0) continue;
//                ans += c * (count(x, y1) * count(x1, y1) + count(x, y2) * count(x1, y2));
//            }
//            return ans;
//        }
//    };


}
