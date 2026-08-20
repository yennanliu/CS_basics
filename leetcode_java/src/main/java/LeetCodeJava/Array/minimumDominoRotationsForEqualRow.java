package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-domino-rotations-for-equal-row/description/

public class minimumDominoRotationsForEqualRow {

    // V0
    // IDEA: GREEDY (only tops[0] or bottoms[0] can be the `same value`)
    /**
     *  KEY IDEA:
     *
     *   if a row can be made `all the same value` x,
     *   then domino-0 must already have x on one of its 2 sides
     *
     *   -> so there are ONLY 2 candidates : tops[0] and bottoms[0]
     *   -> for each candidate, count how many rotations are needed
     *      to make the `top row` all x, and to make the `bottom row` all x
     *      (if some domino has NO x on either side -> candidate is impossible)
     *
     *
     *  time = O(N)
     *  space = O(1)
     */
    public int minDominoRotations(int[] tops, int[] bottoms) {
        // edge
        if (tops == null || bottoms == null || tops.length == 0
                || tops.length != bottoms.length) {
            return -1;
        }

        /** NOTE !!! ONLY 2 possible candidates */
        int res = getRotations(tops, bottoms, tops[0]);
        if (tops[0] != bottoms[0]) {
            int res2 = getRotations(tops, bottoms, bottoms[0]);
            if (res == -1) {
                res = res2;
            } else if (res2 != -1) {
                res = Math.min(res, res2);
            }
        }

        return res;
    }

    // get min rotations so that `top row` or `bottom row` is all val (-1 if impossible)
    private int getRotations(int[] tops, int[] bottoms, int val) {
        int topSwap = 0;
        int bottomSwap = 0;

        for (int i = 0; i < tops.length; i++) {
            /** NOTE !!! if val is on NEITHER side -> impossible */
            if (tops[i] != val && bottoms[i] != val) {
                return -1;
            }
            // need a rotation to bring val to the top
            if (tops[i] != val) {
                topSwap++;
            }
            // need a rotation to bring val to the bottom
            if (bottoms[i] != val) {
                bottomSwap++;
            }
        }

        return Math.min(topSwap, bottomSwap);
    }

    // V1
    // IDEA : ARRAY
    // https://leetcode.com/problems/minimum-domino-rotations-for-equal-row/solutions/376306/java-easy-and-intuitive-solution/
    /**
     * Using 3 arrays
     * we have 1, 2, 3, 4, 5, 6 index
     * countSum is total number of occurrences with each index in either A or B
     *
     * countA is total number of occurrences with each index only in A but not in B
     * countB is total number of occurrences with each index only in B but not in A
     *
     * -> Only if countSum[i] = A.length will the problem have a solution
     *
     * -> return Math.min(countA, countB) since no need for swap when A[i] = B[i]
     *
     */
    public int minDominoRotations_1(int[] A, int[] B) {
        int len  = A.length;
        int[] countSum = new int[7], countA = new int[7], countB = new int[7];

        for(int i = 0 ; i < len; i++) {
            if(A[i] == B[i])
                countSum[A[i]]++;
            else {
                countSum[A[i]]++;
                countSum[B[i]]++;
                countA[A[i]]++;
                countB[B[i]]++;
            }
        }

        for(int i = 1; i <= 6; i++) {
            if(countSum[i] == len)
                return Math.min(countA[i], countB[i]);
        }

        return -1;
    }

    // V2
    // IDEA : DP
    // https://leetcode.com/problems/minimum-domino-rotations-for-equal-row/submissions/1309255149/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minDominoRotations_2(int[] tops, int[] bottoms) {
        int len = tops.length;
        int[][] dp = new int[6][3];
        for(int i=0; i<len; i++){
            if(tops[i]==bottoms[i]){
                dp[tops[i]-1][0]++;
                dp[tops[i]-1][1]++;
                dp[tops[i]-1][2]++;
            }else{
                dp[tops[i]-1][0]++;
                dp[tops[i]-1][1]++;
                dp[bottoms[i]-1][0]++;
                dp[bottoms[i]-1][2]++;
            }
        }
        int min = Integer.MAX_VALUE;
        for(int i=0; i<6; i++){
            if(dp[i][0]==len){
                min = Math.min(min, Math.min(len-dp[i][1], len-dp[i][2]));
            }
        }
        if(min<len)
            return min;
        return -1;
    }


    // V3
    // https://leetcode.com/problems/minimum-domino-rotations-for-equal-row/solutions/1866884/simple-and-easy-to-understand-t-c-o-n-s-c-o-1-java/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minDominoRotations_3(int[] top, int[] bottom) {
        int n = top.length;
        int a = top[0], b = bottom[0];
        for(int i = 1 ; i < n ; i++) {
            if(a != top[i] && a != bottom[i])
                a = -1;
            if(b != top[i] && b != bottom[i])
                b = -1;
            if(a == -1 && b == -1)
                return -1;
        }

        a = a != -1 ? a : b;
        int tswap = 0, bswap = 0;
        for(int i = 0 ; i < n ; i++) {
            if(a != top[i])
                tswap++;

            if(a != bottom[i])
                bswap++;
        }
        return Math.min(tswap, bswap);
    }

}
