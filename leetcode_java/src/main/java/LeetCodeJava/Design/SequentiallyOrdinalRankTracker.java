package LeetCodeJava.Design;

// https://leetcode.com/problems/sequentially-ordinal-rank-tracker/

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  2102. Sequentially Ordinal Rank Tracker
 *  Hard
 *
 *  A scenic location is represented by its name and attractiveness score. Locations
 *  are ranked best to worst: the higher the score the better, and if two scores are
 *  equal the location with the lexicographically smaller name is better.
 *
 *  You are building a system that tracks the ranking of locations, starting with no
 *  locations. It supports adding locations one at a time, and querying the ith best
 *  location of all locations already added, where i is the number of times the
 *  system has been queried (including the current query).
 *
 *  Implement the SORTracker class:
 *    SORTracker() Initializes the tracker system.
 *    void add(String name, int score) Adds a scenic location with name and score.
 *    String get() Queries and returns the ith best location, where i is the number
 *      of times this method has been invoked (including this invocation).
 *
 *  The test data guarantee that at any time the number of queries does not exceed
 *  the number of locations added.
 *
 *  Example 1:
 *    Input
 *      ["SORTracker","add","add","get","add","get","add","get","add","get","add",
 *       "get","get"]
 *      [[],["bradford",2],["branford",3],[],["alps",2],[],["orland",2],[],
 *       ["orlando",3],[],["alpine",2],[],[]]
 *    Output
 *      [null,null,null,"branford",null,"alps",null,"bradford",null,"bradford",
 *       null,"bradford","orland"]
 *    Explanation
 *      after 2 adds the ranking is [branford, bradford] -> 1st get -> "branford"
 *      adding "alps" -> [branford, alps, bradford]      -> 2nd get -> "alps"
 *      ... the i-th get returns the i-th best seen so far
 *
 *  Constraints:
 *    name consists of lowercase English letters and is unique among all locations.
 *    1 <= name.length <= 10
 *    1 <= score <= 10^5
 *    At most 4 * 10^4 calls in total will be made to add and get.
 */
public class SequentiallyOrdinalRankTracker {

    // V0
    // IDEA: TWO HEAPS STRADDLING THE ANSWER POSITION
    //
    //       the i-th get() asks for the i-th best location, and i only ever grows
    //       by one -- so keep the locations split into two piles:
    //         best  : the |best| best locations seen so far, kept as a MAX-heap on
    //                 "badness", i.e. its root is the WORST of the best
    //         rest  : everything else, kept as a MIN-heap on "badness", i.e. its
    //                 root is the BEST of the rest
    //
    //       get()  : move rest's root into best, then best's root IS the i-th best
    //                (it stays in `best`, since later queries ask for a later rank).
    //       add()  : push into rest; if its new root is better than best's root the
    //                two piles are out of order, so swap those two roots.
    //
    //       that keeps the invariant "best holds exactly the |best| best items"
    //       with O(log n) per operation, instead of re-sorting on every query.
    /**
     * time = O(log n) per add / get
     * space = O(n)
     */
    private static class Loc {
        final String name;
        final int score;

        Loc(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    /** negative when a is BETTER than b: higher score first, then smaller name */
    private static final Comparator<Loc> BEST_FIRST = new Comparator<Loc>() {
        @Override
        public int compare(Loc a, Loc b) {
            if (a.score != b.score) {
                return b.score - a.score;
            }
            return a.name.compareTo(b.name);
        }
    };

    private final PriorityQueue<Loc> best; // worst-of-the-best on top
    private final PriorityQueue<Loc> rest; // best-of-the-rest on top

    public SequentiallyOrdinalRankTracker() {
        this.best = new PriorityQueue<>(new Comparator<Loc>() {
            @Override
            public int compare(Loc a, Loc b) {
                return BEST_FIRST.compare(b, a);
            }
        });
        this.rest = new PriorityQueue<>(BEST_FIRST);
    }

    public void add(String name, int score) {
        Loc loc = new Loc(name, score);
        rest.offer(loc);
        // if the best of `rest` beats the worst of `best`, swap them
        if (!best.isEmpty() && BEST_FIRST.compare(rest.peek(), best.peek()) < 0) {
            Loc up = rest.poll();
            Loc down = best.poll();
            best.offer(up);
            rest.offer(down);
        }
    }

    public String get() {
        // the i-th get() promotes one more element, then reads the new boundary
        best.offer(rest.poll());
        return best.peek().name;
    }
}
