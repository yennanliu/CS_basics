package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/lru-cache/description/

import java.util.*;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

public class LRUCache_ {

    // V0
    // TODO : implement

    // V0'
    // IDEA : LinkedHashMap (OrderedDict) (gpt)
    class LRUCache {
        private int capacity;
        private LinkedHashMap<Integer, Integer> lruCache;

        public LRUCache(int capacity) {

            this.capacity = capacity;

            /**
             *  0.75f is the load factor.
             *
             *  By using 0.75f, you ensure a balance between space efficiency and performance in terms of access speed, which is particularly important for operations that are common in an LRU cache like this one.
             *
             *  The 0.75f load factor is a compromise that provides a good balance between memory usage and performance.
             *
             *  It ensures that the hash table will resize when it is 75% full, which helps maintain efficient access times without wasting too much memory.
             */
            this.lruCache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > LRUCache.this.capacity;
                }
            };
        }

        public int get(int key) {
            if (!lruCache.containsKey(key)) {
                return -1;
            } else {
                return lruCache.get(key);
            }
        }

        public void put(int key, int value) {
            lruCache.put(key, value);
        }
    }

    // V0''
    // IDEA : DOUBLE LINKED LIST (gpt)

    class LRUCache0_ {
        private class Node {
            int key;
            int value;
            Node next;
            Node prev;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private int capacity;
        private HashMap<Integer, Node> cache;
        private Node head;
        private Node tail;

        public LRUCache0_(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>(capacity);
            this.head = new Node(-1, -1);
            this.tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!cache.containsKey(key)) {
                return -1;
            }
            Node node = cache.get(key);
            removeNode(node);
            addToHead(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.value = value;
                removeNode(node);
                addToHead(node);
            } else {
                if (cache.size() >= capacity) {
                    cache.remove(tail.prev.key);
                    removeNode(tail.prev);
                }
                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                addToHead(newNode);
            }
        }

        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void addToHead(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }
    }

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
