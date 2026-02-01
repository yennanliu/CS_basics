package LeetCodeJava.Recursion;

// https://leetcode.com/problems/find-mode-in-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 501. Find Mode in Binary Search Tree
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary search tree (BST) with duplicates, return all the mode(s) (i.e., the most frequently occurred element) in it.
 *
 * If the tree has more than one mode, return them in any order.
 *
 * Assume a BST is defined as follows:
 *
 * The left subtree of a node contains only nodes with keys less than or equal to the node's key.
 * The right subtree of a node contains only nodes with keys greater than or equal to the node's key.
 * Both the left and right subtrees must also be binary search trees.
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,null,2,2]
 * Output: [2]
 * Example 2:
 *
 * Input: root = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -105 <= Node.val <= 105
 *
 *
 * Follow up: Could you do that without using any extra space? (Assume that the implicit stack space incurred due to recursion does not count).
 *
 *
 */
public class FindModeInBinarySearchTree {

    // V0
    // IDEA: DFS + BST
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode(TreeNode root) {
        if (root == null)
            return new int[0];

        Map<Integer, Integer> node_cnt = new HashMap<>();
        nodeHelper(root, node_cnt);

        int maxFreq = 0;
        for (int freq : node_cnt.values()) {
            maxFreq = Math.max(maxFreq, freq);
        }

        List<Integer> modes = new ArrayList<>();
        /**
         *  NOTE !!! we use `Entry`
         *           to access both map's key and value
         */
        for (Map.Entry<Integer, Integer> entry : node_cnt.entrySet()) {
            if (entry.getValue() == maxFreq) {
                modes.add(entry.getKey());
            }
        }

        int[] result = new int[modes.size()];
        for (int i = 0; i < modes.size(); i++) {
            result[i] = modes.get(i);
        }
        return result;
    }

    private void nodeHelper(TreeNode node, Map<Integer, Integer> freqMap) {
        if (node == null)
            return;
        freqMap.put(node.val, freqMap.getOrDefault(node.val, 0) + 1);
        nodeHelper(node.left, freqMap);
        nodeHelper(node.right, freqMap);
    }

    // V0-1
    // IDEA: HASHMAP + DFS BST
    Map<Integer, Integer> map = new HashMap<>();
    int topCnt = 0;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_0_1(TreeNode root) {
        // edge
        if (root == null) {
            return new int[]{};
        }

        // Step 1: DFS to count frequencies
        getNodes(root);

        // Step 2: Collect all keys with frequency == topCnt
        List<Integer> list = new ArrayList<>();
        for (Integer x : map.keySet()) {
            if (map.get(x) == topCnt) {
                list.add(x);
            }
        }

        // Step 3: Convert List -> int[]
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    private void getNodes(TreeNode root) {
        if (root == null) {
            return;
        }

        // update frequency map
        int count = map.getOrDefault(root.val, 0) + 1;
        map.put(root.val, count);

        // update max frequency
        topCnt = Math.max(topCnt, count);

        // dfs
        getNodes(root.left);
        getNodes(root.right);
    }


    // V1-1
    // https://leetcode.com/problems/find-mode-in-binary-search-tree/editorial/
    // IDEA: Count Frequency With Hash Map (DFS)
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_1_1(TreeNode root) {
        Map<Integer, Integer> counter = new HashMap();
        dfs(root, counter);
        int maxFreq = 0;

        for (int key : counter.keySet()) {
            maxFreq = Math.max(maxFreq, counter.get(key));
        }

        List<Integer> ans = new ArrayList();
        for (int key : counter.keySet()) {
            if (counter.get(key) == maxFreq) {
                ans.add(key);
            }
        }

        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }

    /**
     * time = O(V + E)
     * space = O(V)
     */
    public void dfs(TreeNode node, Map<Integer, Integer> counter) {
        if (node == null) {
            return;
        }

        counter.put(node.val, counter.getOrDefault(node.val, 0) + 1);
        dfs(node.left, counter);
        dfs(node.right, counter);
    }


    // V1-2
    // https://leetcode.com/problems/find-mode-in-binary-search-tree/editorial/
    // IDEA: Iterative DFS
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_1_2(TreeNode root) {
        Map<Integer, Integer> counter = new HashMap();
        Stack<TreeNode> stack = new Stack();
        stack.push(root);

        while (!stack.empty()) {
            TreeNode node = stack.pop();

            counter.put(node.val, counter.getOrDefault(node.val, 0) + 1);
            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }

        int maxFreq = 0;
        for (int key : counter.keySet()) {
            maxFreq = Math.max(maxFreq, counter.get(key));
        }

        List<Integer> ans = new ArrayList();
        for (int key : counter.keySet()) {
            if (counter.get(key) == maxFreq) {
                ans.add(key);
            }
        }

        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }


    // V1-3
    // https://leetcode.com/problems/find-mode-in-binary-search-tree/editorial/
    // IDEA: BFS
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_1_3(TreeNode root) {
        Map<Integer, Integer> counter = new HashMap();
        Queue<TreeNode> queue = new LinkedList();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.remove();

            counter.put(node.val, counter.getOrDefault(node.val, 0) + 1);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }

        int maxFreq = 0;
        for (int key : counter.keySet()) {
            maxFreq = Math.max(maxFreq, counter.get(key));
        }

        List<Integer> ans = new ArrayList();
        for (int key : counter.keySet()) {
            if (counter.get(key) == maxFreq) {
                ans.add(key);
            }
        }

        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }


