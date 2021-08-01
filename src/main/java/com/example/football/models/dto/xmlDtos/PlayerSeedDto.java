package com.example.football.models.dto.xmlDtos;

import com.example.football.models.entity.enums.Position;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {

    @XmlElement(name = "first-name")
    private String firstName;

    @XmlElement(name = "last-name")
    private String lastName;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "birth-date")
    private String birthDate;

    @XmlElement(name = "position")
    private Position position;

    @XmlElement(name = "town")
    private TownNameDto townNameDto;

    @XmlElement(name = "team")
    private TeamNameDto teamNameDto;

    @XmlElement(name = "stat")
    private StatIdDto statIdDto;

    public PlayerSeedDto() {
    }

    @Size(min = 3)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 3)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TownNameDto getTownNameDto() {
        return townNameDto;
    }

    public void setTownNameDto(TownNameDto townNameDto) {
        this.townNameDto = townNameDto;
    }

    public TeamNameDto getTeamNameDto() {
        return teamNameDto;
    }

    public void setTeamNameDto(TeamNameDto teamNameDto) {
        this.teamNameDto = teamNameDto;
    }

    public StatIdDto getStatIdDto() {
        return statIdDto;
    }

    public void setStatIdDto(StatIdDto statIdDto) {
        this.statIdDto = statIdDto;
    }
}
