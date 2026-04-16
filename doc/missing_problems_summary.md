# Missing LeetCode Problems Analysis

**Date**: 2026-04-16  
**Comparison**: `LC_goog_all.pdf` vs `google_leetcode_problems_by_tags.md`

---

## 📊 Summary

| Metric | Count |
|--------|-------|
| Problems in PDF | 1024 |
| Problems in Markdown | 829 |
| **Missing in Markdown** (need to add) | **306** |
| Extra in Markdown (not in PDF) | 111 |
| Common problems | 718 |

---

## ⚠️ Problems Missing from Markdown (306 total)

These problems are in the PDF but not yet documented in the markdown file. They should be added to their respective tag/difficulty sections.

### Missing Problems by Number Ranges

**100s-200s Range** (20 problems)
```
#109, #119, #122, #141, #160, #169, #179, #189, #190, #206,
#219, #232, #234, #237, #243, #245, #259, #266, #267, #272
```

**200s-300s Range** (19 problems)
```
#287, #293, #296, #311, #313, #325, #348, #351, #356, #365,
#368, #379, #396, #397, #401, #405, #419, #422, #424
```

**400s-500s Range** (27 problems)
```
#427, #433, #435, #438, #442, #447, #452, #480, #481, #487, #491,
#496, #522, #523, #524, #525, #526, #531, #532, #533, #539,
#540, #541, #551, #554, #562, #564
```

**500s-700s Range** (37 problems)
```
#567, #568, #581, #583, #609, #617, #622, #628, #630, #632, #635, #640,
#644, #650, #651, #656, #664, #665, #667, #677, #688, #689, #707, #717,
#721, #722, #730, #734, #756, #759, #763, #768, #770, #775, #777, #779, #780
```

**700s-900s Range** (36 problems)
```
#782, #788, #789, #790, #794, #795, #799, #813, #816, #827, #830, #832, #835,
#838, #839, #840, #845, #846, #849, #871, #876, #879, #906, #916, #917, #926,
#929, #935, #937, #942, #944, #948, #952, #954, #955, #960, #974
```

**900s-1100s Range** (39 problems)
```
#980, #983, #990, #992, #995, #997, #1010, #1012, #1014, #1015, #1016, #1019,
#1027, #1031, #1037, #1041, #1042, #1046, #1048, #1049, #1055, #1056, #1078,
#1087, #1089, #1094, #1101, #1105, #1121, #1124, #1131, #1136, #1138, #1143,
#1144, #1153, #1155, #1166, #1170
```

**1100s-1300s Range** (27 problems)
```
#1184, #1191, #1199, #1200, #1202, #1206, #1209, #1218, #1219, #1229, #1233,
#1244, #1252, #1258, #1265, #1272, #1275, #1277, #1296, #1313, #1326, #1331,
#1342, #1344, #1346, #1353, #1363
```

**1300s-1500s Range** (30 problems)
```
#1381, #1387, #1414, #1422, #1424, #1441, #1449, #1452, #1470, #1471, #1477,
#1488, #1496, #1498, #1513, #1524, #1525, #1544, #1546, #1554, #1562, #1564,
#1570, #1576, #1580, #1582, #1585, #1592, #1593, #1594
```

**1500s-1700s Range** (28 problems)
```
#1605, #1616, #1619, #1622, #1630, #1631, #1636, #1641, #1642, #1680, #1691,
#1697, #1703, #1707, #1708, #1713, #1717, #1722, #1726, #1727, #1753, #1769,
#1774, #1793, #1806, #1807, #1813, #1834
```

**1800s-2000s Range** (37 problems)
```
#1850, #1855, #1858, #1864, #1871, #1874, #1877, #1882, #1884, #1888, #1896,
#1899, #1906, #1910, #1915, #1921, #1922, #1931, #1937, #1943, #1944, #1966,
#2007, #2018, #2022, #2057, #2076, #2079, #2088, #2105, #2114, #2115, #2122,
#2126, #2128, #2135, #2150
```

**2100s-2200s Range** (7 problems)
```
#2155, #2162, #2167, #2172, #2178
```

---

## ℹ️ Extra Problems in Markdown (111 total)

These are problems documented in the markdown file but NOT in the current Google PDF. These are mostly early problems (#1-99) and a few others. They can stay in the markdown file as they're still valid Google interview problems, but they weren't on this particular PDF snapshot from 2022-02-27.

```
#1-18, #20, #22-23, #25-35, #37-39, #41-46, #48-50, #53-58, #62-81,
#84-85, #88, #90-99, #125, #275, #386, #436, #449, #508, #515-516,
#637, #675, #696, #709, #742, #786, #826, #897, #1036, #1201, #1221,
#1266, #1290, #1370, #1430, #1639, #1644, #1678, #1684, #1687, #1762, #2049
```

---

## 🔍 Recommendations

### To Update `google_leetcode_problems_by_tags.md`

1. **Add 306 missing problems** from the PDF by extracting their:
   - Problem number
   - Title
   - Tags
   - Acceptance rate
   - Difficulty level

2. **Keep the 111 extra problems** - they're still valid Google interview problems and provide additional coverage

3. **Priority order** - Add problems in number order (109, 119, 122, ...) for consistency

### How to Proceed

The script `script/compare_pdf_and_markdown_lc.py` can:
- Be re-run against updated PDFs to track changes over time
- Be extended to automatically extract and format problem details
- Help identify discrepancies in future document updates

---

## 📌 Next Steps

1. Review the PDF manually to extract problem details for missing #s
2. Consider if a semi-automated extraction tool would be helpful
3. Update the markdown file with missing problems
4. Re-run this comparison to verify completeness

---

**Generated**: 2026-04-16  
**Script**: `script/compare_pdf_and_markdown_lc.py`  
**Report**: `script/comparison_report.txt`
