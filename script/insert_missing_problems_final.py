#!/usr/bin/env python3
"""
Insert missing LeetCode problems into markdown file.

This script:
1. Reads extracted problem details
2. Finds correct section in markdown (by tag and difficulty)
3. Inserts problems maintaining proper sorting
4. Creates backup of original file

Usage:
    python3 insert_missing_problems_final.py
"""

import re
import json
from pathlib import Path
from typing import Dict, List, Tuple
from collections import defaultdict
from datetime import datetime


def load_problem_details(json_file: str) -> Dict[int, Dict]:
    """Load problem details from JSON."""
    with open(json_file, 'r') as f:
        data = json.load(f)
    # Convert string keys back to ints
    return {int(k): v for k, v in data.items()}


def load_markdown_content(md_file: str) -> str:
    """Load markdown file content."""
    with open(md_file, 'r', encoding='utf-8') as f:
        return f.read()


def extract_markdown_problems_with_location(content: str) -> Dict[int, Tuple[int, int]]:
    """
    Extract problem numbers and their positions in markdown.
    Returns: {problem_num: (start_pos, end_pos)}
    """
    problems = {}
    pattern = r'- #(\d+)\s*-'

    for match in re.finditer(pattern, content):
        num = int(match.group(1))
        problems[num] = (match.start(), match.end())

    return problems


def parse_tag_sections(content: str) -> Dict[str, int]:
    """Find all tag section headers and their positions."""
    tag_positions = {}
    pattern = r'^## (.+)\n'

    for match in re.finditer(pattern, content, re.MULTILINE):
        tag_name = match.group(1).strip()
        tag_positions[tag_name] = match.end()

    return tag_positions


def find_difficulty_section_position(content: str, tag_pos: int, tag_name: str,
                                     difficulty: str) -> Tuple[int, bool]:
    """
    Find position to insert problems in a tag's difficulty section.
    Returns: (position, section_exists)
    """
    # Find the next tag section to limit search
    next_tag_pattern = r'^## '
    next_tag_match = re.search(next_tag_pattern, content[tag_pos:], re.MULTILINE)

    if next_tag_match:
        search_end = tag_pos + next_tag_match.start()
    else:
        search_end = len(content)

    # Search for difficulty header
    difficulty_pattern = rf'^### {difficulty}\n'
    difficulty_match = re.search(difficulty_pattern, content[tag_pos:search_end], re.MULTILINE)

    if not difficulty_match:
        # Difficulty section doesn't exist
        return tag_pos, False

    # Position right after the "### Difficulty" header
    section_start = tag_pos + difficulty_match.end()
    return section_start, True


def get_problems_in_section(content: str, start: int, end: int) -> List[int]:
    """Extract problem numbers from a section."""
    section_content = content[start:end]
    pattern = r'- #(\d+)\s*-'
    problems = [int(m.group(1)) for m in re.finditer(pattern, section_content)]
    return sorted(problems)


def format_problem_line(num: int, info: Dict) -> str:
    """Format a problem for markdown insertion."""
    title = info.get('title', 'Unknown').strip()
    # Clean up title (remove HTML truncation like "...&")
    title = re.sub(r'&?\s*\.\.\.*$', '', title)

    tags = info.get('tags', [])
    if tags:
        # Remove duplicates while preserving order
        seen = set()
        unique_tags = []
        for tag in tags:
            tag = tag.strip()
            if tag not in seen:
                unique_tags.append(tag)
                seen.add(tag)
        tags_str = ', '.join(unique_tags)
    else:
        tags_str = ''

    acceptance = info.get('acceptance', '').strip()

    if tags_str and acceptance:
        return f"- #{num} - {title} - {tags_str} - {acceptance}\n"
    elif tags_str:
        return f"- #{num} - {title} - {tags_str}\n"
    else:
        return f"- #{num} - {title}\n"


def insert_problem_in_section(content: str, insert_pos: int, problem_num: int,
                             info: Dict, existing_problems: List[int]) -> str:
    """Insert a problem in the correct position (maintaining sort order)."""
    # Find where to insert this problem number among existing ones
    # Get all problems in the section from insert_pos onwards
    line_start = content.rfind('\n', 0, insert_pos)
    if line_start == -1:
        line_start = 0
    else:
        line_start += 1

    # Find next problem line or end of problems
    next_problem_pattern = r'- #\d+\s*-'
    remaining_content = content[insert_pos:]
    match = re.search(next_problem_pattern, remaining_content)

    if match:
        # Insert before the next problem if our number should come first
        next_problem_line = remaining_content[:match.start()]
        actual_insert_pos = insert_pos

        # Check existing problems to find correct sorted position
        for existing_num in existing_problems:
            if existing_num > problem_num:
                # Find this existing problem's position
                pattern = rf'- #{existing_num}\s*-'
                pos_match = re.search(pattern, content[insert_pos:])
                if pos_match:
                    actual_insert_pos = insert_pos + pos_match.start()
                    break
        else:
            # This number should go at the end
            # Find last problem before next section
            last_problem_match = None
            for existing_num in sorted(existing_problems, reverse=True):
                pattern = rf'- #{existing_num}\s*-'
                pos_match = re.search(pattern, content[insert_pos:])
                if pos_match:
                    # Find end of this line
                    end_of_line = content.find('\n', insert_pos + pos_match.end())
                    if end_of_line != -1:
                        actual_insert_pos = end_of_line + 1
                    break
    else:
        actual_insert_pos = insert_pos

    problem_line = format_problem_line(problem_num, info)
    return content[:actual_insert_pos] + problem_line + content[actual_insert_pos:]


