#---------------------------------------------------------------
# MD5
#---------------------------------------------------------------
#
# A from-scratch implementation of the MD5 message-digest algorithm.
# Produces a 128-bit hash, rendered as a 32-character hex string.
#
# References:
#   - https://github.com/yennanliu/Python/blob/master/hashes/md5.py
#   - https://en.wikipedia.org/wiki/MD5


import math


def rearrange(bit_string_32):
    """Regroup a 32-bit binary string into little-endian byte order."""
    if len(bit_string_32) != 32:
        raise ValueError("Need length 32")
    new_string = ""
    for i in [3, 2, 1, 0]:
        new_string += bit_string_32[8 * i:8 * i + 8]
    return new_string


def reformat_hex(i):
    """Convert an integer into a little-endian 8-digit hex string."""
    hex_rep = format(i, "08x")
    result = ""
    for j in [3, 2, 1, 0]:
        result += hex_rep[2 * j:2 * j + 2]
    return result


def pad(bit_string):
    """Pad a binary string so its length is a multiple of 512 bits."""
    start_length = len(bit_string)
    bit_string += "1"
    while len(bit_string) % 512 != 448:
        bit_string += "0"
    last_part = format(start_length, "064b")
    bit_string += rearrange(last_part[32:]) + rearrange(last_part[:32])
    return bit_string


def get_block(bit_string):
    """Yield each 512-bit block as a list of sixteen 32-bit integers."""
    cur_pos = 0
    while cur_pos < len(bit_string):
        cur_part = bit_string[cur_pos:cur_pos + 512]
        splits = []
        for i in range(16):
            splits.append(int(rearrange(cur_part[32 * i:32 * i + 32]), 2))
        yield splits
        cur_pos += 512


def not32(i):
    i_str = format(i, "032b")
    new_str = "".join("1" if c == "0" else "0" for c in i_str)
    return int(new_str, 2)


def sum32(a, b):
    return (a + b) % 2 ** 32


def leftrot32(i, s):
    return (i << s) ^ (i >> (32 - s))


def md5me(test_string):
    """Return the 32-character hex MD5 digest of test_string."""
    bs = ""
    for char in test_string:
        bs += format(ord(char), "08b")
    bs = pad(bs)

    tvals = [int(2 ** 32 * abs(math.sin(i + 1))) for i in range(64)]

    a0 = 0x67452301
    b0 = 0xEFCDAB89
    c0 = 0x98BADCFE
    d0 = 0x10325476

    s = [7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
         5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
         4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
         6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21]

    for m in get_block(bs):
        a = a0
        b = b0
        c = c0
        d = d0
        for i in range(64):
            if i <= 15:
                f = d ^ (b & (c ^ d))
                g = i
            elif i <= 31:
                f = c ^ (d & (b ^ c))
                g = (5 * i + 1) % 16
            elif i <= 47:
                f = b ^ c ^ d
                g = (3 * i + 5) % 16
            else:
                f = c ^ (b | not32(d))
                g = (7 * i) % 16
            d_temp = d
            d = c
            c = b
            b = sum32(b, leftrot32((a + f + tvals[i] + m[g]) % 2 ** 32, s[i]))
            a = d_temp
        a0 = sum32(a0, a)
        b0 = sum32(b0, b)
        c0 = sum32(c0, c)
        d0 = sum32(d0, d)

    return reformat_hex(a0) + reformat_hex(b0) + reformat_hex(c0) + reformat_hex(d0)


if __name__ == "__main__":
    assert md5me("") == "d41d8cd98f00b204e9800998ecf8427e"
    assert md5me("The quick brown fox jumps over the lazy dog") == \
        "9e107d9d372bb6826bd81d3542a419d6"
    print("Success.")
