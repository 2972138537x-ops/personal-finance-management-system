package com.study.usermanagement.mapper;

import com.study.usermanagement.vo.CategoryStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TransactionStatsMapper {
    // 查询某用户某年某月某类型的总金额
    // 指定ユーザー・年月・タイプの合計金額を取得する
    BigDecimal sumAmountByMonthAndType(@Param("userId") Integer userId,
                                       @Param("type") String type,
                                       @Param("year") Integer year,
                                       @Param("month") Integer month);

    // 按分类统计某用户某年某月某类型的合计金额
    // 指定ユーザー・年月・タイプをカテゴリ別に集計する
    List<CategoryStatsVO> sumByCategoryAndType(
            @Param("userId") Integer userId,
            @Param("type") String type,
            @Param("year") Integer year,
            @Param("month") Integer month
    );
}
