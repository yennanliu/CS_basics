package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/linked-list-components/description/

import LeetCodeJava.DataStructure.ListNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 817. Linked List Components
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given the head of a linked list containing unique integer values and an integer array nums that is a subset of the linked list values.
 *
 * Return the number of connected components in nums where two values are connected if they appear consecutively in the linked list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [0,1,2,3], nums = [0,1,3]
 * Output: 2
 * Explanation: 0 and 1 are connected, so [0, 1] and [3] are the two connected components.
 * Example 2:
 *
 *
 * Input: head = [0,1,2,3,4], nums = [0,3,1,4]
 * Output: 2
 * Explanation: 0 and 1 are connected, 3 and 4 are connected, so [0, 1] and [3, 4] are the two connected components.
 *
 *
 * Constraints:
 *
 * The number of nodes in the linked list is n.
 * 1 <= n <= 104
 * 0 <= Node.val < n
 * All the values Node.val are unique.
 * 1 <= nums.length <= n
 * 0 <= nums[i] < n
 * All the values of nums are unique.
 *
 */
public class LinkedListComponents {
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode() {}
     *     ListNode(int val) { this.val = val; }
     *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    // V0
    // IDEA: SET + LINKED LIST OP
    public int numComponents(ListNode head, int[] nums) {

//        // edge
//        if (head.next == null && nums.length == 1){
//            if(head.val == nums[0]){
//                return 1;
//            }
//            return 0;
//        }

        Set<Integer> set = new HashSet<>();
        for(int x: nums){
            set.add(x);
        }

        int cnt = 0;
        boolean pvevInSet = false;

        while(head != null){

            if(!set.contains(head.val)){
                if(pvevInSet){
                    cnt += 1;
                }
                pvevInSet = false;
            }else{
                pvevInSet = true;
            }

            head = head.next;
        }

        return pvevInSet ? cnt+1 : cnt;
    }

    // V1
    // IDEA: set, linkedlist (gpt)
    public int numComponents_1(ListNode head, int[] nums) {
        // Convert nums array to a HashSet for O(1) lookups
        Set<Integer> numsSet = new HashSet<>();
        for (int num : nums) {
            numsSet.add(num);
        }

        int count = 0;
        boolean inComponent = false;

        // Traverse the linked list
        while (head != null) {
            if (numsSet.contains(head.val)) {
                // Start a new component if not already in one
                if (!inComponent) {
                    count++;
                    inComponent = true;
                }
            } else {
                // End the current component
                inComponent = false;
            }
            head = head.next;
        }

        return count;
    }

    // V2
    // https://leetcode.com/problems/linked-list-components/editorial/
    // IDEA: GROUPING
    public int numComponents_2(ListNode head, int[] G) {
        Set<Integer> Gset = new HashSet();
        for (int x : G)
            Gset.add(x);

        ListNode cur = head;
        int ans = 0;

        while (cur != null) {
            if (Gset.contains(cur.val) &&
                    (cur.next == null || !Gset.contains(cur.next.val)))
                ans++;
            cur = cur.next;
        }

        return ans;
    }

}
