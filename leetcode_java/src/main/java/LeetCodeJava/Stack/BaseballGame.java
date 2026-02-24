package LeetCodeJava.Stack;

// https://leetcode.com/problems/baseball-game/
/**
 *
 682. Baseball Game
 Solved
 Easy
 Topics
 premium lock icon
 Companies
 You are keeping the scores for a baseball game with strange rules. At the beginning of the game, you start with an empty record.

 You are given a list of strings operations, where operations[i] is the ith operation you must apply to the record and is one of the following:

 An integer x.
 Record a new score of x.
 '+'.
 Record a new score that is the sum of the previous two scores.
 'D'.
 Record a new score that is the double of the previous score.
 'C'.
 Invalidate the previous score, removing it from the record.
 Return the sum of all the scores on the record after applying all the operations.

 The test cases are generated such that the answer and all intermediate calculations fit in a 32-bit integer and that all operations are valid.



 Example 1:

 Input: ops = ["5","2","C","D","+"]
 Output: 30
 Explanation:
 "5" - Add 5 to the record, record is now [5].
 "2" - Add 2 to the record, record is now [5, 2].
 "C" - Invalidate and remove the previous score, record is now [5].
 "D" - Add 2 * 5 = 10 to the record, record is now [5, 10].
 "+" - Add 5 + 10 = 15 to the record, record is now [5, 10, 15].
 The total sum is 5 + 10 + 15 = 30.
 Example 2:

 Input: ops = ["5","-2","4","C","D","9","+","+"]
 Output: 27
 Explanation:
 "5" - Add 5 to the record, record is now [5].
 "-2" - Add -2 to the record, record is now [5, -2].
 "4" - Add 4 to the record, record is now [5, -2, 4].
 "C" - Invalidate and remove the previous score, record is now [5, -2].
 "D" - Add 2 * -2 = -4 to the record, record is now [5, -2, -4].
 "9" - Add 9 to the record, record is now [5, -2, -4, 9].
 "+" - Add -4 + 9 = 5 to the record, record is now [5, -2, -4, 9, 5].
 "+" - Add 9 + 5 = 14 to the record, record is now [5, -2, -4, 9, 5, 14].
 The total sum is 5 + -2 + -4 + 9 + 5 + 14 = 27.
 Example 3:

 Input: ops = ["1","C"]
 Output: 0
 Explanation:
 "1" - Add 1 to the record, record is now [1].
 "C" - Invalidate and remove the previous score, record is now [].
 Since the record is empty, the total sum is 0.


 Constraints:

 1 <= operations.length <= 1000
 operations[i] is "C", "D", "+", or a string representing an integer in the range [-3 * 104, 3 * 104].
 For operation "+", there will always be at least two previous scores on the record.
 For operations "C" and "D", there will always be at least one previous score on the record.
 *
 *
 *
 */
import java.util.Stack;

public class BaseballGame {

    // V0
    /**
     * time = O(1)
     * space = O(1)
     */
    public int calPoints(String[] operations) {

        Integer ans = 0;
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < operations.length; i++){
            String element = operations[i];
            int stackLen = stack.toArray().length;
            System.out.println("element = " + element + " stack = " + stack);
            if (isInteger(element)){
                stack.push(element);
            } else if (element.equals("+")) {
                if(stackLen >= 2){
                    // TODO: use stack peak, pop, push ... op instead
                    int prevous_1 = Integer.parseInt(stack.get(stackLen-1));
                    int prevous_2 = Integer.parseInt(stack.get(stackLen-2));
                    int curSum = prevous_1 + prevous_2;
                    stack.push(String.valueOf(curSum));
                }
            } else if (element.equals("C")) {
                if(stackLen >= 1){
                    stack.pop();
                }
            } else if (element.equals("D")) {
                if(stackLen >= 1){
                    int last = Integer.parseInt(stack.get(stackLen-1));
                    stack.push(String.valueOf(last * 2));
                }
            }
        }

        // sum up
        for (String item : stack){
            ans += Integer.parseInt(item);
        }
        //System.out.println("stack = " + stack.toString());
        return ans;
    }

    private Boolean isInteger(String input){
        try{
            int res = Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    // V0-1
    // IDEA: STACK (gemini)
    public int calPoints_0_1(String[] operations) {
        if (operations == null || operations.length == 0) {
            return 0;
        }

        Stack<Integer> st = new Stack<>();

        for (String op : operations) {
            if (op.equals("C")) {
                if (!st.isEmpty()) {
                    st.pop();
                }
            } else if (op.equals("D")) {
                if (!st.isEmpty()) {
                    // Peek doesn't remove the element!
                    // We just need the value to double it.
                    st.push(st.peek() * 2);
                }
            } else if (op.equals("+")) {
                if (st.size() >= 2) {
                    // We need the last two, but we must keep them in the stack
                    int last = st.pop();
                    int secondLast = st.peek();
                    int newScore = last + secondLast;

                    // Put 'last' back, then add the 'newScore'
                    st.push(last);
                    st.push(newScore);
                }
            } else {
                st.push(Integer.parseInt(op));
            }
        }

        int res = 0;
        while (!st.isEmpty()) {
            res += st.pop();
        }
        return res;
    }


    // V1
    // https://leetcode.com/problems/baseball-game/solutions/3152920/beats-100-easy-sol-in-java/
    /**
     * time = O(1)
     * space = O(1)
     */
    public int calPoints_1(String[] operations) {
        int sum=0;
        int[] arr = new int[operations.length];
        int count=0;

        for(int i=0;i<operations.length;i++){
            // System.out.println(count + " " + operations[i]);
            if(operations[i].equals("C")){
                arr[--count]=0;
            }
            else if(operations[i].equals("D")){
                arr[count] = 2 * arr[count-1];
                count++;
            }
            else if(operations[i].equals("+")){
                arr[count]= arr[count-1] + arr[count-2];
                count++;
            }
            else{
                // System.out.println(operations[i]);
                arr[count] = Integer.parseInt(operations[i]);
                count++;
            }
        }
        for(int j=0;j<arr.length;j++){
            //System.out.println(arr[j]);
            sum += arr[j];
        }
        return sum;
    }

    // V2
    // https://leetcode.com/problems/baseball-game/solutions/3534032/java-stack-baseball-game/
    /**
     * time = O(1)
     * space = O(1)
     */
    public int calPoints_2(String[] operations) {
        Stack<Integer> stack = new Stack<>();
        for(String step : operations)
        {
            switch (step)
            {
                case "+":
                {
                    int second = stack.pop();
                    int first = stack.peek();
                    stack.push(second);
                    stack.push(second + first);
                    break;
                }
                case "D": {
                    stack.push(stack.peek() * 2);
                    break;
                }
                case "C":
                {
                    stack.pop();
                    break;
                }
                default:
                    stack.push(Integer.parseInt(step));
            }
        }
        int sum = 0;
        while (stack.size() > 0)
            sum += stack.pop();
        return sum;
    }




}
