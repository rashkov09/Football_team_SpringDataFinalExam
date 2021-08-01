package com.example.football.service.impl;

import com.example.football.models.dto.jsonDtos.TownsSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class TownServiceImpl implements TownService {
    private final static String TOWNS_FILE_PATH = "src/main/resources/files/json/towns.json";
    private final TownRepository townRepository;
    private final Gson gson;
    private final StringBuilder stringBuilder;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, Gson gson, StringBuilder stringBuilder, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.stringBuilder = stringBuilder;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        TownsSeedDto[] townsSeedDtos = gson.fromJson(readTownsFileContent(),TownsSeedDto[].class);
        Arrays.stream(townsSeedDtos)
                .forEach(townsSeedDto -> {
                    boolean isValid = validationUtil.isValid(townsSeedDto);
                    if (isValid){
                        townRepository.save(modelMapper.map(townsSeedDto,Town.class));
                        stringBuilder.append(String.format("Successfully imported Town %s - %d",townsSeedDto.getName(),townsSeedDto.getPopulation()));
                    } else {
                        stringBuilder.append("Invalid Town");
                    }
                    stringBuilder.append(System.lineSeparator());
                });
        return stringBuilder.toString();
    }

    @Override
    public Town findTownByName(String townName) {
        return townRepository.findTownByName(townName);
    }
}
