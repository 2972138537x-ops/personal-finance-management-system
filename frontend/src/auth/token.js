import { readJson } from "@/utils/appearance.js";

export function getToken() {
  return localStorage.getItem("pf.token") || "";
}

export function setToken(token) {
  if (token) localStorage.setItem("pf.token", token);
  else localStorage.removeItem("pf.token");
}

export function clearToken() {
  localStorage.removeItem("pf.token");
}

export function getCurrentUser() {
  return readJson(localStorage.getItem("pf.user")) || null;
}

export function setCurrentUser(user) {
  if (user) localStorage.setItem("pf.user", JSON.stringify(user));
  else localStorage.removeItem("pf.user");
}

export function clearCurrentUser() {
  localStorage.removeItem("pf.user");
}

export function clearAuth() {
  clearToken();
  clearCurrentUser();
}
