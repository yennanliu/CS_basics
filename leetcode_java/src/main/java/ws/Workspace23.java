package ws;

import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.Heap.LongestHappyString;

import java.util.*;

public class Workspace23 {

    // LC 1405
    // 7.51 - 8.01
    class ValCnt {
        char val;
        int cnt;

        ValCnt(char val, int cnt) {
            this.val = val;
            this.cnt = cnt;
        }
    }


    public String longestDiverseString(int a, int b, int c) {

        /** NOTE !!! we define custom class to trace both val and cnt */
        PriorityQueue<ValCnt> pq = new PriorityQueue<>(
                (x, y) -> y.cnt - x.cnt);

        StringBuilder sb = new StringBuilder();


        if (a > 0)
            pq.add(new ValCnt('a', a));
        if (b > 0)
            pq.add(new ValCnt('b', b));
        if (c > 0)
            pq.add(new ValCnt('c', c));


        /** NOTE !!!
         *
         *   ONLY 2 cases below:
         *
         *   1. if adding this char causes 3 consecutive
         *   2. else (can add cur char)
         */

        // /?????
        while(!pq.isEmpty()){

            // ???
            ValCnt first = pq.poll();
            int len = sb.length();

            //  1. if adding this char causes 3 consecutive
            if(len >= 2 &&
                    sb.charAt(len - 1) == first.val &&
                    sb.charAt(len - 2) == first.val){

                // edge case !!!
                if(pq.isEmpty()){
                    break;
                }

                // ???
                ValCnt second = pq.poll();
                sb.append(second.val);
                if(second.cnt - 1 > 0){
                    pq.add(new ValCnt(second.val, second.cnt - 1));
                }
                pq.add(first);
            }
            // 2. else (can add cur char)
            else{
                sb.append(first.val);
                if(first.cnt - 1 > 0){
                    pq.add(new ValCnt(first.val, first.cnt - 1));
                }
            }


        }

        return sb.toString();
    }



    // LC 1054
    // 8.32 - 43 am
    /**
     * IDEA 1) BRUTE FORCE
     *
     * IDEA 2) PQ ???
     *
     *
     */
    class ValCnt2{
        int val;
        int cnt;

        ValCnt2(int val, int cnt) {
            this.val = val;
            this.cnt = cnt;
        }

