package LeetCodeJava.Sort;

// https://leetcode.com/problems/top-k-frequent-words/description/

import java.util.*;
import java.util.stream.Collectors;

/**
 * 692. Top K Frequent Words
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of strings words and an integer k, return the k most frequent strings.
 *
 * Return the answer sorted by the frequency from highest to lowest. Sort the words with the same frequency by their lexicographical order.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["i","love","leetcode","i","love","coding"], k = 2
 * Output: ["i","love"]
 * Explanation: "i" and "love" are the two most frequent words.
 * Note that "i" comes before "love" due to a lower alphabetical order.
 * Example 2:
 *
 * Input: words = ["the","day","is","sunny","the","the","the","sunny","is","is"], k = 4
 * Output: ["the","is","sunny","day"]
 * Explanation: "the", "is", "sunny" and "day" are the four most frequent words, with the number of occurrence being 4, 3, 2 and 1 respectively.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 500
 * 1 <= words[i].length <= 10
 * words[i] consists of lowercase English letters.
 * k is in the range [1, The number of unique words[i]]
 *
 *
 * Follow-up: Could you solve it in O(n log(k)) time and O(n) extra space?
 *
 */
public class TopKFrequentWords {

    // V0
//    public List<String> topKFrequent(String[] words, int k) {
//    }

    // V0-1
    // IDEA: Sort on map key set
    public List<String> topKFrequent_0_1(String[] words, int k) {

        // IDEA: map sorting
        HashMap<String, Integer> freq = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            freq.put(words[i], freq.getOrDefault(words[i], 0) + 1);
        }
        List<String> res = new ArrayList(freq.keySet());

        /**
         * NOTE !!!
         *
         *  we directly sort over map's keySet
         *  (with the data val, key that read from map)
         *
         *
         *  example:
         *
         *          Collections.sort(res,
         *                 (w1, w2) -> freq.get(w1).equals(freq.get(w2)) ? w1.compareTo(w2) : freq.get(w2) - freq.get(w1));
         */
        Collections.sort(res, (x, y) -> {
            int valDiff = freq.get(y) - freq.get(x); // sort on `value` bigger number first (decreasing order)
            if (valDiff == 0){
                /**
                 *  NOTE !!!
                 *
                 *    sort `lexicographically`
                 *
                 *    -> a.compareTo(b);
                 *
                 */
                // Sort on `key ` with `lexicographically` order (increasing order)
                //return y.length() - x.length(); // ?
                return x.compareTo(y);
            }
            return valDiff;
        });

