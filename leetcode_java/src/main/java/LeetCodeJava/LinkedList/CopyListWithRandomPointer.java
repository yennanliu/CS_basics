package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/copy-list-with-random-pointer/
/**
 * 138. Copy List with Random Pointer
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * A linked list of length n is given such that each node contains an additional random pointer, which could point to any node in the list, or null.
 *
 * Construct a deep copy of the list. The deep copy should consist of exactly n brand new nodes, where each new node has its value set to the value of its corresponding original node. Both the next and random pointer of the new nodes should point to new nodes in the copied list such that the pointers in the original list and copied list represent the same list state. None of the pointers in the new list should point to nodes in the original list.
 *
 * For example, if there are two nodes X and Y in the original list, where X.random --> Y, then for the corresponding two nodes x and y in the copied list, x.random --> y.
 *
 * Return the head of the copied linked list.
 *
 * The linked list is represented in the input/output as a list of n nodes. Each node is represented as a pair of [val, random_index] where:
 *
 * val: an integer representing Node.val
 * random_index: the index of the node (range from 0 to n-1) that the random pointer points to, or null if it does not point to any node.
 * Your code will only be given the head of the original linked list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Example 2:
 *
 *
 * Input: head = [[1,1],[2,1]]
 * Output: [[1,1],[2,1]]
 * Example 3:
 *
 *
 *
 * Input: head = [[3,null],[3,0],[3,null]]
 * Output: [[3,null],[3,0],[3,null]]
 *
 *
 * Constraints:
 *
 * 0 <= n <= 1000
 * -104 <= Node.val <= 104
 * Node.random is null or is pointing to some node in the linked list.
 *
 */

import java.util.HashMap;
import java.util.Map;

/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

class Node {

    // attr
    int val;
    Node next;
    Node random;

    // constructor
    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }

    /** NOTE !!! we init a new constructor with more parameters */
    public Node(int _val,Node _next,Node _random) {
        val = _val;
        next = _next;
        random = _random;
    }
}

public class CopyListWithRandomPointer {

    // V0
//    public Node copyRandomList(Node head) {
//
//        if (head.equals(null) || head.next.equals(null)){
//            return head;
//        }
//
//        Node _random = new Node(0);
//        Node dummy = new Node(0);
//        Node _node = new Node(0);
//        dummy.next = _node;
//
//        while (!head.equals(null)){
//            Node _cur = head;
//            Node _next = head.next;
//            _node.next = new Node(_cur.val);
//            _node.random = new Node(head.random.val);
//            _node = _node.next;
//            head = _next;
//        }
//
//        return dummy.next;
//    }

    // V1-1
    // https://youtu.be/5Y2EiZST97Y?feature=shared
    // https://neetcode.io/problems/copy-linked-list-with-random-pointer
    // IDEA: HASHMAP (RECURSION)
    HashMap<Node, Node> map = new HashMap<>();

    /**
     * time = O(N)
     * space = O(N)
     */
    public Node copyRandomList_1_1(Node head) {
        if (head == null) return null;
        if (map.containsKey(head)) return map.get(head);

        Node copy = new Node(head.val);
        map.put(head, copy);
        copy.next = copyRandomList_1_1(head.next);
        copy.random = map.get(head.random);
        return copy;
    }

    // V1-2
    // https://youtu.be/5Y2EiZST97Y?feature=shared
    // https://neetcode.io/problems/copy-linked-list-with-random-pointer
    // IDEA: Hash Map (Two Pass)
    /**
     * time = O(N)
     * space = O(N)
     */
    public Node copyRandomList_1_2(Node head) {
        Map<Node, Node> oldToCopy = new HashMap<>();
        oldToCopy.put(null, null);

        Node cur = head;
        while (cur != null) {
            Node copy = new Node(cur.val);
            oldToCopy.put(cur, copy);
            cur = cur.next;
        }

        cur = head;
        while (cur != null) {
            Node copy = oldToCopy.get(cur);
            copy.next = oldToCopy.get(cur.next);
            copy.random = oldToCopy.get(cur.random);
            cur = cur.next;
        }

        return oldToCopy.get(head);
    }


    // V1-3
    // https://youtu.be/5Y2EiZST97Y?feature=shared
    // https://neetcode.io/problems/copy-linked-list-with-random-pointer
    // IDEA: Hash Map (One Pass)
    /**
     * time = O(N)
     * space = O(N)
     */
    public Node copyRandomList_1_3(Node head) {
        HashMap<Node, Node> oldToCopy = new HashMap<>();
        oldToCopy.put(null, null);

        Node cur = head;
        while (cur != null) {
            if (!oldToCopy.containsKey(cur)) {
                oldToCopy.put(cur, new Node(0));
            }
            oldToCopy.get(cur).val = cur.val;

            if (!oldToCopy.containsKey(cur.next)) {
                oldToCopy.put(cur.next, new Node(0));
            }
            oldToCopy.get(cur).next = oldToCopy.get(cur.next);

            if (!oldToCopy.containsKey(cur.random)) {
                oldToCopy.put(cur.random, new Node(0));
            }
            oldToCopy.get(cur).random = oldToCopy.get(cur.random);
            cur = cur.next;
        }
        return oldToCopy.get(head);
    }


    // V1-4
    // https://youtu.be/5Y2EiZST97Y?feature=shared
    // https://neetcode.io/problems/copy-linked-list-with-random-pointer
    // IDEA: Space Optimized - I
    /**
     * time = O(N)
     * space = O(1)
     */
    public Node copyRandomList_1_4(Node head) {
        if (head == null) {
            return null;
        }

        Node l1 = head;
        while (l1 != null) {
            Node l2 = new Node(l1.val);
            l2.next = l1.next;
            l1.next = l2;
            l1 = l2.next;
        }

        Node newHead = head.next;

        l1 = head;
        while (l1 != null) {
            if (l1.random != null) {
                l1.next.random = l1.random.next;
            }
            l1 = l1.next.next;
        }

        l1 = head;
        while (l1 != null) {
            Node l2 = l1.next;
            l1.next = l2.next;
            if (l2.next != null) {
                l2.next = l2.next.next;
            }
            l1 = l1.next;
        }

        return newHead;
    }


