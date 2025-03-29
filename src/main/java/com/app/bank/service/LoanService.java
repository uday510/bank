package com.app.bank.service;

import com.app.bank.dto.LoanResponse;
import com.app.bank.model.Loans;
import com.app.bank.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public List<Loans> getLoanDetails(long customerId) {
        return loanRepository.findByCustomerIdOrderByStartDateDesc(customerId);
    }

    public List<LoanResponse> getLoanDetailsWithDTO(long customerId) {
        return Optional.ofNullable(loanRepository.findByCustomerIdOrderByStartDateDesc(customerId))
                .orElse(List.of())
                .stream()
                .map(loan -> new LoanResponse(
                        loan.getLoanNumber(),
                        loan.getCustomerId(),
                        loan.getStartDate(),
                        loan.getLoanType(),
                        loan.getTotalLoan(),
                        loan.getAmountPaid(),
                        loan.getOutstandingAmount()
                ))
                .toList();
    }
}
