package LeetCodeJava.Stack;

// https://leetcode.com/problems/baseball-game/

import java.util.Stack;

public class BaseballGame {

    // V0
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

    // V1
    // https://leetcode.com/problems/baseball-game/solutions/3152920/beats-100-easy-sol-in-java/
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
