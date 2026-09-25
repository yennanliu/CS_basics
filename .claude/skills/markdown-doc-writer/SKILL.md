---
name: markdown-doc-writer
description: Technical documentation writer specializing in creating clear, well-structured markdown documents for algorithms, system design, interview preparation, and code documentation. Use when writing README files, algorithm explanations, system design docs, or technical guides.
allowed-tools: Read, Glob, Grep, Edit, Write
---

# Markdown Documentation Writer

## When to use this Skill

Use this Skill when:
- Writing README files
- Creating algorithm explanations
- Documenting system design solutions
- Writing interview preparation guides
- Creating cheat sheets and reference materials
- Adding code documentation

## Documentation Standards

### 1. Structure Guidelines

**A document outside `doc/cheatsheet/` and `doc/faq/` usually has** a clear H1, a
one-paragraph description, and sections that never skip a heading level. Those two
trees have their own required shape (see Project-Specific Guidelines below).

**Standard Template:**
```markdown
# Title

Brief description of what this document covers.

## Table of Contents
- [Section 1](#section-1)
- [Section 2](#section-2)

## Section 1

Content...

## Section 2

Content...

## References
- [Link 1](url)
```

### 2. Algorithm Documentation Format

**Use this structure for algorithm problems:**

```markdown
# Problem Number: Problem Title

**Difficulty**: Easy/Medium/Hard
**Topics**: Array, Two Pointers, Hash Table
**Companies**: Google, Amazon, Meta

## Problem Statement

[Clear description of the problem]

**Example 1:**
```
Input: [example input]
Output: [example output]
Explanation: [why this is the output]
```

**Constraints:**
- [List constraints]

## Approach

### Intuition

[Explain the key insight in simple terms]

### Algorithm

1. [Step 1]
2. [Step 2]
3. [Step 3]

### Complexity Analysis

- **Time Complexity**: O(n) - [Explain why]
- **Space Complexity**: O(1) - [Explain why]

## Solution

### Java
```java
class Solution {
    public ReturnType method(InputType param) {
        // Implementation
    }
}
```

### Python
```python
class Solution:
    def method(self, param: InputType) -> ReturnType:
        # Implementation
```

## Alternative Approaches

### Approach 2: [Name]

[Description]

**Complexity**: O(?) time, O(?) space

### Comparison

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Approach 1 | O(n) | O(1) | Optimal |
| Approach 2 | O(n²) | O(1) | Simpler code |

## Key Takeaways

- [Learning point 1]
- [Learning point 2]

## Related Problems

- [Problem A](link)
- [Problem B](link)
```

### 3. System Design Documentation Format

**Follow the template structure:**

```markdown
# System Name: Brief Description

## 1. Requirements

### Functional Requirements
- Feature 1: [Description]
- Feature 2: [Description]

### Non-Functional Requirements
- **Scale**: X million DAU, Y QPS
- **Performance**: p99 latency < Z ms
- **Availability**: 99.9% uptime

## 2. Capacity Estimation

### Traffic
- Daily Active Users: 100M
- Requests per user: 10/day
- QPS: 100M * 10 / 86400 ≈ 11,574

### Storage
- Per user data: 1KB
- Total: 100M * 1KB = 100GB

### Bandwidth
- Average request size: 10KB
- Bandwidth: 11,574 QPS * 10KB ≈ 115MB/s

## 3. API Design

```
POST /api/resource
GET  /api/resource/{id}
PUT  /api/resource/{id}
DELETE /api/resource/{id}
```

## 4. High-Level Architecture

```
[Client] → [Load Balancer] → [App Servers]
                                    ↓
                            [Cache] [DB]
```

## 5. Database Design

### Schema

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);
```

### Indexing Strategy
- Index on `username` for fast lookup
- Index on `created_at` for sorting

## 6. Detailed Component Design

### Component 1: [Name]

**Responsibility**: [What it does]

**Technology**: [Specific tech choice]

**Scaling**: [How to scale]

## 7. Scalability & Reliability

### Caching Strategy
- [Cache what, where, why]

### Sharding Strategy
- [How to partition data]

### Replication
- [Master-slave setup]

## 8. Trade-offs & Alternatives

| Decision | Chosen | Alternative | Rationale |
|----------|--------|-------------|-----------|
| Database | PostgreSQL | MongoDB | Need ACID |

## 9. Monitoring & Alerting

- Metrics to track: [List]
- Alerts: [When to trigger]

## 10. Security Considerations

- Authentication: [Method]
- Authorization: [Method]
- Data encryption: [At rest, in transit]

## References
- [External resources]
```

### 4. Code Formatting

**Inline code**: Use `backticks` for variable names, commands, short code

**Code blocks**: Use fenced code blocks with language

```markdown
```java
public class Example {
    // Code here
}
```
```

**Supported languages:**
- `java`, `python`, `javascript`, `sql`, `bash`
- `json`, `yaml`, `xml`, `markdown`
- `c`, `cpp`, `scala`, `go`

### 5. Visual Elements

**Tables:**
```markdown
| Column 1 | Column 2 | Column 3 |
|----------|----------|----------|
| Data 1   | Data 2   | Data 3   |
```

**Lists:**
```markdown
Unordered:
- Item 1
  - Nested item
