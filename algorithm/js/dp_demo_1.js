//---------------------------------------------------------------
// DYNAMIC PROGRAMMING (1) -- memoization from first principles
//---------------------------------------------------------------
//
// Scope: what a memo IS, and how a closure gives each function its own
//        private cache. See fibonacci_dp.js for the same idea applied
//        to a genuinely expensive recursion, plus the bottom-up form.
//
// Dynamic programming = recursion + remembering. It applies when a
// problem has:
//   1) OVERLAPPING SUBPROBLEMS  -- the same call happens again and again
//   2) OPTIMAL SUBSTRUCTURE     -- the answer is built from the answers
//                                  to those subproblems
//
// MEMOIZATION is the top-down half: keep writing the natural recursion,
// but record each result the first time and return the record after
// that. It trades memory for time.
//
// The step below that matters most in practice is moving the cache
// from a GLOBAL into a CLOSURE. A global cache is shared by every
// caller, never freed, and impossible to test in isolation; a closure
// gives each memoized function its own.
//
// Time  : O(1) per repeat call once cached
// Space : O(number of distinct arguments)

// A stand-in for an expensive computation. `calls` lets the demo below
// prove the cache is actually doing something.
let calls = 0;
function slowAddTo80(n) {
  calls++;
  return n + 80;
}

//--- version 1: a global cache -- works, but leaks and cannot be reset
const globalCache = {};
function memoizedGlobal(n) {
  if (n in globalCache) return globalCache[n];
  globalCache[n] = slowAddTo80(n);
  return globalCache[n];
}

//--- version 2: the cache lives in a closure, private to the returned
//    function. Two memoized copies no longer share state.
function makeMemoizedAddTo80() {
  const cache = {}; // captured by the inner function, invisible outside
  return function (n) {
    if (n in cache) return cache[n];
    cache[n] = slowAddTo80(n);
    return cache[n];
  };
}

//--- version 3: memoize ANY single-argument pure function
//    NOTE this only works for pure functions of hashable arguments --
//    caching a function that depends on outside state returns stale
//    answers, which is the classic memoization bug.
function memoize(fn) {
  const cache = new Map();
  return function (arg) {
    if (cache.has(arg)) return cache.get(arg);
    const result = fn(arg);
    cache.set(arg, result);
    return result;
  };
}

// demo
calls = 0;
console.assert(memoizedGlobal(6) === 86 && calls === 1, 'first call computes');
console.assert(memoizedGlobal(6) === 86 && calls === 1, 'second call is cached');
console.assert(memoizedGlobal(7) === 87 && calls === 2, 'a new argument computes again');

// each closure gets its OWN cache
calls = 0;
const memoA = makeMemoizedAddTo80();
const memoB = makeMemoizedAddTo80();
console.assert(memoA(6) === 86 && calls === 1, 'memoA computes');
console.assert(memoA(6) === 86 && calls === 1, 'memoA is cached');
console.assert(memoB(6) === 86 && calls === 2, 'memoB has a separate cache');

// the generic wrapper
calls = 0;
const memoized = memoize(slowAddTo80);
console.assert(memoized(5) === 85 && calls === 1);
console.assert(memoized(5) === 85 && calls === 1, 'generic memoize caches too');

console.log('Success.');

module.exports = { memoize, makeMemoizedAddTo80 };
