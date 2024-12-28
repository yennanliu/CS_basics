package LeetCodeJava.Design;

// https://leetcode.com/problems/design-phone-directory/description/
// https://leetcode.ca/all/379.html

import java.util.*;

/**
 * 379. Design Phone Directory
 * Design a Phone Directory which supports the following operations:
 *
 *
 * get: Provide a number which is not assigned to anyone.
 * check: Check if a number is available or not.
 * release: Recycle or release a number.
 * Example:
 *
 * // Init a phone directory containing a total of 3 numbers: 0, 1, and 2.
 * PhoneDirectory directory = new PhoneDirectory(3);
 *
 * // It can return any available phone number. Here we assume it returns 0.
 * directory.get();
 *
 * // Assume it returns 1.
 * directory.get();
 *
 * // The number 2 is available, so return true.
 * directory.check(2);
 *
 * // It returns 2, the only number that is left.
 * directory.get();
 *
 * // The number 2 is no longer available, so return false.
 * directory.check(2);
 *
 * // Release number 2 back to the pool.
 * directory.release(2);
 *
 * // Number 2 is available again, return true.
 * directory.check(2);
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Dropbox Google Microsoft
 */
public class DesignPhoneDirectory {

  /**
   * Your PhoneDirectory object will be instantiated and called as such:
   * PhoneDirectory obj = new PhoneDirectory(maxNumbers);
   * int param_1 = obj.get();
   * boolean param_2 = obj.check(number);
   * obj.release(number);
   */

  // V0
  // TODO: validate below
  //    class PhoneDirectory {
  //
  //        // attr
  //        //int assignedCnt;
  //        /**
  //         * assignedPhone : {assigned_number: 1}
  //         *
  //         */
  //        Map<Integer, Integer> assignedPhone;
  //        int maxNumbers;
  //        //Random random;
  //
  //        /** Initialize your data structure here
  //         @param maxNumbers - The maximum numbers that can be stored in the phone directory. */
  //        public PhoneDirectory(int maxNumbers) {
  //            //this.assignedCnt = 0;
  //            this.assignedPhone = new HashMap<>();
  //            this.maxNumbers = maxNumbers;
  //            //this.random = new Random();
  //        }
  //
  //        /** Provide a number which is not assigned to anyone.
  //         @return - Return an available number. Return -1 if none is available. */
  //        public int get() {
  //            if(this.assignedPhone.isEmpty()){
  //                return -1;
  //            }
  //            int key = -1;
  //            for (int i = 0; i < this.maxNumbers; i++){
  //                if(!this.assignedPhone.containsKey(i)){
  //                    key = i;
  //                    break;
  //                }
  //            }
  //            if(key != -1){
  //                this.assignedPhone.put(key, 1);
  //            }
  //            return key;
  //        }
  //
  //        /** Check if a number is available or not. */
  //        public boolean check(int number) {
  //            return this.assignedPhone.containsKey(number);
  //        }
  //
  //        /** Recycle or release a number. */
  //        public void release(int number) {
  //            if(this.assignedPhone.containsKey(number)){
  //                this.assignedPhone.remove(number);
  //            }
  //        }
  //    }

  // V0-1
  // IDEA: queue + set (fixed by gpt)
  /**
   * Optimized Solution:
   *
   * To efficiently solve the problem, we can use:
   * 	1.	A queue to store the available numbers.
   * 	2.	A set to quickly check if a number is assigned or not.
   *
   * Here’s a corrected and efficient implementation:
   *
   *
   * Explanation:
   * 	1.	Initialization:
   * 	    •	A Queue<Integer> (availableNumbers) holds all available numbers initially, starting from 0 to maxNumbers - 1.
   * 	    •	A Set<Integer> (assignedNumbers) keeps track of all assigned numbers for efficient lookups.
   *
   * 	2.	get():
   * 	    •	If the queue of available numbers is not empty, it dequeues the next available number, adds it to the set of assigned numbers, and returns it.
   * 	    •	If no numbers are available, it returns -1.
   *
   * 	3.	check(int number):
   * 	    •	Returns true if the number is not in the set of assigned numbers, indicating it is available.
   * 	    •	Handles out-of-bound numbers by returning false.
   *
   * 	4.	release(int number):
   * 	    •	Removes the number from the set of assigned numbers and enqueues it back into the queue of available numbers.
   *
   */
  class PhoneDirectory_0_1 {

        private Queue<Integer> availableNumbers;
        private Set<Integer> assignedNumbers;
        private int maxNumbers;

