"""

765. Couples Holding Hands
Hard

There are n couples sitting in 2n seats arranged in a row and want to hold
hands.

The people and seats are represented by an integer array row where row[i] is
the ID of the person sitting in the ith seat. The couples are numbered in
order, the first couple being (0, 1), the second couple being (2, 3), and so on
with the last couple being (2n - 2, 2n - 1).

Return the minimum number of swaps so that every couple is sitting side by
side. A swap consists of choosing any two people, then they stand up and switch
seats.


Example 1:

Input: row = [0,2,1,3]
Output: 1
Explanation: We only need to swap the second (row[1]) and third (row[2]) person.

Example 2:

Input: row = [3,2,0,1]
Output: 0
Explanation: All couples are already seated side by side.


Constraints:

2n == row.length
2 <= n <= 30
n is even.
0 <= row[i] < 2n
All the elements of row are unique.

"""

# V0
# IDEA : GREEDY + HASHMAP (person -> seat)
#
#   Walk the row one bench (seats i, i+1) at a time. The person in seat i
#   stays put; if seat i+1 does not hold their partner, swap the partner into
#   seat i+1. Each swap seats one couple for good and never un-seats an
#   earlier bench, so every swap is necessary -> the count is minimal.
#
#   partner of x: x is even -> x + 1, x is odd -> x - 1
#
#   e.g. row = [0,2,1,3] -> bench 0: 0 wants 1 (seat 2), swap 2 <-> 1
#        -> [0,1,2,3] -> bench 1: 2 sits with 3 -> 1 swap
#
# time = O(n), space = O(n)
class Solution(object):
    def minSwapsCouples(self, row):
        """
        :type row: List[int]
        :rtype: int
        """
        # work on a copy, so the caller's list is not reordered
        seats = list(row)

        # person -> the seat they currently sit in
        seat_of = {}
        for idx, person in enumerate(seats):
            seat_of[person] = idx

        swaps = 0
        # one bench = two seats, so step by 2
        for i in range(0, len(seats), 2):
            first = seats[i]
            if first % 2 == 0:
                partner = first + 1
            else:
                partner = first - 1

            # bench already holds a couple
            if seats[i + 1] == partner:
                continue

            # swap the stranger in seat i+1 with the partner
            partner_seat = seat_of[partner]
            stranger = seats[i + 1]
            seats[i + 1] = partner
            seats[partner_seat] = stranger

            # NOTE !!! the map must follow the array, or a later lookup
            #          sends the next swap to a stale seat
            seat_of[partner] = i + 1
            seat_of[stranger] = partner_seat

            swaps += 1

        return swaps


# V1
# IDEA : UNION FIND (couples as nodes, benches as edges)
#
#   Each couple is a node (couple id = person // 2). Each bench joins the two
#   couples sitting on it. The benches form disjoint cycles: a cycle of k
#   couples needs exactly k - 1 swaps to untangle (each swap seats one couple,
#   and the last swap seats two). Summing over cycles:
#
#       answer = n_couples - number_of_components
#
#   e.g. row = [0,2,1,3] -> benches join (0,1) and (0,1) -> 1 component of
#        2 couples -> 2 - 1 = 1
#
#   NOTE : same bound as V0; worth knowing because it explains WHY the greedy
#          count is minimal (it is the cycle decomposition)
#
# time = O(n * alpha(n)), space = O(n)
class Solution2(object):
    def minSwapsCouples(self, row):
        """
        :type row: List[int]
        :rtype: int
        """
        n_couples = len(row) // 2
        parent = list(range(n_couples))

        def find(x):
            """Root of x's component, with path halving."""
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        components = n_couples
        for i in range(0, len(row), 2):
            # the two couples sharing this bench
            couple_a = row[i] // 2
            couple_b = row[i + 1] // 2
            root_a = find(couple_a)
            root_b = find(couple_b)
            # a merge joins two couples into one cycle
            if root_a != root_b:
                parent[root_a] = root_b
                components -= 1

        return n_couples - components
