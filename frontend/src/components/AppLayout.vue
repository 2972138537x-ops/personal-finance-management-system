<script setup>
const props = defineProps({
  user: { type: Object, default: null },
  activeTab: { type: String, required: true },
  t: { type: Function, required: true }
});
const emit = defineEmits(["tab", "refresh", "logout"]);

const navs = [
  ["home", "home"],
  ["profile", "profileNav"],
  ["categories", "categories"],
  ["records", "records"],
  ["stats", "stats"],
  ["ai", "aiAssistant"]
];

function role() {
  return String(props.user?.role || "USER").toUpperCase();
}
</script>

<template>
  <section class="app-layout">
    <aside class="sidebar">
      <section class="side-card mobile-hidden-account">
        <p class="muted">{{ t("loggedIn") }}</p>
        <strong>{{ user?.username || "-" }}</strong>
        <p><span>{{ t("role") }}</span>：<span>{{ role() }}</span></p>

        <div class="side-actions">
          <button type="button" @click="$emit('refresh')">{{ t("refresh") }}</button>
          <button type="button" @click="$emit('logout')">{{ t("logout") }}</button>
        </div>
      </section>

      <nav class="nav" :class="{ 'admin-nav': role() === 'ADMIN' }">
        <button
          v-for="[tab, key] in navs"
          :key="tab"
          type="button"
          class="nav-btn"
          :data-tab="tab"
          :class="{ active: activeTab === tab }"
          @click="$emit('tab', tab)"
        >{{ t(key) }}</button>
        <button
          v-if="role() === 'ADMIN'"
          type="button"
          class="nav-btn"
          data-tab="admin"
          :class="{ active: activeTab === 'admin' }"
          @click="$emit('tab', 'admin')"
        >{{ t("admin") }}</button>
      </nav>
    </aside>

    <div class="content">
      <slot />
    </div>
  </section>
</template>