        @Override
        public String toString() {
            System.out.println(">> val = " + val + ", cnt = " + cnt);
           // return super.toString();
            return ">> val = " + val + ", cnt = " + cnt;
        }

    }
    public int[] rearrangeBarcodes(int[] barcodes) {
        // edge

        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();

        for(int x: barcodes){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // pq : big pq, sort on cnt (big -> small)
        PriorityQueue<ValCnt2> pq = new PriorityQueue<>(new Comparator<ValCnt2>() {
            @Override
            public int compare(ValCnt2 o1, ValCnt2 o2) {
                int diff = o2.cnt - o1.cnt;
                return diff;
            }
        });

        for(int k: map.keySet()){
            pq.add(new ValCnt2(k, map.get(k)));
        }

        int[] res = new int[barcodes.length];


        // ???
        int i = 0;
        while (!pq.isEmpty()){
            ValCnt2 first = pq.poll();
            int len = pq.size();

            System.out.println(">>> res = " + Arrays.toString(res) +
                    ", len = " + len +
                    ", first = " + first);

            // case 1) prev == cur
            if(len >= 1 && res[i-1] == first.val){
                ValCnt2 second = pq.poll();

                System.out.println(">>> second = " + second);

                res[i] = second.val;
                second.cnt -= 1;
                if(second.cnt > 0){
                    pq.add(second);
                }
                pq.add(first);
            }
            // case 2) else
            else{
                //pq.add(first);
                res[i] = first.val;
                first.cnt -= 1;
                if(first.cnt > 0){
                    pq.add(first);
                }
            }

            i += 1;
        }


        return res;
    }


    // LC 871
    // 9.50 - 10 am
    /**
     *  -> Return the `minimum` number of
     *     refueling stops the car must make in order
     *     to reach its destination.
     *       - If it cannot reach the destination,
     *          return -1.
     *
     *  - car: start -> dest
     *  - target miles away (east)
     *  - car has `startFuel` of gas at beginning
     *  - use 1 litter of gas / mile
     *
     *   - stations[i] = [positioni, fueli]
     *
     *     - positioni: positioni  miles east of the start point
     *     - fueli: has fueli of gas
     *
     *   -
     *
     *
     *  ------------------
     *
     *   IDEA 1) BRUTE FORCE ???
     *
     *   IDEA 2) PQ ???
     *
     *     -> big PQ. sort on positioni,
     *        ONLY add stations when its dist <= cur gas + cur_position
     *
     *        ...
     *
     *        repeat above till can or can't reach the destination
     *
     *  IDEA 3) GREEDY / DP ???
     *
     *
     *
     *  -----------------
     *
     *  ex 3)
     *   Input: target = 100, startFuel = 10,
     *   stations = [[10,60],[20,30],[30,30],[60,40]]
     *   Output: 2
     *
     *
     *   -> PQ =  [ [10,60] ],  p = 0, sf = 10, t = 100,
     *   -> PQ = [ [20,30],[30,30],[60,40] ]  p = 10, sf = 60, t = 100
     *   -> PQ =   [ [20,30],[30,30] ], p = 60, sf = 50, t = 100
     *   ->  p + sf >= 100, so we CAN already reach the dest,
     *       NO NEED to visit gas station anymore
     *
     *
     *
     */
    // IDEA 2) PQ ???
    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        // edge

        // PQ: big PQ: [positioni, fueli]
        // sort on positioni (big -> small)
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[0] - o1[0];
                return diff;
            }
        });

        // /??
        //init
        for(int[] s: stations){
            pq.add(new Integer[]{s[0], s[1]});
        }

        int curPosition = 0;
        int opCnt = 0;

        //List<Integer[]> backup = new ArrayList<>();

        // ????
        while (curPosition < target || !pq.isEmpty()){

            // ??? reset backup everytime ???? //?? better way ????
            List<Integer[]> backup = new ArrayList<>();

            while (!pq.isEmpty() && pq.peek()[0] > curPosition + startFuel){
                backup.add(pq.poll());
            }

            // edge ???
            if(pq.isEmpty() && curPosition < target){
                return -1;
            }

            // found the `next` station
            Integer[] next = pq.poll();
            target += next[0];
            startFuel = ( next[1] + startFuel - next[0] );
            opCnt += 1;

            // add backup back to PQ
            for(Integer[] x: backup){
                pq.add(x);
            }
        }



        return opCnt > 0 ? opCnt : -1;
    }




    // LC 834
    // 11.13 - 23 am
    /**
     * -> Return an array answer of length n
     *   where answer[i]
     *    - is the sum of the distances between the ith
     *       - node in the tree and all other nodes.
     *
     *
     *  - n nodes: 0 -> n-1
     *
     *  - n-1 edges
     *    - edges[i] = [ai, bi]
     *       -  edge between nodes ai and bi in the tree.
     *
     *
     *
     *
     *  -----------------
     *
     *   IDEA 1) DFS ?????
     *
     *     dist(a,b) =
     *       if `different` side
     *        - dist(a, root) + dist(b, root)
     *       if `same` side
     *         - dist(a, root) - dist(b, root)
     *
     *   IDEA 1) DFS + prefix sum ???
     *
     *
     *
     *
     *  -----------------
     *
     *
     */

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // edge


        // build tree ?????

        // { parent:
        Map<Integer, List<Integer>> map = new HashMap<>();

        // ??
        int[] pathList = new int[edges.length];

        return pathList;
    }


    private void distHelper(){

    }

