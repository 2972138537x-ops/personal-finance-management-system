<script setup>
import { onMounted, ref } from "vue";
import { statsApi } from "@/api/statsApi.js";
import { currentYear, currentMonth, money, getResultData } from "@/utils/format.js";
import PieChart from "@/components/PieChart.vue";
import TrendChart from "@/components/TrendChart.vue";
import { recordApi } from "@/api/recordApi.js";

const props = defineProps({ t: Function, toast: Function });
const year = ref(currentYear());
const month = ref(currentMonth());
const stats = ref({});
const incomeStats = ref([]);
const expenseStats = ref([]);
const trendRecords = ref([]);

function normalize(list) {
  return (Array.isArray(list) ? list : []).map((item) => ({
    name: item.categoryName || item.name || item.category || "-",
    amount: Number(item.totalAmount || item.amount || item.total || 0)
  })).filter((item) => item.amount > 0);
}

async function load() {
  try {
    stats.value = getResultData(await statsApi.month(year.value, month.value)) || {};
    incomeStats.value = normalize(getResultData(await statsApi.category("income", year.value, month.value)));
    expenseStats.value = normalize(getResultData(await statsApi.category("expense", year.value, month.value)));
    const startRecordDate = `${year.value}-${String(month.value).padStart(2, "0")}-01`;
    const endOfRecordDate = new Date(year.value, month.value, 0).toISOString().slice(0, 10);
    const recordsData = getResultData(await recordApi.search({ page: 1, size: 200, startRecordDate, endOfRecordDate })) || {};
    trendRecords.value = Array.isArray(recordsData.list) ? recordsData.list : [];
  } catch (error) { props.toast(error.message); }
}

onMounted(load);
defineExpose({ refresh: load });
</script>

<template>
  <section class="view-panel active">
    <div class="content-header"><div><h2>{{ t("analyticsCenter") || t("stats") }}</h2><p>{{ t("analyticsHint") || t("statsHint") }}</p></div></div>
    <section class="panel">
      <h3>{{ t("monthlyStats") }}</h3>
      <form class="inline-form" @submit.prevent="load">
        <label><span>{{ t("year") }}</span><input v-model.number="year" type="number"></label>
        <label><span>{{ t("month") }}</span><input v-model.number="month" type="number" min="1" max="12"></label>
        <button type="submit" class="primary">{{ t("query") }}</button>
      </form>
    </section>

    <div class="summary-grid">
      <div class="summary-card income-card"><span>{{ t("incomeTotal") }}</span><strong class="amount-income">{{ money(stats.incomeTotal || stats.income || stats.totalIncome) }}</strong></div>
      <div class="summary-card expense-card"><span>{{ t("expenseTotal") }}</span><strong class="amount-expense">{{ money(stats.expenseTotal || stats.expense || stats.totalExpense) }}</strong></div>
      <div class="summary-card balance-card"><span>{{ t("balance") }}</span><strong :class="Number(stats.balance || 0) < 0 ? 'amount-expense' : 'amount-income'">{{ money(stats.balance) }}</strong></div>
    </div>

    <section class="panel trend-panel">
      <TrendChart :records="trendRecords" :t="t" />
    </section>

    <div class="analytics-chart-grid premium-chart-grid">
      <section class="panel chart-panel"><h3>{{ t("incomeCategoryStats") }}</h3><PieChart :items="incomeStats" type="income" :t="t" /></section>
      <section class="panel chart-panel"><h3>{{ t("expenseCategoryStats") }}</h3><PieChart :items="expenseStats" type="expense" :t="t" /></section>
    </div>
  </section>
</template>
