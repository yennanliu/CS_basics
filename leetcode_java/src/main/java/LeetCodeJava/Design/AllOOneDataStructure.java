package LeetCodeJava.Design;

// https://leetcode.com/problems/all-oone-data-structure/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  432. All O`one Data Structure
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Design a data structure to store the strings' count with the ability to return the strings with minimum and maximum counts.
 *
 * Implement the AllOne class:
 *
 * AllOne() Initializes the object of the data structure.
 * inc(String key) Increments the count of the string key by 1. If key does not exist in the data structure, insert it with count 1.
 * dec(String key) Decrements the count of the string key by 1. If the count of key is 0 after the decrement, remove it from the data structure. It is guaranteed that key exists in the data structure before the decrement.
 * getMaxKey() Returns one of the keys with the maximal count. If no element exists, return an empty string "".
 * getMinKey() Returns one of the keys with the minimum count. If no element exists, return an empty string "".
 * Note that each function must run in O(1) average time complexity.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["AllOne", "inc", "inc", "getMaxKey", "getMinKey", "inc", "getMaxKey", "getMinKey"]
 * [[], ["hello"], ["hello"], [], [], ["leet"], [], []]
 * Output
 * [null, null, null, "hello", "hello", null, "hello", "leet"]
 *
 * Explanation
 * AllOne allOne = new AllOne();
 * allOne.inc("hello");
 * allOne.inc("hello");
 * allOne.getMaxKey(); // return "hello"
 * allOne.getMinKey(); // return "hello"
 * allOne.inc("leet");
 * allOne.getMaxKey(); // return "hello"
 * allOne.getMinKey(); // return "leet"
 *
 *
 * Constraints:
 *
 * 1 <= key.length <= 10
 * key consists of lowercase English letters.
 * It is guaranteed that for each call to dec, key is existing in the data structure.
 * At most 5 * 104 calls will be made to inc, dec, getMaxKey, and getMinKey.
 */
public class AllOOneDataStructure {

    // V0
//    class AllOne {
//
//        public AllOne() {
//
//        }
//
//        public void inc(String key) {
//
//        }
//
//        public void dec(String key) {
//
//        }
//
//        public String getMaxKey() {
//
//        }
//
//        public String getMinKey() {
//
//        }
//    }

    /**
     * Your AllOne object will be instantiated and called as such:
     * AllOne obj = new AllOne();
     * obj.inc(key);
     * obj.dec(key);
     * String param_3 = obj.getMaxKey();
     * String param_4 = obj.getMinKey();
     */

    // V1-1
    // IDEA: Doubly Linked List
    // https://leetcode.com/problems/all-oone-data-structure/editorial/
    public class Node {

        int freq;
        Node prev;
        Node next;
        Set<String> keys = new HashSet<>();

        Node(int freq) {
            this.freq = freq;
        }
    }

    class AllOne_1_1 {

        Node head;
        Node tail;
        Map<String, Node> map = new HashMap<>();

        AllOne_1_1() {
            head = new Node(0);
            tail = new Node(0);
            head.next = tail;
            tail.prev = head;
        }

        public void inc(String key) {
            if (map.containsKey(key)) {
                Node node = map.get(key);
                int freq = node.freq;
                node.keys.remove(key);

                Node nextNode = node.next;
                if (nextNode == tail || nextNode.freq != freq + 1) {
                    Node newNode = new Node(freq + 1);
                    newNode.keys.add(key);
                    newNode.prev = node;
                    newNode.next = nextNode;
                    node.next = newNode;
                    nextNode.prev = newNode;
                    map.put(key, newNode);
                } else {
                    nextNode.keys.add(key);
                    map.put(key, nextNode);
                }

                if (node.keys.isEmpty()) {
                    removeNode(node);
                }
            } else {
                Node firstNode = head.next;
                if (firstNode == tail || firstNode.freq > 1) {
                    Node newNode = new Node(1);
                    newNode.keys.add(key);
                    newNode.prev = head;
                    newNode.next = firstNode;
                    head.next = newNode;
                    firstNode.prev = newNode;
                    map.put(key, newNode);
                } else {
                    firstNode.keys.add(key);
                    map.put(key, firstNode);
                }
            }
        }

        public void dec(String key) {
            if (!map.containsKey(key)) {
                return;
            }

            Node node = map.get(key);
            node.keys.remove(key);
            int freq = node.freq;

            if (freq == 1) {
                map.remove(key);
            } else {
                Node prevNode = node.prev;
                if (prevNode == head || prevNode.freq != freq - 1) {
                    Node newNode = new Node(freq - 1);
                    newNode.keys.add(key);
                    newNode.prev = prevNode;
                    newNode.next = node;
                    prevNode.next = newNode;
                    node.prev = newNode;
                    map.put(key, newNode);
                } else {
                    prevNode.keys.add(key);
                    map.put(key, prevNode);
                }
            }

            if (node.keys.isEmpty()) {
                removeNode(node);
            }
        }

        public String getMaxKey() {
            if (tail.prev == head) {
                return "";
            }
            return tail.prev.keys.iterator().next();
        }

        public String getMinKey() {
            if (head.next == tail) {
                return "";
            }
            return head.next.keys.iterator().next();
        }

        private void removeNode(Node node) {
            Node prevNode = node.prev;
            Node nextNode = node.next;

            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }


    // V2


}
