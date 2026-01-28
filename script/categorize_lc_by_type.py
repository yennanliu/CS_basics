#!/usr/bin/env python3
"""
Categorize LeetCode Problems by Type

This script extracts unique LeetCode problems from data/progress.md for a specified
year range and categorizes them based on the directory structure in leetcode_python/
and leetcode_java/.

Usage:
    python3 script/categorize_lc_by_type.py [year_start] [year_end]

    Default: 2025-2026

Example:
    python3 script/categorize_lc_by_type.py 2025 2026

Output:
    data/LC_Practice_{year_start}_{year_end}_By_Category.md
"""

import re
import os
import sys
from pathlib import Path
from collections import defaultdict


def extract_lc_problems(file_path, year_start=2025, year_end=2026):
    """Extract unique LC problems from progress.md for specified year range"""
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    lc_problems = set()
    in_range = False
    year_pattern = '|'.join(str(y) for y in range(year_start, year_end + 1))

    for line in lines:
        # Check if we're in the specified year range
        if re.match(rf'^# ({year_pattern})', line):
            in_range = True
            continue
        # Stop when we reach years outside the range
        if re.match(r'^# (19|20)\d{2}', line):
            year_match = re.match(r'^# (\d{4})', line)
            if year_match:
                year = int(year_match.group(1))
                if year < year_start or year > year_end:
                    in_range = False
            continue

        if in_range:
            # Match LC problem format: "LC 123" or "LC 003"
            lc_match = re.search(r'LC\s+(\d+)', line)
            if lc_match:
                lc_problems.add(int(lc_match.group(1)))
                continue

            # Extract standalone numbers (with indentation)
            num_line = re.search(r'^\s*[-\t]+\s*(\d+(?:\s*,\s*\d+)*)', line)
            if num_line:
                nums_str = num_line.group(1)
                for num_str in re.findall(r'\d+', nums_str):
                    num = int(num_str)
                    if 1 <= num <= 3500:
                        lc_problems.add(num)
                continue

            # Match numbers with just tabs/spaces (no dash)
            num_only = re.match(r'^\s+(\d+)', line)
            if num_only:
                num = int(num_only.group(1))
                if 1 <= num <= 3500:
                    lc_problems.add(num)

    return sorted(lc_problems)


def scan_directory_for_problems(base_dir, problem_set, file_ext):
    """Scan a directory for problem files and extract problem numbers"""
    problem_to_category = {}

    if not os.path.exists(base_dir):
        return problem_to_category

    # Get all category directories
    categories = [d for d in os.listdir(base_dir)
                  if os.path.isdir(os.path.join(base_dir, d)) and not d.startswith('.')]

    for category in categories:
        category_path = os.path.join(base_dir, category)
        if not os.path.exists(category_path):
            continue

        for filename in os.listdir(category_path):
            if filename.endswith(file_ext):
                filepath = os.path.join(category_path, filename)
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        # Read first 20 lines to find problem number
                        for _ in range(20):
                            line = f.readline()
                            if not line:
                                break
                            # Look for patterns like:
                            # "15. 3Sum" or "1. Two Sum" (Python)
                            # "* 57. Insert Interval" (Java comments)
                            match = re.search(r'[*\s]?(\d+)\.\s+\w', line)
                            if match:
                                prob_num = int(match.group(1))
                                if prob_num in problem_set:
                                    # Normalize category names
                                    normalized_cat = normalize_category_name(category)
                                    if prob_num not in problem_to_category:
                                        problem_to_category[prob_num] = normalized_cat
                                break
                except Exception as e:
                    continue

    return problem_to_category


def normalize_category_name(category):
    """Normalize category names from different sources"""
    # Mapping Java category names to Python equivalents
    category_mapping = {
        'BackTrack': 'Backtracking',
        'BFS': 'Breadth-First-Search',
        'DFS': 'Depth-First-Search',
        'BinarySearch': 'Binary_Search',
        'BinarySearchTree': 'Binary_Search_Tree',
        'BitManipulation': 'Bit_Manipulation',
        'DynamicProgramming': 'Dynamic_Programming',
        'HashTable': 'Hash_table',
        'LinkedList': 'Linked_list',
        'SlideWindow': 'Sliding_Window',
        'Sliding_Window': 'Sliding_Window',
        'DataStructure': 'Design',
        'TwoPointer': 'Two_Pointers',
    }

    return category_mapping.get(category, category)


