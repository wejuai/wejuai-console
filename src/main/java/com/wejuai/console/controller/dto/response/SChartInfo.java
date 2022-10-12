package com.wejuai.console.controller.dto.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZM.Wang
 */
public class SChartInfo {

    private final List<String> labels;

    private List<Datasets> datasets;

    static class Datasets {
        private final String label;

        private final List<Long> data;

        public Datasets(String label, List<Long> data) {
            this.label = label;
            this.data = data;
        }

        public String getLabel() {
            return label;
        }

        public List<Long> getData() {
            return data;
        }
    }

    public SChartInfo(List<String> labels) {
        this.labels = labels;
    }

    public SChartInfo addDates(String label, List<Long> data) {
        if (datasets == null) {
            this.datasets = new ArrayList<>();
        }
        this.datasets.add(new Datasets(label, data));
        return this;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Datasets> getDatasets() {
        return datasets;
    }
}
