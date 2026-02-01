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

    // V0-1
    // IDEA: BRUTE FORCE
    class MapSum_0_1 {

        // { string : val }
        Map<String, Integer> map;

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_0_1() {
            this.map = new HashMap<>();
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            this.map.put(key, val);
            System.out.println(">>> this.map " + this.map);
        }

        /** BRUTE FORCE */
        /**
         * time = O(N)
         * space = O(H)
         */
        public int sum(String prefix) {
            int res = 0;
            /**  NOTE !!!
             *
             *  we loop over key of hashmap
             *  and loop get the sub string of cur key
             *
             *  -> so we'll have double loop (brute force),
             *
             *  within the double loop. we check if the cur sub string
             *  equal to the prefix, if so, we add the cur map val to res,
             *  and break cur loop (2nd loop),
             *
             *  since the cur key already
             *  `count` as the prefixSum res, we should STOP and check the next
             *  brand new hashMap key
             */
            for (String k : this.map.keySet()) {
                for (int i = 0; i < k.length(); i++) {
                    /**  NOTE !!!
                     *
                     *  we can do below via string.startsWith() method
                     *  (V0-3)
                     *
                     *
                     *  e.g.:
                     *
                     *       if (key.startsWith(prefix)) {
                     *                     res += this.map.get(key);
                     *       }
                     */
                    String sub = String.copyValueOf(k.toCharArray(), 0, i + 1);
                    // System.out.println(
                    //         " k = " + k +
                    //                 " i = " + i +
                    //                 " sub = " + sub +
                    //                 ", res = " + res);

                    if (sub.equals(prefix)) {
                        res += this.map.get(k);
                        // already add to res per cur key,
                        // break, move to next key
                        break;
                    }
                }
            }

            return res;
        }

    }

    // V0-2
    // IDEA: TRIE (gemini)
    class MapSum_0_2 {

        // --- Trie Node Class ---
        private class TrieNode {
            Map<Character, TrieNode> children;
            // Stores the sum of values of all keys passing through this node.
            // This is necessary for the brute-force/Trie-search approach.
            // For the optimized approach, this can store the final value of the key,
            // or for the *summation* optimization, it stores the delta value.
            int val;

            TrieNode() {
                this.children = new HashMap<>();
                this.val = 0;
            }
        }

        private final TrieNode root;
        // Map to store the original key-value pairs to calculate the delta upon update/re-insertion.
        private final Map<String, Integer> map;

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_0_2() {
            this.root = new TrieNode();
            this.map = new HashMap<>();
        }

        /**
         * Inserts the key-value pair, updating the Trie with the delta value.
         * Time: O(L), where L is the length of the key.
         */
        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            // Calculate the difference between the new value and the old value (0 if new key).
            // This is the delta we must apply to all nodes along the path.
            int delta = val - this.map.getOrDefault(key, 0);

            // Update the key-value map
            this.map.put(key, val);

            TrieNode current = root;

            // Traverse the Trie and update the cumulative sum (val field) using the delta
            for (char ch : key.toCharArray()) {
                current.children.putIfAbsent(ch, new TrieNode());
                current = current.children.get(ch);
                current.val += delta; // Update the sum for all nodes in the path
            }
        }

        /**
         * Returns the sum of values of all keys starting with the given prefix.
         * Time: O(L), where L is the length of the prefix.
         */
        /**
         * time = O(N)
         * space = O(H)
         */
        public int sum(String prefix) {
            TrieNode current = root;

            // Traverse the Trie to find the node corresponding to the end of the prefix
            for (char ch : prefix.toCharArray()) {
                if (!current.children.containsKey(ch)) {
                    return 0; // Prefix not found
                }
                current = current.children.get(ch);
            }

            // The value stored at the prefix node is the pre-calculated sum of all keys
            // that extend from this point (due to the insert logic).
            return current.val;
        }
    }


    // V0-3
    // IDEA: BRUTE FORCE (gpt)
    class MapSum_0_3 {

        // { key : value }
        Map<String, Integer> map;

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_0_3() {
            this.map = new HashMap<>();
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            this.map.put(key, val);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int sum(String prefix) {
            int res = 0;

            for (String key : this.map.keySet()) {
                /** NOTE !!! `startsWith` trick */
                if (key.startsWith(prefix)) {
                    res += this.map.get(key);
                }
            }

            return res;
        }
    }

    // V0-4
    // IDEA: PREFIX MAP (gpt)
    class MapSum_0_4 {

        // real key → value
        Map<String, Integer> values;

        // prefix → sum of all keys having that prefix
        Map<String, Integer> prefixSum;

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_0_4() {
            this.values = new HashMap<>();
            this.prefixSum = new HashMap<>();
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            int oldVal = values.getOrDefault(key, 0);
            /** NOTE !!!
             *
             *  below trick
             */
            int diff = val - oldVal; // important: adjust previous value

            // store new value
            values.put(key, val);

            // update all prefixes
            String prefix = "";
            for (char ch : key.toCharArray()) {
                /** NOTE !!!
                 *
                 *  below trick
                 */
                prefix += ch;
                prefixSum.put(prefix, prefixSum.getOrDefault(prefix, 0) + diff);
            }
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int sum(String prefix) {
            return prefixSum.getOrDefault(prefix, 0);
        }
    }


    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/map-sum-pairs/editorial/
    class MapSum_1_1 {
        HashMap<String, Integer> map;

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_1_1() {
            map = new HashMap<>();
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            map.put(key, val);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_1_2() {
            map = new HashMap();
            score = new HashMap();
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            int delta = val - map.getOrDefault(key, 0);
            map.put(key, val);
            String prefix = "";
            for (char c : key.toCharArray()) {
                prefix += c;
                score.put(prefix, score.getOrDefault(prefix, 0) + delta);
            }
        }

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_1_3() {
            map = new HashMap();
            root = new TrieNode();
        }

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
        public MapSum_2() {
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void insert(String key, int val) {
            int x = val - d.getOrDefault(key, 0);
            d.put(key, val);
            trie.insert(key, x);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int sum(String prefix) {
            return trie.search(prefix);
        }
    }


}
