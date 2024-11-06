package org.example.api_project.controllers;

import org.example.api_project.dto.*;
import org.example.api_project.services.CalcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BudgetController {
    private final CalcService calcService;

    public BudgetController(CalcService calcService) {
        this.calcService = calcService;
    }

    @PostMapping("/budget-calculation")
    public ResponseEntity<GetIdResponse> makeBudgetCalculation(
            @RequestBody MakeCalcS3Request request
    ) {
        var answer = calcService.makeCalc(request);
        return ResponseEntity.status(answer.status()).body(answer.info().orElse(null));
    }

    @GetMapping("/budget-calculation/{id}")
    public ResponseEntity<GetBudgetCalculationResponse> getBudgetCalculation(
            @PathVariable long id) {
        var answer = calcService.getCalc(id);
        return ResponseEntity.status(answer.status()).body(answer.info().orElse(null));
    }

    @GetMapping("/get-calculations-history")
    public ResponseEntity<List<GetCalculationHistoryResponse>> getCalculationsHistory() {
        var answer = calcService.getHistory();

        return ResponseEntity.status(answer.status()).body(answer.info().orElse(null));
    }

    @DeleteMapping("/delete-calculations")
    public ResponseEntity<?> deleteCalculation(
            @RequestBody GetIdResponse response) {
        var answer = calcService.deleteCalc(response.id());
        return ResponseEntity.status(answer.status()).body(answer.info().orElse(null));
    }

    @PostMapping("/insert-result")
    public ResponseEntity<?> insertResult(@RequestBody InsertResultRequest request) {
        var answer = calcService.insertResult(request);
        return ResponseEntity.status(answer.status()).body(answer.info().orElse(null));
    }

    @PostMapping("/budget-calculation-file")
    public ResponseEntity<GetIdResponse> getBudgetCalculation(
            @RequestBody MakeCalcFileRequest request) {
        var answer = calcService.makeCalcByFile(request);
        return ResponseEntity.status(answer.status()).body(answer.info().orElse(null));
    }
}
