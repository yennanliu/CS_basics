package LeetCodeJava.Design;

// https://leetcode.com/problems/design-parking-system/

/**
 *  1603. Design Parking System
 *  Easy
 *
 *  Design a parking system for a parking lot. The parking lot has three kinds of parking
 *  spaces: big, medium, and small, with a fixed number of slots for each size.
 *
 *  Implement the ParkingSystem class:
 *
 *   - ParkingSystem(int big, int medium, int small) Initializes the object. The number of
 *     slots for each parking space are given as part of the constructor.
 *   - boolean addCar(int carType) Checks whether there is a parking space of carType.
 *     carType is 1 (big), 2 (medium) or 3 (small). A car can only park in a space of its
 *     own carType. If there is no space available return false, else park the car and
 *     return true.
 *
 *  Example 1:
 *
 *  Input
 *  ["ParkingSystem", "addCar", "addCar", "addCar", "addCar"]
 *  [[1, 1, 0], [1], [2], [3], [1]]
 *  Output
 *  [null, true, true, false, false]
 *
 *  Constraints:
 *
 *   0 <= big, medium, small <= 1000
 *   carType is 1, 2, or 3
 *   At most 1000 calls will be made to addCar
 */
public class DesignParkingSystem {

    // V0
    // IDEA: COUNTER ARRAY indexed by (carType - 1)
    /**
     * time = O(1) per addCar
     * space = O(1)
     */
    private final int[] slots;

    public DesignParkingSystem(int big, int medium, int small) {
        this.slots = new int[]{big, medium, small};
    }

    public boolean addCar(int carType) {
        int idx = carType - 1;
        if (slots[idx] > 0) {
            slots[idx]--;
            return true;
        }
        return false;
    }
}
