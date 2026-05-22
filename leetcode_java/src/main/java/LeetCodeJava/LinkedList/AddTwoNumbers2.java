package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/add-two-numbers-ii/description/

import java.math.BigInteger;
import java.util.Stack;

/**
 * 445. Add Two Numbers II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: l1 = [7,2,4,3], l2 = [5,6,4]
 * Output: [7,8,0,7]
 * Example 2:
 *
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [8,0,7]
 * Example 3:
 *
 * Input: l1 = [0], l2 = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * The number of nodes in each linked list is in the range [1, 100].
 * 0 <= Node.val <= 9
 * It is guaranteed that the list represents a number that does not have leading zeros.
 *
 *
 * Follow up: Could you solve it without reversing the input lists?
 *
 *
 */
public class AddTwoNumbers2 {

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {}

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }


    // V0
//    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
//
//    }


    // V0-1
    // IDEA: STR -> INT -> LINKED LIST (GPT)
    public ListNode addTwoNumbers_0_1(ListNode l1, ListNode l2) {

        /** NOTE !!!
         *
         *
         *  Linked list -> str
         *
         */
        // convert l1 -> string
        StringBuilder sb1 = new StringBuilder();
        while (l1 != null) {
            sb1.append(l1.val);
            l1 = l1.next;
        }

        // convert l2 -> string
        StringBuilder sb2 = new StringBuilder();
        while (l2 != null) {
            sb2.append(l2.val);
            l2 = l2.next;
        }

        /** NOTE !!!
         *
         *  1.  Str -> big integer
         *
         *
         *  2. use `BigInteger` to avoid overflow
         *
         */
        // use BigInteger to avoid overflow
        BigInteger num1 = new BigInteger(sb1.toString());
        BigInteger num2 = new BigInteger(sb2.toString());

        /** NOTE !!!
         *
         *
         *  BigInteger -> str
         *
         */
        String sum = num1.add(num2).toString();

        /** NOTE !!!
         *
         *
         *  we init dummy,
         *  and assign its val to cur,
         *  -> so cur can be used to `re-connect` nodes
         *  -> and dummy can be returned as the `placeholder` of final result
         *
         */
        // build linked list
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;

        for (char ch : sum.toCharArray()) {
            cur.next = new ListNode(ch - '0');
            cur = cur.next;
        }

        return dummy.next;
    }



    // V0-2
    // IDEA: STACK (gemini)
    public ListNode addTwoNumbers_0_2(ListNode l1, ListNode l2) {
        // Edge cases
        if (l1 == null)
            return l2;
        if (l2 == null)
            return l1;

        // Two stacks to store nodes so we can process them backward
        /** NOTE !!!
         *
         *
         *   STACK: FILO
         *
         */
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();

        // Push all digits of l1 onto s1
        while (l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }

        // Push all digits of l2 onto s2
        while (l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }

        int carry = 0;
        ListNode head = null;

        /** NOTE !!!
         *
         *
         *   STACK: FILO
         *
         */
        // Process digits from right to left until both stacks are empty AND carry is 0
        while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {
            int sum = carry;

            if (!s1.isEmpty()) {
                sum += s1.pop();
            }
            if (!s2.isEmpty()) {
                sum += s2.pop();
            }

            // Calculate the new carry
            carry = sum / 10;

            // Create a new node with the single-digit remainder
            ListNode newNode = new ListNode(sum % 10);

            // Reconstruct the list forward:
            // Connect our new node to the front of the previously built head
            newNode.next = head;
            head = newNode;
        }

        return head;
    }



    // V1-1
    // IDEA: Reverse Given Linked Lists
    // https://leetcode.com/problems/add-two-numbers-ii/editorial/
    public ListNode reverseList_1_1(ListNode head) {
        ListNode prev = null, temp;
        while (head != null) {
            // Keep the next node
            temp = head.next;
            // Reverse the link
            head.next = prev;
            // Update the previous node and the current node.
            prev = head;
            head = temp;
        }
        return prev;
    }

    public ListNode addTwoNumbers_1_1(ListNode l1, ListNode l2) {
        ListNode r1 = reverseList_1_1(l1);
        ListNode r2 = reverseList_1_1(l2);

        int totalSum = 0, carry = 0;
        ListNode ans = new ListNode();
        while (r1 != null || r2 != null) {
            if (r1 != null) {
                totalSum += r1.val;
                r1 = r1.next;
            }
            if (r2 != null) {
                totalSum += r2.val;
                r2 = r2.next;
            }

            ans.val = totalSum % 10;
            carry = totalSum / 10;
            ListNode head = new ListNode(carry);
            head.next = ans;
            ans = head;
            totalSum = carry;
        }

        return carry == 0 ? ans.next : ans;
    }


    // V1-2
    // IDEA: STACK
    // https://leetcode.com/problems/add-two-numbers-ii/editorial/
    public ListNode addTwoNumbers_1_2(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<Integer>();
        Stack<Integer> s2 = new Stack<Integer>();

        while (l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }
        ;
        while (l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }

        int totalSum = 0, carry = 0;
        ListNode ans = new ListNode();
        while (!s1.empty() || !s2.empty()) {
            if (!s1.empty()) {
                totalSum += s1.pop();
            }
            if (!s2.empty()) {
                totalSum += s2.pop();
            }

            ans.val = totalSum % 10;
            carry = totalSum / 10;
            ListNode head = new ListNode(carry);
            head.next = ans;
            ans = head;
            totalSum = carry;
        }

        return carry == 0 ? ans.next : ans;
    }



    // V2



    // V3



}
