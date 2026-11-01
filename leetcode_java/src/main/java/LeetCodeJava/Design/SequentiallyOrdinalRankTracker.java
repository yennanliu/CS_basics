package LeetCodeJava.Design;

// https://leetcode.com/problems/sequentially-ordinal-rank-tracker/description/

import java.util.PriorityQueue;

/**
 * 2102. Sequentially Ordinal Rank Tracker
 * Hard
 *
 * A scenic location is represented by its name and attractiveness score, where name is a
 * unique string among all locations and score is an integer. Locations can be ranked from
 * the best to the worst. The higher the score, the better the location. If the scores of two
 * locations are equal, then the location with the lexicographically smaller name is better.
 *
 * You are building a system that tracks the ranking of locations with the system initially
 * starting with no locations. It supports:
 *
 * Adding scenic locations, one at a time.
 * Querying the ith best location of all locations already added, where i is the number of
 * times the system has been queried (including the current query).
 * For example, when the system is queried for the 4th time, it returns the 4th best location
 * of all locations already added.
 *
 * Note that the test data are generated so that at any time, the number of queries does not
 * exceed the number of locations added to the system.
 *
 * Implement the SORTracker class:
 *
 * SORTracker() Initializes the tracker system.
 * void add(string name, int score) Adds a scenic location with name and score to the system.
 * string get() Queries and returns the ith best location, where i is the number of times
 * this method has been invoked (including this invocation).
 *
 *
 * Example 1:
 *
 * Input
 * ["SORTracker", "add", "add", "get", "add", "get", "add", "get", "add", "get", "add", "get", "get"]
 * [[], ["bradford", 2], ["branford", 3], [], ["alps", 2], [], ["orland", 2], [], ["orlando", 3], [], ["alpine", 2], [], []]
 * Output
 * [null, null, null, "branford", null, "alps", null, "bradford", null, "bradford", null, "bradford", "orland"]
 *
 * Explanation
 * SORTracker tracker = new SORTracker(); // Initialize the tracker system.
 * tracker.add("bradford", 2); // Add location with name="bradford" and score=2 to the system.
 * tracker.add("branford", 3); // Add location with name="branford" and score=3 to the system.
 * tracker.get();              // The sorted locations, from best to worst, are: branford, bradford.
 *                             // Note that branford precedes bradford due to its higher score (3 > 2).
 *                             // This is the 1st time get() is called, so return the best location: "branford".
 * tracker.add("alps", 2);     // Add location with name="alps" and score=2 to the system.
 * tracker.get();              // Sorted locations: branford, alps, bradford.
 *                             // Note that alps precedes bradford even though they have the same score (2)
 *                             // because "alps" is lexicographically smaller than "bradford".
 *                             // Return the 2nd best location "alps", as it is the 2nd time get() is called.
 * tracker.add("orland", 2);   // Add location with name="orland" and score=2 to the system.
 * tracker.get();              // Sorted locations: branford, alps, bradford, orland.
 *                             // Return "bradford", as it is the 3rd time get() is called.
 * tracker.add("orlando", 3);  // Add location with name="orlando" and score=3 to the system.
 * tracker.get();              // Sorted locations: branford, orlando, alps, bradford, orland.
 *                             // Return "bradford".
 * tracker.add("alpine", 2);   // Add location with name="alpine" and score=2 to the system.
 * tracker.get();              // Sorted locations: branford, orlando, alpine, alps, bradford, orland.
 *                             // Return "bradford".
 * tracker.get();              // Sorted locations: branford, orlando, alpine, alps, bradford, orland.
 *                             // Return "orland".
 *
 *
 * Constraints:
 *
 * name consists of lowercase English letters, and is unique among all locations.
 * 1 <= name.length <= 10
 * 1 <= score <= 10^5
 * At most 4 * 10^4 calls in total will be made to add and get.
 *
 */
public class SequentiallyOrdinalRankTracker {

    // V0
    // IDEA: TWO HEAPS STRADDLING THE ANSWER POSITION
    //       the i-th get() asks for the i-th best location, and i only ever grows by one.
    //       so split every location seen so far into two piles:
    //
    //         handedOut = the best i already returned -> its WORST on top
    //         rest      = everything else             -> its BEST on top
    //
    //       invariant: every location in handedOut is better than every location in rest.
    //
    //       add(name, score): push into rest; if rest's best now beats handedOut's worst,
    //                         swap the two tops so the invariant holds again.
    //       get():            move rest's best into handedOut and return it -- after k calls
    //                         handedOut holds exactly the top k, and the one just moved is
    //                         the k-th best.
    //       NOTE: the answer is never simply popped away: a location already returned can
    //             be returned again if better ones were added in between.

    /** One scenic location. */
    private static class Location {
        final String name;
        final int score;

        Location(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    /** Negative when a is BETTER than b: higher score first, then smaller name. */
    private static int compareBetterFirst(Location a, Location b) {
        if (a.score != b.score) {
            // higher score is better -> sort descending by score
            return Integer.compare(b.score, a.score);
        }
        // tie on score -> lexicographically smaller name is better
        return a.name.compareTo(b.name);
    }

    // best-first heap: its head is the best location not yet handed out
    private final PriorityQueue<Location> rest;
    // worst-first heap: its head is the worst of the top-i already handed out
    private final PriorityQueue<Location> handedOut;

    /**
     * time = O(1)
     * space = O(1)
     */
    public SequentiallyOrdinalRankTracker() {
        this.rest = new PriorityQueue<>((a, b) -> compareBetterFirst(a, b));
        this.handedOut = new PriorityQueue<>((a, b) -> compareBetterFirst(b, a));
    }

    /**
     * time = O(log n)
     * space = O(1) extra (O(n) overall)
     */
    public void add(String name, int score) {
        this.rest.add(new Location(name, score));

        // the new location may be better than the worst one already handed out:
        // then they swap piles, so handedOut still holds exactly the top i
        if (!this.handedOut.isEmpty()
                && compareBetterFirst(this.rest.peek(), this.handedOut.peek()) < 0) {
            Location better = this.rest.poll();
            Location worse = this.handedOut.poll();
            this.rest.add(worse);
            this.handedOut.add(better);
        }
    }

    /**
     * time = O(log n)
     * space = O(1)
     */
    public String get() {
        // the best of the rest is the (i)-th best overall
        Location next = this.rest.poll();
        this.handedOut.add(next);
        return next.name;
    }
}
