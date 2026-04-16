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







}
