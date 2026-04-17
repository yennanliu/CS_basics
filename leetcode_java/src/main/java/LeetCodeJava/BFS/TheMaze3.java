package LeetCodeJava.BFS;

// https://leetcode.ca/2017-04-12-499-The-Maze-III/

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *  499. The Maze III
 * There is a ball in a maze with empty spaces and walls. The ball can go through empty spaces by rolling up (u), down (d), left (l) or right (r), but it won't stop rolling until hitting a wall. When the ball stops, it could choose the next direction. There is also a hole in this maze. The ball will drop into the hole if it rolls on to the hole.
 *
 * Given the ball position, the hole position and the maze, find out how the ball could drop into the hole by moving the shortest distance. The distance is defined by the number of empty spaces traveled by the ball from the start position (excluded) to the hole (included). Output the moving directions by using 'u', 'd', 'l' and 'r'. Since there could be several different shortest ways, you should output the lexicographically smallest way. If the ball cannot reach the hole, output "impossible".
 *
 * The maze is represented by a binary 2D array. 1 means the wall and 0 means the empty space. You may assume that the borders of the maze are all walls. The ball and the hole coordinates are represented by row and column indexes.
 *
 *
 *
 * Example 1:
 *
 * Input 1: a maze represented by a 2D array
 *
 * 0 0 0 0 0
 * 1 1 0 0 1
 * 0 0 0 0 0
 * 0 1 0 0 1
 * 0 1 0 0 0
 *
 * Input 2: ball coordinate (rowBall, colBall) = (4, 3)
 * Input 3: hole coordinate (rowHole, colHole) = (0, 1)
 *
 * Output: "lul"
 *
 * Explanation: There are two shortest ways for the ball to drop into the hole.
 * The first way is left -> up -> left, represented by "lul".
 * The second way is up -> left, represented by 'ul'.
 * Both ways have shortest distance 6, but the first way is lexicographically smaller because 'l' < 'u'. So the output is "lul".
 *
 * Example 2:
 *
 * Input 1: a maze represented by a 2D array
 *
 * 0 0 0 0 0
 * 1 1 0 0 1
 * 0 0 0 0 0
 * 0 1 0 0 1
 * 0 1 0 0 0
 *
 * Input 2: ball coordinate (rowBall, colBall) = (4, 3)
 * Input 3: hole coordinate (rowHole, colHole) = (3, 0)
 *
 * Output: "impossible"
 *
 * Explanation: The ball cannot reach the hole.
 *
 *
 *
 * Note:
 *
 * There is only one ball and one hole in the maze.
 * Both the ball and hole exist on an empty space, and they will not be at the same position initially.
 * The given maze does not contain border (like the red rectangle in the example pictures), but you could assume the border of the maze are all walls.
 * The maze contains at least 2 empty spaces, and the width and the height of the maze won't exceed 30.
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Google
 * Problem Solution
 */
public class TheMaze3 {

    // V0
//    public String findShortestWay(int[][] maze, int[] ball, int[] hole) {
//
//    }

    // V0-1
    // IDEA: Dijkstra (GPT)
    // TODO: validate
    class State {
        int x, y, dist;
        String path;

