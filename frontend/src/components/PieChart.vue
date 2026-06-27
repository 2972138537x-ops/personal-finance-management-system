<script setup>
import { computed, ref } from "vue";
import { money } from "@/utils/format.js";

const props = defineProps({
  items: { type: Array, default: () => [] },
  type: { type: String, default: "income" },
  t: { type: Function, required: true }
});

const activeIndex = ref(-1);

const palette = computed(() => props.type === "income"
  ? ["#0f8277", "#36b37e", "#68d8c1", "#8bd3a3", "#4fb3a8", "#7fc8e8", "#a8d08d", "#74b49b"]
  : ["#0f8277", "#52b788", "#3f8ed8", "#d9a52f", "#dd6b5f", "#7b6ed6", "#e08b4f", "#5aa7a7"]
);

const total = computed(() => props.items.reduce((sum, item) => sum + Number(item.amount || 0), 0));

const chartItems = computed(() => {
  let start = -90;
  return props.items.map((item, index) => {
    const value = Number(item.amount || 0);
    const angle = total.value ? value / total.value * 360 : 0;
    const end = start + angle;
    const mid = start + angle / 2;
    const result = {
      ...item,
      value,
      index,
      start,
      end,
      mid,
      percent: total.value ? value / total.value * 100 : 0,
      color: palette.value[index % palette.value.length]
    };
    start = end;
    return result;
  });
});

function polar(cx, cy, r, angleDeg) {
  const angle = (angleDeg - 90) * Math.PI / 180;
  return {
    x: cx + r * Math.cos(angle),
    y: cy + r * Math.sin(angle)
  };
}

function arcPath(item, radius = 92) {
  const cx = 120;
  const cy = 120;
  const start = polar(cx, cy, radius, item.start + 90);
  const end = polar(cx, cy, radius, item.end + 90);
  const largeArc = item.end - item.start > 180 ? 1 : 0;
  return `M ${cx} ${cy} L ${start.x} ${start.y} A ${radius} ${radius} 0 ${largeArc} 1 ${end.x} ${end.y} Z`;
}

function labelPosition(item) {
  const p1 = polar(120, 120, 98, item.mid + 90);
  const p2 = polar(120, 120, 118, item.mid + 90);
  const right = p2.x >= 120;
  const x3 = p2.x + (right ? 34 : -34);
  return { p1, p2, x3, y3: p2.y, right };
}

function activeItem() {
  return activeIndex.value >= 0 ? chartItems.value[activeIndex.value] : null;
}
</script>

<template>
  <div v-if="!items.length" class="empty">{{ t("noData") }}</div>

  <div v-else class="premium-pie-layout">
    <div class="premium-pie-stage">
      <div v-if="activeItem()" class="mobile-pie-active-label">
        <strong>{{ activeItem().percent.toFixed(1) }}%</strong>
        <span>{{ activeItem().name }} · {{ money(activeItem().amount) }}</span>
      </div>

      <svg class="premium-pie-svg" viewBox="0 0 300 250" aria-label="category chart">
        <defs>
          <filter id="pieShadow" x="-30%" y="-30%" width="160%" height="160%">
            <feDropShadow dx="0" dy="12" stdDeviation="10" flood-color="#10242a" flood-opacity="0.14" />
          </filter>
          <filter id="pieSoftShadow" x="-25%" y="-25%" width="150%" height="150%">
            <feDropShadow dx="0" dy="5" stdDeviation="5" flood-color="#10242a" flood-opacity="0.10" />
          </filter>
          <radialGradient id="pieGloss" cx="35%" cy="25%" r="70%">
            <stop offset="0%" stop-color="rgba(255,255,255,0.45)" />
            <stop offset="100%" stop-color="rgba(255,255,255,0)" />
          </radialGradient>
        </defs>

        <ellipse class="pie-base-shadow" cx="120" cy="210" rx="86" ry="18" />

        <g filter="url(#pieShadow)">
          <path
            v-for="item in chartItems"
            :key="item.name"
            class="premium-pie-slice"
            :class="{ active: activeIndex === item.index, dimmed: activeIndex >= 0 && activeIndex !== item.index }"
            :d="arcPath(item)"
            :fill="item.color"
            @pointerenter="activeIndex = item.index"
            @pointerleave="activeIndex = -1"
            @pointerdown="activeIndex = activeIndex === item.index ? -1 : item.index"
          />
        </g>

        <circle cx="120" cy="120" r="92" fill="url(#pieGloss)" pointer-events="none" opacity="0.55" />

        <g v-if="activeItem()" class="pie-label-layer">
          <template v-for="item in [activeItem()]" :key="item.name">
            <polyline
              class="pie-leader-line"
              :points="`${labelPosition(item).p1.x},${labelPosition(item).p1.y} ${labelPosition(item).p2.x},${labelPosition(item).p2.y} ${labelPosition(item).x3},${labelPosition(item).y3}`"
            />
            <circle class="pie-leader-dot" :cx="labelPosition(item).p1.x" :cy="labelPosition(item).p1.y" r="3.4" :fill="item.color" />
            <g :transform="`translate(${labelPosition(item).right ? labelPosition(item).x3 + 8 : labelPosition(item).x3 - 68}, ${labelPosition(item).y3 - 15})`">
              <rect class="pie-percent-box" width="60" height="30" rx="15" />
              <text class="pie-percent-text" x="30" y="19">{{ item.percent.toFixed(1) }}%</text>
            </g>
          </template>
        </g>
      </svg>
    </div>

    <div class="premium-chart-legend">
      <button
        v-for="item in chartItems"
        :key="item.name"
        type="button"
        class="premium-legend-row"
        :class="{ active: activeIndex === item.index }"
        @pointerenter="activeIndex = item.index"
        @pointerleave="activeIndex = -1"
        @click="activeIndex = activeIndex === item.index ? -1 : item.index"
      >
        <span class="chart-dot" :style="{ background: item.color }"></span>
        <span class="legend-name">{{ item.name }}</span>
        <span class="legend-value">
          <strong :class="type === 'income' ? 'amount-income' : 'amount-expense'">{{ money(item.amount) }}</strong>
          <small>{{ item.percent.toFixed(1) }}%</small>
        </span>
      </button>
    </div>
  </div>
</template>
