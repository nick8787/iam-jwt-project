package spring.security.jwt.kafka.model.out;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationMessage extends IamOutMessage implements Serializable {
    private Integer id;
    private String name;
    private CountryCode countryCode;
    private Long updatedAt;
    private Boolean deleted;

    private Integer tenantId;

}
