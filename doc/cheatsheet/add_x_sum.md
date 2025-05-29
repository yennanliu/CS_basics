# Add `xxx` to Sum

- https://leetcode.com/problems/add-strings/solution/

`Facebook` interviewers like this question and propose it in four main variations. The choice of algorithm should be based on the input format:

1. Strings (the current problem). Use schoolbook digit-by-digit addition. Note, that to fit into constant space is not possible for languages with immutable strings, for example, for Java and Python. Here are two examples:
    - LC 067 : Add Binary: sum two binary strings.
    - LC 415 : Add Strings: sum two non-negative numbers in a string representation without converting them to integers directly.

2. Integers. Usually, the interviewer would ask you to implement a sum without using + and - operators. Use bit manipulation approach. Here is an example:
    - LC 371 : Sum of Two Integers: Sum two integers without using + and - operators.

3. Arrays. The same textbook addition. Here is an example:
    - LC 989 : Add to Array Form of Integer.

4. Linked Lists. Sentinel Head + Textbook Addition. Here are some examples:
    - LC 66 : Plus One.
    - LC 002 : Add Two Numbers.
    - LC 445 : Add Two Numbers II.


## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Add Binary
```python
# LC 067 Add Binary: sum two binary strings.
# V0
# IDEA : STRING + BINARY
class Solution(object):
    def addBinary(self, a, b):

        _len = max(len(a), len(b))
        if len(a) < _len:
            a = (_len - len(a)) * '0' + a
        if len(b) < _len:
            b = (_len - len(b)) * '0' + b

        plus = 0
        result = ""

        # INVERSE LOOPING THE a, b
        for i in range(len(a))[::-1]:
            tmp = int(a[i]) + int(b[i]) + plus
            if tmp > 1:
                tmp -= 2
                plus = 1
            else:
                plus = 0

            result += str(tmp)

        if plus == 1:
            return '1' + result[::-1]  ### NOTE WE NEED TO REVERSE IT!
        else: 
            return result[::-1] ### NOTE WE NEED TO REVERSE IT!

# V0
class Solution(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        res = ''
        i, j, plus = len(a)-1, len(b)-1, 0
        while i>=0 or j>=0 or plus==1:
            plus += int(a[i]) if i>= 0 else 0
            plus += int(b[j]) if j>= 0 else 0
            res = str(plus % 2) + res
            i, j, plus = i-1, j-1, plus//2  # since max of "plus" is 3 in bimary case, so "plus//2" works here
        return res
```

### 2-2) Add Strings

```java
// java
// LC 415
// V0
// IDEA: string op (fixed by gpt)
public String addStrings(String num1, String num2) {
    if (num1 == null || num2 == null) {
        if (num1 == null) {
            return num2;
        }
        return num1;
    }
    if (num1.equals("0") && num2.equals("0")) {
        return "0";
    }

    StringBuilder sb = new StringBuilder();

    int plus = 0;
    int idx_1 = num1.length() - 1;
    int idx_2 = num2.length() - 1;

    /** NOTE !!!
     *
     *  1. while loop
     *  2. idx_1 >= 0 or idx_2 >= 0
     */
    while (idx_1 >= 0 || idx_2 >= 0) {

        int v1 = 0;
        int v2 = 0;

        int new_val = 0;

        /** NOTE !!!
         *
         *  if idx_1 >= 0, then get val from it
         */
        if (idx_1 >= 0) {
            v1 = Integer.parseInt(String.valueOf(num1.charAt(idx_1)));
            idx_1 -= 1;
        }

        /** NOTE !!!
         *
         *  if idx_1 >= 0, then get val from it
         */
        if (idx_2 >= 0) {
            v2 = Integer.parseInt(String.valueOf(num2.charAt(idx_2)));
            idx_2 -= 1;
        }

        new_val = (new_val + v1 + v2 + plus);

        /** NOTE !!!
         *
         *  if new_vla > 9,
         *  we should `subtract 10` (instead of 9)
         */
        if (new_val > 9) {
            plus = 1;
            new_val -= 10;
        } else {
            plus = 0;
        }

        sb.append(new_val);
    }

    /** NOTE !!!
     *
     *  need to add the `remaining plus` to res
     *  if there is it
     */
    if (plus > 0) {
        sb.append(plus);
    }

    // reverse
    return sb.reverse().toString();
}
```


