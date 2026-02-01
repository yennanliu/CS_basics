package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/description/
/**
 * 82. Remove Duplicates from Sorted List II
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list. Return the linked list sorted as well.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,3,4,4,5]
 * Output: [1,2,5]
 * Example 2:
 *
 *
 * Input: head = [1,1,1,2,3]
 * Output: [2,3]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 300].
 * -100 <= Node.val <= 100
 * The list is guaranteed to be sorted in ascending order.
 */

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class RemoveDuplicatesFromSortedList2 {

    // V0
//    public ListNode deleteDuplicates(ListNode head) {
//
//    }

    // V0-1

    // IDEA: LINKED LIST -> HASHMAP, SET -> LINKED LIST
    /**
     * time = O(N)
     * space = O(N)
     */
    public ListNode deleteDuplicates_0_1(ListNode head) {
        // edge
        if (head == null || head.next == null) {
            return head;
        }

        /**
         *  NOTE !!!
         *
         *  1. HashSet DOES NOT keep the `insert order` in java
         *  2. LinkedHashSet DOES keep the `insert order` in java
         *     -> so we use `LinkedHashSet` for tracking the `order` in linkedlist
         */
        //Set<Integer> set = new HashSet<>(); // order
        Set<Integer> set = new LinkedHashSet<>();
        Map<Integer, Integer> map = new HashMap<>(); // {val : cnt}

        while (head != null) {
            Integer val = head.val;
            map.put(val, map.getOrDefault(val, 0) + 1);
            set.add(val);
            head = head.next;
        }
        ListNode dummy = new ListNode();
        ListNode res = dummy; // ???

        for (Integer x : set) {
            if (map.get(x) == 1) {
                ListNode node = new ListNode(x);
                dummy.next = node;
                dummy = node;
            }
        }

        //System.out.println(">>> dummy = " + dummy);
        return res.next;
    }

    // V0-2
    // IDEA: LINKED LIST + 2 POINTERS
    /**
     *  DEMO
     *
     * 1 -> 2 -> 2 -> 3 -> 3 -> 4
     *
     * We want to remove all duplicates entirely, so the expected output is:
     *
     * 1 -> 4
     *
     *
     * ⸻
     *
     * Step 0: Initialization
     *
     * dummy -> 1 -> 2 -> 2 -> 3 -> 3 -> 4
     * pre = dummy
     * cur = head (1)
     *
     *
     * ⸻
     *
     * Step 1: Process value 1
     * 	•	Inner loop: cur.next.val == cur.val? → 2 != 1 → skip inner loop
     * 	•	Check pre.next == cur → dummy.next == 1 → true → no duplicates
     *
     * pre = cur (1)
     * cur = cur.next (2)
     *
     * List remains unchanged:
     *
     * dummy -> 1 -> 2 -> 2 -> 3 -> 3 -> 4
     * pre -> 1
     * cur -> 2
     *
     *
     * ⸻
     *
     * Step 2: Process value 2
     * 	•	Inner loop: cur.next.val == cur.val → 2 == 2 → move cur to next 2
     *
     * cur -> second 2
     *
     * 	•	Inner loop again: cur.next.val == cur.val → 3 != 2 → exit inner loop
     * 	•	Check pre.next == cur → 1.next == 2 → false → duplicates exist
     * 	•	Remove duplicates:
     *
     * pre.next = cur.next (3)
     * cur = cur.next (3)
     *
     * Updated list:
     *
     * dummy -> 1 -> 3 -> 3 -> 4
     * pre -> 1
     * cur -> 3
     *
     *
     * ⸻
     *
     * Step 3: Process value 3
     * 	•	Inner loop: cur.next.val == cur.val → 3 == 3 → move cur to second 3
     * 	•	Inner loop again: cur.next.val == cur.val → 4 != 3 → exit inner loop
     * 	•	Check pre.next == cur → 1.next == 3 → false → duplicates exist
     * 	•	Remove duplicates:
     *
     * pre.next = cur.next (4)
     * cur = cur.next (4)
     *
     * Updated list:
     *
     * dummy -> 1 -> 4
     * pre -> 1
     * cur -> 4
     *
     *
     * ⸻
     *
     * Step 4: Process value 4
     * 	•	Inner loop: cur.next is null → exit
     * 	•	Check pre.next == cur → 1.next == 4 → true → no duplicates
     * 	•	Move pre:
     *
     * pre = cur (4)
     * cur = cur.next (null)
     *
     *
     * ⸻
     *
     * Step 5: Finish
     *
     * cur is null → exit while loop
     *
     * Return:
     *
     * dummy.next -> 1 -> 4
     *
     * ✅ Final list after removing all duplicates:
     *
     * 1 -> 4
     *
     *
     */

    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteDuplicates_0_2(ListNode head) {

        /**
         * 	•	dummy is a dummy head to simplify edge cases
         * 	    (like when the first few nodes are duplicates).
         *
         * 	•	pre points to the last node before the current sequence of duplicates.
         *
         */
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        // 	cur iterates through the list.
        ListNode cur = head;

        while (cur != null) {
            /**
             * 	Skip duplicates:
             *
             *    - This inner loop moves cur to the last node of a `duplicate` sequence.
             *
             * 	  - Example:
             * 	     if list is 1 -> 2 -> 2 -> 2 -> 3,
             * 	       and cur is the first 2,
             * 	       after this loop cur points to the last 2.
             */
            while (cur.next != null && cur.next.val == cur.val) {
                cur = cur.next;  // skip all nodes with the same value
            }
            /**
             * Check if duplicates existed:
             *
             * 	•	pre.next == cur → no duplicates for this value, so move pre forward.
             *
             * 	•	Otherwise → duplicates exist,
             * 	    so skip the entire sequence by linking pre.next to cur.next.
             */
            if (pre.next == cur) {
                pre = cur;       // no duplicates detected, move pre forward
            } else {
                pre.next = cur.next; // duplicates detected, remove them
            }
            cur = cur.next;       // move to the next new value
        }

        // 	dummy.next points to the head of the cleaned list (without duplicates).
        return dummy.next;
    }

    // V1

    // https://leetcode.ca/2016-02-20-82-Remove-Duplicates-from-Sorted-List-II/
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteDuplicates_1(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur != null) {
            while (cur.next != null && cur.next.val == cur.val) {
                cur = cur.next;
            }
            if (pre.next == cur) {
                pre = cur;
            } else {
                pre.next = cur.next;
            }
            cur = cur.next;
        }
        return dummy.next;
    }

    //  V2

    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/solutions/6801800/beats-100-easiest-explanation-for-beginn-6noz/
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteDuplicates_2(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode dummy = new ListNode(-1); // Dummy node to handle head removals
        dummy.next = head;
        ListNode prev = dummy;
        ListNode cur = head;

        while (cur != null && cur.next != null) {
            if (cur.val == cur.next.val) {
                // Skip all nodes with the same value
                while (cur.next != null && cur.val == cur.next.val) {
                    cur = cur.next;
                }
                prev.next = cur.next; // Remove duplicates
            } else {
                prev = prev.next; // Move to next distinct node
            }
            cur = cur.next;
        }

        return dummy.next;
    }

    // V3

    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/solutions/7002012/simple-solution-by-harshita_114-2gqn/
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteDuplicates_3(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode prev = new ListNode(-1);
        ListNode dummy = prev;
        dummy.next = head;
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                while (curr.next != null && curr.val == curr.next.val) {
                    curr = curr.next;
                }
                dummy.next = curr.next;
            } else {
                dummy = dummy.next;
            }
            curr = curr.next;
        }
        return prev.next;
    }

    // V4

    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/solutions/7192741/remove-duplicates-from-sorted-list-ii-on-4dmz/
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteDuplicates_4(ListNode head) {
        if (head == null)
            return head;
        ListNode temp = new ListNode(0, head);
        ListNode prev = temp;
        ListNode curr = head;

        while (curr != null && curr.next != null) {
            boolean isDuplicate = false;

            while (curr.next != null && curr.val == curr.next.val) {
                curr = curr.next;
                isDuplicate = true;
            }

            if (isDuplicate)
                prev.next = curr.next;
            else
                prev = prev.next;

            curr = curr.next;
        }
        return temp.next;
    }



}
