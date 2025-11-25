package LeetCodeJava.Tree;

// https://leetcode.com/problems/map-sum-pairs/description/

import java.util.HashMap;
import java.util.Map;

/**
 *  677. Map Sum Pairs
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Design a map that allows you to do the following:
 *
 * Maps a string key to a given value.
 * Returns the sum of the values that have a key with a prefix equal to a given string.
 * Implement the MapSum class:
 *
 * MapSum() Initializes the MapSum object.
 * void insert(String key, int val) Inserts the key-val pair into the map. If the key already existed, the original key-value pair will be overridden to the new one.
 * int sum(string prefix) Returns the sum of all the pairs' value whose key starts with the prefix.
 *
 *
 * Example 1:
 *
 * Input
 * ["MapSum", "insert", "sum", "insert", "sum"]
 * [[], ["apple", 3], ["ap"], ["app", 2], ["ap"]]
 * Output
 * [null, null, 3, null, 5]
 *
 * Explanation
 * MapSum mapSum = new MapSum();
 * mapSum.insert("apple", 3);
 * mapSum.sum("ap");           // return 3 (apple = 3)
 * mapSum.insert("app", 2);
 * mapSum.sum("ap");           // return 5 (apple + app = 3 + 2 = 5)
 *
 *
 * Constraints:
 *
 * 1 <= key.length, prefix.length <= 50
 * key and prefix consist of only lowercase English letters.
 * 1 <= val <= 1000
 * At most 50 calls will be made to insert and sum.
 *
 */
public class MapSumPairs {

    // V0
//    class MapSum {
//
//        public MapSum() {
//
//        }
//
//        public void insert(String key, int val) {
//
//        }
//
//        public int sum(String prefix) {
//
//        }
//    }

    /**
     * Your MapSum object will be instantiated and called as such:
     * MapSum obj = new MapSum();
     * obj.insert(key,val);
     * int param_2 = obj.sum(prefix);
     */

    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/map-sum-pairs/editorial/
    class MapSum_1_1 {
        HashMap<String, Integer> map;

        public MapSum_1_1() {
            map = new HashMap<>();
        }

        public void insert(String key, int val) {
            map.put(key, val);
        }

        public int sum(String prefix) {
            int ans = 0;
            for (String key : map.keySet()) {
                if (key.startsWith(prefix)) {
                    ans += map.get(key);
                }
            }
            return ans;
        }
    }


    // V1-2
    // IDEA: PREFIX HASHMAP
    // https://leetcode.com/problems/map-sum-pairs/editorial/
    class MapSum_1_2 {
        Map<String, Integer> map;
        Map<String, Integer> score;

        public MapSum_1_2() {
            map = new HashMap();
            score = new HashMap();
        }

        public void insert(String key, int val) {
            int delta = val - map.getOrDefault(key, 0);
            map.put(key, val);
            String prefix = "";
            for (char c : key.toCharArray()) {
                prefix += c;
                score.put(prefix, score.getOrDefault(prefix, 0) + delta);
            }
        }

        public int sum(String prefix) {
            return score.getOrDefault(prefix, 0);
        }
    }


    // V1-3
    // IDEA: TRIE
    // https://leetcode.com/problems/map-sum-pairs/editorial/
    class MapSum_1_3 {
        HashMap<String, Integer> map;
        TrieNode root;

        public MapSum_1_3() {
            map = new HashMap();
            root = new TrieNode();
        }

        public void insert(String key, int val) {
            int delta = val - map.getOrDefault(key, 0);
            map.put(key, val);
            TrieNode cur = root;
            cur.score += delta;
            for (char c : key.toCharArray()) {
                cur.children.putIfAbsent(c, new TrieNode());
                cur = cur.children.get(c);
                cur.score += delta;
            }
        }

        public int sum(String prefix) {
            TrieNode cur = root;
            for (char c : prefix.toCharArray()) {
                cur = cur.children.get(c);
                if (cur == null)
                    return 0;
            }
            return cur.score;
        }
    }

    class TrieNode {
        Map<Character, TrieNode> children = new HashMap();
        int score;
    }



    // V2
    // IDEA: TRIE
    // https://leetcode.ca/2017-10-07-677-Map-Sum-Pairs/
    class Trie {
        private Trie[] children = new Trie[26];
        private int val;

        public void insert(String w, int x) {
            Trie node = this;
            for (int i = 0; i < w.length(); ++i) {
                int idx = w.charAt(i) - 'a';
                if (node.children[idx] == null) {
                    node.children[idx] = new Trie();
                }
                node = node.children[idx];
                node.val += x;
            }
        }

        public int search(String w) {
            Trie node = this;
            for (int i = 0; i < w.length(); ++i) {
                int idx = w.charAt(i) - 'a';
                if (node.children[idx] == null) {
                    return 0;
                }
                node = node.children[idx];
            }
            return node.val;
        }
    }

    class MapSum_2 {
        private Map<String, Integer> d = new HashMap<>();
        private Trie trie = new Trie();

        public MapSum_2() {
        }

        public void insert(String key, int val) {
            int x = val - d.getOrDefault(key, 0);
            d.put(key, val);
            trie.insert(key, x);
        }

        public int sum(String prefix) {
            return trie.search(prefix);
        }
    }


}
