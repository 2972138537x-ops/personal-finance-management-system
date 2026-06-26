<script setup>
defineProps({
  lang: { type: String, required: true },
  t: { type: Function, required: true }
});
defineEmits(["changeLang"]);
</script>

<template>
  <header class="topbar">
    <div class="brand">
      <div class="logo">PF</div>
      <div>
        <h1>{{ t("appTitle") }}</h1>
        <p>{{ t("appSubTitle") }}</p>
      </div>
    </div>

    <div class="language-select-wrap">
      <select class="language-select" aria-label="Language" :value="lang" @change="$emit('changeLang', $event.target.value)">
        <option value="zh">中文</option>
        <option value="ja">日本語</option>
        <option value="en">English</option>
      </select>
    </div>
  </header>
</template>
