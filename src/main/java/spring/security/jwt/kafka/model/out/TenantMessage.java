package spring.security.jwt.kafka.model.out;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantMessage extends IamOutMessage implements Serializable {
    private Integer id;
    private String name;
    private CountryCode countryCode;
    private String partyId;
    private Long updatedAt;

    private Boolean active;
    private Boolean deleted;

}
