package LeetCodeJava.Design;

// https://leetcode.com/problems/booking-concert-tickets-in-groups/

/**
 *  2286. Booking Concert Tickets in Groups
 *  Hard
 *
 *  A concert hall has n rows numbered from 0 to n - 1, each with m seats, numbered
 *  from 0 to m - 1. You need to design a ticketing system that can allocate seats in
 *  the following cases:
 *
 *   - If a group of k spectators can sit together in a row.
 *   - If every member of a group of k spectators can get a seat. They may or may not
 *     sit together.
 *
 *  Note that the spectators are very picky. Hence:
 *   - They will book seats only if each member of their group can get a seat with row
 *     number less than or equal to maxRow. maxRow can vary from group to group.
 *   - In case there are multiple rows to choose from, the row with the smallest number
 *     is chosen. If there are multiple seats to choose in the same row, the seat with
 *     the smallest number is chosen.
 *
 *  Implement the BookMyShow class:
 *
 *   - BookMyShow(int n, int m) Initializes the object with n as number of rows and m
 *     as number of seats per row.
 *   - int[] gather(int k, int maxRow) Returns an array of length 2 denoting the row and
 *     seat number (respectively) of the first seat being allocated to the k members of
 *     the group, who must sit together. In other words, it returns the smallest possible
 *     r and c such that all [c, c + k - 1] seats are valid and empty in row r, and
 *     r <= maxRow. Returns [] in case it is not possible to allocate seats to the group.
 *   - boolean scatter(int k, int maxRow) Returns true if all k members of the group can
 *     be allocated seats in rows 0 to maxRow, who may or may not sit together. If the
 *     seats can be allocated, it allocates k seats to the group with the smallest row
 *     numbers, and the smallest possible seat numbers in each row. Otherwise, returns false.
 *
 *  Example 1:
 *    Input
 *      ["BookMyShow", "gather", "gather", "scatter", "scatter"]
 *      [[2, 5], [4, 0], [2, 0], [5, 1], [5, 1]]
 *    Output
 *      [null, [0, 0], [], true, false]
 *    Explanation
 *      BookMyShow bms = new BookMyShow(2, 5); // 2 rows with 5 seats each
 *      bms.gather(4, 0);  // return [0, 0], the group books seats [0, 3] of row 0
 *      bms.gather(2, 0);  // return [], only 1 seat left in row 0
 *      bms.scatter(5, 1); // return true, seat 4 of row 0 and seats [0, 3] of row 1
 *      bms.scatter(5, 1); // return false, only one seat left in the hall
 *
 *  Constraints:
 *    1 <= n <= 5 * 10^4
 *    1 <= m, k <= 10^9
 *    0 <= maxRow <= n - 1
 *    At most 5 * 10^4 calls in total will be made to gather and scatter.
 */
public class BookingConcertTicketsInGroups {

    // V0
    // IDEA: SEGMENT TREE HOLDING (MAX FREE SEATS, TOTAL FREE SEATS) PER ROW
    //
    //   the two operations need two different aggregates:
    //     gather  -> the LEFTMOST row in [0, maxRow] whose free count is >= k,
    //                which a MAX-tree can find by descending left-first
    //     scatter -> the TOTAL free seats in [0, maxRow] to decide feasibility,
    //                which a SUM-tree answers
    //   so each node keeps both.
    //
    //   scatter then fills rows greedily from the front. the crucial detail is the
    //   persistent pointer `first`: rows before it are completely full, and it only
    //   ever moves FORWARD, so the total filling work across ALL calls is O(n)
    //   rather than O(n) per call.
    //
    //   seats in a row are always taken from the left, so a row with `free`
    //   remaining has its next free seat at column m - free.
    /**
     * time = O(log N) per gather, amortised O(log N) per scatter
     * space = O(N)
     */
    private final int n;
    private final int m;
    private final int[] free;
    private final int[] maxTree;
    private final long[] sumTree;
    private int first = 0;   // first row that still has a free seat

    public BookingConcertTicketsInGroups(int n, int m) {
        this.n = n;
        this.m = m;
        this.free = new int[n];
        for (int i = 0; i < n; i++) {
            this.free[i] = m;
        }
        this.maxTree = new int[4 * n];
        this.sumTree = new long[4 * n];
        build(1, 0, n - 1);
    }

    private void build(int node, int lo, int hi) {
        if (lo == hi) {
            this.maxTree[node] = this.m;
            this.sumTree[node] = this.m;
            return;
        }
        int mid = (lo + hi) / 2;
        build(2 * node, lo, mid);
        build(2 * node + 1, mid + 1, hi);
        pull(node);
    }

    private void pull(int node) {
        this.maxTree[node] = Math.max(this.maxTree[2 * node], this.maxTree[2 * node + 1]);
        this.sumTree[node] = this.sumTree[2 * node] + this.sumTree[2 * node + 1];
    }

    private void update(int node, int lo, int hi, int idx, int value) {
        if (lo == hi) {
            this.maxTree[node] = value;
            this.sumTree[node] = value;
            return;
        }
        int mid = (lo + hi) / 2;
        if (idx <= mid) {
            update(2 * node, lo, mid, idx, value);
        } else {
            update(2 * node + 1, mid + 1, hi, idx, value);
        }
        pull(node);
    }

    private long querySum(int node, int lo, int hi, int left, int right) {
        if (right < lo || hi < left) {
            return 0L;
        }
        if (left <= lo && hi <= right) {
            return this.sumTree[node];
        }
        int mid = (lo + hi) / 2;
        return querySum(2 * node, lo, mid, left, right)
                + querySum(2 * node + 1, mid + 1, hi, left, right);
    }

    /** leftmost index in [0, right] whose free count is >= k, else -1 */
    private int findFirst(int node, int lo, int hi, int right, int k) {
        if (lo > right || this.maxTree[node] < k) {
            return -1;
        }
        if (lo == hi) {
            return lo;
        }
        int mid = (lo + hi) / 2;
        int found = findFirst(2 * node, lo, mid, right, k);
        if (found != -1) {
            return found;
        }
        return findFirst(2 * node + 1, mid + 1, hi, right, k);
    }

    public int[] gather(int k, int maxRow) {
        int row = findFirst(1, 0, this.n - 1, maxRow, k);
        if (row == -1) {
            return new int[0];
        }
        int seat = this.m - this.free[row];
        this.free[row] -= k;
        update(1, 0, this.n - 1, row, this.free[row]);
        return new int[]{row, seat};
    }

    public boolean scatter(int k, int maxRow) {
        if (querySum(1, 0, this.n - 1, 0, maxRow) < (long) k) {
            return false;
        }
        long remain = k;
        int row = this.first;
        while (remain > 0) {
            int take = (int) Math.min(remain, (long) this.free[row]);
            this.free[row] -= take;
            remain -= take;
            update(1, 0, this.n - 1, row, this.free[row]);
            if (this.free[row] == 0) {
                this.first = row + 1;
            }
            row++;
        }
        return true;
    }
}
