package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-duplicates-from-sorted-array/

import java.util.LinkedHashSet;

public class RemoveDuplicatesFromSortedArray {

    // V0

    // V1
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3416595/c-java-python-javascript-fully-explained-two-pointer/
    public int removeDuplicates(int[] nums) {
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[i] != nums[j]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    // V2
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3342081/100-faster-java-2-approaches-step-by-step-explained/
    public int removeDuplicates_2(int[] nums) {

        int newIndex = 1; // Start with index 1 because the first element is already in place

        for (int i = 0; i < nums.length - 1; i++) {

            if (nums[i] < nums[i + 1]) { // If the current element is less than the next element

                nums[newIndex] = nums[i + 1]; // Move the next element to the new index
                newIndex++; // Increment the new index
            }
        }
        return newIndex; // Return the length of the new subarray
    }

    // V2'
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3342081/100-faster-java-2-approaches-step-by-step-explained/
    public int removeDuplicates_3(int[] nums) {

        //Insert all array element in the Set.
        //Set does not allow duplicates and sets like LinkedHashSet maintains the order of insertion so it will remove duplicates and elements will be printed in the same order in which it is inserted

        LinkedHashSet<Integer> set = new LinkedHashSet<>();

        for(int i = 0; i < nums.length; i++){
            set.add(nums[i]);
        }
        //copy unique element back to array
        int i = 0;

        for(int ele:set){
            nums[i++] = ele;
        }
        return set.size();
    }


}
