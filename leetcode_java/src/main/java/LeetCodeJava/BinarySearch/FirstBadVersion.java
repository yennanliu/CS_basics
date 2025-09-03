package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/first-bad-version/
/**
 *
 * 278. First Bad Version
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * You are a product manager and currently leading a team to develop a new product. Unfortunately, the latest version of your product fails the quality check. Since each version is developed based on the previous version, all the versions after a bad version are also bad.
 *
 * Suppose you have n versions [1, 2, ..., n] and you want to find out the first bad one, which causes all the following ones to be bad.
 *
 * You are given an API bool isBadVersion(version) which returns whether version is bad. Implement a function to find the first bad version. You should minimize the number of calls to the API.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 5, bad = 4
 * Output: 4
 * Explanation:
 * call isBadVersion(3) -> false
 * call isBadVersion(5) -> true
 * call isBadVersion(4) -> true
 * Then 4 is the first bad version.
 * Example 2:
 *
 * Input: n = 1, bad = 1
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= bad <= n <= 231 - 1
 */
public class FirstBadVersion {

    // V0
    // IDEA : BINARY SEARCH ( r >= l )
    public int firstBadVersion(int n) {

        if (n == 1) {
            return 1;
        }

        if (n == 2){
            if (isBadVersion(2)== true && isBadVersion(1) == false){
                return 2;
            }
            return 1;
        }

        // binary search
        int left = 0;
        int right = n;

        while (right >= left) {

            // NOTE !!! we use left + (right-left) / 2; to avoid number overflow
            //  mid = l + (r - l)//2
            int mid = left + (right-left) / 2;
            //System.out.println("left = " + left + ", right = " + right + " mid = " + mid + " isBadVersion(mid) = " + isBadVersion(mid)  + " isBadVersion(mid-1) = " + isBadVersion(mid-1));

            if (isBadVersion(mid) == true) {
                if (isBadVersion(mid - 1) == false) {
                    return mid;
                }

                if (isBadVersion(mid - 1) == true) {
                    right = mid - 1;
                }
            } else {
                left = mid + 1;
            }

        }

        return -1;
    }

    // dummy method avoid func not found alert in IntelliJ
    private Boolean isBadVersion(int n) {
        return true;
    }

    // V0-1
    // IDEA: BINARY SEARCH ( r > l) (gpt)
    public int firstBadVersion_0_1(int n) {
        int l = 1; // versions start at 1
        int r = n;

        while (l < r) {
            int mid = l + (r - l) / 2; // avoid overflow

            if (isBadVersion(mid)) {
                // mid might be the first bad version
                r = mid;
            } else {
                // mid is good, so the first bad version is after mid
                l = mid + 1;
            }
        }

        /**
         *  NOTE !!!!
         *
         *  -> when loop ends, l == r and points to the first bad version
         */
        return l;
    }

    // V0-2
    // IDEA: BINARY SEARCH ( r > l) (fixed by gpt)
    public int firstBadVersion_0_2(int n) {
        // edge
        if (n <= 1) {
            return n;
        }
        int l = 1; // 0;
        int r = n;

        while (r > l) {
            int mid = l + (r - l) / 2;
            // System.out.println(">>> l = " + l + ", r = " + r + " mid = " + mid);
            // [0, mid] are `OK`
            if (!isBadVersion(mid)) {
                if (isBadVersion(mid + 1)) {
                    return mid + 1;
                }
                l = mid + 1;
            }
            // mid or some idx within mid - end are `bad`
            else {
                r = mid; // /??
            }
        }

        return l; //???
    }

    // V1
    // IDEA : Binary Search
    // https://leetcode.com/problems/first-bad-version/editorial/
    public int firstBadVersion_2(int n) {
        int left = 1;
        int right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

}
