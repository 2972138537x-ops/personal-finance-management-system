<script setup>
import { reactive, ref, watch } from "vue";
import PasswordInput from "@/components/PasswordInput.vue";
import { authApi } from "@/api/authApi.js";
import { defaultAppearance, applyAppearance } from "@/utils/appearance.js";

const props = defineProps({ user: Object, appearance: Object, t: Function, toast: Function });
const emit = defineEmits(["updateAppearance", "logout", "deleted", "passwordChanged"]);
const passwordForm = reactive({ oldPassword: "", newPassword: "", confirmPassword: "" });
const deleteForm = reactive({ password: "", confirmPassword: "" });
const localAppearance = reactive({ ...props.appearance });
const open = reactive({ section: '', securityPanel: '' });

function toggleSection(name) {
  open.section = open.section === name ? '' : name;
}

function toggleSecurity(name) {
  open.securityPanel = open.securityPanel === name ? '' : name;
}

watch(() => props.appearance, (value) => Object.assign(localAppearance, value || {}), { deep: true });
watch(localAppearance, (value) => applyAppearance(value), { deep: true });

async function changePassword() {
  if (!passwordForm.oldPassword || !passwordForm.newPassword || !passwordForm.confirmPassword) return props.toast(props.t("changePasswordRequired"));
  if (passwordForm.newPassword !== passwordForm.confirmPassword) return props.toast(props.t("newPasswordMismatch"));
  try {
    const result = await authApi.changePassword(passwordForm.oldPassword, passwordForm.newPassword);
    props.toast(result.message || props.t("changePasswordSuccess"));
    Object.assign(passwordForm, { oldPassword: "", newPassword: "", confirmPassword: "" });
    setTimeout(() => emit("passwordChanged"), 500);
  } catch (error) { props.toast(error.message); }
}

async function deleteAccount() {
  if (!deleteForm.password || !deleteForm.confirmPassword) return props.toast(props.t("deletePasswordRequired"));
  if (deleteForm.password !== deleteForm.confirmPassword) return props.toast(props.t("deletePasswordMismatch"));
  const username = props.user?.username || "";
  if (!confirm(props.t("deleteAccountConfirm1").replace("{username}", username))) return;
  try {
    const result = await authApi.deleteMe(deleteForm.password, deleteForm.confirmPassword);
    props.toast(result.message || props.t("deleteAccountSuccess"));
    emit("deleted");
  } catch (error) { props.toast(error.message); }
}

function saveAppearance() {
  emit("updateAppearance", { ...localAppearance });
  props.toast(props.t("appearanceSaved"));
}

function resetAppearance() {
  Object.assign(localAppearance, defaultAppearance);
  saveAppearance();
}
</script>

