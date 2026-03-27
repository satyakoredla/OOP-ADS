import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class TestClient {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -cp \"algs4.jar;.\" TestClient input8.txt");
            return;
        }

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the brute force line segments
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        StdOut.println("Brute Collinear Points (" + brute.numberOfSegments() + "):");
        for (LineSegment segment : brute.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        StdOut.println();

        // print and draw the fast collinear line segments
        FastCollinearPoints fast = new FastCollinearPoints(points);
        StdOut.println("Fast Collinear Points (" + fast.numberOfSegments() + "):");
        for (LineSegment segment : fast.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
