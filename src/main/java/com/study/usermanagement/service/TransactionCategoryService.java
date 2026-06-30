package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.TransactionRecord;
import com.study.usermanagement.exception.BusinessException;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import com.study.usermanagement.mapper.TransactionRecordMapper;
import com.study.usermanagement.vo.TransactionCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionCategoryService {
    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    private static final List<DefaultCategory> DEFAULT_CATEGORIES = List.of(
            new DefaultCategory("SALARY", "工资", "income"),
            new DefaultCategory("PART_TIME", "兼职", "income"),
            new DefaultCategory("BONUS", "奖金", "income"),
            new DefaultCategory("OTHER_INCOME", "其他收入", "income"),

            new DefaultCategory("FOOD", "餐饮", "expense"),
            new DefaultCategory("TRANSPORT", "交通", "expense"),
            new DefaultCategory("STUDY", "学习", "expense"),
            new DefaultCategory("DAILY_GOODS", "生活用品", "expense"),
            new DefaultCategory("RENT", "房租", "expense"),
            new DefaultCategory("UTILITIES", "水电网费", "expense"),
            new DefaultCategory("ENTERTAINMENT", "娱乐", "expense"),
            new DefaultCategory("OTHER_EXPENSE", "其他支出", "expense")
    );

    // 新用户注册成功后调用：初始化默认分类
    public void createDefaultCategories(Integer userId) {
        if (userId == null) {
            return;
        }
        int count = transactionCategoryMapper.countDefaultCategories(userId);
        if (count > 0) {
            return;
        }
        for (DefaultCategory item : DEFAULT_CATEGORIES) {
            TransactionCategory category = new TransactionCategory();
            category.setUserId(userId);
            category.setName(item.name);
            category.setType(item.type);
            category.setCode(item.code);
            category.setIsDefault(true);
            transactionCategoryMapper.insert(category);
        }
    }

    public Result addCategory(Integer userId, TransactionCategory category) {
        if (category == null) {
            throw new BusinessException("请求体不能为空");
        }
        String name = category.getName();
        String type = category.getType();
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (name == null || name.isEmpty()) {
            throw new BusinessException("分类名不能为空");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("分类类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
        }

        TransactionCategory oldCategory =
                transactionCategoryMapper.findByUserIdAndNameAndType(userId, name, type);
        if (oldCategory != null) {
            throw new BusinessException("该分类已存在");
        }

        category.setUserId(userId);
        // 用户手动新增的分类属于自定义分类，不参与语言自动翻译
        category.setCode(null);
        category.setIsDefault(false);

        int rows = transactionCategoryMapper.insert(category);
        if (rows > 0) {
            return new Result(true, "新增分类成功",
                    new TransactionCategoryVO(category.getId(), category.getName(), category.getType(), category.getCode(), category.getIsDefault()));
        }
        throw new BusinessException("新增分类失败");
    }

    public Result findByUserId(Integer userId) {
        if (userId == null) {
            throw new BusinessException("登录状态异常，请重新登录");
        }
        List<TransactionCategory> categoryList = transactionCategoryMapper.findByUserId(userId);
        List<TransactionCategoryVO> categoryVOList = new ArrayList<>();
        for (TransactionCategory category : categoryList) {
            categoryVOList.add(new TransactionCategoryVO(
                    category.getId(),
                    category.getName(),
                    category.getType(),
                    category.getCode(),
                    category.getIsDefault()
            ));
        }
        return new Result(true, "查询成功", categoryVOList);
    }

    public Result updateByIdAndUserId(Integer id, Integer userId, TransactionCategory category) {
        if (category == null) {
            throw new BusinessException("请求体不能为空");
        }
        String name = category.getName();
        String type = category.getType();
        if (id == null) {
            throw new BusinessException("分类id不能为空");
        }
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (name == null || name.isEmpty()) {
            throw new BusinessException("分类名不能为空");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("分类类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
        }

        TransactionCategory oldCategory =
                transactionCategoryMapper.findByUserIdAndNameAndType(userId, name, type);
        if (oldCategory != null && !oldCategory.getId().equals(id)) {
            throw new BusinessException("该分类已存在");
        }

        // 用户手动编辑后，默认分类也转为自定义分类，避免以后切换语言覆盖用户自己的命名
        category.setCode(null);
        category.setIsDefault(false);

        int rows = transactionCategoryMapper.updateByIdAndUserId(id, userId, category);
        if (rows > 0) {
            return new Result(true, "修改成功",
                    new TransactionCategoryVO(id, category.getName(), category.getType(), category.getCode(), category.getIsDefault()));
        }
        throw new BusinessException("修改失败");
    }

    public Result deleteByIdAndUserId(Integer id, Integer userId) {
        if (id == null) {
            throw new BusinessException("分类id不能为空");
        }
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }

        TransactionCategory category = transactionCategoryMapper.findByIdAndUserId(id, userId);
        if (category == null) {
            throw new BusinessException("该分类不存在 或 该分类不属于当前用户");
        }

        List<TransactionRecord> records =
                transactionRecordMapper.findByUserIdAndCategoryId(userId, id);
        if (records != null && !records.isEmpty()) {
            throw new BusinessException("该分类下已有收支记录，不能删除");
        }

        int rows = transactionCategoryMapper.deleteByIdAndUserId(id, userId);
        if (rows > 0) {
            return new Result(true, "删除成功", id);
        }
        throw new BusinessException("删除失败");
    }

    private static class DefaultCategory {
        private final String code;
        private final String name;
        private final String type;

        private DefaultCategory(String code, String name, String type) {
            this.code = code;
            this.name = name;
            this.type = type;
        }
    }
}
