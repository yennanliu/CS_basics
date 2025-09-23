package dev;

import LeetCodeJava.DataStructure.ListNode;

import java.util.*;

public class workspace16 {

    // LC 876
    // 14.50 - 15.00 pm
    /**
     *  IDEA: LINKED LIST
     *
     */
    public ListNode middleNode(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        // fast, slow pointers
        ListNode fast = head; // ??
        ListNode slow = head; // ??

        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }

    // LC 142
    // 14.57 - 15.10 pm
    /**
     *
     *  -> return the node where the cycle begins
     *  -> if NO cycle, return null
     *
     *  IDEA 1) LINKED LIST OP
     *
     */
    public ListNode detectCycle(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return null;
        }
        HashSet<ListNode> set = new HashSet<>();
        while(head != null){
            if(set.contains(head)){
                return head; // ???
            }
            set.add(head);
            head = head.next;
        }

        return null;
    }

    // LC 82
    // 9.33 - 10.00 am
    /**
     * IDEA 1) LINKED LIST OP
     *
     * IDEA 2) LINKED LIST -> array -> dedup -> linkedlist
     *
     *
     */
    // 10.18 - 10.23 am
    // IDEA 1) pure LINKED LIST  op + 2 pointers ???
    public ListNode deleteDuplicates(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        ListNode dummy = new ListNode();
        ListNode res = dummy;

        ListNode _prev = null;
        ListNode _next = null;

        while(head != null){

            _prev = head;

            // case 1) _prev != cur
            _prev.next = new ListNode(head.val); // ???

            // case 2) _prev == cur
            while(_prev.val == head.val){
                _prev = _prev.next;
            }



            head = head.next;
        }

        return res.next;
    }
    // IDEA 2) LINKED LIST -> array -> dedup -> linkedlist
    public ListNode deleteDuplicates_100(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }
        //Set<Integer> set = new HashSet<>(); // order
        Set<Integer> set = new LinkedHashSet<>();
        Map<Integer, Integer> map =new HashMap<>(); // {val : cnt}

        while(head != null){
            Integer val = head.val;
            map.put(val, map.getOrDefault(val, 0) + 1);
            set.add(val);
            head = head.next;
        }
        ListNode dummy = new ListNode();
        ListNode res = dummy; // ???

        System.out.println(">>> map = " + map);
        System.out.println(">>> set = " + set);

        for(Integer x: set){
            if(map.get(x) == 1){
                ListNode node = new ListNode(x);
                dummy.next = node;
                dummy = node;
            }
        }

        System.out.println(">>> dummy = " + dummy);

        return res.next;
    }


    // LC 61
    public ListNode rotateRight(ListNode head, int k) {

        return null;
    }

}
