package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/lru-cache/description/

import java.util.*;

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

public class LRUCache_ {

    // V0
    // IDEA : DICT + QUEUE
    // or DICT + ARRAY
    // TODO : fix below, ref : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Design/lru-cache.py#L48
//    class LRUCache {
//
//        // attr
//        private HashMap<Integer, Integer> map = new HashMap<>();
//        Integer lruKey = null;
//        Queue<Integer> queue = new LinkedList<>();
//        Integer capacity = 0;
//
//        public LRUCache(int capacity) {
//            this.capacity = capacity;
//        }
//
//        public int get(int key) {
//
//            if (!this.map.containsKey(key)){
//                return -1;
//            }
//
//            int val = this.map.get(this.queue.peek());
//            // update LRU cache
//            this.updateStack(val);
//            return val;
//        }
//
//        public void put(int key, int value) {
//
//            this.queue.add(key);
//
//            // case 1) exceed capacity
//            if (this.map.keySet().size() == this.capacity){
//                int toDelete = this.queue.peek();
//                map.remove(toDelete);
//                map.put(key, value);
//                // update LRU cache
//                this.updateStack(key);
//                return;
//            }
//
//            // case 2) key already existed, but size still lower than capacity
//            else if (this.map.containsKey(key)){
//                map.put(key, value);
//                // update LRU cache
//                this.updateStack(key);
//                return;
//            }
//
//            // case 3) key not existed and not exceed capacity
//            else{
//                map.put(key, value);
//                // update LRU cache
//                this.updateStack(key);
//                return;
//            }
//        }
//
//        private void updateStack(int lruKey){
//
//            if (queue.isEmpty()){
//                this.lruKey = lruKey;
//                return;
//            }
//
//            System.out.println(">>> this.queue = " + this.queue.toString());
//            System.out.println(">>> this.queue = " + Arrays.toString(this.queue.toArray()));
//
//            Queue<Integer> newQueue = new LinkedList<>();
//            Integer toAdd = null;
//            while (!this.queue.isEmpty()){
//                int _val = this.queue.poll();
//                if (map.containsKey(_val)){
//                    if (_val == lruKey){
//                        toAdd = _val;
//                    }else{
//                        newQueue.add(_val);
//                    }
//                }
//            }
//
//            if (toAdd != null){
//                newQueue.add(toAdd);
//            }
//
//            this.lruKey = newQueue.peek();
//            this.queue = newQueue;
//        }
//
//    }

    // V1
    // IDEA :  DOUBLE LINKED LIST
    // https://leetcode.com/problems/lru-cache/editorial/
    class ListNode {
        int key;
        int val;
        ListNode next;
        ListNode prev;

        public ListNode(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    class LRUCache_2 {
        int capacity;
        Map<Integer, ListNode> dic;
        ListNode head;
        ListNode tail;

        public LRUCache_2(int capacity) {
            this.capacity = capacity;
            dic = new HashMap<>();
            head = new ListNode(-1, -1);
            tail = new ListNode(-1, -1);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!dic.containsKey(key)) {
                return -1;
            }

            ListNode node = dic.get(key);
            remove(node);
            add(node);
            return node.val;
        }

        public void put(int key, int value) {
            if (dic.containsKey(key)) {
                ListNode oldNode = dic.get(key);
                remove(oldNode);
            }

            ListNode node = new ListNode(key, value);
            dic.put(key, node);
            add(node);

            if (dic.size() > capacity) {
                ListNode nodeToDelete = head.next;
                remove(nodeToDelete);
                dic.remove(nodeToDelete.key);
            }
        }

        public void add(ListNode node) {
            ListNode previousEnd = tail.prev;
            previousEnd.next = node;
            node.prev = previousEnd;
            node.next = tail;
            tail.prev = node;
        }

        public void remove(ListNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }


    // V2
    // IDEA : BUILD IN FUNC
    // https://leetcode.com/problems/lru-cache/editorial/
    class LRUCache_3 {
        int capacity;
        LinkedHashMap<Integer, Integer> dic;

        public LRUCache_3(int capacity) {
            this.capacity = capacity;
            dic = new LinkedHashMap<Integer, Integer>(5, 0.75f, true) {
                //@Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > capacity;
                }
            };
        }

        public int get(int key) {
            return dic.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            dic.put(key, value);
        }

    }

}
