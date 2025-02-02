package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-nth-node-from-end-of-list/
/**
 *  19. Remove Nth Node From End of List
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given the head of a linked list, remove the nth node from the end of the list and return its head.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5], n = 2
 * Output: [1,2,3,5]
 * Example 2:
 *
 * Input: head = [1], n = 1
 * Output: []
 * Example 3:
 *
 * Input: head = [1,2], n = 1
 * Output: [1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is sz.
 * 1 <= sz <= 30
 * 0 <= Node.val <= 100
 * 1 <= n <= sz
 *
 *
 * Follow up: Could you do this in one pass?
 *
 */
import LeetCodeJava.DataStructure.ListNode;

public class RemoveNthNodeFromEndOfList {

    // V0
    // IDEA : 2 POINTERS
    // https://www.youtube.com/watch?v=oMzRxtXvDbA&list=PL-qDGN2q6cbClYCRpJ2pyrlhYK9VjZmL3&index=13
    public ListNode removeNthFromEnd(ListNode head, int n) {

        if (head == null){
            return head;
        }

        if (head.next == null && head.val == n){
            return null;
        }

        // move fast pointer only with n+1 step
        // 2 cases:
        //   - 1) node count is even
        //   - 2) node count is odd
        /** NOTE !! we init dummy pointer, and let fast, slow pointers point to it */
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        // NOTE here
        ListNode fast = dummy;
        ListNode slow = dummy;
    /**
     *  Explanation V1:
     *
     *   -> So we have fast, and slow pointer,
     *   if we move fast N steps first,
     *   then slow starts to move
     *      -> fast, slow has N step difference
     *      -> what's more, when fast reach the end,
     *      -> fast, slow STILL has N step difference
     *      -> and slow has N step difference with the end,
     *      -> so we can remove N th pointer accordingly
     *
     *  Explanation V2:
     *
     *
     *   // NOTE !!! we let fast pointer move N+1 step first
     *   // so before fast pointers reach the end,  fast, and slow pointers move together
     *   // we are sure that slow pointer is at N-1 node
     *   // so All we need to do is :
     *   // point slow.next to slow.next.next
     *   // then we remove N node from linked list
     */
    /**
     *  DEMO
     *
     *  head = 1 -> 2 -> 3 -> 4 -> 5, n = 1
     *
     *  ->  n = 1 (4 need to be removed)
     *
     *
     *  1 -> 2 -> 3 -> 4 -> 5
     *                 x
     *
     *  so, ops as below:
     *
     * 1 -> 2 -> 3 -> 4 -> 5
     * f
     *
     *
     * // fast move n+1 (1+1=2) steps
     * 1 -> 2 -> 3 -> 4 -> 5
     *           f
     *
     * // then move slow, fast on the same time, and STOP while fast reach the end
     * 1 -> 2 -> 3 -> 4 -> 5
     * s         f
     *
     * 1 -> 2 -> 3 -> 4 -> 5
     *      s         f
     *
     * 1 -> 2 -> 3 -> 4 -> 5
     *           s         f
     *
     *
     * // repoint slow node to `next next` node
     * 1 -> 2 -> 3 -> 4 -> 5
     *           s  ------> 5
     *
     */
    for (int i = 1; i <= n + 1; i++) {
            //System.out.println("i = " + i);
            fast = fast.next;
        }

        // move fast and slow pointers on the same time
        while (fast != null){
            fast = fast.next;
            slow = slow.next;
        }

        // NOTE here
        slow.next = slow.next.next;
        // NOTE !!! we return dummy.next instead of slow
        return dummy.next;
    }

    // V0-1
    // IDEA : get len of linkedlist, and re-point node
    public ListNode removeNthFromEnd_0_1(ListNode head, int n) {

        if (head.next == null){
            return null;
        }

        // below op is optional
//        if (head.next.next == null){
//            if (n == 1){
//                return new ListNode(head.val);
//            }
//            return new ListNode(head.next.val);
//        }

        // get len
        int len = 0;
        ListNode head_ = head;
        while (head_ != null){
            head_ = head_.next;
            len += 1;
        }

        ListNode root = new ListNode();
        /** NOTE !!! root_ is the actual final result */
        ListNode root_ = root;

        // if n == len
        if (n == len){
            head = head.next;
            root.next = head;
            root = root.next;
        }

        /**
         *  IDEA: get length of linked list,
         *        then if want to delete n node from the end of linked list,
         *        -> then we need to stop at "len - n" idx,
         *        -> and reconnect "len - n" idx to "len -n + 2" idx
         *        -> (which equals delete "n" idx node
         *
         *
         *  Consider linked list below :
         *
         *   0, 1, 2 , 3, 4 .... k-2, k-1, k
         *
         *   if n = 1, then "k-1" is the node to be removed.
         *   -> so we find "k-2" node, and re-connect it to "k" node
         */
        /** NOTE !!!
         *
         *  idx is the index, that we "stop",  and re-connect
         *  from idx to its next next node (which is the actual "delete" node op
         */
        int idx = len - n; // NOTE !!! this
        while (idx > 0){
            root.next = head;
            root = root.next;
            head = head.next;
            idx -= 1;
        }

        ListNode next = head.next;
        root.next = next;

        return root_.next;
    }

    // V0-2
    // IDEA: FAST, SLOW POINTERS (GPT)
    public ListNode removeNthFromEnd_0_2(ListNode head, int n) {
        // Create a dummy node to simplify edge cases
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;

        // Move `first` n+1 steps ahead
        for (int i = 0; i <= n; i++) {
            first = first.next;
        }

        // Move both `first` and `second` until `first` reaches the end
        while (first != null) {
            first = first.next;
            second = second.next;
        }

        // Remove nth node
        second.next = second.next.next;

        return dummy.next; // Return new head
    }

    // V1
    // IDEA :  Two pass algorithm
    // https://leetcode.com/problems/remove-nth-node-from-end-of-list/editorial/
    public ListNode removeNthFromEnd_2(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        int length  = 0;
        ListNode first = head;
        while (first != null) {
            length++;
            first = first.next;
        }
        length -= n;
        first = dummy;
        while (length > 0) {
            length--;
            first = first.next;
        }
        first.next = first.next.next;
        return dummy.next;
    }

    // V2
    // https://leetcode.com/problems/remove-nth-node-from-end-of-list/editorial/
    public ListNode removeNthFromEnd_3(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

}
