package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/partition-list/description/

import DataStructure.ListNode;

/**
 * 86. Partition List
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.
 *
 * You should preserve the original relative order of the nodes in each of the two partitions.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,4,3,2,5,2], x = 3
 * Output: [1,2,2,4,3,5]
 * Example 2:
 *
 * Input: head = [2,1], x = 2
 * Output: [1,2]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 200].
 * -100 <= Node.val <= 100
 * -200 <= x <= 200
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 *
 */
public class PartitionList {

    // V0
//    public ListNode partition(ListNode head, int x) {
//
//    }

    // V1
    // https://leetcode.ca/2016-02-24-86-Partition-List/
    public ListNode partition_1(ListNode head, int x) {
        ListNode d1 = new ListNode();
        ListNode d2 = new ListNode();
        ListNode t1 = d1, t2 = d2;
        while (head != null) {
            if (head.val < x) {
                t1.next = head;
                t1 = t1.next;
            } else {
                t2.next = head;
                t2 = t2.next;
            }
            head = head.next;
        }
        t1.next = d2.next;
        t2.next = null;
        return d1.next;
    }


    // V2
    // https://leetcode.com/problems/partition-list/solutions/6750680/video-solution-by-niits-yzyp/
    public ListNode partition_2(ListNode head, int x) {
        ListNode slist = new ListNode();
        ListNode blist = new ListNode();
        ListNode small = slist;
        ListNode big = blist;

        while (head != null) {
            if (head.val < x) {
                small.next = head;
                small = small.next;
            } else {
                big.next = head;
                big = big.next;
            }

            head = head.next;
        }

        small.next = blist.next;
        big.next = null;

        return slist.next;
    }


    // V3
    // https://leetcode.com/problems/partition-list/solutions/7234561/beats-100-very-very-very-easy-to-underst-eppp/
    public ListNode partition_3(ListNode head, int x) {
        ListNode temp = head;
        if (head == null) {
            return null;
        }

        if (head.next == null)
            return head;

        int value = x;
        ListNode dummy = new ListNode();
        ListNode dummy2 = new ListNode();
        ListNode tempd1 = dummy;
        ListNode tempd2 = dummy2;
        ListNode itt = head;
        while (itt != null) {
            if (itt.val < value) {
                tempd1.next = itt;
                tempd1 = tempd1.next;
            } else {
                tempd2.next = itt;
                tempd2 = tempd2.next;
            }
            itt = itt.next;
        }

        tempd2.next = null;
        tempd1.next = dummy2.next;

        return dummy.next;

    }



}
