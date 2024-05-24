package LeetCodeJava.Greedy;

public class JumpGame {

    // V0
    // IDEA : GREEDY
    // NOTE !!! :  goal is reaching last index
    // Return true if you can reach the last index, or false otherwise.
    /**
     *  example 1:
     *      nums = [2,3,1,1,4]
     *
     * -> log :
     * cur = 0 i = 0
     * cur = 2 i = 1
     * cur = 4 i = 2
     * cur = 4 i = 3
     * cur = 4 i = 4
     *
     *
     *  example 2:
     *      nums = [3,2,1,0,4]
     *
     * -> log:
     *
     * cur = 0 i = 0
     * cur = 3 i = 1
     * cur = 3 i = 2
     * cur = 3 i = 3
     * cur = 3 i = 4
     *
     *
     */

    // (below is wrong)
//    public boolean canJump(int[] nums) {
//
//        if (nums == null || nums.length == 0){
//            return true;
//        }
//
//        int cur = 0;
//        for (int i = 0; i < nums.length; i++){
//            cur = i + nums[i];
//            if (cur > nums.length){
//                return true;
//            }
//            cur = 0;
//        }
//
//        return false;
//    }

        public boolean canJump(int[] nums) {

        if (nums == null || nums.length <= 1){
            return true;
        }

        int cur = 0;
        for (int i = 0; i < nums.length; i++){
            System.out.println("cur = " + cur + " i = " + i);
            // NOTE !!! : if cur < i, means it's NOT possible to visit idx i, so should return false directly
            if (cur < i){
                return false;
            }
            // NOTE !!! : we keep get bigger one from cur, i + nums[i], so we have "MUST" steps can move forward
            //           -> no need to do "cur -= 1", since "i++" when loop goes next iteration, cur will be compared with updated i
            cur = Math.max(cur, i + nums[i]);
        }

        return true;
    }

    // V0'
    // IDEA : GREEDY
    public boolean canJump_(int[] nums) {
        int lastPos = nums.length - 1;
        // NOTE : loop from right to left (<--)
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i + nums[i] >= lastPos) {
                lastPos = i;
            }
        }
        return lastPos == 0;
    }

    // V0''
    // IDEA : GREEDY (modified by GPT)
    public boolean canJump_0(int[] nums) {
        // If there is only one element, we are already at the last index
        if (nums.length == 1) {
            return true;
        }

        int maxReach = 0; // The maximum index we can currently reach

        for (int i = 0; i < nums.length; i++) {
            // If we cannot reach this index, return false
            if (i > maxReach) {
                return false;
            }

            // Update the maximum index we can reach
            maxReach = Math.max(maxReach, i + nums[i]);

            // If we can reach or exceed the last index, return true
            if (maxReach >= nums.length - 1) {
                return true;
            }
        }

        // If we exit the loop without having reached the last index, return false
        return false;
    }


    // V1
    // IDEA : Backtracking
    // https://leetcode.com/problems/jump-game/editorial/
    public boolean canJumpFromPosition(int position, int[] nums) {
        if (position == nums.length - 1) {
            return true;
        }

        int furthestJump = Math.min(position + nums[position], nums.length - 1);
        for (int nextPosition = position + 1; nextPosition <= furthestJump; nextPosition++) {
            if (canJumpFromPosition(nextPosition, nums)) {
                return true;
            }
        }

        return false;
    }

    public boolean canJump_2(int[] nums) {
        return canJumpFromPosition(0, nums);
    }

    // V2
    // IDEA : Dynamic Programming Top-down
    // https://leetcode.com/problems/jump-game/editorial/
    enum Index {
        GOOD, BAD, UNKNOWN
    }
    Index[] memo;

    public boolean canJumpFromPosition_3(int position, int[] nums) {
        if (memo[position] != Index.UNKNOWN) {
            return memo[position] == Index.GOOD ? true : false;
        }

        int furthestJump = Math.min(position + nums[position], nums.length - 1);
        for (int nextPosition = position + 1; nextPosition <= furthestJump; nextPosition++) {
            if (canJumpFromPosition_3(nextPosition, nums)) {
                memo[position] = Index.GOOD;
                return true;
            }
        }

        memo[position] = Index.BAD;
        return false;
    }

    public boolean canJump_3(int[] nums) {
        memo = new Index[nums.length];
        for (int i = 0; i < memo.length; i++) {
            memo[i] = Index.UNKNOWN;
        }
        memo[memo.length - 1] = Index.GOOD;
        return canJumpFromPosition_3(0, nums);
    }


    // V3
    // IDEA :  Dynamic Programming Bottom-up
    // https://leetcode.com/problems/jump-game/editorial/
    public boolean canJump_4(int[] nums) {
        Index[] memo = new Index[nums.length];
        for (int i = 0; i < memo.length; i++) {
            memo[i] = Index.UNKNOWN;
        }
        memo[memo.length - 1] = Index.GOOD;

        for (int i = nums.length - 2; i >= 0; i--) {
            int furthestJump = Math.min(i + nums[i], nums.length - 1);
            for (int j = i + 1; j <= furthestJump; j++) {
                if (memo[j] == Index.GOOD) {
                    memo[i] = Index.GOOD;
                    break;
                }
            }
        }

        return memo[0] == Index.GOOD;
}

    // V4
    // IDEA : GREEDY
    // https://leetcode.com/problems/jump-game/editorial/
    public boolean canJump_5(int[] nums) {
        int lastPos = nums.length - 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i + nums[i] >= lastPos) {
                lastPos = i;
            }
        }
        return lastPos == 0;
    }

}
