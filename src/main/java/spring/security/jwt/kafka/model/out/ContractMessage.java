package spring.security.jwt.kafka.model.out;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractMessage extends IamOutMessage implements Serializable {
    private Integer id;
    private String uuid;
    private Boolean accessFullTenant;

    private Long updatedAt;

    private Integer partnerId;
    private CountryCode partnerCountryCode;
    private String partnerPartyId;

    private Integer tenantId;
    private Set<Integer> organizationIds;
    private Set<Integer> broadcastPartnerIds;

}
