package LeetCodeJava.Design;

// https://leetcode.com/problems/search-suggestions-system/description/

import java.util.*;

/**
 *  1268. Search Suggestions System
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an array of strings products and a string searchWord.
 *
 * Design a system that suggests at most three product names from products after each character of searchWord is typed. Suggested products should have common prefix with searchWord. If there are more than three products with a common prefix return the three lexicographically minimums products.
 *
 * Return a list of lists of the suggested products after each character of searchWord is typed.
 *
 *
 *
 * Example 1:
 *
 * Input: products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
 * Output: [["mobile","moneypot","monitor"],["mobile","moneypot","monitor"],["mouse","mousepad"],["mouse","mousepad"],["mouse","mousepad"]]
 * Explanation: products sorted lexicographically = ["mobile","moneypot","monitor","mouse","mousepad"].
 * After typing m and mo all products match and we show user ["mobile","moneypot","monitor"].
 * After typing mou, mous and mouse the system suggests ["mouse","mousepad"].
 * Example 2:
 *
 * Input: products = ["havana"], searchWord = "havana"
 * Output: [["havana"],["havana"],["havana"],["havana"],["havana"],["havana"]]
 * Explanation: The only word "havana" will be always suggested while typing the search word.
 *
 *
 * Constraints:
 *
 * 1 <= products.length <= 1000
 * 1 <= products[i].length <= 3000
 * 1 <= sum(products[i].length) <= 2 * 104
 * All the strings of products are unique.
 * products[i] consists of lowercase English letters.
 * 1 <= searchWord.length <= 1000
 * searchWord consists of lowercase English letters.
 *
 */
public class SearchSuggestionsSystem {

    // V0
//    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
//
//    }

    // V0-4
    // IDEA: 2 POINTERS
    // https://www.youtube.com/watch?v=D4T2N0yAr20
    public List<List<String>> suggestedProducts_0_4(String[] products, String searchWord) {
        // Final list to store the results for each prefix
        List<List<String>> result = new ArrayList<>();

        /** NOTE !!!
         *
         *  sort words alphabetically
         *
         *  -> so the java default sorting meets the requirement
         */
        // Step 1: Sort the products array alphabetically (O(N log N)).
        // This ensures that products with the same prefix are grouped together,
        // and suggestions are returned in lexicographical order.
        Arrays.sort(products);

        int left = 0; // Left pointer for the current matching range
        int right = products.length - 1; // Right pointer for the current matching range

        /** NOTE !!!
         *
         *   The looping structure:
         *
         *     for(alphabet in searchWord){
         *
         *         while(left boundary Not valid ...){
         *             // update left pointer
         *         }
         *
         *         while(right boundary Not valid ...){
         *             // update right pointer
         *         }
         *
         *     }
         *
         */
        // Step 2: Iterate through each character of the search word to build prefixes.
        for (int i = 0; i < searchWord.length(); i++) {
            // NOTE !!! we get current search alphabet as `char` type
            char c = searchWord.charAt(i);

            /** NOTE !!!
             *
             *  we split `left, right` pointer `narrow` op one by one
             *
             *  -> e.g. we DO the left, right boundary update separately
             */
            // --- Narrow the Left Pointer ---
            // Move 'left' pointer forward as long as:
            // 1. The pointers haven't crossed (left <= right).
            // 2. The current product is too short (doesn't have character at index i).
            // 3. The character at index i in the current product doesn't match the search character 'c'.
            while (left <= right &&
                    (products[left].length() <= i || products[left].charAt(i) != c)) {
                left++;
            }

            /** NOTE !!!
             *
             *  we split `left, right` pointer `narrow` op one by one
             *
             *  -> e.g. we DO the left, right boundary update separately
             */
            // --- Narrow the Right Pointer ---
            // Move 'right' pointer backward using the same logic.
            while (left <= right &&
                    (products[right].length() <= i || products[right].charAt(i) != c)) {
                right--;
            }

            // --- Collect Suggestions ---
            List<String> suggestions = new ArrayList<>();

            // The number of valid suggestions in the current window [left, right]
            int numSuggestions = right - left + 1;

            /** NOTE !!! max to-collect list is  size = 3 */
            // We only want the first 3 suggestions (which are the lexicographically smallest).
            int limit = Math.min(3, numSuggestions);

            // Add up to 3 products from the start of the valid range (index 'left').
            for (int j = 0; j < limit; j++) {
                // Add product at index left + j
                suggestions.add(products[left + j]);
            }

            result.add(suggestions);
        }

        return result;
    }


