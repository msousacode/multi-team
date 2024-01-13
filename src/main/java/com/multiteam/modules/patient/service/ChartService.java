package com.multiteam.modules.patient.service;

import com.multiteam.modules.patient.controller.dto.ChartDTO;
import com.multiteam.modules.patient.controller.dto.DatasetDTO;
import com.multiteam.modules.program.entity.BehaviorCollect;
import com.multiteam.modules.program.service.BehaviorCollectService;
import com.multiteam.modules.program.service.FolderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChartService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    private final BehaviorCollectService behaviorCollectService;
    private final FolderService folderService;

    public ChartService(
            BehaviorCollectService behaviorCollectService,
            FolderService folderService) {
        this.behaviorCollectService = behaviorCollectService;
        this.folderService = folderService;
    }

    public ChartDTO findChart(UUID patientId) {
        List<BehaviorCollect> behaviorCollects = getBehaviorCollects(patientId);

        Set<LocalDateTime> dateCollects = behaviorCollects.stream()
                .map(BehaviorCollect::getCollectionDate)
                .collect(Collectors.toSet());

        List<LocalDateTime> dateCollectsSorted = dateCollects.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        Map<String, List<Integer>> organizedData = organizeData(dateCollectsSorted);

        ChartDTO chart = createChart("Titulo", organizedData);

        return chart;
    }

    private List<BehaviorCollect> getBehaviorCollects(UUID patientId) {
        return folderService.getFolderByPatientId(patientId).stream()
                .flatMap(folder -> folder.getFolderPrograms().stream())
                .flatMap(program -> behaviorCollectService.findBehaviorsCollects(program.getProgram().getId()).stream())
                .collect(Collectors.toList());
    }

    private Map<String, List<Integer>> organizeData(List<LocalDateTime> dateCollects) {
        Map<String, List<Integer>> organizedData = new HashMap<>();

        dateCollects.forEach(date -> {
            List<Integer> counts = Arrays.asList(
                    behaviorCollectService.findResponseCount(date, "nao-fez"),
                    behaviorCollectService.findResponseCount(date, "sem-ajuda"),
                    behaviorCollectService.findResponseCount(date, "com-ajuda")
            );

            organizedData.put(date.format(formatter), counts);
        });

        return organizedData;
    }

    private ChartDTO createChart(String title, Map<String, List<Integer>> organizedData) {
        ChartDTO chart = new ChartDTO();
        chart.setTitle(title);

        List<Integer> naoFez = new ArrayList<>();
        List<Integer> comAjuda = new ArrayList<>();
        List<Integer> semAjuda = new ArrayList<>();

        organizedData.forEach((date, values) -> {
            naoFez.add(values.get(0));
            semAjuda.add(values.get(1));
            comAjuda.add(values.get(2));
        });

        DatasetDTO dataSetNaoFez = createDataset("Não Ajuda", "rgb(247, 59, 70)", naoFez);
        DatasetDTO dataSetSemAjuda = createDataset("Sem Ajuda", "rgb(70, 134, 0)", semAjuda);
        DatasetDTO dataSetComAjuda = createDataset("Com Ajuda", "rgb(255, 205, 86)", comAjuda);

        chart.setLabels(new HashSet<>(organizedData.keySet()));
        chart.setDatasets(List.of(dataSetNaoFez, dataSetSemAjuda, dataSetComAjuda));

        return chart;
    }

    private DatasetDTO createDataset(String label, String color, List<Integer> data) {
        DatasetDTO dataset = new DatasetDTO();
        dataset.setLabel(label);
        dataset.setBackgroundColor(color);
        dataset.setBorderColor(color);
        dataset.setData(data);
        return dataset;
    }
}
