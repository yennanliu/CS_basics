// V0

// V1
// https://leetcode.com/problems/group-anagrams/discuss/442604/My-Scala-solution
object Solution {
    def groupAnagrams(strs: Array[String]): List[List[String]] = {
        strs.groupBy(s => s.sortWith(_<_)).values.map(_.toList).toList
    }
}

// V1'
// https://amitbansal7.github.io/2018-04-24/leetcode-group-anagrams
object Solution {
  def groupAnagrams(strs: Array[String]): List[List[String]] = {
    def solve(strs: Array[String],
              idx: Int,
              mp: Map[String, List[String]]): Map[String, List[String]] = {
      if (idx >= strs.size) mp
      else {
        val str = strs(idx).sortWith(_ < _)
        mp.get(str) match {
          case Some(xs: List[String]) =>
            solve(strs, idx + 1, mp.updated(str, xs :+ strs(idx)))
          case _ => solve(strs, idx + 1, mp + (str -> List(strs(idx))))
        }
      }
    }
    solve(strs, 0, Map[String, List[String]]()).values.toList
  }
}

// V2