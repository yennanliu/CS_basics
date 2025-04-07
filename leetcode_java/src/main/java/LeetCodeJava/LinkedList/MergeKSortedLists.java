package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/merge-k-sorted-lists/
/**
 * 23. Merge k Sorted Lists
 * Solved
 * Hard
 * Topics
 * Companies
 * You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
 *
 * Merge all the linked-lists into one sorted linked-list and return it.
 *
 *
 *
 * Example 1:
 *
 * Input: lists = [[1,4,5],[1,3,4],[2,6]]
 * Output: [1,1,2,3,4,4,5,6]
 * Explanation: The linked-lists are:
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * merging them into one sorted list:
 * 1->1->2->3->4->4->5->6
 * Example 2:
 *
 * Input: lists = []
 * Output: []
 * Example 3:
 *
 * Input: lists = [[]]
 * Output: []
 *
 *
 * Constraints:
 *
 * k == lists.length
 * 0 <= k <= 104
 * 0 <= lists[i].length <= 500
 * -104 <= lists[i][j] <= 104
 * lists[i] is sorted in ascending order.
 * The sum of lists[i].length will not exceed 104.
 *
 *
 *
 */
import LeetCodeJava.DataStructure.ListNode;

import java.util.*;


//public class ListNode {
//      int val;
//      ListNode next;
//      ListNode() {}
//      ListNode(int val) { this.val = val; }
//      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
//  }

public class MergeKSortedLists {

    // V0
    // IDEA: LINKED LIST -> ARRAY -> SORT -> LINKED LIST
    public ListNode mergeKLists(ListNode[] lists) {
        // edge
        if(lists == null || lists.length == 0){
            return null; // ??
        }
        if(lists.length == 1){
            return lists[0];
        }
        // TODO: optimize below
        List<Integer> list = new ArrayList<>();
        for(ListNode node: lists){
            while(node != null){
                list.add(node.val);
                node = node.next;
            }

        }

        // sort (small -> big)
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        ListNode dummy = new ListNode();
        ListNode dummy2 = dummy; // ???

        for(Integer x: list){
            ListNode _node = new ListNode(x);
            dummy.next = _node;
            dummy = _node;
        }

        return dummy2.next;
    }

    // V0-1
    // IDEA : to array -> sort -> add to new linked list
    public ListNode mergeKLists_0_1(ListNode[] lists) {

        if (lists == null || lists.length == 0) {
            // NOTE !! need to return null below
            return null;
        }

        ListNode node = new ListNode();
        ListNode head = node;

        List<Integer> tmp = new ArrayList();
        for (ListNode _node : lists){
            // double check if correct method : _node.next != null
            // (check if node is not null)
            while (_node != null){
                tmp.add(_node.val);
                _node = _node.next;
            }
        }

        //System.out.println(tmp);
        // TODO : double check
        tmp.sort(Comparator.comparing(x -> x));
        System.out.println(tmp);

        for (Integer i : tmp){
            node.next  = new ListNode(i);
            node = node.next;
            //System.out.println(i);
        }

        return head.next;
    }

    // V1-1
    // https://neetcode.io/problems/merge-k-sorted-linked-lists
    // IDEA: BRUTE FORCE
    public ListNode mergeKLists_1_1(ListNode[] lists) {
        List<Integer> nodes = new ArrayList<>();
        for (ListNode lst : lists) {
            while (lst != null) {
                nodes.add(lst.val);
                lst = lst.next;
            }
        }
        Collections.sort(nodes);

        ListNode res = new ListNode(0);
        ListNode cur = res;
        for (int node : nodes) {
            cur.next = new ListNode(node);
            cur = cur.next;
        }
        return res.next;
    }


    // V1-2
    // https://neetcode.io/problems/merge-k-sorted-linked-lists
    // IDEA: Iteration
    public ListNode mergeKLists_1_2(ListNode[] lists) {
        ListNode res = new ListNode(0);
        ListNode cur = res;

        while (true) {
            int minNode = -1;
            for (int i = 0; i < lists.length; i++) {
                if (lists[i] == null) {
                    continue;
                }
                if (minNode == -1 || lists[minNode].val > lists[i].val) {
                    minNode = i;
                }
            }

            if (minNode == -1) {
                break;
            }
            cur.next = lists[minNode];
            lists[minNode] = lists[minNode].next;
            cur = cur.next;
        }

        return res.next;
    }

