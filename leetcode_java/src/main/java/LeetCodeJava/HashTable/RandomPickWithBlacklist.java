package LeetCodeJava.HashTable;

// https://leetcode.com/problems/random-pick-with-blacklist/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *  710. Random Pick with Blacklist
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer n and an array of unique integers blacklist. Design an algorithm to pick a random integer in the range [0, n - 1] that is not in blacklist. Any integer that is in the mentioned range and not in blacklist should be equally likely to be returned.
 *
 * Optimize your algorithm such that it minimizes the number of calls to the built-in random function of your language.
 *
 * Implement the Solution class:
 *
 * Solution(int n, int[] blacklist) Initializes the object with the integer n and the blacklisted integers blacklist.
 * int pick() Returns a random integer in the range [0, n - 1] and not in blacklist.
 *
 *
 * Example 1:
 *
 * Input
 * ["Solution", "pick", "pick", "pick", "pick", "pick", "pick", "pick"]
 * [[7, [2, 3, 5]], [], [], [], [], [], [], []]
 * Output
 * [null, 0, 4, 1, 6, 1, 0, 4]
 *
 * Explanation
 * Solution solution = new Solution(7, [2, 3, 5]);
 * solution.pick(); // return 0, any integer from [0,1,4,6] should be ok. Note that for every call of pick,
 *                  // 0, 1, 4, and 6 must be equally likely to be returned (i.e., with probability 1/4).
 * solution.pick(); // return 4
 * solution.pick(); // return 1
 * solution.pick(); // return 6
 * solution.pick(); // return 1
 * solution.pick(); // return 0
 * solution.pick(); // return 4
 *
 *
 * Constraints:
 *
 * 1 <= n <= 109
 * 0 <= blacklist.length <= min(105, n - 1)
 * 0 <= blacklist[i] < n
 * All the values of blacklist are unique.
 * At most 2 * 104 calls will be made to pick.
 *
 */
public class RandomPickWithBlacklist {

    // V0
//    class Solution {
//
//        public Solution(int n, int[] blacklist) {
//
//        }
//
//        public int pick() {
//
//        }
//    }


    // V1


    // V2
    // https://leetcode.com/problems/random-pick-with-blacklist/solutions/6459117/simple-o1-design-with-detailed-explanati-g9yk/
//    List<int[]> dividedRange;
//    int numberOfDivisions = 0;
//    Random rand;
//
//    public Solution_1_1(int n, int[] arr) {
//        Arrays.sort(arr);
//        rand = new Random();
//        dividedRange = new ArrayList<>();
//        int start = 0, end = 0;
//        for (int i = 0; i < arr.length; i++) {
//            end = arr[i] - 1;
//            if (start > end) {
//                start = arr[i] + 1;
//                continue;
//            }
//            dividedRange.add(new int[] { start, end });
//            start = arr[i] + 1;
//        }
//        if (start < n) {
//            dividedRange.add(new int[] { start, n - 1 });
//        }
//        numberOfDivisions = dividedRange.size();
//    }
//
//    public int pick() {
//        int pickedDivision = rand.nextInt(numberOfDivisions);
//        int start = dividedRange.get(pickedDivision)[0];
//        int end = dividedRange.get(pickedDivision)[1];
//        return rand.nextInt(end - start + 1) + start;
//    }




    // V3
    // https://leetcode.com/problems/random-pick-with-blacklist/solutions/4296646/java-39ms-beats-99-randomly-pick-up-numb-uln4/



    // V4
    // https://leetcode.com/problems/random-pick-with-blacklist/solutions/1992074/java-solution-using-weighted-intervals-b-lryq/




}
