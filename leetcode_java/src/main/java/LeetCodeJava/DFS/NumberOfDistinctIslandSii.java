package LeetCodeJava.DFS;

import java.util.*;

// V1
// http://www.noteanddata.com/leetcode-711-Number-of-Distinct-Islands-II-java-solution-note.html

// V1'
// http://www.noteanddata.com/leetcode-711-Number-of-Distinct-Islands-II-java-solution-note.html
class NumberOfDistinctIslandSii {
    public int numDistinctIslands2(int[][] grid) {
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        Set<String> set = new HashSet<String>();
        int count = 0;
        for(int i = 0; i < grid.length; ++i) {
            for(int j = 0; j < grid[i].length; ++j) {
                if(grid[i][j] == 1 && !visited[i][j]) {
                    List<List<int[]>> allList = new ArrayList<List<int[]>>();
                    for(int t = 0; t < 8; ++t) {
                        allList.add(new ArrayList<int[]>());
                    }

                    dfs(grid, i, j, visited, i, j, allList);

                    boolean exist = false;
                    List<String> nset = new ArrayList<String>();
                    for(List<int[]> list : allList) {
                        if(list.size() == 0) continue;

                        // sort after finishing all operations
                        Collections.sort(list, new Comparator<int[]>() {
                            public int compare(int[] a, int[] b) {
                                return (a[0] != b[0]) ? (a[0] - b[0]) : (a[1] - b[1]);
                            }
                        });

                        String s = encode(list);

                        if(set.contains(s)) {
                            exist = true;
                        }
                        nset.add(s);
                    }

                    if(!exist) {
                        count++;
                        set.addAll(nset);
                    }
                }
            }
        }
        return count;
    }

    String encode(List<int[]> list) {
        int ox = list.get(0)[0];
        int oy = list.get(0)[1];

        StringBuilder sb = new StringBuilder();
        sb.append("0,0;");

        for(int k = 1; k < list.size(); ++k) {
            int dx = list.get(k)[0] - ox;
            int dy = list.get(k)[1] - oy;
            sb.append(dx).append(",").append(dy).append(";");
        }

        String s = sb.toString();
        return s;
    }

    void addAll(List<List<int[]>> allList, int dx, int dy) {
        allList.get(0).add(new int[]{dx, dy});

        // rotation
        allList.get(1).add(new int[]{-dy, dx});
        allList.get(2).add(new int[]{-dx, -dy});
        allList.get(3).add(new int[]{dy, -dx});

        // mirror
        allList.get(4).add(new int[]{-dx, dy});
        allList.get(5).add(new int[]{dx, -dy});

        allList.get(6).add(new int[]{dy, dx});
        allList.get(7).add(new int[]{dy, -dx});

    }

    void dfs(int[][] grid, int x, int y, boolean[][] visited, int ox, int oy, List<List<int[]>> allList) {
        if(x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) return;
        if(visited[x][y] || grid[x][y] == 0) return;
        visited[x][y] = true;

        addAll(allList, x-ox, y-oy);

        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for(int i  = 0; i < dirs.length; ++i) {
            int nextx = x + dirs[i][0];
            int nexty = y + dirs[i][1];
            dfs(grid, nextx, nexty, visited, ox, oy, allList);
        }
    }
}