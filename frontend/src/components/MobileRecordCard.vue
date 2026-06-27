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
const longPressing = ref(false);

let timer = null;
let suppressClick = false;

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
  suppressClick = false;
  longPressing.value = false;
  pressing.value = true;

  timer = setTimeout(() => {
    longPressing.value = true;
    suppressClick = true;
    open.value = true;
    pressing.value = false;
  }, 420);
}

function pointerUp(event) {
  if (event.pointerType === "mouse") return;
  clearTimer();
  pressing.value = false;

  if (longPressing.value) {
    // 长按预览：松手后柔和收回，不再一直停留。
    window.setTimeout(() => {
      open.value = false;
      longPressing.value = false;
      window.setTimeout(() => {
        suppressClick = false;
      }, 80);
    }, 90);
  }
}

function pointerCancel(event) {
  if (event.pointerType === "mouse") return;
  clearTimer();
  pressing.value = false;
  if (longPressing.value) {
    open.value = false;
    longPressing.value = false;
  }
  window.setTimeout(() => {
    suppressClick = false;
  }, 80);
}

function toggle() {
  if (suppressClick) {
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
  <article class="mobile-record-card" :class="{ 'is-open': open, 'is-pressing': pressing, 'is-long-preview': longPressing }">
    <button
      type="button"
      class="mobile-record-main"
      :class="{ open, pressing }"
      @click="toggle"
      @pointerdown="pointerDown"
      @pointerup="pointerUp"
      @pointercancel="pointerCancel"
      @pointerleave="pointerCancel"
      @blur="pressing = false"
    >
      <span class="mobile-record-left">
        <strong>{{ getCategoryName(record, categories, t) }}</strong>
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
