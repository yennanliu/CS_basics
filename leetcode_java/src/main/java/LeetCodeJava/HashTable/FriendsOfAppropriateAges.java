package LeetCodeJava.HashTable;

// https://leetcode.com/problems/friends-of-appropriate-ages/

/**
 *  825. Friends Of Appropriate Ages
 *  Medium
 *
 *  There are n persons on a social media website. You are given an integer array
 *  ages where ages[i] is the age of the ith person.
 *
 *  A Person x will NOT send a friend request to a person y (x != y) if any of the
 *  following conditions is true:
 *    - age[y] <= 0.5 * age[x] + 7
 *    - age[y] > age[x]
 *    - age[y] > 100 && age[x] < 100
 *  Otherwise, x will send a friend request to y.
 *
 *  Note that if x sends a request to y, y will not necessarily send a request to x.
 *  Also, a person will not send a friend request to themself.
 *
 *  Return the total number of friend requests made.
 *
 *  Example 1:
 *  Input: ages = [16,16]
 *  Output: 2
 *
 *  Example 2:
 *  Input: ages = [16,17,18]
 *  Output: 2  (17 -> 16, 18 -> 17)
 *
 *  Example 3:
 *  Input: ages = [20,30,100,110,120]
 *  Output: 3
 *
 *  Constraints:
 *  n == ages.length
 *  1 <= n <= 2 * 10^4
 *  1 <= ages[i] <= 120
 */
public class FriendsOfAppropriateAges {

    // V0
    // IDEA: COUNTING (age is bounded by 120) + PAIR ENUMERATION
    /**
     * time = O(n + A^2), A = 121 (distinct ages) -> O(n)
     * space = O(A) = O(1)
     */
    public int numFriendRequests(int[] ages) {

        // edge
        if (ages == null || ages.length < 2) {
            return 0;
        }

        int[] cnt = new int[121];
        for (int a : ages) {
            cnt[a]++;
        }

        int res = 0;
        // a = requester age, b = receiver age
        for (int a = 1; a <= 120; a++) {
            if (cnt[a] == 0) {
                continue;
            }
            for (int b = 1; b <= 120; b++) {
                if (cnt[b] == 0) {
                    continue;
                }
                // condition 1 : b <= 0.5 * a + 7  -> use *2 to avoid float
                if (2 * b <= a + 14) {
                    continue;
                }
                // condition 2 : b > a
                if (b > a) {
                    continue;
                }
                // condition 3 : b > 100 && a < 100 (implied by b <= a, kept for clarity)
                if (b > 100 && a < 100) {
                    continue;
                }
                // a person never requests themself
                res += cnt[a] * (cnt[b] - (a == b ? 1 : 0));
            }
        }

        return res;
    }

    // V1
    // IDEA: SORT + SLIDING WINDOW (two pointers over sorted ages)
    /**
     * time = O(n log n)
     * space = O(1) (ignoring sort space)
     */
    public int numFriendRequests_1(int[] ages) {

        if (ages == null || ages.length < 2) {
            return 0;
        }

        java.util.Arrays.sort(ages);

        int res = 0;
        int left = 0;
        int right = 0;
        for (int i = 0; i < ages.length; i++) {
            int a = ages[i];
            // valid receivers b satisfy : 0.5 * a + 7 < b <= a
            // move left to the first index with 2*ages[left] > a + 14
            while (left < ages.length && 2 * ages[left] <= a + 14) {
                left++;
            }
            // move right to the first index with ages[right] > a
            while (right < ages.length && ages[right] <= a) {
                right++;
            }
            int windowSize = right - left; // [left, right)
            if (windowSize > 0 && i >= left && i < right) {
                res += windowSize - 1; // exclude self
            } else if (windowSize > 0) {
                res += windowSize;
            }
        }

        return res;
    }
}
