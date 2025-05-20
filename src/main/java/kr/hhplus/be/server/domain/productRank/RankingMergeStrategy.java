package kr.hhplus.be.server.domain.productRank;

import java.util.List;
import lombok.Getter;

@Getter
public class RankingMergeStrategy {

    private List<String> existingKeys;

    public MergeType mergeType;

    public RankingMergeStrategy(List<String> existingKeys, MergeType mergeType) {
        this.existingKeys = existingKeys;
        this.mergeType = mergeType;
    }


    public enum MergeType {
        COPY, UNION_TWO, UNION_MULTIPLE;
    }


}
