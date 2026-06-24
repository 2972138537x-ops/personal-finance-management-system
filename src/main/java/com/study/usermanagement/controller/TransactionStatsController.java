package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionStatsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-stats")
public class TransactionStatsController {
    @Autowired
    private TransactionStatsService transactionStatsService;
    //月度总览，根据用户id查询某月 总收入、总支出，以及结余
    @GetMapping("/month")
    public Result getMonthlyStats(HttpServletRequest request,
                                  @RequestParam Integer year,
                                  @RequestParam Integer month){
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.getMonthlyStats(userId,year,month);
    }

    @GetMapping("/category/{type}")
    //按type统计,统计某个月，每个type 每个分类名花了多少钱
    public Result getStatsByCategoryAndType(HttpServletRequest request,
                                            @PathVariable String type,
                                            @RequestParam Integer year,
                                            @RequestParam Integer month){
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.getStatsByCategoryAndType(userId, type, year, month);
    }







}
