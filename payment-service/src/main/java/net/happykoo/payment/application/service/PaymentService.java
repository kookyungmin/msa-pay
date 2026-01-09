package net.happykoo.payment.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.payment.application.port.in.RequestPaymentUseCase;
import net.happykoo.payment.application.port.in.command.RequestPaymentCommand;
import net.happykoo.payment.application.port.out.CreatePaymentPort;
import net.happykoo.payment.domain.Payment;
import net.happykoo.payment.domain.Payment.FranchiseFeeRate;
import net.happykoo.payment.domain.Payment.FranchiseId;
import net.happykoo.payment.domain.Payment.MembershipId;
import net.happykoo.payment.domain.Payment.Price;

@UseCase
@RequiredArgsConstructor
@Transactional
public class PaymentService implements RequestPaymentUseCase {

  private final CreatePaymentPort createPaymentPort;

  @Override
  public Payment requestPayment(RequestPaymentCommand command) {
    //TODO: 멤버쉽 확인, 머니 유효성 확인 (없으면 충전)

    return createPaymentPort.createPayment(
        new MembershipId(command.getRequestMembershipId()),
        new Price(command.getRequestPrice()),
        new FranchiseId(command.getFranchiseId()),
        new FranchiseFeeRate(command.getFranchiseFeeRate())
    );
  }
}
