#!/usr/bin/env python3
"""
Extract missing LeetCode problems from PDF and add them to markdown file.

This script:
1. Reads the PDF and extracts problem details
2. Finds missing problems compared to markdown
3. Organizes them by tags and difficulty
4. Adds them to the correct sections in markdown
5. Maintains proper formatting and sorting

Usage:
    python3 add_missing_problems_to_md.py
"""

import re
import sys
from pathlib import Path
from typing import Set, Dict, List, Tuple
from collections import defaultdict
import json


def extract_pdf_problems_with_details(pdf_file: str) -> Dict[int, Dict]:
    """
    Extract all problems from PDF with full details.
    Returns dict: {problem_num: {title, tags, acceptance, difficulty, frequency}}
    """
    try:
        import fitz
    except ImportError:
        print("❌ PyMuPDF not installed. Install with: pip3 install pymupdf")
        return {}

    problems = {}

    try:
        with fitz.open(pdf_file) as doc:
            full_text = ""
            for page in doc:
                full_text += page.get_text()

            # Parse the problem table entries
            # Pattern: Problem number followed by title, tags, acceptance, difficulty, frequency
            lines = full_text.split('\n')

            i = 0
            while i < len(lines):
                line = lines[i].strip()

                # Check if line starts with a problem number (3-4 digits)
                if re.match(r'^(\d{3,4})$', line) or re.match(r'^(\d{3,4})\s', line):
                    match = re.match(r'^(\d{3,4})', line)
                    if match:
                        problem_num = int(match.group(1))

                        # Collect next lines for problem details
                        problem_info = {
                            'num': problem_num,
                            'title': '',
                            'tags': [],
                            'acceptance': '',
                            'difficulty': '',
                            'frequency': ''
                        }

                        # Get title from next line(s)
                        if i + 1 < len(lines):
                            title_line = lines[i + 1].strip()
                            # Title might span multiple lines
                            if title_line and not re.match(r'^[A-Z]', title_line.lower()) or title_line:
                                problem_info['title'] = title_line

                        # Try to extract info from following lines
                        j = i + 1
                        temp_lines = []
                        while j < min(i + 20, len(lines)) and j - i < 15:
                            temp_lines.append(lines[j].strip())
                            j += 1

                        combined_text = ' '.join(temp_lines)

                        # Extract tags (text between parentheses)
                        tag_matches = re.findall(r'/tag/([^)]+)\)', combined_text)
                        if tag_matches:
                            problem_info['tags'] = tag_matches

                        # Extract difficulty (Easy, Medium, Hard)
                        difficulty_match = re.search(r'\b(Easy|Medium|Hard)\b', combined_text, re.IGNORECASE)
                        if difficulty_match:
                            problem_info['difficulty'] = difficulty_match.group(1)

                        # Extract acceptance percentage
                        acceptance_match = re.search(r'(\d+\.?\d*)%', combined_text)
                        if acceptance_match:
                            problem_info['acceptance'] = acceptance_match.group(0)

                        # Extract frequency
                        freq_match = re.search(r'(?:Frequency|frequency):\s*(\w+)', combined_text)
                        if freq_match:
                            problem_info['frequency'] = freq_match.group(1)

                        if problem_info['title'] and problem_info['tags']:
                            problems[problem_num] = problem_info

                i += 1

    except Exception as e:
        print(f"❌ Error reading PDF: {e}")
        return {}

    return problems


