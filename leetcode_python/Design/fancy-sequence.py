"""

1622. Fancy Sequence
Hard

Write an API that generates fancy sequences using the append, addAll, and multAll operations.

Implement the Fancy class:

- Fancy() Initializes the object with an empty sequence.
- void append(val) Appends an integer val to the end of the sequence.
- void addAll(inc) Increments all existing values in the sequence by an integer inc.
- void multAll(m) Multiplies all existing values in the sequence by an integer m.
- int getIndex(idx) Gets the current value at index idx (0-indexed) of the sequence modulo 10^9 + 7. If the index is greater or equal than the length of the sequence, return -1.


Example 1:

Input
["Fancy", "append", "addAll", "append", "multAll", "getIndex", "addAll", "append", "multAll", "getIndex", "getIndex", "getIndex"]
[[], [2], [3], [7], [2], [0], [3], [10], [2], [0], [1], [2]]
Output
[null, null, null, null, null, 10, null, null, null, 26, 34, 20]

Explanation
Fancy fancy = new Fancy();
fancy.append(2);   // fancy sequence: [2]
fancy.addAll(3);   // fancy sequence: [2+3] -> [5]
fancy.append(7);   // fancy sequence: [5, 7]
fancy.multAll(2);  // fancy sequence: [5*2, 7*2] -> [10, 14]
fancy.getIndex(0); // return 10
fancy.addAll(3);   // fancy sequence: [10+3, 14+3] -> [13, 17]
fancy.append(10);  // fancy sequence: [13, 17, 10]
fancy.multAll(2);  // fancy sequence: [13*2, 17*2, 10*2] -> [26, 34, 20]
fancy.getIndex(0); // return 26
fancy.getIndex(1); // return 34
fancy.getIndex(2); // return 20


Constraints:

1 <= val, inc, m <= 100
0 <= idx <= 10^5
At most 10^5 calls total will be made to append, addAll, multAll, and getIndex.

"""

# V0
# IDEA : LAZY AFFINE TRANSFORM + MODULAR INVERSE
#
#   every element currently reads  v = mul * base + add  where (mul, add)
#   is one global affine transform accumulated so far:
#     addAll(inc)  -> add = add + inc
#     multAll(m)   -> mul = mul * m ; add = add * m
#
#   append(val) must store a `base` such that applying the CURRENT (mul,
#   add) gives back val, i.e. base = (val - add) / mul  (mod 1e9+7).
#   division is multiplication by the modular inverse; 1e9+7 is prime so
#   inv(mul) = pow(mul, MOD-2, MOD) by Fermat.
#
#   NOTE : mul is never 0 mod MOD because 1 <= m <= 100 < MOD.
#
# time = O(1) for append (amortised, O(log MOD) for the inverse), O(1) others
# space = O(n)
class Fancy(object):

    def __init__(self):
        self.MOD = 10 ** 9 + 7
        self.mul = 1
        self.add = 0
        self.base = []

    def append(self, val):
        inv = pow(self.mul, self.MOD - 2, self.MOD)
        self.base.append((val - self.add) * inv % self.MOD)

    def addAll(self, inc):
        self.add = (self.add + inc) % self.MOD

    def multAll(self, m):
        self.mul = self.mul * m % self.MOD
        self.add = self.add * m % self.MOD

    def getIndex(self, idx):
        if idx >= len(self.base):
            return -1
        return (self.base[idx] * self.mul + self.add) % self.MOD


# Your Fancy object will be instantiated and called as such:
# obj = Fancy()
# obj.append(val)
# obj.addAll(inc)
# obj.multAll(m)
# param_4 = obj.getIndex(idx)
