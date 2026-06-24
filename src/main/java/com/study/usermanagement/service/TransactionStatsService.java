package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.mapper.TransactionRecordMapper;
import com.study.usermanagement.mapper.TransactionStatsMapper;
import com.study.usermanagement.vo.MonthlyStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class TransactionStatsService {
@Autowired
private TransactionStatsMapper transactionStatsMapper;
@Autowired
private TransactionRecordMapper transactionRecordMapper;

    //根据用户id，收支分类类型，日期年，日期月 统计
    public Result sumAmountByMonthAndType(Integer userId, String type, Integer year, Integer month) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        if(year==null || year<=0){
            return new Result(false,"年份不能为空 且 不能小于零",null);
        }
        if (month==null || (month<=0 || month>12)) {
            return new Result(false,"月份不能为空 且 必须在1~12之间",null);
        }
        return new Result(true,"查询成功",transactionStatsMapper.sumAmountByMonthAndType(userId,type,year,month));

    }

    //根据用户id查询某月 总收入、总支出，以及结余
    public Result getMonthlyStats(Integer userId, Integer year, Integer month){
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if(year==null || year<=0){
            return new Result(false,"年份不能为空 且 不能小于零",null);
        }
        if (month==null || (month<=0 || month>12)) {
            return new Result(false,"月份不能为空 且 必须在1~12之间",null);
        }
        BigDecimal incomeTotal =
                transactionStatsMapper.sumAmountByMonthAndType(userId, "income", year, month);
        BigDecimal expenseTotal =
                transactionStatsMapper.sumAmountByMonthAndType(userId, "expense", year, month);
        BigDecimal balance = incomeTotal.subtract(expenseTotal);
        return new Result(true,"查询成功",new MonthlyStatsVO(incomeTotal,expenseTotal,balance));
    }

    //按支出分类统计,统计某个月，每个支出分类花了多少钱
    public Result getStatsByCategoryAndType(Integer userId,String type, Integer year, Integer month){
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        if(year==null || year<=0){
            return new Result(false,"年份不能为空 且 不能小于零",null);
        }
        if (month==null || (month<=0 || month>12)) {
            return new Result(false,"月份不能为空 且 必须在1~12之间",null);
        }
        return new Result(true,"查询成功",transactionStatsMapper.sumByCategoryAndType(userId, type, year,month));
    }
}
