package org.example.api_project.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.example.api_project.dto.InsertResultRequest;

@Data
@Entity(name = "answers")
@Table(name = "answers")
public class CalcAnswer {
    public static CalcAnswer newInstance(
            InsertResultRequest dto
    ) {
        CalcAnswer answer = new CalcAnswer();

        answer.grossMerchandiseValue = dto.gmv();
        answer.purchaseCount = dto.purchaseCount();
        answer.totalCashback = dto.totalCashback();
        answer.requestId = dto.id();

        return answer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "gross_merchandise_value", nullable = false)
    private Double grossMerchandiseValue;

    @Column(name = "purchase_count", nullable = false)
    private Long purchaseCount;

    @Column(name = "total_cashback", nullable = false)
    private Double totalCashback;

    // Связь с таблицей requests
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "request_id", insertable = false, updatable = false)
    private BudgetRequest request;
}
