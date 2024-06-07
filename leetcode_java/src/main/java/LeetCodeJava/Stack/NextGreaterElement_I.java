package LeetCodeJava.Stack;

// https://leetcode.com/problems/next-greater-element-i/
// https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/Basics/Array1D.java#L14

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class NextGreaterElement_I {

    // V0
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {

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
