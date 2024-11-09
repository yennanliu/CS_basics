package LeetCodeJava.BFS;

// https://leetcode.com/problems/sequence-reconstruction/description/
// https://leetcode.ca/all/444.html

import java.util.*;

/**
 * 444. Sequence Reconstruction
 * Check whether the original sequence org can be uniquely reconstructed from the sequences in seqs. The org sequence is a permutation of the integers from 1 to n, with 1 ≤ n ≤ 104. Reconstruction means building a shortest common supersequence of the sequences in seqs (i.e., a shortest sequence so that all sequences in seqs are subsequences of it). Determine whether there is only one sequence that can be reconstructed from seqs and it is the org sequence.
 *
 * Example 1:
 *
 * Input:
 * org: [1,2,3], seqs: [[1,2],[1,3]]
 *
 * Output:
 * false
 *
 * Explanation:
 * [1,2,3] is not the only one sequence that can be reconstructed, because [1,3,2] is also a valid sequence that can be reconstructed.
 * Example 2:
 *
 * Input:
 * org: [1,2,3], seqs: [[1,2]]
 *
 * Output:
 * false
 *
 * Explanation:
 * The reconstructed sequence can only be [1,2].
 * Example 3:
 *
 * Input:
 * org: [1,2,3], seqs: [[1,2],[1,3],[2,3]]
 *
 * Output:
 * true
 *
 * Explanation:
 * The sequences [1,2], [1,3], and [2,3] can uniquely reconstruct the original sequence [1,2,3].
 * Example 4:
 *
 * Input:
 * org: [4,1,5,2,6,3], seqs: [[5,2,6,3],[4,1,5,2]]
 *
 * Output:
 * true
 * UPDATE (2017/1/8):
 * The seqs parameter had been changed to a list of list of strings (instead of a 2d array of strings). Please reload the code definition to get the latest changes.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Google
 *
 */
public class SequenceReconstruction {

    // V0
    // TODO : implement
//    public boolean sequenceReconstruction(int[] nums, List<List<Integer>> sequences) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=FHY1q1h9gq0
    // https://www.jiakaobo.com/leetcode/444.%20Sequence%20Reconstruction.html
    Map<Integer, Set<Integer>> map;
    Map<Integer, Integer> indegree;

    public boolean sequenceReconstruction_1(int[] nums, List<List<Integer>> sequences) {
        map = new HashMap<>();
        indegree = new HashMap<>();

        for(List<Integer> seq: sequences) {
            if(seq.size() == 1) {
                addNode(seq.get(0));
            } else {
                for(int i = 0; i < seq.size() - 1; i++) {
                    addNode(seq.get(i));
                    addNode(seq.get(i + 1));

                    // 加入子节点, 子节点增加一个入度
                    // [1,2] => 1 -> 2
                    // 1: [2]
                    int curr = seq.get(i);
                    int next = seq.get(i + 1);
                    if(map.get(curr).add(next)) {
                        indegree.put(next, indegree.get(next) + 1);
                    }
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for(int key : indegree.keySet()) {
            if(indegree.get(key) == 0){
                queue.offer(key);
            }
        }

        int index = 0;
        while(!queue.isEmpty()) {
            // 如果只有唯一解, 那么queue的大小永远都是1
            if(queue.size() != 1) return false;

            int curr = queue.poll();
            if(curr != nums[index++]) return false;

            for(int next: map.get(curr)) {
                indegree.put(next, indegree.get(next) - 1);
                if(indegree.get(next) == 0) {
                    queue.offer(next);
                }
            }
        }

        return index == nums.length;
    }

    private void addNode(int node) {
        if(!map.containsKey(node)) {
            map.put(node, new HashSet<>());
            indegree.put(node, 0);
        }
    }

    // V2
    // https://leetcode.ca/2017-02-16-444-Sequence-Reconstruction/
    public boolean sequenceReconstruction_2(int[] nums, List<List<Integer>> sequences) {
        int n = nums.length;
        int[] indeg = new int[n];
        List<Integer>[] g = new List[n];
        Arrays.setAll(g, k -> new ArrayList<>());
        for (List<Integer> seq : sequences) {
            for (int i = 1; i < seq.size(); ++i) {
                int a = seq.get(i - 1) - 1, b = seq.get(i) - 1;
                g[a].add(b);
                indeg[b]++;
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; ++i) {
            if (indeg[i] == 0) {
                q.offer(i);
            }
        }
        while (!q.isEmpty()) {
            if (q.size() > 1) {
                return false;
            }
            int i = q.poll();
            for (int j : g[i]) {
                if (--indeg[j] == 0) {
                    q.offer(j);
                }
            }
        }
        return true;
    }

    // V3
    // IDEA : topological sorting  (gpt)
    // TODO : validate
    public boolean sequenceReconstruction_3(int[] org, List<List<Integer>> seqs) {
        int n = org.length;

        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        for (int i = 1; i <= n; i++) {
            graph.put(i, new ArrayList<>());
            inDegree.put(i, 0);
        }

        int count = 0; // Count valid nodes in seqs
        for (List<Integer> seq : seqs) {
            count += seq.size();
            for (int i = 0; i < seq.size(); i++) {
                if (seq.get(i) < 1 || seq.get(i) > n) {
                    return false; // Invalid element in seqs
                }
                if (i > 0) {
                    int prev = seq.get(i - 1), next = seq.get(i);
                    graph.get(prev).add(next);
                    inDegree.put(next, inDegree.get(next) + 1);
                }
            }
        }

        // If seqs is empty or does not include enough information
        if (count < n) {
            return false;
        }

        // Step 2: Topological Sort using BFS
        Queue<Integer> queue = new LinkedList<>();
        for (int key : inDegree.keySet()) {
            if (inDegree.get(key) == 0) {
                queue.offer(key);
            }
        }

        int index = 0;
        while (!queue.isEmpty()) {
            if (queue.size() > 1) {
                return false; // More than one way to reconstruct
            }

            int current = queue.poll();
            if (index == n || org[index] != current) {
                return false; // Current number does not match org
            }
            index++;

            for (int neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Check if we used all numbers in org
        return index == n;
    }

    // V4
    // https://blog.csdn.net/qq_46105170/article/details/105727262
}
