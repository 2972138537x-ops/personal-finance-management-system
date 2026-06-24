package com.study.usermanagement.mapper;

import com.study.usermanagement.vo.CategoryStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TransactionStatsMapper {
    //根据用户id 分类type  查询某月  收支情况
    BigDecimal sumAmountByMonthAndType(@Param("userId") Integer userId,
                                       @Param("type") String type,
                                       @Param("year") Integer year,
                                       @Param("month") Integer month);

    //按支出分类统计,统计某个月，每个支出分类花了多少钱
    List<CategoryStatsVO> sumByCategoryAndType(
            @Param("userId") Integer userId,
            @Param("type") String type,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

    //统计某年某月，每个收入分类收入多少钱


    //统计最近 6 个月，每个月的收入、支出、结余

    //查询某年某月最大的一笔支出记录

    //统计某个月每个支出分类占总支出的比例
}
