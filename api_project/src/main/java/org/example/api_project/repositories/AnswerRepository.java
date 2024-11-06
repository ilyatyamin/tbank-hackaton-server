package org.example.api_project.repositories;

import org.example.api_project.dao.CalcAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<CalcAnswer, Long> {
    boolean existsByRequestId(Long answerId);
    CalcAnswer findCalcAnswerByRequestId(Long answerId);
}