def extract_markdown_problems(md_file: str) -> Set[int]:
    """Extract all problem numbers currently in markdown."""
    problems = set()

    with open(md_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find all #123 patterns
    matches = re.findall(r'#(\d+)\s*-', content)
    problems.update(int(m) for m in matches)

    return problems


def find_missing_problems(pdf_problems: Dict[int, Dict], md_problems: Set[int]) -> Dict[int, Dict]:
    """Find problems in PDF but not in markdown."""
    missing = {}
    for num, info in pdf_problems.items():
        if num not in md_problems:
            missing[num] = info
    return missing


def organize_by_tags_and_difficulty(problems: Dict[int, Dict]) -> Dict[str, Dict[str, List[Tuple]]]:
    """
    Organize problems by tags and difficulty.
    Returns: {tag: {difficulty: [(num, title, acceptance), ...]}}
    """
    organized = defaultdict(lambda: defaultdict(list))

    for num, info in problems.items():
        title = info.get('title', 'Unknown')
        acceptance = info.get('acceptance', '')
        difficulty = info.get('difficulty', 'Unknown').capitalize()
        tags = info.get('tags', [])

        # Each problem might have multiple tags
        for tag in tags:
            tag_name = tag.strip()
            if tag_name:
                organized[tag_name][difficulty].append((num, title, acceptance))

    # Sort within each difficulty
    for tag in organized:
        for difficulty in organized[tag]:
            organized[tag][difficulty].sort(key=lambda x: x[0])

    return organized


def format_problem_entry(num: int, title: str, acceptance: str, tags: List[str]) -> str:
    """Format a problem entry for markdown."""
    tags_str = ', '.join(tags)
    if acceptance:
        return f"- #{num} - {title} - {tags_str} - {acceptance}"
    else:
        return f"- #{num} - {title} - {tags_str}"


def load_markdown_structure(md_file: str) -> Dict[str, str]:
    """Load and parse the markdown file structure."""
    with open(md_file, 'r', encoding='utf-8') as f:
        content = f.read()
    return {'content': content}


def find_section_position(content: str, tag: str, difficulty: str) -> Tuple[int, bool]:
    """
    Find the position where a new problem should be inserted.
    Returns (position, section_exists)
    """
    # Look for the tag header
    tag_pattern = rf'^## {re.escape(tag)}\n'
    tag_match = re.search(tag_pattern, content, re.MULTILINE)

    if not tag_match:
        return -1, False

    tag_start = tag_match.start()

    # Look for difficulty subsection
    difficulty_pattern = rf'^### {difficulty}\n'
    difficulty_match = re.search(difficulty_pattern, content[tag_start:], re.MULTILINE)

    if not difficulty_match:
        # Difficulty section doesn't exist, need to create it
        return tag_start + len(tag_match.group(0)), False

    section_start = tag_start + tag_match.end() + difficulty_match.start() + len(difficulty_match.group(0))

    # Find the end of this difficulty section
    # (either next "###" or next "##" or end of file)
    next_section = re.search(r'\n(?:### |## )', content[section_start:])
    if next_section:
        section_end = section_start + next_section.start()
    else:
        section_end = len(content)

    return section_start, section_end, True


def print_summary(missing_problems: Dict[int, Dict]):
    """Print summary of problems to be added."""
    print("\n📊 Summary of Missing Problems to Add:")
    print(f"Total: {len(missing_problems)}")

    # Group by difficulty
    by_difficulty = defaultdict(list)
    for num, info in missing_problems.items():
        difficulty = info.get('difficulty', 'Unknown').capitalize()
        by_difficulty[difficulty].append(num)

    for difficulty in ['Easy', 'Medium', 'Hard']:
        if difficulty in by_difficulty:
            nums = sorted(by_difficulty[difficulty])
            print(f"\n{difficulty}: {len(nums)} problems")
            print(f"  Range: #{nums[0]} - #{nums[-1]}")

    # Group by primary tag
    by_tag = defaultdict(int)
    for num, info in missing_problems.items():
        tags = info.get('tags', [])
        if tags:
            by_tag[tags[0]] += 1

    print(f"\n📁 By Primary Tag:")
    for tag in sorted(by_tag.keys()):
        print(f"  {tag}: {by_tag[tag]} problems")


def main():
    """Main function."""
    script_dir = Path(__file__).parent.parent
    pdf_file = script_dir / "doc/leetcode_company_V6/LC_goog_all.pdf"
    md_file = script_dir / "doc/google_leetcode_problems_by_tags.md"

    print("🔍 Extracting problem details from PDF...")
    pdf_problems = extract_pdf_problems_with_details(str(pdf_file))
    print(f"   Found {len(pdf_problems)} problems in PDF")

    print("\n📖 Parsing markdown file...")
    md_problems = extract_markdown_problems(str(md_file))
    print(f"   Found {len(md_problems)} problems in markdown")

    print("\n🔎 Finding missing problems...")
    missing = find_missing_problems(pdf_problems, md_problems)
    print(f"   Found {len(missing)} missing problems")

    # Print summary
    print_summary(missing)

    # Save missing problems details to JSON for reference
    missing_json_file = Path(__file__).parent / "missing_problems_details.json"
    with open(missing_json_file, 'w') as f:
        json.dump(
            {str(k): v for k, v in missing.items()},
            f,
            indent=2
        )
    print(f"\n💾 Saved problem details to: {missing_json_file}")

    # Organize by tags and difficulty
    print("\n📋 Organizing by tags and difficulty...")
    organized = organize_by_tags_and_difficulty(missing)

    print("\n✅ Ready to add problems to markdown!")
    print(f"   Script can now format these for insertion...")

    # Save organized structure
    org_file = Path(__file__).parent / "missing_problems_organized.json"
    with open(org_file, 'w') as f:
        # Convert defaultdict to regular dict for JSON serialization
        org_dict = {}
        for tag, difficulties in organized.items():
            org_dict[tag] = {}
            for difficulty, problems_list in difficulties.items():
                org_dict[tag][difficulty] = problems_list
        json.dump(org_dict, f, indent=2)
    print(f"   Saved organized structure to: {org_file}")


if __name__ == "__main__":
    main()
