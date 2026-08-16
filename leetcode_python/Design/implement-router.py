"""

3508. Implement Router
Medium

Design a data structure that can efficiently manage data packets in a network
router. Each data packet consists of the following attributes:

source: A unique identifier for the machine that generated the packet.

destination: A unique identifier for the target machine.

timestamp: The time at which the packet arrived at the router.

Implement the Router class:

Router(int memoryLimit): Initializes the Router object with a fixed memory
limit.

memoryLimit is the maximum number of packets the router can store at any given
time.

If adding a new packet would exceed this limit, the oldest packet must be
removed to free up space.

bool addPacket(int source, int destination, int timestamp): Adds a packet with
the given attributes to the router.

A packet is considered a duplicate if another packet with the same source,
destination, and timestamp already exists in the router.

Return true if the packet is successfully added (i.e., it is not a duplicate);
otherwise return false.

int[] forwardPacket(): Forwards the next packet in FIFO (First In First Out)
order.

Remove the packet from storage.

Return the packet as an array [source, destination, timestamp].

If there are no packets to forward, return an empty array.

int getCount(int destination, int startTime, int endTime):

Returns the number of packets currently stored in the router (i.e., not yet
forwarded) that have the specified destination and have timestamps in the
inclusive range [startTime, endTime].

Note that queries for addPacket will be made in non-decreasing order of
timestamp.

Example 1:

Input:

["Router", "addPacket", "addPacket", "addPacket", "addPacket", "addPacket",
"forwardPacket", "addPacket", "getCount"]

[[3], [1, 4, 90], [2, 5, 90], [1, 4, 90], [3, 5, 95], [4, 5, 105], [], [5, 2,
110], [5, 100, 110]]

Output:

[null, true, true, false, true, true, [2, 5, 90], true, 1]

Explanation

Router router = new Router(3); // Initialize Router with memoryLimit of 3.

router.addPacket(1, 4, 90); // Packet is added. Return True.

router.addPacket(2, 5, 90); // Packet is added. Return True.

router.addPacket(1, 4, 90); // This is a duplicate packet. Return False.

router.addPacket(3, 5, 95); // Packet is added. Return True

router.addPacket(4, 5, 105); // Packet is added, [1, 4, 90] is removed as number
of packets exceeds memoryLimit. Return True.

router.forwardPacket(); // Return [2, 5, 90] and remove it from router.

router.addPacket(5, 2, 110); // Packet is added. Return True.

router.getCount(5, 100, 110); // The only packet with destination 5 and
timestamp in the inclusive range [100, 110] is [4, 5, 105]. Return 1.

Example 2:

Input:

["Router", "addPacket", "forwardPacket", "forwardPacket"]

[[2], [7, 4, 90], [], []]

Output:

[null, true, [7, 4, 90], []]

Explanation

Router router = new Router(2); // Initialize Router with memoryLimit of 2.

router.addPacket(7, 4, 90); // Return True.

router.forwardPacket(); // Return [7, 4, 90].

router.forwardPacket(); // There are no packets left, return [].

Constraints:

2 <= memoryLimit <= 10^5

1 <= source, destination <= 2 * 10^5

1 <= timestamp <= 10^9

1 <= startTime <= endTime <= 10^9

At most 10^5 calls will be made to addPacket, forwardPacket, and getCount
methods altogether.

queries for addPacket will be made in non-decreasing order of timestamp.

"""

# V0
# IDEA : FIFO QUEUE + PER-DESTINATION TIMESTAMP LIST WITH A CONSUMED POINTER
#
#   two things have to be fast at once: FIFO eviction, and counting the live
#   packets of one destination inside a timestamp range.  a deque gives the
#   first.  for the second, keep one list of timestamps per destination.
#
#   the trick that makes those lists usable is the guarantee that addPacket is
#   called with non-decreasing timestamps: each per-destination list is then
#   already sorted, so a range count is two binary searches.  and because
#   removal is strictly FIFO, the packets of a destination always leave in the
#   same order they arrived -- so the departed ones are exactly a prefix of its
#   list.  storing how many left (a pointer) is enough to skip them; nothing
#   ever has to be deleted from the middle of a list.
#
#   duplicates are rejected with a set of (source, destination, timestamp)
#   triples, kept in sync with the queue.
#
# time = O(1) add / forward amortised, O(log n) getCount, space = O(n)
import bisect
from collections import deque, defaultdict


class Router(object):

    def __init__(self, memoryLimit):
        self.limit = memoryLimit
        self.q = deque()
        self.seen = set()
        self.times = defaultdict(list)     # destination -> arrival timestamps
        self.gone = defaultdict(int)       # destination -> how many left already

    def addPacket(self, source, destination, timestamp):
        key = (source, destination, timestamp)
        if key in self.seen:
            return False
        if len(self.q) >= self.limit:
            self.forwardPacket()
        self.seen.add(key)
        self.q.append(key)
        self.times[destination].append(timestamp)
        return True

    def forwardPacket(self):
        if not self.q:
            return []
        s, d, t = self.q.popleft()
        self.seen.discard((s, d, t))
        self.gone[d] += 1
        return [s, d, t]

    def getCount(self, destination, startTime, endTime):
        ts = self.times[destination]
        start = self.gone[destination]     # everything before this already left
        lo = bisect.bisect_left(ts, startTime, start)
        hi = bisect.bisect_right(ts, endTime, start)
        return hi - lo
