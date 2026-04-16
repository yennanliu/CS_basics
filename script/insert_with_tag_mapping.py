#!/usr/bin/env python3
"""
Insert missing problems with proper tag mapping.

Maps extracted PDF tags to markdown section headers.
"""

import re
import json
from pathlib import Path
from typing import Dict, List, Tuple
from collections import defaultdict


# Mapping from PDF tag names to markdown section headers
TAG_MAPPING = {
    'array': 'Array',
    'hash-table': 'Hash Table',
    'hash table': 'Hash Table',
    'string': 'String',
    'dynamic-programming': 'Dynamic Programming',
    'depth-first-search': 'Depth-First Search (DFS)',
    'dfs': 'Depth-First Search (DFS)',
    'breadth-first-search': 'Breadth-First Search (BFS)',
    'bfs': 'Breadth-First Search (BFS)',
    'binary-search': 'Binary Search',
    'stack': 'Stack',
    'queue': 'Queue',
    'tree': 'Tree',
    'binary-tree': 'Tree',
    'graph': 'Graph',
    'greedy': 'Greedy',
    'sorting': 'Sorting',
    'two-pointers': 'Two Pointers',
    'sliding-window': 'Sliding Window',
    'matrix': 'Matrix',
    'design': 'Design',
    'math': 'Math',
    'backtracking': 'Backtracking',
    'linked-list': 'Linked List',
    'union-find': 'Union Find',
    'bit-manipulation': 'Bit Manipulation',
    'divide-and-conquer': 'Divide and Conquer',
    'heap': 'Heap (Priority Queue)',
    'heap-priority-queue': 'Heap (Priority Queue)',
    'ordered-set': 'Ordered Set',
    'trie': 'Trie',
    'segment-tree': 'Segment Tree',
    'recursion': 'Recursion',
    'counting': 'Counting',
    'interactive': 'Interactive',
    'simulation': 'Simulation',
    'string-matching': 'String Matching',
    'rolling-hash': 'Rolling Hash',
    'monolithic-stack': 'Monotonic Stack',
    'combinatorics': 'Combinatorics',
    'geometry': 'Geometry',
    'line-sweep': 'Line Sweep',
    'memorization': 'Memorization',
    'enumeration': 'Enumeration',
}


def get_markdown_tags(md_file: str) -> List[str]:
    """Extract all tag headers from markdown."""
    with open(md_file, 'r', encoding='utf-8') as f:
        content = f.read()

    tags = re.findall(r'^## (.+?)\n', content, re.MULTILINE)
    return tags


def map_pdf_tags_to_markdown(pdf_tags: List[str], md_tags: List[str]) -> str:
    """
    Map PDF tags to markdown tags.
    Returns the best matching markdown tag.
    """
    if not pdf_tags:
        return None

    for pdf_tag in pdf_tags:
        pdf_tag_lower = pdf_tag.strip().lower()

        # Try exact mapping first
        if pdf_tag_lower in TAG_MAPPING:
            md_tag = TAG_MAPPING[pdf_tag_lower]
            if md_tag in md_tags:
                return md_tag

    # Try partial matching
    for pdf_tag in pdf_tags:
        pdf_tag_lower = pdf_tag.strip().lower()
        for md_tag in md_tags:
            if pdf_tag_lower in md_tag.lower() or md_tag.lower() in pdf_tag_lower:
                return md_tag

    # Default to None if no match found
    return None


def load_problem_details(json_file: str) -> Dict[int, Dict]:
    """Load problem details from JSON."""
    with open(json_file, 'r') as f:
        data = json.load(f)
    return {int(k): v for k, v in data.items()}


def load_markdown(md_file: str) -> str:
    """Load markdown content."""
    with open(md_file, 'r', encoding='utf-8') as f:
        return f.read()


def find_insertion_point(content: str, tag_name: str, difficulty: str) -> Tuple[int, bool]:
    """
    Find where to insert problem in markdown.
    Returns: (position, section_exists)
    """
    # Find tag section
    tag_pattern = rf'^## {re.escape(tag_name)}\n'
    tag_match = re.search(tag_pattern, content, re.MULTILINE)

    if not tag_match:
        return None, False

    tag_pos = tag_match.end()

    # Find next tag to limit search scope
    next_tag_pattern = r'^## '
    next_tag_match = re.search(next_tag_pattern, content[tag_pos:], re.MULTILINE)
    if next_tag_match:
        search_end = tag_pos + next_tag_match.start()
    else:
        search_end = len(content)

    # Find difficulty subsection
    diff_pattern = rf'^### {difficulty}\n'
    diff_match = re.search(diff_pattern, content[tag_pos:search_end], re.MULTILINE)

    if not diff_match:
        return tag_pos, False

    return tag_pos + diff_match.end(), True


def get_problems_in_range(content: str, start: int, end: int) -> List[int]:
    """Extract problem numbers in a given range."""
    section = content[start:end]
    matches = re.findall(r'- #(\d+)\s*-', section)
    return sorted([int(m) for m in matches])