def categorize_problems(problems, python_dir, java_dir):
    """Map LC problems to their categories based on directory structure"""
    category_map = defaultdict(list)
    uncategorized = []
    problem_set = set(problems)

    # Build a map of problem number to category from both Python and Java dirs
    problem_to_category = {}

    # Scan Python directory
    python_map = scan_directory_for_problems(python_dir, problem_set, '.py')
    problem_to_category.update(python_map)

    # Scan Java directory
    java_map = scan_directory_for_problems(java_dir, problem_set, '.java')
    # Only add Java mappings if not already found in Python
    for prob_num, category in java_map.items():
        if prob_num not in problem_to_category:
            problem_to_category[prob_num] = category

    # Organize by category
    for prob_num in problems:
        if prob_num in problem_to_category:
            category_map[problem_to_category[prob_num]].append(prob_num)
        else:
            uncategorized.append(prob_num)

    # Sort problems within each category
    for category in category_map:
        category_map[category].sort()

    return category_map, uncategorized


def format_category_name(cat):
    """Format category name for display"""
    return cat.replace('_', ' ').title()


def generate_markdown(category_map, uncategorized, total_count, year_start, year_end):
    """Generate the markdown document"""
    from datetime import date

    md = []
    md.append(f"# LeetCode Practice List ({year_start}-{year_end}) - By Category")
    md.append("")
    md.append(f"**Total Unique Problems: {total_count}**")
    md.append("")
    md.append("Generated from: `data/progress.md`")
    md.append(f"Date: {date.today().strftime('%Y-%m-%d')}")
    md.append("")
    md.append("---")
    md.append("")

    # Sort categories by name
    sorted_categories = sorted(category_map.items())

    # Table of contents
    md.append("## Table of Contents")
    md.append("")
    for category, problems in sorted_categories:
        md.append(f"- [{format_category_name(category)}](#{category.lower().replace('_', '-')}) ({len(problems)} problems)")
    if uncategorized:
        md.append(f"- [Uncategorized](#uncategorized) ({len(uncategorized)} problems)")
    md.append("")
    md.append("---")
    md.append("")

    # Generate sections for each category
    for category, problems in sorted_categories:
        md.append(f"## {format_category_name(category)}")
        md.append("")
        md.append(f"**Count: {len(problems)}**")
        md.append("")
        # Format problems in rows of 10
        for i in range(0, len(problems), 10):
            row = problems[i:i+10]
            md.append(", ".join(str(p) for p in row))
        md.append("")
        md.append("---")
        md.append("")

    # Uncategorized section
    if uncategorized:
        md.append("## Uncategorized")
        md.append("")
        md.append(f"**Count: {len(uncategorized)}**")
        md.append("")
        md.append("These problems were not found in the leetcode_python or leetcode_java directory structure.")
        md.append("")
        for i in range(0, len(uncategorized), 10):
            row = uncategorized[i:i+10]
            md.append(", ".join(str(p) for p in row))
        md.append("")
        md.append("---")
        md.append("")

    # Summary statistics
    md.append("## Summary by Category")
    md.append("")
    md.append("| Category | Count | Percentage |")
    md.append("|----------|-------|------------|")
    for category, problems in sorted(sorted_categories, key=lambda x: len(x[1]), reverse=True):
        percentage = (len(problems) / total_count) * 100
        md.append(f"| {format_category_name(category)} | {len(problems)} | {percentage:.1f}% |")
    if uncategorized:
        percentage = (len(uncategorized) / total_count) * 100
        md.append(f"| Uncategorized | {len(uncategorized)} | {percentage:.1f}% |")
    md.append("")

    return "\n".join(md)


if __name__ == '__main__':
    # Parse command line arguments
    year_start = int(sys.argv[1]) if len(sys.argv) > 1 else 2025
    year_end = int(sys.argv[2]) if len(sys.argv) > 2 else 2026

    print(f"Extracting LeetCode problems from {year_start}-{year_end}...")

    # Extract problems from progress.md
    problems = extract_lc_problems('data/progress.md', year_start, year_end)
    print(f"Extracted {len(problems)} unique problems from {year_start}-{year_end}")

    # Categorize based on both leetcode_python and leetcode_java directories
    python_dir = 'leetcode_python'
    java_dir = 'leetcode_java/src/main/java/LeetCodeJava'
    category_map, uncategorized = categorize_problems(problems, python_dir, java_dir)

    print(f"Categorized: {sum(len(v) for v in category_map.values())} problems")
    print(f"Uncategorized: {len(uncategorized)} problems")
    print(f"\nCategories found:")
    for cat in sorted(category_map.keys()):
        print(f"  - {format_category_name(cat)}: {len(category_map[cat])} problems")

    # Generate markdown
    markdown = generate_markdown(category_map, uncategorized, len(problems), year_start, year_end)

    # Write to file
    output_file = f'data/LC_Practice_{year_start}_{year_end}_By_Category.md'
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(markdown)

    print(f"\n✅ Document generated: {output_file}")
    print(f"\nTop 5 categories:")
    for i, (cat, probs) in enumerate(sorted(category_map.items(), key=lambda x: len(x[1]), reverse=True)[:5], 1):
        percentage = (len(probs) / len(problems)) * 100
        print(f"  {i}. {format_category_name(cat)}: {len(probs)} problems ({percentage:.1f}%)")
