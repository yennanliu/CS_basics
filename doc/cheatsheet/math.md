# Math 

## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

#### 1-1-0) transform interger to N base
- to 4 base
- to 7 base ..
- https://github.com/yennanliu/CS_basics/blob/master/doc/pic/convert_int_n_base.png
```python
# python
# 504. Base 7

# V0
# IDEA : MATH : 10 based -> 7 based
"""
### NOTE :
    1) for negative num : transform to positive int first, do 10 based -> 7 based op
                          then add "-" at beginning
    2) for positive num : do 10 based -> 7 based op
                        -> keep checking if num % N == 0
                        -> if not == 0, keep do  num = num % N, and append cur result to res
                        -> reverse res
                        -> make array to string
                        -> return result
    3) example:
        100 (10 based) -> "202" (7 based)

        tmp = 100
        a, b = divmod(tmp, 7)  -> a = 14, b = 2,  tmp  = 14
        a, b = divmod(tmp, 7)  -> a= 2, b = 0, tmp = 2
        a, b = divmod(tmp, 7)  -> a = 0, b = 2

        -> so res = [2,0,2]
"""
class Solution(object):
    def convertToBase7(self, num):
        # edge case
        if num == 0:
            return '0'
        tmp = abs(num)
        res = []
        while tmp > 0:
            a, b = divmod(tmp, 7)
            res.append(str(b))
            tmp = a
        res = res[::-1]
        _res = "".join(res)
        if num > 0:
            return _res
        else:
            return "-" + _res

# test
#----------------
# exmaple :
# 7 base
# 20 -> 26
# -100 -> -202
#----------------
num = 20   # 26
num = -100 # -202
s = Solution()
r = s.convertToBase7(num)
print (r)

# V1
# https://www.itread01.com/content/1544603062.html
# https://kknews.cc/code/jlv38qp.html
class Solution(object):
    def convertToBase7(self, num):
        # edge case
        if num == 0:
            return '0'
        tmp = abs(num)
        res = []
        while tmp:
            i = tmp % 7
            res.append(str(i))
            tmp = tmp // 7
        res = res[::-1]
        _res = "".join(res)
        if num > 0:
            return _res
        else:
            return "-" + _res 
```

#### 1-1-1) check prime number
```python
# LC 762 Prime Number of Set Bits in Binary Representation
def check_prime(x):
    if x <= 1:
        return False
    for i in range(2, int(x**(0.5)+1)):
        if x % i == 0:
            return False
    return True
```

#### 1-1-2) count prime number
```python
# LC 204 Count Primes
# V0
# IDEA : set
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
# prime(x) : number of prime in [0, x]
# prime(0) = 0
# prime(1) = 0
# prime(2) = 0
# prime(3) = 1
# prime(4) = 2
# prime(5) = 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = set()
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes.add(j)
        return n - len(nonprimes) - 2  # remove prime(1), prime(2)
```

```java
// java
// algorithm book (labu) p.362
// V1
int countPrimes(int n){
    boolean[] isPrime = new boolean[n];

    // init array to true
    Arrays.fill(isPrime, true);

    // prime number start from 2
    for (int i = 2; i < n; i++){
        if (isPrime[i]){
            // if i is prime, then i's multiple is NOT prime
            for (int j = 2; j = 2 * i; j += i){
                isPrime[j] = false;
            }
        }
    }

    int count = 0;
    for (int i = 2; i < n; i++){
        if (isPrime[i]){
            count ++;
        }
    }
    return count;
}
```

```java
// java
// algorithm book (labu) p.363
// V1' (optimization)
int countPrimes(int n){
    boolean[] isPrime = new boolean[n];

    // init array to true
    Arrays.fill(isPrime, true);

    // prime number start from 2
    for (int i = 2; i * i  < n; i++){
        if (isPrime[i]){
            // if i is prime, then i's multiple is NOT prime
            /** optimize here :  make j start from i * i, instead of 2 * i */
            // (the only difference between V1 and V1')
            for (int j = i * i; j < n; j += i){
                isPrime[j] = false;
            }
        }
    }

    int count = 0;
    for (int i = 2; i < n; i++){
        if (isPrime[i]){
            count ++;
        }
    }
    return count;
}
```

## 2) LC Example

### 2-1) Excel Sheet Column Title
```python
# 168 Excel Sheet Column Title
# https://leetcode.com/problems/excel-sheet-column-title/discuss/205987/Python-Solution-with-explanation

# V0
# https://www.jianshu.com/p/591d3a2ab45d
class Solution(object):
    def convertToTitle(self, n):
        tar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        res = ""
        while n > 0:
            # why (n-1) ? : idx start from 0
            m = (n-1) % 26
            #result += tar[m]
            res = (tar[m] + res)
            if m == 0:
                # why n=n+1 ? : since there is no 0 residual (m = (n-1) % 26), so we need to "pass" this case
                n = n + 1
            n = (n-1) // 26
        return res
# V0'
class Solution:
    def convertToTitle(self, n):
        """
        :type n: int
        :rtype: str
        """
        d='0ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        res=''
        if n<=26:
            return d[n]
        else:
            while n > 0:
                n,r=divmod(n,26)
                # This is the catcha on this problem where when r==0 as a result of n%26. eg, n=52//26=2, r=52%26=0. 
                #To get 'AZ' as known for 52, n-=1 and r+=26. Same goes to 702.
                if r == 0:
                    n-=1
                    r+=26
                res = d[r] + res
        return res
```

### 2-2) Solve the Equation
```python
# LC 640. Solve the Equation
# V0
# IDEA : replace + eval + math
# https://leetcode.com/problems/solve-the-equation/discuss/105362/Simple-2-liner-(and-more)
# eval : The eval() method parses the expression passed to this method and runs python expression (code) within the program.
# -> https://www.runoob.com/python/python-func-eval.html
class Solution(object):
    def solveEquation(self, equation):
        tmp = equation.replace('x', 'j').replace('=', '-(')
        z = eval(tmp + ")" , {'j':1j})
        # print ("equation = " + str(equation))
        # print ("tmp = " + str(tmp))
        # print ("z = " + str(z))
        a, x = z.real, -z.imag
        return 'x=%d' % (a / x) if x else 'No solution' if a else 'Infinite solutions'
```