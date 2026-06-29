export function today() {
  return new Date().toISOString().slice(0, 10);
}

export function currentYear() {
  return new Date().getFullYear();
}

export function currentMonth() {
  return new Date().getMonth() + 1;
}

export function money(value) {
  return Number(value || 0).toFixed(2);
}

export function getResultData(result) {
  if (!result) return null;
  if (Object.prototype.hasOwnProperty.call(result, "data")) return result.data;
  return result;
}

export function getErrorMessage(result) {
  if (!result) return "";
  return result.message || result.error || result.msg || "";
}

const DEFAULT_CATEGORY_NAME_TO_CODE = {
  "工资": "SALARY",
  "兼职": "PART_TIME",
  "奖金": "BONUS",
  "其他收入": "OTHER_INCOME",
  "餐饮": "FOOD",
  "交通": "TRANSPORT",
  "学习": "STUDY",
  "生活用品": "DAILY_GOODS",
  "房租": "RENT",
  "水电网费": "UTILITIES",
  "娱乐": "ENTERTAINMENT",
  "其他支出": "OTHER_EXPENSE"
};

export function isDefaultCategory(category) {
  if (!category) return false;
  return category.isDefault === true ||
    category.isDefault === 1 ||
    category.is_default === true ||
    category.is_default === 1;
}

export function defaultCategoryCode(category) {
  if (!category) return "";
  return category.code || category.categoryCode || category.defaultCode || "";
}

export function categoryName(category) {
  if (!category) return "-";
  return category.name || category.categoryName || category.category || "-";
}

export function getDefaultCategoryCodeByName(name) {
  return DEFAULT_CATEGORY_NAME_TO_CODE[name] || "";
}

export function getCategoryDisplayName(category, t) {
  if (!category) return "-";
  const rawName = categoryName(category);
  const codeFromName = getDefaultCategoryCodeByName(rawName);
  const code = defaultCategoryCode(category) || codeFromName;

  if (t && code && (isDefaultCategory(category) || defaultCategoryCode(category) || codeFromName)) {
    const translated = t(`defaultCategory_${code}`);
    if (translated && translated !== `defaultCategory_${code}`) {
      return translated;
    }
  }

  return rawName;
}

export function getCategoryName(record, categories = [], t) {
  if (!record) return "-";

  const hit = categories.find((item) => String(item.id) === String(record.categoryId));
  if (hit) {
    return getCategoryDisplayName(hit, t);
  }

  const rawName = record.categoryName || record.category || record.name;
  if (!rawName) return "-";

  const code = record.code || record.categoryCode || getDefaultCategoryCodeByName(rawName);
  const defaultFlag = record.isDefault === true || record.isDefault === 1 || record.is_default === true || record.is_default === 1;

  if (t && code && (defaultFlag || record.code || record.categoryCode || getDefaultCategoryCodeByName(rawName))) {
    const translated = t(`defaultCategory_${code}`);
    if (translated && translated !== `defaultCategory_${code}`) {
      return translated;
    }
  }

  return rawName;
}

export function normalizeType(type) {
  return type === "income" ? "income" : "expense";
}

export function buildQuery(params) {
  const search = new URLSearchParams();
  Object.entries(params || {}).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      search.set(key, value);
    }
  });
  return search.toString();
}

export function isAuthExpiredMessage(message) {
  const value = String(message || "").toLowerCase();
  return value.includes("token") ||
    value.includes("未登录") ||
    value.includes("登录失效") ||
    value.includes("请先登录") ||
    value.includes("重新登录") ||
    value.includes("无效") ||
    value.includes("unauthorized") ||
    value.includes("forbidden") ||
    value.includes("login");
}