//    private Throwable myBuildTree(int[][] edges, ){
//        // ???
//        int[] rootItem = edges[]
//    }


    // LC 872
    // 17.28 - 38 pm
    /**
     *
     * IDEA 1) DFS (pre-order ???)
     *   - pre-order: can quit earlier
     *
     */
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        // edge

        // ??
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        getNodeSqe(root1, list1);
        getNodeSqe(root2, list2);

        return list1.equals(list2);
    }


    // ???
    private void getNodeSqe(TreeNode root, List<Integer> list){
        // edge
        if(root == null){
            return;
        }
        if(root.left == null && root.right == null){
            list.add(root.val);
            return;
        }
        getNodeSqe(root.left, list);
        getNodeSqe(root.right, list);
    }



    // LC 892
    // 7.56 - 8.06 am
    /**
     *  -> Return the total surface
     *  area of the resulting shapes.
     *
     *
     *  - Each value v = grid[i][j]
     *     - a tower of v cubes placed on top of cell (i, j).
     *
     *
     *  ----------------
     *
     *   IDEA 1) BRUTE FORCE ????
     *
     *
     *   ----------------
     *
     *
     */
    //  IDEA 1) BRUTE FORCE ????
    public int surfaceArea(int[][] grid) {
        // edge
        int l = grid.length;
        int w = grid[0].length;

        int res = 0;

        int upSurface = 0;
        int zeroCnt = 0;
        // ???
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] != 0){
                    upSurface += 1;
                }else{
                    zeroCnt += 1; // ???
                }
            }
        }

        // ???
        int xSurface = 0;
        for(int x = 0; x < w; x++){
            if(grid[0][x] != 0){
                xSurface += 1;
            }
        }

        int ySurface = 0;
        for(int y = 0; y < l; y++){
            if(grid[y][0] != 0){
                ySurface += 1;
            }
        }


        // 6 faces
        res = 2 * upSurface + 2 * xSurface + 2 * ySurface + zeroCnt * 4;

        return res;
    }


    // LC 463
    // 8.26 - 36 am
    /**
     *
     * -> Determine the `perimeter`
     *    of the island.
     *
     *   grid[i][j] = 1 -> island
     *   grid[i][j] = 0 -> water
     *
     *  ----------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DFS ???
     *
     */
    // IDEA 1) BRUTE FORCE
    // time:  O(N * M)
    // space: O(N * M)
    public int islandPerimeter(int[][] grid) {
        // edge

        int l = grid.length;
        int w = grid[0].length;

        int res = 0;
        // ???
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){

                // check how many surrounded island
//                int surroundedCnt = getSurroundedIsland(grid, x, y);
//                res += (4 - surroundedCnt);

                if(grid[y][x] == 1){
                    res += (4 - getSurroundedIsland(grid, x, y));
                }
            }
        }


        return res;
    }


    private int getSurroundedIsland(int[][] grid, int x, int y){
        int l = grid.length;
        int w = grid[0].length;

        int cnt = 0;
        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
        // ???
        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                if(grid[y_][x_] == 1){
                    cnt += 1;
                }
            }
        }

        return cnt;
    }


    // LC 200
    // 9.35 - 45 am
    /**
     *  ->  return the number of `islands`.
     *
     *  -----------
     *
     *  IDEA 1) DFS
     *
     */
    //  IDEA 1) DFS
    // time: O(N * M) // ???
    // space: O(1)
    public int numIslands(char[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        int island = 0;
        //boolean[][] visited = new boolean[l][w];

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == '1'){
                    isLandCntHelper(grid, x, y);
                    island += 1;
                }
            }
        }


        return island;
    }


    private void isLandCntHelper(char[][] grid, int x, int y){
        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        int l = grid.length;
        int w = grid[0].length;


        // mark as `visited`
        grid[y][x] = '#';

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                if(grid[y_][x_] == '1' && grid[y_][x_] != '#'){
                    //cnt += 1;
                    isLandCntHelper(grid, x_, y_);
                }
            }
        }
    }



    // LC 695
    // 9.52 - 10.02 am
    /**
     *  -> Return the maximum area of an island in grid.
     *      If there is NO island, return 0.
     *
     *  - grid: m x n
     *  - island: 1 cells
     *
     *
     *  -------------
     *
     *  IDEA 1) DFS
     *   -> find all island size
     *   -> get max
     *
     *  IDEA 2) BFS
     *
     *
     *  -------------
     *
     */
    // IDEA 1) DFS
    // time:  O(N * M)
    // space: O(N * M)
    public int maxAreaOfIsland(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        int maxSize = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 1){
//                    getIslandSize(grid, x, y);
//                    island += 1;
                    maxSize = Math.max(maxSize,
                            getIslandSize(grid, x, y));
                }
            }
        }

        return maxSize;
    }

    private int getIslandSize(int[][] grid, int x, int y){
        //int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        int l = grid.length;
        int w = grid[0].length;

        // NOTE !!! we validate cell first (in this version of dfs)
        if(x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1){
            return 0;
        }

        // mark as `visited`
        grid[y][x] = -1;

        // ???
        return 1 + getIslandSize(grid, x + 1, y) +
                getIslandSize(grid, x - 1, y) +
                getIslandSize(grid, x, y + 1) +
                getIslandSize(grid, x, y - 1);
    }


    // LC 1254
    // 10.18 - 28 am
    /**
     * -> Return the number of closed islands.
     *
     *  NOTE:
     *    - An island is a maximal 4-directionally
     *    connected group of 0s and a closed island is an island
     *    totally (all left, top, right, bottom) surrounded by 1s.
     *
     *    -> island now is `0 cells`
     *
     *
     *
     * --------------
     *
     *  IDEA 1) 2 PASS DFS
     *
     *   - 1. mark `all at corner 0` as -1,
     *        so we are NOT visting them in the future
     *     2. get all `surrounded 0`
     *     3. dfs visit above and cnt them
     *
     *  IDEA 2) OTHER GRAPH ALGO ???
     *
     *
     * --------------
     *
     */
    // IDEA 1) 2 PASS DFS
    // time: O(N * M)
    // space:  O(N * M)
    public int closedIsland(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        int isLand = 0;

        // 1) first pass
        // visit x = 0 dir
        for(int x = 0; x < w; x++){
            if(grid[0][x] == 0){
                updateCell(grid, x, 0, -1);
            }
            if(grid[l-1][x] == 0){
                updateCell(grid, x, l-1, -1);
            }
        }

        // visit y = 0 dir
        for(int y = 0; y < l; y++){
            if(grid[y][0] == 0){
                updateCell(grid, 0, y, -1);
            }
            if(grid[y][w-1] == 0){
                updateCell(grid, w-1, y, -1);
            }
        }

        // 2) 2nd pass
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 0){
                    updateCell(grid, x, y, 100);
                    isLand += 1;
                }
            }
        }

        return isLand;
    }

    private void updateCell(int[][] grid, int x, int y, int newColor){

        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        int l = grid.length;
        int w = grid[0].length;

        // mark as `visited`
        grid[y][x] = newColor; // ??   '#';

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                if(grid[y_][x_] == 0 && grid[y_][x_] != newColor){
                    //cnt += 1;
                    updateCell(grid, x_, y_, newColor);
                }
            }
        }

    }


    // LC 988
    // 9.49 am  - 59 am
    /**
     * -> Return the `lexicographically smallest `
     *    string that` starts at a leaf` of this tree
     *    and ends at the root.
     *
     *    - root (binary tree)
     *      - node val in [0,25]
     *      - 'a' -> 'z'
     *      - 0 -> 25
     *
     *    - NOTE:
     *      -  any `shorter` prefix of
     *         a string is lexicographically `smaller`.
     *
     *          - 'ab' < 'aba'
     *
     *      - leaf: NO children
     *
     *
     *
     *
     *  -------------
     *
     *   IDEA 1) DFS
     *
     *    -> path,  pre order ???
     *       reverse
     *
     *   IDEA 2) BFS
     *
     *
     *  -------------
     *
     *
     */
    // IDEA 1) DFS - pre order ???
    // ???
    // time: O(N)
    // space: O(N)
    Map<String, Integer> pathMap2 = new HashMap<>();
    public String smallestFromLeaf(TreeNode root) {
        // edge

        // build path
        buildPath(root, "");

        // get path list
        Set<String> pathSet = pathMap2.keySet();
        List<String> paths = new ArrayList<>(); // ??
        for(String p: pathSet){
            paths.add(p);
        }

        // reverse
        paths.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // ?? the right way to do lexicographically sort ??
                // small -> big ??
                int diff = o1.compareTo(o2); /// ????
                return diff;
            }
        });

        // get smallest
        return !paths.isEmpty() ? paths.get(0) : "";
    }

    private void buildPath(TreeNode root, String path){
        // edge
        if(root == null){
            return;
        }
        // ???
        //sb.append(root.val);
        //String path = sb.toString();
        //path += root.val;
        //path.
        path += root.val;

        // ?? ONLY add to hashmap when reach `leave`
        if(root.left ==null && root.right == null){
            pathMap2.put(path, pathMap2.getOrDefault(path, 0) + 1);
            // early quit
            return;
        }

        buildPath(root.left, path);
        buildPath(root.right, path);

        // for `StringBuilder`,
        // do we need to do `backtrack` ??? (undo) ???
        //sb.delete(sb.length() - 2, sb.length() - 1); //??
    }


    // LC 669
    // 10.46 - 56 am
    /**
     *
     *  -> Return the root of the trimmed binary search tree.
     *  Note that the root may change
     *  depending on the given bounds.
     *
     *  trim the tree so that all its
     *  elements lies in `[low, high].`
     *
     *  should not change the relative structure
     *
     *
     *  ------------
     *
     *   IDEA 1) DFS + PREORDER ??? + LC 776 ????
     *
     *  ------------
     *
     */
    // IDEA 1) DFS + PRE- ORDER ??? + LC 776 ????
    // root -> left -> right ???
    // NOTE !!!
    // BST: binary search tree ( left <= root <= right )
    // time:   O(log N) // ?? BST ???
    // space:  O(N) (worst case ???)
    public TreeNode trimBST(TreeNode root, int low, int high) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            if(root.val <= high && root.val >= low){
                return null;
            }
            return root;
        }


        return trimHelper(root, null, low, high);
    }


    // ??? param are correct ???
    private TreeNode trimHelper(TreeNode root, TreeNode parent, int low, int high){
        // edge
        if(root == null){
            return null;
        }


        // case 1) root is in [low, high]
        if(root.val <= high && root.val >= low){
            //return null; // ???
            // ???
            TreeNode _left = trimHelper(root.left, root, low, high);
            TreeNode _right = trimHelper(root.right, root, low, high);

            // NOTE !!! re-connect ???
            parent.left = _left;
            parent.right = _right;
        }
        // case 2) root is NOT in [low, high]
        else{
            TreeNode _left = trimHelper(root.left, root, low, high);
            TreeNode _right = trimHelper(root.right, root, low, high);
            root.left = _left;
            root.right = _right;
        }

        return root;
    }




