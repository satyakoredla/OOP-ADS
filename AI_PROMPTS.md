# AI_PROMPTS.md — Team 15 (Project 3: Collinear Points)

This file documents all significant AI-assisted interactions during the development of Project 3 (Pattern Recognition — Collinear Points), as required by the course evaluation framework.

---

## 1. Understanding the Project Specification

**Problem:** We needed to understand the exact API contracts for `Point`, `BruteCollinearPoints`, and `FastCollinearPoints` as described in the Sedgewick specification.

**Prompt used:**
> "Explain the Sedgewick collinear points project. What should `slopeTo()` return for a horizontal line, a vertical line, and the same point? What is the expected API for `BruteCollinearPoints` and `FastCollinearPoints`?"

**AI Tool:** GitHub Copilot / ChatGPT

**What AI provided:** A plain-English explanation of the four slope edge cases (`NEGATIVE_INFINITY`, `+0.0`, `POSITIVE_INFINITY`, and the standard formula), and a summary of the required constructor and method signatures.

**Modifications made:** We independently implemented each method using the Sedgewick API; the AI explanation was used only as a reading aid to understand the spec, not to generate code directly.

---

## 2. slopeOrder() Comparator Design

**Problem:** We were unsure how to implement a `Comparator<Point>` that compares two points by their slope to the invoking point.

**Prompt used:**
> "How do I write a nested Comparator class inside Point.java in Java that compares two Point objects based on their slope to `this` point? Should I use subtraction or `Double.compare()`?"

**AI Tool:** GitHub Copilot

**What AI provided:** Advised using `Double.compare()` instead of subtraction (to avoid floating-point sign errors near zero), and showed the general pattern of a named inner class implementing `Comparator<Point>`.

**Modifications made:** We wrote the `SlopeOrder` inner class ourselves, using `Double.compare()` as advised. The logic of calling `Point.this.slopeTo()` on each argument was our own code.

---

## 3. FastCollinearPoints — Stable Sort Insight

**Problem:** We were getting duplicate segments in `FastCollinearPoints` on inputs with 5+ collinear points. We did not understand why the deduplication check `p.compareTo(sortedBySlope[j]) < 0` worked.

**Prompt used:**
> "In FastCollinearPoints, after sorting by slope relative to point p, why is it correct to only record a segment when p is less than all other points in the collinear group? Why does the stable sort matter here?"

**AI Tool:** ChatGPT (GPT-4)

**What AI provided:** Explained that because `Arrays.sort` is stable and the array was pre-sorted by position, within any group of equal-slope points the positional order is preserved. This means the minimum point in the group is always encountered first when scanning from left-to-right, allowing deduplication using a single `compareTo` check.

**Modifications made:** We already had a version of the deduplication check. AI clarified *why* it is correct. The comment in `FastCollinearPoints.java` ("Because Java's object sort is stable, and ptsCopy was sorted positionally ahead of time…") was written by us to explain this understanding; we used AI to verify our reasoning before finalising the comment.

---

## 4. BruteCollinearPoints — Refactoring to O(n⁴)

**Problem:** Our original `BruteCollinearPoints` used a 5-loop structure to detect maximal segments (4 loops for combinations + 1 loop for before/after scan), resulting in O(n⁵) complexity. The spec requires O(n⁴).

**Prompt used:**
> "My BruteCollinearPoints has an extra scan loop inside the 4-point loop to check if the segment is maximal. This makes it O(n^5). How can I restructure to O(n^4) while still only returning maximal segments?"

**AI Tool:** ChatGPT (GPT-4)

**What AI provided:** Explained that since the input array is sorted in natural order before the loops begin, `ptsCopy[i]` (the outermost loop variable) is always the smallest point in any 4-tuple `(i, j, k, l)` with `i < j < k < l`. Therefore it is guaranteed to be the segment's minimum endpoint, and `ptsCopy[l]` is the maximum. No extra scan is needed — each maximal segment is found exactly once.

**Modifications made:** We rewrote the constructor using exactly 4 nested loops (`i < j < k < l`) with an early-prune on the `i-j-k` collinearity check before entering the innermost loop. The final code is entirely our own; AI provided the conceptual insight about why the pre-sort eliminates the need for the 5th scan loop.

---

## 5. Formatting and Documentation Improvements

**Problem:** We wanted to improve comments and Javadoc-style documentation across all files for readability.

**Commit tagged:** `bc85b33` — `refactor: improve formatting and documentation in BruteCollinearPoints [AI-Assisted]`

**Prompt used:**
> "Can you rewrite the inline comments in this method to be clearer and more concise? [pasted BruteCollinearPoints constructor]"

**AI Tool:** GitHub Copilot

**What AI provided:** Suggested alternative phrasings for inline comments. We accepted some suggestions verbatim and paraphrased others.

**Modifications made:** Final comment text is a mix of AI suggestions and our own edits. All logic and structure remained unchanged.

---

## Summary Table

| # | Area | AI Tool | Nature of Use | Commit(s) Affected |
|---|------|---------|---------------|--------------------|
| 1 | Spec Understanding | ChatGPT | Reading aid only | None |
| 2 | `slopeOrder()` Comparator | Copilot | Design guidance | Point.java commits |
| 3 | Stable sort / dedup insight | ChatGPT | Concept verification | FastCollinearPoints.java commits |
| 4 | O(n⁴) restructure | ChatGPT | Algorithm insight | BruteCollinearPoints.java commits |
| 5 | Comment formatting | Copilot | Text suggestion | bc85b33 [AI-Assisted] |
