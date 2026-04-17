package ws;

import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.SIPUSH;

import java.util.*;

public class Workspace25 {

    // LC 1905
    // 8.22 - 32 am
    /**
     *   -> Return the number of islands in
     *   grid2 that are considered sub-islands.
     *
     *
     *  ------------
     *
     *  IDEA 1) 2 PASS DFS / BFS
     *     - first pass: mark `invalid island` in grid2
     *                   e.g. island in grid2, but water in grid1
     *
     *     - second pass:  count and check remaining island in grid2,
     *                     if they are BOTH island in grid2 and grid1,
     *                     maintain the distinct island cnt
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     */
    // IDEA 1) 2 PASS DFS / BFS
    public int countSubIslands(int[][] grid1, int[][] grid2) {
        // edge

        int l = grid1.length;
        int w = grid1[0].length;

        // int subIsland = 0;

         for(int y = 0; y < l; y++){
             for(int x = 0; x < w; x++){
                 // ???
                 boolean[][] visited = new boolean[l][w];

                 if(grid2[y][x] == 1 && grid1[y][x] == 0){
                     dfsColor(grid1, grid2, visited, x, y, -1);
                 }
             }
         }

        int subIsland = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // ???
                boolean[][] visited = new boolean[l][w];

                if(grid2[y][x] == 1 && grid1[y][x] == 1){
                    dfsColor(grid1, grid2, visited, x, y, -2);
                    subIsland += 1;
                }
            }
        }


        return subIsland;
    }

    // ????
    private void dfsColor(int[][] grid1, int[][] grid2, boolean[][] visited, int x, int y, int newColor){
        // edge

        int l = grid1.length;
        int w = grid1[0].length;

        // need validate here ???

        // mark as new color !!!
//        grid2[y][x] = newColor; // ??
//        visited[y][x] = true;

        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && !visited[y_][x_] && grid2[y_][x_] == 1){
                // ???? do below (within for loop, or at beginning of this DFS fuc ??)
                grid2[y_][x_] = newColor; // ??
                visited[y_][x_] = true;

                dfsColor(grid1, grid2, visited, x_, y_, newColor);
            }
        }


    }

    // LC 827
    // 10.04 - 14 am
    /**
     *  -> Return the size of the
     *  largest island in grid after applying
     *  this operation.
     *
     *   NOTE:
     *      - You are allowed to change
     *        `at most `one` 0 to be 1.
     *
     *
     *
     *  ------------------
     *
     *   IDEA 1) DFS / BFS + brute force
     *
     *    -> find all `0`, and check one by one,
     *       get the max updated `1` area
     *
     *   IDEA 2) `reverse` way. DFS / BFS
     *
     *    -> get all `1` island area, and check there neighbor `0`
     *
     *
     *
     *  ------------------
     *
     *
     *
     */
    // IDEA: 2 PASS DFS
    List<List<Integer[]>> isLandList = new ArrayList<>(); // ???
    public int largestIsland(int[][] grid) {

        int l = grid.length;
        int w = grid[0].length;

        List<List<Integer>> collected = new ArrayList<>();
        boolean[][] visited = new boolean[l][w];

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 1){
                    List<Integer[]> tmp = new ArrayList<>();
                    dfsGetIslands(grid, x, y, visited, tmp);
                    // ???
                    isLandList.add(tmp);
                }
            }
        }


        int biggestArea = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 1){
                    biggestArea = Math.max(biggestArea,
                            dfsFlip(grid, x, y, 1));
                }
            }
        }



        return biggestArea;
    }

    private List<Integer[]> dfsGetIslands(int[][] grid, int x, int y, boolean[][] visited, List<Integer[]> tmp){

        int l = grid.length;
        int w = grid[0].length;
        int[][] moves = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && !visited[y_][x_] && grid[y_][x_] == 1){

                // ???? do below (within for loop, or at beginning of this DFS fuc ??)
                visited[y][x] = true;
                tmp.add(new Integer[]{x,y});

                dfsGetIslands(grid, x_, y_, visited, tmp);
            }
        }

        return tmp;

    }

    private int dfsFlip(int[][] grid, int x, int y, int remainingOp){

        int l = grid.length;
        int w = grid[0].length;
        int[][] moves = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        return 0;
    }



    // LC 1492
    // 7.31 - 41 am
    /**
     *
     *   ->  return the kth factor in this
     *       or return -1 if n has less than k factors
     *
     *
     *   -  a list of all factors of n
     *      sorted in `ascending` order
     *
     *
     *   n, k: positive int
     *
     *   factor of n
     *      -> an integer i where n % i == 0.
     *
     *  --------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) MATH
     */
    public int kthFactor(int n, int k) {
        // edge
        if(n <= 0){
            return -1;
        }

        for(int i = 1; i < n + 1; i++){
            if(n % i == 0){
                k -= 1;
                // ???
                if(k == 0){
                    return i;
                }
            }
        }

        return -1; // ???
    }


    // LC 1167
    // 8.04 - 14 am
    /**
     * -> Return the minimum cost of
     * connecting all the given sticks into
     * one stick in this way.
     *
     * -------------
     *
     *   ex 1)
     *
     *    sticks = [2,4,3]
     *
     *    -> pay = (2+3) = 5, [5,3]
     *    -> pay = 5 + (5+3) = 13 ??? [8]
     *
     *
     *   ex 2)
     *    sticks = [1,8,3,5]
     *
     *    -> p = 1+3 =4    [ 4,8,5]
     *    -> p = 4 + (5+4) = 13,    [9,8]
     *    -> p  = 13 + 17 = 30, [17]
     *
     *
     */
    // IDEA 1) SMALL PQ
    public int connectSticks(int[] sticks) {
        return 0;
    }






}
