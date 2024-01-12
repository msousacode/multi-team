package com.multiteam.modules.patient.service;

import com.multiteam.modules.patient.controller.dto.ChartDTO;
import com.multiteam.modules.patient.controller.dto.DatasetDTO;
import com.multiteam.modules.program.entity.BehaviorCollect;
import com.multiteam.modules.program.service.BehaviorCollectService;
import com.multiteam.modules.program.service.FolderService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChartService {

    private final BehaviorCollectService behaviorCollectService;
    private final FolderService folderService;

    public ChartService(
            BehaviorCollectService behaviorCollectService,
            FolderService folderService) {
        this.behaviorCollectService = behaviorCollectService;
        this.folderService = folderService;
    }

    public ChartDTO findChart(UUID patientId) {

        // Criar um formato personalizado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        //1 - Todos os folders do aprendiz TODO filtrar somente as pastas diferentes de NAO_ALOCADO
        var folders = folderService.getFolderByPatientId(patientId);

        List<BehaviorCollect> behaviorCollects = new ArrayList<>();

        folders.forEach(folder ->
                folder.getFolderPrograms()
                        .forEach(
                                p -> behaviorCollects.addAll(behaviorCollectService.findBehaviorsCollects(p.getProgram().getId()))
                        )
        );

        var chart = new ChartDTO();
        chart.setTitle("Titulo");

        behaviorCollects.forEach(behaviorCollect -> {

            var dataSetNaoFez = new DatasetDTO();
            dataSetNaoFez.setBackgroundColor("rgb(247, 59, 70)");
            dataSetNaoFez.setBorderColor("rgb(247, 59, 70)");

            var dataSetSemAjuda = new DatasetDTO();
            dataSetSemAjuda.setBackgroundColor("rgb(70, 134, 0)");
            dataSetSemAjuda.setBorderColor("rgb(70, 134, 0)");

            var dataSetComAjuda = new DatasetDTO();
            dataSetComAjuda.setBackgroundColor("rgb(255, 205, 86)");
            dataSetComAjuda.setBorderColor("rgb(255, 205, 86)");

            //constroi as labels
            Set<String> labels = new HashSet<>();

            List<Integer> countNaoFez = new ArrayList<>();
            List<Integer> countSemAjuda = new ArrayList<>();
            List<Integer> countComAjuda = new ArrayList<>();

            behaviorCollects.forEach(b -> {

                labels.add(b.getCollectionDate().format(formatter));

                countNaoFez.add(behaviorCollectService.findResponseCount(b.getCollectionDate(), "nao-fez"));

                countSemAjuda.add(behaviorCollectService.findResponseCount(b.getCollectionDate(), "sem-ajuda"));

                countComAjuda.add(behaviorCollectService.findResponseCount(b.getCollectionDate(), "com-ajuda"));
            });

            dataSetComAjuda.setLabel("Com Ajuda");
            dataSetComAjuda.setData(countComAjuda);

            dataSetSemAjuda.setLabel("Sem Ajuda");
            dataSetSemAjuda.setData(countSemAjuda);

            dataSetNaoFez.setLabel("Não Fez");
            dataSetNaoFez.setData(countNaoFez);

            chart.setLabels(labels);
            chart.setDatasets(List.of(dataSetNaoFez, dataSetSemAjuda, dataSetComAjuda));
        });

        return chart;
    }
}