//    private TreeNode[] splitHelper3(TreeNode root, int low, int high){
//
//    }




    // LC 99
    // 11. 33 - 43 am
    /**
     *
     *  -> Recover the tree without
     *  changing its structure.
     *
     *   NOTE:
     *      -  values of exactly` two` nodes of
     *      the tree were `swapped by mistake.`
     *
     *      -  root of a binary search tree (BST)
     *
     *
     *   ------------
     *
     *   IDEA 1) DFS ???
     *
     *    ->
     *      1. find the first `wrong` node
     *      2. swap with `minimum` node ??
     *         -> right
     *         -> left
     *         ...
     *
     *      -> keep tracking min, max in recursion 9???
     *
     *
     *
     *  binary search tree (BST),
     *
     *  --------------
     *
     *
     *   IDEA 1) IN-ORDER ???
     *    left -> root -> right ??
     *
     *    -> reason:
     *
     *     An In-order traversal of a valid BST must be a strictly increasing sequence.
     *     If two nodes are swapped, you will see "drops" in that sequence.
     *    ---
     *
     *
     *
     *
     */
    // ???
    TreeNode node1;
    TreeNode node2;
    public void recoverTree(TreeNode root) {
        // edge ??
        if(root == null){
            return;
        }

        // get node1, node2 ??
        // so we can swap
        recoveryHelper(root, -1 * Integer.MAX_VALUE, Integer.MAX_VALUE);

        // ??? if NOTHING to swap
        if(node1 == null && node2 == null){
            return;
        }

        TreeNode cache = node1; //??
        // swap ??
        node1.val = node2.val;
        node2.val = cache.val;
    }

    private void recoveryHelper(TreeNode root, int min, int max){
        // ???
        if(root == null){
            return;
        }
        // ???
        if(root.val >= max || root.val <= min){
            if(node1 != null){
                node1 = root;
            }else{
                node2 = root;
            }
        }

        recoveryHelper(root.left, min, root.val);
        recoveryHelper(root.right, root.val, max);
    }








}
