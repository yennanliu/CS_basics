package LeetCodeJava.Design;

// https://leetcode.com/problems/prefix-and-suffix-search/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  745. Prefix and Suffix Search
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Design a special dictionary that searches the words in it by a prefix and a suffix.
 *
 * Implement the WordFilter class:
 *
 * WordFilter(string[] words) Initializes the object with the words in the dictionary.
 * f(string pref, string suff) Returns the index of the word in the dictionary, which has the prefix pref and the suffix suff. If there is more than one valid index, return the largest of them. If there is no such word in the dictionary, return -1.
 *
 *
 * Example 1:
 *
 * Input
 * ["WordFilter", "f"]
 * [[["apple"]], ["a", "e"]]
 * Output
 * [null, 0]
 * Explanation
 * WordFilter wordFilter = new WordFilter(["apple"]);
 * wordFilter.f("a", "e"); // return 0, because the word at index 0 has prefix = "a" and suffix = "e".
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 104
 * 1 <= words[i].length <= 7
 * 1 <= pref.length, suff.length <= 7
 * words[i], pref and suff consist of lowercase English letters only.
 * At most 104 calls will be made to the function f.
 *
 */
public class PrefixAndSuffixSearch {

    // V0
//    class WordFilter {
//
//        public WordFilter(String[] words) {
//
//        }
//
//        public int f(String pref, String suff) {
//
//        }
//    }

    // V0-0-1
    // IDEA: CUSTOM NODE + TRIE (fixed by gemini)
    // NOTE !!! below may NOT be a most elegant, efficient approach
    //          ; but just as a note for future reference
    //          . Also shows how we try to use `same basic pattern/foundation`
    //          on different LCs
    class MyNode101 {
        // Stores the largest weight (index) of the word passing through this node.
        int maxWeight;
        // Child map changed to use Character key for better consistency.
        Map<Character, MyNode101> child;

        /**
         * time = O(1)
         * space = O(1)
         */
        MyNode101() {
            // We use 0 as the default, non-functional weight.
            this.maxWeight = 0;
            this.child = new HashMap<>();
        }
    }

    class MyTrie101 {

        // attr
        MyNode101 root; // Renamed to root for clarity

        // constructor
        /**
         * time = O(1)
         * space = O(1)
         */
        MyTrie101() {
            this.root = new MyNode101();
        }

        /**
         * Corrected add method to update the maxWeight along the path.
         * Time: O(L)
         */
        public void add(String key, int weight) { // Added 'weight' parameter
            MyNode101 node = this.root;

            for (char ch : key.toCharArray()) {
                if (!node.child.containsKey(ch)) {
                    node.child.put(ch, new MyNode101());
                }
                node = node.child.get(ch);

                // Crucially, update the maxWeight at the current node
                node.maxWeight = Math.max(node.maxWeight, weight);
            }
            // node.isEnd is not strictly needed since maxWeight > 0 implies a word passes through.
        }

        /**
         * New search method optimized for the combined key.
         * Time: O(L)
         */
        public int searchMaxWeight(String key) {
            MyNode101 node = this.root;

            for (char ch : key.toCharArray()) {
                if (!node.child.containsKey(ch)) {
                    // Path does not exist
                    return -1;
                }
                node = node.child.get(ch);
            }

            // Return the max weight stored at the end of the path
            return node.maxWeight;
        }

        // The methods isStartWith, contain, and wordWithPrefix are no longer needed
        // for the optimized solution.
    }

    class WordFilter_0_0_1 {

        MyTrie101 myTrie;

        /**
         * Preprocessing: Inserts all combinations of (suffix + "#" + word) for every word.
         * Time Complexity: O(N * L^2)
         */
        public WordFilter_0_0_1(String[] words) {
            this.myTrie = new MyTrie101();

            for (int weight = 0; weight < words.length; weight++) {
                String word = words[weight];
                int length = word.length();

                // Iterate through all possible suffixes (i=0 gives the full word as suffix)
                for (int i = 0; i <= length; i++) {
                    String suffix = word.substring(i);

                    // Form the combined key: suffix + "#" + word
                    String combinedKey = suffix + "#" + word;

                    // Use the corrected add method
                    this.myTrie.add(combinedKey, weight);
                }
            }
        }

