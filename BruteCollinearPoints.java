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

        // Examine every combination of 4 points: C(n,4) — exactly 4 nested loops → O(n^4)
        // Because ptsCopy is sorted, ptsCopy[i] < ptsCopy[j] < ptsCopy[k] < ptsCopy[l].
        // ptsCopy[i] is always the minimum endpoint, so no extra scan is needed to
        // avoid duplicates — each maximal segment is discovered exactly once.
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                double slopeIJ = ptsCopy[i].slopeTo(ptsCopy[j]);
                for (int k = j + 1; k < n - 1; k++) {
                    // Prune early: if i-j-k are not collinear, no need to check l
                    if (Double.compare(slopeIJ, ptsCopy[i].slopeTo(ptsCopy[k])) != 0) continue;
                    for (int l = k + 1; l < n; l++) {
                        // All four points are collinear if the third slope also matches
                        if (Double.compare(slopeIJ, ptsCopy[i].slopeTo(ptsCopy[l])) == 0) {
                            // ptsCopy[i] is the minimum (array is sorted) and
                            // ptsCopy[l] is the maximum — this is the maximal segment.
                            if (size == tempSegments.length) {
                                LineSegment[] resized = new LineSegment[tempSegments.length * 2];
                                for (int x = 0; x < size; x++) {
                                    resized[x] = tempSegments[x];
                                }
                                tempSegments = resized;
                            }
                            tempSegments[size++] = new LineSegment(ptsCopy[i], ptsCopy[l]);
                        }
                    }
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
