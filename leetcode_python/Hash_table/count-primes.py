
# Time:  O(n)
# Space: O(n)

# Description:
#
# Count the number of prime numbers less than a non-negative number, n
#
# Hint: The number n could be in the order of 100,000 to 5,000,000.


# V1  : dev 

"""

class Solution(object):
    def countPrimes(self, n):
        prime_nums=[]
        for i in range(1,n+1):
            print ('---')
            print ('i = ', i)
            
            for j in range(2,i+1):
                print ('j = ' , j)
                if i%j !=0:
                    prime_nums.append(i)
                else:
                    break
        return prime_nums
                    
        

"""




#  V2
class Solution:
    # @param {integer} n
    # @return {integer}
    def countPrimes(self, n):
        if n <= 2:
            return 0

        is_prime = [True] * n
        num = n / 2
        for i in range(3, n, 2):
            if i * i >= n:
                break

            if not is_prime[i]:
                continue

            for j in range(i*i, n, 2*i):
                if not is_prime[j]:
                    continue

                num -= 1
                is_prime[j] = False

        return num

    def countPrimes2(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n < 3:
            return 0
        primes = [True] * n
        primes[0] = primes[1] = False
        for i in range(2, int(n ** 0.5) + 1):
            if primes[i]:
                primes[i * i: n: i] = [False] * len(primes[i * i: n: i])
        return sum(primes)



