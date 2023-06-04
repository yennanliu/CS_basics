package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/merge-two-sorted-lists/

public class ListNode{

    // attr
    int val;
    ListNode next;

    // constructor
    ListNode(){

    }

    ListNode(int val){
        this.val = val;
    }

    ListNode(int val, ListNode next){
        this.val = val;
        this.next = next;
    }

}
