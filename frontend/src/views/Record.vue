<script setup>
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import { categoryApi } from "@/api/categoryApi.js";
import { recordApi } from "@/api/recordApi.js";
import { getResultData, getCategoryDisplayName, getCategoryName, money, today } from "@/utils/format.js";
import MobileRecordCard from "@/components/MobileRecordCard.vue";

const props = defineProps({ t: Function, toast: Function });
const categories = ref([]);
const records = ref([]);
const total = ref(0);
const page = ref(1);
const size = ref(10);
const formOpen = ref(false);
const formPanel = ref(null);
const filterOpen = ref(false);
const form = reactive({ id: "", type: "expense", categoryId: "", amount: "", recordDate: today(), remark: "" });
const filter = reactive({ type: "", categoryId: "", startRecordDate: "", endOfRecordDate: "" });

const filteredCategories = computed(() => categories.value.filter((item) => !item.type || item.type === form.type));
const totalPage = computed(() => Math.max(1, Math.ceil(total.value / size.value)));

function typeText(type) { return type === "income" ? props.t("income") : props.t("expense"); }

async function loadCategories() {
  const result = await categoryApi.list();
  categories.value = Array.isArray(getResultData(result)) ? getResultData(result) : [];
}

async function load() {
  try {
    await loadCategories();
    const result = await recordApi.search({
      page: page.value,
      size: size.value,
      type: filter.type,
      categoryId: filter.categoryId,
      startRecordDate: filter.startRecordDate,
      endOfRecordDate: filter.endOfRecordDate
    });
    const data = getResultData(result) || {};
    records.value = Array.isArray(data.list) ? data.list : [];
    total.value = Number(data.total || 0);
    page.value = Number(data.page || page.value);
    size.value = Number(data.size || size.value);
  } catch (error) { props.toast(error.message); }
}

watch(() => form.type, () => {
  if (filteredCategories.value.length && !filteredCategories.value.some((item) => String(item.id) === String(form.categoryId))) {
    form.categoryId = String(filteredCategories.value[0].id);
  }
});

async function save() {
  if (!form.categoryId) return props.toast(props.t("categoryRequired"));
  if (!form.amount) return props.toast(props.t("amountRequired"));
  if (!form.recordDate) return props.toast(props.t("dateRequired"));
  try {
    const body = { categoryId: Number(form.categoryId), type: form.type, amount: Number(form.amount), recordDate: form.recordDate, remark: form.remark.trim() };
    const result = form.id ? await recordApi.update(form.id, body) : await recordApi.create(body);
    props.toast(result.message || props.t("saveSuccess"));
    clear();
    page.value = 1;
    await load();
  } catch (error) { props.toast(error.message); }
}

function clear() { Object.assign(form, { id: "", type: "expense", categoryId: "", amount: "", recordDate: today(), remark: "" }); }
async function edit(record) {
  Object.assign(form, {
    id: record.id,
    type: record.type || "expense",
    categoryId: record.categoryId || "",
    amount: record.amount || "",
    recordDate: record.recordDate || today(),
    remark: record.remark || ""
  });
  formOpen.value = true;
  await nextTick();
  formPanel.value?.scrollIntoView({ behavior: "smooth", block: "start" });
  props.toast(props.t("editModeRecord"));
}
async function remove(record) { if (!confirm(props.t("confirmDeleteRecord"))) return; try { const result = await recordApi.remove(record.id); props.toast(result.message || props.t("deleteSuccess")); await load(); } catch (error) { props.toast(error.message); } }
async function applyFilter(type) { filter.type = type; page.value = 1; await load(); props.toast(props.t("recordsUpdated")); }
async function applyRange() {
  if ((filter.startRecordDate && !filter.endOfRecordDate) || (!filter.startRecordDate && filter.endOfRecordDate)) return props.toast(props.t("dateRangeRequired"));
  if (filter.startRecordDate && filter.endOfRecordDate && filter.startRecordDate > filter.endOfRecordDate) return props.toast(props.t("startDateAfterEndDate"));
  page.value = 1;
  await load();
}
async function prev() { if (page.value > 1) { page.value--; await load(); } }
async function next() { if (page.value < totalPage.value) { page.value++; await load(); } }

onMounted(load);
defineExpose({ refresh: load });
</script>

