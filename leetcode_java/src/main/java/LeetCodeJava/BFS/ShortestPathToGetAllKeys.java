package LeetCodeJava.BFS;

import java.awt.Point;
import java.util.*;

// https://leetcode.com/problems/shortest-path-to-get-all-keys/description/
/**
 * 864. Shortest Path to Get All Keys
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given an m x n grid grid where:
 *
 * '.' is an empty cell.
 * '#' is a wall.
 * '@' is the starting point.
 * Lowercase letters represent keys.
 * Uppercase letters represent locks.
 * You start at the starting point and one move consists of walking one space in one of the four cardinal directions. You cannot walk outside the grid, or walk into a wall.
 *
 * If you walk over a key, you can pick it up and you cannot walk over a lock unless you have its corresponding key.
 *
 * For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English alphabet in the grid. This means that there is exactly one key for each lock, and one lock for each key; and also that the letters used to represent the keys and locks were chosen in the same order as the English alphabet.
 *
 * Return the lowest number of moves to acquire all keys. If it is impossible, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = ["@.a..","###.#","b.A.B"]
 * Output: 8
 * Explanation: Note that the goal is to obtain all the keys not to open all the locks.
 * Example 2:
 *
 *
 * Input: grid = ["@..aA","..B#.","....b"]
 * Output: 6
 * Example 3:
 *
 *
 * Input: grid = ["@Aa"]
 * Output: -1
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 30
 * grid[i][j] is either an English letter, '.', '#', or '@'.
 * There is exactly one '@' in the grid.
 * The number of keys in the grid is in the range [1, 6].
 * Each key in the grid is unique.
 * Each key in the grid has a matching lock.
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 91,352/168.4K
 */
public class ShortestPathToGetAllKeys {

    // V0
//    public int shortestPathAllKeys(String[] grid) {
//
//    }

    // V1-1
    // IDEA: Brute Force + Permutations
    // https://leetcode.com/problems/shortest-path-to-get-all-keys/editorial/
    int INF = Integer.MAX_VALUE;
    String[] grid;
    int R, C;
    Map<Character, Point> location;
    int[] dr = new int[] { -1, 0, 1, 0 };
    int[] dc = new int[] { 0, -1, 0, 1 };

    public int shortestPathAllKeys_1_1(String[] grid) {
        this.grid = grid;
        R = grid.length;
        C = grid[0].length();

        //location['a'] = the coordinates of 'a' on the grid, etc.
        location = new HashMap();
        for (int r = 0; r < R; ++r)
            for (int c = 0; c < C; ++c) {
                char v = grid[r].charAt(c);
                if (v != '.' && v != '#')
                    location.put(v, new Point(r, c));
            }

        int ans = INF;
        int num_keys = location.size() / 2;
        String[] alphabet = new String[num_keys];
        for (int i = 0; i < num_keys; ++i)
            alphabet[i] = Character.toString((char) ('a' + i));
        //alphabet = ["a", "b", "c"], if there were 3 keys

        search: for (String cand : permutations(alphabet, 0, num_keys)) {
            //bns : the built candidate answer, consisting of the sum
            //of distances of the segments from '@' to cand[0] to cand[1] etc.
            int bns = 0;
            for (int i = 0; i < num_keys; ++i) {
                char source = i > 0 ? cand.charAt(i - 1) : '@';
                char target = cand.charAt(i);

                //keymask : an integer with the 0-th bit set if we picked up
                // key 'a', the 1-th bit set if we picked up key 'b', etc.
                int keymask = 0;
                for (int j = 0; j < i; ++j)
                    keymask |= 1 << (cand.charAt(j) - 'a');
                int d = bfs(source, target, keymask);
                if (d == INF)
                    continue search;
                bns += d;
                if (bns >= ans)
                    continue search;
            }
            ans = bns;
        }

        return ans < INF ? ans : -1;
    }

    public int bfs(char source, char target, int keymask) {
        int sr = location.get(source).x;
        int sc = location.get(source).y;
        int tr = location.get(target).x;
        int tc = location.get(target).y;
        boolean[][] seen = new boolean[R][C];
        seen[sr][sc] = true;
        int curDepth = 0;
        Queue<Point> queue = new LinkedList();
        queue.offer(new Point(sr, sc));
        queue.offer(null);

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p == null) {
                curDepth++;
                if (!queue.isEmpty())
                    queue.offer(null);
                continue;
            }
            int r = p.x, c = p.y;
            if (r == tr && c == tc)
                return curDepth;
            for (int i = 0; i < 4; ++i) {
                int cr = r + dr[i];
                int cc = c + dc[i];
                if (0 <= cr && cr < R && 0 <= cc && cc < C && !seen[cr][cc]) {
                    char cur = grid[cr].charAt(cc);
                    if (cur != '#') {
                        if (Character.isUpperCase(cur) && (((1 << (cur - 'A')) & keymask) <= 0))
                            continue; // at lock and don't have key

                        queue.offer(new Point(cr, cc));
                        seen[cr][cc] = true;
                    }
                }
            }
        }

        return INF;
    }

    public List<String> permutations(String[] alphabet, int used, int size) {
        List<String> ans = new ArrayList();
        if (size == 0) {
            ans.add(new String(""));
            return ans;
        }

        for (int b = 0; b < alphabet.length; ++b)
            if (((used >> b) & 1) == 0)
                for (String rest : permutations(alphabet, used | (1 << b), size - 1))
                    ans.add(alphabet[b] + rest);
        return ans;
    }


    // V1-2
    // IDEA: Points of Interest + Dijkstra
    // https://leetcode.com/problems/shortest-path-to-get-all-keys/editorial/
