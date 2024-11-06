package org.example.api_project.services;

import org.example.api_project.dao.BudgetRequest;
import org.example.api_project.dao.CalcAnswer;
import org.example.api_project.dto.*;
import org.example.api_project.repositories.AnswerRepository;
import org.example.api_project.repositories.CalcRepository;
import org.example.api_project.utils.CalcStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalcService {
    private final static String URL_MODEL = "http://localhost:10001/fitpredict";

    private final static String OK_MESSAGE = "OK";

    private final CalcRepository calcRepository;
    private final AnswerRepository answerRepository;
    private final S3Service s3Service;
    private final NetworkService networkService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CalcService(CalcRepository calcRepository,
                       AnswerRepository answerRepository,
                       S3Service s3Service,
                       NetworkService networkService) {
        this.calcRepository = calcRepository;
        this.answerRepository = answerRepository;
        this.s3Service = s3Service;
        this.networkService = networkService;
    }

    public ResponseInfo<GetIdResponse> makeCalc(MakeCalcS3Request request) {
        // 1. добавить в БД InProcess
        calcRepository.save(BudgetRequest.newInstance(request));

        // 2. Сходить в S3 и получить файл
        var result = s3Service.downloadFile(request.getClientsFileKey());

        if (result.status() != HttpStatus.OK || result.info().isEmpty()) {
            return new ResponseInfo<>(result.status(),
                    Optional.empty(),
                    result.message());
        }

        // 2. Отослать к питону, получить запрос и отдать его
        GetIdResponse response = new GetIdResponse(calcRepository.findTopByOrderByIdDesc().getId());

        var responseToModel = new ModelAnswerRequest(
                response.id(),
                request.getBrandId(),
                formatter.format(request.getOpenDate()),
                request.getCbPercent(),
                result.info().get().data());
        var modelResult = networkService.makeResponse(URL_MODEL, responseToModel);
        if (modelResult.status() != HttpStatus.OK) {
            return new ResponseInfo<>(modelResult.status(),
                    Optional.empty(),
                    modelResult.message());
        }

        return new ResponseInfo<>(HttpStatus.OK, Optional.of(response), "OK");
    }

    public ResponseInfo<GetBudgetCalculationResponse> getCalc(long id) {
        if (answerRepository.existsByRequestId(id)) {
            CalcAnswer answer = answerRepository.findCalcAnswerByRequestId(id);

            GetBudgetCalculationResponse response = new GetBudgetCalculationResponse(
                    answer.getGrossMerchandiseValue(),
                    answer.getPurchaseCount(),
                    answer.getTotalCashback()
            );

            return new ResponseInfo<>(HttpStatus.OK,
                    Optional.of(response),
                    OK_MESSAGE);
        } else {
            return new ResponseInfo<>(HttpStatus.NOT_FOUND,
                    Optional.empty(),
                    "Not found");
        }
    }

    public ResponseInfo<List<GetCalculationHistoryResponse>> getHistory() {
        List<GetCalculationHistoryResponse> responses = new ArrayList<>();

        // select all
        for (BudgetRequest request : calcRepository.findAll()) {
            CalcAnswer answer = answerRepository.findCalcAnswerByRequestId(request.getId());

            if (answer != null) {
                responses.add(new GetCalculationHistoryResponse(
                        formatter.format(request.getRequestTime()),
                        request.getBrandId(),
                        formatter.format(request.getOfferBeginTime()),
                        request.getCashbackPercent(),
                        request.getCalcStatus(),
                        answer.getGrossMerchandiseValue(),
                        answer.getPurchaseCount(),
                        answer.getTotalCashback()
                ));
            } else {
                responses.add(new GetCalculationHistoryResponse(
                        formatter.format(request.getRequestTime()),
                        request.getBrandId(),
                        formatter.format(request.getOfferBeginTime()),
                        request.getCashbackPercent(),
                        request.getCalcStatus(),
                        null, null, null
                ));
            }
        }

        return new ResponseInfo<>(HttpStatus.OK, Optional.of(responses), "OK");
    }

    public ResponseInfo<?> deleteCalc(long id) {
        if (calcRepository.existsById(id)) {
            calcRepository.deleteById(id);
            return new ResponseInfo<>(HttpStatus.OK, Optional.empty(), "OK");
        } else {
            return new ResponseInfo<>(HttpStatus.NOT_FOUND, Optional.empty(), "Not found");
        }
    }

    public ResponseInfo<?> insertResult(InsertResultRequest request) {
        if (calcRepository.existsById(request.id()) &&
                calcRepository.getReferenceById(request.id()).getCalcStatus() == CalcStatus.IN_PROCESS) {
            answerRepository.save(CalcAnswer.newInstance(request));
            return new ResponseInfo<>(HttpStatus.OK, Optional.empty(), "OK");
        }

        return new ResponseInfo<>(HttpStatus.BAD_REQUEST, Optional.empty(), "bad request");
    }

    public ResponseInfo<GetIdResponse> makeCalcByFile(MakeCalcFileRequest request) {
        // 1. добавить в БД InProcess
        calcRepository.save(BudgetRequest.fromMakeCalcFileRequest(request));

        // 2. Сходить в S3 и получить файл
        String content;
        try {
            byte[] targetArray = request.getFile().getBytes();
            content = new String(targetArray, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new ResponseInfo<>(HttpStatus.BAD_REQUEST,
                    Optional.empty(),
                    "Problem with data (conversion file to byte array)");
        }
        // 2. Отослать к питону, получить запрос и отдать его
        GetIdResponse response = new GetIdResponse(calcRepository.findTopByOrderByIdDesc().getId());

        var responseToModel = new ModelAnswerRequest(
                response.id(),
                request.getBrandId(),
                formatter.format(request.getOpenDate()),
                request.getCbPercent(),
                content);

        var modelResult = networkService.makeResponse(URL_MODEL, responseToModel);
        if (modelResult.status() != HttpStatus.OK) {
            return new ResponseInfo<>(modelResult.status(),
                    Optional.empty(),
                    modelResult.message());
        }

        return new ResponseInfo<>(HttpStatus.OK, Optional.of(response), "OK");
    }
}
