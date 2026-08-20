package LeetCodeJava.Design;

// https://leetcode.com/problems/implement-router/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  3508. Implement Router
 *  Medium
 *
 *  Design a data structure that can efficiently manage data packets in a network
 *  router. Each data packet consists of source, destination and timestamp.
 *
 *  Implement the Router class:
 *    Router(int memoryLimit) Initializes the Router object with a fixed memory
 *      limit, the maximum number of packets it can store at any given time. If
 *      adding a new packet would exceed this limit, the oldest packet must be
 *      removed to free up space.
 *    boolean addPacket(int source, int destination, int timestamp) Adds a packet.
 *      A packet is a duplicate if another packet with the same source, destination
 *      and timestamp already exists in the router. Return true if the packet was
 *      added (i.e. it is not a duplicate), otherwise false.
 *    int[] forwardPacket() Forwards the next packet in FIFO order, removes it from
 *      storage and returns it as [source, destination, timestamp]. If there are no
 *      packets to forward, return an empty array.
 *    int getCount(int destination, int startTime, int endTime) Returns the number
 *      of packets currently stored (i.e. not yet forwarded) with the given
 *      destination and a timestamp in the inclusive range [startTime, endTime].
 *
 *  Note that queries for addPacket will be made in non-decreasing order of timestamp.
 *
 *  Example 1:
 *    Input
 *      ["Router","addPacket","addPacket","addPacket","addPacket","addPacket",
 *       "forwardPacket","addPacket","getCount"]
 *      [[3],[1,4,90],[2,5,90],[1,4,90],[3,5,95],[4,5,105],[],[5,2,110],[5,100,110]]
 *    Output
 *      [null,true,true,false,true,true,[2,5,90],true,1]
 *    Explanation
 *      addPacket(1,4,90) / (2,5,90) -> true ; (1,4,90) again -> false (duplicate)
 *      addPacket(4,5,105) evicts [1,4,90] (memoryLimit = 3)
 *      forwardPacket() -> [2,5,90]
 *      getCount(5,100,110) -> 1  (only [4,5,105])
 *
 *  Example 2:
 *    Input
 *      ["Router","addPacket","forwardPacket","forwardPacket"]
 *      [[2],[7,4,90],[],[]]
 *    Output
 *      [null,true,[7,4,90],[]]
 *
 *  Constraints:
 *    2 <= memoryLimit <= 10^5
 *    1 <= source, destination <= 2 * 10^5
 *    1 <= timestamp <= 10^9
 *    1 <= startTime <= endTime <= 10^9
 *    At most 10^5 calls will be made to addPacket, forwardPacket and getCount.
 *    Queries for addPacket will be made in non-decreasing order of timestamp.
 */
public class ImplementRouter {

    // V0
    // IDEA: FIFO QUEUE + PER-DESTINATION TIMESTAMP LIST WITH A "CONSUMED" POINTER
    //
    //       two things must be fast at once: FIFO eviction, and counting the LIVE
    //       packets of one destination inside a timestamp range.
    //         - a deque gives the FIFO half (plus a hash set for duplicates).
    //         - for the range count, keep one timestamp list per destination.
    //       what makes those lists usable is the guarantee that addPacket arrives
    //       with NON-DECREASING timestamps: each list is therefore already sorted,
    //       so a range count is two binary searches. and because removal is strictly
    //       FIFO, a destination's packets also leave in arrival order -- the departed
    //       ones are exactly a PREFIX of its list, so one integer ("how many left")
    //       is enough to skip them; nothing ever has to be deleted from the middle.
    /**
     * time = O(1) per addPacket / forwardPacket, O(log n) per getCount
     * space = O(total number of added packets)
     */
    private final int memoryLimit;
    private final Deque<int[]> queue;                   // live packets, FIFO
    private final Set<String> live;                     // duplicate detection
    private final Map<Integer, List<Integer>> destTs;   // destination -> timestamps
    private final Map<Integer, Integer> consumed;       // destination -> #forwarded

    public ImplementRouter(int memoryLimit) {
        this.memoryLimit = memoryLimit;
        this.queue = new ArrayDeque<>();
        this.live = new HashSet<>();
        this.destTs = new HashMap<>();
        this.consumed = new HashMap<>();
    }

    public boolean addPacket(int source, int destination, int timestamp) {
        String key = key(source, destination, timestamp);
        if (live.contains(key)) {
            return false;
        }
        if (queue.size() == memoryLimit) {
            forwardPacket(); // evict the oldest
        }
        queue.addLast(new int[]{source, destination, timestamp});
        live.add(key);
        List<Integer> ts = destTs.get(destination);
        if (ts == null) {
            ts = new ArrayList<>();
            destTs.put(destination, ts);
        }
        ts.add(timestamp);
        return true;
    }

    public int[] forwardPacket() {
        if (queue.isEmpty()) {
            return new int[0];
        }
        int[] p = queue.pollFirst();
        live.remove(key(p[0], p[1], p[2]));
        Integer c = consumed.get(p[1]);
        consumed.put(p[1], (c == null ? 0 : c) + 1);
        return p;
    }

    public int getCount(int destination, int startTime, int endTime) {
        List<Integer> ts = destTs.get(destination);
        if (ts == null) {
            return 0;
        }
        Integer c = consumed.get(destination);
        int from = c == null ? 0 : c;
        int lo = lowerBound(ts, from, startTime);
        int hi = lowerBound(ts, from, endTime + 1);
        return hi - lo;
    }

    /** first index in [from, size) whose value >= target */
    private int lowerBound(List<Integer> ts, int from, int target) {
        int l = from;
        int r = ts.size();
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (ts.get(mid) < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    private String key(int source, int destination, int timestamp) {
        return source + "#" + destination + "#" + timestamp;
    }
}
