<script setup>
const model = defineModel({ type: String, default: "" });
const props = defineProps({
  autocomplete: { type: String, default: "" },
  placeholder: { type: String, default: "" },
  titleShow: { type: String, default: "显示密码" },
  titleHide: { type: String, default: "隐藏密码" }
});
import { ref, computed } from "vue";
const visible = ref(false);
const type = computed(() => visible.value ? "text" : "password");
</script>

<template>
  <div class="password-field">
    <input v-model="model" :type="type" :autocomplete="autocomplete" :placeholder="placeholder" />
    <button
      type="button"
      class="password-toggle"
      :title="visible ? titleHide : titleShow"
      :aria-label="visible ? titleHide : titleShow"
      @click="visible = !visible"
    >{{ visible ? "🙈" : "👁" }}</button>
  </div>
</template>
