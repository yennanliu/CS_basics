package LeetCodeJava.Greedy;

// https://leetcode.com/problems/container-with-most-water/

public class ContainerWithMostWater {

    // V0
    // IDEA : BRUTE FORCE
    public int maxArea(int[] height) {

        if (height.length == 0 || height.equals(null)){
            return 0;
        }

        int ans = 0;
        for (int i = 0; i < height.length-1; i++){
            for (int j = 1; j < height.length; j++){
                int amount = (j - i) * Math.min(height[i], height[j]);
                ans = Math.max(amount, ans);
            }
        }

        return ans;
    }

    // V1
    // IDEA : 2 POINTERS
    public int maxArea_2(int[] height) {

        if (height.length == 0 || height.equals(null)){
            return 0;
        }

        int ans = 0;
        int left = 0;
        int right = height.length - 1;
        while (right >= left){
            int leftVal = height[left];
            int rightVal = height[right];
            int amount = (right - left) * Math.min(leftVal, rightVal);
            ans = Math.max(amount, ans);
            if (rightVal > leftVal){
                left += 1;
            }else{
                right -= 1;
            }
        }
        return ans;
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/container-with-most-water/editorial/
    public int maxArea_3(int[] height) {
        int maxarea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int width = right - left;
            maxarea = Math.max(maxarea, Math.min(height[left], height[right]) * width);
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxarea;
    }

}
