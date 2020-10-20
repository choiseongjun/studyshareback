package study.share.com.source.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public enum AccountTypeConverter implements AttributeConverter<AccountType, String> {
    ;


    @Override
    public String convertToDatabaseColumn(AccountType attribute) {
        return attribute.name();
    }

    @Override
    public AccountType convertToEntityAttribute(String dbData) {
        return AccountType.ofAccountType(dbData);
    }
}