    // V1-3
    // https://neetcode.io/problems/merge-k-sorted-linked-lists
    // IDEA:  Merge Lists One By One
    public ListNode mergeKLists_1_3(ListNode[] lists) {
        if (lists.length == 0) return null;

        for (int i = 1; i < lists.length; i++) {
            lists[i] = merge_1_3(lists[i], lists[i - 1]);
        }
        return lists[lists.length - 1];
    }

    private ListNode merge_1_3(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }

            curr = curr.next;
        }

        if (l1 != null) {
            curr.next = l1;
        } else {
            curr.next = l2;
        }

        return dummy.next;
    }

    // V1-4
    // https://neetcode.io/problems/merge-k-sorted-linked-lists
    // IDEA: HEAP
    public ListNode mergeKLists_1_4(ListNode[] lists) {
        if (lists.length == 0) return null;

        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }

        ListNode res = new ListNode(0);
        ListNode cur = res;
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            cur.next = node;
            cur = cur.next;

            node = node.next;
            if (node != null) {
                minHeap.offer(node);
            }
        }
        return res.next;
    }


    // V1-5
    // https://neetcode.io/problems/merge-k-sorted-linked-lists
    // IDEA: Divide And Conquer (Recursion)
    public ListNode mergeKLists_1_5(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        return divide(lists, 0, lists.length - 1);
    }

    private ListNode divide(ListNode[] lists, int l, int r) {
        if (l > r) {
            return null;
        }
        if (l == r) {
            return lists[l];
        }

        int mid = l + (r - l) / 2;
        ListNode left = divide(lists, l, mid);
        ListNode right = divide(lists, mid + 1, r);

        return conquer(left, right);
    }

    private ListNode conquer(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }

            curr = curr.next;
        }

        if (l1 != null) {
            curr.next = l1;
        } else {
            curr.next = l2;
        }

        return dummy.next;
    }

    // V1-6
    // https://neetcode.io/problems/merge-k-sorted-linked-lists
    // IDEA: Divide And Conquer (Iteration)
    public ListNode mergeKLists_1_6(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }

        while (lists.length > 1) {
            List<ListNode> mergedLists = new ArrayList<>();
            for (int i = 0; i < lists.length; i += 2) {
                ListNode l1 = lists[i];
                ListNode l2 = (i + 1) < lists.length ? lists[i + 1] : null;
                mergedLists.add(mergeList(l1, l2));
            }
            lists = mergedLists.toArray(new ListNode[0]);
        }
        return lists[0];
    }

    private ListNode mergeList(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode();
        ListNode tail = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }

        if (l1 != null) {
            tail.next = l1;
        }
        if (l2 != null) {
            tail.next = l2;
        }

        return dummy.next;
    }


    // V2
    // IDEA : MERGE 2 LINKED LIST ONE BY ONE
    // https://leetcode.com/problems/merge-k-sorted-lists/solutions/3285930/100-faster-c-java-python/
    public ListNode mergeKLists_2(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        return mergeKListsHelper(lists, 0, lists.length - 1);
    }

    private ListNode mergeKListsHelper(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        if (start + 1 == end) {
            return merge(lists[start], lists[end]);
        }
        int mid = start + (end - start) / 2;
        ListNode left = mergeKListsHelper(lists, start, mid);
        ListNode right = mergeKListsHelper(lists, mid + 1, end);
        return merge(left, right);
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }

        curr.next = (l1 != null) ? l1 : l2;

        return dummy.next;
    }

    // V3
    // IDEA : ALL METHODS COMPARISON
    // https://leetcode.com/problems/merge-k-sorted-lists/solutions/429518/java-summary-of-all-solutions-b-f-minpq-divide-and-conquer/


    // V4
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V5
    // IDEA :  Compare one by one
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V6
    // IDEA : Optimize Approach 2 by Priority Queue
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V7
    // IDEA : Merge lists one by one
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V8
    // IDEA : Merge with Divide And Conquer
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


}
