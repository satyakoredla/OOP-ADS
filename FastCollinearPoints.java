import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private int size = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument 'points' cannot be null.");
        }

        // Check for null points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Array contains null point at index " + i + ".");
            }
        }

        // Clone to prevent mutating input arguments
        Point[] ptsCopy = points.clone();
        Arrays.sort(ptsCopy);

        // Check for duplicated points after sorting
        for (int i = 0; i < ptsCopy.length - 1; i++) {
            if (ptsCopy[i].compareTo(ptsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Repeated points found.");
            }
        }

        LineSegment[] tempSegments = new LineSegment[2];
        int n = ptsCopy.length;

        for (int i = 0; i < n; i++) {
            Point p = ptsCopy[i];

            // Re-copy original points array to avoid scrambling the sorted positional order
            // which guarantees that when we use the stable sort Collections/Arrays.sort(), 
            // points with exactly the same slope with P will be positionally sorted inside their group.
            Point[] sortedBySlope = ptsCopy.clone();
            Arrays.sort(sortedBySlope, p.slopeOrder());

            int j = 1; // start from 1 since index 0 is p itself (slope -Infinity)
            while (j < n) {
                double slope = p.slopeTo(sortedBySlope[j]);
                int count = 1;
                
                // Keep moving forward to count all adjacent points with identical slope respecting p
                while (j + count < n && Double.compare(p.slopeTo(sortedBySlope[j + count]), slope) == 0) {
                    count++;
                }

                // If we found at least 3 points + origin p = 4 collinear points
                if (count >= 3) {
                    // Because Java's object sort is stable, and ptsCopy was sorted positionally ahead of time,
                    // sortedBySlope[j] will be the smallest position relative point in this collinear block.
                    // This block will be valid ONLY if the origin 'p' is strictly less than 'sortedBySlope[j]'.
                    // So we check if p is the bottom-left-most point of the segment pattern to avoid permutations.
                    if (p.compareTo(sortedBySlope[j]) < 0) {
                        if (size == tempSegments.length) {
                            LineSegment[] resized = new LineSegment[tempSegments.length * 2];
                            for (int x = 0; x < size; x++) {
                                resized[x] = tempSegments[x];
                            }
                            tempSegments = resized;
                        }
                        tempSegments[size++] = new LineSegment(p, sortedBySlope[j + count - 1]);
                    }
                }
                j += count;
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

    public LineSegment[] segments() {
        // Must return a newly allocated array so structure cannot be internally modified (immutability)
        return segments.clone();
    }
}
