package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-linked-list-elements/
/**
 *  203. Remove Linked List Elements
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a linked list and an integer val, remove all the nodes of the linked list that has Node.val == val, and return the new head.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,6,3,4,5,6], val = 6
 * Output: [1,2,3,4,5]
 * Example 2:
 *
 * Input: head = [], val = 1
 * Output: []
 * Example 3:
 *
 * Input: head = [7,7,7,7], val = 7
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 104].
 * 1 <= Node.val <= 50
 * 0 <= val <= 50
 *
 *
 */
import LeetCodeJava.DataStructure.ListNode;

public class RemoveLinkedListElements {


    // V0
    // IDEA: LINKED LIST (gemini)
    /**  NOTE !!!
     *
     *  below code can handle case like:
     *
     *  head = [6,6,6,3,4,5,6], val = 6
     *
     *  NOTE:
     *    1. when we meet `val`, we ONLY update the `point connection`,
     *       -> the `cur` node still NOT move, till next `curr.next.val != val`
     *
     *  -------
     *
     *  dry run demo:
     *
     *  Yes, the code I provided handles continuous nodes (like `6, 6, 6`) perfectly.
     *
     * The secret is in the **`else`** block.
     * Look closely at how the `curr` pointer moves:
     *
     *
     * ### 🔍 Dry Run: `[6, 6, 6, 3]`, `val = 6`
     *
     * 1. **Initial State**: `dummy` -> `6` -> `6` -> `6` -> `3`. `curr` is at `dummy`.
     * 2. **Step 1**: `curr.next.val` is `6`.
     * * Action: `curr.next = curr.next.next`.
     * * Result: `dummy` -> `6` -> `6` -> `3`. (**`curr` stays at `dummy**`).
     *
     *
     * 3. **Step 2**: `curr.next.val` is **still** `6` (the second one).
     * * Action: `curr.next = curr.next.next`.
     * * Result: `dummy` -> `6` -> `3`. (**`curr` stays at `dummy**`).
     *
     *
     * 4. **Step 3**: `curr.next.val` is **still** `6` (the third one).
     * * Action: `curr.next = curr.next.next`.
     * * Result: `dummy` -> `3`. (**`curr` stays at `dummy**`).
     *
     *
     * 5. **Step 4**: `curr.next.val` is `3`.
     * * Action: `curr = curr.next`. (**Now we move `curr` to the node with `3**`).
     *
     *
     *
     */
    public ListNode removeElements(ListNode head, int val) {
        // 1. Create a dummy node that points to the head
        // This helps if we need to delete the very first node
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // 2. Use a 'curr' pointer to traverse the list
        ListNode curr = dummy;

        // 3. Look ahead at the NEXT node
        while (curr.next != null) {
            if (curr.next.val == val) {
                // Found the value! Skip the next node
                curr.next = curr.next.next;
            } else {
                // Not the value, just move the pointer forward
                curr = curr.next;
            }
        }

        // 4. Return the actual head (which is dummy.next)
        return dummy.next;
    }


    // V0-1
    // IDEA: LINKED LIST (GPT)
    public ListNode removeElements_0_1(ListNode head, int val) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode cur = dummy;

        while (cur.next != null) {
            if (cur.next.val == val) {
                cur.next = cur.next.next; // remove node
            } else {
                cur = cur.next; // move forward
            }
        }

        return dummy.next;
    }



    // V0-5
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode removeElements_0_5(ListNode head, int val) {

        if (head == null){
            return null;
        }

        if (String.valueOf(val).equals(null)){
            return head;
        }

        ListNode ans = new ListNode();
        ListNode res = ans;

        while(head != null){
            if (head.val != val){
                ans.next = new ListNode(head.val);
                ans = ans.next;
            }

            head = head.next;
        }

        return res.next;
    }


    // V1
    // https://leetcode.com/problems/remove-linked-list-elements/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode removeElements_2(ListNode head, int val) {
        ListNode sentinel = new ListNode(0);
        sentinel.next = head;

        ListNode prev = sentinel, curr = head;
        while (curr != null) {
            if (curr.val == val) prev.next = curr.next;
            else prev = curr;
            curr = curr.next;
        }
        return sentinel.next;
    }




}
