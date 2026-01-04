package net.happykoo.banking.application.port.out.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipData {

  private String membershipId;

  private String name;

  private String email;

  private String address;

  private boolean isValid;

  private boolean isCorp;
}
