package net.happykoo.banking.application.port.out.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountData {

  private String bankName;           // 은행명 (Ex: 신한은행)
  private String bankCode;           // 은행 표준 코드 (Ex: 088)
  private String bankAccountNumber;  // 계좌번호
  private String accountHolderName;  // 예금주 성명
  private boolean isValid;           // 계좌 유효 여부
}
