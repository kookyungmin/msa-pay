package net.happykoo.money.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.WebAdapter;
import net.happykoo.money.adapter.in.web.request.IncreaseMoneyRequest;
import net.happykoo.money.application.port.in.IncreaseMoneyRequestUseCase;
import net.happykoo.money.application.port.in.command.IncreaseMoneyRequestCommand;
import net.happykoo.money.domain.MoneyChangingRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class RequestMoneyChangingController {

  private final IncreaseMoneyRequestUseCase increaseMoneyRequestUseCase;

  @PostMapping("/money/increase")
  ResponseEntity<MoneyChangingRequest> increaseMoneyRequest(
      @RequestBody IncreaseMoneyRequest request
  ) {
    return ResponseEntity.ok(increaseMoneyRequestUseCase.increaseMoneyRequest(
        new IncreaseMoneyRequestCommand(
            request.targetMembershipId(),
            request.moneyAmount()
        )
    ));
  }
}