    // V1-4
    // https://leetcode.com/problems/find-mode-in-binary-search-tree/editorial/
    // IDEA: No Hash-Map
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_1_4(TreeNode root) {
        List<Integer> values = new ArrayList();
        dfs(root, values);

        int maxStreak = 0;
        int currStreak = 0;
        int currNum = 0;
        List<Integer> ans = new ArrayList();

        for (int num : values) {
            if (num == currNum) {
                currStreak++;
            } else {
                currStreak = 1;
                currNum = num;
            }

            if (currStreak > maxStreak) {
                ans = new ArrayList();
                maxStreak = currStreak;
            }

            if (currStreak == maxStreak) {
                ans.add(num);
            }
        }

        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }

    /**
     * time = O(V + E)
     * space = O(V)
     */
    public void dfs(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }

        // Inorder traversal visits nodes in sorted order
        dfs(node.left, values);
        values.add(node.val);
        dfs(node.right, values);
    }


    // V1-5
    // https://leetcode.com/problems/find-mode-in-binary-search-tree/editorial/
    // IDEA: No "Values" Array
    int maxStreak = 0;
    int currStreak = 0;
    int currNum = 0;
    List<Integer> ans = new ArrayList();

    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_1_5(TreeNode root) {
        dfs(root);

        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }

    /**
     * time = O(V + E)
     * space = O(V)
     */
    public void dfs(TreeNode node) {
        if (node == null) {
            return;
        }

        dfs(node.left);
        int num = node.val;
        if (num == currNum) {
            currStreak++;
        } else {
            currStreak = 1;
            currNum = num;
        }

        if (currStreak > maxStreak) {
            ans = new ArrayList();
            maxStreak = currStreak;
        }

        if (currStreak == maxStreak) {
            ans.add(num);
        }

        dfs(node.right);
    }


    // V1-6
    // https://leetcode.com/problems/find-mode-in-binary-search-tree/editorial/
    // IDEA: True Constant Space: Morris Traversal
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findMode_1_6(TreeNode root) {
        int maxStreak = 0;
        int currStreak = 0;
        int currNum = 0;
        List<Integer> ans = new ArrayList();

        TreeNode curr = root;
        while (curr != null) {
            if (curr.left != null) {
                // Find the friend
                TreeNode friend = curr.left;
                while (friend.right != null) {
                    friend = friend.right;
                }

                friend.right = curr;

                // Delete the edge after using it
                TreeNode left = curr.left;
                curr.left = null;
                curr = left;
            } else {
                // Handle the current node
                int num = curr.val;
                if (num == currNum) {
                    currStreak++;
                } else {
                    currStreak = 1;
                    currNum = num;
                }

                if (currStreak > maxStreak) {
                    ans = new ArrayList();
                    maxStreak = currStreak;
                }

                if (currStreak == maxStreak) {
                    ans.add(num);
                }

                curr = curr.right;
            }
        }

        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }

        return result;
    }

    // V2

    // V3


}
