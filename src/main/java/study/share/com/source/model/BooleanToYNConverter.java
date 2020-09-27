package study.share.com.source.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/*전역적으로 불린타입 Y,N으로 변경 */
@Converter(autoApply = true)
class BooleanToYNConverter implements AttributeConverter<Boolean, String>{
    @Override
    public String convertToDatabaseColumn(Boolean attribute){
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData){
        return "Y".equals(dbData);
    }
}