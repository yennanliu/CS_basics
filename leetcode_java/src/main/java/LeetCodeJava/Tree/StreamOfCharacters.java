package LeetCodeJava.Tree;

// https://leetcode.com/problems/stream-of-characters/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1032. Stream of Characters
 * Hard
 * Topics
 * Companies
 * Hint
 * Design an algorithm that accepts a stream of characters and checks if a suffix of these characters is a string of a given array of strings words.
 *
 * For example, if words = ["abc", "xyz"] and the stream added the four characters (one by one) 'a', 'x', 'y', and 'z', your algorithm should detect that the suffix "xyz" of the characters "axyz" matches "xyz" from words.
 *
 * Implement the StreamChecker class:
 *
 * StreamChecker(String[] words) Initializes the object with the strings array words.
 * boolean query(char letter) Accepts a new character from the stream and returns true if any non-empty suffix from the stream forms a word that is in words.
 *
 *
 * Example 1:
 *
 * Input
 * ["StreamChecker", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query"]
 * [[["cd", "f", "kl"]], ["a"], ["b"], ["c"], ["d"], ["e"], ["f"], ["g"], ["h"], ["i"], ["j"], ["k"], ["l"]]
 * Output
 * [null, false, false, false, true, false, true, false, false, false, false, false, true]
 *
 * Explanation
 * StreamChecker streamChecker = new StreamChecker(["cd", "f", "kl"]);
 * streamChecker.query("a"); // return False
 * streamChecker.query("b"); // return False
 * streamChecker.query("c"); // return False
 * streamChecker.query("d"); // return True, because 'cd' is in the wordlist
 * streamChecker.query("e"); // return False
 * streamChecker.query("f"); // return True, because 'f' is in the wordlist
 * streamChecker.query("g"); // return False
 * streamChecker.query("h"); // return False
 * streamChecker.query("i"); // return False
 * streamChecker.query("j"); // return False
 * streamChecker.query("k"); // return False
 * streamChecker.query("l"); // return True, because 'kl' is in the wordlist
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 2000
 * 1 <= words[i].length <= 200
 * words[i] consists of lowercase English letters.
 * letter is a lowercase English letter.
 * At most 4 * 104 calls will be made to query.
 *
 */
public class StreamOfCharacters {

    // V0
    /**
     * Your StreamChecker object will be instantiated and called as such:
     * StreamChecker obj = new StreamChecker(words);
     * boolean param_1 = obj.query(letter);
     */
    // TODO : implement below
//    class StreamChecker {
//
//        public StreamChecker(String[] words) {
//
//        }
//
//        public boolean query(char letter) {
//
//        }
//    }


    // V1

    // V2
    // https://leetcode.com/problems/stream-of-characters/solutions/1610403/java-simple-solution-trie-detailed-explanation-using-image/
    class StreamChecker_2 {

        class TrieNode{
            boolean isWord;
            TrieNode children[] = new TrieNode[26];
        }

        TrieNode root = new TrieNode();
        int maxSize;
        StringBuilder sb = new StringBuilder();

        public StreamChecker_2(String[] words) {
            insert(words);
        }

        public boolean query(char letter) {
            if(sb.length()>=maxSize){
                sb.deleteCharAt(0);
            }
            sb.append(letter);
            TrieNode curr = root;

            for(int i=sb.length()-1;i>=0;i--){
                char ch = sb.charAt(i);

                if(curr!=null) curr = curr.children[ch-'a'];

                if(curr!=null && curr.isWord) return true;
            }
            return false;
        }

        public void insert(String[] words){

            for(String s : words){
                maxSize = Math.max(maxSize,s.length());
                TrieNode curr = root;
                for(int i = s.length()-1;i>=0;i--){
                    char ch = s.charAt(i);
                    if(curr.children[ch-'a']==null){
                        curr.children[ch-'a'] = new TrieNode();
                    }
                    curr = curr.children[ch-'a'];
                }
                curr.isWord = true;
            }
        }
    }

    // V4
    // https://leetcode.com/problems/stream-of-characters/solutions/396285/java-solution-with-trie-beats-97-8-easy-to-understand/
    class StreamChecker_4 {

        final static int ALPHABET_LENGTH = 26;
        TrieNode root;
        List<Character> history;

        class TrieNode {
            boolean isWord;
            TrieNode[] next;

            TrieNode() {
                isWord = false;
                next = new TrieNode[ALPHABET_LENGTH];
            }
        }

        public StreamChecker_4(String[] words) {
            root = new TrieNode();
            buildTrie(words);
            history = new ArrayList<>();
        }

        void buildTrie(String[] words) {
            for (String s: words) {
                insert(s);
            }
        }

        void insert(String s) {
            TrieNode p = root;
            //build trie but in reversed order for each word
            for (int i=s.length()-1; i>=0; i--) {
                char c = s.charAt(i);
                if (p.next[c-'a'] == null) {
                    p.next[c-'a'] = new TrieNode();
                }
                p = p.next[c-'a'];
            }
            p.isWord = true;
        }

        public boolean query(char letter) {
            history.add(letter);
            TrieNode p = root;
            for (int i = history.size()-1; i >=0; i--) {
                char c = history.get(i);
                //return false immediately when we can't find c in Trie
                if (p.next[c-'a'] == null) return false;
                //find a word
                if (p.next[c-'a'].isWord) return true;
                p = p.next[c-'a'];
            }
            return false;
        }
    }

    // V5
    // https://leetcode.com/problems/stream-of-characters/solutions/514248/java-99-time-100-memory/
    class StreamChecker_5 {

        class Trie {
            boolean word;
            Trie[] next;

            public Trie() {
                this.next = new Trie[26];
                this.word = false;
            }
        }

        Trie root = null;
        int p = 0;
        char[] history;

        public void insert(String word) {
            Trie curr = root;
            for (int i = word.length() - 1; i >=0; i--) {
                if (curr.next[word.charAt(i) - 'a'] == null) {
                    curr.next[word.charAt(i) - 'a'] = new Trie();
                }
                curr = curr.next[word.charAt(i) - 'a'];
            }
            curr.word = true;
        }

        public StreamChecker_5(String[] words) {
            this.root = new Trie();
            for (String word : words) {
                insert(word);
            }
            this.history = new char[2000];
        }

        public boolean query(char letter) {
            // System.out.println("query " + letter);
            history[p % 2000] = letter;
            Trie curr = root;
            boolean match = false;
            for (int i = 0; i < 2000 && !match; i++) {
                char curChar = history[(p - i + 2000) % 2000];
                // System.out.println("look " + curChar);
                if (curChar == 0) {
                    break;
                }
                if (curr.next[curChar - 'a'] != null) {
                    curr = curr.next[curChar - 'a'];
                    if (curr.word) {
                        match = true;
                        // System.out.println("match " + curChar);
                    }
                } else {
                    break;
                }
            }
            p++;
            return match;
        }
    }

}
