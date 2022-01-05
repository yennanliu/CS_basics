//package LeetCodeJava.BreadthFirstSearch;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class Solution {
//    private List<List<Integer>> forest;
//    private int w, h;
//    private int[] dx = new int[]{1, 0, -1, 0};
//    private int[] dy = new int[]{0, 1, 0, -1};
//    public int cutOffTree(List<List<Integer>> forest) {
//        this.forest = forest;
//        w = forest.size();
//        h = forest.get(0).size();
//        int ans = 0;
//        List<int[]> trees = new LinkedList<>();
//        for (int x = 0; x < w; x++) {
//            for (int y = 0; y < h; y++) {
//                int v = forest.get(x).get(y);
//                if (v <= 1) continue;
//                trees.add(new int[]{x, y, v});
//            };
//        }
//        trees.sort((x, y) -> x[2] - y[2]);
//        int sx = 0, sy = 0;
//        for (int[] tree : trees) {
//            int tx = tree[0], ty = tree[1];
//            int dist = findDist(sx, sy, tx, ty);
//            if (dist < 0) return -1;
//            ans += dist;
//            sx = tx;
//            sy = ty;
//        }
//        return ans;
//    }
//    public int findDist(int sx, int sy, int tx, int ty) {
//        LinkedList<int[]> queue = new LinkedList<>();
//        queue.add(new int[]{sx, sy, 0});
//        Set<Integer> vset = new HashSet<>();
//        vset.add(sx * h + sy);
//        while (!queue.isEmpty()) {
//            int[] first = queue.poll();
//            int x = first[0], y = first[1], s = first[2];
//            if (x == tx && y == ty) return s;
//            for (int i = 0; i < dx.length; i++) {
//                int nx = x + dx[i];
//                int ny = y + dy[i];
//                if (nx < 0 || nx >= w || ny < 0 || ny >= h)
//                    continue;
//                if (forest.get(nx).get(ny) == 0) continue;
//                if (vset.contains(nx * h + ny)) continue;
//                vset.add(nx * h + ny);
//                queue.add(new int[]{nx, ny, s + 1});
//            }
//        }
//        return -1;
//    }
//}