<script setup>
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import TopBar from "@/components/TopBar.vue";
import Toast from "@/components/Toast.vue";
import AppLayout from "@/components/AppLayout.vue";
import AuthView from "@/views/AuthView.vue";
import Home from "@/views/Home.vue";
import MyPage from "@/views/MyPage.vue";
import Category from "@/views/Category.vue";
import Record from "@/views/Record.vue";
import Stats from "@/views/Stats.vue";
import AiAssistant from "@/views/AiAssistant.vue";
import Admin from "@/views/Admin.vue";
import { messages, createI18n } from "@/i18n/index.js";
import { authApi } from "@/api/authApi.js";
import { setAuthExpiredHandler, getResultData } from "@/api/request.js";
import { getToken, setToken, clearAuth, getCurrentUser, setCurrentUser } from "@/auth/token.js";
import { loadAppearance, saveAppearance, applyAppearance } from "@/utils/appearance.js";

const lang = ref(localStorage.getItem("pf.lang") || "ja");
const t = createI18n(() => lang.value);
const token = ref(getToken());
const user = ref(getCurrentUser());
const activeTab = ref("home");
const appearance = reactive(loadAppearance());
const toastState = reactive({ message: "", show: false });
const currentView = ref(null);

const loggedIn = computed(() => !!token.value);
const role = computed(() => String(user.value?.role || "USER").toUpperCase());

function showToast(message) {
  toastState.message = localizeMessage(message || "");
  toastState.show = true;
  clearTimeout(showToast.timer);
  showToast.timer = setTimeout(() => { toastState.show = false; }, 2300);
}

function localizeMessage(message) {
  const raw = String(message || "");
  const exactMap = {
    "登录已失效，请重新登录": "loginRequired",
    "请求失败": "requestFailed",
    "请输入用户名和密码": "usernamePasswordRequired",
    "登录成功，但后端没有返回 token": "loginSuccessNoToken",
    "登录成功": "loginSuccess",
    "注册成功": "registerSuccess",
    "注册成功，请登录": "registerSuccess",
    "注册失败": "registerFailed",
    "用户名不能为空": "usernameRequired",
    "密码不能为空": "passwordRequired",
    "用户名已存在": "usernameExists",
    "用户不存在": "userNotFound",
    "密码错误": "passwordWrong",
    "密码修改成功": "changePasswordSuccess",
    "保存成功": "saveSuccess",
    "删除成功": "deleteSuccess"
  };
  if (exactMap[raw]) return t(exactMap[raw]);
  return raw;
}

function changeLang(value) {
  lang.value = value;
  localStorage.setItem("pf.lang", value);
  document.documentElement.lang = value === "zh" ? "zh-CN" : value;
}

function updateAppearance(next) {
  Object.assign(appearance, next);
  saveAppearance(appearance);
  applyAppearance(appearance);
}

function clearLoginState() {
  token.value = "";
  user.value = null;
  clearAuth();
  activeTab.value = "home";
}

async function loadMe() {
  const data = getResultData(await authApi.me());
  if (data) {
    user.value = { ...(user.value || {}), ...data };
    setCurrentUser(user.value);
  }
}

async function handleLogin({ username, password }) {
  if (!username || !password) return showToast(t("usernamePasswordRequired"));
  try {
    const result = await authApi.login(username, password);
    const data = getResultData(result);
    let nextToken = "";
    if (typeof data === "string") nextToken = data;
    else if (data) nextToken = data.token || data.authorization || data.Authorization || "";
    if (!nextToken && result) nextToken = result.token || "";
    if (!nextToken) throw new Error(t("loginSuccessNoToken"));

    token.value = nextToken;
    setToken(nextToken);
    user.value = { username, role: "USER" };
    setCurrentUser(user.value);
    await loadMe().catch(() => {});
    activeTab.value = "home";
    showToast(t("loginSuccess"));
  } catch (error) {
    showToast(error.message);
  }
}

async function handleRegister({ username, password }) {
  if (!username || !password) return showToast(t("usernamePasswordRequired"));
  try {
    const result = await authApi.register(username, password);
    showToast(result.message || t("registerSuccess"));
  } catch (error) {
    showToast(error.message);
    throw error;
  }
}

async function logout() {
  try { await authApi.logout(); } catch {}
  clearLoginState();
  showToast(t("logout"));
}

function handleAuthExpired(message) {
  clearLoginState();
  showToast(message || t("loginRequired"));
}

function handleDeleted() {
  clearLoginState();
}

function handlePasswordChanged() {
  clearLoginState();
  showToast(t("passwordChangedRelogin"));
}

async function changeTab(tab) {
  if (tab === "admin" && role.value !== "ADMIN") return;
  activeTab.value = tab;
  await nextTick();
  if (currentView.value && typeof currentView.value.refresh === "function") {
    currentView.value.refresh();
  }
}

async function refreshCurrent() {
  if (!loggedIn.value) return;
  if (currentView.value && typeof currentView.value.refresh === "function") {
    await currentView.value.refresh();
  }
  showToast(t("refreshed"));
}

onMounted(async () => {
  applyAppearance(appearance);
  document.documentElement.lang = lang.value === "zh" ? "zh-CN" : lang.value;
  setAuthExpiredHandler(handleAuthExpired);
  if (token.value) {
    await loadMe().catch(() => {});
  }
});

watch(() => appearance.theme, () => applyAppearance(appearance));
watch(lang, () => nextTick(() => applyAppearance(appearance)));
</script>

<template>
  <TopBar :lang="lang" :t="t" @change-lang="changeLang" />

  <main>
    <AuthView v-if="!loggedIn" :t="t" @login="handleLogin" @register="handleRegister" />

    <AppLayout v-else :user="user" :active-tab="activeTab" :t="t" @tab="changeTab" @refresh="refreshCurrent" @logout="logout">
      <Home v-if="activeTab === 'home'" ref="currentView" :t="t" :toast="showToast" @navigate="changeTab" />
      <MyPage v-else-if="activeTab === 'profile'" ref="currentView" :user="user" :appearance="appearance" :t="t" :toast="showToast" @update-appearance="updateAppearance" @logout="logout" @deleted="handleDeleted" @password-changed="handlePasswordChanged" />
      <Category v-else-if="activeTab === 'categories'" ref="currentView" :t="t" :toast="showToast" />
      <Record v-else-if="activeTab === 'records'" ref="currentView" :t="t" :toast="showToast" />
      <Stats v-else-if="activeTab === 'stats'" ref="currentView" :t="t" :toast="showToast" />
      <AiAssistant v-else-if="activeTab === 'ai'" ref="currentView" :t="t" :toast="showToast" />
      <Admin v-else-if="activeTab === 'admin'" ref="currentView" :t="t" :toast="showToast" />
    </AppLayout>
  </main>

  <Toast :message="toastState.message" :show="toastState.show" />
</template>
