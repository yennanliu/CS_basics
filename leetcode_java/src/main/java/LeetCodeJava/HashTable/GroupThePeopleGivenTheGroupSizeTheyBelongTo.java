package LeetCodeJava.HashTable;

// https://leetcode.com/problems/group-the-people-given-the-group-size-they-belong-to/description/

import java.util.*;

/**
 * 1282. Group the People Given the Group Size They Belong To
 * Medium
 *
 * There are n people that are split into some unknown number of groups.
 * Each person is labeled with a unique ID from 0 to n - 1.
 *
 * You are given an integer array groupSizes, where groupSizes[i] is the size of
 * the group that person i is in. For example, if groupSizes[1] = 3, then person 1
 * must be in a group of size 3.
 *
 * Return a list of groups such that each person i is in a group of size groupSizes[i].
 *
 * Each person should appear in exactly one group, and every person must be in a group.
 * If there are multiple answers, return any of them.
 * It is guaranteed that there will be at least one valid solution for the given input.
 *
 *
 * Example 1:
 *
 * Input: groupSizes = [3,3,3,3,3,1,3]
 * Output: [[5],[0,1,2],[3,4,6]]
 * Explanation:
 * The first group is [5]. The size is 1, and groupSizes[5] = 1.
 * The second group is [0,1,2]. The size is 3, and groupSizes[0] = groupSizes[1] = groupSizes[2] = 3.
 * The third group is [3,4,6]. The size is 3, and groupSizes[3] = groupSizes[4] = groupSizes[6] = 3.
 * Other possible solutions are [[2,1,6],[5],[0,4,3]] and [[5],[0,6,2],[4,3,1]].
 *
 * Example 2:
 *
 * Input: groupSizes = [2,1,3,3,3,2]
 * Output: [[1],[0,5],[2,3,4]]
 *
 *
 * Constraints:
 *
 * groupSizes.length == n
 * 1 <= n <= 500
 * 1 <= groupSizes[i] <= n
 *
 */
public class GroupThePeopleGivenTheGroupSizeTheyBelongTo {

    // V0
    // IDEA: GREEDY + HASH MAP (one open group per size; emit it the moment it is full)
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        List<List<Integer>> groups = new ArrayList<>();
        // size -> the group of that size currently being filled
        Map<Integer, List<Integer>> openGroup = new HashMap<>();

        for (int person = 0; person < groupSizes.length; person++) {
            int size = groupSizes[person];
            List<Integer> group = openGroup.computeIfAbsent(size, x -> new ArrayList<>());
            group.add(person);

            // full: emit it and start a fresh group for this size
            if (group.size() == size) {
                groups.add(group);
                openGroup.remove(size);
            }
        }
        return groups;
    }

    // V1
    // IDEA: BUCKET BY SIZE, THEN CUT EACH BUCKET INTO CHUNKS OF THAT SIZE
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<List<Integer>> groupThePeople_1(int[] groupSizes) {
        // size -> every person who needs a group of that size
        Map<Integer, List<Integer>> bucket = new HashMap<>();
        for (int person = 0; person < groupSizes.length; person++) {
            bucket.computeIfAbsent(groupSizes[person], x -> new ArrayList<>()).add(person);
        }

        // a valid answer is guaranteed, so each bucket holds a multiple of its size
        List<List<Integer>> groups = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : bucket.entrySet()) {
            int size = entry.getKey();
            List<Integer> people = entry.getValue();
            for (int start = 0; start < people.size(); start += size) {
                groups.add(new ArrayList<>(people.subList(start, start + size)));
            }
        }
        return groups;
    }
}
