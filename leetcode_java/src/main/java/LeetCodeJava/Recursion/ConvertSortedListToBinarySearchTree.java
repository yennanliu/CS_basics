package LeetCodeJava.Recursion;

// https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/description/

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

/**
 *  109. Convert Sorted List to Binary Search Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a singly linked list where elements are sorted in ascending order, convert it to a height-balanced binary search tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [-10,-3,0,5,9]
 * Output: [0,-3,9,-10,null,5]
 * Explanation: One possible answer is [0,-3,9,-10,null,5], which represents the shown height balanced BST.
 * Example 2:
 *
 * Input: head = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in head is in the range [0, 2 * 104].
 * -105 <= Node.val <= 105
 *
 *
 *
 */
public class ConvertSortedListToBinarySearchTree {

    // V0
//    public TreeNode sortedListToBST(ListNode head) {
//
//    }


    // V0-1
    // IDEA: DFS + MID POINT + LINKED LIST + LC 108 (GPT)
    public TreeNode sortedListToBST_0_1(ListNode head) {
        if (head == null) {
            return null;
        }

        int len = getNodeLen(head);
        return buildBSTFromLinkedList(head, 0, len - 1);
    }

    private TreeNode buildBSTFromLinkedList(ListNode head, int left, int right) {

        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;

        ListNode midNode = getNodeByIdx(head, mid);

        TreeNode root = new TreeNode(midNode.val);

        /** NOTE !!! below */
        root.left = buildBSTFromLinkedList(head, left, mid - 1);
        root.right = buildBSTFromLinkedList(head, mid + 1, right);

        return root;
    }

    private ListNode getNodeByIdx(ListNode head, int idx) {

        while (idx > 0 && head != null) {
            head = head.next;
            idx--;
        }

        return head;
    }

    private int getNodeLen(ListNode head) {
        int len = 0;

        while (head != null) {
            head = head.next;
            len++;
        }

        return len;
    }


    // V0-2
    // IDEA: DFS + (Slow & Fast Pointers) + LINKED LIST (GEMINI)
    /** NOTE !!!
     *
     * in V0-2, we are NOT using `MID POINT` approach,
     * but a slow-fast pointer idea
     */
    public TreeNode sortedListToBST_0_2(ListNode head) {
        // 1. Base cases
        if (head == null)
            return null;
        if (head.next == null)
            return new TreeNode(head.val);

        // 2. Find the middle of the list to use as root
        // We use 'prev' to disconnect the left half from the middle
        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        // 'slow' is now the middle node
        TreeNode root = new TreeNode(slow.val);

        // 3. Disconnect the left half from the middle node
        if (prev != null) {
            prev.next = null;
        }

        // 4. Recursively build left and right subtrees
        // Left half starts at 'head', right half starts at 'slow.next'
        root.left = sortedListToBST_0_2(head);
        root.right = sortedListToBST_0_2(slow.next);

        return root;
    }


    // V0-3
    // IDEA:  recursion + $O(N)$ In-Order Simulation (gemini)
    private ListNode currentHead;
    public TreeNode sortedListToBST_0_3(ListNode head) {
        if (head == null)
            return null;

        // 1. Calculate the total length of the list (O(N))
        int size = countNodes(head);

        // 2. Set the global/class-level head to track our position
        this.currentHead = head;

        // 3. Build the tree using the size to define boundaries
        return buildInOrder(0, size - 1);
    }

    private TreeNode buildInOrder(int left, int right) {
        // Base case: No nodes left in this range
        if (left > right) {
            return null;
        }

        // Find the middle to keep the tree height-balanced
        int mid = left + (right - left) / 2;

        // A) Recursively build the LEFT subtree
        // We don't assign values yet; we just build the structure
        TreeNode leftChild = buildInOrder(left, mid - 1);

        // B) Create the ROOT node
        // In-order means we process the current node AFTER the left child
        TreeNode root = new TreeNode(this.currentHead.val);
        root.left = leftChild;

        // MOVE the global head to the next list node
        this.currentHead = this.currentHead.next;

        // C) Recursively build the RIGHT subtree
        root.right = buildInOrder(mid + 1, right);

        return root;
    }

    private int countNodes(ListNode head) {
        int count = 0;
        while (head != null) {
            count++;
            head = head.next;
        }
        return count;
    }



    // V1

    // V2
    // https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/solutions/6515817/beats-100-best-interview-approach-notes-6v9y6/
    public TreeNode sortedListToBST_2(ListNode head) {
        if (head == null)
            return null;
        if (head.next == null)
            return new TreeNode(head.val);
        ListNode slow = head, fast = head, slow_Prev = null;
        while (fast != null && fast.next != null) {
            slow_Prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode root = new TreeNode(slow.val);//Making A Root Node
        slow_Prev.next = null;//Braeking The Link For L1
        root.left = sortedListToBST_2(head);
        root.right = sortedListToBST_2(slow.next);
        return root;
    }


    // V3
    // https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/solutions/3282028/java-easy-using-slow-fast-pointer-recurs-uszl/
    public TreeNode sortedListToBST_3(ListNode head) {
        if (head == null)
            return null;
        if (head.next == null)
            return new TreeNode(head.val);
        ListNode slow = head;
        ListNode fast = head.next.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode res = new TreeNode(slow.next.val);
        ListNode righthalf = slow.next.next;
        slow.next = null;
        res.left = sortedListToBST_3(head);
        res.right = sortedListToBST_3(righthalf);
        return res;
    }




}
