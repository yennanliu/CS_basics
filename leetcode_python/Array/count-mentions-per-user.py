"""

3433. Count Mentions Per User
Medium

You are given an integer numberOfUsers representing the total number of users
and an array events of size n x 3.

Each events[i] can be either of the following two types:

Message Event: ["MESSAGE", "timestamp_i", "mentions_string_i"]

This event indicates that a set of users was mentioned in a message at
timestamp_i.
The mentions_string_i string can contain one of the following tokens:

id<number>: where <number> is an integer in range [0,numberOfUsers - 1]. There
can be multiple ids separated by a single whitespace and may contain duplicates.
This can mention even the offline users.
ALL: mentions all users.
HERE: mentions all online users.

Offline Event: ["OFFLINE", "timestamp_i", "id_i"]

This event indicates that the user id_i had become offline at timestamp_i for 60
time units. The user will automatically be online again at time timestamp_i +
60.

Return an array mentions where mentions[i] represents the number of mentions the
user with id i has across all MESSAGE events.

All users are initially online, and if a user goes offline or comes back online,
their status change is processed before handling any message event that occurs
at the same timestamp.

Note that a user can be mentioned multiple times in a single message event, and
each mention should be counted separately.

Example 1:

Input: numberOfUsers = 2, events = [["MESSAGE","10","id1 id0"],["OFFLINE","11","0"],["MESSAGE","71","HERE"]]

Output: [2,2]

Explanation:

Initially, all users are online.

At timestamp 10, id1 and id0 are mentioned. mentions = [1,1]

At timestamp 11, id0 goes offline.

At timestamp 71, id0 comes back online and "HERE" is mentioned. mentions = [2,2]

Example 2:

Input: numberOfUsers = 2, events = [["MESSAGE","10","id1 id0"],["OFFLINE","11","0"],["MESSAGE","12","ALL"]]

Output: [2,2]

Explanation:

Initially, all users are online.

At timestamp 10, id1 and id0 are mentioned. mentions = [1,1]

At timestamp 11, id0 goes offline.

At timestamp 12, "ALL" is mentioned. This includes offline users, so both id0
and id1 are mentioned. mentions = [2,2]

Example 3:

Input: numberOfUsers = 2, events = [["OFFLINE","10","0"],["MESSAGE","12","HERE"]]

Output: [0,1]

Explanation:

Initially, all users are online.

At timestamp 10, id0 goes offline.

At timestamp 12, "HERE" is mentioned. Because id0 is still offline, they will
not be mentioned. mentions = [0,1]

Constraints:

1 <= numberOfUsers <= 100
1 <= events.length <= 100
events[i].length == 3
events[i][0] will be one of MESSAGE or OFFLINE.
1 <= int(events[i][1]) <= 10^5
The number of id<number> mentions in any "MESSAGE" event is between 1 and 100.
0 <= <number> <= numberOfUsers - 1
It is guaranteed that the user id referenced in the OFFLINE event is online at
the time the event occurs.

"""

# V0
# IDEA : SORT THE EVENTS, KEEP AN "OFFLINE UNTIL" CLOCK PER USER
#
#   the events arrive in arbitrary order, so first put them on a timeline.  the
#   tie-break matters: at equal timestamps a status change must land before a
#   message, so OFFLINE sorts ahead of MESSAGE.
#
#   coming back online needs no event at all — going offline at t simply means
#   "offline for the half-open span [t, t+60)", so a user is online at time now
#   iff offlineUntil[u] <= now.  that one integer per user replaces any queue of
#   wake-up events.
#
#   the three mention tokens then read straight off: ALL hits everybody
#   regardless of status, HERE hits only the users whose clock has expired, and
#   an explicit idK hits that user even while offline — and repeated ids in one
#   message each count separately, so the tokens are not de-duplicated.
#
# time = O(n log n + total tokens), space = O(numberOfUsers + n)
class Solution(object):
    def countMentions(self, numberOfUsers, events):
        order = sorted(range(len(events)),
                       key=lambda i: (int(events[i][1]),
                                      0 if events[i][0] == "OFFLINE" else 1))
        mentions = [0] * numberOfUsers
        offlineUntil = [0] * numberOfUsers
        for i in order:
            kind, ts, payload = events[i]
            t = int(ts)
            if kind == "OFFLINE":
                offlineUntil[int(payload)] = t + 60
            elif payload == "ALL":
                for u in range(numberOfUsers):
                    mentions[u] += 1
            elif payload == "HERE":
                for u in range(numberOfUsers):
                    if offlineUntil[u] <= t:
                        mentions[u] += 1
            else:
                for tok in payload.split():
                    mentions[int(tok[2:])] += 1
        return mentions


