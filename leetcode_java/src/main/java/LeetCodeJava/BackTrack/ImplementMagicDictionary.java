package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/implement-magic-dictionary/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  676. Implement Magic Dictionary
 *  Medium
 *
 *  Design a data structure that is initialized with a list of different words.
 *  Provided a string, you should determine if you can change exactly one character
 *  in this string to match any word in the data structure.
 *
 *  Implement the MagicDictionary class:
 *
 *  - MagicDictionary() Initializes the object.
 *  - void buildDict(String[] dictionary) Sets the data structure with an array of
 *    distinct strings dictionary.
 *  - bool search(String searchWord) Returns true if you can change exactly one
 *    character in searchWord to match any string in the data structure,
 *    otherwise returns false.
 *
 *  Example 1:
 *
 *  Input
 *  ["MagicDictionary", "buildDict", "search", "search", "search", "search"]
 *  [[], [["hello", "leetcode"]], ["hello"], ["hhllo"], ["hell"], ["leetcoded"]]
 *  Output
 *  [null, null, false, true, false, false]
 *
 *  Constraints:
 *
 *  1 <= dictionary.length <= 100
 *  1 <= dictionary[i].length <= 100
 *  dictionary[i] consists of only lower-case English letters.
 *  All the strings in dictionary are distinct.
 *  1 <= searchWord.length <= 100
 *  searchWord consists of only lower-case English letters.
 *  buildDict will be called only once before search.
 *  At most 100 calls will be made to search.
 */
public class ImplementMagicDictionary {

    private final Map<String, Set<Character>> dmap;

    // V0
    // IDEA: bucket every word under its "wildcard" keys (word with one char masked),
    //       mapping key -> set of the chars that were masked out
    /**
     * time = O(k * l) build (k words, l = word length), O(l) per search
     * space = O(k * l)
     */
    public ImplementMagicDictionary() {
        this.dmap = new HashMap<>();
    }

    public void buildDict(String[] dictionary) {
        this.dmap.clear();
        for (String word : dictionary) {
            char[] arr = word.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                char origin = arr[i];
                arr[i] = '_';
                String key = new String(arr);
                arr[i] = origin;
                Set<Character> set = this.dmap.get(key);
                if (set == null) {
                    set = new HashSet<>();
                    this.dmap.put(key, set);
                }
                set.add(origin);
            }
        }
    }

    public boolean search(String searchWord) {
        char[] arr = searchWord.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char origin = arr[i];
            arr[i] = '_';
            String key = new String(arr);
            arr[i] = origin;
            Set<Character> values = this.dmap.get(key);
            if (values == null || values.isEmpty()) {
                continue;
            }
            /**
             * NOTE !!!
             *
             * we MUST change exactly one char, so the bucket has to contain
             * a char DIFFERENT from the current one
             */
            if (!values.contains(origin) || values.size() > 1) {
                return true;
            }
        }
        return false;
    }
}
