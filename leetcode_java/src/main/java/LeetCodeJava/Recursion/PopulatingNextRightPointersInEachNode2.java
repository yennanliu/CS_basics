package LeetCodeJava.Recursion;

// https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 Code
 Testcase
 Test Result
 Test Result
 117. Populating Next Right Pointers in Each Node II
 Solved
 Medium
 Topics
 Companies
 Given a binary tree

 struct Node {
 int val;
 Node *left;
 Node *right;
 Node *next;
 }
 Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.

 Initially, all next pointers are set to NULL.



 Example 1:


 Input: root = [1,2,3,4,5,null,7]
 Output: [1,#,2,3,#,4,5,7,#]
 Explanation: Given the above binary tree (Figure A), your function should populate each next pointer to point to its next right node, just like in Figure B. The serialized output is in level order as connected by the next pointers, with '#' signifying the end of each level.
 Example 2:

 Input: root = []
 Output: []


 Constraints:

 The number of nodes in the tree is in the range [0, 6000].
 -100 <= Node.val <= 100


 Follow-up:

 You may only use constant extra space.
 The recursive approach is fine. You may assume implicit stack space does not count as extra space for this problem.

 *
 *
 */
public class PopulatingNextRightPointersInEachNode2 {

    /*
    // Definition for a Node.
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    };
    */

    // TODO: move to dataStructure package
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    };

    // V0
//    public Node connect(Node root) {
//
//    }

    // V1
    // https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/solutions/5449615/my-easy-accepted-java-solution-100-by-di-nz2w/
    public Node connect_1(Node root) {
        if (root == null)
            return null;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            Node prev = null;

            for (int i = 0; i < levelSize; i++) {
                Node currentNode = queue.poll();

                if (prev != null) {
                    prev.next = currentNode;
                }

                prev = currentNode;

                if (currentNode.left != null) {
                    queue.offer(currentNode.left);
                }
                if (currentNode.right != null) {
                    queue.offer(currentNode.right);
                }
            }
        }

        return root;
    }

    // V2-1
    // IDEA: BFS
    // https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/solutions/2033511/3-approaches-bfs-bfs-linkedlist-recursio-0q68/

    // V2-2
    // IDEA: BFS + LINKED LIST
    // https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/solutions/2033511/3-approaches-bfs-bfs-linkedlist-recursio-0q68/

    // V2-3
    // IDEA: RECURSION
    // https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/solutions/2033511/3-approaches-bfs-bfs-linkedlist-recursio-0q68/
}