# V0-1
# IDEA : TIMELINE SWEEP WITH A MIN-HEAP OF WAKE-UP EVENTS + LIVE ONLINE COUNT
#
#   instead of one integer clock per user, model "back online at t + 60" as a
#   real event: push (t + 60, user) on a min-heap when the user goes offline,
#   and before handling anything at time t, pop every wake-up whose time has
#   already arrived.  status is then a plain boolean per user.
#
#   the payoff is HERE: keeping a running `online` counter while flipping the
#   booleans makes a HERE mention O(#online-to-add) via the counter instead of
#   an O(numberOfUsers) rescan -- but HERE still has to credit each online user
#   individually, so we walk the flag array.  what the heap really buys is that
#   the wake-up logic is explicit and reusable for "offline for X units"
#   variants where a single deadline no longer suffices.
#
# time = O(n log n + n * numberOfUsers)
# space = O(numberOfUsers + n)
import heapq
class Solution(object):
    def countMentions(self, numberOfUsers, events):
        timeline = sorted(events,
                          key=lambda e: (int(e[1]), 0 if e[0] == "OFFLINE" else 1))
        mentions = [0] * numberOfUsers
        online = [True] * numberOfUsers
        wake = []                                  # min-heap of (time, user)
        for kind, ts, payload in timeline:
            t = int(ts)
            while wake and wake[0][0] <= t:        # process comebacks first
                _, u = heapq.heappop(wake)
                online[u] = True
            if kind == "OFFLINE":
                u = int(payload)
                online[u] = False
                heapq.heappush(wake, (t + 60, u))
            elif payload == "ALL":
                for u in range(numberOfUsers):
                    mentions[u] += 1
            elif payload == "HERE":
                for u in range(numberOfUsers):
                    if online[u]:
                        mentions[u] += 1
            else:
                for tok in payload.split():
                    mentions[int(tok[2:])] += 1
        return mentions


# V0-2
# IDEA : NO SORTING AT ALL -- COLLECT OFFLINE INTERVALS, THEN SCAN THEM PER
#        MESSAGE
#
#   the answer is a plain sum of independent contributions, so the ORDER in
#   which we credit mentions is irrelevant -- ordering only ever mattered as a
#   way of knowing who was online at a given instant.  that question can be
#   answered directly: user u is offline at time t iff some OFFLINE event of u
#   happened in the half-open window (t - 60, t], i.e. ot <= t < ot + 60.
#   (t == ot counts as offline because the status change is processed first,
#   t == ot + 60 counts as online again.)
#
#   so: one pass to harvest every offline interval, a second pass over the
#   MESSAGE events, and for a HERE we test each user against the intervals.
#   quadratic, but sort-free and it never has to reason about a timeline --
#   the honest brute force against which V0 / V0-1 can be checked.
#
# time = O(n * (numberOfUsers + n))
# space = O(n + numberOfUsers)
class Solution(object):
    def countMentions(self, numberOfUsers, events):
        offs = [(int(ts), int(payload))
                for kind, ts, payload in events if kind == "OFFLINE"]
        mentions = [0] * numberOfUsers
        for kind, ts, payload in events:
            if kind != "MESSAGE":
                continue
            t = int(ts)
            if payload == "ALL":
                for u in range(numberOfUsers):
                    mentions[u] += 1
            elif payload == "HERE":
                offline_now = set(u for ot, u in offs if ot <= t < ot + 60)
                for u in range(numberOfUsers):
                    if u not in offline_now:
                        mentions[u] += 1
            else:
                for tok in payload.split():
                    mentions[int(tok[2:])] += 1
        return mentions
