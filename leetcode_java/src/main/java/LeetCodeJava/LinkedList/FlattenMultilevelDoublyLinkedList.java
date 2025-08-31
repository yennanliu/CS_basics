package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/description/
// https://leetcode.cn/problems/flatten-a-multilevel-doubly-linked-list/

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 430. Flatten a Multilevel Doubly Linked List
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given a doubly linked list, which contains nodes that have a next pointer, a previous pointer, and an additional child pointer. This child pointer may or may not point to a separate doubly linked list, also containing these special nodes. These child lists may have one or more children of their own, and so on, to produce a multilevel data structure as shown in the example below.
 *
 * Given the head of the first level of the list, flatten the list so that all the nodes appear in a single-level, doubly linked list. Let curr be a node with a child list. The nodes in the child list should appear after curr and before curr.next in the flattened list.
 *
 * Return the head of the flattened list. The nodes in the list must have all of their child pointers set to null.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 * Output: [1,2,3,7,8,11,12,9,10,4,5,6]
 * Explanation: The multilevel linked list in the input is shown.
 * After flattening the multilevel linked list it becomes:
 *
 * Example 2:
 *
 *
 * Input: head = [1,2,null,3]
 * Output: [1,3,2]
 * Explanation: The multilevel linked list in the input is shown.
 * After flattening the multilevel linked list it becomes:
 *
 * Example 3:
 *
 * Input: head = []
 * Output: []
 * Explanation: There could be empty list in the input.
 *
 *
 * Constraints:
 *
 * The number of Nodes will not exceed 1000.
 * 1 <= Node.val <= 105
 *
 *
 * How the multilevel linked list is represented in test cases:
 *
 * We use the multilevel linked list from Example 1 above:
 *
 *  1---2---3---4---5---6--NULL
 *          |
 *          7---8---9---10--NULL
 *              |
 *              11--12--NULL
 * The serialization of each level is as follows:
 *
 * [1,2,3,4,5,6,null]
 * [7,8,9,10,null]
 * [11,12,null]
 * To serialize all levels together, we will add nulls in each level to signify no node connects to the upper node of the previous level. The serialization becomes:
 *
 * [1,    2,    3, 4, 5, 6, null]
 *              |
 * [null, null, 7,    8, 9, 10, null]
 *                    |
 * [            null, 11, 12, null]
 * Merging the serialization of each level and removing trailing nulls we obtain:
 *
 * [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 *
 *
 */
public class FlattenMultilevelDoublyLinkedList {

    /*
    // Definition for a Node.
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };
    */

    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    // V0
    // IDEA: ITERATIVE + STACK + LINKED LIST OP (fixed by gpt)
    public Node flatten(Node head) {
        if (head == null)
            return null;

        Stack<Node> stack = new Stack<>();

    /** NOTE !!! the node we plan to return as res */
    /**
     *
     * ✅ Why dummy is essential
     * 	•	Dummy ensures you never lose track of the start of the list.
     * 	•	_prev = new Node() by itself doesn’t give you a stable pointer to return.
     *
     * ⸻
     *
     * 👉 So they look similar, but the intent differs:
     * 	•	dummy = fixed anchor, always at start, safe to return dummy.next.
     * 	•	_prev = new Node() = temporary pointer, moves forward, so you lose the head.
     *
     */
    /**
     *  NOTE !!!  below is wrong
     *
     *   Node prev = new Node();
     *
     *   -> we need an `anchor` to check the `init status`
     *
     */
        Node dummy = new Node(); // helper node
        Node prev = dummy;

        stack.push(head);

        while (!stack.isEmpty()) {

            Node curr = stack.pop();

            // connect prev <-> curr
            prev.next = curr;
            curr.prev = prev;

            /**
             *  NOTE !!!
             *
             *  `next` nodes op
             */
            // If `next` exists, push it first (so `child` is processed before `next`)
            // NOTE !!!  child -> next (ordering)
            // stack: FILO
            if (curr.next != null) {
                stack.push(curr.next);
            }

            /**
             *  NOTE !!!
             *
             *  `child` nodes op
             */
            // If child exists, push it
            if (curr.child != null) {
                stack.push(curr.child);
                curr.child = null; // clear child reference
            }

            prev = curr; // move prev forward
        }

        // detach dummy
        dummy.next.prev = null;

        /** NOTE !!! we return dummy.next  */
        return dummy.next;
    }

    // V0-0-1
    // IDEA: ITERATIVE + STACK + LINKED LIST OP (fixed by gpt)
    public Node flatten_0_0_1(Node head) {
        // edge
        if (head == null) {
            return null;
        }
        if (head.next == null && head.child == null) {
            return head;
        }

        // copy current node to a new one
        // do op (break, reconnect) on the new one
        // since they are using the same reference
        // so we can still return head as res
        Node head_2 = head;

        /**
         *  NOTE !!!
         *
         *  should use `stack` (FILO) (NOT queue)
         *  since we need to `append` the `latest added next node`
         */
        // queue
        //Queue<Node> q = new LinkedList<>();
        Stack<Node> st = new Stack<>();

        while (head_2 != null) {

            // handle child
            if (head_2.child != null) {

                /**
                 *  NOTE !!!
                 *
                 *   we push `next` to queue at this stage
                 *   since it there is a `child`
                 *   we will `reconnect child` as `next`
                 *   so we need to `cache` the `original next` first
                 *   and the way we cache it is via `stack`
                 */
                if (head_2.next != null) {
                    st.add(head_2.next);
                }

                // reconnect `child` as `next`
                head_2.next = head_2.child;
                // connect `prev`
                head_2.child.prev = head_2; // ??
                // since reconnect child as next,
                // the original pointer (child) should be null
                head_2.child = null;
            }

            // NOTE !!! below is WRONG
            //--------------------
            //            Node _child = head_2.child;
            //            //Node _last_child = null;
            //            while(_child != null){
            //                _child = _child.next;
            //            }
            //
            //            // handle next, pop `last added next node` from queue
            //            if(!st.isEmpty()){
            //                // ???
            //                Node _next_node = st.pop();
            //                // ???
            //                _child.next = _next_node;
            //                _next_node.prev = _child;
            //            }
            //--------------------

            if (!st.isEmpty() && head_2.next == null) {
                // pop from stack
                Node _next_node = st.pop();
                head_2.next = _next_node;
                _next_node.prev = head_2;
                /**
                 *  NOTE !!!
                 *
                 *   we DON'T need below,
                 *   since we will move head_2 to `next`
                 *   in below code anyway
                 */
                //head_2 = _next_node;  // ???
            }

            /**
             *  NOTE !!!
             *
             *   we CAN'T add `next` to stack at below,
             *   we need to do right after if head_2 has child node
             *   (e.g. head_2.child != null)
             */
            // add `next` to queue
            //            if(head_2.next != null){
            //                q.add(head_2.next);
            //            }

            // move the `next` node
            head_2 = head_2.next;
        }

        return head; // NOTE !!! return head (instead of head.next)
    }

  // V0-0-2
  // IDEA: ITERATIVE + STACK + LINKED LIST OP (fixed by gpt)
  public Node flatten_0_0_2(Node head) {
      if (head == null){
          return null;
      }

      Node cur = head;
      Stack<Node> stack = new Stack<>();

      while (cur != null) {
          // If there is a child
          if (cur.child != null) {
              // If there is a next, push it to stack to process later
              if (cur.next != null) {
                  stack.push(cur.next);
              }

              // Reconnect child as next
              cur.next = cur.child;
              cur.child.prev = cur;
              cur.child = null;
          }

          // If at the end of current level and there's saved nodes
          if (cur.next == null && !stack.isEmpty()) {
              Node nextFromStack = stack.pop();
              cur.next = nextFromStack;
              nextFromStack.prev = cur;
          }

          cur = cur.next;
      }

      return head;
  }

  // V0-1
  // IDEA: ITERATIVE + STACK + LINKED LIST OP (fixed by gpt)
  // 	•	Time Complexity: O(n) — each node is visited once.
  //	•	Space Complexity: O(d) — where d is the maximum depth of nesting (due to stack usage).
  public Node flatten_0_1(Node head) {
        if (head == null)
            return null;

    /**  NOTE !!!
     *
     * 	- Initialize a stack to keep track
     * 	  of nodes we need to revisit
     * 	  (i.e., the next nodes that are interrupted by child lists).
     *
     * 	- curr is a pointer used to walk through the list.
     */
    Stack<Node> stack = new Stack<>();
    Node curr = head;

    while (curr != null) {
      // If the current node has a child
      /**
       * - If there’s a child, we:
       *
       * 	1.	Save the current next node to revisit later
       *    	by pushing it onto the stack.
       *
       * 	2.	Update curr.next to point to the child node.
       *
       * 	3.	Set the child’s prev to the current node.
       *
       * 	4.	Clear curr.child because we’ve moved it into the main list.
       *
       */
      if (curr.child != null) {
                // If there's a next node, push it to the stack to revisit later
                if (curr.next != null) {
                    stack.push(curr.next);
                }

              // Rewire pointers to insert child list
              /**
               *  NOTE !!!
               *
               *   we simply connect to `child`
               *   (via curr.next = curr.child)
               *
               *   (NOT using `child flatten result` here)
               *
               */
                curr.next = curr.child;
                curr.child.prev = curr;
                // NOTE !!! Clear curr.child because we’ve moved it into the main list.
                curr.child = null;
            }

      // If we've reached the end and there's something in the stack
      //  If end of current list and we saved nodes:
      /**
       * 	- If we reach the end of a level and the stack is not empty:
       *
       * 	   - Pop a saved node from the stack
       * 	     (which was the original next before a child was inserted).
       *
       * 	   - Reconnect it to the current node.
       */
      if (curr.next == null && !stack.isEmpty()) {
                Node node = stack.pop();
                curr.next = node;
                node.prev = curr;
      }

            // 	Continue traversing through the list.
            curr = curr.next;
        }

        return head;
    }

    // V0-2
    // IDEA: DFS + LINKED LIST OP (fixed by gpt)
    public Node flatten_0_2(Node head) {
        if (head == null)
            return null;
        flattenDFS(head);
        return head;
    }

    // Returns the tail of the flattened list
    private Node flattenDFS(Node node) {
        Node curr = node;
        Node last = null;

        while (curr != null) {
            Node next = curr.next;

            // If the current node has a child, we flatten it
            if (curr.child != null) {
                Node childTail = flattenDFS(curr.child);

                // Connect current node to child
                /**
                 *  NOTE !!!
                 *
                 *   we simply connect to `child`
                 *   (via curr.next = curr.child)
                 *
                 *   (NOT using `child flatten result` here)
                 *
                 */
                curr.next = curr.child;
                /**
                 *  NOTE !!!
                 *
                 *  NOTE !!! we need to connect `prev` to current
                 */
                curr.child.prev = curr;

                // Connect child's tail to next
                /**
                 *  NOTE !!!
                 *
                 *   need to connect `child tail` to next node
                 */
                if (next != null) {
                    childTail.next = next;
                    /**
                     *  NOTE !!!
                     *
                     *  NOTE !!! we need to connect `prev` to childTail
                     */
                    next.prev = childTail;
                }

                // Null out the child pointer
                curr.child = null;

                last = childTail;
            } else {
                last = curr;
            }

            curr = next;
        }

        return last;
    }

    // V0-3
    // IDEA: ITERATIVE + STACK + LINKED LIST OP (fixed by gpt)
    public Node flatten_0_3(Node head) {
        if (head == null)
            return null;

        Stack<Node> stack = new Stack<>();
        Node current = head;

        while (current != null) {
            // If the current node has a child
            if (current.child != null) {
                // If there's a next node, push it to the stack to reconnect later
                if (current.next != null) {
                    stack.push(current.next);
                }

                // Connect current to child
                current.next = current.child;
                current.child.prev = current;
                current.child = null;
            }

            // If at the end of a level and stack is not empty, pop and reconnect
            if (current.next == null && !stack.isEmpty()) {
                Node nextFromStack = stack.pop();
                current.next = nextFromStack;
                nextFromStack.prev = current;
            }

            current = current.next;
        }

        return head;
    }

    // V0-4
    // IDEA: DFS + LINKED LIST OP (gpt)
    public Node flatten_0_4(Node head) {
        if (head == null)
            return head;
        flattenDFS_0_4(head);
        return head;
    }

    // returns the tail of the flattened list
    private Node flattenDFS_0_4(Node node) {
        Node curr = node;
        Node last = null;

        while (curr != null) {
            Node next = curr.next;

            // Case 1: has child
            if (curr.child != null) {
                Node childHead = curr.child;
                Node childTail = flattenDFS_0_4(childHead); // flatten the child

                // connect curr → child
                curr.next = childHead;
                childHead.prev = curr;

                // connect childTail → next
                if (next != null) {
                    childTail.next = next;
                    next.prev = childTail;
                }

                // remove child pointer
                curr.child = null;
                last = childTail;
            } else {
                last = curr;
            }

            curr = next;
        }

        return last; // return the tail
    }

    // V0-5
    // IDEA: ITERATIVE + LINKED LIST OP (fixed by gpt)
    public Node flatten_0_5(Node head) {
        if (head == null) {
            return null;
        }

        //Node dummy = new Node(0, null, head, null); // dummy prev to head
        Node dummy = new Node(); // dummy prev to head
        Node prev = dummy;

        Stack<Node> st = new Stack<>();
        st.push(head);

        while (!st.isEmpty()) {
            Node cur = st.pop();

            // connect prev ↔ cur
            prev.next = cur;
            cur.prev = prev;

            // push next first, then child (so child processed first)
            if (cur.next != null) {
                st.push(cur.next);
            }
            if (cur.child != null) {
                st.push(cur.child);
                cur.child = null; // clear child link
            }

            prev = cur; // move forward
        }

        // detach dummy
        dummy.next.prev = null;
        return dummy.next;
    }

    // V1
    // IDEA : LINKED LIST OP + one off + iterative
    // https://zihengcat.github.io/2019/09/02/leetcode-430-flatten-a-multilevel-doubly-linked-list/
    public Node flatten_1(Node head) {
        if (head == null) {
            return head;
        }
        /* Using a pointer */
        Node p = head;
        while (p != null) {
            /* CASE: the node has child */
            if (p.child != null) {
                Node childNode = p.child;
                /* Find the tail node of child doubly linked list */
                while (childNode.next != null) {
                    childNode = childNode.next;
                }
                /* Connect tail with p.next, if it is not null */
                childNode.next = p.next;
                if (p.next != null) {
                    p.next.prev = childNode;
                }
                /* Connect p with p.child, and remove p.child */
                p.next = p.child;
                p.child.prev = p;
                p.child = null;
            } else {
                /* CASE: if no child, just move forward */
                p = p.next;
            }
        }
        return head;
    }

    // V2
    // IDEA : DFS + LINKED LIST (modified by GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/flatten_a_multilevel_doubly_linked_list.py#L2
    public Node flatten_2(Node head) {
        dfs_2(head);
        return head;
    }

    private Node dfs_2(Node head) {
        Node cur = head;

        while (cur != null) {
            // If there is a child node, proceed with DFS
            if (cur.child != null) {
                Node next = cur.next;
                cur.next = cur.child;
                cur.next.prev = cur;

                // Recursively flatten the child list
                Node childLast = dfs_2(cur.child);

                // Connect the child list to the next node in the main list
                childLast.next = next;
                if (next != null) {
                    next.prev = childLast;
                }

                // Unlink the child after flattening
                cur.child = null;
            }
            head = cur;
            cur = cur.next;
        }
        return head;
    }

    // V11
    // https://leetcode.cn/problems/flatten-a-multilevel-doubly-linked-list/solutions/1013884/bian-ping-hua-duo-ji-shuang-xiang-lian-b-383h/
    // IDEA : RECURSIVE + LINKED LIST (LC CN official)
    public Node flatten_11(Node head) {
        dfs(head);
        return head;
    }

    public Node dfs(Node node) {
        Node cur = node;
        // record last node in linked list
        Node last = null;

        while (cur != null) {
            Node next = cur.next;
            //  if there is a child node, deal with child node first
            if (cur.child != null) {
                Node childLast = dfs(cur.child);

                next = cur.next;
                //  connect node and child
                cur.next = cur.child;
                cur.child.prev = cur;

                //  if next is not null, connect last and next
                if (next != null) {
                    childLast.next = next;
                    next.prev = childLast;
                }

                // set child as null
                cur.child = null;
                last = childLast;
            } else {
                last = cur;
            }
            cur = next;
        }
        return last;
    }



    // V10
    // https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/solutions/5667355/easy-java-solution/
//    class Node {
//        public int val;
//        public Node prev;
//        public Node next;
//        public Node child;
//    };

    private Queue<Node> store = new LinkedList<>();

    public void helper(Node head) {
        Node temp;
        while (head != null) {
            temp = head.next;
            head.next = null;
            head.prev = null;
            store.offer(head);
            if (head.child != null)
                helper(head.child);
            head.child = null;
            head = temp;
        }
    }

    public Node flatten_10(Node head) {
        helper(head);
        if (store.peek() == null)
            return head;
        Node retval = store.poll();
        Node first = retval;
        while (store.peek() != null) {
            Node second = store.poll();
            first.next = second;
            second.prev = first;
            first = second;
        }
        first.next = null;
        return retval;
    }

    // V12
    // IDEA : LINKED LIST + CUSTOM CLASS
    // https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/solutions/4031041/single-pass-solution-using-custom-class/
    class lc430Helper {
        Node head;
        Node tail;
    }

    public Node flatten_12(Node head) {
        return util1(head).head;
    }

    public lc430Helper util1(Node head) {
        Node dummy = new Node();
        dummy.next = head;
        Node temp = dummy;
        while (temp.next != null) {
            temp = temp.next;
            if (temp.child == null) {
                continue;
            } else {
                lc430Helper bottom = util1(temp.child);
                Node temp2 = temp.next;
                temp.child = null;
                temp.next = bottom.head;
                bottom.head.prev = temp;
                bottom.tail.next = temp2;
                if (temp2 != null) {
                    temp2.prev = bottom.tail;
                }
                temp = bottom.tail;
            }
        }
        lc430Helper ans = new lc430Helper();
        dummy.next = null;
        ans.head = head;
        ans.tail = temp;
        return ans;
    }

    // V13
    // https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/solutions/5328452/easy-to-understand-best-solution/
    public Node flatten_13(Node head) {
        Node temp = head;
        while(temp != null){
            Node list1Tail = temp;
            Node list3Head = temp.next;
            // if the node has child node then append all its node in between

            if(temp.child != null){
                // we are assuming that recursion will give us flatten output, so we just need to adjust the pointers
                Node list2Head = flatten_13(temp.child);

                // find list2 tail
                Node list2Tail = list2Head;
                while(list2Tail.next != null){
                    list2Tail = list2Tail.next;
                }

                // attach the lists
                list1Tail.next = list2Head;
                list2Head.prev = list1Tail;
                list2Tail.next = list3Head;
                if(list3Head != null)
                    list3Head.prev = list2Tail;
                temp.child = null;
            }
            temp = temp.next;
        }
        return head;
    }



}
