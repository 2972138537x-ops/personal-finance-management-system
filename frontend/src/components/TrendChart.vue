<script setup>
import { computed, ref } from "vue";
import { money } from "@/utils/format.js";

const props = defineProps({
  records: { type: Array, default: () => [] },
  t: { type: Function, required: true }
});

const activeIndex = ref(-1);

const points = computed(() => {
  const map = new Map();
  props.records.forEach((record) => {
    const date = record.recordDate || "";
    if (!date) return;
    if (!map.has(date)) map.set(date, { date, income: 0, expense: 0, balance: 0 });
    const item = map.get(date);
    const amount = Number(record.amount || 0);
    if (record.type === "income") item.income += amount;
    else item.expense += amount;
    item.balance = item.income - item.expense;
  });
  return Array.from(map.values()).sort((a, b) => a.date.localeCompare(b.date)).slice(-10);
});

const maxValue = computed(() => {
  const values = points.value.flatMap((item) => [item.income, item.expense, Math.abs(item.balance)]);
  return Math.max(1, ...values);
});

function x(index) {
  if (points.value.length <= 1) return 60;
  return 52 + index * (616 / (points.value.length - 1));
}

function y(value) {
  return 246 - (Number(value || 0) / maxValue.value) * 176;
}

function linePath(key) {
  if (!points.value.length) return "";
  return points.value.map((item, index) => `${index === 0 ? "M" : "L"} ${x(index)} ${y(item[key])}`).join(" ");
}

function areaPath(key) {
  if (!points.value.length) return "";
  const line = linePath(key);
  const lastX = x(points.value.length - 1);
  const firstX = x(0);
  return `${line} L ${lastX} 246 L ${firstX} 246 Z`;
}

function label(date) {
  return date.slice(5);
}
</script>

<template>
  <div class="trend-chart">
    <div class="trend-chart-head">
      <div>
        <h3>{{ t("incomeExpenseTrend") || "收支趋势" }}</h3>
        <p>{{ t("trendHint") || "根据最近记录展示收入、支出和结余变化。" }}</p>
      </div>
      <div class="trend-badges">
        <span class="trend-badge income-dot">{{ t("income") }}</span>
        <span class="trend-badge expense-dot">{{ t("expense") }}</span>
        <span class="trend-badge balance-dot">{{ t("balance") }}</span>
      </div>
    </div>

    <div v-if="!points.length" class="empty">{{ t("noData") }}</div>

    <svg v-else class="trend-svg" viewBox="0 0 720 300" role="img">
      <defs>
        <linearGradient id="incomeArea" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stop-color="#0f8277" stop-opacity="0.20" />
          <stop offset="100%" stop-color="#0f8277" stop-opacity="0.02" />
        </linearGradient>
        <linearGradient id="expenseArea" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stop-color="#b42318" stop-opacity="0.16" />
          <stop offset="100%" stop-color="#b42318" stop-opacity="0.01" />
        </linearGradient>
        <filter id="trendGlow" x="-20%" y="-20%" width="140%" height="140%">
          <feDropShadow dx="0" dy="8" stdDeviation="8" flood-color="#10242a" flood-opacity="0.10" />
        </filter>
      </defs>

      <g class="trend-grid">
        <line v-for="n in 5" :key="n" x1="48" x2="678" :y1="70 + n * 35" :y2="70 + n * 35" />
      </g>

      <path :d="areaPath('income')" fill="url(#incomeArea)" />
      <path :d="areaPath('expense')" fill="url(#expenseArea)" />

      <path :d="linePath('income')" class="trend-line trend-income" filter="url(#trendGlow)" />
      <path :d="linePath('expense')" class="trend-line trend-expense" filter="url(#trendGlow)" />
      <path :d="linePath('balance')" class="trend-line trend-balance" />

      <g v-for="(item, index) in points" :key="item.date">
        <line
          v-if="activeIndex === index"
          class="trend-cursor"
          :x1="x(index)"
          :x2="x(index)"
          y1="52"
          y2="252"
        />
        <circle class="trend-point trend-income-point" :cx="x(index)" :cy="y(item.income)" r="5" @pointerenter="activeIndex = index" @pointerleave="activeIndex = -1" />
        <circle class="trend-point trend-expense-point" :cx="x(index)" :cy="y(item.expense)" r="5" @pointerenter="activeIndex = index" @pointerleave="activeIndex = -1" />
        <text class="trend-date" :x="x(index)" y="276">{{ label(item.date) }}</text>

        <g v-if="activeIndex === index" class="trend-tooltip">
          <rect :x="Math.min(540, Math.max(56, x(index) - 86))" :y="28" width="172" height="76" rx="16" />
          <text :x="Math.min(540, Math.max(56, x(index) - 86)) + 14" y="52">{{ item.date }}</text>
          <text :x="Math.min(540, Math.max(56, x(index) - 86)) + 14" y="74">{{ t("income") }} {{ money(item.income) }}</text>
          <text :x="Math.min(540, Math.max(56, x(index) - 86)) + 14" y="94">{{ t("expense") }} {{ money(item.expense) }}</text>
        </g>
      </g>
    </svg>
  </div>
</template>
