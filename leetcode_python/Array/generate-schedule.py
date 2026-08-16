"""

3680. Generate Schedule
Medium

You are given an integer n representing n teams. You are asked to generate
a schedule such that:

Each team plays every other team exactly twice: once at home and once away.
There is exactly one match per day; the schedule is a list of consecutive
days and schedule[i] is the match on day i.
No team plays on consecutive days.

Return a 2D integer array schedule, where schedule[i][0] represents the
home team and schedule[i][1] represents the away team. If multiple
schedules meet the conditions, return any one of them.

If no schedule exists that meets the conditions, return an empty array.


Example 1:

Input: n = 3
Output: []
Explanation:
Since each team plays every other team exactly twice, a total of 6 matches
need to be played: [0,1],[0,2],[1,2],[1,0],[2,0],[2,1].
It's not possible to create a schedule without at least one team playing
consecutive days.

Example 2:

Input: n = 5
Output: [[0,1],[2,3],[0,4],[1,2],[3,4],[0,2],[1,3],[2,4],[0,3],[1,4],
         [2,0],[3,1],[4,0],[2,1],[4,3],[1,0],[3,2],[4,1],[3,0],[4,2]]
Explanation:
Since each team plays every other team exactly twice, a total of 20 matches
need to be played.
The output shows one of the schedules that meet the conditions. No team
plays on consecutive days.


Constraints:

2 <= n <= 50

"""

# V0
# IDEA : GROUP THE MATCHES BY CIRCULAR GAP, THEN WALK EACH GAP IN ORDER
#
#   seat the teams on a circle 0..n-1 and label a match by its gap
#   g = (away - home) mod n. for a fixed gap g the n matches
#   [i, (i+g) % n], i = 0..n-1, hit every pair at circular distance g once;
#   listing them with i increasing means consecutive days differ by a
#   rotation of one seat, and the two matches {i, i+g} and {i+1, i+1+g}
#   share a team only when g == 1 or g == n-1. so EVERY gap from 2 upward is
#   automatically conflict-free just by sweeping i, and running the block
#   twice (once as [i, i+g], once as [i+g, i]) supplies the home and away
#   copies. gap n/2 for even n is the exception that needs only one block,
#   since {i, i+n/2} and {i+n/2, i} are the same pair seen from both ends.
#
#   gap 1 is the one troublemaker -- [i, i+1] and [i+1, i+2] always touch --
#   so it is laid out by parity instead: all the even-start matches
#   [0,1], [2,3], ... (pairwise disjoint), then the odd-start ones.
#
#   that leaves only the seams between blocks. each block may begin at any
#   rotation i, so the next block is simply started one seat past the team
#   that just played, which clears the seam for every n in range.
#
#   n <= 4 is impossible: with n = 4 each team must play 6 of the 12 days
#   with no two adjacent, which forces two teams onto the odd days and two
#   onto the even days -- so those two pairs would face each other every
#   time instead of twice. n = 2 and n = 3 fail even more directly.
#
# time = O(n^2), space = O(1) besides the output
class Solution(object):
    def generateSchedule(self, n):
        if n <= 4:
            return []

        res = []
        # ---- gap 1, laid out by parity so no two consecutive days touch
        if n % 2 == 0:
            for i in range(0, n, 2):
                res.append([i, i + 1])
            for i in range(0, n, 2):
                res.append([i + 1, i])
            for i in range(1, n, 2):
                res.append([i, (i + 1) % n])
            for i in range(1, n, 2):
                res.append([(i + 1) % n, i])
        else:
            # n odd: stepping by 2 around the circle visits every seat once
            for i in range(0, 2 * n, 2):
                res.append([i % n, (i + 1) % n])
            for i in range(0, 2 * n, 2):
                res.append([(i + 1) % n, i % n])

        # ---- gaps 2 .. ceil(n/2)-1, each as a home block then an away block
        for g in range(2, (n + 1) // 2):
            start = res[-1][0] + 1
            for i in range(start, start + n):
                res.append([i % n, (i + g) % n])
            start = res[-1][1] - 1
            for i in range(start, start + n):
                res.append([(i + g) % n, i % n])

        # ---- the diametric gap for even n covers both orientations at once
        if n % 2 == 0:
            g = n // 2
            start = res[-1][0] - 1
            for i in range(start, start + n):
                res.append([i % n, (i + g) % n])
        return res