        /**
         * Query method: Searches for the single combined key (suff + "#" + pref).
         * Time Complexity: O(L)
         */
        public int f(String pref, String suff) {
            // The query string is formed by putting the requested suffix, delimiter, and prefix.
            String query = suff + "#" + pref;

            // The searchMaxWeight method handles the traversal and returns the highest index found.
            return this.myTrie.searchMaxWeight(query);
        }
    }


    // V0-1
    // IDEA: (fixed by gemini)
    /**
     *  IDEA:
     *
     *  The code you provided is a complete and optimized solution for **LC 745: Prefix and Suffix Search**. It uses a specialized **Trie (Prefix Tree)** data structure to achieve $O(L)$ search time, where $L$ is the maximum length of a word.
     *
     * This approach overcomes the standard $O(L \cdot N)$ complexity of checking every word in a list by cleverly combining the prefix and suffix requirements into a single searchable key.
     *
     * Here is a detailed explanation of the logic and the two main components:
     *
     * ---
     *
     * ## 1. The Trie Structure (`TrieNode`)
     *
     * The `TrieNode` is the fundamental building block of the tree. Unlike a standard Trie that might store a simple `boolean isEnd`, this one stores an integer:
     *
     * * **`maxWeight` (int):** This is the crucial element. It stores the **highest index (weight)** of any word in the original `words` array that passes through that specific node. Since the problem asks for the word with the *largest* index, storing the maximum weight at every node allows the final search to instantly retrieve the correct answer without further checking.
     * * **`children` (Map<Character, TrieNode>):** A map to store pointers to the next nodes, keyed by the character of the next segment of the string.
     *
     * ---
     *
     * ## 2. The `WordFilter` Constructor (Preprocessing)
     *
     * The constructor performs the time-consuming but essential preprocessing step. For every original word $W$ at index $i$ (its weight), it generates and inserts *multiple* augmented strings into the Trie.
     *
     * The core idea is to combine the **suffix** and the **prefix** into a single string that can be searched sequentially.
     *
     * ### The Combined Key: `suffix + "#" + word`
     *
     * For every word, the code iterates through all possible suffixes and creates a key in the format: `[suffix] # [full word]`.
     *
     * **Example:** For the word **`"apple"`** (Weight: 3), the constructor inserts the following keys:
     *
     * | Loop Index ($i$) | Suffix | Combined Key | Action (Weight: 3) |
     * | :---: | :--- | :--- | :--- |
     * | 0 | `"apple"` | `"apple#apple"` | Insert into Trie |
     * | 1 | `"pple"` | `"pple#apple"` | Insert into Trie |
     * | 2 | `"ple"` | `"ple#apple"` | Insert into Trie |
     * | 3 | `"le"` | `"le#apple"` | Insert into Trie |
     * | 4 | `"e"` | `"e#apple"` | Insert into Trie |
     * | 5 | `""` | `"#apple"` | Insert into Trie |
     *
     * ### The `insert` Method
     *
     * When inserting a key (e.g., `"le#apple"`) for a word with a certain `weight`, the `insert` method does two things for every character along the path:
     *
     * 1.  It creates a new node if one doesn't exist.
     * 2.  It updates the `maxWeight` of that node: `node.maxWeight = Math.max(node.maxWeight, weight);`. This ensures that any subsequent search passing through that node will know the highest possible index found so far.
     *
     * ---
     *
     * ## 3. The `f(pref, suff)` Query Method
     *
     * The query becomes a simple and fast search operation in the pre-built Trie.
     *
     * 1.  **Form the Query String:** The method constructs the search key by putting the requested suffix first, followed by the delimiter (`#`), and then the requested prefix: **`suff + "#" + pref`**.
     *
     *     * *Example:* If `pref="ap"` and `suff="le"`, the query string is **`"le#ap"`**.
     *
     * 2.  **Trie Traversal:** The method traverses the Trie using the query string. It effectively checks:
     *     * Does a path exist starting with the suffix (`"le"`)...
     *     * ...followed by the delimiter (`#`)...
     *     * ...followed by the prefix (`"ap"`)...
     *     * ...which means it is searching for the string `"le#ap"`.
     *
     * 3.  **Result Retrieval:**
     *     * If the traversal reaches the end of the query string, the `maxWeight` stored at the final node is the answer. Since the Trie stored the highest index at every node, this value is guaranteed to be the largest weight matching the criteria.
     *     * If the traversal fails (a character is missing), it means no word matches both criteria, and it returns **`-1`**.
     *
     * By using the combined key and storing the max weight, the search is reduced to a single linear traversal of the query string's length ($O(L)$), fulfilling the performance requirements of the problem.
     *
     */
    // --- Trie Node Class ---
    class TrieNode_0_1 {
        // Store the largest weight (index) of the word that passes through this node.
        int maxWeight;
        Map<Character, TrieNode_0_1> children;

