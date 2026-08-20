package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-the-k-th-character-in-string-game-i/

/**
 *  3304. Find the K-th Character in String Game I
 *  Easy
 *
 *  Alice and Bob are playing a game. Initially, Alice has a string word = "a".
 *
 *  You are given a positive integer k.
 *
 *  Now Bob will ask Alice to perform the following operation forever:
 *    Generate a new string by changing each character in word to its next
 *    character in the English alphabet, and append it to the original word.
 *
 *  For example, performing the operation on "c" generates "cd" and performing
 *  the operation on "zb" generates "zbac".
 *
 *  Return the value of the kth character in word, after enough operations have
 *  been done for word to have at least k characters.
 *
 *  Note that the character 'z' can be changed to 'a' in the operation.
 *
 *  Example 1:
 *    Input: k = 5
 *    Output: "b"
 *    Explanation: word goes "a" -> "ab" -> "abbc" -> "abbcbccd"; the 5th char
 *                 is 'b'.
 *
 *  Example 2:
 *    Input: k = 10
 *    Output: "c"
 *
 *  Constraints:
 *    1 <= k <= 500
 */
public class FindTheKthCharacterInStringGameI {

    // V0
    // IDEA: EACH DOUBLING SHIFTS THE COPY BY ONE -> POPCOUNT OF (k-1)
    //       the string doubles every round and the second half is the first half
    //       shifted by one letter, so position p (0-indexed) in the new half maps
    //       back to p - half in the old one, one letter lower.
    //       peeling those halves off is exactly reading the binary digits of the
    //       index: every 1 bit crossed adds one shift, hence
    //           answer = 'a' + popcount(k - 1)
    //       (the shifts stay well inside the alphabet for k <= 500; the % 26 keeps
    //        the 'z' -> 'a' wrap correct in general).
    /**
     * time = O(1)
     * space = O(1)
     */
    public char kthCharacter(int k) {
        return (char) ('a' + (Integer.bitCount(k - 1) % 26));
    }
}
