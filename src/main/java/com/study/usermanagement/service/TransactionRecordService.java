package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.TransactionRecord;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import com.study.usermanagement.mapper.TransactionRecordMapper;
import com.study.usermanagement.vo.TransactionRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionRecordService {
    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    // 新增收支记录：先确认分类属于当前用户，再保存记录
    // 収支記録追加：カテゴリがログイン中ユーザーのものか確認してから保存する
    public Result addRecord(Integer userId, TransactionRecord record) {
        if (record == null) {
            return new Result(false, "请求体不能为空", null);
        }
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        Integer categoryId = record.getCategoryId();
        if (categoryId == null) {
            return new Result(false, "分类id不能为空", null);
        }
        TransactionCategory category = findByIdAndUserId(categoryId, userId);
        if (category == null) {
            return new Result(false, "该分类id不存在 或 该分类id不属于该用户", null);
        }
        String type = record.getType();
        BigDecimal amount = record.getAmount();
        LocalDate recordDate = record.getRecordDate();
        if (type == null || type.isEmpty()) {
            return new Result(false, "类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        if (!type.equals(category.getType())) {
            return new Result(false, "记录类型必须和分类类型一致", null);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Result(false, "金额不能为空 且 必须大于0", null);
        }
        if (recordDate == null) {
            return new Result(false, "日期不能为空", null);
        }
        record.setUserId(userId);
        int rows = transactionRecordMapper.insert(record);
        if (rows > 0) {
            return new Result(true, "新增收支记录成功",
                    new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(false, "新增收支记录失败", null);
    }

    // 查询当前登录用户自己的全部收支记录，并转换成 VO 返回
    // ログイン中ユーザー本人の全収支記録を取得し、VO に変換して返す
    public Result findByUserId(Integer userId) {
        if (userId == null) {
            return new Result(false, "登录状态异常，请重新登录", null);
        }
        List<TransactionRecord> records = transactionRecordMapper.findByUserId(userId);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            TransactionCategory category = findByIdAndUserId(record.getCategoryId(), userId);
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(true, "查询成功", recordsVO);
    }

    // 修改当前登录用户自己的收支记录：只能修改属于自己的记录
    // ログイン中ユーザー本人の収支記録を更新する：本人の記録だけ更新できる
    public Result updateByIdAndUserId(Integer id, Integer userId, TransactionRecord record) {
        if (record == null) {
            return new Result(false, "请求体不能为空", null);
        }
        String type = record.getType();
        BigDecimal amount = record.getAmount();
        LocalDate recordDate = record.getRecordDate();
        if (id == null) {
            return new Result(false, "收支记录id不能为空", null);
        }
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        Integer categoryId = record.getCategoryId();
        if (categoryId == null) {
            return new Result(false, "分类id不能为空", null);
        }
        TransactionCategory category = findByIdAndUserId(categoryId, userId);
        if (category == null) {
            return new Result(false, "该分类id不存在 或 该分类id不属于该用户", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        if (!type.equals(category.getType())) {
            return new Result(false, "记录类型必须和分类类型一致", null);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Result(false, "金额不能为空 且 必须大于0", null);
        }
        if (recordDate == null) {
            return new Result(false, "日期不能为空", null);
        }
        int rows = transactionRecordMapper.updateByIdAndUserId(id, userId, record);
        if (rows > 0) {
            return new Result(true, "修改收支记录成功", new TransactionRecordVO(id, category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(false, "修改收支记录失败", null);
    }

    // 删除当前登录用户自己的收支记录：用记录 id 和 userId 双重限制
    // ログイン中ユーザー本人の収支記録を削除する：記録IDとuserIdで本人の記録に限定する
    public Result deleteByIdAndUserId(Integer id, Integer userId) {
        if (id == null) {
            return new Result(false, "收支记录id不能为空", null);
        }
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        int rows = transactionRecordMapper.deleteByIdAndUserId(id, userId);
        if (rows > 0) {
            return new Result(true, "删除收支记录成功", id);
        }
        return new Result(false, "删除收支记录失败", null);
    }

    // 按类型查询自己的收支记录：type 只能是 income 或 expense
    // タイプ別に本人の収支記録を取得する：type は income または expense のみ
    public Result findByUserIdAndType(Integer userId, String type) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        List<TransactionRecord> records = transactionRecordMapper.findByUserIdAndType(userId, type);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            TransactionCategory category = findByIdAndUserId(record.getCategoryId(), userId);
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(true, "根据收支类型，查询记录成功", recordsVO);
    }

    // 按日期范围查询自己的收支记录：开始日期不能晚于结束日期
    // 日付範囲で本人の収支記録を取得する：開始日は終了日より後にできない
    public Result findByUserIdAndDateRange(Integer userId, LocalDate startRecordDate, LocalDate endOfRecordDate) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if ((startRecordDate == null) || (endOfRecordDate == null)) {
            return new Result(false, "日期不能为空", null);
        }
        if (startRecordDate.isAfter(endOfRecordDate)) {
            return new Result(false, "开始日期不能晚于结束日期", null);
        }
        List<TransactionRecord> records = transactionRecordMapper.findByUserIdAndDateRange(userId, startRecordDate, endOfRecordDate);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            TransactionCategory category = findByIdAndUserId(record.getCategoryId(), userId);
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(true, "按日期范围查询成功", recordsVO);
    }

    // 检查分类 id 是否属于当前用户
    // カテゴリIDがログイン中ユーザーのものか確認する
    public TransactionCategory findByIdAndUserId(Integer categoryId, Integer userId) {
        return transactionCategoryMapper.findByIdAndUserId(categoryId, userId);
    }

    // 查询某个分类下面的具体收支记录
    // 指定カテゴリに属する具体的な収支記録を取得する
    public Result findByUserIdAndCategoryId(Integer userId, Integer categoryId) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (categoryId == null) {
            return new Result(false, "分类id不能为空", null);
        }
        TransactionCategory category = findByIdAndUserId(categoryId, userId);
        if (category == null) {
            return new Result(false, "该分类id不存在 或 该分类id不属于该用户", null);
        }
        List<TransactionRecord> records = transactionRecordMapper.findByUserIdAndCategoryId(userId, categoryId);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(true, "根据分类id，查询所有记录成功", recordsVO);
    }
}
