package LeetCodeJava.Stack;

// https://leetcode.com/problems/next-greater-element-i/
// https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/Basics/Array1D.java#L14

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class NextGreaterElement_I {

    // V0
    // IDEA : STACK
    // IDEA : implement
    /** NOTE !!!
     *
     *  nums1 is "sub set" of nums2,
     *  so all elements in nums1 are in nums2 as well
     *  and in order to find next greater element in nums1 reference nums2
     *  -> ACTUALLY we only need to check nums2
     *  -> then append result per element in nums1
     */

    // V0'
    // IDEA : STACK
    // https://www.youtube.com/watch?v=68a1Dc_qVq4
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0496-next-greater-element-i.java
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {

        int[] res = new int[nums1.length];
        int counter=0;

        for(int i: nums1){
            res[counter++]=ans(i, nums2);
        }

        return res;
    }

    private int ans(int i, int[] nums){
        for(int n=0; n<nums.length; n++){
            if(nums[n]==i){
                for(int j=n+1; j<nums.length; j++){
                    if(nums[j]>i)
                        return nums[j];
                }
            }
        }
        return -1;
    }

    // V0'
    public int[] nextGreaterElement_0_1(int[] nums1, int[] nums2) {

        if (nums1.equals(null) && nums2.equals(null)) {
            return nums1;
        }

        HashMap<Integer, Integer> nums2Map = new HashMap<>();
        for (int j = 0; j < nums2.length; j++) {
            int cur = nums2[j];
            nums2Map.put(cur, j);
        }
        //System.out.println(">>> nums2Map = " + nums2Map.toString());
        int[] ans = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            int nums1Val = nums1[i];
            if (!nums2Map.containsKey(nums1Val)) {
                ans[i] = -1;
            } else {
                int idx = nums2Map.get(nums1Val) + 1;
                int res = -1;
                Boolean found = false;
                while (idx < nums2.length) {
                    int cur = nums2[idx];
                    if (cur > nums1Val) {
                        ans[i] = cur;
                        found = true;
                        break;
                    }
                    idx += 1;
                }
                if (!found) {
                    ans[i] = res;
                }
            }
        }
        return ans;
    }

    // V1
    // IDEA : STACK
    // https://leetcode.com/problems/next-greater-element-i/solutions/2910456/java/
    // https://leetcode.com/problems/next-greater-element-i/solutions/97595/java-10-lines-linear-time-complexity-o-n-with-explanation/?orderBy=most_votes
    public int[] nextGreaterElement_1(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreater = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();

        // NOTE below!!!
        for (int n : nums2) {
            while (!stack.isEmpty() && stack.peek() < n) {
                nextGreater.put(stack.pop(), n);
            }
            stack.push(n);
        }

        int[] answer = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            answer[i] = nextGreater.getOrDefault(nums1[i], -1);
        }

        return answer;
    }

    // V2
    // https://leetcode.com/problems/next-greater-element-i/solutions/2614962/easy-java-solution/
    public int[] nextGreaterElement_2(int[] nums1, int[] nums2) {
        int[] ans = new int[nums1.length];
        for(int a = 0; a<nums1.length;a++)
        {
            ans[a] = findGreater(nums1[a], nums2);
        }

        return ans;
    }

    private int findGreater(int target, int[] nums)
    {
        int index=0;
        for(int i = 0;i<nums.length;i++)
            if(nums[i] == target)
                index = i;
        for(int i = index+1;i<nums.length;i++)
            if(nums[i]>target)
                return nums[i];
        return -1;
    }

}
