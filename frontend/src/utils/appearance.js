export const defaultAppearance = {
  theme: "light",
  incomeColor: "#0f8277",
  expenseColor: "#b42318",
  primaryColor: "#0f8277"
};

export function readJson(value) {
  try {
    return value ? JSON.parse(value) : null;
  } catch {
    return null;
  }
}

export function normalizeHex(value, fallback) {
  const text = String(value || "").trim();
  return /^#[0-9a-fA-F]{6}$/.test(text) ? text : fallback;
}

export function shadeHex(hex, percent) {
  const value = normalizeHex(hex, "#0f8277").slice(1);
  const number = parseInt(value, 16);
  const amount = Math.round(2.55 * percent);
  const r = Math.max(0, Math.min(255, (number >> 16) + amount));
  const g = Math.max(0, Math.min(255, ((number >> 8) & 0x00ff) + amount));
  const b = Math.max(0, Math.min(255, (number & 0x0000ff) + amount));
  return "#" + (0x1000000 + r * 0x10000 + g * 0x100 + b).toString(16).slice(1);
}

export function loadAppearance() {
  const saved = readJson(localStorage.getItem("pf.appearance")) || {};
  return {
    theme: saved.theme || defaultAppearance.theme,
    incomeColor: normalizeHex(saved.incomeColor, defaultAppearance.incomeColor),
    expenseColor: normalizeHex(saved.expenseColor, defaultAppearance.expenseColor),
    primaryColor: normalizeHex(saved.primaryColor, defaultAppearance.primaryColor)
  };
}

export function saveAppearance(appearance) {
  localStorage.setItem("pf.appearance", JSON.stringify(appearance));
}

export function getResolvedTheme(theme) {
  if (theme === "system") {
    return window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
  }
  return theme || "light";
}

export function applyAppearance(appearance) {
  const incomeColor = normalizeHex(appearance.incomeColor, defaultAppearance.incomeColor);
  const expenseColor = normalizeHex(appearance.expenseColor, defaultAppearance.expenseColor);
  const primaryColor = normalizeHex(appearance.primaryColor, defaultAppearance.primaryColor);

  document.documentElement.dataset.theme = getResolvedTheme(appearance.theme);
  document.documentElement.style.setProperty("--income", incomeColor);
  document.documentElement.style.setProperty("--expense", expenseColor);
  document.documentElement.style.setProperty("--primary", primaryColor);
  document.documentElement.style.setProperty("--primary-dark", shadeHex(primaryColor, -18));
  document.documentElement.style.setProperty("--soft", shadeHex(primaryColor, 86));
}
