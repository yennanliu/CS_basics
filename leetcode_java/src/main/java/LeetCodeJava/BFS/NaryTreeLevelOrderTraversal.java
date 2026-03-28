package LeetCodeJava.BFS;

// https://leetcode.com/problems/n-ary-tree-level-order-traversal/description/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 Code
 Testcase
 Testcase
 Test Result
 429. N-ary Tree Level Order Traversal
 Solved
 Medium
 Topics
 premium lock icon
 Companies
 Given an n-ary tree, return the level order traversal of its nodes' values.

 Nary-Tree input serialization is represented in their level order traversal, each group of children is separated by the null value (See examples).



 Example 1:



 Input: root = [1,null,3,2,4,null,5,6]
 Output: [[1],[3,2,4],[5,6]]
 Example 2:



 Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 Output: [[1],[2,3,4,5],[6,7,8,9,10],[11,12,13],[14]]


 Constraints:

 The height of the n-ary tree is less than or equal to 1000
 The total number of nodes is between [0, 104]
 *
 */
public class NaryTreeLevelOrderTraversal {

    class Node {
        public int val;
        public List<Node> children;
        public Node() {}
        public Node(int _val) { val = _val; }
        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    };


    // V0
    // IDEA: BFS (gemini)
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> result = new ArrayList<>();

        // 1. 邊界檢查
        if (root == null)
            return result;

        // 2. 初始化 Queue
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            // 3. 獲取當前層的節點數量
            int size = queue.size();
            List<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                Node curr = queue.poll();
                currentLevel.add(curr.val);

                // 4. 將所有子節點加入 Queue (與二元樹唯一的差別點)
                if (curr.children != null) {
                    for (Node child : curr.children) {
                        queue.offer(child);
                    }
                }
            }
            // 5. 將這一層的結果加入最終清單
            result.add(currentLevel);
        }

        return result;
    }

    // V1
    // https://leetcode.com/problems/n-ary-tree-level-order-traversal/solutions/2532193/java-easy-solution-using-arraylist-90-fa-z4zq/
    public List<List<Integer>> levelOrder_1(Node root) {
        List<List<Integer>> nm = new ArrayList<List<Integer>>();
        if (root == null)
            return nm;
        Queue<Node> kk = new LinkedList<>();
        kk.offer(root);
        while (!kk.isEmpty()) {
            int p = kk.size();
            ArrayList<Integer> k = new ArrayList<>();
            for (int i = 0; i < p; i++) {
                k.add(kk.peek().val);
                for (Node f : kk.peek().children) {
                    kk.offer(f);
                }
                kk.poll();
            }
            nm.add(new ArrayList<>(k));
        }
        return nm;
    }

    // V2
    // https://leetcode.com/problems/n-ary-tree-level-order-traversal/solutions/2532094/easy-solution-bfs-java-video-explainatio-9j3k/
    public List<List<Integer>> levelOrder_2(Node root) {

        List<List<Integer>> output = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();

        if (root == null)
            return output;
        queue.add(root);

        while (!queue.isEmpty()) {

            // stores the number of values in current row (we alter amount in queue later)
            int rowLen = queue.size();
            List<Integer> currRow = new ArrayList<>();
            for (int i = 0; i < rowLen; i++) {

                // Add current value into row list values.
                Node curr = queue.poll();
                currRow.add(curr.val);

                // Add Children of current node into queue.
                int numChildren = curr.children.size();
                for (int c = 0; c < numChildren; c++) {
                    if (curr.children.get(c) != null)
                        queue.add(curr.children.get(c));
                }
            }
            output.add(currRow);
        }
        return output;
    }





}
