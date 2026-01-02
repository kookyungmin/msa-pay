package net.happykoo.banking.application.port.out.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FirmBankingData {

  private boolean isSuccess;
}
