package LeetCodeJava.Sort;

// https://leetcode.com/problems/amount-of-new-area-painted-each-day/

/**
 *  2158. Amount of New Area Painted Each Day
 *  Hard
 *
 *  There is a long and thin painting that can be represented by a number line.
 *  You are given a 0-indexed 2D integer array paint of length n, where
 *  paint[i] = [start_i, end_i]. This means that on the ith day you need to paint
 *  the area between start_i and end_i.
 *
 *  Painting the same area multiple times will create an uneven painting so you
 *  only want to paint each area of the painting at most once.
 *
 *  Return an integer array worklog of length n, where worklog[i] is the amount of
 *  new area that you painted on the ith day.
 *
 *  Example 1:
 *    Input: paint = [[1,4],[4,7],[5,8]]
 *    Output: [3,3,1]
 *    Explanation:
 *      On day 0, paint everything between 1 and 4 -> 4 - 1 = 3.
 *      On day 1, paint everything between 4 and 7 -> 7 - 4 = 3.
 *      On day 2, only 7..8 is new -> 1.
 *
 *  Example 2:
 *    Input: paint = [[1,5],[2,4]]
 *    Output: [4,0]
 *    Explanation: on day 1 everything between 2 and 4 was already painted.
 *
 *  Constraints:
 *    1 <= paint.length <= 10^5
 *    paint[i].length == 2
 *    0 <= start_i < end_i <= 5 * 10^4
 */
public class AmountOfNewAreaPaintedEachDay {

    // V0
    // IDEA: UNION-FIND AS A "JUMP TO THE NEXT UNPAINTED UNIT" POINTER
    //       treat the line as unit cells [x, x+1). parent[x] answers "the first
    //       unpainted cell at or after x", initially parent[x] = x.
    //       for a day covering [start, end) hop with find(start); each landing
    //       still < end is one fresh unit, and painting it points that cell at
    //       the NEXT one (parent[x] = x + 1) so it is skipped forever after.
    //       every cell is painted at most once over the whole run.
    /**
     * time = O((n + RANGE) * alpha)
     * space = O(RANGE)
     */
    public int[] amountPainted(int[][] paint) {
        final int LIMIT = 50001;              // end_i <= 5 * 10^4
        int[] parent = new int[LIMIT + 2];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        int[] res = new int[paint.length];
        for (int d = 0; d < paint.length; d++) {
            int start = paint[d][0];
            int end = paint[d][1];
            int painted = 0;
            int x = find(parent, start);
            while (x < end) {
                painted++;
                parent[x] = x + 1;
                x = find(parent, x + 1);
            }
            res[d] = painted;
        }
        return res;
    }

    // iterative find + path compression (a recursive one could blow the stack)
    private int find(int[] parent, int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int nxt = parent[x];
            parent[x] = root;
            x = nxt;
        }
        return root;
    }
}
