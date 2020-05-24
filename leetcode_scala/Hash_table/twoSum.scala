// V0
object Solution{

  def twoSum(nums: Array[Int], target: Int): Array[Int] = {

    val map = scala.collection.mutable.Map[Int, Int] ()
    var count = 0 
    for (num <- nums){
      val comp = target - num
      map.get(comp) match {
        case None => {map.put(num, count)}
        case Some(index) => return Array(index, count)
      }
      count += 1 
    }
    Array(-1,-1)
  }

}

// V1
// https://leetcode.com/problems/two-sum/discuss/138/scala-on-solution
object Solution {
    def twoSum(nums: Array[Int], target: Int): Array[Int] = {
        val map = scala.collection.mutable.Map[Int, Int] ()
        var count = 0;
        for(num <- nums){
            val comp = target-num
            map.get(comp) match {
                case None => {map.put(num,count)}
                case Some(index) => return Array(index,count) 
            }
            count = count+1
        }
        Array(0,0)
    }
}

// V2
// https://leetcode.com/problems/two-sum/discuss/260824/Scala-Solution
object Solution {
    def twoSum(nums: Array[Int], target: Int): Array[Int] = {
        val withIndex = nums.zipWithIndex
        val hashMap = withIndex.toMap
        withIndex
          .collectFirst {
            case (value, index) if hashMap.get(target - value).exists(_ != index) =>
              Array(index, hashMap(target - value))
          }
          .get
      }
}
