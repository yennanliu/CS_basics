package LeetCodeJava.Greedy;

public class JumpGame2 {

    // V0
    // IDEA : GREEDY
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/jump-game-ii.py#L45
    public int jump(int[] nums) {

        if (nums == null || nums.length <= 1){
            return 0;
        }

        int jumps = 0;
        int current_jump_end = 0;
        int fartheset = 0;

        for (int i = 0; i < nums.length - 1; i++){

            fartheset = Math.max(fartheset, i + nums[i]);

            if (i == current_jump_end){
                jumps += 1;
                current_jump_end = fartheset;
            }
        }

        return jumps;
    }

    // V1
    // IDEA : GREEDY
    // https://leetcode.com/problems/jump-game-ii/editorial/
    public int jump_2(int[] nums) {
        // The starting range of the first jump is [0, 0]
        int answer = 0, n = nums.length;
        int curEnd = 0, curFar = 0;

        for (int i = 0; i < n - 1; ++i) {
            // Update the farthest reachable index of this jump.
            curFar = Math.max(curFar, i + nums[i]);

            // If we finish the starting range of this jump,
            // Move on to the starting range of the next jump.
            if (i == curEnd) {
                answer++;
                curEnd = curFar;
            }
        }

        return answer;
    }

}
