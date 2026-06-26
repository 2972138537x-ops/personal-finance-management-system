<script setup>
import { ref } from "vue";
import { money, getCategoryName } from "@/utils/format.js";

const props = defineProps({
  record: { type: Object, required: true },
  categories: { type: Array, default: () => [] },
  t: { type: Function, required: true }
});
const emit = defineEmits(["edit", "remove"]);
const open = ref(false);
const pressing = ref(false);
let timer = null;
let longPressed = false;

function typeText(type) {
  return type === "income" ? props.t("income") : props.t("expense");
}

function clearTimer() {
  if (timer) {
    clearTimeout(timer);
    timer = null;
  }
}

function pointerDown(event) {
  if (event.pointerType === "mouse") return;
  clearTimer();
  longPressed = false;
  pressing.value = true;
  timer = setTimeout(() => {
    longPressed = true;
    open.value = true;
    pressing.value = false;
  }, 430);
}

function pointerUp(event) {
  if (event.pointerType === "mouse") return;
  clearTimer();
  pressing.value = false;
}

function toggle() {
  if (longPressed) {
    longPressed = false;
    return;
  }
  open.value = !open.value;
}

function handleEdit() {
  open.value = false;
  emit("edit", props.record);
}

function handleRemove() {
  emit("remove", props.record);
}
</script>

<template>
  <article class="mobile-record-card" :class="{ 'is-open': open, 'is-pressing': pressing }">
    <button
      type="button"
      class="mobile-record-main"
      :class="{ open, pressing }"
      @click="toggle"
      @pointerdown="pointerDown"
      @pointerup="pointerUp"
      @pointercancel="pointerUp"
      @pointerleave="pointerUp"
      @blur="pressing = false"
    >
      <span class="mobile-record-left">
        <strong>{{ getCategoryName(record, categories) }}</strong>
        <em>{{ typeText(record.type) }} · {{ record.recordDate || "" }}</em>
      </span>
      <span class="mobile-record-amount" :class="record.type === 'income' ? 'income amount-income' : 'expense amount-expense'">
        {{ money(record.amount) }}
      </span>
      <span class="mobile-record-arrow">⌄</span>
    </button>

    <Transition name="record-detail">
      <div v-if="open" class="mobile-record-detail">
        <div class="mobile-record-row"><span>ID</span><strong>{{ record.id }}</strong></div>
        <div class="mobile-record-row"><span>{{ t("remark") }}</span><strong>{{ record.remark || "-" }}</strong></div>
        <div class="mobile-record-actions">
          <button type="button" @click="handleEdit">{{ t("edit") }}</button>
          <button type="button" class="danger" @click="handleRemove">{{ t("remove") }}</button>
        </div>
      </div>
    </Transition>
  </article>
</template>
