#!/usr/bin/env python3
"""
The one LeetCode GraphQL client in script/.

    from lc_graphql import post, GraphQLError
    data = post(query, {"slug": "two-sum"})          # -> the response's `data`

Five scripts used to build their own request, each with its own headers and its
own idea of failure: two returned None, one raised, and two never looked at the
`errors` field, so a rejected query surfaced later as a KeyError on `data`.
LeetCode answers a rejected query with HTTP 200 and `{"errors": [...], "data":
null}`, so a 200 is not a success until the payload says so.

`post()` has one contract: it returns `data` (a dict) or raises. A network
failure re-raises the original exception (`urllib.error.URLError`,
`TimeoutError`, ...) once the tries are spent; a response that is not a usable
GraphQL result — a rejected query, or an HTML page where JSON should be —
raises `GraphQLError`. A caller that wants None on failure
catches both — see `scrape_lc_discuss_company.gql`.

Retries are opt-in (`tries`, `backoff`): the bulk scraper needs them to ride out
LeetCode's WAF, a one-shot report does not. HTTP 400 is never retried — the
query itself is wrong.
"""

import json
import sys
import time
import urllib.error
import urllib.request

GRAPHQL_URL = "https://leetcode.com/graphql/"
# A browser UA: LeetCode's WAF answers a bare "Mozilla/5.0" or a script UA with
# an HTML challenge page more often.
USER_AGENT = (
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
)


class GraphQLError(RuntimeError):
    """A response that is not a usable GraphQL result (or a query LeetCode rejected)."""


def headers(referer="https://leetcode.com/"):
    return {
        "Content-Type": "application/json",
        "Accept": "*/*",
        "Accept-Language": "en-US,en;q=0.9",
        "Origin": "https://leetcode.com",
        "Referer": referer,
        "User-Agent": USER_AGENT,
    }


def check(payload):
    """A decoded response -> its `data`, or GraphQLError."""
    if not isinstance(payload, dict):
        raise GraphQLError(f"expected a JSON object, got {type(payload).__name__}")
    if payload.get("errors"):
        first = payload["errors"][0]
        msg = first.get("message", first) if isinstance(first, dict) else first
        raise GraphQLError(f"GraphQL error: {msg}")
    if payload.get("data") is None:
        raise GraphQLError("GraphQL response carried no data")
    return payload["data"]


def post(query, variables=None, *, referer="https://leetcode.com/", timeout=30,
         tries=1, backoff=None, quiet=False):
    """POST one query; return its `data` or raise (see the module docstring).

    `backoff(attempt, exc) -> seconds` is called between tries; attempt is
    0-based. Without it there is no sleep.
    """
    body = json.dumps({"query": query, "variables": variables or {}}).encode()
    for attempt in range(tries):
        try:
            req = urllib.request.Request(GRAPHQL_URL, data=body, headers=headers(referer))
            with urllib.request.urlopen(req, timeout=timeout) as resp:
                payload = json.loads(resp.read().decode())
        except urllib.error.HTTPError as exc:
            if exc.code == 400:
                # Malformed query: retrying cannot help.
                detail = exc.read().decode(errors="replace")[:300]
                raise GraphQLError(f"HTTP 400 (bad query): {detail}") from exc
            last = exc
        except ValueError as exc:
            # the WAF answering with an HTML page instead of JSON: retryable,
            # and if it persists it is not a usable result -> GraphQLError
            last = GraphQLError(f"response was not JSON ({exc})")
        except OSError as exc:      # URLError, TimeoutError, a reset socket
            last = exc
        else:
            return check(payload)

        if attempt + 1 < tries:
            if not quiet:
                print(f"    {type(last).__name__}: {last}; retrying", file=sys.stderr)
            if backoff:
                time.sleep(backoff(attempt, last))
    raise last
