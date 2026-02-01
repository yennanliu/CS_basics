package LeetCodeJava.Heap;

// https://leetcode.com/problems/relative-ranks/description/

import DataStructure.Pair;

import java.util.*;

/**
 * 506. Relative Ranks
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer array score of size n, where score[i] is the score of the ith athlete in a competition. All the scores are guaranteed to be unique.
 *
 * The athletes are placed based on their scores, where the 1st place athlete has the highest score, the 2nd place athlete has the 2nd highest score, and so on. The placement of each athlete determines their rank:
 *
 * The 1st place athlete's rank is "Gold Medal".
 * The 2nd place athlete's rank is "Silver Medal".
 * The 3rd place athlete's rank is "Bronze Medal".
 * For the 4th place to the nth place athlete, their rank is their placement number (i.e., the xth place athlete's rank is "x").
 * Return an array answer of size n where answer[i] is the rank of the ith athlete.
 *
 *
 *
 * Example 1:
 *
 * Input: score = [5,4,3,2,1]
 * Output: ["Gold Medal","Silver Medal","Bronze Medal","4","5"]
 * Explanation: The placements are [1st, 2nd, 3rd, 4th, 5th].
 * Example 2:
 *
 * Input: score = [10,3,8,9,4]
 * Output: ["Gold Medal","5","Bronze Medal","Silver Medal","4"]
 * Explanation: The placements are [1st, 5th, 3rd, 2nd, 4th].
 *
 *
 *
 * Constraints:
 *
 * n == score.length
 * 1 <= n <= 104
 * 0 <= score[i] <= 106
 * All the values in score are unique.
 *
 *
 *
 */
public class RelativeRanks {

    // LC 506
//    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String[] findRelativeRanks(int[] score) {
//
//    }

    // V0-1
    // IDEA: PQ
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String[] findRelativeRanks_0_1(int[] score) {
        // edge
        if(score.length == 1){
            return new String[]{"Gold Medal"};
        }
        String GOLD = "Gold Medal";
        String SILVER = "Silver Medal";
        String BRONE = "Bronze Medal";

        // PQ: big PQ
        // NOTE !!! PQ needs to record { idx, val }
        // PQ: { idx, val }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[1] - o1[1];
                return diff;
            }
        });

        for(int i = 0; i < score.length; i++){
            int s = score[i];
            pq.add(new Integer[] {i, s});
        }

        String[] res = new String[pq.size()];
        int cnt = 0;
        while(!pq.isEmpty()){

            Integer[] cur = pq.poll();
            int idx = cur[0];
            int val = cur[1];

            if(cnt == 0){
                res[idx] = GOLD;
            }
            else if(cnt == 1){
                res[idx] = SILVER;
            }
            else if(cnt == 2){
                res[idx] = BRONE;
            }
            else{
                res[idx] = String.valueOf(cnt + 1);
            }
            cnt += 1;
        }


        return res;
    }


    // V1-1
    // IDEA: Sort & Reverse
    // https://leetcode.com/problems/relative-ranks/editorial/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String[] findRelativeRanks_1_1(int[] score) {
        int N = score.length;
        int[] scoreCopy = new int[N];
        System.arraycopy(score, 0, scoreCopy, 0, N);

        // Save the index of each athlete
        Map<Integer, Integer> scoreToIndex = new HashMap<>();
        for (int i = 0; i < N; i++) {
            scoreToIndex.put(scoreCopy[i], i);
        }

        // Sort scoreCopy in ascending order
        Arrays.sort(scoreCopy);

        // Assign ranks to athletes
        // Traverse scoreCopy in reverse
        String[] rank = new String[N];
        for (int i = 0; i < N; i++) {
            if (i == 0) {
                rank[scoreToIndex.get(scoreCopy[N - i - 1])] = "Gold Medal";
            } else if (i == 1) {
                rank[scoreToIndex.get(scoreCopy[N - i - 1])] = "Silver Medal";
            } else if (i == 2) {
                rank[scoreToIndex.get(scoreCopy[N - i - 1])] = "Bronze Medal";
            } else {
                rank[scoreToIndex.get(scoreCopy[N - i - 1])] = Integer.toString(i + 1);
            }
        }

        return rank;
    }


    // V1-2
    // IDEA: Heap (Priority Queue)
    // https://leetcode.com/problems/relative-ranks/editorial/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String[] findRelativeRanks_1_2(int[] score) {
        int N = score.length;

        // Create a max heap of pairs (score, index)
        PriorityQueue<Pair<Integer, Integer>> heap = new PriorityQueue<>(
                (a, b) -> b.getKey() - a.getKey());
        for (int i = 0; i < N; i++) {
            heap.add(new Pair<>(score[i], i));
        }

        // Assign ranks to athletes
        String[] rank = new String[N];
        int place = 1;
        while (!heap.isEmpty()) {
            Pair<Integer, Integer> pair = heap.poll();
            int originalIndex = pair.getValue();
            if (place == 1) {
                rank[originalIndex] = "Gold Medal";
            } else if (place == 2) {
                rank[originalIndex] = "Silver Medal";
            } else if (place == 3) {
                rank[originalIndex] = "Bronze Medal";
            } else {
                rank[originalIndex] = String.valueOf(place);
            }
            place++;
        }
        return rank;
    }


    // V1-3
    // IDEA: Array as Map
    // https://leetcode.com/problems/relative-ranks/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    private int findMax(int[] score) {
        int maxScore = 0;
        for (int s : score) {
            if (s > maxScore) {
                maxScore = s;
            }
        }
        return maxScore;
    }

    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String[] findRelativeRanks_1_3(int[] score) {
        int N = score.length;

        // Add the original index of each score to the array
        // Where the score is the key
        int M = findMax(score);
        int[] scoreToIndex = new int[M + 1];
        for (int i = 0; i < N; i++) {
            scoreToIndex[score[i]] = i + 1;
        }

        final String[] MEDALS = {"Gold Medal", "Silver Medal", "Bronze Medal"};

        // Assign ranks to athletes
        String[] rank = new String[N];
        int place = 1;
        for (int i = M; i >= 0; i--) {
            if (scoreToIndex[i] != 0) {
                int originalIndex = scoreToIndex[i] - 1;
                if (place < 4) {
                    rank[originalIndex] = MEDALS[place - 1];
                } else {
                    rank[originalIndex] = String.valueOf(place);
                }
                place++;
            }
        }
        return rank;
    }


    // V2


}
