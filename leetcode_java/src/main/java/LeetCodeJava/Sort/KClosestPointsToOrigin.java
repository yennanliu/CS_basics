package LeetCodeJava.Sort;

// https://leetcode.com/problems/k-closest-points-to-origin/
/**
 * 973. K Closest Points to Origin
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane and an integer k, return the k closest points to the origin (0, 0).
 *
 * The distance between two points on the X-Y plane is the Euclidean distance (i.e., √(x1 - x2)2 + (y1 - y2)2).
 *
 * You may return the answer in any order. The answer is guaranteed to be unique (except for the order that it is in).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: points = [[1,3],[-2,2]], k = 1
 * Output: [[-2,2]]
 * Explanation:
 * The distance between (1, 3) and the origin is sqrt(10).
 * The distance between (-2, 2) and the origin is sqrt(8).
 * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
 * We only want the closest k = 1 points from the origin, so the answer is just [[-2,2]].
 * Example 2:
 *
 * Input: points = [[3,3],[5,-1],[-2,4]], k = 2
 * Output: [[3,3],[-2,4]]
 * Explanation: The answer [[-2,4],[3,3]] would also be accepted.
 *
 *
 * Constraints:
 *
 * 1 <= k <= points.length <= 104
 * -104 <= xi, yi <= 104
 *
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KClosestPointsToOrigin {

    // V0
    // IDEA: MAX PQ + custom sorting (with function) (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[][] kClosest(int[][] points, int k) {
        if (points == null || points.length == 0 || k <= 0) {
            return new int[0][];
        }

        // Min-heap based on distance
        /**
         *  NOTE !!!
         *
         *   can custom sorting PQ with `method` as well
         *   -> the val comes from `getDist` method
         *   -> and PQ sort on the calculated value
         */
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            /**
             * time = O(N)
             * space = O(N)
             */
            public int compare(int[] a, int[] b) {
                double distA = getDist(a[0], a[1]);
                double distB = getDist(b[0], b[1]);
                return Double.compare(distA, distB); // compare properly
            }
        });

        // Add all points to the heap
        for (int[] point : points) {
            pq.add(point);
        }

        // Extract k closest
        int[][] res = new int[k][2];
        for (int i = 0; i < k; i++) {
            res[i] = pq.poll();
        }

        return res;
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public double getDist(int x, int y) {
        return Math.sqrt(x * x + y * y);
    }

    // V0-0-1
    // IDEA: BIG PQ
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[][] kClosest_0_0_1(int[][] points, int k) {
        // edge
        if (points == null || points.length == 0 || points[0].length == 0) {
            return null;
        }
        // big PQ
        // { [x, y, dist] }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            /**
             * time = O(N)
             * space = O(N)
             */
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[2] - o1[2];
                return diff;
            }
        });

        for (int[] p : points) {
            int x = p[0];
            int y = p[1];
            int dist = (x * x) + (y * y);
            pq.add(new Integer[] { x, y, dist });
            /** NOTE !!!
             *
             *  space optimization:  pop elements with PQ size > K
             *
             *  so the remaining elements in PQ is the answer we will return
             */
            // pop elements with PQ size > K
            while (pq.size() > k) {
                pq.poll();
            }
        }

        /** NOTE !!!
         *
         *  we define res array as `k * 2` dimension array
         */
        int[][] res = new int[k][2]; // ???
        for (int i = 0; i < k; i++) {
            Integer[] arr = pq.poll();
            int x_ = arr[0];
            int y_ = arr[1];
            res[i] = new int[] { x_, y_ };
        }

        return res;
    }


    // V0-1
    // IDEA: PriorityQueue (gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[][] kClosest_0_1(int[][] points, int k) {
        if (points == null || points.length == 0 || k <= 0) {
            return new int[0][0];
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(this::getDistanceSquared));

        for (int[] point : points) {
            pq.offer(point);
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = pq.poll();
        }

        return result;
    }

    private int getDistanceSquared(int[] point) {

        return point[0] * point[0] + point[1] * point[1];
    }


    // V0-2
    // IDEA: HASHMAP + PQ
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[][] kClosest_0_2(int[][] points, int k) {
        // edge
        if (points == null || points.length == 0) {
            return new int[0][0]; // fix
        }
        if (points.length == 1) {
            return points;
        }

        // map : {dist : [[x1,y1], [x2,y2], ....] }
        Map<Integer, List<Integer[]>> dist_map = new HashMap<>();
        for (int[] p : points) {
            int dist = p[0] * p[0] + p[1] * p[1];
            List<Integer[]> tmp = dist_map.getOrDefault(dist, new ArrayList<>());
            tmp.add(new Integer[] { p[0], p[1] }); // fix
            dist_map.put(dist, tmp);
        }

        // pq sorts distances (smallest first)
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            /**
             * time = O(N)
             * space = O(N)
             */
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2); // fix
            }
        });

        for (Integer key : dist_map.keySet()) {
            pq.add(key);
        }

        int[][] res = new int[k][2];
        int i = 0;
        while (i < k && !pq.isEmpty()) {
            int dist = pq.poll();
            List<Integer[]> list = dist_map.get(dist);
            for (int j = 0; j < list.size() && i < k; j++) { // fix
                res[i][0] = list.get(j)[0];
                res[i][1] = list.get(j)[1];
                i++;
            }
        }

        return res;
    }

    // V1
    // IDEA : SORTING
    // https://leetcode.com/problems/k-closest-points-to-origin/editorial/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[][] kClosest_2(int[][] points, int K) {
        int N = points.length;
        int[] dists = new int[N];
        for (int i = 0; i < N; ++i)
            dists[i] = dist(points[i]);

        Arrays.sort(dists);
        int distK = dists[K-1];

        int[][] ans = new int[K][2];
        int t = 0;
        for (int i = 0; i < N; ++i)
            if (dist(points[i]) <= distK)
                ans[t++] = points[i];
        return ans;
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int dist(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    // V2
    // IDEA : Divide and Conquer
    // https://leetcode.com/problems/k-closest-points-to-origin/editorial/
    int[][] points;
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[][] kClosest_3(int[][] points, int K) {
        this.points = points;
        sort(0, points.length - 1, K);
        return Arrays.copyOfRange(points, 0, K);
    }

    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void sort(int i, int j, int K) {
        if (i >= j) return;
        int k = ThreadLocalRandom.current().nextInt(i, j);
        swap(i, k);

        int mid = partition(i, j);
        int leftLength = mid - i + 1;
        if (K < leftLength)
            sort(i, mid - 1, K);
        else if (K > leftLength)
            sort(mid + 1, j, K - leftLength);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int partition(int i, int j) {
        int oi = i;
        int pivot = dist(i);
        i++;

        while (true) {
            while (i < j && dist(i) < pivot)
                i++;
            while (i <= j && dist(j) > pivot)
                j--;
            if (i >= j) break;
            swap(i, j);
        }
        swap(oi, j);
        return j;
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int dist(int i) {
        return points[i][0] * points[i][0] + points[i][1] * points[i][1];
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public void swap(int i, int j) {
        int t0 = points[i][0], t1 = points[i][1];
        points[i][0] = points[j][0];
        points[i][1] = points[j][1];
        points[j][0] = t0;
        points[j][1] = t1;
    }



}
