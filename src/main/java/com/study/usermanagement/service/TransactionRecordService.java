package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.TransactionRecord;
import com.study.usermanagement.exception.BusinessException;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import com.study.usermanagement.mapper.TransactionRecordMapper;
import com.study.usermanagement.vo.PageVO;
import com.study.usermanagement.vo.TransactionRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
// 收支记录业务层：处理记录的增删改查、分页搜索和金额校验
// 収支記録業務層：記録の追加・削除・更新・検索、ページング検索、金額チェックを処理する
public class TransactionRecordService {
    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    // 新增收支记录：先确认分类属于当前用户，再保存记录
    // 収支記録追加：カテゴリがログイン中ユーザーのものか確認してから保存する
    public Result addRecord(Integer userId,
                            TransactionRecord record) {
        if (record == null) {
            throw new BusinessException("请求体不能为空");
        }
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        Integer categoryId = record.getCategoryId();
        if (categoryId == null) {
            throw new BusinessException("分类id不能为空");
        }
        TransactionCategory category = findByIdAndUserId(categoryId, userId);
        if (category == null) {
            throw new BusinessException("该分类id不存在 或 该分类id不属于该用户");
        }
        String type = record.getType();
        BigDecimal amount = record.getAmount();
        LocalDate recordDate = record.getRecordDate();
        if (type == null || type.isEmpty()) {
            throw new BusinessException("类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
        }
        if (!type.equals(category.getType())) {
            throw new BusinessException("记录类型必须和分类类型一致");
        }
        // 金额必须是正数，避免保存 0 或负数的收支记录
        // 金額は正数のみ許可し、0 やマイナスの収支記録を保存しない
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("金额不能为空 且 必须大于0");
        }
        if (recordDate == null) {
            throw new BusinessException("日期不能为空");
        }
        record.setUserId(userId);
        int rows = transactionRecordMapper.insert(record);
        if (rows > 0) {
            return new Result(true, "新增收支记录成功",
                    new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        throw new BusinessException("新增收支记录失败");
    }

    // 查询当前登录用户自己的全部收支记录，并转换成 VO 返回
    // ログイン中ユーザー本人の全収支記録を取得し、VO に変換して返す
    public Result findByUserId(Integer userId) {
        if (userId == null) {
            throw new BusinessException("登录状态异常，请重新登录");
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
    public Result updateByIdAndUserId(Integer id,
                                      Integer userId,
                                      TransactionRecord record) {
        if (record == null) {
            throw new BusinessException("请求体不能为空");
        }
        String type = record.getType();
        BigDecimal amount = record.getAmount();
        LocalDate recordDate = record.getRecordDate();
        if (id == null) {
            throw new BusinessException("收支记录id不能为空");
        }
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        Integer categoryId = record.getCategoryId();
        if (categoryId == null) {
            throw new BusinessException("分类id不能为空");
        }
        TransactionCategory category = findByIdAndUserId(categoryId, userId);
        if (category == null) {
            throw new BusinessException("该分类id不存在 或 该分类id不属于该用户");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("分类类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
        }
        if (!type.equals(category.getType())) {
            throw new BusinessException("记录类型必须和分类类型一致");
        }
        // 修改记录时同样要校验金额，防止把已有记录改成非法金额
        // 記録更新時も金額をチェックし、既存記録が不正な金額にならないようにする
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("金额不能为空 且 必须大于0");
        }
        if (recordDate == null) {
            throw new BusinessException("日期不能为空");
        }
        int rows = transactionRecordMapper.updateByIdAndUserId(id, userId, record);
        if (rows > 0) {
            return new Result(true, "修改收支记录成功", new TransactionRecordVO(id, category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        throw new BusinessException("修改收支记录失败");
    }

    // 删除当前登录用户自己的收支记录：用记录 id 和 userId 双重限制
    // ログイン中ユーザー本人の収支記録を削除する：記録IDとuserIdで本人の記録に限定する
    public Result deleteByIdAndUserId(Integer id,
                                      Integer userId) {
        if (id == null) {
            throw new BusinessException("收支记录id不能为空");
        }
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        int rows = transactionRecordMapper.deleteByIdAndUserId(id, userId);
        if (rows > 0) {
            return new Result(true, "删除收支记录成功", id);
        }
        throw new BusinessException("删除收支记录失败");
    }

    // 按类型查询自己的收支记录：type 只能是 income 或 expense
    // タイプ別に本人の収支記録を取得する：type は income または expense のみ
    public Result findByUserIdAndType(Integer userId,
                                      String type) {
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("分类类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
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
    public Result findByUserIdAndDateRange(Integer userId,
                                           LocalDate startRecordDate,
                                           LocalDate endOfRecordDate) {
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if ((startRecordDate == null) || (endOfRecordDate == null)) {
            throw new BusinessException("日期不能为空");
        }
        if (startRecordDate.isAfter(endOfRecordDate)) {
            throw new BusinessException("开始日期不能晚于结束日期");
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
    public TransactionCategory findByIdAndUserId(Integer categoryId,
                                                 Integer userId) {
        return transactionCategoryMapper.findByIdAndUserId(categoryId, userId);
    }

    // 查询某个分类下面的具体收支记录
    // 指定カテゴリに属する具体的な収支記録を取得する
    public Result findByUserIdAndCategoryId(Integer userId,
                                            Integer categoryId) {
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (categoryId == null) {
            throw new BusinessException("分类id不能为空");
        }
        TransactionCategory category = findByIdAndUserId(categoryId, userId);
        if (category == null) {
            throw new BusinessException("该分类id不存在 或 该分类id不属于该用户");
        }
        List<TransactionRecord> records = transactionRecordMapper.findByUserIdAndCategoryId(userId, categoryId);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        return new Result(true, "根据分类id，查询所有记录成功", recordsVO);
    }

    // 分页查询当前用户的收支记录
    // ログイン中ユーザーの収支記録をページング検索する
    public Result findByUserIdPage(Integer userId,
                                   Integer page,
                                   Integer size) {
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (page == null) {
            throw new BusinessException("page不能为空");
        }
        if (page <= 0) {
            throw new BusinessException("页数必须大于0");
        }
        if (size == null) {
            throw new BusinessException("size不能为空");
        }
        if (size <= 0) {
            throw new BusinessException("查看的每页的记录数量必须大于0");
        }
        // MySQL 分页偏移量：第 1 页从 0 开始，第 2 页从 size 开始
        // MySQL のページング開始位置：1ページ目は0、2ページ目は size から始まる
        Integer offset = (page - 1) * size;
        List<TransactionRecord> records = transactionRecordMapper.findByUserIdPage(userId, offset, size);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            TransactionCategory category = transactionCategoryMapper.findByIdAndUserId(record.getCategoryId(), userId);
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()));
        }
        Integer total = transactionRecordMapper.countByUserId(userId);
        PageVO pageVO = new PageVO(recordsVO, total, page, size);
        return new Result(true, "查询成功", pageVO);

    }

    // 组合条件分页查询：type、categoryId、日期范围都可以作为可选筛选条件
    // 複合条件ページング検索：type、categoryId、日付範囲を任意条件として使える
    public Result searchByUserIdPage(Integer userId,
                                     String type,
                                     Integer categoryId,
                                     LocalDate startRecordDate,
                                     LocalDate endOfRecordDate,
                                     Integer page,
                                     Integer size) {
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (page == null) {
            throw new BusinessException("page不能为空");
        }
        if (page <= 0) {
            throw new BusinessException("页数必须大于0");
        }
        if (size == null) {
            throw new BusinessException("size不能为空");
        }
        if (size <= 0) {
            throw new BusinessException("查看的每页的记录数量必须大于0");
        }
        if (type != null && !type.isEmpty()) {
            if (!"income".equals(type) && !"expense".equals(type)) {
                throw new BusinessException("类型必须是 income 或者 expense");
            }
        }
        if ((startRecordDate == null && endOfRecordDate != null)
                || (startRecordDate != null && endOfRecordDate == null)) {
            throw new BusinessException("开始日期和结束日期必须同时填写");
        }
        if (startRecordDate != null && endOfRecordDate != null) {
            if (startRecordDate.isAfter(endOfRecordDate)) {
                throw new BusinessException("开始日期不能晚于结束日期");
            }
        }
        if (categoryId != null) {
            // 分类筛选必须确认分类属于当前用户，不能查询别人的分类数据
            // カテゴリ条件は現在ユーザー本人のカテゴリか確認し、他人のデータを検索させない
            TransactionCategory category = findByIdAndUserId(categoryId, userId);
            if (category == null) {
                throw new BusinessException("该分类id不存在 或 该分类id不属于该用户");
            }

            if (type != null && !type.isEmpty() && !type.equals(category.getType())) {
                throw new BusinessException("筛选类型必须和分类类型一致");
            }
        }
        // 搜索接口也使用相同分页公式
        // 検索APIでも同じページング計算式を使う
        Integer offset = (page - 1) * size;
        List<TransactionRecord> records = transactionRecordMapper.searchByUserIdPage(userId, type, categoryId, startRecordDate, endOfRecordDate, offset, size);
        List<TransactionRecordVO> recordsVO = new ArrayList<>();
        for (TransactionRecord record : records) {
            TransactionCategory category = findByIdAndUserId(record.getCategoryId(), userId);
            recordsVO.add(new TransactionRecordVO(record.getId(), category.getName(), record.getType(), record.getAmount(), record.getRemark(), record.getRecordDate()
            ));
        }
        Integer total = transactionRecordMapper.countSearchByUserId(userId, type, categoryId, startRecordDate, endOfRecordDate);
        PageVO pageVO = new PageVO(recordsVO, total, page, size);
        return new Result(true, "查询成功", pageVO);
    }

    // 校验金额：必须大于0，不能超过数据库 DECIMAL(10,2) 的范围，小数最多2位
    // 金額チェック：0より大きく、DB の DECIMAL(10,2) の範囲を超えず、小数は2桁まで
    private Result validateAmount(BigDecimal amount) {
        if (amount == null) {
            return new Result(false, "金额不能为空", null);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Result(false, "金额必须大于0", null);
        }

        if (amount.compareTo(new BigDecimal("99999999.99")) > 0) {
            return new Result(false, "金额不能超过99999999.99", null);
        }

        if (amount.scale() > 2) {
            return new Result(false, "金额最多只能保留2位小数", null);
        }

        return null;
    }
}
