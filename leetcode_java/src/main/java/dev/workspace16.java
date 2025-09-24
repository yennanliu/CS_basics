package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.Recursion.PopulatingNextRightPointersInEachNode;

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
    // 14.32 - 14.43 pm
    /**
     *
     * -> Given the head of a linked list,
     *   `rotate` the list to the` right by k places.`
     *
     *
     *  IDEA 1) LINKED LIST OP
     *
     *
     *
     */
    public ListNode rotateRight(ListNode head, int k) {
        // edge
        if(k == 0 || head == null || head.next == null){
            return head;
        }

        //List<Integer> list = new ArrayList<>();
        Deque<Integer> dequeue = new ArrayDeque<>(); // ???

        ListNode head2 = head;

        // get len
        //int len = 0;
        while(head != null){
           // len += 1;
            //list.add(head.val);
            dequeue.add(head.val);
            head = head.next;

        }

        int len = dequeue.size();

        System.out.println(">>> (before normalized) k = " + k);
        System.out.println(">>> len = " + len);


        // edge
        if(k >= len){
            if(len % k == 0){
                return head2;
            }
            // ??
            k = (k %len);
        }

        System.out.println(">>> (after normalized) k = " + k);

        // `rotate`
        int i = 0;
        while(!dequeue.isEmpty() && i < k){
            // pop `last`
            int val = dequeue.pollLast(); // ??
            // append pop val to first idx
            dequeue.addFirst(val);
            i += 1;
        }

        ListNode dummy = new ListNode();
        ListNode res = dummy;

        System.out.println(">>> dequeue = " + dequeue);

//        for(Integer x: dequeue){
//            ListNode cur = new ListNode(x);
//            dummy.next = cur;
//            dummy = cur;
//        }

        while(!dequeue.isEmpty()){
            ListNode cur = new ListNode(dequeue.pollFirst());
            dummy.next = cur;
            dummy = cur;
        }

        return res.next; // ???
    }

    // LC 101
    // 9.36 - 9.46 am
    /**
     *  IDEA 1): DFS
     *
     */
    public boolean isSymmetric(TreeNode root) {
        // edge
        if (root == null) {
            return true;
        }

        return isSymmetricHelper(root.left, root.right);
    }

    // t1: sub left tree, t2: sub right tree
    private boolean isSymmetricHelper(TreeNode t1, TreeNode t2){
        if(t1 == null && t2 == null){
            return true;
        }
        if(t1 == null || t2 == null){
            return false;
        }
        if(t1.val != t2.val){
            return false;
        }

        // ONLY need to compare `one`  ???
        return isSymmetricHelper(t1.left, t2.right)  &&
                isSymmetricHelper(t1.right, t2.left);
    }


    // LC 100
    // 9.47 - 9.57 am
    /**
     *  IDEA 1) DFS
     *
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        // edge
        if(p == null && q == null){
            return true;
        }
        if(p == null || q == null){
            return false;
        }
        if(p.val != q.val){
            return false;
        }

        return isSameTree(p.left, q.left) &&
                isSameTree(p.right, q.right);
    }


    // LC 226
    // 10.00 - 10.10 am
    /**
     * IDEA 1) DFS
     *
     * IDEA 2) BFS
     *
     *
     */
    public TreeNode invertTree(TreeNode root) {
        // edge
        if(root == null){
            return root;
        }
        // ???
        if(root.left == null && root.right == null){
            return root;
        }
        if(root.left == null || root.right == null){
            if(root.left == null){
                root.left = root.right;
            }else{
                root.right = root.left;
            }
            return root;
        }

        // cache
//        TreeNode _right = root.right;
//        TreeNode _left = root.left;
        // ???
        TreeNode _right = invertTree(root.left); // ????
        TreeNode _left = invertTree(root.right);

        root.left = _left;
        root.right = _right;

        return root;
    }

    //private boolean

    // LC 617
    // 10.19 - 10.29 am
    /**
     *  IDEA 1) DFS
     *
     */
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        // edge
        if(root1 == null && root2 == null){
            return root1;
        }
//        if(root1 == null || root2 == null){
//            if(root2 == null){
//                return root1;
//            }
//            return root2;
//        }

        int val1 = root1 != null ? root1.val: 0;
        int val2 = root2 != null ? root2.val: 0;

        // dfs
        // pre-order traverse (root -> left -> right)
        //int newVal = root1.val + root2.val;
        root1.val = val1 + val2;

        // ????
        TreeNode _left = mergeTrees(root1.left, root2.left);
        TreeNode _right = mergeTrees(root1.right, root2.right);

        root1.left = _left;
        root1.right = _right;

        return root1;
    }


    // LC 116
    // 10.38 - 10.55 AM
    /**  IDEA
     *
     *
     *
     */
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    /**
     *  IDEA 1) BFS (visit layer by layer)
     *
     *  IDEA 2) DFS
     *
     */
    // IDEA 1) BFS (visit layer by layer)
    public Node connect(Node root) {
        // edge
        if(root == null){
            return root;
        }
        if(root.left == null && root.right == null){
            root.next = null;
            return root;
        }

        // bfs
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        //int layer = 0;

        Node dummy = new Node(); // ???
        Node res = dummy;

        while(!q.isEmpty()){
            int _size = q.size();
            System.out.println(">>> _size = " + _size + ", q = " + q);
            //dummy = q.poll();
            Node cur = null;
            for(int i = 0; i < _size; i++){
                cur = q.poll();
                System.out.println(">>> cur = " + cur);
                dummy.next = cur;
                dummy = cur;
            }

            // ???
            dummy.next = null; // ??
        }

        System.out.println(">>> dummy = " + dummy + ", res = " + res);

        return res.next;
    }


}
