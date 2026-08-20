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
    // IDEA : TOPOLOGICAL SORT (Kahn's algo) + UNIQUENESS CHECK. LC 210
    /**
     *  NOTE !!!
     *
     *   the question asks whether the topological order is `UNIQUE`
     *   (and equals to `nums`), NOT whether `nums` is merely one of
     *   the valid orders.
     *
     *   -> so, during BFS, if the queue EVER holds more than 1 node,
     *      there are >= 2 valid orders  -> return false
     *
     *   other cases that must return false:
     *    1) a value in `sequences` is out of the [1, n] range
     *    2) some value in [1, n] NEVER shows up in `sequences`
     *       (e.g. nums = [1,2,3], sequences = [[1,2]] -> 3 is unknown)
     *    3) there is a cycle (-> we can NOT visit all n nodes)
     *    4) the produced order differs from `nums`
     *
     * time = O(V + E), V = nums.length, E = total len of sequences
     * space = O(V + E)
     */
    public boolean sequenceReconstruction(int[] nums, List<List<Integer>> sequences) {
        // edge
        if (nums == null || nums.length == 0) {
            return false;
        }
        if (sequences == null || sequences.isEmpty()) {
            return false;
        }

        int n = nums.length;

        // graph : value (1 ~ n) -> list of `next` values
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        int[] indegree = new int[n + 1];
        boolean[] seen = new boolean[n + 1];

        // build graph + indegree
        for (List<Integer> seq : sequences) {
            if (seq == null) {
                continue;
            }
            for (int i = 0; i < seq.size(); i++) {
                int cur = seq.get(i);
                /** NOTE !!! `out of range` value -> can NOT be reconstructed */
                if (cur < 1 || cur > n) {
                    return false;
                }
                seen[cur] = true;
                if (i > 0) {
                    int prev = seq.get(i - 1);
                    graph.get(prev).add(cur);
                    indegree[cur] += 1;
                }
            }
        }

        /** NOTE !!!
         *
         *  EVERY value in [1, n] must show up in `sequences`,
         *  otherwise the super sequence can NOT cover all of `nums`
         */
        for (int v = 1; v <= n; v++) {
            if (!seen[v]) {
                return false;
            }
        }

        // BFS (topological sort)
        Queue<Integer> q = new LinkedList<>();
        for (int v = 1; v <= n; v++) {
            if (indegree[v] == 0) {
                q.offer(v);
            }
        }

        int idx = 0;
        while (!q.isEmpty()) {
            /** NOTE !!! more than 1 `degree = 0` node -> order is NOT unique */
            if (q.size() > 1) {
                return false;
            }

            int cur = q.poll();

            // the unique order must match `nums`
            if (idx >= n || nums[idx] != cur) {
                return false;
            }
            idx += 1;

            for (int next : graph.get(cur)) {
                indegree[next] -= 1;
                if (indegree[next] == 0) {
                    q.offer(next);
                }
            }
        }

        // if `idx != n` -> there is a cycle (or a missing node)
        return idx == n;
    }


    // V0-1
    // IDEA: Kahn's Algorithm (Topological Sort) (gemini)
    /**
     * Logic:
     * 1. Build a directed graph where an edge u -> v exists if u appears before v in any sequence.
     * 2. Use Kahn's Algorithm (Topological Sort).
     * 3. Key constraint: To ensure uniqueness, the queue must always have EXACTLY one element.
     * 4. Verify the resulting order matches 'org'.
     *
     * Time Complexity: O(V + E), where V is num of elements and E is total length of sequences.
     * Space Complexity: O(V + E) for the adjacency list.
     */
    public boolean sequenceReconstruction_0_1(int[] org, List<List<Integer>> seqs) {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        // 1. Initialize maps with all elements found in seqs
        for (List<Integer> seq : seqs) {
            for (int val : seq) {
                adj.putIfAbsent(val, new ArrayList<>());
                inDegree.putIfAbsent(val, 0);
            }
        }

        // Validate that all elements in 'org' exist in the graph and vice-versa
        if (inDegree.size() != org.length) return false;

        // 2. Build the graph and calculate in-degrees
        for (List<Integer> seq : seqs) {
            for (int i = 0; i < seq.size() - 1; i++) {
                int u = seq.get(i);
                int v = seq.get(i + 1);
                adj.get(u).add(v);
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        // 3. Initialize queue with nodes having in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
            }
        }

        // 4. Perform Topological Sort
        int index = 0;
        while (!queue.isEmpty()) {
            // Uniqueness Check: If more than one node is available, the sequence isn't unique
            if (queue.size() > 1) return false;

            int curr = queue.poll();

            // Verify order matches org
            if (index >= org.length || curr != org[index]) return false;
            index++;

            for (int neighbor : adj.getOrDefault(curr, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Ensure all elements of org were processed
        return index == org.length;
    }


    // V0-2
    // IDEA: Kahn's Algorithm (Topological Sort)  (gpt)
    /**  IDEA:
     *
     * ✅ 解題思路（關鍵）
     * 	1.	將所有序列中的相鄰關係建成有向圖。
     * 	2.	計算每個節點的入度（indegree）。
     * 	3.	用 BFS 拓撲排序：
     * 	•	每一次 queue 裡只能有 唯一一個入度為 0 的節點，否則就表示不是唯一序列。
     * 	•	確保 BFS 過程生成的序列和 org 一致。
     * 	4.	如果整個排序合法且唯一，即返回 true，否則返回 false。
     */
    public boolean sequenceReconstruction_0_2(int[] org, List<List<Integer>> seqs) {
        int n = org.length;
        // 邊界條件
        if (seqs == null || seqs.isEmpty()) {
            return false;
        }

        // 建圖 + 計算 indegree
        List<List<Integer>> graph = new ArrayList<>();
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (List<Integer> seq : seqs) {
            for (int i = 1; i < seq.size(); ++i) {
                int a = seq.get(i - 1) - 1; // 轉為 0-base
                int b = seq.get(i) - 1;
                graph.get(a).add(b);
                indeg[b]++;
            }
        }

        // 拓撲排序 + 唯一性檢查
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                queue.offer(i);
            }
        }

        int idx = 0;
        while (!queue.isEmpty()) {
            // 有超過一個入度為 0 => 不唯一
            if (queue.size() > 1) {
                return false;
            }
            int u = queue.poll();

            // 與 org 序列比較
            if (org[idx] - 1 != u) {
                return false;
            }
            idx++;

            for (int v : graph.get(u)) {
                if (--indeg[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // 是否全部比對完
        return idx == n;
    }




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
    /**
     * time = O(V + E)
     * space = O(V)
     */
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
        /**
         *  NOTE !!!
         *
         *  a unique topological order is NOT enough: it also has to BE `nums`.
         *  Without the `i != nums[emitted] - 1` check below, this returns true for
         *  nums = [1,2,3], sequences = [[3,2,1]] - the order [3,2,1] is unique,
         *  but it is not nums, so the answer is false.
         *
         *  The `emitted != n` check at the end rejects a cycle, where the queue
         *  empties before every value has been placed.
         */
        int emitted = 0;
        while (!q.isEmpty()) {
            if (q.size() > 1) {
                return false;
            }
            int i = q.poll();
            if (i != nums[emitted] - 1) {
                return false;
            }
            emitted++;
            for (int j : g[i]) {
                if (--indeg[j] == 0) {
                    q.offer(j);
                }
            }
        }
        return emitted == n;
    }

    // V3
    // IDEA : topological sorting  (gpt)
    /**
     * time = O(V + E)
     * space = O(V)
     */
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