<template>
  <section class="view-panel active">
    <div class="content-header"><div><h2>{{ t("records") }}</h2><p>{{ t("recordsHint") }}</p></div></div>

    <section ref="formPanel" class="panel mobile-collapsible" :class="{ collapsed: !formOpen }">
      <h3><button type="button" class="mobile-panel-toggle" @click="formOpen = !formOpen"><span>{{ t("recordForm") }}</span><span class="mobile-panel-arrow">{{ formOpen ? "−" : "＋" }}</span></button></h3>
      <form class="form-grid mobile-collapsible-body" @submit.prevent="save">
        <input v-model="form.id" type="hidden">
        <label><span>{{ t("type") }}</span><select v-model="form.type"><option value="income">{{ t("income") }}</option><option value="expense">{{ t("expense") }}</option></select></label>
        <label><span>{{ t("category") }}</span><select v-model="form.categoryId"><option v-if="!filteredCategories.length" value="">{{ t("noData") }}</option><option v-for="item in filteredCategories" :key="item.id" :value="item.id">{{ getCategoryDisplayName(item, t) }}</option></select><em v-if="!filteredCategories.length" class="field-hint">{{ t("createCategoryFirst") }}</em></label>
        <label><span>{{ t("amount") }}</span><input v-model="form.amount" type="number" step="0.01"></label>
        <label><span>{{ t("date") }}</span><input v-model="form.recordDate" type="date"></label>
        <label><span>{{ t("remark") }}</span><input v-model="form.remark" type="text"></label>
        <div class="form-actions"><button type="submit" class="primary">{{ t("save") }}</button><button type="button" @click="clear">{{ t("clear") }}</button></div>
      </form>
    </section>

    <section class="panel mobile-collapsible" :class="{ collapsed: !filterOpen }">
      <h3><button type="button" class="mobile-panel-toggle" @click="filterOpen = !filterOpen"><span>{{ t("filter") }}</span><span class="mobile-panel-arrow">{{ filterOpen ? "−" : "＋" }}</span></button></h3>
      <div class="mobile-collapsible-body">
        <div class="filter-buttons">
          <button type="button" @click="applyFilter('')">{{ t("allRecords") }}</button>
          <button type="button" @click="applyFilter('income')">{{ t("incomeOnly") }}</button>
          <button type="button" @click="applyFilter('expense')">{{ t("expenseOnly") }}</button>
        </div>
        <form class="inline-form" @submit.prevent="applyRange">
          <label><span>{{ t("startDate") }}</span><input v-model="filter.startRecordDate" type="date"></label>
          <label><span>{{ t("endDate") }}</span><input v-model="filter.endOfRecordDate" type="date"></label>
          <label><span>{{ t("category") }}</span><select v-model="filter.categoryId"><option value="">{{ t("allCategories") }}</option><option v-for="item in categories" :key="item.id" :value="item.id">{{ getCategoryDisplayName(item, t) }} / {{ typeText(item.type) }}</option></select></label>
          <button type="submit" class="primary">{{ t("queryByDate") }}</button>
        </form>
      </div>
    </section>

    <section class="panel">
      <h3>{{ t("myRecords") }}</h3>
      <div class="table-wrap">
        <table>
          <thead><tr><th>ID</th><th>{{ t("category") }}</th><th>{{ t("type") }}</th><th>{{ t("amount") }}</th><th>{{ t("remark") }}</th><th>{{ t("date") }}</th><th>{{ t("actions") }}</th></tr></thead>
          <tbody>
            <tr v-if="!records.length"><td colspan="7" class="empty">{{ t("noData") }}</td></tr>
            <tr v-for="record in records" :key="record.id">
              <td>{{ record.id }}</td><td>{{ getCategoryName(record, categories, t) }}</td><td>{{ typeText(record.type) }}</td><td :class="record.type === 'income' ? 'amount-income' : 'amount-expense'">{{ money(record.amount) }}</td><td>{{ record.remark }}</td><td>{{ record.recordDate }}</td>
              <td class="actions"><button type="button" @click="edit(record)">{{ t("edit") }}</button><button type="button" class="danger" @click="remove(record)">{{ t("remove") }}</button></td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="mobile-record-list">
        <div v-if="!records.length" class="mobile-empty">{{ t("noData") }}</div>
        <MobileRecordCard v-for="record in records" :key="record.id" :record="record" :categories="categories" :t="t" @edit="edit" @remove="remove" />
      </div>

      <div class="pagination">
        <button type="button" class="pager-button" :disabled="page <= 1" @click="prev">{{ t("prevPage") }}</button>
        <span class="pagination-info">{{ page }} / {{ totalPage }} {{ t("pageUnit") }}，{{ t("totalPrefix") }} {{ total }} {{ t("totalSuffix") }}</span>
        <button type="button" class="pager-button" :disabled="page >= totalPage" @click="next">{{ t("nextPage") }}</button>
      </div>
    </section>
  </section>
</template>
