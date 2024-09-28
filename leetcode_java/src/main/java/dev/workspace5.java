package dev;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class workspace5 {

    // LC 430
    // 7.23 pm - 7.40 pm
    /*
    // Definition for a Node.
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };
    */
    // recursive

    // attr
    Node res = new Node();
    //Node ans = new Node();

    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    public Node flatten(Node head) {
        if(head == null){
            return head;
        }
        if (head.next == null && head.child == null){
            return head;
        }
        Node node = new Node();
        Node ans = node;

        Node cur = head; // /???

        /**
         *  if without "child"
         *  then it's a regular bi-linked list
         *  -> so we deal with child node via dfs (recursive)
         *  -> and deal with next node via while loop (iteration)
         */
        while(cur != null){

            Node _next = cur.next;
            cur.next = cur.child; // NOTE !!! need to flatten
            cur.next.prev = cur; // NOTE !!! need to connect to prev

            Node _child = this.dfs(cur.child);

            _child.next = _next;

            if (_next != null){
                _next.prev = _child;
            }

            cur.child = null;

            //Node _cur = new Node();

//            while(_child != null){
//                Node _next2 = _child.next;
//                _cur.next = _next2;
//                _child = _next2;
//                _cur = _cur.next;
//            }
//
//            if (_child == null){
//                _cur.next = _next; // ??
//            }

        }

        return ans;
    }

    private Node dfs(Node head){
        if (head == null){
            return null;
        }
        while (head.child != null){
            Node _child = head.child;
            head.child = this.dfs(head);
            head = _child;
        }
        return head;
    }

//    public Node flatten(Node head) {
//
//        Queue<Node> store = new LinkedList<>();
//        /**
//         *     /**
//         *      * Inserts the specified element into this queue if it is possible to do
//         *      * so immediately without violating capacity restrictions.
//         *      * When using a capacity-restricted queue, this method is generally
//         *      * preferable to {@link #add}, which can fail to insert an element only
//         *      * by throwing an exception.
//         */
//        //store.offer(new Node());// ?
//
//        if (head == null){
//            return head;
//        }
//
//        if (head.child == null && head.next == null){
//            return head;
//        }
//
//        Node res = new Node();
//        Node ans = res;
//        while(head.next != null){
//            dfs(head);
//            Node _next = head.next;
//            head = _next;
//        }
//
//        return ans;
//    }
//
//    private void dfs(Node head){
//        while (head.child != null){
//            Node _child = head.child;
//            Node _child_res = this.res.child; // ??
//            this.res.child = _child;
//            head = _child;
//            this.res = _child_res; // ???
//        }
//        //return this.res;
//    }

    // LC 1011
    // 8.23 pm - 8.40 pm
    public int shipWithinDays(int[] weights, int days) {

        if (weights.length==1){
            return weights[0] / days;
        }

        List<Integer> weightsList = new ArrayList<>();
        int maxWeight = 0;
        int totalWeight = 0;
        for (int w : weights){
            weightsList.add(w);
            maxWeight = Math.max(maxWeight, w);
            totalWeight += w;
        }

        int left = maxWeight;
        int right = totalWeight;
        // binary search
        while(right > left){
            int mid = (left + right) / 2;
            int calculatedDays = getDays(weightsList, mid);
            System.out.println(">>> mid = " + mid  + ", maxWeight = " + maxWeight + ", totalWeight = " + totalWeight);
            // need to return max possible speed within D days
            if (calculatedDays <= days){
                right = mid;
            }else{
                left = mid+1;
            }
        }

        return left; // ??? or return mid
    }

    private int getDays(List<Integer> weightsList, int speed){
        int cur = 0;
        int days = 0;
        for (Integer w :weightsList){
            if (cur + w <= speed){
                cur += w;
            }else{
                days += 1;
                cur = w;
            }
        }
        if (cur > 0){
            days += 1;
        }
        System.out.println(">>> speed = " + speed + ", days = " + days);
        return days;
    }

    // LC 939
    // 7.41 - 8.10 pm
    /**
     *  NOTE !!!
     *
     *   Return the minimum area of a rectangle ...
     *
     *
     *   exp 1:
     *
     *   Input: points = [[1,1],[1,3],[3,1],[3,3],[2,2]]
     *   Output: 4
     *
     *
     *
     *   idea :
     *     get min "x len"
     *     and get min "y len"
     *     then ans = x_len * y_len
     *
     */
    public int minAreaRect(int[][] points) {
        // edge case
//        if (points.length == 0 && points[0].length == 0){
//            return 0;
//        }
        if (points.length == 1 || points[0].length == 1) {
            return points.length * points[0].length;
        }
        // init
        int x_len = 0;
        int y_len = 0;
        // find min x_len and min y_len

        return x_len * y_len;
    }

    // LC 1087
    // https://leetcode.ca/2018-11-21-1087-Brace-Expansion/
    // 5.02 pm - 5.20 pm
//    public static void main(String[] args) {
//        System.out.println(this.expand(s));
//    }

    List<String> collected = new ArrayList<>();
    List<String> regular = new ArrayList<>();
    public String[] expand(String s) {

        // no possible to have bracket ("{}")
        if (s.length() < 3){
            return new String[]{s};
        }
        List<String> candidates = new ArrayList<>();
        for (String x : s.split("")){
            Queue<String> q = new LinkedList<>();
            boolean tooAdd = false;
            if (x == "{"){
                tooAdd =  true;
                break;
            }
            else if (!tooAdd){
                q.add(x);
                break;
            }
            else if (x == "}"){
                tooAdd = false;
                StringBuilder sb = new StringBuilder();
                while(!q.isEmpty()){
                    sb.append(q.poll());
                    candidates.add(sb.toString());
                }
            }else{
            }


        }


        // dfs
        //List<String> collected = new ArrayList<>();
        String[] x= (String[]) collected.toArray();
        // order with lexicographical

        return (String[]) collected.toArray(); // TODO : double check
    }

    int strLen = 5; // TODO : fix
    private void dfs(List<String> regular, List<String> candidates, int startIdx, String cur){
        if (candidates.size()==0){
            return;
        }
        if (cur.length() == strLen){
            this.collected.add(cur);
            cur = "";
            return;
        }
        if (cur.length() > strLen){
            cur = "";
            return;
        }

    }




}
