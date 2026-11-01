package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/filling-bookcase-shelves/description/

/**
 * 1105. Filling Bookcase Shelves
 * Medium
 *
 * You are given an array books where books[i] = [thickness_i, height_i] indicates
 * the thickness and height of the i-th book. You are also given an integer shelfWidth.
 *
 * We want to place these books in order onto bookcase shelves that have a total
 * width shelfWidth.
 *
 * We choose some of the books to place on this shelf such that the sum of their
 * thickness is less than or equal to shelfWidth, then build another level of the
 * shelf of the bookcase so that the total height of the bookcase has increased by
 * the maximum height of the books we just put down. We repeat this process until
 * there are no more books to place.
 *
 * Note that at each step of the above process, the order of the books we place is
 * the same order as the given sequence of books.
 *
 * - For example, if we have an ordered list of 5 books, we might place the first
 *   and second book onto the first shelf, the third book on the second shelf, and
 *   the fourth and fifth book on the last shelf.
 *
 * Return the minimum possible height that the total bookshelf can be after placing
 * shelves in this manner.
 *
 *
 * Example 1:
 *
 * Input: books = [[1,1],[2,3],[2,3],[1,1],[1,1],[1,1],[1,2]], shelfWidth = 4
 * Output: 6
 * Explanation:
 * The sum of the heights of the 3 shelves is 1 + 3 + 2 = 6.
 * Notice that book number 2 does not have to be on the first shelf.
 *
 * Example 2:
 *
 * Input: books = [[1,3],[2,4],[3,2]], shelfWidth = 6
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= books.length <= 1000
 * 1 <= thickness_i <= shelfWidth <= 1000
 * 1 <= height_i <= 1000
 *
 */
public class FillingBookcaseShelves {

    // V0
    // IDEA: 1D DP (PARTITION THE ORDERED BOOKS INTO CONSECUTIVE SHELVES)
    /**
     * dp[i] = min total height after placing the first i books
     *
     * the last shelf holds books j..i (1-indexed), all fitting in shelfWidth:
     *   dp[i] = min over j of ( dp[j-1] + max height of books j..i )
     *
     * base: dp[0] = 0
     */
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minHeightShelves(int[][] books, int shelfWidth) {
        int n = books.length;
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            int shelfThickness = books[i - 1][0];
            int shelfHeight = books[i - 1][1];

            // case 1: book i starts a new shelf on its own
            dp[i] = dp[i - 1] + shelfHeight;

            // case 2: extend the last shelf leftwards to include book j
            for (int j = i - 1; j >= 1; j--) {
                shelfThickness += books[j - 1][0];
                // stop as soon as the shelf overflows; adding more books only makes it wider
                if (shelfThickness > shelfWidth) {
                    break;
                }
                shelfHeight = Math.max(shelfHeight, books[j - 1][1]);
                dp[i] = Math.min(dp[i], dp[j - 1] + shelfHeight);
            }
        }
        return dp[n];
    }
}
