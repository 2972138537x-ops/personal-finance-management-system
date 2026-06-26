<script setup>
import { money } from "@/utils/format.js";

defineProps({
  items: { type: Array, default: () => [] },
  type: { type: String, default: "income" },
  t: { type: Function, required: true }
});
</script>

<template>
  <div class="stat-list">
    <div v-if="!items.length" class="empty">{{ t("noData") }}</div>
    <div v-for="item in items" :key="item.name" class="stat-item">
      <span>{{ item.name }}</span>
      <strong :class="type === 'income' ? 'amount-income' : 'amount-expense'">{{ money(item.amount) }}</strong>
    </div>
  </div>
</template>
