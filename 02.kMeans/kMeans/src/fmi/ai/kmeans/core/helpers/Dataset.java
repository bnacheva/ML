package fmi.ai.kmeans.core.helpers;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dataset {

    private List<Point> entries = new ArrayList<>();

    public Dataset() {}

    @NotNull
    public static Dataset readFromFile(String fileName) {
        Dataset dataset = new Dataset();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.isEmpty()) {
                    line = bufferedReader.readLine();
                    continue;
                }

                List<String> entry = Arrays.asList(line.split("\\s+"));
                dataset.addEntry(Double.parseDouble(entry.get(0)), Double.parseDouble(entry.get(1)));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public void addEntry(double x, double y) {
        entries.add(new Point(x, y));
    }

    public int size() {
        return entries.size();
    }

    public List<Point> getEntries() {
        return entries;
    }
}
