package kr.hhplus.be.server.domain.coupon;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CouponStatusConverter implements AttributeConverter<CouponStatus, String> {


    @Override
    public String convertToDatabaseColumn(CouponStatus attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public CouponStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? CouponStatus.fromCode(dbData) : null;
    }

}
