import { getToken, clearAuth } from "@/auth/token.js";
import { getErrorMessage, getResultData, isAuthExpiredMessage } from "@/utils/format.js";

let authExpiredHandler = null;

export function setAuthExpiredHandler(handler) {
  authExpiredHandler = handler;
}

function notifyAuthExpired(message) {
  clearAuth();
  if (typeof authExpiredHandler === "function") {
    authExpiredHandler(message);
  }
}

function buildBody(body, headers) {
  if (body === undefined || body === null) return undefined;
  if (body instanceof URLSearchParams) return body;
  if (body instanceof FormData) {
    delete headers["Content-Type"];
    return body;
  }
  if (typeof body === "string") return body;
  return JSON.stringify(body);
}

export async function request(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  const token = getToken();
  if (token && options.auth !== false) {
    headers.Authorization = token;
  }

  const response = await fetch(path, {
    method: options.method || "GET",
    headers,
    body: buildBody(options.body, headers)
  });

  const result = await response.json().catch(() => null);

  if (options.auth !== false && (response.status === 401 || response.status === 403)) {
    const message = getErrorMessage(result) || "登录已失效，请重新登录";
    notifyAuthExpired(message);
    throw new Error(message);
  }

  if (!response.ok) {
    throw new Error(getErrorMessage(result) || ("HTTP " + response.status));
  }

  if (result && result.success === false) {
    const message = result.message || "请求失败";
    if (options.auth !== false && isAuthExpiredMessage(message)) {
      notifyAuthExpired(message);
    }
    throw new Error(message);
  }

  if (result && result.code !== undefined && Number(result.code) >= 400) {
    const message = result.message || "请求失败";
    if (options.auth !== false && (Number(result.code) === 401 || Number(result.code) === 403 || isAuthExpiredMessage(message))) {
      notifyAuthExpired(message);
    }
    throw new Error(message);
  }

  return result;
}

export { getResultData };
