<script setup>
defineProps({
  message: { type: String, default: "" },
  show: { type: Boolean, default: false }
});
</script>

<template>
  <div class="toast" :class="{ show }">{{ message }}</div>
</template>