//    int INF = Integer.MAX_VALUE;
//    String[] grid;
//    int R, C;
//    Map<Character, Point> location;
//    int[] dr = new int[] { -1, 0, 1, 0 };
//    int[] dc = new int[] { 0, -1, 0, 1 };

    public int shortestPathAllKeys_1_2(String[] grid) {
        this.grid = grid;
        R = grid.length;
        C = grid[0].length();

        //location : the points of interest
        location = new HashMap();
        for (int r = 0; r < R; ++r)
            for (int c = 0; c < C; ++c) {
                char v = grid[r].charAt(c);
                if (v != '.' && v != '#')
                    location.put(v, new Point(r, c));
            }

        int targetState = (1 << (location.size() / 2)) - 1;
        Map<Character, Map<Character, Integer>> dists = new HashMap();
        for (char place : location.keySet())
            dists.put(place, bfsFrom(place));

        //Dijkstra
        PriorityQueue<ANode> pq = new PriorityQueue<ANode>((a, b) -> Integer.compare(a.dist, b.dist));
        pq.offer(new ANode(new Node('@', 0), 0));
        Map<Node, Integer> finalDist = new HashMap();
        finalDist.put(new Node('@', 0), 0);

        while (!pq.isEmpty()) {
            ANode anode = pq.poll();
            Node node = anode.node;
            int d = anode.dist;
            if (finalDist.getOrDefault(node, INF) < d)
                continue;
            if (node.state == targetState)
                return d;

            for (char destination : dists.get(node.place).keySet()) {
                int d2 = dists.get(node.place).get(destination);
                int state2 = node.state;
                if (Character.isLowerCase(destination)) //key
                    state2 |= (1 << (destination - 'a'));
                if (Character.isUpperCase(destination)) //lock
                    if ((node.state & (1 << (destination - 'A'))) == 0) // no key
                        continue;

                if (d + d2 < finalDist.getOrDefault(new Node(destination, state2), INF)) {
                    finalDist.put(new Node(destination, state2), d + d2);
                    pq.offer(new ANode(new Node(destination, state2), d + d2));
                }
            }
        }

        return -1;
    }

    public Map<Character, Integer> bfsFrom(char source) {
        int sr = location.get(source).x;
        int sc = location.get(source).y;
        boolean[][] seen = new boolean[R][C];
        seen[sr][sc] = true;
        int curDepth = 0;
        Queue<Point> queue = new LinkedList();
        queue.offer(new Point(sr, sc));
        queue.offer(null);
        Map<Character, Integer> dist = new HashMap();

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p == null) {
                curDepth++;
                if (!queue.isEmpty())
                    queue.offer(null);
                continue;
            }

            int r = p.x, c = p.y;
            if (grid[r].charAt(c) != source && grid[r].charAt(c) != '.') {
                dist.put(grid[r].charAt(c), curDepth);
                continue; // Stop walking from here if we reach a point of interest
            }

            for (int i = 0; i < 4; ++i) {
                int cr = r + dr[i];
                int cc = c + dc[i];
                if (0 <= cr && cr < R && 0 <= cc && cc < C && !seen[cr][cc]) {
                    if (grid[cr].charAt(cc) != '#') {
                        queue.offer(new Point(cr, cc));
                        seen[cr][cc] = true;
                    }
                }
            }
        }

        return dist;
    }

    /** NOTE !! custom class */
    // ANode: Annotated Node
    class ANode {
        Node node;
        int dist;

        ANode(Node n, int d) {
            node = n;
            dist = d;
        }
    }

    class Node {
        char place;
        int state;

        Node(char p, int s) {
            place = p;
            state = s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Node))
                return false;
            Node other = (Node) o;
            return (place == other.place && state == other.state);
        }

        @Override
        public int hashCode() {
            return 256 * state + place;
        }
    }


    // V2
    // https://www.youtube.com/watch?v=A-8ziQc_4pk


    // V3



}