- Item 2

Ordered:
1. First step
2. Second step
3. Third step
```

**Emphasis:**
```markdown
*italic* or _italic_
**bold** or __bold__
***bold italic***
`code`
~~strikethrough~~
```

**Links:**
```markdown
[Link text](URL)
[Link with title](URL "Title")
[Reference link][ref]

[ref]: URL "Title"
```

**Images:**
```markdown
![Alt text](image-url)
![Alt text](image-url "Title")
```

### 6. Complexity Analysis Documentation

**Standard format:**
```markdown
## Complexity Analysis

### Time Complexity: O(n log n)
- Sorting takes O(n log n)
- Single pass takes O(n)
- Overall: O(n log n)

### Space Complexity: O(n)
- Hash map stores n elements: O(n)
- Result array: O(n)
- Overall: O(n)

### Optimization Notes
- Can reduce space to O(1) by modifying input in-place
- Trade-off: Destroys original input
```

**Complexity cheat sheet to reference:**
```markdown
| Notation | Name | Example |
|----------|------|---------|
| O(1) | Constant | Array access |
| O(log n) | Logarithmic | Binary search |
| O(n) | Linear | Array scan |
| O(n log n) | Linearithmic | Merge sort |
| O(n²) | Quadratic | Nested loops |
| O(2ⁿ) | Exponential | Recursive Fibonacci |
| O(n!) | Factorial | Permutations |
```

### 7. Writing Style Guidelines

**Be Clear:**
- Use simple language
- Avoid jargon unless necessary
- Define technical terms on first use
- Use active voice

**Be Concise:**
- Remove unnecessary words
- Use bullet points for lists
- Break long paragraphs
- One idea per paragraph

**Be Consistent:**
- Use same terminology throughout
- Follow naming conventions
- Maintain consistent formatting
- Use templates for similar documents

**Examples:**

❌ Bad:
```
The algorithm basically works by iterating through the array and
then it checks if the element is what we're looking for.
```

✅ Good:
```
The algorithm iterates through the array to find the target element.
```

### 8. Interview Preparation Docs

**Pattern template:**
```markdown
# Pattern Name

## When to Use
- [Characteristic 1]
- [Characteristic 2]

## Template Code

```python
def pattern_template(arr):
    # Step 1: Setup

    # Step 2: Main logic

    # Step 3: Return result
```

## Example Problems
1. [Problem 1](link) - Easy
2. [Problem 2](link) - Medium
3. [Problem 3](link) - Hard

## Key Points
- [Tip 1]
- [Tip 2]
```

### 9. Cheat Sheet Format

**Keep it scannable:**
```markdown
# Topic Cheat Sheet

## Quick Reference

| Operation | Syntax | Complexity |
|-----------|--------|------------|
| Access | arr[i] | O(1) |
| Search | arr.indexOf(x) | O(n) |

## Common Patterns

### Pattern 1
```code
// Code snippet
```
**Use when**: [Description]

### Pattern 2
```code
// Code snippet
```
**Use when**: [Description]

## Gotchas
- ⚠️ [Common mistake 1]
- ⚠️ [Common mistake 2]
```

### 10. Document Maintenance

**Version control:**
- Use git to track changes
- Write meaningful commit messages
- Keep documents up to date with code

**Cross-references:**
- Link related documents
- Reference source code files
- Point to external resources

**Validation:**
- Check all links work
- Verify code examples compile
- Test complexity analysis accuracy

## Project-Specific Guidelines

**For CS_basics repository:**

- **Cheatsheets** live in `doc/cheatsheet/` and follow the Cheatsheet Style Guide in
  `CLAUDE.md` (Skeleton A/B, the mandatory Scope line, a `data/cheatsheet_meta.json`
  entry). Use `/lc-cheatsheet` for them; this skill is for everything else.
- **FAQs** live in `doc/faq/` and have a zh overlay; use `/lc-faq-add`.
- **System design** docs follow `system_design/00_template.md`.

## Quality Checklist

Before finalizing documentation:
- [ ] Clear title and description
- [ ] Proper heading hierarchy
- [ ] Code examples tested and working
- [ ] Complexity analysis included
- [ ] Consistent formatting
- [ ] No broken links
- [ ] Spell-checked
- [ ] Follows project conventions
- [ ] Related content linked

## Tools & References

**Markdown validation:**
- Check syntax with markdown linters
- Preview before committing
- Use consistent line breaks

**Useful symbols:**
- ✅ Checkmark for correct approach
- ❌ X for incorrect approach
- ⚠️ Warning for gotchas
- 💡 Bulb for tips
- 📝 Note for important points
