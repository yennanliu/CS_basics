#!/usr/bin/env python3
"""
Compare LeetCode problems in PDF and Markdown files to find missing problems.

This script:
1. Extracts LeetCode problem numbers from a PDF file
2. Extracts problem numbers from a markdown file
3. Identifies problems in PDF but not in markdown
4. Generates a report of missing problems

Usage:
    python3 compare_pdf_and_markdown_lc.py [pdf_file] [markdown_file]

Default:
    python3 compare_pdf_and_markdown_lc.py \
        doc/leetcode_company_V6/LC_goog_all.pdf \
        doc/google_leetcode_problems_by_tags.md
"""

import re
import sys
from pathlib import Path
from typing import Set, List, Tuple
from collections import defaultdict


def extract_problems_from_markdown(md_file: str) -> Set[int]:
    """
    Extract all LeetCode problem numbers from markdown file.

    Looks for patterns like:
    - #1 - Problem Name
    - LC 72 - Problem Name
    """
    problems = set()

    if not Path(md_file).exists():
        print(f"❌ Markdown file not found: {md_file}")
        return problems

    with open(md_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Pattern 1: #123 - Problem Name
    pattern1 = r'#(\d+)\s*-'
    matches1 = re.findall(pattern1, content)
    problems.update(int(m) for m in matches1)

    # Pattern 2: LC 123 - Problem Name
    pattern2 = r'LC\s+(\d+)\s*(?:-|:)'
    matches2 = re.findall(pattern2, content)
    problems.update(int(m) for m in matches2)

    return problems


def extract_problems_from_pdf_text(pdf_text: str) -> Set[int]:
    """
    Extract LeetCode problem numbers from PDF text content.

    Looks for patterns like:
    - #123, #456
    - problem 123
    - LC 123
    - Plain numbers from PDF table format (3-4 digit numbers at line start)
    """
    problems = set()

    # Pattern 1: #123 or #123,
    pattern1 = r'#(\d+)(?:[,\s]|$)'
    matches1 = re.findall(pattern1, pdf_text)
    problems.update(int(m) for m in matches1)

    # Pattern 2: problem 123, problem number 123, etc.
    pattern2 = r'(?:problem|problems?(?:\s+numbers?)?)\s+(\d+)'
    matches2 = re.findall(pattern2, pdf_text, re.IGNORECASE)
    problems.update(int(m) for m in matches2)

    # Pattern 3: LC 123
    pattern3 = r'LC\s+(\d+)'
    matches3 = re.findall(pattern3, pdf_text, re.IGNORECASE)
    problems.update(int(m) for m in matches3)

    # Pattern 4: Line that starts with a 3-4 digit number (from PDF table format)
    # Common in table exports where problem # appears at start of each row
    for line in pdf_text.split('\n'):
        line = line.strip()
        # Match lines starting with 3-4 digit numbers (LC problem numbers)
        # But exclude common words like dates, percentages, etc.
        if line and re.match(r'^(\d{3,4})(?:\s|$)', line):
            # Make sure it's not a date or percentage
            if not re.match(r'^\d{4}[-/]', line) and not re.search(r'%', line):
                match = re.match(r'^(\d{3,4})', line)
                if match:
                    num = int(match.group(1))
                    # LeetCode problem numbers are typically 1-3000
                    if 1 <= num <= 3000:
                        problems.add(num)

    return problems


def try_extract_pdf_with_pymupdf(pdf_file: str) -> str:
    """Try to extract text from PDF using PyMuPDF (fitz)."""
    try:
        import fitz
        text = ""
        with fitz.open(pdf_file) as doc:
            for page_num in range(len(doc) - 1, -1, -1):  # Start from last page
                page = doc[page_num]
                text += f"\n--- Page {page_num + 1} ---\n"
                text += page.get_text()
        return text
    except ImportError:
        return None
    except Exception as e:
        print(f"⚠️  Error reading PDF with PyMuPDF: {e}")
        return None


def try_extract_pdf_with_pypdf(pdf_file: str) -> str:
    """Try to extract text from PDF using PyPDF."""
    try:
        from PyPDF2 import PdfReader
        text = ""
        reader = PdfReader(pdf_file)
        # Iterate from last page backwards
        for page_num in range(len(reader.pages) - 1, -1, -1):
            page = reader.pages[page_num]
            text += f"\n--- Page {page_num + 1} ---\n"
            text += page.extract_text()
        return text
    except ImportError:
        return None
    except Exception as e:
        print(f"⚠️  Error reading PDF with PyPDF: {e}")
        return None


def try_extract_pdf_with_pdfplumber(pdf_file: str) -> str:
    """Try to extract text from PDF using pdfplumber."""
    try:
        import pdfplumber
        text = ""
        with pdfplumber.open(pdf_file) as pdf:
            # Iterate from last page backwards
            for page_num in range(len(pdf.pages) - 1, -1, -1):
                page = pdf.pages[page_num]
                text += f"\n--- Page {page_num + 1} ---\n"
                text += page.extract_text() or ""
        return text
    except ImportError:
        return None
    except Exception as e:
        print(f"⚠️  Error reading PDF with pdfplumber: {e}")
        return None


def extract_problems_from_pdf(pdf_file: str) -> Set[int]:
    """
    Extract LeetCode problem numbers from PDF file.
    Tries multiple methods to read the PDF.
    """
    if not Path(pdf_file).exists():
        print(f"❌ PDF file not found: {pdf_file}")
        return set()

    pdf_text = None

    # Try different PDF extraction methods
    print(f"📄 Attempting to read PDF: {pdf_file}")

    print("  Trying PyMuPDF (fitz)...")
    pdf_text = try_extract_pdf_with_pymupdf(pdf_file)
    if pdf_text:
        print("  ✅ Successfully read PDF with PyMuPDF")
        return extract_problems_from_pdf_text(pdf_text)

    print("  Trying PyPDF2...")
    pdf_text = try_extract_pdf_with_pypdf(pdf_file)
    if pdf_text:
        print("  ✅ Successfully read PDF with PyPDF2")
        return extract_problems_from_pdf_text(pdf_text)

    print("  Trying pdfplumber...")
    pdf_text = try_extract_pdf_with_pdfplumber(pdf_file)
    if pdf_text:
        print("  ✅ Successfully read PDF with pdfplumber")
        return extract_problems_from_pdf_text(pdf_text)

    print("\n❌ Could not extract PDF. Please install one of:")
    print("   pip install pymupdf")
    print("   pip install PyPDF2")
    print("   pip install pdfplumber")
    return set()


def generate_report(pdf_problems: Set[int], md_problems: Set[int]) -> str:
    """Generate comparison report."""
    missing_in_md = pdf_problems - md_problems
    extra_in_md = md_problems - pdf_problems
    common = pdf_problems & md_problems

    report = []
    report.append("=" * 80)
    report.append("📊 LeetCode Problem Comparison Report")
    report.append("=" * 80)
    report.append("")

    report.append(f"📈 Statistics:")
    report.append(f"  • Problems in PDF:         {len(pdf_problems)}")
    report.append(f"  • Problems in Markdown:    {len(md_problems)}")
    report.append(f"  • Common problems:         {len(common)}")
    report.append(f"  • Missing in Markdown:     {len(missing_in_md)}")
    report.append(f"  • Extra in Markdown:       {len(extra_in_md)}")
    report.append("")

    if missing_in_md:
        report.append("⚠️  MISSING IN MARKDOWN (Problems in PDF but not in Markdown):")
        report.append(f"   Count: {len(missing_in_md)}")
        report.append("")
        # Sort by problem number
        sorted_missing = sorted(missing_in_md)
        # Group into chunks of 10
        for i in range(0, len(sorted_missing), 10):
            chunk = sorted_missing[i:i+10]
            report.append(f"   {', '.join(f'#{p}' for p in chunk)}")
        report.append("")
    else:
        report.append("✅ All problems in PDF are also in Markdown!")
        report.append("")

    if extra_in_md:
        report.append("ℹ️  EXTRA IN MARKDOWN (Problems in Markdown but not in PDF):")
        report.append(f"   Count: {len(extra_in_md)}")
        report.append("")
        sorted_extra = sorted(extra_in_md)
        for i in range(0, len(sorted_extra), 10):
            chunk = sorted_extra[i:i+10]
            report.append(f"   {', '.join(f'#{p}' for p in chunk)}")
        report.append("")

    report.append("=" * 80)

    return "\n".join(report)


def main():
    """Main function."""
    # Default file paths
    script_dir = Path(__file__).parent.parent
    default_pdf = script_dir / "doc/leetcode_company_V6/LC_goog_all.pdf"
    default_md = script_dir / "doc/google_leetcode_problems_by_tags.md"

    # Get paths from command line or use defaults
    pdf_file = sys.argv[1] if len(sys.argv) > 1 else str(default_pdf)
    md_file = sys.argv[2] if len(sys.argv) > 2 else str(default_md)

    print("\n🔍 Extracting LeetCode problems from files...")
    print(f"   PDF:      {pdf_file}")
    print(f"   Markdown: {md_file}")
    print()

    # Extract problems
    pdf_problems = extract_problems_from_pdf(pdf_file)
    md_problems = extract_problems_from_markdown(md_file)

    print()

    # Generate and print report
    if pdf_problems or md_problems:
        report = generate_report(pdf_problems, md_problems)
        print(report)

        # Save report to file
        report_file = Path(__file__).parent / "comparison_report.txt"
        with open(report_file, 'w') as f:
            f.write(report)
        print(f"\n📝 Report saved to: {report_file}")
    else:
        print("❌ Could not extract problems from files")
        sys.exit(1)


if __name__ == "__main__":
    main()
