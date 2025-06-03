package LeetCodeJava.Array;

// https://leetcode.com/problems/put-boxes-into-the-warehouse-i/
// https://leetcode.ca/all/1564.html

import java.util.*;

/**
 * 1564. Put Boxes Into the Warehouse I
 * Given two arrays of positive integers boxes and warehouse representing the heights of some boxes of unit width, and the heights of n rooms in a warehouse, respectively. The warehouse's rooms are labeled from 0 to n - 1 from left to right where warehouse[i] (0-indexed) is the height of the ith room.
 *
 * Boxes are put into the warehouse by the following rules:
 *
 * Boxes can't be piled up.
 * You can rearrange the order of the boxes.
 * Boxes can only be pushed into the warehouse from left to right only.
 * If the height of some room in the warehouse is less than the height of a box, then the box will be stopped before that room, so are the boxes behind it.
 * Return the maximum number of boxes you can put into the warehouse.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: boxes = [4,3,4,1], warehouse = [5,3,3,4,1]
 * Output: 3
 * Explanation:
 *
 * We can first put the box of height 1 in room 4. Then we can put the box of height 3 in either of the 3 rooms 1, 2, or 3. Lastly, we can put one box of height 4 in room 0.
 * There is no way we can fit all 4 boxes in the warehouse.
 * Example 2:
 *
 *
 *
 * Input: boxes = [1,2,2,3,4], warehouse = [3,4,1,2]
 * Output: 3
 * Explanation:
 *
 * Notice that it's not possible to put the box of height 4 into the warehouse since it cannot pass the first room of height 3.
 * Also, for the last two rooms, 2 and 3, only boxes of height 1 can fit.
 * We can fit 3 boxes maximum as shown above. The yellow box can also be put in room 2 instead.
 * Swapping the orange and green boxes is also valid, or swapping one of them with the red box.
 * Example 3:
 *
 * Input: boxes = [1,2,3], warehouse = [1,2,3,4]
 * Output: 1
 * Explanation: Since the first room in the warehouse is of height 1, we can only put boxes of height 1.
 * Example 4:
 *
 * Input: boxes = [4,5,6], warehouse = [3,3,3,3,3]
 * Output: 0
 *
 *
 * Constraints:
 *
 * n == warehouse.length
 * 1 <= boxes.length, warehouse.length <= 10^5
 * 1 <= boxes[i], warehouse[i] <= 10^9
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 *
 *
 */
public class PutBoxesIntoTheWarehouseI {

    // V0
//    public int maxBoxesInWarehouse(int[] boxes, int[] warehouse) {
//
//    }

    // V0-1
    // IDEA: STACK, PQ
    // TODO: validate below:
//    public int maxBoxesInWarehouse_0_1(int[] boxes, int[] warehouse) {
//        // edge
//        if(boxes == null || warehouse == null){
//            return 0;
//        }
//
//        // stack (FILO)
//        Stack<Integer> st = new Stack<>();
//        for(int x: warehouse){
//            st.add(x);
//        }
//
//        // small PQ
//        PriorityQueue<Integer> b_pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o1 - o2;
//                return diff;
//            }
//        });
//
//        for(int x: boxes){
//            b_pq.add(x);
//        }
//
//        List<Integer> collected = new ArrayList<>();
//
//        // try to `move box into warehouse`
//        while(!st.isEmpty()){
//            // leave earlier
//            if(b_pq.isEmpty()){
//                break;
//            }
//            Integer w = st.peek();
//            while (!b_pq.isEmpty() && b_pq.peek() > w){
//                b_pq.poll();
//            }
//            st.pop();
//            collected.add(b_pq.poll());
//        }
//
//        return collected.size();
//    }


    // V0-2
    // IDEA: PQ, QUEUE (gpt)
    // TODO: validate below:
    public int maxBoxesInWarehouse_0_2(int[] boxes, int[] warehouse) {
        // Edge case: check for null or empty arrays
        if (boxes == null || warehouse == null || boxes.length == 0 || warehouse.length == 0) {
            return 0;
        }

        // Sort the boxes (ascending order) to try placing the smallest boxes first
        Arrays.sort(boxes);

        // Sort the warehouse heights (ascending order)
        int n = warehouse.length;
        int[] sortedWarehouse = new int[n];
        for (int i = 0; i < n; i++) {
            sortedWarehouse[i] = warehouse[i];
        }
        Arrays.sort(sortedWarehouse);

        int boxIndex = 0;
        int warehouseIndex = 0;
        int boxCount = 0;

        // Greedily place boxes into the warehouse
        while (boxIndex < boxes.length && warehouseIndex < n) {
            if (boxes[boxIndex] <= sortedWarehouse[warehouseIndex]) {
                boxCount++;
                boxIndex++;
            }
            warehouseIndex++;
        }

        return boxCount;
    }


    // V1
    // https://leetcode.ca/2020-03-12-1564-Put-Boxes-Into-the-Warehouse-I/
    public int maxBoxesInWarehouse_1(int[] boxes, int[] warehouse) {
        int n = warehouse.length;
        int[] left = new int[n];
        left[0] = warehouse[0];
        for (int i = 1; i < n; ++i) {
            left[i] = Math.min(left[i - 1], warehouse[i]);
        }
        Arrays.sort(boxes);
        int i = 0, j = n - 1;
        while (i < boxes.length) {
            while (j >= 0 && left[j] < boxes[i]) {
                --j;
            }
            if (j < 0) {
                break;
            }
            ++i;
            --j;
        }
        return i;
    }

    // V2


}
