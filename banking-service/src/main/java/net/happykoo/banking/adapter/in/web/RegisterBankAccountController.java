package net.happykoo.banking.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.banking.adapter.in.web.request.RegisterBankAccountRequest;
import net.happykoo.banking.application.port.in.RegisterBankAccountUseCase;
import net.happykoo.banking.application.port.in.command.RegisterBankAccountCommand;
import net.happykoo.banking.domain.RegisteredBankAccount;
import net.happykoo.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//TODO: 원래는 useCase 결과값은 result dto, controller의 결과 값은 response로 변환 과정을 거쳐야 함

@WebAdapter
@RequiredArgsConstructor
public class RegisterBankAccountController {

  private final RegisterBankAccountUseCase registerBankAccountUseCase;

  @PostMapping("/banking/account")
  ResponseEntity<RegisteredBankAccount> registerBankAccount(
      @RequestBody RegisterBankAccountRequest request
  ) {

    RegisterBankAccountCommand command = RegisterBankAccountCommand.builder()
        .membershipId(request.membershipId())
        .bankName(request.bankName())
        .bankAccountNumber(request.bankAccountNumber())
        .build();
    return ResponseEntity.ok(registerBankAccountUseCase.registerBankAccount(command));
  }
}
