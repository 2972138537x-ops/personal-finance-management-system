package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.TransactionRecord;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import com.study.usermanagement.mapper.TransactionRecordMapper;
import com.study.usermanagement.vo.TransactionCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
// 收支分类业务层：处理当前用户自己的收入和支出分类
// 収支カテゴリ業務層：ログイン中ユーザー本人の収入・支出カテゴリを処理する
public class TransactionCategoryService {
    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    // 添加收支分类：同一个用户不能重复创建同名同类型分类
    // 収支カテゴリ追加：同じユーザーは同名・同タイプのカテゴリを重複作成できない
    public Result addCategory(Integer userId, TransactionCategory category) {
        if (category == null) {
            return new Result(false, "请求体不能为空", null);
        }
        String name = category.getName();
        String type = category.getType();
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (name == null || name.isEmpty()) {
            return new Result(false, "分类名不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        // 同一个用户下，同类型同名称的分类不能重复
        // 同じユーザーでは、同じタイプ・同じ名前のカテゴリを重複できない
        TransactionCategory oldCategory =
                transactionCategoryMapper.findByUserIdAndNameAndType(userId, name, type);
        if (oldCategory != null) {
            return new Result(false, "该分类已存在", null);
        }
        category.setUserId(userId);
        int rows = transactionCategoryMapper.insert(category);
        if (rows > 0) {
            return new Result(true, "新增分类成功", new TransactionCategoryVO(category.getId(), category.getName(), category.getType()));
        }
        return new Result(false, "新增分类失败", null);
    }

    // 查询当前登录用户自己的分类，并转换成 VO 返回
    // ログイン中ユーザー本人のカテゴリを取得し、VO に変換して返す
    public Result findByUserId(Integer userId) {
        if (userId == null) {
            return new Result(false, "登录状态异常，请重新登录", null);
        }
        List<TransactionCategory> categoryList = transactionCategoryMapper.findByUserId(userId);
        List<TransactionCategoryVO> categoryVOList = new ArrayList<>();
        for (TransactionCategory category : categoryList) {
            categoryVOList.add(new TransactionCategoryVO(category.getId(), category.getName(), category.getType()));
        }
        return new Result(true, "查询成功", categoryVOList);
    }

    // 修改分类：只能修改属于当前登录用户自己的分类
    // カテゴリ更新：ログイン中ユーザー本人のカテゴリだけ更新できる
    public Result updateByIdAndUserId(Integer id, Integer userId, TransactionCategory category) {
        if (category == null) {
            return new Result(false, "请求体不能为空", null);
        }
        String name = category.getName();
        String type = category.getType();
        if (id == null) {
            return new Result(false, "分类id不能为空", null);
        }
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (name == null || name.isEmpty()) {
            return new Result(false, "分类名不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        // 修改时允许保留原分类名，但不能改成和其它分类重复
        // 更新時は元のカテゴリ名は許可するが、他カテゴリとの重複は許可しない
        TransactionCategory oldCategory =
                transactionCategoryMapper.findByUserIdAndNameAndType(userId, name, type);
        if (oldCategory != null && !oldCategory.getId().equals(id)) {
            return new Result(false, "该分类已存在", null);
        }
        int rows = transactionCategoryMapper.updateByIdAndUserId(id, userId, category);
        if (rows > 0) {
            return new Result(true, "修改成功", new TransactionCategoryVO(id, category.getName(), category.getType()));
        }
        return new Result(false, "修改失败", null);
    }

    // 删除分类：只能删除当前登录用户自己的分类
    // カテゴリ削除：ログイン中ユーザー本人のカテゴリだけ削除できる
    public Result deleteByIdAndUserId(Integer id, Integer userId) {
        if (id == null) {
            return new Result(false, "分类id不能为空", null);
        }
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        // 删除分类前，先检查该分类是否属于当前用户
        // カテゴリ削除前に、そのカテゴリがログイン中ユーザー本人のものか確認する
        TransactionCategory category = transactionCategoryMapper.findByIdAndUserId(id, userId);

        if (category == null) {
            return new Result(false, "该分类不存在 或 该分类不属于当前用户", null);
        }
        // 删除分类前，检查该分类下是否已有收支记录
        // カテゴリ削除前に、そのカテゴリに収支記録が存在するか確認する
        List<TransactionRecord> records =
                transactionRecordMapper.findByUserIdAndCategoryId( userId, id);
        if (records != null && !records.isEmpty()) {
            return new Result(false, "该分类下已有收支记录，不能删除", null);
        }
        int rows = transactionCategoryMapper.deleteByIdAndUserId(id, userId);
        if (rows > 0) {
            return new Result(true, "删除成功", id);
        }
        return new Result(false, "删除失败", null);
    }
}
