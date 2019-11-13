package path;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 */
public class Path {

    private SortedSet<DataPoint> points;

    public Path() {
        points = new TreeSet<>(Comparator.comparing(p -> p.date()));
    }

    public void addDataPoint(DataPoint dataPoint) {
        points.add(dataPoint);
    }

    public SortedSet<DataPoint> getData() {
        return Collections.unmodifiableSortedSet(points);
    }


}
