package net.happykoo.banking.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.banking.adapter.in.web.request.RequestFirmBankingRequest;
import net.happykoo.banking.application.port.in.RequestFirmBankingUseCase;
import net.happykoo.banking.application.port.in.command.RequestFirmBankingCommand;
import net.happykoo.banking.domain.FirmBankingRequest;
import net.happykoo.common.annotation.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//TODO: 원래는 useCase 결과값은 result dto, controller의 결과 값은 response로 변환 과정을 거쳐야 함

@WebAdapter
@RequiredArgsConstructor
public class RequestFirmBankingController {

  private final RequestFirmBankingUseCase requestFirmBankingUseCase;

  @PostMapping("/banking/firmbanking/request")
  ResponseEntity<FirmBankingRequest> requestFirmBanking(
      @RequestBody RequestFirmBankingRequest request
  ) {
    var command = RequestFirmBankingCommand.builder()
        .fromBankName(request.fromBankName())
        .fromBankAccountNumber(request.fromBankAccountNumber())
        .toBankName(request.toBankName())
        .toBankAccountNumber(request.toBankAccountNumber())
        .moneyAmount(request.moneyAmount())
        .build();

    return ResponseEntity.ok(requestFirmBankingUseCase.requestFirmBanking(command));
  }
}
