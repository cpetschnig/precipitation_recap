package dev.cpetschnig.precipitation_recap.formatter;

import dev.cpetschnig.precipitation_recap.open_meteo.Archive;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.lang.Math.max;

public class CliOutputFormatter {

    static final int ABSCISSA_MAX_ROWS = 3;

    private final Archive archive;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private double maxValue;
    private final double[][] values;
    private Consumer<String> outputFunction;

    public CliOutputFormatter(Archive archive, LocalDate startDate, LocalDate endDate) {
        this.archive = archive;
        this.startDate = startDate;
        this.endDate = endDate;
        maxValue = Double.MIN_VALUE;
        values = new double[endDate.compareTo(startDate) + 1][24];
        buildValues();
        this.outputFunction = System.out::println;
    }

    public void setOutput(Consumer<String> outputFunction) {
        this.outputFunction = outputFunction;
    }

    public void print() {
        StringBuilder[] builderGraphs = new StringBuilder[ABSCISSA_MAX_ROWS];

        iterateOverDays((date, i) -> {
            printHeadlineForDay(date);

            StringBuilder builderLegend = new StringBuilder("    ");

            final double[] valuesOfTheDay = values[i];

            for (int j = 0; j < ABSCISSA_MAX_ROWS; j++) {
                builderGraphs[j] = new StringBuilder("      ");
            }

            Arrays.stream(valuesOfTheDay).forEach(value -> {
                IntStream.range(0, ABSCISSA_MAX_ROWS).forEach(index -> {
                    double halfLimit = (ABSCISSA_MAX_ROWS - index - 1) * maxValue / ABSCISSA_MAX_ROWS;
                    double limit = (ABSCISSA_MAX_ROWS - index - 0.5) * maxValue / ABSCISSA_MAX_ROWS;

                    String emptyPlaceholder = index == ABSCISSA_MAX_ROWS - 1 ? "_" : " ";
                    builderGraphs[index].append(value > limit ? '█' : (value > halfLimit ? '▄' : emptyPlaceholder));
                    builderGraphs[index].append("    ");
                });

                builderLegend.append("%4.1f ".formatted(value));
            });

            Arrays.stream(builderGraphs).map(StringBuilder::toString).forEach(outputFunction);
            outputFunction.accept(builderLegend + "\n");
        });
    }

    private void buildValues() {
        iterateOverDays((date, i) -> {
            double[] result = archive.getPrecipitationForDay(date);
            values[i] = result;

            double resMax = Arrays.stream(result).reduce(0.0, (memo, obj) -> max(obj, memo));
            if (resMax > maxValue) {
                maxValue = resMax;
            }
        });
    }

    private void iterateOverDays(BiConsumer<LocalDate, Integer> biConsumer) {
        LocalDate dateIterator = startDate;
        int i = 0;
        while (!dateIterator.isAfter(endDate)) {
            biConsumer.accept(dateIterator, i);

            i++;
            dateIterator = dateIterator.plusDays(1);
        }
    }

    private void printHeadlineForDay(LocalDate date) {
        outputFunction.accept(date + ":");
    }
}
