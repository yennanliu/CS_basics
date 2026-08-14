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
["SORTracker", "add", "add", "get", "add", "get", "add", "get", "add", "get", "add", "get", "get"]
[[], ["bradford", 2], ["branford", 3], [], ["alps", 2], [], ["orland", 2], [], ["orlando", 3], [], ["alpine", 2], [], []]
Output
[null, null, null, "branford", null, "alps", null, "bradford", null, "bradford", null, "bradford", "orland"]

Explanation
SORTracker tracker = new SORTracker(); // Initialize the tracker system.
tracker.add("bradford", 2); // Add location with name="bradford" and score=2 to the system.
tracker.add("branford", 3); // Add location with name="branford" and score=3 to the system.
tracker.get();              // The sorted locations, from best to worst, are: branford, bradford.
                            // Note that branford precedes bradford due to its higher score (3 > 2).
                            // This is the 1st time get() is called, so return the best location: "branford".
tracker.add("alps", 2);     // Add location with name="alps" and score=2 to the system.
tracker.get();              // Sorted locations: branford, alps, bradford.
                            // Note that alps precedes bradford even though they have the same score (2)
                            // because "alps" is lexicographically smaller than "bradford".
                            // Return the 2nd best location "alps", as it is the 2nd time get() is called.
tracker.add("orland", 2);   // Add location with name="orland" and score=2 to the system.
tracker.get();              // Sorted locations: branford, alps, bradford, orland.
                            // Return "bradford", as it is the 3rd time get() is called.
tracker.add("orlando", 3);  // Add location with name="orlando" and score=3 to the system.
tracker.get();              // Sorted locations: branford, orlando, alps, bradford, orland.
                            // Return "bradford".
tracker.add("alpine", 2);   // Add location with name="alpine" and score=2 to the system.
tracker.get();              // Sorted locations: branford, orlando, alpine, alps, bradford, orland.
                            // Return "bradford".
tracker.get();              // Sorted locations: branford, orlando, alpine, alps, bradford, orland.
                            // Return "orland".


Constraints:

name consists of lowercase English letters, and is unique among all locations.
1 <= name.length <= 10
1 <= score <= 10^5
At most 4 * 10^4 calls in total will be made to add and get.

"""

# V0
# IDEA : TWO HEAPS STRADDLING THE ANSWER POSITION
#
#   the i-th get() asks for the i-th best location, and i only ever grows by
#   one. so split every location seen so far into two piles :
#
#     left  = the best i already handed out — a MAX-heap, so its WORST is on top
#     right = everything else               — a MIN-heap, so its BEST  is on top
#
#   "better" means HIGHER score, and on a tie the LEXICOGRAPHICALLY SMALLER
#   name. that ordering does not match any plain tuple comparison (the score
#   wants descending, the name ascending), so it lives in a tiny wrapper
#   class, with a second wrapper inverting it for the max-heap.
#
#   add(name, score) : push into `right`, then restore the invariant "every
#                      left entry is better than every right entry" by
#                      swapping the two tops if they are out of order
#   get()            : move right's best into left and return it — after k
#                      calls left holds exactly the top k, and the one just
#                      moved is the k-th best
#
#   NOTE : an already-returned location can be returned AGAIN by a later
#          get() if better locations were added in between — that is why the
#          answer is never simply popped off.
#
# time = O(log n) per call, space = O(n)
import heapq


class _Loc(object):
    """orders BETTER locations first (min-heap -> best on top)"""
    __slots__ = ('score', 'name')

    def __init__(self, score, name):
        self.score = score
        self.name = name

    def __lt__(self, other):
        if self.score != other.score:
            return self.score > other.score
        return self.name < other.name


class _Worst(object):
    """orders WORSE locations first (min-heap -> worst on top)"""
    __slots__ = ('loc',)

    def __init__(self, loc):
        self.loc = loc

    def __lt__(self, other):
        return other.loc < self.loc


class SORTracker(object):

    def __init__(self):
        self.left = []    # _Worst wrappers : the best-so-far, worst on top
        self.right = []   # _Loc            : the rest, best on top

    def add(self, name, score):
        heapq.heappush(self.right, _Loc(score, name))
        if self.left and self.right[0] < self.left[0].loc:
            better = heapq.heappop(self.right)
            worse = heapq.heappop(self.left).loc
            heapq.heappush(self.right, worse)
            heapq.heappush(self.left, _Worst(better))

    def get(self):
        best = heapq.heappop(self.right)
        heapq.heappush(self.left, _Worst(best))
        return best.name


# Your SORTracker object will be instantiated and called as such:
# obj = SORTracker()
# obj.add(name,score)
# param_2 = obj.get()
