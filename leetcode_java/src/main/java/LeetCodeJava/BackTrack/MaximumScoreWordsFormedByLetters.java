package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/maximum-score-words-formed-by-letters/

/**
 *  1255. Maximum Score Words Formed by Letters
 *  Hard
 *
 *  Given a list of words, list of single letters (might be repeating) and score
 *  of every character.
 *
 *  Return the maximum score of any valid set of words formed by using the given
 *  letters (words[i] cannot be used two or more times).
 *
 *  It is not necessary to use all characters in letters and each letter can only
 *  be used once. Score of letters 'a', 'b', 'c', ..., 'z' is given by score[0],
 *  score[1], ..., score[25] respectively.
 *
 *  Example 1:
 *    Input: words = ["dog","cat","dad","good"],
 *           letters = ["a","a","c","d","d","d","g","o","o"],
 *           score = [1,0,9,5,0,0,3,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0]
 *    Output: 23
 *    Explanation: a=1, c=9, d=5, g=3, o=2 -> "dad" (11) + "good" (12) = 23
 *
 *  Example 2:
 *    Input: words = ["xxxz","ax","bx","cx"],
 *           letters = ["z","a","b","c","x","x","x"],
 *           score = [4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,10]
 *    Output: 27
 *    Explanation: "ax" + "bx" + "cx" = 9 + 9 + 9 = 27
 *
 *  Constraints:
 *    1 <= words.length <= 14
 *    1 <= words[i].length <= 15
 *    1 <= letters.length <= 100
 *    letters[i].length == 1
 *    score.length == 26
 *    0 <= score[i] <= 10
 *    words[i], letters[i] contains only lower case English letters.
 */
public class MaximumScoreWordsFormedByLetters {

    // V0
    // IDEA: BACKTRACKING (take / skip each word, undo the letter spend on return)
    //       n <= 14, so exploring every subset of words is fine.
    //       walk the words left to right holding a live letter budget:
    //         - skip word i                                    -> recurse i + 1
    //         - take word i, if every letter is still in stock -> pay the
    //           letters, add its score, recurse i + 1, then REFUND the letters
    //       NOTE: the refund (backtrack step) is what keeps the two branches
    //             independent - without it the budget leaks across subsets.
    //       NOTE: scoring a word is a pure function of its letters, so
    //             precompute each word's letter counts + score once.
    /**
     * time = O(2^n * 26), space = O(n * 26)
     *   n = words.length
     */
    private int res;
    private int[] budget;
    private int[][] need;
    private int[] gain;

    public int maxScoreWords(String[] words, char[] letters, int[] score) {
        this.budget = new int[26];
        for (char c : letters) {
            this.budget[c - 'a']++;
        }

        int n = words.length;
        this.need = new int[n][26];
        this.gain = new int[n];
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) {
                this.need[i][c - 'a']++;
                this.gain[i] += score[c - 'a'];
            }
        }

        this.res = 0;
        backtrack(0, 0, n);
        return this.res;
    }

    private void backtrack(int i, int cur, int n) {
        if (i == n) {
            this.res = Math.max(this.res, cur);
            return;
        }

        // skip words[i]
        backtrack(i + 1, cur, n);

        // take words[i] if affordable
        boolean ok = true;
        for (int c = 0; c < 26; c++) {
            if (this.budget[c] < this.need[i][c]) {
                ok = false;
                break;
            }
        }
        if (ok) {
            for (int c = 0; c < 26; c++) {
                this.budget[c] -= this.need[i][c];
            }
            backtrack(i + 1, cur + this.gain[i], n);
            for (int c = 0; c < 26; c++) {
                this.budget[c] += this.need[i][c];
            }
        }
    }
}
