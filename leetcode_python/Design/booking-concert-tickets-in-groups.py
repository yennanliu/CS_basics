"""

2286. Booking Concert Tickets in Groups
Hard

A concert hall has n rows numbered from 0 to n - 1, each with m seats, numbered from 0 to m - 1. You need to design a ticketing system that can allocate seats in the following cases:

If a group of k spectators can sit together in a row.
If every member of a group of k spectators can get a seat. They may or may not sit together.

Note that the spectators are very picky. Hence:

They will book seats only if each member of their group can get a seat with row number less than or equal to maxRow. maxRow can vary from group to group.
In case there are multiple rows to choose from, the row with the smallest number is chosen. If there are multiple seats to choose in the same row, the seat with the smallest number is chosen.

Implement the BookMyShow class:

BookMyShow(int n, int m) Initializes the object with n as number of rows and m as number of seats per row.
int[] gather(int k, int maxRow) Returns an array of length 2 denoting the row and seat number (respectively) of the first seat being allocated to the k members of the group, who must sit together. In other words, it returns the smallest possible r and c such that all [c, c + k - 1] seats are valid and empty in row r, and r <= maxRow. Returns [] in case it is not possible to allocate seats to the group.
boolean scatter(int k, int maxRow) Returns true if all k members of the group can be allocated seats in rows 0 to maxRow, who may or may not sit together. If the seats can be allocated, it allocates k seats to the group with the smallest row numbers, and the smallest possible seat numbers in each row. Otherwise, returns false.


Example 1:

Input
["BookMyShow", "gather", "gather", "scatter", "scatter"]
[[2, 5], [4, 0], [2, 0], [5, 1], [5, 1]]
Output
[null, [0, 0], [], true, false]

Explanation
BookMyShow bms = new BookMyShow(2, 5); // There are 2 rows with 5 seats each
bms.gather(4, 0); // return [0, 0]
                  // The group books seats [0, 3] of row 0.
bms.gather(2, 0); // return []
                  // There is only 1 seat left in row 0,
                  // so it is not possible to book 2 consecutive seats.
bms.scatter(5, 1); // return True
                   // The group books seat 4 of row 0 and seats [0, 3] of row 1.
bms.scatter(5, 1); // return False
                   // There is only one seat left in the hall.


Constraints:

1 <= n <= 5 * 10^4
1 <= m, k <= 10^9
0 <= maxRow <= n - 1
At most 5 * 10^4 calls in total will be made to gather and scatter.

"""

# V0
# IDEA : SEGMENT TREE HOLDING (MAX FREE SEATS, TOTAL FREE SEATS) PER ROW
#
#   the two operations need two different aggregates :
#     gather  -> the LEFTMOST row in [0, maxRow] whose free count is >= k,
#                which a MAX-tree can find by descending left-first
#     scatter -> the TOTAL free seats in [0, maxRow] to decide feasibility,
#                which a SUM-tree answers
#   so each node keeps both.
#
#   scatter then fills rows greedily from the front. the crucial detail is
#   the persistent pointer `first`: rows before it are completely full, and
#   it only ever moves FORWARD, so the total filling work across all calls is
#   O(n) rather than O(n) per call.
#
#   seats in a row are always taken from the left, so a row with `free`
#   remaining has its next free seat at column m - free.
#
# time = O(log n) per gather, amortised O(log n) per scatter; space = O(n)
class BookMyShow(object):

    def __init__(self, n, m):
        self.n = n
        self.m = m
        self.free = [m] * n
        self.max_tree = [0] * (4 * n)
        self.sum_tree = [0] * (4 * n)
        self._build(1, 0, n - 1)
        self.first = 0            # first row that still has a free seat

    def _build(self, node, lo, hi):
        if lo == hi:
            self.max_tree[node] = self.m
            self.sum_tree[node] = self.m
            return
        mid = (lo + hi) // 2
        self._build(2 * node, lo, mid)
        self._build(2 * node + 1, mid + 1, hi)
        self.max_tree[node] = max(self.max_tree[2 * node], self.max_tree[2 * node + 1])
        self.sum_tree[node] = self.sum_tree[2 * node] + self.sum_tree[2 * node + 1]

    def _update(self, node, lo, hi, idx, value):
        if lo == hi:
            self.max_tree[node] = value
            self.sum_tree[node] = value
            return
        mid = (lo + hi) // 2
        if idx <= mid:
            self._update(2 * node, lo, mid, idx, value)
        else:
            self._update(2 * node + 1, mid + 1, hi, idx, value)
        self.max_tree[node] = max(self.max_tree[2 * node], self.max_tree[2 * node + 1])
        self.sum_tree[node] = self.sum_tree[2 * node] + self.sum_tree[2 * node + 1]

    def _query_sum(self, node, lo, hi, left, right):
        if right < lo or hi < left:
            return 0
        if left <= lo and hi <= right:
            return self.sum_tree[node]
        mid = (lo + hi) // 2
        return (self._query_sum(2 * node, lo, mid, left, right)
                + self._query_sum(2 * node + 1, mid + 1, hi, left, right))

    def _find_first(self, node, lo, hi, right, k):
        """leftmost index in [0, right] whose free count is >= k, else -1"""
        if lo > right or self.max_tree[node] < k:
            return -1
        if lo == hi:
            return lo
        mid = (lo + hi) // 2
        found = self._find_first(2 * node, lo, mid, right, k)
        if found != -1:
            return found
        return self._find_first(2 * node + 1, mid + 1, hi, right, k)

    def gather(self, k, maxRow):
        row = self._find_first(1, 0, self.n - 1, maxRow, k)
        if row == -1:
            return []
        seat = self.m - self.free[row]
        self.free[row] -= k
        self._update(1, 0, self.n - 1, row, self.free[row])
        return [row, seat]

    def scatter(self, k, maxRow):
        if self._query_sum(1, 0, self.n - 1, 0, maxRow) < k:
            return False
        row = self.first
        while k > 0:
            take = min(k, self.free[row])
            self.free[row] -= take
            k -= take
            self._update(1, 0, self.n - 1, row, self.free[row])
            if self.free[row] == 0:
                self.first = row + 1
            row += 1
        return True


# Your BookMyShow object will be instantiated and called as such:
# obj = BookMyShow(n, m)
# param_1 = obj.gather(k,maxRow)
# param_2 = obj.scatter(k,maxRow)
