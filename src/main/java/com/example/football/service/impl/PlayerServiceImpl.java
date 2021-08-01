package com.example.football.service.impl;

import com.example.football.models.dto.xmlDtos.PlayersRootSeedDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final static String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";
    private final PlayerRepository playerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final StringBuilder stringBuilder;
    private final TownService townService;
    private final StatService statService;
    private final TeamService teamService;

    public PlayerServiceImpl(PlayerRepository playerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, StringBuilder stringBuilder, TownService townService, StatService statService, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.stringBuilder = stringBuilder;
        this.townService = townService;
        this.statService = statService;
        this.teamService = teamService;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        PlayersRootSeedDto playersRootSeedDto = xmlParser.fromFile(PLAYERS_FILE_PATH, PlayersRootSeedDto.class);
        Set<Player> filteredPlayers = playersRootSeedDto
                .getPlayers()
                .stream()
                .filter(validationUtil::isValid)
                .map(playerSeedDto -> modelMapper.map(playerSeedDto, Player.class))
                .collect(Collectors.toSet());

        playersRootSeedDto.getPlayers()
                .forEach(playerSeedDto -> {
                    Player player = modelMapper.map(playerSeedDto, Player.class);
                    if (filteredPlayers.contains(player)) {
                        Town town = townService.findTownByName(playerSeedDto.getTownNameDto().getName());
                        Team team = teamService.findTeamByName(playerSeedDto.getTeamNameDto().getName());
                        Stat stat = statService.findStatById(playerSeedDto.getStatIdDto().getId());

                        if(town != null && team != null && stat != null ){
                            player.setStat(stat);
                            player.setTown(town);
                            player.setTeam(team);
                            stringBuilder.append(String.format("Successfully imported Player %s %s - %s", player.getFirstName(),player.getLastName(),player.getPosition().name()));
                            playerRepository.save(player);
                        } else {
                            stringBuilder.append("Invalid Player");
                        }
                    } else {
                        stringBuilder.append("Invalid Player");
                    }
                    stringBuilder.append(System.lineSeparator());
                });
        return stringBuilder.toString();
    }

    @Override
    public String exportBestPlayers() {
        List<Player>  players = playerRepository.findAllByBirthDateAfterAndBirthDateBefore(LocalDate.of(1995,1,1),LocalDate.of(2003,1,1));

        return players.stream().map(Player::toString).collect(Collectors.joining("\n"));
    }
}
