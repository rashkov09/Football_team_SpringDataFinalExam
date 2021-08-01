package com.example.football.service.impl;

import com.example.football.models.dto.jsonDtos.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    private final static String TEAMS_FILE_PATH = "src/main/resources/files/json/teams.json";
    private final TeamRepository teamRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final StringBuilder stringBuilder;
    private final TownService townService;

    public TeamServiceImpl(TeamRepository teamRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, StringBuilder stringBuilder, TownService townService) {
        this.teamRepository = teamRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.stringBuilder = stringBuilder;
        this.townService = townService;
    }


    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        TeamSeedDto[] teamSeedDtos =gson.fromJson(readTeamsFileContent(),TeamSeedDto[].class);
        Arrays.stream(teamSeedDtos)
                .forEach(teamSeedDto -> {
                   boolean isValid = validationUtil.isValid(teamSeedDto);
                   if (isValid){
                       Town town = townService.findTownByName(teamSeedDto.getTownName());
                       Team team = modelMapper.map(teamSeedDto,Team.class);
                       team.setTown(town);
                       teamRepository.save(team);
                       stringBuilder.append(String.format("Successfully imported Team %s - %d",team.getName(),team.getFanBase()));
                   } else {
                       stringBuilder.append("Invalid Team");
                   }
                   stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString();
    }

    @Override
    public Team findTeamByName(String name) {
        return teamRepository.findTeamByName(name);
    }
}
