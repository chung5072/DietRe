package com.dietre.db.repository;

import com.dietre.db.entity.DailyHistory;
import com.dietre.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyHistoryRepository extends JpaRepository<DailyHistory, Long> {
    Optional<DailyHistory> findByUserAndDate(User user, LocalDate date);
    Optional<DailyHistory> findByUserIdAndDate(Long UserId, LocalDate date);
}
