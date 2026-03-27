import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int size = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument 'points' cannot be null.");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Array contains null point at index " + i + ".");
            }
        }

        // Clone to avoid mutating the original array passed
        Point[] ptsCopy = points.clone();
        Arrays.sort(ptsCopy);

        // Check for duplicated points
        for (int i = 0; i < ptsCopy.length - 1; i++) {
            if (ptsCopy[i].compareTo(ptsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Repeated points found.");
            }
        }
        LineSegment[] tempSegments = new LineSegment[2];
        int n = ptsCopy.length;

        // Examine 4 points at a time
        for (int i = 0; i < n - 3; i++) {
            for (int m = n - 1; m >= i + 3; m--) {
                Point p = ptsCopy[i];
                Point s = ptsCopy[m];
                double slopePS = p.slopeTo(s);
                
                boolean foundCollinear = false;
                for (int j = i + 1; j < m - 1; j++) {
                    if (foundCollinear) break;
                    for (int k = j + 1; k < m; k++) {
                        Point q = ptsCopy[j];
                        Point r = ptsCopy[k];

                        // To check if they are collinear, compare the slopes
                        if (Double.compare(slopePS, p.slopeTo(q)) == 0 &&
                            Double.compare(slopePS, p.slopeTo(r)) == 0) {
                            
                            foundCollinear = true;

                            // Check if this is the longest segment (p is start, s is end)
                            // 1. Is there any point in the array BEFORE p that is collinear?
                            boolean hasBefore = false;
                            for (int x = 0; x < i; x++) {
                                if (Double.compare(ptsCopy[x].slopeTo(p), slopePS) == 0) {
                                    hasBefore = true;
                                    break;
                                }
                            }
                            if (hasBefore) break;

                            // 2. Is there any point in the array AFTER s that is collinear?
                            boolean hasAfter = false;
                            for (int x = m + 1; x < n; x++) {
                                if (Double.compare(p.slopeTo(ptsCopy[x]), slopePS) == 0) {
                                    hasAfter = true;
                                    break;
                                }
                            }
                            if (hasAfter) break;

                            if (size == tempSegments.length) {
                                LineSegment[] resized = new LineSegment[tempSegments.length * 2];
                                for (int x = 0; x < size; x++) {
                                    resized[x] = tempSegments[x];
                                }
                                tempSegments = resized;
                            }
                            tempSegments[size++] = new LineSegment(p, s);
                            break;
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

    public LineSegment[] segments() {
        return segments.clone();
    }
}