        State(int x, int y, int dist, String path) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.path = path;
        }
    }

    public String findShortestWay_0_1(int[][] maze, int[] ball, int[] hole) {
        int m = maze.length, n = maze[0].length;

        int[][] dirs = {{0, -1}, {-1, 0}, {1, 0}, {0, 1}};
        char[] moves = {'l', 'u', 'd', 'r'};

        // distance + lexicographic ordering
        PriorityQueue<State> pq = new PriorityQueue<>(
                (a, b) -> a.dist == b.dist ? a.path.compareTo(b.path) : a.dist - b.dist
        );

        int[][] dist = new int[m][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        pq.offer(new State(ball[0], ball[1], 0, ""));
        dist[ball[0]][ball[1]] = 0;

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (cur.x == hole[0] && cur.y == hole[1]) {
                return cur.path;
            }

            for (int i = 0; i < 4; i++) {
                int x = cur.x;
                int y = cur.y;
                int steps = 0;

                // roll the ball
                while (x + dirs[i][0] >= 0 && x + dirs[i][0] < m &&
                        y + dirs[i][1] >= 0 && y + dirs[i][1] < n &&
                        maze[x + dirs[i][0]][y + dirs[i][1]] == 0) {

                    x += dirs[i][0];
                    y += dirs[i][1];
                    steps++;

                    if (x == hole[0] && y == hole[1]) break;
                }

                String newPath = cur.path + moves[i];

                if (cur.dist + steps < dist[x][y] ||
                        (cur.dist + steps == dist[x][y] &&
                                newPath.compareTo(newPath) < 0)) {

                    dist[x][y] = cur.dist + steps;
                    pq.offer(new State(x, y, dist[x][y], newPath));
                }
            }
        }

        return "impossible";
    }

    // V0-2
    // IDEA: Dijkstra (GEMINI)
    // TODO: validate
    class Node implements Comparable<Node> {
        int r, c, dist;
        String path;

        Node(int r, int c, int dist, String path) {
            this.r = r;
            this.c = c;
            this.dist = dist;
            this.path = path;
        }

        @Override
        public int compareTo(Node other) {
            if (this.dist != other.dist) return this.dist - other.dist;
            return this.path.compareTo(other.path);
        }
    }

    public String findShortestWay_0_2(int[][] maze, int[] ball, int[] hole) {
        int rows = maze.length, cols = maze[0].length;
        Node[][] dists = new Node[rows][cols];

        // Initialize distances with "Infinity" nodes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dists[i][j] = new Node(i, j, Integer.MAX_VALUE, "");
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(ball[0], ball[1], 0, ""));

        int[][] dirs = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}};
        String[] dirChars = {"d", "l", "r", "u"};

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            // If we found a better way to this point already, skip
            if (curr.compareTo(dists[curr.r][curr.c]) >= 0 && dists[curr.r][curr.c].dist != Integer.MAX_VALUE) continue;
            dists[curr.r][curr.c] = curr;

            if (curr.r == hole[0] && curr.c == hole[1]) return curr.path;

            for (int i = 0; i < 4; i++) {
                int nr = curr.r, nc = curr.c, d = curr.dist;

                // Roll the ball
                while (nr + dirs[i][0] >= 0 && nr + dirs[i][0] < rows &&
                        nc + dirs[i][1] >= 0 && nc + dirs[i][1] < cols &&
                        maze[nr + dirs[i][0]][nc + dirs[i][1]] == 0) {
                    nr += dirs[i][0];
                    nc += dirs[i][1];
                    d++;
                    // Stop rolling if we fall into the hole
                    if (nr == hole[0] && nc == hole[1]) break;
                }

                Node next = new Node(nr, nc, d, curr.path + dirChars[i]);
                if (next.compareTo(dists[nr][nc]) < 0) {
                    pq.offer(next);
                }
            }
        }

        return "impossible";
    }



    // V1
    // https://leetcode.ca/2017-04-12-499-The-Maze-III/
    public String findShortestWay_1(int[][] maze, int[] ball, int[] hole) {
        int m = maze.length;
        int n = maze[0].length;
        int r = ball[0], c = ball[1];
        int rh = hole[0], ch = hole[1];
        Deque<int[]> q = new LinkedList<>();
        q.offer(new int[] {r, c});
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; ++i) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[r][c] = 0;
        String[][] path = new String[m][n];
        path[r][c] = "";
        int[][] dirs = { {-1, 0, 'u'}, {1, 0, 'd'}, {0, -1, 'l'}, {0, 1, 'r'} };
        while (!q.isEmpty()) {
            int[] p = q.poll();
            int i = p[0], j = p[1];
            for (int[] dir : dirs) {
                int a = dir[0], b = dir[1];
                String d = String.valueOf((char) (dir[2]));
                int x = i, y = j;
                int step = dist[i][j];
                while (x + a >= 0 && x + a < m && y + b >= 0 && y + b < n && maze[x + a][y + b] == 0
                        && (x != rh || y != ch)) {
                    x += a;
                    y += b;
                    ++step;
                }
                if (dist[x][y] > step
                        || (dist[x][y] == step && (path[i][j] + d).compareTo(path[x][y]) < 0)) {
                    dist[x][y] = step;
                    path[x][y] = path[i][j] + d;
                    if (x != rh || y != ch) {
                        q.offer(new int[] {x, y});
                    }
                }
            }
        }
        return path[rh][ch] == null ? "impossible" : path[rh][ch];
    }



    // V2




}
