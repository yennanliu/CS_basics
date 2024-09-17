package LeetCodeJava.Greedy;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 659. Split Array into Consecutive Subsequences
 * Medium
 * Topics
 * Companies
 * You are given an integer array nums that is sorted in non-decreasing order.
 *
 * Determine if it is possible to split nums into one or more subsequences such that both of the following conditions are true:
 *
 * Each subsequence is a consecutive increasing sequence (i.e. each integer is exactly one more than the previous integer).
 * All subsequences have a length of 3 or more.
 * Return true if you can split nums according to the above conditions, or false otherwise.
 *
 * A subsequence of an array is a new array that is formed from the original array by deleting some (can be none) of the elements without disturbing the relative positions of the remaining elements. (i.e., [1,3,5] is a subsequence of [1,2,3,4,5] while [1,3,2] is not).
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,3,4,5]
 * Output: true
 * Explanation: nums can be split into the following subsequences:
 * [1,2,3,3,4,5] --> 1, 2, 3
 * [1,2,3,3,4,5] --> 3, 4, 5
 * Example 2:
 *
 * Input: nums = [1,2,3,3,4,4,5,5]
 * Output: true
 * Explanation: nums can be split into the following subsequences:
 * [1,2,3,3,4,4,5,5] --> 1, 2, 3, 4, 5
 * [1,2,3,3,4,4,5,5] --> 3, 4, 5
 * Example 3:
 *
 * Input: nums = [1,2,3,4,4,5]
 * Output: false
 * Explanation: It is impossible to split nums into consecutive increasing subsequences of length 3 or more.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -1000 <= nums[i] <= 1000
 * nums is sorted in non-decreasing order.
 *
 */
// https://leetcode.com/problems/split-array-into-consecutive-subsequences/description/

public class SplitArrayIntoConsecutiveSubsequences {

    // V0
    // TODO : implement
//    public boolean isPossible(int[] nums) {
//
//    }

    // V1
    // IDEA : MAP
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/solutions/2447905/two-maps-clean/
    public boolean isPossible_1(int[] nums) {
        Map<Integer, Integer> notPlacedCount = new HashMap<>();   // entry = {num, count of unplaced nums}
        Map<Integer, Integer> sequenceEndCount = new HashMap<>(); // entry = {num, count of sequences ending at num}

        for (int num : nums) increaseCount(notPlacedCount, num);

        for (int num : nums) {

            // NOTE !!! below conditions
            boolean alreadyContains = notPlacedCount.get(num) == 0;
            boolean canAddToExisting = sequenceEndCount.getOrDefault(num - 1, 0) > 0;
            boolean canAddNewSequence = notPlacedCount.getOrDefault(num + 1, 0) > 0 && notPlacedCount.getOrDefault(num + 2, 0) > 0;

            if (alreadyContains)
                continue;

            if (canAddToExisting) {
                decreaseCount(notPlacedCount, num);
                decreaseCount(sequenceEndCount, num - 1);
                increaseCount(sequenceEndCount, num);
            }

            else if (canAddNewSequence) {
                decreaseCount(notPlacedCount, num);
                decreaseCount(notPlacedCount, num + 1);
                decreaseCount(notPlacedCount, num + 2);
                increaseCount(sequenceEndCount, num + 2);
            }

            else
                return false;
        }
        return true;
    }

    private void increaseCount(Map<Integer, Integer> countMap, int num) {
        countMap.put(num, countMap.getOrDefault(num, 0) + 1);
    }

    private void decreaseCount(Map<Integer, Integer> countMap, int num) {
        countMap.put(num, countMap.get(num) - 1);
    }


    // V 1_1
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/solutions/2447452/java-greedy-just-a-few-lines-explained/
    public boolean isPossible_1_1(int[] nums) {
        int[] count = new int[2003];
        int[] end = new int[2003];
        for (int n : nums){
            count[n+1000]++;
        }
        for (int i = 0; i <= 2002; i++){
            if (count[i] < 0){ // can't be less than 0, return false
                return false;
            }else if (i <= 2000){
                int cont = Math.min(count[i], i==0?0:end[i-1]); // extend the subsequences
                count[i] -= cont;
                end[i] += cont;
                count[i+1] -= count[i]; // start some new subsequences and we must take those
                count[i+2] -= count[i]; // take those
                end[i+2] += count[i];   // update end to include the new subsequences.
            }
        }
        return true;
    }


