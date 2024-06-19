package LeetCodeJava.String;

// https://leetcode.com/problems/multiply-strings/description/

import java.util.ArrayList;

/**
 *  Similar LC problems
 *
 *      66. Plus One
 *      67. Add Binary
 *      415. Add Strings
 *      989. Add to Array-Form of Integer
 *
 */

public class MultiplyStrings {

    // V0
    // IDEA : STRING OP
    // TODO : fix
//    public String multiply(String num1, String num2) {
//
//        if (num1.equals("0") || num2.equals("0")){
//            return "0";
//        }
//
//        if (num1.equals("1") || num2.equals("1")){
//            if (num1.equals("1")){
//                return num2;
//            }
//            return num1;
//        }
//
//        int res = 0;
//        Long num2Int = Long.parseLong(num2);
//        Long num1Int = Long.parseLong(num1);
//        while (num2Int > 0){
//            res += num1Int;
//            num2Int -= 1L;
//        }
//
//        return String.valueOf(res);
//    }

    // V1
    // IDEA : BRUTE FORCE + STRING OP (gpt)
    public String multiply_1(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        int m = num1.length();
        int n = num2.length();
        int[] result = new int[m + n];

        /** NOTE !!!
         *
         *  double loop
         *  i traverse from first string
         *  j traverse from second string
         */
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j;
                int p2 = i + j + 1;
                int sum = mul + result[p2];

                result[p1] += sum / 10;
                result[p2] = sum % 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int num : result) {
            if (!(sb.length() == 0 && num == 0)) {
                sb.append(num);
            }
        }

        return sb.length() == 0 ? "0" : sb.toString();
    }

    // V2
    // https://leetcode.com/problems/multiply-strings/editorial/
    // IDEA : MATH
    // Calculate the sum of all of the results from multiplyOneDigit.
    private StringBuilder sumResults(ArrayList<ArrayList<Integer>> results) {
        // Initialize answer as a number from results.
        ArrayList<Integer> answer = new ArrayList<>(
                results.get(results.size() - 1)
        );
        ArrayList<Integer> newAnswer = new ArrayList<>();

        // Sum each digit from answer and result
        for (int j = 0; j < results.size() - 1; ++j) {
            ArrayList<Integer> result = new ArrayList<>(results.get(j));
            newAnswer = new ArrayList<>();

            int carry = 0;

            for (int i = 0; i < answer.size() || i < result.size(); ++i) {
                // If answer is shorter than result or vice versa, use 0 as the current digit.
                int digit1 = i < result.size() ? result.get(i) : 0;
                int digit2 = i < answer.size() ? answer.get(i) : 0;
                // Add current digits of both numbers.
                int sum = digit1 + digit2 + carry;
                // Set carry equal to the tens place digit of sum.
                carry = sum / 10;
                // Append the ones place digit of sum to answer.
                newAnswer.add(sum % 10);
            }

            if (carry != 0) {
                newAnswer.add(carry);
            }
            answer = newAnswer;
        }

        // Convert answer to a string.
        StringBuilder finalAnswer = new StringBuilder();
        for (int digit : answer) {
            finalAnswer.append(digit);
        }
        return finalAnswer;
    }

    // Multiply the current digit of secondNumber with firstNumber.
    ArrayList<Integer> multiplyOneDigit(
            StringBuilder firstNumber,
            char secondNumberDigit,
            int numZeros
    ) {
        // Insert zeros at the beginning based on the current digit's place.
        ArrayList<Integer> currentResult = new ArrayList<>();
        for (int i = 0; i < numZeros; ++i) {
            currentResult.add(0);
        }

        int carry = 0;

        // Multiply firstNumber with the current digit of secondNumber.
        for (int i = 0; i < firstNumber.length(); ++i) {
            char firstNumberDigit = firstNumber.charAt(i);
            int multiplication =
                    (secondNumberDigit - '0') * (firstNumberDigit - '0') + carry;
            // Set carry equal to the tens place digit of multiplication.
            carry = multiplication / 10;
            // Append last digit to the current result.
            currentResult.add(multiplication % 10);
        }

        if (carry != 0) {
            currentResult.add(carry);
        }
        return currentResult;
    }

    public String multiply_2_1(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        StringBuilder firstNumber = new StringBuilder(num1);
        StringBuilder secondNumber = new StringBuilder(num2);

        // Reverse both the numbers.
        firstNumber.reverse();
        secondNumber.reverse();

        // For each digit in secondNumber, multipy the digit by firstNumber and
        // store the multiplication result (reversed) in results.
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        for (int i = 0; i < secondNumber.length(); ++i) {
            results.add(
                    multiplyOneDigit(firstNumber, secondNumber.charAt(i), i)
            );
        }

        // Add all the results in the results array, and store the sum in the answer string.
        StringBuilder answer = sumResults(results);

        // answer is reversed, so reverse it to get the final answer.
        answer.reverse();
        return answer.toString();
    }

    // V3
    // https://leetcode.com/problems/multiply-strings/editorial/
    // IDEA : MATH + less space usage


    // V4
    // https://leetcode.com/problems/multiply-strings/editorial/
    // IDEA : Sum the products from all pairs of digits
    public String multiply_4(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        StringBuilder firstNumber = new StringBuilder(num1);
        StringBuilder secondNumber = new StringBuilder(num2);

        // Reverse both the numbers.
        firstNumber.reverse();
        secondNumber.reverse();

        // To store the multiplication result of each digit of secondNumber with firstNumber.
        int N = firstNumber.length() + secondNumber.length();
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < N; ++i) {
            answer.append(0);
        }

        for (int place2 = 0; place2 < secondNumber.length(); place2++) {
            int digit2 = secondNumber.charAt(place2) - '0';

            // For each digit in secondNumber multiply the digit by all digits in firstNumber.
            for (int place1 = 0; place1 < firstNumber.length(); place1++) {
                int digit1 = firstNumber.charAt(place1) - '0';

                // The number of zeros from multiplying to digits depends on the
                // place of digit2 in secondNumber and the place of the digit1 in firstNumber.
                int currentPos = place1 + place2;

                // The digit currently at position currentPos in the answer string
                // is carried over and summed with the current result.
                int carry = answer.charAt(currentPos) - '0';
                int multiplication = digit1 * digit2 + carry;

                // Set the ones place of the multiplication result.
                answer.setCharAt(
                        currentPos,
                        (char) ((multiplication % 10) + '0')
                );

                // Carry the tens place of the multiplication result by
                // adding it to the next position in the answer array.
                int value =
                        (answer.charAt(currentPos + 1) - '0') + multiplication / 10;
                answer.setCharAt(currentPos + 1, (char) (value + '0'));
            }
        }

        // Pop excess 0 from the rear of answer.
        if (answer.charAt(answer.length() - 1) == '0') {
            answer.deleteCharAt(answer.length() - 1);
        }

        answer.reverse();
        return answer.toString();
    }

}
