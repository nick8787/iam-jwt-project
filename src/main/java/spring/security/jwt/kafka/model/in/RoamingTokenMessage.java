package spring.security.jwt.kafka.model.in;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flywaydb.core.internal.parser.TokenType;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoamingTokenMessage extends IamInMessage {
    private Integer tenantId;

    private CountryCode countryCode;
    private String partyId;
    private String uid;
    private TokenType tokenType;
    private String contractUid;
    private String issuer;
    private Long updated; // epoch millis

    private String visualNumber;
    private String groupUid;
    private String language;

}