    // V2
    // IDEA : GREEDY
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/solutions/2447452/java-greedy-just-a-few-lines-explained/
    public boolean isPossible_2(int[] nums) {
        // greedy algorithm
        if (nums == null || nums.length < 3)
            return false;

        // map to save the frequency of each number
        Map<Integer, Integer> freq = new HashMap<>();

        // map to save that the number of subsequences that are
        // ended with number i
        Map<Integer, Integer> tail = new HashMap<>();

        // first pass, fill teh freq map
        for (int i : nums) {
            freq.put(i, freq.getOrDefault(i, 0) + 1);
        }

        // second pass:
        for (int i : nums) {
            // there is no such number available
            if (freq.get(i) == 0) {
                continue;
            }
            // if there is some subsequence that is ended with i - 1:
            // then we can put the number i in the subsequence
            if (tail.get(i-1) != null && tail.get(i-1) > 0) {
                // the number of sequence ended with i-1 decreases
                tail.put(i-1, tail.get(i-1) - 1);
                // the number of sequences ended with i increases
                tail.put(i, tail.getOrDefault(i, 0) + 1);
                // we used one number i, decrease the freq
                freq.put(i, freq.get(i) - 1);
            } else {
                // there is no such subsequence that is ended with i-1
                // we build a new subsequence start with i,
                // we then need i + 1 and i + 2 to make a valid subsequence
                if (freq.get(i+1) != null && freq.get(i+1) > 0 &&
                        freq.get(i+2) != null && freq.get(i+2) > 0) {
                    // if we have available i + 1 and i + 2
                    // we now have one more subsequence ended with i+2
                    tail.put(i+2, tail.getOrDefault(i+2, 0) + 1);
                    // decrease the frequency
                    freq.put(i, freq.get(i) - 1);
                    freq.put(i + 1, freq.get(i + 1) - 1);
                    freq.put(i + 2, freq.get(i + 2) - 1);
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    // V3
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/solutions/844738/java-very-easy-explanation-through-a-story-time-o-n-space-o-n/
    public boolean isPossible_3(int[] nums) {
        // This hashmap tells us about whether a number in num is available for a job or not
        HashMap<Integer,Integer> avaibilityMap = new HashMap<>();

        // This hashmap tells a number (say x), if there is a job vacancy for them
        HashMap<Integer,Integer> wantMap = new HashMap<>();

        // We store the count of every num in nums into avaibilityMap. Basically, a number's count is the avaibility of it.
        for(int i : nums){
            avaibilityMap.put(i, avaibilityMap.getOrDefault(i,0)+1);
        }

        // We iterate through each number in the nums array. Remember the story ? So, treat them like a person.
        for(int i=0;i<nums.length;i++){
            // First we check if our current num/person is available. If it is not we just continue with next num/person
            if(avaibilityMap.get(nums[i])<=0){
                continue;
            }

            // If the person is available, first we check if there is a job vacancy for him/her. Basically, is someone looking for him/her?
            else if(wantMap.getOrDefault(nums[i],0)>0){
                // Yes, someone is looking, so we decrease the avaibility count of that number
                avaibilityMap.put(nums[i], avaibilityMap.getOrDefault(nums[i],0)-1);

                // we also decrease its count from the job vacancy space / wantMap
                wantMap.put(nums[i], wantMap.getOrDefault(nums[i],0)-1);

                // Then as a goodwill, he/she will also create a job vacancy for (num[i]+1) in job vacancy space / wantMap, as we need consecutive numbers only
                wantMap.put(nums[i]+1, wantMap.getOrDefault(nums[i]+1,0)+1);
            }

            // Ooh, we are here means nums[i] was not able to find a job.
            // so, nums[i] tries to start his/her own company by checking avaibility of his/her friends i.e. (nums[i]+1) and (nums[i]+2)
            else if(avaibilityMap.getOrDefault(nums[i],0)>0 && avaibilityMap.getOrDefault(nums[i]+1,0)>0 && avaibilityMap.getOrDefault(nums[i]+2,0)>0){

                // Yay! both 2 friends are available. Let's start a company.
                // So we decrease the avaibility count of nums[i], (nums[i]+1) and (nums[i]+2) from the
                // avaibilityMap
                avaibilityMap.put(nums[i], avaibilityMap.getOrDefault(nums[i],0)-1);
                avaibilityMap.put(nums[i]+1, avaibilityMap.getOrDefault(nums[i]+1,0)-1);
                avaibilityMap.put(nums[i]+2, avaibilityMap.getOrDefault(nums[i]+2,0)-1);

                // Also, as a goodwill, we create a new job vacancy for (nums[i]+3), as we need consecutive numbers only
                wantMap.put(nums[i]+3, wantMap.getOrDefault(nums[i]+3,0)+1);
            }

            // Bad luck case, nums[i] not able to start his/her company, so just return false
            else{
                return false;
            }
        }

        // All good till here so we return true
        return true;
    }

    // V4
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/solutions/2448247/short-c-java-python-explained-solution-beginner-friendly-by-mr-coder/
    public boolean isPossible_4(int[] nums) {
        Map<Integer,Integer> availability = new HashMap<>();
        Map<Integer,Integer> possibility = new HashMap<>();
        for(int num:nums){
            availability.put(num,availability.getOrDefault(num,0)+1);
        }
        for(int num:nums){
            if(availability.get(num)==0)continue;
            if(possibility.getOrDefault(num,0)>0){
                possibility.put(num,possibility.getOrDefault(num,0)-1);
                possibility.put(num+1,possibility.getOrDefault(num+1,0)+1);
            }
            else if(availability.getOrDefault(num+1,0)>0 && availability.getOrDefault(num+2,0)>0 ){
                possibility.put(num+3,possibility.getOrDefault(num+3,0)+1);
                availability.put(num+1,availability.getOrDefault(num+1,0)-1);
                availability.put(num+2,availability.getOrDefault(num+2,0)-1);
            }
            else{
                return false;
            }
            availability.put(num,availability.get(num)-1);
        }
        return true;
    }

    // V5
    // IDEA: DP + GREEDY + PQ
    // https://leetcode.com/problems/split-array-into-consecutive-subsequences/submissions/1385341810/
    public boolean isPossible_5(int[] nums) {
        /**
         *  NOTE !!!
         *
         *  Map<Integer, PriorityQueue<Integer>> lastElements, is a map
         *
         *  - where the key is an integer representing the "last element" of a subsequence
         *  - the value is a PriorityQueue<Integer> containing the "lengths of subsequences ending with that key".
         *
         *
         *  -> so,
         *  ->   key is the "last element" of subsequences
         *  ->   value is the length of subsequences end with that key
         */
        Map<Integer, PriorityQueue<Integer>>lastElements = new HashMap<>();
        // The loop processes each element in the nums array to update the lastElements map.
        for (int element: nums){
            /**
             *  NOTE !!!
             *
             *  For each element in nums,
             *   it checks if there is a subsequence that ends with element-1
             *   (i.e., a subsequence that can be extended by the current element).
             * 	   - If such a subsequence is found (i.e., lastElements.containsKey(element - 1)), the length of the smallest subsequence (obtained using poll()) is used. This subsequence length is then incremented by 1, reflecting that the subsequence is extended to include the current element.
             * 	   - If no subsequence ends with element-1, subseqCount remains 0, indicating that the current element starts a new subsequence.
             *
             */
            int subseqCount = 0;
            if (lastElements.containsKey(element-1)){
                subseqCount = lastElements.get(element-1).poll();
                if (lastElements.get(element-1).isEmpty()) lastElements.remove(element-1);
            }
            // The lastElements map is updated to include or update the entry for the current element,
            // Adding a new subsequence of length subseqCount + 1.
            lastElements.putIfAbsent(element, new PriorityQueue<>());
            /**
             *
             *
             * - subseqCount is the length of the subsequence that was just extended to include the current element.
             * - subseqCount + 1 indicates that the current subsequence is being extended by 1, because the element has been added to the subsequence.
             * - add() adds this updated subsequence length
             *    (i.e., subseqCount + 1) to the PriorityQueue associated with the element.
             */
            lastElements.get(element).add(subseqCount+1);
        }
        for (Map.Entry<Integer,PriorityQueue<Integer>>entry: lastElements.entrySet()){
            /**
             * - After processing all elements, the code iterates over the lastElements map to ensure
             *    all subsequences have a length of at least 3.
             *
             * 	- For each entry in the map, the PriorityQueue is checked,
             * 	  and if any subsequence length is less than 3, the method returns false.
             */
            while (!entry.getValue().isEmpty()){
                if (entry.getValue().poll()<3){
                    return false;
                }
            }
        }
        return true;
    }

}
