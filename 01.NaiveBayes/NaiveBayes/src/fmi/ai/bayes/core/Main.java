package fmi.ai.bayes.core;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final Scanner in = new Scanner(System.in);
        final Model m = new Model();
        m.naiveBayes();
    }
}
