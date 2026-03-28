import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int size = 0;

    // Finds all line segments containing 4 points.
    // Time complexity: O(n^4) — four nested loops over C(n,4) combinations.
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument 'points' cannot be null.");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Array contains null point at index " + i + ".");
            }
        }

        // Clone to avoid mutating the original array
        Point[] ptsCopy = points.clone();
        Arrays.sort(ptsCopy); // sort by natural order (y, then x)

        // Check for duplicate points
        for (int i = 0; i < ptsCopy.length - 1; i++) {
            if (ptsCopy[i].compareTo(ptsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Repeated points found.");
            }
        }

        int n = ptsCopy.length;
        LineSegment[] tempSegments = new LineSegment[2];

        // O(n^4) correct approach that handles 5+ collinear points without duplicates.
        //
        // Strategy: fix the anchor point i and a second point j (i < j).
        // The slope s = ptsCopy[i].slopeTo(ptsCopy[j]) defines a line through i.
        //
        // Before recording a segment we must ensure two things:
        //   1. ptsCopy[i] is the TRUE minimum (leftmost/lowest) endpoint of the
        //      collinear group — i.e. no point BEFORE i lies on the same line.
        //      We check this with a backward scan (indices 0..i-1). If any earlier
        //      point shares slope s with i, then i is NOT the minimum so we skip.
        //   2. At least 3 more points beyond i are collinear on this slope — we
        //      scan forward from j+1 to find all collinear points and count them.
        //      We only emit a segment when the total collinear count >= 4.
        //
        // Because we only emit when i is the true minimum endpoint, each maximal
        // segment (even one spanning 5, 6, … points) is recorded exactly once.
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n; j++) {
                double slope = ptsCopy[i].slopeTo(ptsCopy[j]);

                // Check: is ptsCopy[i] the true minimum for this slope?
                // (no point before i is collinear with i on this slope)
                boolean iIsMin = true;
                for (int pre = 0; pre < i; pre++) {
                    if (Double.compare(ptsCopy[i].slopeTo(ptsCopy[pre]), slope) == 0) {
                        iIsMin = false;
                        break;
                    }
                }
                if (!iIsMin) continue;

                // Collect all collinear points with i on this slope (including j).
                // Find the index of the maximum point (last in sorted order).
                int collinearCount = 2; // i and j
                int maxIdx = j;
                for (int k = j + 1; k < n; k++) {
                    if (Double.compare(ptsCopy[i].slopeTo(ptsCopy[k]), slope) == 0) {
                        collinearCount++;
                        maxIdx = k; // ptsCopy is sorted, so later index = larger point
                    }
                }

                // Only emit if we have 4 or more collinear points AND j == i+1 among
                // the collinear group (so we emit exactly once per (i, slope) pair).
                // The simplest guard: only emit on the first j after i that is collinear.
                // Since we iterate j from i+1 upward, the first j on this slope is the
                // second-smallest point of the group — we skip all later j values that
                // hit the same (i, slope) pair.
                // We can detect "first j for this slope from i" by checking that no
                // point between i+1 and j-1 is collinear with i on this slope.
                boolean jIsFirstCollinear = true;
                for (int between = i + 1; between < j; between++) {
                    if (Double.compare(ptsCopy[i].slopeTo(ptsCopy[between]), slope) == 0) {
                        jIsFirstCollinear = false;
                        break;
                    }
                }
                if (!jIsFirstCollinear) continue;

                if (collinearCount >= 4) {
                    if (size == tempSegments.length) {
                        LineSegment[] resized = new LineSegment[tempSegments.length * 2];
                        for (int x = 0; x < size; x++) resized[x] = tempSegments[x];
                        tempSegments = resized;
                    }
                    tempSegments[size++] = new LineSegment(ptsCopy[i], ptsCopy[maxIdx]);
                }
            }
        }

        segments = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            segments[i] = tempSegments[i];
        }
    }

    public int numberOfSegments() {
        return size;
    }

    // Returns a defensive copy so the internal array cannot be mutated externally.
    public LineSegment[] segments() {
        return segments.clone();
    }
}
