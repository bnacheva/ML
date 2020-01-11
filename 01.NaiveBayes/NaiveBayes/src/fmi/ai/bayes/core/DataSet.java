package fmi.ai.bayes.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSet {

    private static final List<List<String>> data = new ArrayList<>();

    public static List<List<String>> getDataSet() {
        readDataSet();
        return data;
    }

    public static void readDataSet() {
        final File file = new File("house-votes-84.data.txt");
        data.clear();
        try (final BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String example[] = line.split(",");
                if (example.length != 17) {
                    continue;
                }
                data.add(List.of(example));
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
