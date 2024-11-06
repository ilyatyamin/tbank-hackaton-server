package org.example.api_project.repositories;

import org.example.api_project.dao.BudgetRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalcRepository extends JpaRepository<BudgetRequest, Long> {
    BudgetRequest findTopByOrderByIdDesc();
}
