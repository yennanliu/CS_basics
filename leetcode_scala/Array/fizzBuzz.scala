// V0
object Solution(n : Int): List[String] = {

    val ans = scala.collection.mutable.ListBuffer[String]()
    for (i <- 1 to n ){
        if (i%3 == 0 && i%5 == 0) li = li :+ "FizzBuzz"
        else if (i%3 == 0) li = li += "Fizz"
        else if (i%5 == 0) li = li += "Buzz"
        else li = li :+ i.toString

    }

    li.toList
}

// V1
object Solution {
    def fizzBuzz(n: Int): List[String] = {
        var li = scala.collection.mutable.ListBuffer[String]()
        
        for(i <- 1 to n) {
            if(i%3 == 0 && i%5 == 0) li = li :+ "FizzBuzz"
            else if (i%3 == 0) li = li :+ "Fizz"
            else if (i%5 == 0) li = li :+ "Buzz"
            else li = li :+ i.toString
        }
        li.toList
    }
}

// V2
