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

export function categoryName(category) {
  if (!category) return "-";
  return category.name || category.categoryName || category.category || "-";
}

export function getCategoryName(record, categories = []) {
  if (!record) return "-";
  if (record.categoryName || record.category) return record.categoryName || record.category;
  const hit = categories.find((item) => String(item.id) === String(record.categoryId));
  return categoryName(hit);
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
