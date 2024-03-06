package LeetCodeJava.Sort;

// https://leetcode.com/problems/longest-consecutive-sequence/

import java.util.*;

public class LongestConsecutiveSequence {

    // V0
    // IDEA : SET + SORT + for loop
    // step 1) set : only collect unique element
    // step 2) sort, so element is in ascending ordering
    // step 3) loop over final list, and maintain a max len of sequence
    public int longestConsecutive(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return 0;
        }

        // get set
        Set<Integer> set = new HashSet<>();
        List<Integer> uniqueNums = new ArrayList<>();

        for (int i : nums){
            if (!set.contains(i)){
                set.add(i);
                //cnt += 1;
                uniqueNums.add(i);
            }
        }
        // sort
        uniqueNums.sort((a, b) -> Integer.compare(a, b));
        int ans = 0;
        int tmp = 0;

        // NOTE !!! start from idx = 1
        for (int i = 1; i < uniqueNums.size(); i++){
            // compare with cur, and last element
            // check cur = last + 1 (e.g. consecutive)
            if (uniqueNums.get(i) == uniqueNums.get(i-1) + 1){
                tmp += 1;
                ans = Math.max(ans, tmp);
            }
            else{
                tmp = 0;
            }
        }

        // NOTE : ans + 1, since we start from idx = 1
        return ans+1;
    }

    // V0'
    // IDEA : SET + SORT + SHIFT ARRAY
    // Step 1) we get unique array as new one, since we get possible longest array from it
    // Step 2) we sort unique array from above
    // Step 3) we shift to with 1 index from above unique array as shift array
    // Step 4) we loop over unique array, shift array and calculate max length when diff = 1
    // Step 5) return amx length from above
    public int longestConsecutive_1(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return 0;
        }

        // get set
        Set<Integer> set = new HashSet<>();
        List<Integer> uniqueNums = new ArrayList<>();

        for (int i : nums){
            if (!set.contains(i)){
                set.add(i);
                //cnt += 1;
                uniqueNums.add(i);
            }
        }
        // sort
        uniqueNums.sort((a, b) -> Integer.compare(a, b));
        int ans = 0;
        int tmp = 0;

        // shift to right with 1 idx
        int [] numsShift = new int[uniqueNums.size()+1];
        for (int i = 0; i < uniqueNums.size()-1; i++){
            numsShift[i+1] = uniqueNums.get(i);
        }

        for (int j = 1; j < uniqueNums.size(); j++){
            int diff = uniqueNums.get(j) - numsShift[j];
            if (diff == 1){
                tmp += 1;
                ans = Math.max(ans, tmp);
            }else{
                tmp = 0;
            }
        }

        return ans+1;
    }

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/longest-consecutive-sequence/editorial/
    private boolean arrayContains(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    public int longestConsecutive_2(int[] nums) {
        int longestStreak = 0;

        for (int num : nums) {
            int currentNum = num;
            int currentStreak = 1;

            while (arrayContains(nums, currentNum + 1)) {
                currentNum += 1;
                currentStreak += 1;
            }

            longestStreak = Math.max(longestStreak, currentStreak);
        }

        return longestStreak;
    }

    // V2
    // IDEA : SORTING
    // https://leetcode.com/problems/longest-consecutive-sequence/editorial/
    public int longestConsecutive_3(int[] nums) {

        if (nums.length == 0) {
            return 0;
        }

        Arrays.sort(nums);

        int longestStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i-1]) {
                if (nums[i] == nums[i-1]+1) {
                    currentStreak += 1;
                }
                else {
                    longestStreak = Math.max(longestStreak, currentStreak);
                    currentStreak = 1;
                }
            }
        }

        return Math.max(longestStreak, currentStreak);
    }

}
