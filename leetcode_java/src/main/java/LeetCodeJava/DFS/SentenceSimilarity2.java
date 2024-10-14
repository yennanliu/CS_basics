package LeetCodeJava.DFS;

// https://leetcode.com/problems/sentence-similarity-ii/editorial/
// https://leetcode.ca/all/737.html

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 737. Sentence Similarity II
 * Given two sentences words1, words2 (each represented as an array of strings), and a list of similar word pairs pairs, determine if two sentences are similar.
 *
 * For example, words1 = ["great", "acting", "skills"] and words2 = ["fine", "drama", "talent"] are similar, if the similar word pairs are pairs = [["great", "good"], ["fine", "good"], ["acting","drama"], ["skills","talent"]].
 *
 * Note that the similarity relation is transitive. For example, if "great" and "good" are similar, and "fine" and "good" are similar, then "great" and "fine" are similar.
 *
 * Similarity is also symmetric. For example, "great" and "fine" being similar is the same as "fine" and "great" being similar.
 *
 * Also, a word is always similar with itself. For example, the sentences words1 = ["great"], words2 = ["great"], pairs = [] are similar, even though there are no specified similar word pairs.
 *
 * Finally, sentences can only be similar if they have the same number of words. So a sentence like words1 = ["great"] can never be similar to words2 = ["doubleplus","good"].
 *
 * Note:
 *
 * The length of words1 and words2 will not exceed 1000.
 * The length of pairs will not exceed 2000.
 * The length of each pairs[i] will be 2.
 * The length of each words[i] and pairs[i][j] will be in the range [1, 20].
 *
 */
public class SentenceSimilarity2 {

    // V0
    // TODO : implement
//    public boolean areSentencesSimilarTwo(){}

    // V1
    // https://leetcode.ca/2017-12-06-737-Sentence-Similarity-II/
    private int[] p;

    public boolean areSentencesSimilarTwo_1(
            String[] sentence1, String[] sentence2, List<List<String>> similarPairs) {
        if (sentence1.length != sentence2.length) {
            return false;
        }
        int n = similarPairs.size();
        p = new int[n << 1];
        for (int i = 0; i < p.length; ++i) {
            p[i] = i;
        }
        Map<String, Integer> words = new HashMap<>();
        int idx = 0;
        for (List<String> e : similarPairs) {
            String a = e.get(0), b = e.get(1);
            if (!words.containsKey(a)) {
                words.put(a, idx++);
            }
            if (!words.containsKey(b)) {
                words.put(b, idx++);
            }
            p[find(words.get(a))] = find(words.get(b));
        }
        for (int i = 0; i < sentence1.length; ++i) {
            if (Objects.equals(sentence1[i], sentence2[i])) {
                continue;
            }
            if (!words.containsKey(sentence1[i]) || !words.containsKey(sentence2[i])
                    || find(words.get(sentence1[i])) != find(words.get(sentence2[i]))) {
                return false;
            }
        }
        return true;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }

    // V2

    // V3
    // IDEA : Disjoint-set
    // https://blog.csdn.net/susuxuezhang/article/details/100127908
    // https://www.cnblogs.com/grandyang/p/8053934.html

    // V4-1
    // IDEA : DFS
    // https://zxi.mytechroad.com/blog/hashtable/leetcode-737-sentence-similarity-ii/
    // C++
//    class Solution {
//        public:
//        bool areSentencesSimilarTwo(vector<string>& words1, vector<string>& words2, vector<pair<string, string>>& pairs) {
//            if (words1.size() != words2.size()) return false;
//
//            g_.clear();
//
//            for (const auto& p : pairs) {
//                g_[p.first].insert(p.second);
//                g_[p.second].insert(p.first);
//            }
//
//            unordered_set<string> visited;
//
//            for (int i = 0; i < words1.size(); ++i) {
//                visited.clear();
//                if (!dfs(words1[i], words2[i], visited)) return false;
//            }
//
//            return true;
//        }
//        private:
//        bool dfs(const string& src, const string& dst, unordered_set<string>& visited) {
//            if (src == dst) return true;
//            visited.insert(src);
//            for (const auto& next : g_[src]) {
//                if (visited.count(next)) continue;
//                if (dfs(next, dst, visited)) return true;
//            }
//            return false;
//        }
//        unordered_map<string, unordered_set<string>> g_;
//    };


    // V4-2
    // IDEA : Union Find
    // https://zxi.mytechroad.com/blog/hashtable/leetcode-737-sentence-similarity-ii/
    // C++
//    class UnionFindSet {
//        public:
//        bool Union(const string& word1, const string& word2) {
//        const string& p1 = Find(word1, true);
//        const string& p2 = Find(word2, true);
//            if (p1 == p2) return false;
//            parents_[p1] = p2;
//            return true;
//        }
//
//    const string& Find(const string& word, bool create = false) {
//            if (!parents_.count(word)) {
//                if (!create) return word;
//                return parents_[word] = word;
//            }
//
//            string w = word;
//            while (w != parents_[w]) {
//                parents_[w] = parents_[parents_[w]];
//                w = parents_[w];
//            }
//
//            return parents_[w];
//        }
//        private:
//        unordered_map<string, string> parents_;
//    };
//
//    class Solution {
//        public:
//        bool areSentencesSimilarTwo(vector<string>& words1, vector<string>& words2, vector<pair<string, string>>& pairs) {
//            if (words1.size() != words2.size()) return false;
//
//            UnionFindSet s;
//            for (const auto& pair : pairs)
//            s.Union(pair.first, pair.second);
//
//            for (int i = 0; i < words1.size(); ++i)
//                if (s.Find(words1[i]) != s.Find(words2[i])) return false;
//
//            return true;
//        }
//    };

}
