package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    @Query("SELECT p FROM Player p WHERE p.birthDate > ?1 and p.birthDate < ?2 ORDER BY p.stat.shooting desc ,p.stat.passing desc ,p.stat.endurance desc , p.lastName")
    List<Player> findAllByBirthDateAfterAndBirthDateBefore(LocalDate before, LocalDate after);

}
