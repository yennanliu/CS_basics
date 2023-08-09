package LeetCodeJava.Sort;

// https://leetcode.com/problems/k-closest-points-to-origin/


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KClosestPointsToOrigin {

    // V0
    // TODO : fix below
//    public int[][] kClosest(int[][] points, int k) {
//
//        //int[] ans = new int[]{};
//        List<int[]> ans = new ArrayList<>();
//
//        if (points.length == 1){
//            return points;
//        }
//
//        // minimum PQ
//        PriorityQueue<Integer> pq = new PriorityQueue<>();
//        HashMap<Integer, Integer[]> map = new HashMap<>();
//
//        for (int[] x : points){
//            Integer dist = x[0] * x[0] + x[1] * x[1];
//            //map.put(dist, x);
//            pq.add(dist);
//        }
//
//        for (int i = 0; i < k; i++){
//            int key = pq.remove();
//            Integer[] val = map.get(key);
//            ans.add(val);
//        }
//
//        int[] ans2 = new int[ans.size()];
//        for (int j = 0; j < ans.size(); j++){
//            ans2[j] = ans.get(j);
//        }
//        //return (int[][]) ans.toArray();
//        return null;
//    }

    // V1
    // IDEA : SORTING
    // https://leetcode.com/problems/k-closest-points-to-origin/editorial/
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

    public int dist(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    // V2
    // IDEA : Divide and Conquer
    // https://leetcode.com/problems/k-closest-points-to-origin/editorial/
    int[][] points;
    public int[][] kClosest_3(int[][] points, int K) {
        this.points = points;
        sort(0, points.length - 1, K);
        return Arrays.copyOfRange(points, 0, K);
    }

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

    public int dist(int i) {
        return points[i][0] * points[i][0] + points[i][1] * points[i][1];
    }

    public void swap(int i, int j) {
        int t0 = points[i][0], t1 = points[i][1];
        points[i][0] = points[j][0];
        points[i][1] = points[j][1];
        points[j][0] = t0;
        points[j][1] = t1;
    }

}