```python
# LC 415. Add Strings
# V0
# IDEA : string + math
class Solution(object):
    def addStrings(self, num1, num2):
        result = []
        # note : we init carry as 0
        carry = 0
        num1 = list(num1)
        num2 = list(num2)
        # while there is still non-add digit in num1, and num2; or there is non-zero carry 
        while num1 or num2 or carry:
            digit = carry
            if num1:
                tmp1 = num1.pop(-1)
                digit += int(tmp1)
            if num2:
                tmp2 = num2.pop(-1)
                digit += int(tmp2)
            """
            if digit > 9 -> we need to "carry" 1 to next digit -> carry = 1
            else -> carry = 0
            """
            if digit > 9:
                carry = 1
            else:
                carry = 0
            # NOTE !!! we get "remain" by 10 via below code
            result.append(str(digit % 10))
        return ''.join(result[::-1]) 
```

### 2-3) Sum of Two Integers
```python
# LC 371 : Sum of Two Integers
# V0
# https://leetcode.com/problems/sum-of-two-integers/discuss/1214257/Python-1-line%3A-91-faster
class Souluton:
    def getSum(self, a, b):
        tmp = math.exp(a) * math.exp(b)
        r = int(math.log(tmp))
        return r
```

### 2-4) Add to Array Form of Integer
```python
# LC 989 Add to Array Form of Integer
# V0
# IDEA : array op
class Solution:
    def addToArrayForm(self, num, k):
        s = ""
        for i in num:
            s += str(i)       
        answer = int(s) + k
        return  list("".join(str(answer)))
```

### 2-5) Plus One
```python
# LC 66 Plus One
# V0
# NOTE : Notice the index of inverse loop  : range(len(a)-1, -1, -1)
# a = ['a', 'b', 'c']
# for i in range(len(a)-1, -1, -1):
#     print (a[i])
# c
# b
# a
class Solution(object):
    def plusOne(self, digits):
        """
        :type digits: List[int]
        :rtype: List[int]
        """
        plus = 1
        for i in range(len(digits)-1, -1, -1):
            if digits[i] + plus > 9:
                digits[i] = 0
                plus = 1
            else:
                digits[i] = digits[i] + plus
                plus = 0
        if plus == 1:
            digits.insert(0, 1)
        return digits
```

### 2-6) Add Two Numbers
```python
# LC 002 Add Two Numbers
# V0
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        NOTE :
         1. we init linkedlist via ListNode()
         2. we NEED make extra head refer same linkedlist, since we need to return beginning of linkedlust of this func, while res will meet "tail" at the end of while loop
        """
        head = res = ListNode()
        plus = 0
        tmp = 0
        while l1 or l2:
            tmp += plus
            plus = 0
            if l1:
                tmp += l1.val
                l1 = l1.next
            if l2:
                tmp += l2.val
                l2 = l2.next
            if tmp > 9:
                tmp -= 10
                plus = 1

            res.next = ListNode(tmp)
            res = res.next
            tmp = 0
        ### NOTE : need to deal with case : l1, l2 are completed, but still "remaining" plus
        if plus != 0:
            res.next = ListNode(plus)
            res = res.next
        #print ("res = " + str(res))
        #print ("head = " + str(head))
        return head.next
```

### 2-7) Add Two Numbers II
```python
# LC 445 Add Two Numbers II
# V0
# IDEA : string + linked list
# DEMO
# input :
# [7,2,4,3]
# [5,6,4]
# intermedia output : 
# l1_num = 7243
# l2_num = 564
class Solution:
    def addTwoNumbers(self, l1, l2):
        if not l1 and not l2:
            return None

        l1_num = 0
        while l1:
            l1_num = l1_num * 10 + l1.val
            l1 = l1.next

        l2_num = 0
        while l2:
            l2_num = l2_num * 10 + l2.val
            l2 = l2.next

        print ("l1_num = " + str(l1_num))
        print ("l2_num = " + str(l2_num))


        ### NOTE : trick here :
        #    -> get int format of 2 linked list first (l1, l2)
        #    -> then sum them (l1_num + l2_num)
        lsum = l1_num + l2_num

        head = ListNode(None)
        cur = head
        ### NOTE : go thrpigh the linked list int sum, append each digit to ListNode and return it
        for istr in str(lsum):
            cur.next = ListNode(int(istr))
            cur = cur.next
        # NOTE : need to return head (but not cur, since cur already meet the end of ListNode)
        return head.next
```