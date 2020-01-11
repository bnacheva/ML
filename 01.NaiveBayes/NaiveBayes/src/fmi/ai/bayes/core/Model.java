package fmi.ai.bayes.core;

import fmi.ai.bayes.core.enums.Classification;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Model {

    private final double[][][] model; // 16 attributes, 2 classes, 3 possible answers
    private final List<List<String>> data;
    private double republicanProbability;
    private double democratProbability;

    public Model() {
        this.model = new double[16][2][3];
        init();
        this.data = DataSet.getDataSet();
        calculateProbabilities();
    }

    public void naiveBayes() {
        final int foldSize = this.data.size() / 10;

        Collections.shuffle(this.data);

        double total = 0;

        for (int i = 0; i < 10; i++) {
            init();
            final int currentTestMin = i * foldSize;
            final int currentTestMax = i * foldSize + foldSize - 1;
            for (int j = 0; j < this.data.size(); j++) {
                // Train
                if (j >= currentTestMin && j <= currentTestMax) {
                    continue;
                }
                final int c = currentClass(this.data.get(j).get(0));
                for (int k = 1; k < this.data.get(j).size(); k++) {
                    if (this.data.get(j).get(k).toLowerCase().equals("y")) {
                        this.model[k - 1][c][0]++;
                    } else if (this.data.get(j).get(k).toLowerCase().equals("n")) {
                        this.model[k - 1][c][1]++;
                    } else {
                        this.model[k - 1][c][2]++;
                    }
                }
            }

            // Test
            double correct = 0;
            double all = 0;
            for (int l = currentTestMin; l <= currentTestMax; l++) {
                final Classification c = classification(this.data.get(l));
                if (this.data.get(l).get(0).equals(c.toString())) {
                    correct++;
                }
                all++;
            }
            final double currentAccuracy = correct / all;
            total += currentAccuracy;
            System.out.println("Accuracy: " + currentAccuracy);
        }
        System.out.println("Mean: " + total / 10);
    }

    public Classification classification(@NotNull List<String> x) {
        double repP = this.republicanProbability;
        for (int i = 1; i < x.size(); i++) {
            if (x.get(i).equals("y")) {
                repP *= getProbability(i - 1, 0, 0);
            } else if (x.get(i).equals("n")) {
                repP *= getProbability(i - 1, 0, 1);
            } else {
                repP *= getProbability(i - 1, 0, 2);
            }
        }

        double demP = this.democratProbability;
        for (int i = 1; i < x.size(); i++) {
            if (x.get(i).equals("y")) {
                demP *= getProbability(i - 1, 1, 0);
            } else if (x.get(i).equals("n")) {
                demP *= getProbability(i - 1, 1, 1);
            } else {
                demP *= getProbability(i - 1, 1, 2);
            }
        }
        return repP > demP ? Classification.republican : Classification.democrat;
    }

    public double getProbability(int i, int j, int k) {
        return this.model[i][j][k] / (this.model[i][j][0] + this.model[i][j][1] + this.model[i][j][2]);
    }

    private int currentClass(@NotNull String c) {
        if (c.equals("republican")) {
            return 0;
        } else if (c.equals("democrat")) {
            return 1;
        }
        return -1;
    }

    private void init() {
        for (int i = 0; i < this.model.length; i++) {
            for (int j = 0; j < this.model[0].length; j++) {
                for (int k = 0; k < this.model[0][0].length; k++) {
                    this.model[i][j][k] = 1;
                }
            }
        }
    }

    private void calculateProbabilities() {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).get(0).equals("republican")) {
                this.republicanProbability++;
            } else {
                this.democratProbability++;
            }
        }

        this.republicanProbability = this.republicanProbability / this.data.size();
        this.democratProbability = this.democratProbability / this.data.size();
    }
}
