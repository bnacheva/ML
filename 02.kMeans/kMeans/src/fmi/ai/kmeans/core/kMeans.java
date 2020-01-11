package fmi.ai.kmeans.core;

import fmi.ai.kmeans.core.helpers.Dataset;
import fmi.ai.kmeans.core.helpers.Point;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class kMeans {

    private static final int MAX_ITERATIONS = Short.MAX_VALUE;

    @NotNull
    private static List<Point> getRandomCentroids(Dataset dataset, int centroidsCounts) {
        List<Point> centroids = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < centroidsCounts; i++) {
            Point point = dataset.getEntries().get(rand.nextInt(dataset.getEntries().size()));
            centroids.add(new Point(point));
        }
        return centroids;
    }

    @NotNull
    private static List<Point> getLabels(@NotNull Dataset dataset, List<Point> centroids) {
        List<Point> labels = new ArrayList<>();
        List<Point> entries = dataset.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            labels.add(getClosestCentroid(entries.get(i), centroids));
        }
        return labels;
    }

    private static Point getClosestCentroid(Point point, @NotNull List<Point> centroids) {
        int closest = 0;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < centroids.size(); i++) {
            double distance = Math.sqrt(Math.pow((point.x - centroids.get(i).x), 2) +
                    Math.pow((point.y - centroids.get(i).y), 2));
            if (distance < minDistance) {
                minDistance = distance;
                closest = i;
            }
        }
        return centroids.get(closest);
    }

    @Contract(pure = true)
    private static boolean shouldStop(List<Point> oldCentroids, List<Point> centroids, int iterations) {
        if (iterations > MAX_ITERATIONS) {
            return true;
        }
        if (oldCentroids == null) {
            return false;
        }
        if (oldCentroids.equals(centroids)) {
            return true;
        }
        return false;
    }

    @NotNull
    private static List<Point> getCentroids(@NotNull Dataset dataset, List<Point> labels, @NotNull List<Point> oldCentroids) {
        List<Point> centroids = new ArrayList<>();
        List<Point> entries = dataset.getEntries();
        for (int i = 0; i < oldCentroids.size(); i++) {
            Point current = oldCentroids.get(i);
            double sumX = 0;
            double sumY = 0;
            double count = 0;
            for (int j = 0; j < labels.size(); j++) {
                if (labels.get(j).equals(current)) {
                    sumX += entries.get(j).x;
                    count++;
                    sumY += entries.get(j).y;
                }
            }
            if (count == 0) {
                centroids.add(new Point(Math.random(), Math.random()));
            } else {
                centroids.add(new Point(sumX / count, sumY / count));
            }
        }
        return centroids;
    }

    public static List<Point> KMeans(Dataset dataset, int k) {
        List<Point> centroids = getRandomCentroids(dataset, k);
        int iterations = 0;
        List<Point> oldCentroids = null;
        while (!shouldStop(oldCentroids, centroids, iterations)) {
            oldCentroids = centroids;
            iterations++;
            // Assign labels to each datapoint based on centroids
            List<Point> labels = getLabels(dataset, centroids);
            // Assign centroids based on datapoint labels
            centroids = getCentroids(dataset, labels, centroids);
        }
        return centroids;
    }
}
