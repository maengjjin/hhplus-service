package kr.hhplus.be.server.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RedisKeyFactory {


    // 단일 날짜 상품 랭킹 키를 생성합니다.
    public static String getProductRankKey(LocalDate date) {
        return "product:rank:" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
    }


    // 통합 상품 랭킹 키를 생성합니다.
    public static String getTopProductRankKey(LocalDate date) {
        return "top:product:rank:" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
    }


    // 통합 상품 랭킹 상세 정보 키를 생성합니다.
    public static String getTopProductDetailKey(LocalDate date) {
        return "top:product:rank:detail:" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
    }


}
