//---------------------------------------------------------------
// DYNAMIC PROGRAMMING (2) -- Fibonacci, three ways
//---------------------------------------------------------------
//
// Scope: the same problem solved naively, top-down, and bottom-up, so
//        the cost of each is directly comparable. See dp_demo_1.js for
//        what memoization is.
//
//   0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, ...
//   fib(n) = fib(n-1) + fib(n-2)
//
// WHY THE NAIVE VERSION IS SO BAD -- the call tree re-solves the same
// subproblems over and over:
//
//               fib(5)
//              /      \
//         fib(4)       fib(3)      <- fib(3) computed twice
//          /     \       /    \
//     fib(3)  fib(2)  fib(2)  fib(1)   <- fib(2) computed three times
//
// The tree roughly doubles each level, so it is O(2^N). There are only
// N distinct subproblems, though -- which is exactly the "overlapping
// subproblems" signal that DP applies.
//
//   naive       O(2^N) time,  O(N) stack     fib(35) ~ 30M calls
//   top-down    O(N) time,    O(N) space     memoized recursion
//   bottom-up   O(N) time,    O(1) space     a loop and two variables
//
// Bottom-up is the one to reach for: no recursion limit, no cache, and
// because fib(n) only ever looks back two steps, the whole table
// collapses to two variables.

//--- 1) naive recursion: O(2^N) --------------------------------------
function fibonacci(n) {
  if (n < 2) return n;
  return fibonacci(n - 1) + fibonacci(n - 2);
}

//--- 2) top-down (memoized recursion): O(N) --------------------------
// The cache lives in a closure, so each returned function has its own.
function makeMemoFibonacci() {
  const cache = {};
  let calls = 0;

  const fib = (n) => {
    calls++;
    if (n in cache) return cache[n];
    if (n < 2) return n;
    cache[n] = fib(n - 1) + fib(n - 2);
    return cache[n];
  };

  fib.callCount = () => calls;
  return fib;
}

//--- 3) bottom-up (tabulation): O(N) time, O(1) space ----------------
// Start from the base cases and build up. Only the last two values are
// ever needed, so no array is required at all.
function fibonacciBottomUp(n) {
  if (n < 2) return n;

  let previous = 0; // fib(0)
  let current = 1;  // fib(1)
  for (let i = 2; i <= n; i++) {
    [previous, current] = [current, previous + current];
  }
  return current;
}

// demo
const expected = [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55];

// all three agree on the small cases
const memoFib = makeMemoFibonacci();
for (let n = 0; n < expected.length; n++) {
  console.assert(fibonacci(n) === expected[n], `naive fib(${n})`);
  console.assert(memoFib(n) === expected[n], `memoized fib(${n})`);
  console.assert(fibonacciBottomUp(n) === expected[n], `bottom-up fib(${n})`);
}

// the DP versions reach sizes the naive one never could
console.assert(fibonacciBottomUp(50) === 12586269025, 'fib(50), bottom-up');
console.assert(makeMemoFibonacci()(50) === 12586269025, 'fib(50), memoized');

// memoization turns exponential work into linear work
const counted = makeMemoFibonacci();
counted(30);
console.assert(counted.callCount() < 100, `fib(30) took ${counted.callCount()} calls, not ~2.7M`);

// NOTE beyond fib(78) the result exceeds Number.MAX_SAFE_INTEGER and
// silently loses precision -- use BigInt if you need exact values.
console.assert(fibonacciBottomUp(78) === 8944394323791464, 'largest exact value');

console.log('naive     fib(30) =', fibonacci(30));
console.log('memoized  fib(50) =', makeMemoFibonacci()(50));
console.log('bottom-up fib(50) =', fibonacciBottomUp(50));
console.log('memoized fib(30) took', counted.callCount(), 'calls instead of ~2.7M');
console.log('Success.');

module.exports = { fibonacci, makeMemoFibonacci, fibonacciBottomUp };
