"""

2102. Sequentially Ordinal Rank Tracker
Hard

A scenic location is represented by its name and attractiveness score, where name is a unique string among all locations and score is an integer. Locations can be ranked from the best to the worst. The higher the score, the better the location. If the scores of two locations are equal, then the location with the lexicographically smaller name is better.

You are building a system that tracks the ranking of locations with the system initially starting with no locations. It supports:

Adding scenic locations, one at a time.
Querying the ith best location of all locations already added, where i is the number of times the system has been queried (including the current query).
For example, when the system is queried for the 4th time, it returns the 4th best location of all locations already added.

Note that the test data are generated so that at any time, the number of queries does not exceed the number of locations added to the system.

Implement the SORTracker class:

SORTracker() Initializes the tracker system.
void add(string name, int score) Adds a scenic location with name and score to the system.
string get() Queries and returns the ith best location, where i is the number of times this method has been invoked (including this invocation).


Example 1:

Input
["SORTracker", "add", "add", "get", "add", "get", "add", "get", "add", "get", "get"]
[[], ["bradford", 2], ["branford", 3], [], ["alps", 2], [], ["orland", 2], [], ["orlando", 3], [], []]
Output
[null, null, null, "branford", null, "alps", null, "bradford", null, "bradford", "orland"]

Explanation
SORTracker tracker = new SORTracker(); // Initialize the tracker system.
tracker.add("bradford", 2); // Add location with name="bradford" and score=2 to the system.
tracker.add("branford", 3); // Add location with name="branford" and score=3 to the system.
tracker.get();              // The sorted locations, from best to worst, are: branford, bradford.
                            // Note that branford precedes bradford due to its higher score (3 > 2).
                            // This is the 1st time get() is called, so return the best location: "branford".
tracker.add("alps", 2);     // Add location with name="alps" and score=2 to the system.
tracker.get();              // Sorted locations: branford, alps, bradford.
                            // Note that alps precedes bradford even though they have the same score (2).
                            // This is because "alps" is lexicographically smaller than "bradford".
                            // Return the 2nd best location "alps", as it is the 2nd time get() is called.
tracker.add("orland", 2);   // Add location with name="orland" and score=2 to the system.
tracker.get();              // Sorted locations: branford, alps, bradford, orland.
                            // Return "bradford", as it is the 3rd time get() is called.
tracker.add("orlando", 3);  // Add location with name="orlando" and score=3 to the system.
tracker.get();              // Sorted locations: branford, orlando, alps, bradford, orland.
                            // Return "bradford", as it is the 4th time get() is called.
tracker.get();              // Sorted locations: branford, orlando, alps, bradford, orland.
                            // Return "orland", as it is the 5th time get() is called.


Constraints:

name consists of lowercase English letters, and is unique among all locations.
1 <= name.length <= 10
1 <= score <= 10^5
At any time, the number of calls to get does not exceed the number of calls to add.
At most 4 * 10^4 calls in total will be made to add and get.

"""

# V0
# IDEA : TWO HEAPS STRADDLING THE ANSWER POSITION
#
#   the k-th get() must return the k-th best location, and k only ever grows
#   by one. so keep the locations split in two :
#
#       best  : the k best so far, as a MAX-heap on "goodness"
#               -> its top is the WORST of the good ones = the k-th best
#       rest  : everything else, as a MIN-heap on "goodness"
#               -> its top is the best one not yet promoted
#
#   get()  : promote rest's top into best, then read best's top.
#   add()  : drop the new location into rest; if it is better than best's
#            current worst, swap the two so `best` still holds the true top-k.
#
#   ordering — better means higher score, ties broken by SMALLER name. so as
#   a min-heap key,  (-score, name)  puts the best first. for the max-heap
#   the name must compare backwards, hence the tiny _RevStr wrapper.
#
# time = O(log n) per add / get, space = O(n)
import heapq


class _RevStr(object):
    """String that compares in reverse order, so heapq can act as a max-heap."""

    def __init__(self, s):
        self.s = s

    def __lt__(self, other):
        return self.s > other.s

    def __eq__(self, other):
        return self.s == other.s


class SORTracker(object):

    def __init__(self):
        self.best = []   # max-heap of (score, _RevStr(name)) -> worst-of-best on top
        self.rest = []   # min-heap of (-score, name)         -> best-of-rest on top

    def add(self, name, score):
        heapq.heappush(self.rest, (-score, name))
        if self.best:
            b_score, b_name = self.best[0][0], self.best[0][1].s
            r_score, r_name = -self.rest[0][0], self.rest[0][1]
            # is rest's best strictly better than best's worst ?
            if (-r_score, r_name) < (-b_score, b_name):
                heapq.heappop(self.rest)
                heapq.heappop(self.best)
                heapq.heappush(self.best, (r_score, _RevStr(r_name)))
                heapq.heappush(self.rest, (-b_score, b_name))

    def get(self):
        score, name = heapq.heappop(self.rest)
        heapq.heappush(self.best, (-score, _RevStr(name)))
        return self.best[0][1].s


# Your SORTracker object will be instantiated and called as such:
# obj = SORTracker()
# obj.add(name,score)
# param_2 = obj.get()
