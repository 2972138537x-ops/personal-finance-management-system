<script setup>
import { getCategoryDisplayName, isDefaultCategory } from "@/utils/format.js";
const props = defineProps({
  category: { type: Object, required: true },
  t: { type: Function, required: true }
});
defineEmits(["edit", "remove"]);

function categoryName(item) {
  return getCategoryDisplayName(item, props.t);
}
function typeText(type) {
  return type === "income" ? props.t("income") : props.t("expense");
}
</script>

<template>
  <article class="mobile-category-card">
    <div class="mobile-category-main">
      <strong>{{ categoryName(category) }} <span v-if="isDefaultCategory(category)" class="category-badge">{{ t("defaultCategoryBadge") }}</span></strong>
      <span>{{ typeText(category.type) }}</span>
    </div>
    <div class="mobile-category-actions">
      <button type="button" @click="$emit('edit', category)">{{ t("edit") }}</button>
      <button type="button" class="danger" @click="$emit('remove', category)">{{ t("remove") }}</button>
    </div>
  </article>
</template>
