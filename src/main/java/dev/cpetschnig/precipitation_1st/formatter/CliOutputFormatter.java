package dev.cpetschnig.precipitation_1st.formatter;

import dev.cpetschnig.precipitation_1st.open_meteo.Archive;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.max;

public class CliOutputFormatter {

    private final Archive archive;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private double maxValue;
    private final double[][] values;


    public CliOutputFormatter(Archive archive, LocalDate startDate, LocalDate endDate) {
        this.archive = archive;
        this.startDate = startDate;
        this.endDate = endDate;
        maxValue = Double.MIN_VALUE;
        values = new double[endDate.compareTo(startDate) + 1][24];
        buildValues();
    }

    public void print() {
        LocalDate dateIterator = startDate;
        int i = 0;
        while (!dateIterator.isAfter(endDate)) {
            System.out.println(dateIterator + ":");

            StringBuilder builderLegend = new StringBuilder("    ");

            int segments = 3;
            final double[] valuesOfTheDay = values[i];
            StringBuilder[] builderGraphs = new StringBuilder[segments];

            for (int j = 0; j < segments; j++) {
                builderGraphs[j] = new StringBuilder("      ");
            }

            Arrays.stream(valuesOfTheDay).forEach(value -> {

                IntStream.range(0, segments).forEach(index -> {

                    double limit = (segments - index - 1) * maxValue / segments;

                    String emptyPlaceholder = index == segments - 1 ? "_" : " ";
                    builderGraphs[index].append(value > limit ? '█' : emptyPlaceholder);
                    builderGraphs[index].append("    ");
                });

                builderLegend.append("%4.1f ".formatted(value));
            });

            Arrays.stream(builderGraphs).forEach(System.out::println);
            System.out.println(builderLegend + "\n");

            i++;
            dateIterator = dateIterator.plusDays(1);
        }
    }

    private void buildValues() {
        LocalDate dateIterator = startDate;
        int i = 0;
        while (!dateIterator.isAfter(endDate)) {
            double[] result = archive.getPrecipitationForDay(dateIterator);
            values[i] = result;

            double resMax = Arrays.stream(result).reduce(0.0, (memo, obj) -> max(obj, memo));
            if (resMax > maxValue) {
                maxValue = resMax;
            }

            i++;
            dateIterator = dateIterator.plusDays(1);
        }
    }
}
