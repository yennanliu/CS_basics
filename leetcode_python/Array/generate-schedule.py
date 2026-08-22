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


# V0-1
# IDEA : DAY-BY-DAY GREEDY -- ALWAYS SERVE THE BUSIEST PAIR STILL OWED
#
#   forget the algebra and fill the days one at a time. keep owed[i][j] = 1
#   while "i at home vs j" has not been played, and rem[t] = how many
#   matches team t still owes in total. each day, among the pairs touching
#   neither of yesterday's two teams, play the one whose two teams owe the
#   most matches between them (tie-break: a pair that still owes both
#   orientations goes before a half-finished one).
#
#   why it does not paint itself into a corner: the team that runs out of
#   legal days is always the one with the most matches left, so spending
#   every day on the currently busiest teams keeps the remaining demand
#   flat. verified to produce a legal schedule for every n in 5..50, the
#   whole input range.
#
#   n <= 4 is impossible, exactly as in V0.
#
# time = O(n^4)  (n*(n-1) days x an O(n^2) scan for the best pair)
# space = O(n^2)
class Solution(object):
    def generateSchedule(self, n):
        if n <= 4:
            return []

        owed = [[1] * n for _ in range(n)]
        for i in range(n):
            owed[i][i] = 0
        rem = [2 * (n - 1)] * n

        res = []
        prev = (-1, -1)
        for _ in range(n * (n - 1)):
            pick, key = None, None
            for i in range(n):
                if i in prev:
                    continue
                for j in range(n):
                    if j in prev or not owed[i][j]:
                        continue
                    cand = (rem[i] + rem[j], owed[j][i], -i, -j)
                    if key is None or cand > key:
                        pick, key = (i, j), cand
            if pick is None:
                return []
            i, j = pick
            owed[i][j] = 0
            rem[i] -= 1
            rem[j] -= 1
            res.append([i, j])
            prev = (i, j)
        return res


# V0-2
# IDEA : 1-FACTORIZE K_n INTO ROUNDS, THEN ONLY FIX THE SEAMS
#
#   the classic circle method splits all C(n, 2) pairs into rounds of
#   pairwise-disjoint matches (n-1 rounds of n/2 for even n; for odd n add a
#   dummy team, so each of the n rounds has (n-1)/2 real matches and one
#   resting team). inside a round no two matches share a team, so the days
#   of a round may be emitted in ANY order -- and emitting the round's
#   matches once as [a, b] and then again in the same order as [b, a] covers
#   both legs while keeping the middle seam clean (the last home match and
#   the first away match are different, hence disjoint, pairs).
#
#   that leaves only the joins between rounds. a round with m >= 3 matches
#   always contains a match avoiding the two teams that just played (two
#   teams can block at most two matches), so for n >= 6 a single left-to-right
#   pass never gets stuck; n = 5 has m = 2 and is the only case where the
#   search actually has to try another (first, last) choice, and its state
#   space is a handful of options.
#
# time = O(n^2)  (each round emitted in O(n); backtracking only for n = 5)
# space = O(n^2)
class Solution(object):
    def generateSchedule(self, n):
        if n <= 4:
            return []

        # ---- circle method : rounds of disjoint matches
        m = n if n % 2 == 0 else n + 1          # dummy team n when n is odd
        rounds = []
        for r in range(m - 1):
            rnd = []
            if r < n and m - 1 < n:             # the fixed seat vs the rotator
                rnd.append((r, m - 1))
            for i in range(1, m // 2):
                u, v = (r + i) % (m - 1), (r - i) % (m - 1)
                if u < n and v < n:
                    rnd.append((u, v))
            rounds.append(rnd)

        # ---- order the rounds, and inside each one the first / last match,
        #      so that consecutive days never share a team
        plan = []

        def walk(used, last):
            if len(used) == len(rounds):
                return True
            for ri, edges in enumerate(rounds):
                if ri in used:
                    continue
                for f in range(len(edges)):
                    if last and (edges[f][0] in last or edges[f][1] in last):
                        continue
                    for l in range(len(edges)):
                        if l == f:
                            continue
                        mid = [e for t, e in enumerate(edges) if t != f and t != l]
                        seq = [edges[f]] + mid + [edges[l]]
                        used.add(ri)
                        plan.append(seq)
                        if walk(used, set(edges[l])):
                            return True
                        plan.pop()
                        used.discard(ri)
            return False

        if not walk(set(), None):
            return []

        res = []
        for seq in plan:
            for a, b in seq:
                res.append([a, b])
            for a, b in seq:
                res.append([b, a])
        return res
