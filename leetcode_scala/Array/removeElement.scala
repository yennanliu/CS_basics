// VO
// need to fix 
// object Solution {
//     def removeElement(nums: Array[Int], `val`: Int): Int = {
//         if (nums.length == 0 ) 0
//         else 
//         for (i <- nums.indices){
//             var length = 0
//             if (nums(i) != `val`){
//                 nums(length) = nums(i)
//                 length += 1
//             }
//         } 
//     } 
// }

// V1
// JAVA
// http://buttercola.blogspot.com/2014/09/leetcode-remove-element.html
public class Solution {
    public int removeElement(int[] A, int elem) {
        if (A == null || A.length == 0) return 0;
         
        int counter = 0;
        for (int i = 0; i < A.length; i++) {
            if (A[i] != elem) {
                A[counter++] = A[i];
            }
        }
        return counter;
    }
}