        /** Initialize your data structure here. */
        public PhoneDirectory_0_1(int maxNumbers) {
            this.maxNumbers = maxNumbers;
            this.availableNumbers = new LinkedList<>();
            this.assignedNumbers = new HashSet<>();

            // Initialize the queue with all numbers
            for (int i = 0; i < maxNumbers; i++) {
                this.availableNumbers.offer(i);
            }
        }

        /** Provide a number which is not assigned to anyone.
         @return - Return an available number. Return -1 if none is available. */
        public int get() {
            if (availableNumbers.isEmpty()) {
                return -1; // No available numbers
            }
            int number = availableNumbers.poll();
            assignedNumbers.add(number);
            return number;
        }

        /** Check if a number is available or not. */
        public boolean check(int number) {
            if (number < 0 || number >= maxNumbers) {
                return false; // Out of bounds
            }
            return !assignedNumbers.contains(number);
        }

        /** Recycle or release a number. */
        public void release(int number) {
            if (assignedNumbers.contains(number)) {
                assignedNumbers.remove(number);
                availableNumbers.offer(number); // Add back to the pool of available numbers
            }
        }
    }

    // V1-1
    // https://leetcode.ca/2016-12-13-379-Design-Phone-Directory/
    class PhoneDirectory_1_1 {
        int maxNumbers;
        boolean[] available;
        Queue<Integer> unused;

        /** Initialize your data structure here
         @param maxNumbers - The maximum numbers that can be stored in the phone directory. */
        public PhoneDirectory_1_1(int maxNumbers) {
            this.maxNumbers = maxNumbers;
            available = new boolean[maxNumbers];
            unused = new LinkedList<Integer>();
            for (int i = 0; i < maxNumbers; i++) {
                available[i] = true;
                unused.offer(i);
            }
        }

        /** Provide a number which is not assigned to anyone.
         @return - Return an available number. Return -1 if none is available. */
        public int get() {
            if (unused.isEmpty())
                return -1;
            else {
                int next = unused.poll();
                available[next] = false;
                return next;
            }
        }

        /** Check if a number is available or not. */
        public boolean check(int number) {
            if (number < 0 || number >= maxNumbers)
                return false;
            else
                return available[number];
        }

        /** Recycle or release a number. */
        public void release(int number) {
            if (!available[number]) {
                unused.offer(number);
                available[number] = true;
            }
        }
    }


    // V1-2
    // https://leetcode.ca/2016-12-13-379-Design-Phone-Directory/
    public class PhoneDirectory_1_2 {

        int max;
        HashSet<Integer> set; // assigned number
        LinkedList<Integer> queue; // available number

        /**
         * Initialize your data structure here
         *
         * @param maxNumbers - The maximum numbers that can be stored in the phone directory.
         */
        public PhoneDirectory_1_2(int maxNumbers) {
            set = new HashSet<Integer>();
            queue = new LinkedList<Integer>();
            for (int i = 0; i < maxNumbers; i++) {
                queue.offer(i);
            }
            max = maxNumbers - 1;
        }

        /**
         * Provide a number which is not assigned to anyone.
         *
         * @return - Return an available number. Return -1 if none is available.
         */
        public int get() {
            if (queue.isEmpty()) {
                return -1;
            }

            int e = queue.poll();
            set.add(e);
            return e;
        }

        /**
         * Check if a number is available or not.
         */
        public boolean check(int number) {
            return !set.contains(number) && number <= max;
        }

        /**
         * Recycle or release a number.
         */
        public void release(int number) {
            if (set.contains(number)) {
                set.remove(number);
                queue.offer(number);
            }
        }
    }

    // V1-3
    // https://leetcode.ca/2016-12-13-379-Design-Phone-Directory/
    class PhoneDirectory_1_3 {

        private boolean[] provided;

        /**
         Initialize your data structure here
         @param maxNumbers - The maximum numbers that can be stored in the phone directory.
         */
        public PhoneDirectory_1_3(int maxNumbers) {
            provided = new boolean[maxNumbers];
        }

        /**
         Provide a number which is not assigned to anyone.
         @return - Return an available number. Return -1 if none is available.
         */
        public int get() {
            for (int i = 0; i < provided.length; ++i) {
                if (!provided[i]) {
                    provided[i] = true;
                    return i;
                }
            }
            return -1;
        }

        /** Check if a number is available or not. */
        public boolean check(int number) {
            return !provided[number];
        }

        /** Recycle or release a number. */
        public void release(int number) {
            provided[number] = false;
        }
    }


    // V2
}