    // V1-1
    // IDEA: TRIE + DFS
    // https://leetcode.com/problems/search-suggestions-system/editorial/
    class Trie {

        // Node definition of a trie
        class Node {
            boolean isWord = false;
            List<Node> children = Arrays.asList(new Node[26]);
        };

        Node Root, curr;
        List<String> resultBuffer;

        // Runs a DFS on trie starting with given prefix and adds all the words in the resultBuffer, limiting result size to 3
        void dfsWithPrefix(Node curr, String word) {
            if (resultBuffer.size() == 3)
                return;
            if (curr.isWord)
                resultBuffer.add(word);

            // Run DFS on all possible paths.
            for (char c = 'a'; c <= 'z'; c++)
                if (curr.children.get(c - 'a') != null)
                    dfsWithPrefix(curr.children.get(c - 'a'), word + c);
        }

        Trie() {
            Root = new Node();
        }

        // Inserts the string in trie.
        void insert(String s) {

            // Points curr to the root of trie.
            curr = Root;
            for (char c : s.toCharArray()) {
                if (curr.children.get(c - 'a') == null)
                    curr.children.set(c - 'a', new Node());
                curr = curr.children.get(c - 'a');
            }

            // Mark this node as a completed word.
            curr.isWord = true;
        }

        List<String> getWordsStartingWith(String prefix) {
            curr = Root;
            resultBuffer = new ArrayList<String>();
            // Move curr to the end of prefix in its trie representation.
            for (char c : prefix.toCharArray()) {
                if (curr.children.get(c - 'a') == null)
                    return resultBuffer;
                curr = curr.children.get(c - 'a');
            }
            dfsWithPrefix(curr, prefix);
            return resultBuffer;
        }
    };

    List<List<String>> suggestedProducts_1_1(String[] products,
                                         String searchWord) {
        Trie trie = new Trie();
        List<List<String>> result = new ArrayList<>();
        // Add all words to trie.
        for (String w : products)
            trie.insert(w);
        String prefix = new String();
        for (char c : searchWord.toCharArray()) {
            prefix += c;
            result.add(trie.getWordsStartingWith(prefix));
        }
        return result;
    }


    // V1-2
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/search-suggestions-system/editorial/
    // Equivalent code for lower_bound in Java
    int lower_bound(String[] products, int start, String word) {
        int i = start, j = products.length, mid;
        while (i < j) {
            mid = (i + j) / 2;
            if (products[mid].compareTo(word) >= 0)
                j = mid;
            else
                i = mid + 1;
        }
        return i;
    }

    public List<List<String>> suggestedProducts_1_2(String[] products, String searchWord) {
        Arrays.sort(products);
        List<List<String>> result = new ArrayList<>();
        int start = 0, bsStart = 0, n = products.length;
        String prefix = new String();
        for (char c : searchWord.toCharArray()) {
            prefix += c;

            // Get the starting index of word starting with `prefix`.
            start = lower_bound(products, bsStart, prefix);

            // Add empty vector to result.
            result.add(new ArrayList<>());

            // Add the words with the same prefix to the result.
            // Loop runs until `i` reaches the end of input or 3 times or till the
            // prefix is same for `products[i]` Whichever comes first.
            for (int i = start; i < Math.min(start + 3, n); i++) {
                if (products[i].length() < prefix.length() || !products[i].substring(0, prefix.length()).equals(prefix))
                    break;
                result.get(result.size() - 1).add(products[i]);
            }

            // Reduce the size of elements to binary search on since we know
            bsStart = Math.abs(start);
        }
        return result;
    }

