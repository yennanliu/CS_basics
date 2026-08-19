package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/

/**
 *  426. Convert Binary Search Tree to Sorted Doubly Linked List
 *  Medium
 *
 *  Convert a Binary Search Tree to a sorted Circular Doubly-Linked List in place.
 *
 *  You can think of the left and right pointers as synonymous to the predecessor
 *  and successor pointers in a doubly-linked list. For a circular doubly linked
 *  list, the predecessor of the first element is the last element, and the
 *  successor of the last element is the first element.
 *
 *  We want to do the transformation in place. After the transformation, the left
 *  pointer of the tree node should point to its predecessor, and the right
 *  pointer should point to its successor. Return the pointer to the smallest
 *  element of the linked list.
 *
 *  Example 1:
 *   Input: root = [4,2,5,1,3]
 *   Output: [1,2,3,4,5]
 *
 *  Example 2:
 *   Input: root = [2,1,3]
 *   Output: [1,2,3]
 *
 *  Constraints:
 *   The number of nodes in the tree is in the range [0, 2000].
 *   -1000 <= Node.val <= 1000
 *   All the values of the tree are unique.
 */
public class ConvertBinarySearchTreeToSortedDoublyLinkedList {

    // in-order walk state (used by V0)
    private NodeX head;
    private NodeX prev;

    // LC "Node" for this problem (val + left/right), defined locally since the
    // shared LeetCodeJava.DataStructure.Node is the clone-graph shape.
    public static class NodeX {
        public int val;
        public NodeX left;
        public NodeX right;

        public NodeX() {
        }

        public NodeX(int val) {
            this.val = val;
        }

        public NodeX(int val, NodeX left, NodeX right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // V0
    // IDEA: in-order DFS; keep the previously visited node and wire prev.right /
    //       cur.left as we go, then close the ring between head and tail.
    /**
     * time = O(n)
     * space = O(h)   // h = tree height (recursion stack)
     */
    public NodeX treeToDoublyList(NodeX root) {
        if (root == null) {
            return null;
        }
        head = null;
        prev = null;

        inorder(root);

        // close the circle
        prev.right = head;
        head.left = prev;
        return head;
    }

    private void inorder(NodeX node) {
        if (node == null) {
            return;
        }
        inorder(node.left);

        if (prev == null) {
            head = node;
        } else {
            prev.right = node;
            node.left = prev;
        }
        prev = node;

        inorder(node.right);
    }

    // V1
    // IDEA: same in-order wiring done iteratively with an explicit stack
    //       (avoids recursion depth on skewed trees).
    /**
     * time = O(n)
     * space = O(h)
     */
    public NodeX treeToDoublyList_1(NodeX root) {
        if (root == null) {
            return null;
        }

        java.util.Deque<NodeX> stack = new java.util.ArrayDeque<>();
        NodeX cur = root;
        NodeX first = null;
        NodeX last = null;

        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();

            if (first == null) {
                first = cur;
            } else {
                last.right = cur;
                cur.left = last;
            }
            last = cur;

            cur = cur.right;
        }

        first.left = last;
        last.right = first;
        return first;
    }
}
