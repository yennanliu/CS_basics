package LeetCodeJava.Greedy;

// https://leetcode.com/problems/count-tested-devices-after-test-operations/description/
/**
 * 2960. Count Tested Devices After Test Operations
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array batteryPercentages having length n, denoting the battery percentages of n 0-indexed devices.
 *
 * Your task is to test each device i in order from 0 to n - 1, by performing the following test operations:
 *
 * If batteryPercentages[i] is greater than 0:
 * Increment the count of tested devices.
 * Decrease the battery percentage of all devices with indices j in the range [i + 1, n - 1] by 1, ensuring their battery percentage never goes below 0, i.e, batteryPercentages[j] = max(0, batteryPercentages[j] - 1).
 * Move to the next device.
 * Otherwise, move to the next device without performing any test.
 * Return an integer denoting the number of devices that will be tested after performing the test operations in order.
 *
 *
 *
 * Example 1:
 *
 * Input: batteryPercentages = [1,1,2,1,3]
 * Output: 3
 * Explanation: Performing the test operations in order starting from device 0:
 * At device 0, batteryPercentages[0] > 0, so there is now 1 tested device, and batteryPercentages becomes [1,0,1,0,2].
 * At device 1, batteryPercentages[1] == 0, so we move to the next device without testing.
 * At device 2, batteryPercentages[2] > 0, so there are now 2 tested devices, and batteryPercentages becomes [1,0,1,0,1].
 * At device 3, batteryPercentages[3] == 0, so we move to the next device without testing.
 * At device 4, batteryPercentages[4] > 0, so there are now 3 tested devices, and batteryPercentages stays the same.
 * So, the answer is 3.
 * Example 2:
 *
 * Input: batteryPercentages = [0,1,2]
 * Output: 2
 * Explanation: Performing the test operations in order starting from device 0:
 * At device 0, batteryPercentages[0] == 0, so we move to the next device without testing.
 * At device 1, batteryPercentages[1] > 0, so there is now 1 tested device, and batteryPercentages becomes [0,1,1].
 * At device 2, batteryPercentages[2] > 0, so there are now 2 tested devices, and batteryPercentages stays the same.
 * So, the answer is 2.
 *
 *
 * Constraints:
 *
 * 1 <= n == batteryPercentages.length <= 100
 * 0 <= batteryPercentages[i] <= 100
 *
 */
public class CountTestedDevicesAfterTestOperations {

    // V0
//    public int countTestedDevices(int[] batteryPercentages) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (fixed by gemini)
    /**
     * Counts the number of devices tested based on the sequential testing rules.
     * * @param batteryPercentages Array of device battery percentages.
     * @return The total count of tested devices.
     */
    public int countTestedDevices_0_1(int[] batteryPercentages) {
        int testedCount = 0;

        // Edge cases are implicitly handled by the loop and the initial check.
        if (batteryPercentages == null || batteryPercentages.length == 0) {
            return 0;
        }

        // Iterate through each device starting from the first one (index i=0).
        for (int i = 0; i < batteryPercentages.length; i++) {

            // The condition to test device 'i' is based on its *current* effective percentage.
            // The current effective percentage is the original percentage minus the total number
            // of devices tested *before* index i.

            int effectivePercentage = batteryPercentages[i] - testedCount;

            // Op Trigger Condition: Check if the effective battery percentage is > 0.
            if (effectivePercentage > 0) {

                // Op 1: Test the device and increment the count.
                testedCount++;

                // Op 2: In the original problem, testing device 'i' reduces the battery
                // of ALL subsequent devices (j > i) by 1.
                //
                // By incrementing 'testedCount' here, we are implicitly and correctly applying
                // this reduction to all future devices (j > i).
                //
                // We do NOT need the inner loop (for j = i+1; ...) that was in the original code,
                // as that leads to O(N^2) complexity and complex in-place modification.
                // This O(N) approach is more efficient.
            }
        }

        return testedCount;
    }

    // V0-2
    public int countTestedDevices_0_2(int[] batteryPercentages) {
        int tested = 0;

        // Each tested device reduces all future device battery by 1
        for (int i = 0; i < batteryPercentages.length; i++) {
            if (batteryPercentages[i] - tested > 0) {
                tested++;
            }
        }

        return tested;
    }


    // V1
    // https://leetcode.com/problems/count-tested-devices-after-test-operations/solutions/4384489/javacpython-easy-and-concise-by-lee215-7bx1/
    public int countTestedDevices_1(int[] A) {
        int k = 0;
        for (int a : A)
            k += a > k ?  1 : 0;
        return k;
    }

    // V2
    // https://leetcode.com/problems/count-tested-devices-after-test-operations/solutions/7220816/simple-approach-by-i_am_groot_2k-ba0y/
    public int countTestedDevices_2(int[] batteryPercentages) {
        int ans = 0;
        int decrease = 0;
        for (int i = 0; i < batteryPercentages.length; i++) {
            if (batteryPercentages[i] + decrease > 0) {
                ans++;
                decrease--;
            }
        }
        return ans;
    }

    // V3
    public int countTestedDevices_3(int[] batteryPercentages) {
        int n = batteryPercentages.length;
        int testedDevices = 0;

        for (int i = 0; i < n; ++i) {
            if (batteryPercentages[i] > 0) {
                ++testedDevices;

                for (int j = i + 1; j < n; ++j) {
                    batteryPercentages[j] = Math.max(0, batteryPercentages[j] - 1);
                }
            }
        }

        return testedDevices;
    }

    // V4


}
