package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/synonymous-sentences/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1258. Synonymous Sentences
 *  Medium
 *
 *  You are given a list of equivalent string pairs synonyms where
 *  synonyms[i] = [si, ti] indicates that si and ti are equivalent strings. You are
 *  also given a sentence text.
 *
 *  Return all possible synonymous sentences sorted lexicographically.
 *
 *  Example 1:
 *    Input: synonyms = [["happy","joy"],["sad","sorrow"],["joy","cheerful"]],
 *           text = "I am happy today but was sad yesterday"
 *    Output: ["I am cheerful today but was sad yesterday",
 *             "I am cheerful today but was sorrow yesterday",
 *             "I am happy today but was sad yesterday",
 *             "I am happy today but was sorrow yesterday",
 *             "I am joy today but was sad yesterday",
 *             "I am joy today but was sorrow yesterday"]
 *
 *  Example 2:
 *    Input: synonyms = [["happy","joy"],["cheerful","glad"]],
 *           text = "I am happy today but was sad yesterday"
 *    Output: ["I am happy today but was sad yesterday",
 *             "I am joy today but was sad yesterday"]
 *
 *  Constraints:
 *    0 <= synonyms.length <= 10
 *    synonyms[i].length == 2
 *    1 <= si.length, ti.length <= 10
 *    si != ti
 *    text consists of at most 10 words.
 *    All the pairs of synonyms are unique.
 *    The words of text are separated by single spaces.
 */
public class SynonymousSentences {

    private Map<String, String> parent = new HashMap<>();
    private Map<String, List<String>> group = new HashMap<>();
    private String[] words;
    private List<String> cur = new ArrayList<>();
    private List<String> res = new ArrayList<>();

    // V0
    // IDEA: UNION FIND + BACKTRACK (DFS)
    //       1) union all synonym pairs -> each group = a set of interchangeable words
    //       2) DFS over the words of `text`, branching on every word of its group
    //       words with no synonym are kept as they are (single branch).
    /**
     * time = O(m^w * w), m = max group size, w = number of words
     * space = O(n + w), n = number of distinct synonym words
     */
    public List<String> generateSentences(List<List<String>> synonyms, String text) {
        for (List<String> p : synonyms) {
            union(p.get(0), p.get(1));
        }
        for (String w : parent.keySet()) {
            String r = find(w);
            if (!group.containsKey(r)) {
                group.put(r, new ArrayList<String>());
            }
            group.get(r).add(w);
        }
        for (List<String> g : group.values()) {
            Collections.sort(g);
        }

        this.words = text.split(" ");
        dfs(0);
        Collections.sort(res);
        return res;
    }

    private String find(String x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x);
        }
        while (!parent.get(x).equals(x)) {
            parent.put(x, parent.get(parent.get(x)));
            x = parent.get(x);
        }
        return x;
    }

    private void union(String a, String b) {
        String ra = find(a);
        String rb = find(b);
        if (!ra.equals(rb)) {
            parent.put(ra, rb);
        }
    }

    private void dfs(int i) {
        if (i == words.length) {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < cur.size(); k++) {
                if (k > 0) {
                    sb.append(' ');
                }
                sb.append(cur.get(k));
            }
            res.add(sb.toString());
            return;
        }
        String w = words[i];
        if (!parent.containsKey(w)) {
            // word has no synonym -> keep it as it is
            cur.add(w);
            dfs(i + 1);
            cur.remove(cur.size() - 1);
        } else {
            for (String cand : group.get(find(w))) {
                cur.add(cand);
                dfs(i + 1);
                cur.remove(cur.size() - 1);
            }
        }
    }
}
