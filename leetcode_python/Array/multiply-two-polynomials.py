"""

3549. Multiply Two Polynomials
Hard

You are given two integer arrays poly1 and poly2, where the element at index i
in each array represents the coefficient of x^i in a polynomial.

Let A(x) and B(x) be the polynomials represented by poly1 and poly2,
respectively.

Return an integer array result of length (poly1.length + poly2.length - 1)
representing the coefficients of the product polynomial R(x) = A(x) * B(x),
where result[i] denotes the coefficient of x^i in R(x).

Example 1:

Input: poly1 = [3,2,5], poly2 = [1,4]

Output: [3,14,13,20]

Explanation:

A(x) = 3 + 2x + 5x^2 and B(x) = 1 + 4x

R(x) = (3 + 2x + 5x^2) * (1 + 4x)

R(x) = 3 * 1 + (3 * 4 + 2 * 1)x + (2 * 4 + 5 * 1)x^2 + (5 * 4)x^3

R(x) = 3 + 14x + 13x^2 + 20x^3

Thus, result = [3, 14, 13, 20].

Example 2:

Input: poly1 = [1,0,-2], poly2 = [-1]

Output: [-1,0,2]

Explanation:

A(x) = 1 + 0x - 2x^2 and B(x) = -1

R(x) = (1 + 0x - 2x^2) * (-1)

R(x) = -1 + 0x + 2x^2

Thus, result = [-1, 0, 2].

Example 3:

Input: poly1 = [1,5,-3], poly2 = [-4,2,0]

Output: [-4,-18,22,-6,0]

Explanation:

A(x) = 1 + 5x - 3x^2 and B(x) = -4 + 2x + 0x^2

R(x) = (1 + 5x - 3x^2) * (-4 + 2x + 0x^2)

R(x) = 1 * -4 + (1 * 2 + 5 * -4)x + (5 * 2 + -3 * -4)x^2 + (-3 * 2)x^3 + 0x^4

R(x) = -4 -18x + 22x^2 -6x^3 + 0x^4

Thus, result = [-4, -18, 22, -6, 0].

Constraints:

1 <= poly1.length, poly2.length <= 5 * 10^4

-10^3 <= poly1[i], poly2[i] <= 10^3

poly1 and poly2 contain at least one non-zero coefficient.

"""

# V0
# IDEA : KRONECKER SUBSTITUTION -- LET PYTHON'S BIG-INT MULTIPLY DO THE FFT
#
#   the schoolbook convolution is 2.5 * 10^9 multiplications at these sizes, so
#   the product has to come from a fast algorithm.  rather than writing an FFT,
#   evaluate both polynomials at x = 2^B: a polynomial becomes a single huge
#   integer with each coefficient parked in its own B-bit slot, and the
#   integer product's slots are exactly the convolution -- provided no slot
#   overflows into its neighbour.
#
#   with |coefficients| <= 10^3 and up to 5*10^4 terms, a product coefficient
#   is at most 5*10^10 < 2^40, so B = 40 bits (five whole bytes) is safe and
#   lets the packing and unpacking be plain byte-slicing instead of shifting a
#   huge integer once per term.
#
#   slots have to stay non-negative or the borrow would corrupt the neighbour,
#   so each polynomial is split into its positive and negative parts and the
#   answer is recombined as (A+ - A-)(B+ - B-) -- four clean multiplications of
#   non-negative packings.
#
# time = O(N log N) via the big-int multiply, space = O(N)
class Solution(object):
    def multiply(self, poly1, poly2):
        n, m = len(poly1), len(poly2)
        size = n + m - 1
        W = 5                                  # bytes per slot, i.e. 40 bits

        def pack(coeffs, keep_positive):
            buf = bytearray(W * len(coeffs) + W)
            for i, v in enumerate(coeffs):
                v = v if keep_positive else -v
                if v > 0:
                    buf[W * i:W * i + W] = v.to_bytes(W, "little")
            return int.from_bytes(buf, "little")

        def unpack(x):
            raw = x.to_bytes(W * (size + 4), "little")
            return [int.from_bytes(raw[W * i:W * i + W], "little")
                    for i in range(size)]

        ap, an = pack(poly1, True), pack(poly1, False)
        bp, bn = pack(poly2, True), pack(poly2, False)
        pos = unpack(ap * bp + an * bn)        # the two like-sign products
        neg = unpack(ap * bn + an * bp)        # the two opposite-sign products
        return [pos[i] - neg[i] for i in range(size)]