    // V2
    // IDEA: PQ
    // https://leetcode.com/problems/search-suggestions-system/solutions/471718/java-priority-queue-no-sort-or-trie-by-g-bxvg/
    public List<List<String>> suggestedProducts_2(String[] products, String searchWord) {
        PriorityQueue<String> pq = new PriorityQueue<>(3, (s1, s2) -> s1.compareTo(s2));
        List<List<String>> list = new ArrayList<>();

        for (int i = 1; i <= searchWord.length(); i++) {
            String temp = searchWord.substring(0, i);
            for (String s : products) {
                if (s.startsWith(temp)) {
                    pq.offer(s);
                }
            }
            List<String> temp_list = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (pq.peek() != null) {
                    temp_list.add(pq.poll());
                }
            }
            pq.clear();
            list.add(temp_list);
        }
        return list;
    }

    // V3
    // IDEA: TRIE
    // https://leetcode.com/problems/search-suggestions-system/solutions/2167835/a-more-intuitive-solution-java-explained-cu5k/
    class TrieNode {
        public TrieNode[] children;
        public PriorityQueue<String> pq;

        public TrieNode() {
            children = new TrieNode[26];
            pq = new PriorityQueue<>((a,b) -> b.compareTo(a));
        }

        public void addToPQ(String word) {
            pq.add(word);
            if (pq.size() > 3) pq.poll();
        }

        public List<String> getTopThree() {
            List<String> topThree = new ArrayList<>();
            while (!pq.isEmpty()) topThree.add(pq.poll());
            Collections.reverse(topThree);
            return topThree;
        }
    }

    public List<List<String>> suggestedProducts_3(String[] products, String searchWord) {
        TrieNode root = new TrieNode();
        for (String product: products) insert(root, product);

        List<List<String>> results = new ArrayList<>();
        for (char c: searchWord.toCharArray()) {
            if ((root = root.children[c - 'a']) == null) break;
            results.add(root.getTopThree());
        }

        while (results.size() < searchWord.length())
            results.add(new ArrayList<>());
        return results;
    }

    private void insert(TrieNode root, String word) {
        for (char c : word.toCharArray()) {
            if (root.children[c - 'a'] == null)
                root.children[c - 'a'] = new TrieNode();
            root = root.children[c - 'a'];
            root.addToPQ(word);
        }
    }


    // V10
    // https://leetcode.ca/2019-05-21-1268-Search-Suggestions-System/
//    class Trie {
//        Trie[] children = new Trie[26];
//        List<Integer> v = new ArrayList<>();
//
//        public void insert(String w, int i) {
//            Trie node = this;
//            for (int j = 0; j < w.length(); ++j) {
//                int idx = w.charAt(j) - 'a';
//                if (node.children[idx] == null) {
//                    node.children[idx] = new Trie();
//                }
//                node = node.children[idx];
//                if (node.v.size() < 3) {
//                    node.v.add(i);
//                }
//            }
//        }
//
//        public List<Integer>[] search(String w) {
//            Trie node = this;
//            int n = w.length();
//            List<Integer>[] ans = new List[n];
//            Arrays.setAll(ans, k -> new ArrayList<>());
//            for (int i = 0; i < n; ++i) {
//                int idx = w.charAt(i) - 'a';
//                if (node.children[idx] == null) {
//                    break;
//                }
//                node = node.children[idx];
//                ans[i] = node.v;
//            }
//            return ans;
//        }
//    }
//
//    public List<List<String>> suggestedProducts_2(String[] products, String searchWord) {
//        Arrays.sort(products);
//        Trie trie = new Trie();
//        for (int i = 0; i < products.length; ++i) {
//            trie.insert(products[i], i);
//        }
//        List<List<String>> ans = new ArrayList<>();
//        for (var v : trie.search(searchWord)) {
//            List<String> t = new ArrayList<>();
//            for (int i : v) {
//                t.add(products[i]);
//            }
//            ans.add(t);
//        }
//        return ans;
//    }




}
