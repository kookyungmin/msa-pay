package net.happykoo.payment.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.WebAdapter;
import net.happykoo.payment.adapter.in.web.request.PaymentRequest;
import net.happykoo.payment.application.port.in.RequestPaymentUseCase;
import net.happykoo.payment.application.port.in.command.RequestPaymentCommand;
import net.happykoo.payment.domain.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class RequestPaymentController {

  private final RequestPaymentUseCase requestPaymentUseCase;

  @PostMapping("/payment/request")
  ResponseEntity<Payment> requestPayment(
      @RequestBody PaymentRequest request
  ) {
    var payment = requestPaymentUseCase.requestPayment(
        new RequestPaymentCommand(
            request.requestMembershipId(),
            request.requestPrice(),
            request.franchiseId(),
            request.franchiseFeeRate()
        )
    );
    return ResponseEntity.ok(payment);
  }
}
