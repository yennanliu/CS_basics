package LeetCodeJava.LinkedList;

import LeetCodeJava.DataStructure.ListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// https://leetcode.com/problems/palindrome-linked-list/description/
/**
 *  234. Palindrome Linked List
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a singly linked list, return true if it is a palindrome or false otherwise.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,2,1]
 * Output: true
 * Example 2:
 *
 *
 * Input: head = [1,2]
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [1, 105].
 * 0 <= Node.val <= 9
 *
 *
 * Follow up: Could you do it in O(n) time and O(1) space?
 *
 */

public class PalindromeLinkedList {

    // V0
    // IDEA : ARRAY + LINKED LIST
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome(ListNode head) {

        if (head == null || head.next == null){
            return true;
        }

        List<Integer> tmp = new ArrayList<>();
        while (head != null){
            tmp.add(head.val);
            head = head.next;
        }

        System.out.println("tmp = " + tmp);
        int l = 0;
        int r = tmp.size()-1;
        while (r > l){
            if (!tmp.get(l).equals(tmp.get(r))){
                return false;
            }
            l += 1;
            r -= 1;
        }

        return true;
    }

    // V1
    // https://leetcode.com/problems/palindrome-linked-list/solutions/7047915/video-two-pointers-by-niits-wwqq/
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isPalindrome_1(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        ListNode prev = null;
        while (slow != null) {
            ListNode temp = slow.next;
            slow.next = prev;
            prev = slow;
            slow = temp;
        }

        ListNode first = head;
        ListNode second = prev;

        while (second != null) {
            if (first.val != second.val) {
                return false;
            }
            first = first.next;
            second = second.next;
        }

        return true;
    }

    // V2-1
    // https://leetcode.com/problems/palindrome-linked-list/solutions/4908031/interview-approach-4-approach-list-stack-r0wz/
    // IDEA: STACK
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome_2_1(ListNode head) {
        Stack<Integer> stack = new Stack();
        ListNode curr = head;
        while(curr != null) {
            stack.push(curr.val);
            curr = curr.next;
        }
        curr = head;
        while(curr != null && curr.val == stack.pop()) {
            curr = curr.next;
        }
        return curr == null;
    }

    // V2-2
    // https://leetcode.com/problems/palindrome-linked-list/solutions/4908031/interview-approach-4-approach-list-stack-r0wz/
    // IDEA: RECURSION
    ListNode curr;
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome_2_2(ListNode head) {
        curr = head;
        return solve(head);
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean solve(ListNode head) {
        if (head == null)
            return true;
        boolean ans = solve(head.next) && head.val == curr.val;
        curr = curr.next;
        return ans;
    }

    // V2-3
    // https://leetcode.com/problems/palindrome-linked-list/solutions/4908031/interview-approach-4-approach-list-stack-r0wz/
    // IDEA: LINKED LIST
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isPalindrome_2_3(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode rev = reverse(slow.next); // reverse second list
        slow.next = null;
        while (rev != null) {
            if (head.val != rev.val) {
                return false;
            }
            head = head.next;
            rev = rev.next;
        }
        return true;
    }


}
