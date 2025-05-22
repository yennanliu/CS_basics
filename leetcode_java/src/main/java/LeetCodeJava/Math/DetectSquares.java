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
    class DetectSquares_0{

        // attr
        Map<String, Integer> pointCnt;
        Set<String> points; // To check the existence of points quickly

        public DetectSquares_0() {
            this.pointCnt = new HashMap<>();
            this.points = new HashSet<>();
        }

        public void add(int[] point) {
            String key = point[0] + "," + point[1]; // Use a string to represent the point
            this.pointCnt.put(key, this.pointCnt.getOrDefault(key, 0) + 1);
            this.points.add(key); // Add the point to the set
        }

        public int count(int[] point) {
            int cnt = 0;
            int p_x = point[0];
            int p_y = point[1];

            // Check all potential squares that can be formed with the given point
            for (String key : this.pointCnt.keySet()) {
                String[] parts = key.split(",");
                int x1 = Integer.parseInt(parts[0]);
                int y1 = Integer.parseInt(parts[1]);

                // Ensure the point we're comparing with is not the same as the input point
                if (x1 == p_x && y1 == p_y)
                    continue;

                // Check if a square can be formed by having another point at (p_x, y1) and (x1,
                // p_y)
                if (Math.abs(x1 - p_x) == Math.abs(y1 - p_y)) {
                    // Form the potential other two points in the square
                    String coord1 = p_x + "," + y1; // Point (p_x, y1)
                    String coord2 = x1 + "," + p_y; // Point (x1, p_y)

                    // If both of these points exist, a square is formed
                    if (this.points.contains(coord1) && this.points.contains(coord2)) {
                        // Multiply the counts of the points involved in forming a square
                        cnt += this.pointCnt.get(key) * this.pointCnt.get(coord1) * this.pointCnt.get(coord2);
                    }
                }
            }

            return cnt;
        }
    }

    // V0-1
    // IDEA: HASHMAP + list + math (fixed by gpt)
    class DetectSquares_0_1 {

        // attr
        Map<String, Integer> pointCnt;
        Set<String> points; // To check the existence of points quickly

        public DetectSquares_0_1() {
            this.pointCnt = new HashMap<>();
            this.points = new HashSet<>();
        }

        public void add(int[] point) {
            String key = point[0] + "," + point[1]; // Use a string to represent the point
            this.pointCnt.put(key, this.pointCnt.getOrDefault(key, 0) + 1);
            this.points.add(key); // Add the point to the set
        }

        public int count(int[] point) {
            int cnt = 0;
            int p_x = point[0];
            int p_y = point[1];

            // Check all potential squares that can be formed with the given point
            for (String key : this.pointCnt.keySet()) {
                String[] parts = key.split(",");
                int x1 = Integer.parseInt(parts[0]);
                int y1 = Integer.parseInt(parts[1]);

                // Ensure the point we're comparing with is not the same as the input point
                if (x1 == p_x && y1 == p_y)
                    continue;

                // Check if a square can be formed by having another point at (p_x, y1) and (x1,
                // p_y)
                if (Math.abs(x1 - p_x) == Math.abs(y1 - p_y)) {
                    // Form the potential other two points in the square
                    String coord1 = p_x + "," + y1; // Point (p_x, y1)
                    String coord2 = x1 + "," + p_y; // Point (x1, p_y)

                    // If both of these points exist, a square is formed
                    if (this.points.contains(coord1) && this.points.contains(coord2)) {
                        // Multiply the counts of the points involved in forming a square
                        cnt += this.pointCnt.get(key) * this.pointCnt.get(coord1) * this.pointCnt.get(coord2);
                    }
                }
            }

            return cnt;
        }
    }

    // V0-2

    class DetectSquares_0_2 {
        // Map<x, Map<y, count>>: stores count of each point (x, y)
        private Map<Integer, Map<Integer, Integer>> pointMap;

        public DetectSquares_0_2() {
            pointMap = new HashMap<>();
        }

        public void add(int[] point) {
            int x = point[0], y = point[1];
            pointMap.putIfAbsent(x, new HashMap<>());
            Map<Integer, Integer> yMap = pointMap.get(x);
            yMap.put(y, yMap.getOrDefault(y, 0) + 1);
        }

        public int count(int[] point) {
            int x = point[0], y = point[1];
            int total = 0;

            if (!pointMap.containsKey(x))
                return 0;

            // Iterate over all possible square side lengths (dx)
            for (int x1 : pointMap.keySet()) {
                if (x1 == x)
                    continue; // skip same column
                int d = x1 - x;

                for (int yOffset : new int[]{d, -d}) {
                    int y1 = y + yOffset;

                    int c1 = pointMap.getOrDefault(x1, Collections.emptyMap()).getOrDefault(y, 0); // (x1, y)
                    int c2 = pointMap.getOrDefault(x1, Collections.emptyMap()).getOrDefault(y1, 0); // (x1, y1)
                    int c3 = pointMap.getOrDefault(x, Collections.emptyMap()).getOrDefault(y1, 0); // (x, y1)

                    total += c1 * c2 * c3;
                }
            }

            return total;
        }


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
}
