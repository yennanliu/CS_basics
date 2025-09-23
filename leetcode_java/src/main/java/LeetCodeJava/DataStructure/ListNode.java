package LeetCodeJava.DataStructure;

// https://leetcode.com/problems/merge-two-sorted-lists/

// V1
//public class ListNode{
//
//    // attr
//    public int val;
//    public ListNode next;
//
//    // constructor
//    public ListNode(){
//
//    }
//
//    public ListNode(int val){
//        this.val = val;
//    }
//
//    ListNode(int val, ListNode next){
//        this.val = val;
//        this.next = next;
//    }
//
//}

// V2
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode() {}
    public ListNode(int val) {
        this.val = val;
    }
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
