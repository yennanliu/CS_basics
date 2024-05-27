package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/description/

public class RemoveDuplicatesFromSortedArray2 {

    // V0
    // IDEA : 2 POINTERS ( gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/remove-duplicates-from-sorted-array-ii.py
    public int removeDuplicates(int[] nums) {
        if (nums.length < 3) {
            return nums.length;
        }

        int slow = 1; // slow starts from 1
        for (int fast = 2; fast < nums.length; fast++) { // fast starts from 2
            // If the condition is met, update the array and increment slow pointer
            if (nums[slow] != nums[fast] || nums[slow] != nums[slow - 1]) {
                nums[++slow] = nums[fast];
            }
        }
        return slow + 1; // Return the length of the modified array
    }

    // V1
    // IDEA : 2 POINTERS
    // The approach employs a two-pointer strategy. The variable j is used to keep track of the current position in the modified array where elements are being stored without violating the constraint. The loop iterates through the array, and for each element, it checks whether it is the same as the element two positions behind the current j. If it is, it means there are already two occurrences of this element in the modified array, and we should skip adding another one to adhere to the constraint. Otherwise, the element is added to the modified array at position j, and j is incremented.
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/solutions/4804983/beats-100-0ms-advanced-two-pointer-approach-java-c-python-rust/
    public int removeDuplicates_1(int[] nums) {
        int j = 1;
        for (int i = 1; i < nums.length; i++) {
            if (j == 1 || nums[i] != nums[j - 2]) {
                nums[j++] = nums[i];
            }
        }
        return j;
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/solutions/4511964/easy-o-n-python-java-go-c-beginner-friendly/
    public int removeDuplicates_2(int[] nums) {

        int index = 1;
        int occurance = 1;

        for(int i=1; i < nums.length; i++){
            if (nums[i] == nums[i-1]){
                occurance++;
            }else{
                occurance = 1;
            }

            if (occurance <= 2){
                nums[index] = nums[i];
                index++;
            }
        }
        return index;
    }

}
