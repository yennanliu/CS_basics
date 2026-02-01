#!/usr/bin/env python3
import re
import sys
import os

def add_javadoc_complexity(file_path):
    """Add Javadoc complexity comments to Java methods."""
    
    with open(file_path, 'r') as f:
        content = f.read()
    
    original_content = content
    
    # Pattern to match inline complexity comments like: // time: O(N), space: O(1)
    pattern = r'(\s*)(//[^\n]*\n)(\s*)(//\s*time:\s*O\([^)]+\),?\s*space:\s*O\([^)]+\))\s*\n(\s*public\s+)'
    
    def replacement(match):
        indent = match.group(1)
        comment_line = match.group(2)
        next_indent = match.group(3)
        complexity_line = match.group(4)
        public_keyword = match.group(5)
        
        # Extract time and space complexity
        time_match = re.search(r'time:\s*O\(([^)]+)\)', complexity_line)
        space_match = re.search(r'space:\s*O\(([^)]+)\)', complexity_line)
        
        if time_match and space_match:
            time_complexity = time_match.group(1)
            space_complexity = space_match.group(1)
            
            javadoc = f'''{indent}{comment_line}{next_indent}/**
{next_indent} * time = O({time_complexity})
{next_indent} * space = O({space_complexity})
{next_indent} */
{next_indent}{public_keyword}'''
            return javadoc
        
        return match.group(0)
    
    # Apply the replacement
    content = re.sub(pattern, replacement, content)
    
    # Only write if content changed
    if content != original_content:
        with open(file_path, 'w') as f:
            f.write(content)
        return True
    return False

if __name__ == "__main__":
    files_to_process = sys.argv[1:]
    
    modified_count = 0
    for file_path in files_to_process:
        if os.path.exists(file_path):
            if add_javadoc_complexity(file_path):
                print(f"Modified: {os.path.basename(file_path)}")
                modified_count += 1
    
    print(f"\nTotal files modified: {modified_count}")
