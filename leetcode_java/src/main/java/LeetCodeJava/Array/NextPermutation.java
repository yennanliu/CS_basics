package LeetCodeJava.Array;

// https://leetcode.com/problems/next-permutation/description/

public class NextPermutation {

    // V0
    // IDEA : 2 POINTERS (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/next-permutation.py#L43
    public void nextPermutation(int[] nums) {

        int i = nums.length - 1;
        int j = nums.length - 1;

        // Find the first decreasing element from the end
        while (i > 0 && nums[i - 1] >= nums[i]) {
            i--;
        }

        // If the entire array is in descending order, reverse it
        if (i == 0) {
            reverse_(nums, 0, nums.length - 1);
            return;
        }

        int k = i - 1; // Find the last "ascending" position

        // Find the element just larger than nums[k]
        while (nums[j] <= nums[k]) {
            j--;
        }

        // Swap the elements at positions k and j
        swap_(nums, k, j);

        // Reverse the sequence from k+1 to the end
        reverse_(nums, k + 1, nums.length - 1);
    }

    // Helper method to swap elements at positions i and j
    private void swap_(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // Helper method to reverse elements from start to end
    private void reverse_(int[] nums, int start, int end) {
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }

    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/next-permutation/solutions/3473399/beats-100-full-explanation-in-steps/
    public void nextPermutation_1(int[] nums) {
        int ind1=-1;
        int ind2=-1;
        // step 1 find breaking point
        for(int i=nums.length-2;i>=0;i--){
            if(nums[i]<nums[i+1]){
                ind1=i;
                break;
            }
        }
        // if there is no breaking  point
        if(ind1==-1){
            reverse(nums,0);
        }

        else{
            // step 2 find next greater element and swap with ind2
            for(int i=nums.length-1;i>=0;i--){
                if(nums[i]>nums[ind1]){
                    ind2=i;
                    break;
                }
            }

            swap(nums,ind1,ind2);
            // step 3 reverse the rest right half
            reverse(nums,ind1+1);
        }
    }
    void swap(int[] nums,int i,int j){
        int temp=nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }
    void reverse(int[] nums,int start){
        int i=start;
        int j=nums.length-1;
        while(i<j){
            swap(nums,i,j);
            i++;
            j--;
        }
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/next-permutation/solutions/5205798/java-solution-0ms-solution-100-faster-solution-full-explanation-code/
    public void nextPermutation_2(int[] nums) {
        // Traverse form the end and find the element which is smaller
        // than its next element. Keep that index as ind

        // Traverse the array from the back and find the element which is
        // nearest element grater that the element present in index ind.
        // Swap them.

        // Reverse the array after the index the index ind

        int ind = -1;

        int n = nums.length;

        for(int i = n - 1; i >= 1; i--) {
            if(nums[i] > nums[i - 1]) {
                ind = i - 1;
                break;
            }
        }

        if(ind == -1) {
            int i = 0, j = n - 1;

            while(i < j) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;

                i++;
                j--;
            }

            return;
        }
        else {
            for(int i = n - 1; i > ind; i--) {
                if(nums[i] > nums[ind]) {
                    int temp = nums[ind];
                    nums[ind] = nums[i];
                    nums[i] = temp;
                    break;
                }
            }
        }

        int i = ind + 1, j = n - 1;

        while(i < j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;

            i++;
            j--;
        }

        return;
    }

}