    // V1-5
    // https://youtu.be/5Y2EiZST97Y?feature=shared
    // https://neetcode.io/problems/copy-linked-list-with-random-pointer
    // IDEA:  Space Optimized - II
    /**
     * time = O(N)
     * space = O(1)
     */
    public Node copyRandomList_1_5(Node head) {
        if (head == null) {
            return null;
        }

        Node l1 = head;
        while (l1 != null) {
            Node l2 = new Node(l1.val);
            l2.next = l1.random;
            l1.random = l2;
            l1 = l1.next;
        }

        Node newHead = head.random;

        l1 = head;
        while (l1 != null) {
            Node l2 = l1.random;
            l2.random = (l2.next != null) ? l2.next.random : null;
            l1 = l1.next;
        }

        l1 = head;
        while (l1 != null) {
            Node l2 = l1.random;
            l1.random = l2.next;
            l2.next = (l1.next != null) ? l1.next.random : null;
            l1 = l1.next;
        }

        return newHead;
    }


    // V2
    // IDEA : Recursive
    // https://leetcode.com/problems/copy-list-with-random-pointer/editorial/
    // HashMap which holds old nodes as keys and new nodes as its values.
    HashMap<Node, Node> visitedHash = new HashMap<Node, Node>();

    /**
     * time = O(N)
     * space = O(N)
     */
    public Node copyRandomList_2(Node head) {

        if (head == null) {
            return null;
        }

        // If we have already processed the current node, then we simply return the cloned version of
        // it.
        if (this.visitedHash.containsKey(head)) {
            return this.visitedHash.get(head);
        }

        // Create a new node with the value same as old node. (i.e. copy the node)
        Node node = new Node(head.val, null, null);

        // Save this value in the hash map. This is needed since there might be
        // loops during traversal due to randomness of random pointers and this would help us avoid
        // them.
        this.visitedHash.put(head, node);

        // Recursively copy the remaining linked list starting once from the next pointer and then from
        // the random pointer.
        // Thus we have two independent recursive calls.
        // Finally we update the next and random pointers for the new node created.
        node.next = this.copyRandomList_2(head.next);
        node.random = this.copyRandomList_2(head.random);

        return node;
    }


    // V3
    // IDEA :  Iterative with O(N) Space
    // https://leetcode.com/problems/copy-list-with-random-pointer/editorial/
    // Visited dictionary to hold old node reference as "key" and new node reference as the "value"
    HashMap<Node, Node> visited = new HashMap<Node, Node>();

    /**
     * time = O(1)
     * space = O(1)
     */
    public Node getClonedNode(Node node) {
        // If the node exists then
        if (node != null) {
            // Check if the node is in the visited dictionary
            if (this.visited.containsKey(node)) {
                // If its in the visited dictionary then return the new node reference from the dictionary
                return this.visited.get(node);
            } else {
                // Otherwise create a new node, add to the dictionary and return it
                this.visited.put(node, new Node(node.val, null, null));
                return this.visited.get(node);
            }
        }
        return null;
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public Node copyRandomList_3(Node head) {

        if (head == null) {
            return null;
        }

        Node oldNode = head;

        // Creating the new head node.
        Node newNode = new Node(oldNode.val);
        this.visited.put(oldNode, newNode);

        // Iterate on the linked list until all nodes are cloned.
        while (oldNode != null) {
            // Get the clones of the nodes referenced by random and next pointers.
            newNode.random = this.getClonedNode(oldNode.random);
            newNode.next = this.getClonedNode(oldNode.next);

            // Move one step ahead in the linked list.
            oldNode = oldNode.next;
            newNode = newNode.next;
        }
        return this.visited.get(head);
    }

    // V4
    // IDEA :  Iterative with O(1) Space
    // https://leetcode.com/problems/copy-list-with-random-pointer/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public Node copyRandomList_4(Node head) {

        if (head == null) {
            return null;
        }

        // Creating a new weaved list of original and copied nodes.
        Node ptr = head;
        while (ptr != null) {

            // Cloned node
            Node newNode = new Node(ptr.val);

            // Inserting the cloned node just next to the original node.
            // If A->B->C is the original linked list,
            // Linked list after weaving cloned nodes would be A->A'->B->B'->C->C'
            newNode.next = ptr.next;
            ptr.next = newNode;
            ptr = newNode.next;
        }

        ptr = head;

        // Now link the random pointers of the new nodes created.
        // Iterate the newly created list and use the original nodes' random pointers,
        // to assign references to random pointers for cloned nodes.
        while (ptr != null) {
            ptr.next.random = (ptr.random != null) ? ptr.random.next : null;
            ptr = ptr.next.next;
        }

        // Unweave the linked list to get back the original linked list and the cloned list.
        // i.e. A->A'->B->B'->C->C' would be broken to A->B->C and A'->B'->C'
        Node ptr_old_list = head; // A->B->C
        Node ptr_new_list = head.next; // A'->B'->C'
        Node head_old = head.next;
        while (ptr_old_list != null) {
            ptr_old_list.next = ptr_old_list.next.next;
            ptr_new_list.next = (ptr_new_list.next != null) ? ptr_new_list.next.next : null;
            ptr_old_list = ptr_old_list.next;
            ptr_new_list = ptr_new_list.next;
        }
        return head_old;
    }

}