def format_problem(num: int, title: str, tags: List[str], acceptance: str) -> str:
    """Format problem line."""
    title = title.strip()
    title = re.sub(r'&?\s*\.\.\.*$', '', title)

    # Deduplicate and clean tags
    seen = set()
    unique_tags = []
    for tag in tags:
        tag = tag.strip()
        if tag and tag not in seen:
            unique_tags.append(tag)
            seen.add(tag)

    if unique_tags and acceptance:
        return f"- #{num} - {title} - {', '.join(unique_tags)} - {acceptance}\n"
    elif unique_tags:
        return f"- #{num} - {title} - {', '.join(unique_tags)}\n"
    else:
        return f"- #{num} - {title}\n"


def main():
    """Main function."""
    script_dir = Path(__file__).parent.parent
    details_file = Path(__file__).parent / "missing_problems_details.json"
    md_file = script_dir / "doc/google_leetcode_problems_by_tags.md"

    print("📖 Loading data...")
    problems = load_problem_details(str(details_file))
    content = load_markdown(str(md_file))
    md_tags = get_markdown_tags(str(md_file))

    print(f"   Loaded {len(problems)} problems")
    print(f"   Found {len(md_tags)} tag sections in markdown")

    # Organize by mapped tag and difficulty
    by_mapped_tag = defaultdict(lambda: defaultdict(list))
    unmapped_count = 0

    for num, info in problems.items():
        pdf_tags = info.get('tags', [])
        difficulty = info.get('difficulty', 'Unknown').capitalize()
        title = info.get('title', 'Unknown')
        acceptance = info.get('acceptance', '')

        # Find best matching markdown tag
        mapped_tag = map_pdf_tags_to_markdown(pdf_tags, md_tags)

        if mapped_tag:
            by_mapped_tag[mapped_tag][difficulty].append({
                'num': num,
                'title': title,
                'tags': pdf_tags,
                'acceptance': acceptance
            })
        else:
            unmapped_count += 1

    print(f"   Successfully mapped {len(problems) - unmapped_count} problems")
    if unmapped_count > 0:
        print(f"   ⚠️  Could not map {unmapped_count} problems to markdown tags")

    # Create backup
    backup_file = md_file.with_suffix('.md.backup')
    with open(backup_file, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"\n✅ Backup created: {backup_file}")

    # Add problems to markdown
    print(f"\n📝 Adding problems to markdown...")
    added_count = 0
    skipped_count = 0

    for md_tag in sorted(by_mapped_tag.keys()):
        print(f"\n  Tag: {md_tag}")

        for difficulty in ['Easy', 'Medium', 'Hard']:
            if difficulty not in by_mapped_tag[md_tag]:
                continue

            tag_problems = by_mapped_tag[md_tag][difficulty]
            insert_pos, section_exists = find_insertion_point(content, md_tag, difficulty)

            if insert_pos is None:
                print(f"    ❌ {difficulty}: Tag not found, skipping {len(tag_problems)} problems")
                skipped_count += len(tag_problems)
                continue

            if not section_exists:
                print(f"    ⚠️  {difficulty}: Section doesn't exist, skipping {len(tag_problems)} problems")
                skipped_count += len(tag_problems)
                continue

            print(f"    ✅ {difficulty}: Adding {len(tag_problems)} problems")

            # Find where to insert each problem
            for problem_info in sorted(tag_problems, key=lambda x: x['num']):
                num = problem_info['num']
                title = problem_info['title']
                tags = problem_info['tags']
                acceptance = problem_info['acceptance']

                # Find next section boundary
                next_section_pattern = r'^### |^## '
                remaining = content[insert_pos:]
                next_match = re.search(next_section_pattern, remaining, re.MULTILINE)

                if next_match:
                    section_end = insert_pos + next_match.start()
                else:
                    section_end = len(content)

                existing_problems = get_problems_in_range(content, insert_pos, section_end)

                if num in existing_problems:
                    continue  # Already exists

                # Find correct insertion position (sorted by number)
                actual_pos = insert_pos
                for existing_num in existing_problems:
                    if existing_num > num:
                        # Find this problem's position
                        pattern = rf'- #{existing_num}\s*-'
                        match = re.search(pattern, content[insert_pos:section_end])
                        if match:
                            actual_pos = insert_pos + match.start()
                            break

                if actual_pos == insert_pos:
                    # Insert at end (after last problem)
                    last_problem_match = re.finditer(r'- #\d+\s*-[^\n]*\n', content[insert_pos:section_end])
                    for match in last_problem_match:
                        actual_pos = insert_pos + match.end()

                problem_line = format_problem(num, title, tags, acceptance)
                content = content[:actual_pos] + problem_line + content[actual_pos:]
                added_count += 1

    # Save updated markdown
    print(f"\n{'='*60}")
    if added_count > 0:
        with open(md_file, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"✅ Updated {md_file}")
        print(f"   Problems added: {added_count}")
        print(f"   Problems skipped: {skipped_count + unmapped_count}")
    else:
        print(f"⚠️  No problems were added")

    print(f"\n✨ Done!")


if __name__ == "__main__":
    main()