        /**
         * time = O(1)
         * space = O(1)
         */
        TrieNode_0_1() {
            this.maxWeight = 0;
            this.children = new HashMap<>();
        }
    }

    // --- WordFilter Class ---
    class WordFilter_0_1 {

        private final TrieNode_0_1 root;

        /**
         * Constructor: Preprocessing step to build the Trie.
         * Inserts all combinations of (suffix + "#" + word) for every word.
         * Time Complexity: O(N * L^2), where N is number of words, L is max word length.
         */
        public WordFilter_0_1(String[] words) {
            this.root = new TrieNode_0_1();

            for (int weight = 0; weight < words.length; weight++) {
                String word = words[weight];
                int length = word.length();

                // Loop through all possible suffixes of the current word
                // The combined key is: suffix + "#" + word
                for (int i = 0; i <= length; i++) {
                    String combinedKey = word.substring(i) + "#" + word;
                    insert(combinedKey, weight);
                }
            }
        }

        // Helper method to insert a string into the Trie and update maxWeight along the path.
        private void insert(String key, int weight) {
            TrieNode_0_1 node = root;

            for (char ch : key.toCharArray()) {
                node.children.putIfAbsent(ch, new TrieNode_0_1());
                node = node.children.get(ch);

                // Update the maxWeight at the current node
                node.maxWeight = Math.max(node.maxWeight, weight);
            }
        }

        /**
         * Query method: Finds the word with the largest weight that matches pref and suff.
         * Time Complexity: O(L), where L is the length of the query string.
         */
        public int f(String pref, String suff) {
            // Search for the combined key: suff + "#" + pref
            String query = suff + "#" + pref;
            TrieNode_0_1 node = root;

            for (char ch : query.toCharArray()) {
                if (!node.children.containsKey(ch)) {
                    // If any character in the query path is missing, no matching word exists.
                    return -1;
                }
                node = node.children.get(ch);
            }

            // The maxWeight at the final node represents the largest index (weight)
            // of a word that contains the required suffix and prefix.
            return node.maxWeight;
        }
    }

    /**
     * Your MyNode101, MyTrie101 classes are not needed in this fixed solution,
     * as the logic is integrated directly into the final WordFilter class.
     */


    // V0-2
    // IDEA: (gpt)
    /**
     *    IDEA:
     *
     *    Absolutely — here is a full visual breakdown of how the Trie works for LC 745.
     *
     * We use one Trie, storing every rotation of suffix + ‘{’ + word, so that:
     *
     * Query = suffix + '{' + prefix
     *
     * If this path exists → it maps directly to the highest-indexed matching word.
     *
     * ⸻
     *
     * Word = "apple"
     *
     * We insert:
     *
     * i	Inserted into Trie
     * 0	"apple{apple"
     * 1	"pple{apple"
     * 2	"ple{apple"
     * 3	"le{apple"
     * 4	"e{apple"
     * 5	"{apple"
     *
     * So searches for:
     *
     * ("ap", "le")  → "le{ap"
     *
     * will follow nodes:
     *
     *  l → e → { → a → p
     *
     * and land on a node with weight = index of "apple"
     *
     * ⸻
     *
     * 📍 VISUAL TRIE (Partial)
     *
     * (root)
     *  ├── '{'
     *  │    └── a
     *  │         └── p
     *  │              └── p
     *  │                   └── l
     *  │                        └── e      (weight = idx)
     *  │
     *  ├── a
     *  │   └── p
     *  │       └── p
     *  │           └── l
     *  │               └── e
     *  │                    └── {
     *  │                        └── a
     *  │                            └── p
     *  │                                └── ...
     *  │
     *  ├── p
     *  │   └── p
     *  │       └── l
     *  │           └── e
     *  │               └── {
     *  │                   └── a
     *  │                       └── p
     *  │                           └── ...
     *  │
     *  ├── l
     *  │   └── e
     *  │       └── {
     *  │           └── a
     *  │               └── p
     *  │                   └── ...
     *  │
     *  └── e
     *      └── {
     *          └── a
     *              └── p
     *                  └── ...
     *
     * Every branch eventually stores the full rotated pattern, all pointing to the same weight index.
     *
     * ⸻
     *
     * 🔍 Example Query Walkthrough
     *
     * Query:
     *
     * prefix = "ap"
     * suffix = "le"
     * → query = "le{ap"
     *
     * Walk the Trie:
     *
     * root
     *  ↓
     * 'l'
     *  ↓
     * 'e'
     *  ↓
     * '{'
     *  ↓
     * 'a'
     *  ↓
     * 'p'
     *  ✔ FOUND — return node.weight (largest index)
     *
     * No searching lists
     * No filtering words
     * Just a direct pointer lookup 💡
     *
     *
     */
    class WordFilter_0_2 {

