// V0 : to fix
// object Solution {
//     def removeDuplicates(nums: Array[Int]): Int = {
//         if (nums.length == 0) { 0 }
//         else {
//             var j = 0
//             for (i <- nums.indices){
//                 if (nums(i) != nums(j)) {
//                     {nums(i) = nums(j+1)
//                     nums(j+1) =  nums(i)
//                     j = j + 1}
//                 }
//             }  j + 1
//         } 
//     }
// }

// V1
// https://leetcode.com/problems/remove-duplicates-from-sorted-array/discuss/337877/scala-solution
object Solution {

  def removeDuplicates(nums: Array[Int]): Int = {
    def aux(s: Int, f: Int): Int = {
      // 1-base and 0-base
      if (f > nums.length - 1) s
      else if (nums(s) == nums(f)) aux(s, f + 1)
      else {
        nums(s + 1) = nums(f)
        aux(s + 1, f + 1)
      }
    }

    if (nums.isEmpty) 0
    else 1 + aux(0, 1)
  }

}

// V1'
// https://leetcode.com/problems/remove-duplicates-from-sorted-array/discuss/351346/Scala-solution-using-foldLeft
object Solution {
    def removeDuplicates(nums: Array[Int]): Int = {
      if (nums.isEmpty) 0
      else {
        nums
          .drop(1)
          .foldLeft((1, nums(0)))((tuple, current) => {
            val (uniqueCount: Int, previous: Int) = tuple

            if (current == previous) (uniqueCount, current)
            else {
              nums(uniqueCount) = current
              (uniqueCount + 1, current)
            }
          })
          ._1
      }
    }
  }