def insert_difficulty_section(content: str, insert_pos: int, difficulty: str,
                             problems: List[Tuple[int, Dict]]) -> str:
    """Create a new difficulty section with problems."""
    lines = [f"### {difficulty}\n"]

    # Sort problems by number
    problems.sort(key=lambda x: x[0])

    for num, info in problems:
        lines.append(format_problem_line(num, info))

    lines.append("\n")

    section_text = ''.join(lines)
    return content[:insert_pos] + section_text + content[insert_pos:]


def main():
    """Main function."""
    script_dir = Path(__file__).parent.parent
    details_file = Path(__file__).parent / "missing_problems_details.json"
    md_file = script_dir / "doc/google_leetcode_problems_by_tags.md"

    print("📖 Loading problem details...")
    problems = load_problem_details(str(details_file))
    print(f"   Loaded {len(problems)} problems")

    print("\n📄 Loading markdown file...")
    content = load_markdown_content(str(md_file))
    original_content = content
    print(f"   File size: {len(content)} bytes")

    # Create backup
    backup_file = md_file.with_suffix('.md.backup')
    with open(backup_file, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"✅ Backup created: {backup_file}")

    # Organize problems by tags and difficulty
    by_tag_difficulty = defaultdict(lambda: defaultdict(list))
    for num, info in problems.items():
        tags = info.get('tags', [])
        difficulty = info.get('difficulty', 'Unknown').capitalize()

        # Use first tag as primary
        if tags:
            primary_tag = tags[0].strip().capitalize()
            # Format tag name to match markdown (capitalize words)
            primary_tag = ' '.join(word.capitalize() for word in primary_tag.split('-'))
            by_tag_difficulty[primary_tag][difficulty].append((num, info))

    print(f"\n📋 Processing {len(problems)} problems...")
    print(f"   Tags to update: {len(by_tag_difficulty)}")

    # For each tag and difficulty, find the section and add problems
    problems_added = 0
    problems_skipped = 0

    for tag in sorted(by_tag_difficulty.keys()):
        print(f"\n🔍 Processing tag: {tag}")

        for difficulty in ['Easy', 'Medium', 'Hard']:
            if difficulty not in by_tag_difficulty[tag]:
                continue

            tag_problems = by_tag_difficulty[tag][difficulty]
            print(f"  - {difficulty}: {len(tag_problems)} problems")

            # Find the tag section in markdown
            tag_pattern = rf'^## {re.escape(tag)}\n'
            tag_match = re.search(tag_pattern, content, re.MULTILINE)

            if not tag_match:
                print(f"    ⚠️  Tag '{tag}' not found in markdown, skipping")
                problems_skipped += len(tag_problems)
                continue

            tag_pos = tag_match.end()

            # Find or create difficulty section
            diff_pattern = rf'^### {difficulty}\n'
            diff_match = re.search(diff_pattern, content[tag_pos:], re.MULTILINE)

            if not diff_match:
                print(f"    ⚠️  Difficulty '{difficulty}' not found for tag '{tag}', skipping")
                problems_skipped += len(tag_problems)
                continue

            section_start = tag_pos + diff_match.end()

            # Get existing problems in this section
            section_end_pattern = r'^### |^## '
            next_section = re.search(section_end_pattern, content[section_start:], re.MULTILINE)
            if next_section:
                section_end = section_start + next_section.start()
            else:
                section_end = len(content)

            existing_problems = get_problems_in_section(content, section_start, section_end)

            # Add each problem
            for num, info in sorted(tag_problems, key=lambda x: x[0]):
                if num not in existing_problems:
                    # Find insertion position
                    insert_pos = section_start
                    for existing_num in existing_problems:
                        if existing_num > num:
                            # Find this problem's position and insert before it
                            pattern = rf'- #{existing_num}\s*-'
                            match = re.search(pattern, content[section_start:section_end])
                            if match:
                                insert_pos = section_start + match.start()
                                break
                    else:
                        # Insert at end of section
                        insert_pos = section_end

                    problem_line = format_problem_line(num, info)
                    content = content[:insert_pos] + problem_line + content[insert_pos:]
                    existing_problems.append(num)
                    existing_problems.sort()
                    section_end += len(problem_line)
                    problems_added += 1

    print(f"\n{'='*60}")
    print(f"✅ Insertion complete!")
    print(f"   Problems added: {problems_added}")
    print(f"   Problems skipped: {problems_skipped}")
    print(f"   Total: {problems_added + problems_skipped}")

    # Save updated markdown
    if problems_added > 0:
        with open(md_file, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"\n💾 Updated markdown file: {md_file}")
        print(f"   Original size: {len(original_content)} bytes")
        print(f"   New size: {len(content)} bytes")
        print(f"   Added: {len(content) - len(original_content)} bytes")
    else:
        print("\n⚠️  No problems were added (all tags/difficulties may not exist in markdown)")

    print(f"\n✅ Done! {problems_added} problems added to markdown.")


if __name__ == "__main__":
    main()
