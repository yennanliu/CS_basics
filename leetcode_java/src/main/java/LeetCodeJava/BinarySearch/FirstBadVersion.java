package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/first-bad-version/

public class FirstBadVersion {

    // V0
    // IDEA : BINARY SEARCH
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
