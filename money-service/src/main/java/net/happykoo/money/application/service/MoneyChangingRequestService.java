package net.happykoo.money.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.money.application.port.in.IncreaseMoneyRequestUseCase;
import net.happykoo.money.application.port.in.command.IncreaseMoneyRequestCommand;
import net.happykoo.money.application.port.out.ChangeMemberMoneyPort;
import net.happykoo.money.application.port.out.SaveMoneyChangingRequestPort;
import net.happykoo.money.domain.MemberMoney;
import net.happykoo.money.domain.MoneyChangingRequest;
import net.happykoo.money.domain.MoneyChangingRequestType;

@UseCase
@RequiredArgsConstructor
@Transactional
public class MoneyChangingRequestService implements IncreaseMoneyRequestUseCase {

  private final SaveMoneyChangingRequestPort saveMoneyChangingRequestPort;
  private final ChangeMemberMoneyPort changeMemberMoneyPort;

  @Override
  public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {
    //TODO: 1. 고객 정보가 정상인지 확인 (멤버 서비스) -> 아니면 예외 발생

    //TODO: 2. 고객의 연동된 계좌가 있고, 정상적인지 확인 (잔액이 충분한지도) (뱅킹 서비스) -> 아니면 예외 발생

    //TODO: 3. 법인 계좌 상태가 정상인지 확인 (뱅킹 서비스) -> 아니면 예외 발생

    //4. 증액을 위한 기록 -> MoneyChangingRequest 생성
    var moneyChangingRequest = saveMoneyChangingRequestPort.createMoneyChangingRequest(
        new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
        new MoneyChangingRequest.RequestType(MoneyChangingRequestType.INCREASE),
        new MoneyChangingRequest.MoneyAmount(command.getMoneyAmount())
    );

    //TODO: 5. 펌뱅킹 수행 (고객의 연동된 계좌 -> 해피페이 법인 계좌) (뱅킹 서비스)

    //TODO: 6. 펌뱅킹 성공하면, 멤버의 머니도 증액, 실패하면 상태 변경
    boolean isSuccess = true;
    if (isSuccess) {
      changeMemberMoneyPort.increaseMemberMoney(
          new MemberMoney.MembershipId(command.getTargetMembershipId()),
          new MemberMoney.Balance(command.getMoneyAmount())
      );
      moneyChangingRequest.success();
    }

    return saveMoneyChangingRequestPort.updateMoneyChangingStatus(
        new MoneyChangingRequest.MoneyChangingRequestId(
            moneyChangingRequest.getMoneyChangingRequestId()),
        new MoneyChangingRequest.RequestStatus(moneyChangingRequest.getRequestStatus())
    );
  }
}
