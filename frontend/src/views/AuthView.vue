<script setup>
import { ref } from "vue";
import PasswordInput from "@/components/PasswordInput.vue";

const props = defineProps({ t: { type: Function, required: true } });
const emit = defineEmits(["login", "register"]);
const mode = ref("login");
const loginUsername = ref("");
const loginPassword = ref("");
const registerUsername = ref("");
const registerPassword = ref("");
const submitting = ref(false);

async function submitLogin() {
  if (submitting.value) return;
  submitting.value = true;
  try {
    await emit("login", { username: loginUsername.value.trim(), password: loginPassword.value });
  } finally {
    submitting.value = false;
  }
}

async function submitRegister() {
  if (submitting.value) return;
  submitting.value = true;
  try {
    await emit("register", { username: registerUsername.value.trim(), password: registerPassword.value });
    loginUsername.value = registerUsername.value.trim();
    loginPassword.value = "";
    registerPassword.value = "";
    mode.value = "login";
  } finally {
    submitting.value = false;
  }
}
</script>

<template>
  <section class="auth-view">
    <div class="auth-card">
      <div class="segmented">
        <button type="button" class="segment" :class="{ active: mode === 'login' }" @click="mode = 'login'">{{ t("login") }}</button>
        <button type="button" class="segment" :class="{ active: mode === 'register' }" @click="mode = 'register'">{{ t("register") }}</button>
      </div>

      <form v-if="mode === 'login'" class="auth-form" :class="{ 'auth-submitting': submitting }" @submit.prevent="submitLogin">
        <h2>{{ t("login") }}</h2>
        <p class="muted">{{ t("loginHint") }}</p>

        <label>
          <span>{{ t("username") }}</span>
          <input v-model="loginUsername" type="text" autocomplete="username">
        </label>

        <label>
          <span>{{ t("password") }}</span>
          <PasswordInput v-model="loginPassword" autocomplete="current-password" :title-show="t('showPassword')" :title-hide="t('hidePassword')" />
        </label>

        <button type="submit" class="primary">{{ t("login") }}</button>

        <p class="auth-link">
          <span>{{ t("noAccount") }}</span>
          <button type="button" @click="mode = 'register'">{{ t("goRegister") }}</button>
        </p>
      </form>

      <form v-else class="auth-form" :class="{ 'auth-submitting': submitting }" @submit.prevent="submitRegister">
        <h2>{{ t("register") }}</h2>
        <p class="muted">{{ t("registerHint") }}</p>

        <label>
          <span>{{ t("username") }}</span>
          <input v-model="registerUsername" type="text" autocomplete="username">
        </label>

        <label>
          <span>{{ t("password") }}</span>
          <PasswordInput v-model="registerPassword" autocomplete="new-password" :title-show="t('showPassword')" :title-hide="t('hidePassword')" />
        </label>

        <button type="submit" class="primary">{{ t("register") }}</button>

        <p class="auth-link">
          <span>{{ t("hasAccount") }}</span>
          <button type="button" @click="mode = 'login'">{{ t("goLogin") }}</button>
        </p>
      </form>
    </div>
  </section>
</template>
