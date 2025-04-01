package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/guess-number-higher-or-lower/
/**
 * 374. Guess Number Higher or Lower
 * Solved
 * Easy
 * Topics
 * Companies
 * We are playing the Guess Game. The game is as follows:
 *
 * I pick a number from 1 to n. You have to guess which number I picked.
 *
 * Every time you guess wrong, I will tell you whether the number I picked is higher or lower than your guess.
 *
 * You call a pre-defined API int guess(int num), which returns three possible results:
 *
 * -1: Your guess is higher than the number I picked (i.e. num > pick).
 * 1: Your guess is lower than the number I picked (i.e. num < pick).
 * 0: your guess is equal to the number I picked (i.e. num == pick).
 * Return the number that I picked.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 10, pick = 6
 * Output: 6
 * Example 2:
 *
 * Input: n = 1, pick = 1
 * Output: 1
 * Example 3:
 *
 * Input: n = 2, pick = 1
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= n <= 231 - 1
 * 1 <= pick <= n
 *
 */
public class GuessNumberHigherOrLower {

    /**
     * Forward declaration of guess API.
     * @param  num   your guess
     * @return 	     -1 if num is higher than the picked number
     *			      1 if num is lower than the picked number
     *               otherwise return 0
     * int guess(int num);
     */
    // V0
//    public int guessNumber(int n) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/guess-number-higher-or-lower/solutions/6592740/simple-java-code-using-binary-search-by-3mz5a/
    public int guessNumber_2(int n) {
        int low = 1;
        int high = n;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (guess(mid) == 0) {
                return mid;
            } else if (guess(mid) == -1) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    // V3
    // https://leetcode.com/problems/guess-number-higher-or-lower/solutions/6087292/easy-beats-proof-java-beginner-frendly-d-hxt7/
    public int guessNumber_3(int n) {
        int low = 0;
        int high = n;
        int mid = 0;

        while (low <= high) {
            mid = low + (high - low) / 2;

            if (guess(mid) == 0)
                return mid;
            if (guess(mid) == -1)
                high = mid - 1;
            if (guess(mid) == 1)
                low = mid + 1;
        }

        return -1;
    }

    public int guess(int num){
        return 0;
    }

}
