package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-time-required-to-eliminate-bacterial-strains/description/
// https://leetcode.ca/2025-04-29-3506-Find-Time-Required-to-Eliminate-Bacterial-Strains/

import java.util.*;

/**
 * 3506. Find Time Required to Eliminate Bacterial Strains
 * Hard
 *
 * You are given an integer array timeReq and an integer splitTime.
 *
 * In the microscopic world of the human body, the immune system faces an
 * extraordinary challenge: combatting a rapidly multiplying bacterial colony that
 * threatens the body's survival.
 *
 * Initially, only one white blood cell (WBC) is deployed to eliminate the
 * bacteria. However, the lone WBC quickly realizes it cannot keep up with the
 * bacterial growth rate.
 *
 * The WBC devises a clever strategy to fight the bacteria:
 *
 * The i^th bacterial strain takes timeReq[i] units of time to be eliminated.
 *
 * A single WBC can eliminate only one bacterial strain. Afterwards, the WBC is
 * exhausted and cannot perform any other tasks.
 *
 * A WBC can split itself into two WBCs, but this requires splitTime units of time.
 * Once split, the two WBCs can work in parallel on eliminating the bacteria.
 *
 * Only one WBC can work on a single bacterial strain. Multiple WBCs cannot attack
 * one strain in parallel.
 *
 * You must determine the minimum time required to eliminate all the bacterial
 * strains.
 *
 * Note that the bacterial strains can be eliminated in any order.
 *
 * Example 1:
 *
 * Input: timeReq = [10,4,5], splitTime = 2
 *
 * Output: 12
 *
 * Explanation:
 *
 * The elimination process goes as follows:
 *
 * Initially, there is a single WBC. The WBC splits into 2 WBCs after 2 units of
 * time.
 *
 * One of the WBCs eliminates strain 0 at a time t = 2 + 10 = 12. The other WBC
 * splits again, using 2 units of time.
 *
 * The 2 new WBCs eliminate the bacteria at times t = 2 + 2 + 4 and t = 2 + 2 + 5.
 *
 * Example 2:
 *
 * Input: timeReq = [10,4], splitTime = 5
 *
 * Output:15
 *
 * Explanation:
 *
 * The elimination process goes as follows:
 *
 * Initially, there is a single WBC. The WBC splits into 2 WBCs after 5 units of
 * time.
 *
 * The 2 new WBCs eliminate the bacteria at times t = 5 + 10 and t = 5 + 4.
 *
 * Constraints:
 *
 * 2 <= timeReq.length <= 10^5
 *
 * 1 <= timeReq[i] <= 10^9
 *
 * 1 <= splitTime <= 10^9
 *
 */
public class FindTimeRequiredToEliminateBacterialStrains {

    // V0
    // IDEA: REVERSE SPLITS INTO MERGES + MIN-HEAP (HUFFMAN-LIKE GREEDY)
    //       splits form a binary tree with the strains as leaves; a leaf at depth d
    //       finishes at d * splitTime + timeReq[i]. reading it backwards, merging two
    //       subtrees costs splitTime + max(their finishing times).
    //       always merge the two smallest: the slow strains stay shallow.
    //       the popped pair contributes only its max, so push (second smallest + splitTime).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long minEliminationTime(int[] timeReq, int splitTime) {
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        for (int t : timeReq) {
            minHeap.offer((long) t);
        }

        while (minHeap.size() > 1) {
            minHeap.poll();                      // the smaller one is absorbed by the merge
            long larger = minHeap.poll();
            minHeap.offer(larger + splitTime);   // one more split above both
        }
        return minHeap.peek();
    }
}
