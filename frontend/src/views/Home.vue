<script setup>
import { computed, onMounted, ref } from "vue";
import { currentYear, currentMonth, money, getResultData, getCategoryName, getCategoryDisplayName } from "@/utils/format.js";
import { statsApi } from "@/api/statsApi.js";
import { recordApi } from "@/api/recordApi.js";
import { categoryApi } from "@/api/categoryApi.js";

const props = defineProps({ t: Function, toast: Function });
const emit = defineEmits(["navigate"]);
const year = ref(currentYear());
const month = ref(currentMonth());
const stats = ref({});
const categories = ref([]);
const recent = ref([]);
const incomeStats = ref([]);
const expenseStats = ref([]);
const topIncome = computed(() => incomeStats.value[0] || null);
const topExpense = computed(() => expenseStats.value[0] || null);
const trendRecords = ref([]);

function normalizeStats(list) {
  const raw = Array.isArray(list) ? list : [];
  return raw.map((item) => ({
    name: getCategoryDisplayName(item, props.t),
    amount: Number(item.totalAmount || item.amount || item.total || 0)
  })).filter((item) => item.amount > 0);
}

async function load() {
  try {
    const statResult = await statsApi.month(year.value, month.value);
    stats.value = getResultData(statResult) || {};

    const categoryResult = await categoryApi.list();
    categories.value = Array.isArray(getResultData(categoryResult)) ? getResultData(categoryResult) : [];

    const startRecordDate = `${year.value}-${String(month.value).padStart(2, "0")}-01`;
    const endOfRecordDate = new Date(year.value, month.value, 0).toISOString().slice(0, 10);
    const recentResult = await recordApi.search({ page: 1, size: 5, startRecordDate, endOfRecordDate });
    recent.value = getResultData(recentResult)?.list || [];
    const trendResult = await recordApi.search({ page: 1, size: 200, startRecordDate, endOfRecordDate });
    trendRecords.value = getResultData(trendResult)?.list || [];

    incomeStats.value = normalizeStats(getResultData(await statsApi.category("income", year.value, month.value)));
    expenseStats.value = normalizeStats(getResultData(await statsApi.category("expense", year.value, month.value)));
  } catch (error) {
    props.toast(error.message);
  }
}

onMounted(load);
defineExpose({ refresh: load });
</script>

<template>
  <section class="view-panel active">
    <div class="content-header">
      <div>
        <h2>{{ t("home") }}</h2>
        <p>{{ t("homeHint") }}</p>
      </div>
    </div>

    <section class="panel home-insight-panel">
      <div class="home-section-head">
        <div>
          <h3>{{ t("homeOverview") }}</h3>
          <p>{{ t("homeOverviewHint") }}</p>
        </div>
      </div>
      <div class="home-insight-grid">
        <div class="home-insight-card">
          <span>{{ t("topIncomeCategory") }}</span>
          <strong>{{ topIncome ? topIncome.name : "-" }}</strong>
          <em class="amount-income">{{ topIncome ? money(topIncome.amount) : money(0) }}</em>
        </div>
        <div class="home-insight-card">
          <span>{{ t("topExpenseCategory") }}</span>
          <strong>{{ topExpense ? topExpense.name : "-" }}</strong>
          <em class="amount-expense">{{ topExpense ? money(topExpense.amount) : money(0) }}</em>
        </div>
        <div class="home-insight-card">
          <span>{{ t("recordCount") }}</span>
          <strong>{{ trendRecords.length }}</strong>
          <em>{{ t("records") }}</em>
        </div>
      </div>
    </section>

<section class="panel">
      <h3>{{ t("recentRecords") }}</h3>
      <div class="table-wrap">
        <table>
          <thead><tr><th>ID</th><th>{{ t("category") }}</th><th>{{ t("type") }}</th><th>{{ t("amount") }}</th><th>{{ t("date") }}</th></tr></thead>
          <tbody>
            <tr v-if="!recent.length"><td colspan="5" class="empty">{{ t("noData") }}</td></tr>
            <tr v-for="record in recent" :key="record.id">
              <td>{{ record.id }}</td>
              <td>{{ getCategoryName(record, categories, t) }}</td>
              <td>{{ record.type === 'income' ? t('income') : t('expense') }}</td>
              <td :class="record.type === 'income' ? 'amount-income' : 'amount-expense'">{{ money(record.amount) }}</td>
              <td>{{ record.recordDate }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="home-mobile-recent-list">
        <div v-for="record in recent" :key="record.id" class="home-mobile-recent-card">
          <div>
            <strong>{{ getCategoryName(record, categories, t) }}</strong>
            <span>{{ record.type === "income" ? t("income") : t("expense") }} · {{ record.recordDate }}</span>
          </div>
          <em :class="record.type === 'income' ? 'amount-income' : 'amount-expense'">{{ money(record.amount) }}</em>
        </div>
        <div v-if="!recent.length" class="empty">{{ t("noData") }}</div>
      </div>
    </section>


    <section class="panel home-shortcut-panel">
      <div class="home-section-head">
        <div>
          <h3>{{ t("quickActions") }}</h3>
          <p>{{ t("quickActionsHint") }}</p>
        </div>
      </div>
      <div class="home-shortcut-grid">
        <button type="button" class="home-shortcut-card" @click="emit('navigate', 'records')">
          <strong>{{ t("records") }}</strong>
          <span>{{ t("quickRecordHint") }}</span>
        </button>
        <button type="button" class="home-shortcut-card" @click="emit('navigate', 'categories')">
          <strong>{{ t("categories") }}</strong>
          <span>{{ t("quickCategoryHint") }}</span>
        </button>
        <button type="button" class="home-shortcut-card" @click="emit('navigate', 'stats')">
          <strong>{{ t("stats") }}</strong>
          <span>{{ t("quickStatsHint") }}</span>
        </button>
      </div>
    </section>
</section>
</template>
