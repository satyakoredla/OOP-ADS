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
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int m = k + 1; m < n; m++) {
                        Point p = ptsCopy[i];
                        Point q = ptsCopy[j];
                        Point r = ptsCopy[k];
                        Point s = ptsCopy[m];

                        // To check if they are collinear, compare the slopes
                        if (Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0 &&
                            Double.compare(p.slopeTo(q), p.slopeTo(s)) == 0) {
                            
                            if (size == tempSegments.length) {
                                LineSegment[] resized = new LineSegment[tempSegments.length * 2];
                                for (int x = 0; x < size; x++) {
                                    resized[x] = tempSegments[x];
                                }
                                tempSegments = resized;
                            }
                            tempSegments[size++] = new LineSegment(p, s);
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