<template>
  <section class="view-panel active">
    <div class="content-header">
      <div><h2>{{ t("profile") }}</h2><p>{{ t("profileHint") }}</p></div>
    </div>

    <section class="panel account-info-panel collapsible-panel" :class="{ collapsed: open.section !== 'info' }">
      <h3><button type="button" class="mobile-panel-toggle always-toggle" @click="toggleSection('info')"><span>{{ t("accountInfo") }}</span><span class="mobile-panel-arrow">{{ open.section === "info" ? "−" : "＋" }}</span></button></h3>
      <div class="collapsible-body">
        <div class="detail-box">
          <div class="detail-row"><span>{{ t("username") }}</span><strong>{{ user?.username || "-" }}</strong></div>
          <div class="detail-row"><span>{{ t("role") }}</span><strong>{{ user?.role || "USER" }}</strong></div>
        </div>
        <div class="profile-actions"><button type="button" class="danger" @click="$emit('logout')">{{ t("logout") }}</button></div>
      </div>
    </section>

    <section class="panel account-security-panel collapsible-panel" :class="{ collapsed: open.section !== 'security' }">
      <h3><button type="button" class="mobile-panel-toggle always-toggle" @click="toggleSection('security')"><span>{{ t("accountSecurity") }}</span><span class="mobile-panel-arrow">{{ open.section === "security" ? "−" : "＋" }}</span></button></h3>
      <div class="collapsible-body">
        <section class="security-section nested-security-panel collapsible-panel" :class="{ collapsed: open.securityPanel !== 'password' }">
          <h4><button type="button" class="mobile-panel-toggle always-toggle nested-toggle" @click="toggleSecurity('password')"><span>{{ t("changePassword") }}</span><span class="mobile-panel-arrow">{{ open.securityPanel === "password" ? "−" : "＋" }}</span></button></h4>
          <form class="form-grid collapsible-body" @submit.prevent="changePassword">
            <label><span>{{ t("oldPassword") }}</span><PasswordInput v-model="passwordForm.oldPassword" :title-show="t('showPassword')" :title-hide="t('hidePassword')" /></label>
            <label><span>{{ t("newPassword") }}</span><PasswordInput v-model="passwordForm.newPassword" :title-show="t('showPassword')" :title-hide="t('hidePassword')" /></label>
            <label><span>{{ t("confirmPassword") }}</span><PasswordInput v-model="passwordForm.confirmPassword" :title-show="t('showPassword')" :title-hide="t('hidePassword')" /></label>
            <div class="form-actions"><button type="submit" class="primary">{{ t("save") }}</button></div>
          </form>
        </section>

        <section class="security-section danger-zone-inline nested-security-panel collapsible-panel" :class="{ collapsed: open.securityPanel !== 'deleteAccount' }">
          <h4><button type="button" class="mobile-panel-toggle always-toggle nested-toggle danger-toggle" @click="toggleSecurity('deleteAccount')"><span>{{ t("deleteAccount") }}</span><span class="mobile-panel-arrow">{{ open.securityPanel === "deleteAccount" ? "−" : "＋" }}</span></button></h4>
          <form class="form-grid collapsible-body" @submit.prevent="deleteAccount">
            <p class="muted">{{ t("deleteAccountHint") }}</p>
            <label><span>{{ t("deletePassword") }}</span><PasswordInput v-model="deleteForm.password" :title-show="t('showPassword')" :title-hide="t('hidePassword')" /></label>
            <label><span>{{ t("deleteConfirmPassword") }}</span><PasswordInput v-model="deleteForm.confirmPassword" :title-show="t('showPassword')" :title-hide="t('hidePassword')" /></label>
            <div class="form-actions"><button type="submit" class="danger">{{ t("deleteAccount") }}</button></div>
          </form>
        </section>
      </div>
    </section>

    <section class="panel settings-panel collapsible-panel" :class="{ collapsed: open.section !== 'display' }">
      <h3><button type="button" class="mobile-panel-toggle always-toggle" @click="toggleSection('display')"><span>{{ t("displaySettings") }}</span><span class="mobile-panel-arrow">{{ open.section === "display" ? "−" : "＋" }}</span></button></h3>
      <div class="collapsible-body">
        <p class="muted">{{ t("settingsHint") }}</p>
        <form class="form-grid" @submit.prevent="saveAppearance">
          <label><span>{{ t("theme") }}</span><select v-model="localAppearance.theme"><option value="light">{{ t("themeLight") }}</option><option value="dark">{{ t("themeDark") }}</option><option value="system">{{ t("themeSystem") }}</option></select></label>
          <label><span>{{ t("incomeColor") }}</span><input v-model="localAppearance.incomeColor" type="color"></label>
          <label><span>{{ t("expenseColor") }}</span><input v-model="localAppearance.expenseColor" type="color"></label>
          <label><span>{{ t("primaryColor") }}</span><input v-model="localAppearance.primaryColor" type="color"></label>
          <div class="form-actions"><button type="submit" class="primary">{{ t("save") }}</button><button type="button" @click="resetAppearance">{{ t("resetAppearance") }}</button></div>
        </form>
      </div>
    </section>
  </section>
</template>
