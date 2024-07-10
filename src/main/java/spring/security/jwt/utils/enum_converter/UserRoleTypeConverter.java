package spring.security.jwt.utils.enum_converter;

import spring.security.jwt.service.model.IamServiceUserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserRoleTypeConverter implements AttributeConverter<IamServiceUserRole, String> {

    @Override
    public String convertToDatabaseColumn(IamServiceUserRole attribute) {
        return attribute.name();
    }

    @Override
    public IamServiceUserRole convertToEntityAttribute(String dbData) {
        return IamServiceUserRole.fromName(dbData);
    }
}