package net.happykoo.payment.application.port.in;

import net.happykoo.payment.application.port.in.command.RequestPaymentCommand;
import net.happykoo.payment.domain.Payment;

public interface RequestPaymentUseCase {

  Payment requestPayment(RequestPaymentCommand command);
}
