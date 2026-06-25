package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionRecord;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transaction-records")
@Tag(name = "收支记录", description = "当前登录用户新增、查询、修改、删除和分页搜索自己的收支记录")
public class TransactionRecordController {
    @Autowired
    private TransactionRecordService transactionRecordService;

    // 新增收支记录
    // 収支記録を追加する
    @Operation(summary = "新增收支记录", description = "为当前登录用户新增一条收入或支出记录，记录所属用户由 token 确定，分类必须属于当前用户。")
    @PostMapping
    public Result addTransactionRecord(HttpServletRequest request, @RequestBody @Valid TransactionRecord record) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.addRecord(userId, record);
    }

    // 查询当前登录用户自己的全部收支记录
    // ログイン中ユーザー本人の全収支記録を取得する
    @Operation(summary = "查询我的全部收支记录", description = "查询当前登录用户自己的全部收入和支出记录。")
    @GetMapping
    public Result findMyRecords(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserId(userId);
    }

    // 修改当前登录用户自己的收支记录
    // ログイン中ユーザー本人の収支記録を更新する
    @Operation(summary = "修改我的收支记录", description = "根据记录 id 修改当前登录用户自己的收支记录，只能修改属于自己的记录。")
    @PutMapping("/{id}")
    public Result updateMyRecord(@Parameter(description = "要修改的收支记录 id")
                                 @PathVariable Integer id,
                                 HttpServletRequest request,
                                 @RequestBody @Valid TransactionRecord record) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.updateByIdAndUserId(id, userId, record);
    }

    // 删除当前登录用户自己的收支记录
    // ログイン中ユーザー本人の収支記録を削除する
    @Operation(summary = "删除我的收支记录", description = "根据记录 id 删除当前登录用户自己的收支记录，只能删除属于自己的记录。")
    @DeleteMapping("/{id}")
    public Result deleteMyRecord(@Parameter(description = "要删除的收支记录 id")
                                 @PathVariable Integer id,
                                 HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.deleteByIdAndUserId(id, userId);
    }

    // 按类型查询自己的收支记录
    // タイプ別に本人の収支記録を取得する
    @Operation(summary = "按类型查询我的收支记录", description = "按 income 或 expense 查询当前登录用户自己的收支记录。")
    @GetMapping("/type/{type}")
    public Result findMyRecordsByType(HttpServletRequest request,
                                      @Parameter(description = "收支类型：income 表示收入，expense 表示支出")
                                      @PathVariable String type) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndType(userId, type);
    }

    // 按日期范围查询自己的收支记录
    // 日付範囲で本人の収支記録を取得する
    @Operation(summary = "按日期范围查询我的收支记录", description = "根据开始日期和结束日期查询当前登录用户自己的收支记录。")
    @GetMapping("/range")
    public Result findMyRecordsByDateRange(HttpServletRequest request,
                                           @Parameter(description = "开始日期，格式：yyyy-MM-dd")
                                           @RequestParam LocalDate startRecordDate,
                                           @Parameter(description = "结束日期，格式：yyyy-MM-dd")
                                           @RequestParam LocalDate endOfRecordDate) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndDateRange(userId, startRecordDate, endOfRecordDate);
    }

    // 按分类 id 查询该分类下的具体收支记录
    // カテゴリIDで、そのカテゴリに属する本人の収支記録を取得する
    @Operation(summary = "按分类查询我的收支记录", description = "根据分类 id 查询当前登录用户在该分类下的收支记录。")
    @GetMapping("/category/{categoryId}")
    public Result findMyRecordsByCategoryId(HttpServletRequest request,
                                            @Parameter(description = "分类 id")
                                            @PathVariable Integer categoryId) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndCategoryId(userId, categoryId);
    }

    //分页，指定记录数
    @Operation(summary = "分页查询我的收支记录", description = "按页码和每页条数分页查询当前登录用户自己的收支记录。")
    @GetMapping("/page")
    public Result findMyRecordsByPage(HttpServletRequest request,
                                      @Parameter(description = "页码，从 1 开始")
                                      @RequestParam Integer page,
                                      @Parameter(description = "每页记录数")
                                      @RequestParam Integer size) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdPage(userId, page, size);
    }

    // 组合条件分页查询收支记录
    @Operation(summary = "组合条件分页搜索我的收支记录", description = "按类型、分类、日期范围组合筛选，并分页查询当前登录用户自己的收支记录。")
    @GetMapping("/search")
    public Result searchMyRecordsByPage(HttpServletRequest request,
                                        @Parameter(description = "收支类型，可选：income 或 expense")
                                        @RequestParam(required = false) String type,
                                        @Parameter(description = "分类 id，可选")
                                        @RequestParam(required = false) Integer categoryId,
                                        @Parameter(description = "开始日期，可选，格式：yyyy-MM-dd")
                                        @RequestParam(required = false) LocalDate startRecordDate,
                                        @Parameter(description = "结束日期，可选，格式：yyyy-MM-dd")
                                        @RequestParam(required = false) LocalDate endOfRecordDate,
                                        @Parameter(description = "页码，从 1 开始")
                                        @RequestParam Integer page,
                                        @Parameter(description = "每页记录数")
                                        @RequestParam Integer size) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.searchByUserIdPage(userId, type, categoryId, startRecordDate, endOfRecordDate, page, size);
    }
}
