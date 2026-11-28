#---------------------------------------------------------------
# MD5
#---------------------------------------------------------------
#
# A from-scratch implementation of the MD5 message-digest algorithm.
# Produces a 128-bit hash, rendered as a 32-character hex string.
#
# A HASH FUNCTION turns input of any length into a fixed-size
# fingerprint: the same input always gives the same digest, and a
# one-character change gives a completely different one.
#
#     md5("")      = d41d8cd98f00b204e9800998ecf8427e
#     md5("a")     = 0cc175b9c0f1b6a831c399e269772661
#     md5("b")     = 92eb5ffee6ae2fec3ad71c777531578f
#
# THE SHAPE OF THE ALGORITHM
#   1) PAD the message to a multiple of 512 bits: a single 1 bit, then
#      0 bits, then the original length as a 64-bit number
#   2) keep a 128-bit STATE: four 32-bit words a0, b0, c0, d0
#   3) for each 512-bit BLOCK, run 64 rounds that mix one 32-bit piece
#      of the block into the state (4 groups of 16 rounds, each group
#      with its own bitwise function), then add the result to the state
#   4) the digest is the final state, written out little-endian
#
# This file works on STRINGS OF "0" / "1" CHARACTERS rather than
# integers, so every step can be printed and read. That makes it slow
# -- it is for understanding, not for use.
#
# DO NOT USE MD5 FOR SECURITY. Collisions (two inputs with the same
# digest) can be produced in seconds, so it is broken for signatures,
# certificates and password storage. It survives as a checksum against
# ACCIDENTAL corruption. In real code: hashlib.md5 for checksums,
# hashlib.sha256 when it matters, and a dedicated password hash
# (bcrypt, scrypt, argon2) for passwords.
#
# Time  : O(N) in the length of the message
# Space : O(N) here (the bit string); O(1) in a streaming implementation
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
    """Bitwise NOT of a 32-bit value (flip every one of the 32 bits)."""
    i_str = format(i, "032b")
    new_str = "".join("1" if c == "0" else "0" for c in i_str)
    return int(new_str, 2)


def sum32(a, b):
    """Add two 32-bit values, wrapping around on overflow like a CPU register."""
    return (a + b) % 2 ** 32


def leftrot32(i, s):
    """Rotate a 32-bit value left by s bits: bits pushed off the top re-enter at the bottom."""
    return (i << s) ^ (i >> (32 - s))


def md5me(test_string):
    """Return the 32-character hex MD5 digest of test_string."""
    # the message as a string of bits, 8 per byte. Encoding to UTF-8
    # first matters for non-ASCII text: "é" is TWO bytes, not one.
    bs = ""
    for byte in test_string.encode("utf-8"):
        bs += format(byte, "08b")
    bs = pad(bs)

    # 64 round constants: the integer part of 2^32 * |sin(i + 1)|.
    # Derived from a formula so nobody can suspect a hidden trapdoor.
    tvals = [int(2 ** 32 * abs(math.sin(i + 1))) for i in range(64)]

    # the initial state: fixed by the standard
    a0 = 0x67452301
    b0 = 0xEFCDAB89
    c0 = 0x98BADCFE
    d0 = 0x10325476

    # how far to rotate in each of the 64 rounds
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
            # f mixes three of the state words; g picks which of the
            # block's sixteen 32-bit pieces this round consumes
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
    assert md5me("a") == "0cc175b9c0f1b6a831c399e269772661"
    assert md5me("The quick brown fox jumps over the lazy dog") == \
        "9e107d9d372bb6826bd81d3542a419d6"

    # one character changes the whole digest
    assert md5me("The quick brown fox jumps over the lazy dog.") == \
        "e4d909c290d0fb1ca068ffaddf22cbd0"

    # the helpers
    assert sum32(0xFFFFFFFF, 1) == 0                     # wraps around
    assert not32(0) == 0xFFFFFFFF
    assert leftrot32(0x80000000, 1) & 0xFFFFFFFF == 1    # the top bit comes back in at the bottom
    assert len(pad("")) == 512 and len(pad("1" * 448)) == 1024

    # same digest as the standard library, including the lengths around
    # the 448-bit padding boundary (55, 56, 57 bytes) and non-ASCII text
    import hashlib

    samples = ["x" * n for n in (1, 54, 55, 56, 57, 63, 64, 65, 120)]
    samples += ["héllo wörld", "漢字", "line one\nline two"]
    for text in samples:
        assert md5me(text) == hashlib.md5(text.encode("utf-8")).hexdigest()
    print("Success.")
