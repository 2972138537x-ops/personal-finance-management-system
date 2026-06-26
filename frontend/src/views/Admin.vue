<script setup>
import { nextTick, reactive, ref } from "vue";
import PasswordInput from "@/components/PasswordInput.vue";
import { adminApi } from "@/api/adminApi.js";
import { getResultData } from "@/utils/format.js";

const props = defineProps({ t: Function, toast: Function });
const users = ref([]);
const detail = ref(null);
const searchUsername = ref("");
const reset = reactive({ username: "", password: "" });
const detailPanel = ref(null);
const resetPanel = ref(null);

function normalizeUsers(data) {
  return Array.isArray(data) ? data : [];
}

async function scrollTo(elRef) {
  await nextTick();
  elRef.value?.scrollIntoView({ behavior: "smooth", block: "start" });
}

async function loadAll() {
  try {
    const data = getResultData(await adminApi.users());
    users.value = normalizeUsers(data);
    if (!users.value.length) detail.value = null;
  } catch (error) {
    props.toast(error.message);
  }
}

async function search() {
  const username = searchUsername.value.trim();
  if (!username) return props.toast(props.t("usernameRequired"));
  try {
    const data = getResultData(await adminApi.detail(username));
    detail.value = data || null;
    users.value = data ? [data] : [];
    if (detail.value) await scrollTo(detailPanel);
  } catch (error) {
    props.toast(error.message);
  }
}

async function showDetail(user) {
  try {
    detail.value = getResultData(await adminApi.detail(user.username)) || user;
    await scrollTo(detailPanel);
  } catch (error) {
    props.toast(error.message);
  }
}

async function chooseReset(user) {
  reset.username = user.username;
  await scrollTo(resetPanel);
}

async function resetPassword() {
  if (!reset.username.trim() || !reset.password) return props.toast(props.t("usernameNewPasswordRequired"));
  try {
    const result = await adminApi.resetPassword(reset.username.trim(), reset.password);
    props.toast(result.message || props.t("resetPasswordSuccess"));
    reset.password = "";
  } catch (error) {
    props.toast(error.message);
  }
}

async function remove(user) {
  if (!confirm(props.t("confirmDeleteUser").replace("{username}", user.username))) return;
  try {
    const result = await adminApi.remove(user.username);
    props.toast(result.message || props.t("deleteSuccess"));
    await loadAll();
    detail.value = null;
  } catch (error) {
    props.toast(error.message);
  }
}

defineExpose({ refresh: loadAll });
</script>

<template>
  <section class="view-panel active admin-view">
    <div class="content-header">
      <div>
        <h2>{{ t("admin") }}</h2>
        <p>{{ t("adminHint") }}</p>
      </div>
    </div>

    <section class="panel admin-panel">
      <h3>{{ t("userManagement") }}</h3>

      <form class="admin-search-form" @submit.prevent="search">
        <label>
          <span>{{ t("username") }}</span>
          <input v-model="searchUsername" type="text" autocomplete="off">
        </label>

        <div class="admin-search-actions">
          <button type="submit" class="primary admin-action-button">{{ t("queryOneUser") }}</button>
          <button type="button" class="admin-action-button secondary" @click="loadAll">{{ t("queryAllUsers") }}</button>
        </div>
      </form>

      <div class="table-wrap admin-table-wrap">
        <table>
          <thead>
            <tr>
              <th>{{ t("username") }}</th>
              <th>{{ t("role") }}</th>
              <th>{{ t("actions") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!users.length"><td colspan="3" class="empty">{{ t("noData") }}</td></tr>
            <tr v-for="user in users" :key="user.username">
              <td>{{ user.username }}</td>
              <td>{{ user.role }}</td>
              <td class="actions">
                <button type="button" @click="showDetail(user)">{{ t("detail") }}</button>
                <button type="button" @click="chooseReset(user)">{{ t("resetPassword") }}</button>
                <button type="button" class="danger" @click="remove(user)">{{ t("remove") }}</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="admin-mobile-user-list">
        <article v-for="user in users" :key="user.username" class="admin-user-card">
          <div class="admin-user-card-head">
            <div>
              <strong>{{ user.username }}</strong>
              <span>{{ t("role") }}：{{ user.role || "-" }}</span>
            </div>
          </div>
          <div class="admin-user-card-actions">
            <button type="button" @click="showDetail(user)">{{ t("detail") }}</button>
            <button type="button" @click="chooseReset(user)">{{ t("resetPassword") }}</button>
            <button type="button" class="danger" @click="remove(user)">{{ t("remove") }}</button>
          </div>
        </article>
        <div v-if="!users.length" class="empty admin-mobile-empty">{{ t("noData") }}</div>
      </div>
    </section>

    <section ref="detailPanel" class="panel admin-panel admin-detail-panel">
      <h3>{{ t("userDetail") }}</h3>
      <div class="admin-detail-card" v-if="detail">
        <div class="detail-row" v-if="detail.id !== undefined"><span>ID</span><strong>{{ detail.id }}</strong></div>
        <div class="detail-row"><span>{{ t("username") }}</span><strong>{{ detail.username || "-" }}</strong></div>
        <div class="detail-row"><span>{{ t("role") }}</span><strong>{{ detail.role || "-" }}</strong></div>
        <div class="detail-row" v-if="detail.registerDate"><span>{{ t("registerDate") || "注册日期" }}</span><strong>{{ detail.registerDate }}</strong></div>
      </div>
      <div v-else class="detail-box admin-empty-detail">{{ t("selectUserHint") }}</div>
    </section>

    <section ref="resetPanel" class="panel admin-panel admin-reset-panel">
      <h3>{{ t("resetPassword") }}</h3>
      <form class="form-grid admin-reset-form" @submit.prevent="resetPassword">
        <label>
          <span>{{ t("username") }}</span>
          <input v-model="reset.username" type="text" autocomplete="off">
        </label>
        <label>
          <span>{{ t("newPassword") }}</span>
          <PasswordInput v-model="reset.password" :title-show="t('showPassword')" :title-hide="t('hidePassword')" />
        </label>
        <div class="form-actions">
          <button type="submit" class="primary">{{ t("save") }}</button>
        </div>
      </form>
    </section>
  </section>
</template>
