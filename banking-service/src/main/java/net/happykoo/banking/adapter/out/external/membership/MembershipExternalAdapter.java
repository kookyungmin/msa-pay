package net.happykoo.banking.adapter.out.external.membership;

import lombok.extern.slf4j.Slf4j;
import net.happykoo.banking.application.port.out.RequestMembershipPort;
import net.happykoo.banking.application.port.out.data.MembershipData;
import net.happykoo.banking.domain.RegisteredBankAccount.MembershipId;
import net.happykoo.common.annotation.ExternalSystemAdapter;
import net.happykoo.common.http.CommonHttpClient;
import org.springframework.beans.factory.annotation.Value;

@ExternalSystemAdapter
@Slf4j
public class MembershipExternalAdapter implements RequestMembershipPort {

  private final CommonHttpClient commonHttpClient;
  private final String membershipServiceUrl;

  public MembershipExternalAdapter(
      CommonHttpClient commonHttpClient,
      @Value("${service.membership.url}") String membershipServiceUrl
  ) {
    this.commonHttpClient = commonHttpClient;
    this.membershipServiceUrl = membershipServiceUrl;
  }

  @Override
  public boolean existsMembership(MembershipId membershipId) {
    String url = String.join("/",
        membershipServiceUrl,
        "membership",
        membershipId.value());

    try {
      MembershipData response = commonHttpClient.sendGetRequest(url, MembershipData.class);
      log.info("membership response : {}", response);
      return response.isValid();
    } catch (Exception e) {
      log.error("error occurred when requesting membership : {}", e.getMessage());
      return false;
    }
  }
}
