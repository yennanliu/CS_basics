package LeetCodeJava.BFS;

import java.util.*;

public class WorderLadder {

    // V0
    // TODO : implement
//    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
//
//    }

    // V0'
    // IDEA : BFS (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/word-ladder.py#L42
    public int ladderLength_0_1(String beginWord, String endWord, List<String> wordList) {
        // Convert wordList to a set for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordList);
        // Queue for BFS
        Queue<Pair> bfs = new LinkedList<>();
        // Add the beginWord with the initial length of 1
        bfs.offer(new Pair(beginWord, 1));

        while (!bfs.isEmpty()) {
            Pair current = bfs.poll();
            String word = current.word;
            int length = current.length;

            // If we reach the endWord, return the length of the sequence
            if (word.equals(endWord)) {
                return length;
            }

            // Try changing each character of the current word to find new words
            for (int i = 0; i < word.length(); i++) {
                char[] wordArray = word.toCharArray();
                for (char c = 'a'; c <= 'z'; c++) {
                    wordArray[i] = c;
                    String newWord = new String(wordArray);

                    // If newWord is in the wordSet and it's not the original word
                    if (wordSet.contains(newWord) && !newWord.equals(word)) {
                        wordSet.remove(newWord);
                        bfs.offer(new Pair(newWord, length + 1));
                    }
                }
            }
        }

        // If no sequence is found, return 0
        return 0;
    }

    // Helper class to store the current word and the sequence length
    private static class Pair {
        String word;
        int length;

        Pair(String word, int length) {
            this.word = word;
            this.length = length;
        }
    }

    // V1
    // IDEA : BFS + PAIR
    // https://leetcode.com/problems/word-ladder/solutions/4538368/word-ladder-simple-bfs-java/
    class Pair_{
        String str;
        int count;
        Pair_(String str,int count){
            this.str = str;
            this.count = count;
        }
    }

    public int ladderLength_1(String beginWord, String endWord, List<String> wordList) {

        Queue<Pair_> q = new LinkedList<>();
        Set<String> set = new HashSet<>();

        q.offer(new Pair_(beginWord,1));

        for(String s : wordList)
            set.add(s);

        set.remove(beginWord);

        while(!q.isEmpty()){

            String currStr = q.peek().str;
            int currCount = q.peek().count;
            q.poll();
            int len = currStr.length();

            if(currStr.equals(endWord))
                return currCount;

            for( int i=0 ; i<len ; i++){
                for(char ch='a' ; ch<='z' ;ch++){
                    String temp = currStr.substring(0,i) + ch + currStr.substring(i+1);
                    if(set.contains(temp)){
                        set.remove(temp);
                        q.offer(new Pair_(temp , currCount+1));
                    }
                }
            }
        }

        return 0;
    }

}
