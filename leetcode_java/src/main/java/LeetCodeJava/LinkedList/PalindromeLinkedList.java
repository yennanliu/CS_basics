package LeetCodeJava.LinkedList;

import LeetCodeJava.DataStructure.ListNode;

import java.util.ArrayList;
import java.util.List;

public class PalindromeLinkedList {

    // V0
    // IDEA : ARRAY + LINKED LIST
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

}
