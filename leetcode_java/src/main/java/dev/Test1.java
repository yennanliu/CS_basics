package dev;

import LeetCodeJava.DataStructure.ListNode;

import java.sql.Array;
import java.util.*;

public class Test1 {

    public ListNode swapPairs(ListNode head) {

        while (head != null){

            ListNode firstNode = head;
            ListNode secondNode = head.next;

        }
        return null;
    }

    public static void main(String[] args) {
//        String[] my_array = new String[10];
//        my_array[1] = "wef";
//        my_array[2] = "xxxf";
//
//        System.out.println(String.valueOf(my_array[1]));

        // reverse list
        List<Integer> l1 = new ArrayList<Integer>();
        l1.add(10);
        l1.add(-1);
        l1.add(99);
        System.out.println(l1);
        Collections.reverse(l1);
        /**
         *  src code:
         *
         *     @SuppressWarnings({"rawtypes", "unchecked"})
         *     public static void reverse(List<?> list) {
         *         int size = list.size();
         *         if (size < REVERSE_THRESHOLD || list instanceof RandomAccess) {
         *             for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
         *                 swap(list, i, j);
         *         } else {
         *             // instead of using a raw type here, it's possible to capture
         *             // the wildcard but it will require a call to a supplementary
         *             // private method
         *             ListIterator fwd = list.listIterator();
         *             ListIterator rev = list.listIterator(size);
         *             for (int i=0, mid=list.size()>>1; i<mid; i++) {
         *                 Object tmp = fwd.next();
         *                 fwd.set(rev.previous());
         *                 rev.set(tmp);
         *             }
         *         }
         *     }
         */
        System.out.println(l1);


        System.out.println("========================");

        // https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Sort/MeetingRooms.java
        int[][] intervals = {{0,1}, {2,3}, {3,4}};
        //System.out.println(Arrays.stream(intervals).toArray().toString());
        // Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(intervals, (x,y) -> Integer.compare(x[1], y[1]));

        int[] _array = {0,1,2,3};
        System.out.println(_array);
    }

    class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }


    // https://leetcode.com/problems/remove-nth-node-from-end-of-list/
    // idea : slow - fast pointers
    // NOTE !!! Given the head of a linked list, remove the nth node from the end of the list and return its head.
    // FROM THE END
    public ListNode removeNthFromEnd(ListNode head, int n) {

        if (head == null || head.next == null){
            return null;
        }

        if (n < 0){
            return null;
        }

//        ListNode head_ = head;
//        int len = 0;
//        while (head_ != null){
//            len += 1;
//            head_ = head_.next;
//        }

        ListNode dummy = new ListNode(0);

        dummy.next = head;

        ListNode slow = dummy;
        ListNode fast = dummy;

        // move fast n+1 steps
        //int j = len - n;
        for (int i = 0; i < n; i++){
            fast = fast.next;
        }

        // move fast, and slow on the same time, till fast reach the end of listNode
        while (fast != null){
            fast = fast.next;
            slow = slow.next;
        }

        // return slow as result
        slow = slow.next.next;
        return dummy.next;

        //return null;
    }

    /**
     *  "abcabcbb"
     *   lr
     *
     *   "abcabcbb"
     *    l r
     *
     *    "abcabcbb"
     *     l  r
     *
     *     "abcabcbb"
     *       l r
     *
     *
     *     "pwwkew"
     *      lr
     *
     *      "pwwkew"
     *       l r
     *
     *     "pwwkew"
     *       lr
     *
     *     "pwwkew"
     *        l
     *        r
     *
     *    "pwwkew"
     *        l
     *        r
     *
     *
     *  5:25
     *
     */

    // https://leetcode.com/problems/longest-substring-without-repeating-characters/
    // IDEA : sliding window + hashset
    public int lengthOfLongestSubstring(String s) {

        if (s == null || s.length() == 0){
            return 0;
        }

        HashSet<String> set = new HashSet<>();
        int l = 0;
        int ans = 1;
        int r = 0;
        while (r < s.length() && l < s.length()){
            System.out.println("r = " + r + " l = " + l + " set = " + set + " ans =  " + ans);
            String cur = String.valueOf(s.charAt(r));
            if (!set.contains(cur)){
                set.add(cur);
                //ans += 1;
                ans = Math.max(ans, r - l + 1);
                r += 1;
                //continue;
            }else{
                while (set.contains(cur) && l < r){
                    // keep moving left pointer, if sub array element still in set
                    l += 1;
                }
                // finally remove cur from set
                set.remove(cur);
            }
        }

        return ans;
    }

    // V2
    // IDEA : SLIDING WINDOW
    // https://leetcode.com/problems/longest-substring-without-repeating-characters/editorial/
    public int lengthOfLongestSubstring_3(String s) {

        Map<String, Integer> map = new HashMap();

        // left pointer
        int left = 0;
        // right pointer
        int right = 0;
        // final result
        int res = 0;

        while (right < s.length()) {

            String cur = String.valueOf(s.charAt(right));

            // NOTE !!! we add element to map first
            if (map.containsKey(cur)){
                map.put(cur, map.get(cur)+1);
            }else{
                map.put(cur, 1);
            }

            // NOTE !!! if map has element -> duplicated elelment
            //          -> keep remove count from element and move left pointer to right
            while (map.get(cur) > 1) {
                String l = String.valueOf(s.charAt(left));
                map.put(l, map.get(l) - 1);
                left += 1;
            }

            res = Math.max(res, right - left + 1);

            right += 1;
        }
        return res;
    }

}
