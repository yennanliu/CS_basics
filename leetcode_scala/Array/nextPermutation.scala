// V0

// V1
// https://leetcode.com/problems/next-permutation/discuss/267375/Explain-the-algorithm
object Solution {
    def nextPermutation(nums: Array[Int]): Unit = {
        @annotation.tailrec
        def reverse(i: Int, j: Int): Unit = {
            if (i < j) {
                val tmp = nums(i)
                nums(i) = nums(j)
                nums(j) = tmp
                reverse(i + 1, j - 1)
            }
        }
        
        def swap(i: Int, j: Int): Unit= {
            val tmp = nums(i)
            nums(i) = nums(j)
            nums(j) = tmp
        }
        
        nums.indices.drop(1).reverse.find { rightNeighborOfX =>
            // Iterate though right to left to find out X,
            // which is less than its right neighbor
            val x = rightNeighborOfX - 1
            nums(x) < nums(rightNeighborOfX)                 
        } match {
            case None =>
                // X is not found because the entire number is descending ordered
                reverse(0, nums.indices.last)
            case Some(rightNeighborOfX) =>
                val x = rightNeighborOfX - 1
                
                // Perform a right-to-left linear search to find the digit Y,
                // which is just greater than X
                val Some(y) = (rightNeighborOfX until nums.length).reverse.find { y =>
                    nums(y) > nums(x)
                }
            
                swap(x, y)
            
                // Reverse right hand side digits of X to turn them from descending order to ascending order
                reverse(rightNeighborOfX, nums.indices.last)
        }
    }
}