        // get top K result
        return res.subList(0, k);
    }

    // V0-2
    // IDEA: MAP + MIN PQ (fixed by gpt)
    public List<String> topKFrequent_0_2(String[] words, int k) {
        List<String> res = new ArrayList<>();
        if (words == null || words.length == 0 || k <= 0) {
            return res;
        }

        // Count frequency
        Map<String, Integer> freqMap = new HashMap<>();
        for (String w : words) {
            freqMap.put(w, freqMap.getOrDefault(w, 0) + 1);
        }

        // Min-heap: sort by frequency ascending, then lexicographically descending
        PriorityQueue<String> pq = new PriorityQueue<>((w1, w2) -> {
            int diff = freqMap.get(w1) - freqMap.get(w2);
            if (diff == 0) {
                return w2.compareTo(w1); // reverse lexicographical
            }
            return diff;
        });

        for (String word : freqMap.keySet()) {
            pq.add(word);
            if (pq.size() > k) {
                pq.poll(); // remove least frequent / "least important" word
            }
        }

        // Build result in descending order
        while (!pq.isEmpty()) {
            res.add(0, pq.poll());
        }

        return res;
    }

    // V1
    // IDEA: SORT
    // https://leetcode.com/problems/top-k-frequent-words/solutions/1244692/java-solution-by-keerthy0212-1jtv/
    public List<String> topKFrequent_1(String[] words, int k) {
        HashMap<String, Integer> freq = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            freq.put(words[i], freq.getOrDefault(words[i], 0) + 1);
        }
        List<String> res = new ArrayList(freq.keySet());
        // sorting
        // if two words have the same frequency, then the word with the lower
        // alphabetical order comes first.
        // else most frequent words will come first
        Collections.sort(res,
                (w1, w2) -> freq.get(w1).equals(freq.get(w2)) ? w1.compareTo(w2) : freq.get(w2) - freq.get(w1));

        return res.subList(0, k);
    }

    // V2
    // IDEA: SORT
    // https://leetcode.com/problems/top-k-frequent-words/solutions/2720232/java-easy-solution-with-explanation-hash-mk80/
    public List<String> topKFrequent_2(String[] words, int k) {

        // map hold the word: counts
        HashMap<String, Integer> map = new HashMap();

        // sort the map by frequency high->low order, sort words lexi order
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(
                (a, b) -> {
                    if (a.getValue() != b.getValue())
                        return a.getValue().compareTo(b.getValue());
                    return -a.getKey().compareTo(b.getKey());
                });

        // fill the map
        for (String word : words) {
            map.merge(word, 1, Integer::sum);
        }

        // put into heap
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            heap.offer(entry);
            if (heap.size() > k)
                heap.poll();
        }

        // pop out the answer
        List<String> ans = new ArrayList();
        while (heap.size() > 0)
            ans.add(heap.poll().getKey());

        // check the order
        Collections.reverse(ans);
        return ans;
    }

    // V3
    // IDEA: SORT + STREAM API
    // https://leetcode.com/problems/top-k-frequent-words/solutions/2721452/java-stream-api-memory-usage-less-than-8-d1t4/
    public List<String> topKFrequent_3(String[] words, int k) {
        TreeMap<String, Integer> map = new TreeMap<>(String::compareTo);
        Arrays.stream(words).forEach(x -> map.put(x, map.getOrDefault(x, 0) + 1));
        return map.entrySet().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .map(Map.Entry::getKey)
                .limit(k)
                .collect(Collectors.toList());
    }

    // V4
    // https://leetcode.com/problems/top-k-frequent-words/solutions/5983153/simple-solution-with-diagrams-in-video-j-myeq/
    class TrieNode {
        TrieNode[] children;
        String word;

        public TrieNode() {
            this.children = new TrieNode[26];
            this.word = null;
        }
    }

    class Trie {
        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void addWord(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (cur.children[index] == null) {
                    cur.children[index] = new TrieNode();
                }
                cur = cur.children[index];
            }
            cur.word = word;
        }

        public void getWords(TrieNode node, List<String> result) {
            if (node == null)
                return;
            if (node.word != null)
                result.add(node.word);
            for (TrieNode child : node.children) {
                if (child != null) {
                    getWords(child, result);
                }
            }
        }

        // Added method to expose root
        public TrieNode getRoot() {
            return root;
        }
    }
    public List<String> topKFrequent_4(String[] words, int k) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        List<Trie> buckets = new ArrayList<>(Collections.nCopies(words.length + 1, null));
        List<String> topK = new ArrayList<>();

        // Count word frequencies
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        // Add words to buckets based on frequency
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String word = entry.getKey();
            int freq = entry.getValue();
            if (buckets.get(freq) == null) {
                buckets.set(freq, new Trie());
            }
            buckets.get(freq).addWord(word);
        }

        // Retrieve top k words from buckets
        for (int i = buckets.size() - 1; i >= 0 && topK.size() < k; i--) {
            if (buckets.get(i) != null) {
                List<String> wordsInBucket = new ArrayList<>();
                // Use the new getRoot method
                buckets.get(i).getWords(buckets.get(i).getRoot(), wordsInBucket);

                // Sort words in lexicographical order
                Collections.sort(wordsInBucket);

                // Add words from the bucket to the result until top k is filled
                for (String word : wordsInBucket) {
                    if (topK.size() < k) {
                        topK.add(word);
                    } else {
                        break;
                    }
                }
            }
        }

        return topK;
    }

}
