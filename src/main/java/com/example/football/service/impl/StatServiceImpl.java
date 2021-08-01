package com.example.football.service.impl;


import com.example.football.models.dto.xmlDtos.StatsRootSeedDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {
private final static String STATS_FILE_PATH = "src/main/resources/files/xml/stats.xml";
private final StatRepository statRepository;
private final XmlParser xmlParser;
private final ModelMapper modelMapper;
private final ValidationUtil validationUtil;
private final StringBuilder stringBuilder;

    public StatServiceImpl(StatRepository statRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, StringBuilder stringBuilder) {
        this.statRepository = statRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.stringBuilder = stringBuilder;
    }

    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STATS_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StatsRootSeedDto statRootSeedDto = xmlParser.fromFile(STATS_FILE_PATH,StatsRootSeedDto.class);
       Set<Stat> filteredStats = statRootSeedDto
                .getStats()
               .stream()
               .filter(validationUtil::isValid)
               .map(statSeedDto-> modelMapper.map(statSeedDto,Stat.class))
               .collect(Collectors.toSet());
       statRootSeedDto.getStats()
               .forEach(statSeedDto -> {
                   Stat stat = modelMapper.map(statSeedDto,Stat.class);
                   if(filteredStats.contains(stat)){
                       stringBuilder.append(String.format("Successfully imported Stat %.2f-%.2f-%.2f",stat.getPassing(),stat.getShooting(),stat.getEndurance()));
                       statRepository.save(stat);
                   } else {
                       stringBuilder.append("Invalid Stat");
                   }
                   stringBuilder.append(System.lineSeparator());
               });
        return stringBuilder.toString();
    }

    @Override
    public Stat findStatById(Long id) {
        return statRepository.findStatById(id);
    }
}
