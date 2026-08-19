package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/online-election/

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *  911. Online Election
 *  Medium
 *
 *  You are given two integer arrays persons and times. In an election, the ith
 *  vote was cast for persons[i] at time times[i].
 *
 *  For each query at a time t, find the person that was leading the election at
 *  time t. Votes cast at time t count towards the query. In the case of a tie,
 *  the most recent leading candidate wins.
 *
 *  Implement the TopVotedCandidate class:
 *
 *  - TopVotedCandidate(int[] persons, int[] times) Initializes the object with
 *    the persons and times arrays.
 *  - int q(int t) Returns the number of the person that was leading the election
 *    at time t according to the mentioned rules.
 *
 *  Example 1:
 *
 *  Input
 *  ["TopVotedCandidate", "q", "q", "q", "q", "q", "q"]
 *  [[[0, 1, 1, 0, 0, 1, 0], [0, 5, 10, 15, 20, 25, 30]], [3], [12], [25], [15], [24], [8]]
 *  Output
 *  [null, 0, 1, 1, 0, 0, 1]
 *
 *  Constraints:
 *
 *  1 <= persons.length <= 5000
 *  times.length == persons.length
 *  0 <= persons[i] < persons.length
 *  0 <= times[i] <= 10^9
 *  times is sorted in a strictly increasing order.
 *  times[0] <= t <= 10^9
 *  At most 10^4 calls will be made to q.
 */
public class OnlineElection {

    private final int[] times;
    private final int[] leads;

    // V0
    // IDEA: precompute the leader after every vote, then binary search the last
    //       vote time <= t on a q() call
    /**
     * time = O(n) ctor, O(log n) per q()
     * space = O(n)
     */
    public OnlineElection(int[] persons, int[] times) {
        this.times = times;
        int n = persons.length;
        this.leads = new int[n];
        Map<Integer, Integer> count = new HashMap<>();
        int lead = -1;
        for (int i = 0; i < n; i++) {
            int p = persons[i];
            int c = count.getOrDefault(p, 0) + 1;
            count.put(p, c);
            // NOTE !!! ">=" so the most recent candidate wins on a tie
            if (c >= count.getOrDefault(lead, 0)) {
                lead = p;
            }
            this.leads[i] = lead;
        }
    }

    public int q(int t) {
        // find the last idx with times[idx] <= t
        int l = 0;
        int r = this.times.length - 1;
        int res = 0;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (this.times[mid] <= t) {
                res = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return this.leads[res];
    }

    // V1
    // IDEA: same precomputed leader-after-each-vote idea, but the "last vote time
    //       <= t" lookup is delegated to a TreeMap floorEntry instead of a
    //       hand-rolled binary search over the times array.
    /**
     * time = O(n log n) ctor, O(log n) per q()
     * space = O(n)
     */
    public static class OnlineElectionV1 {

        private final TreeMap<Integer, Integer> timeToLead = new TreeMap<>();

        public OnlineElectionV1(int[] persons, int[] times) {
            Map<Integer, Integer> count = new HashMap<>();
            int lead = -1;
            for (int i = 0; i < persons.length; i++) {
                int p = persons[i];
                int c = count.getOrDefault(p, 0) + 1;
                count.put(p, c);
                if (c >= count.getOrDefault(lead, 0)) {
                    lead = p;
                }
                timeToLead.put(times[i], lead);
            }
        }

        public int q(int t) {
            return timeToLead.floorEntry(t).getValue();
        }
    }

    // V2
    // IDEA: brute force -- no precomputation at all, every q() replays the votes
    //       up to time t and recounts. Kept as a readable correctness reference.
    /**
     * time = O(1) ctor, O(n) per q()
     * space = O(n) for the recount map
     */
    public static class OnlineElectionV2 {

        private final int[] persons;
        private final int[] times;

        public OnlineElectionV2(int[] persons, int[] times) {
            this.persons = persons;
            this.times = times;
        }

        public int q(int t) {
            Map<Integer, Integer> count = new HashMap<>();
            int lead = -1;
            for (int i = 0; i < this.times.length && this.times[i] <= t; i++) {
                int p = this.persons[i];
                int c = count.getOrDefault(p, 0) + 1;
                count.put(p, c);
                if (c >= count.getOrDefault(lead, 0)) {
                    lead = p;
                }
            }
            return lead;
        }
    }
}