        class TrieNode {
            TrieNode[] child = new TrieNode[27]; // 26 chars + '{'
            int weight = -1;
        }

        TrieNode root = new TrieNode();

        public WordFilter_0_2(String[] words) {
            for (int weight = 0; weight < words.length; weight++) {
                String w = words[weight] + "{"; // add separator

                // For suffix-prefix combinations
                for (int i = 0; i < w.length(); i++) {
                    insert(w.substring(i) + words[weight], weight);
                }
            }
        }

        /**
         *
         *  NOTE !!!
         *
         *   we encode word like below:
         *
         *   apple → "apple{"
         *
         *   Insert patterns:
         *
         *     {apple + "apple"
         *     "pple{apple"
         *     "ple{apple"
         *     "le{apple"
         *     "e{apple"
         *
         */
        private void insert(String word, int weight) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int idx = (c == '{') ? 26 : c - 'a';
                if (node.child[idx] == null)
                    node.child[idx] = new TrieNode();
                node = node.child[idx];
                node.weight = weight;
            }
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int f(String pref, String suff) {
            String query = suff + "{" + pref;
            TrieNode node = root;

            for (char c : query.toCharArray()) {
                int idx = (c == '{') ? 26 : c - 'a';
                if (node.child[idx] == null)
                    return -1;
                node = node.child[idx];
            }

            return node.weight;
        }
    }


    // V1-1
    // IDEA: Trie + Set Intersection [Time Limit Exceeded] (TLE)
    // https://leetcode.com/problems/prefix-and-suffix-search/editorial/
    class WordFilter_1_1 {
        TrieNode trie1, trie2;

        public WordFilter_1_1(String[] words) {
            trie1 = new TrieNode();
            trie2 = new TrieNode();
            int wt = 0;
            for (String word : words) {
                char[] ca = word.toCharArray();

                TrieNode cur = trie1;
                cur.weight.add(wt);
                for (char letter : ca) {
                    if (cur.children[letter - 'a'] == null) {
                        cur.children[letter - 'a'] = new TrieNode();
                    }
                    cur = cur.children[letter - 'a'];
                    cur.weight.add(wt);
                }

                cur = trie2;
                cur.weight.add(wt);
                for (int j = ca.length - 1; j >= 0; --j) {
                    char letter = ca[j];
                    if (cur.children[letter - 'a'] == null) {
                        cur.children[letter - 'a'] = new TrieNode();
                    }
                    cur = cur.children[letter - 'a'];
                    cur.weight.add(wt);
                }
                wt++;
            }
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int f(String prefix, String suffix) {
            TrieNode cur1 = trie1, cur2 = trie2;
            for (char letter : prefix.toCharArray()) {
                if (cur1.children[letter - 'a'] == null) {
                    return -1;
                }
                cur1 = cur1.children[letter - 'a'];
            }
            char[] ca = suffix.toCharArray();
            for (int j = ca.length - 1; j >= 0; --j) {
                char letter = ca[j];
                if (cur2.children[letter - 'a'] == null) {
                    return -1;
                }
                cur2 = cur2.children[letter - 'a'];
            }

            int ans = -1;
            for (int w1 : cur1.weight) {
                if (w1 > ans && cur2.weight.contains(w1)) {
                    ans = w1;
                }
            }

            return ans;
        }
    }

    class TrieNode {
        TrieNode[] children;
        Set<Integer> weight;

        public TrieNode() {
            children = new TrieNode[26];
            weight = new HashSet();
        }
    }



    // V1-2
    // IDEA: Paired Trie [Accepted]
    // https://leetcode.com/problems/prefix-and-suffix-search/editorial/
    class WordFilter_1_2 {
        TrieNode_1_2 trie;

        public WordFilter_1_2(String[] words) {
            trie = new TrieNode_1_2();
            int wt = 0;
            for (String word : words) {
                TrieNode_1_2 cur = trie;
                cur.weight = wt;
                int L = word.length();
                char[] chars = word.toCharArray();
                for (int i = 0; i < L; ++i) {

                    TrieNode_1_2 tmp = cur;
                    for (int j = i; j < L; ++j) {
                        int code = (chars[j] - '`') * 27;
                        if (tmp.children.get(code) == null) {
                            tmp.children.put(code, new TrieNode_1_2());
                        }
                        tmp = tmp.children.get(code);
                        tmp.weight = wt;
                    }

                    tmp = cur;
                    for (int k = L - 1 - i; k >= 0; --k) {
                        int code = (chars[k] - '`');
                        if (tmp.children.get(code) == null) {
                            tmp.children.put(code, new TrieNode_1_2());
                        }
                        tmp = tmp.children.get(code);
                        tmp.weight = wt;
                    }

                    int code = (chars[i] - '`') * 27 + (chars[L - 1 - i] - '`');
                    if (cur.children.get(code) == null) {
                        cur.children.put(code, new TrieNode_1_2());
                    }
                    cur = cur.children.get(code);
                    cur.weight = wt;

                }
                wt++;
            }
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int f(String prefix, String suffix) {
            TrieNode_1_2 cur = trie;
            int i = 0, j = suffix.length() - 1;
            while (i < prefix.length() || j >= 0) {
                char c1 = i < prefix.length() ? prefix.charAt(i) : '`';
                char c2 = j >= 0 ? suffix.charAt(j) : '`';
                int code = (c1 - '`') * 27 + (c2 - '`');
                cur = cur.children.get(code);
                if (cur == null) {
                    return -1;
                }
                i++;
                j--;
            }
            return cur.weight;
        }
    }

    class TrieNode_1_2 {
        Map<Integer, TrieNode_1_2> children;
        int weight;

        public TrieNode_1_2() {
            children = new HashMap();
            weight = 0;
        }
    }


    // V1-3
    // IDEA: Trie of Suffix Wrapped Words
    // https://leetcode.com/problems/prefix-and-suffix-search/editorial/
    class WordFilter_1_3 {
        TrieNode_1_3 trie;

        public WordFilter_1_3(String[] words) {
            trie = new TrieNode_1_3();
            for (int weight = 0; weight < words.length; ++weight) {
                String word = words[weight] + "{";
                for (int i = 0; i < word.length(); ++i) {
                    TrieNode_1_3 cur = trie;
                    cur.weight = weight;
                    for (int j = i; j < 2 * word.length() - 1; ++j) {
                        int k = word.charAt(j % word.length()) - 'a';
                        if (cur.children[k] == null) {
                            cur.children[k] = new TrieNode_1_3();
                        }
                        cur = cur.children[k];
                        cur.weight = weight;
                    }
                }
            }
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int f(String prefix, String suffix) {
            TrieNode_1_3 cur = trie;
            for (char letter : (suffix + '{' + prefix).toCharArray()) {
                if (cur.children[letter - 'a'] == null) {
                    return -1;
                }
                cur = cur.children[letter - 'a'];
            }
            return cur.weight;
        }
    }

    class TrieNode_1_3 {
        TrieNode_1_3[] children;
        int weight;

        public TrieNode_1_3() {
            children = new TrieNode_1_3[27];
            weight = 0;
        }
    }


    // V2


    // V3



}
