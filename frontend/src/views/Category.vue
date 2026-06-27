<script setup>
import { nextTick, onMounted, reactive, ref } from "vue";
import { categoryApi } from "@/api/categoryApi.js";
import { getResultData, getCategoryDisplayName, isDefaultCategory } from "@/utils/format.js";
import MobileCategoryCard from "@/components/MobileCategoryCard.vue";

const props = defineProps({ t: Function, toast: Function });
const categories = ref([]);
const form = reactive({ id: "", name: "", type: "expense" });
const formOpen = ref(false);
const formPanel = ref(null);

function typeText(type) { return type === "income" ? props.t("income") : props.t("expense"); }
function nameOf(item) { return getCategoryDisplayName(item, props.t); }
function rawNameOf(item) {
  // 默认分类编辑时，表单里显示当前语言的名称，避免英文/日文界面出现中文。
  // 保存后后端会把它转成自定义分类，不再跟随语言自动翻译。
  if (isDefaultCategory(item)) {
    return getCategoryDisplayName(item, props.t);
  }
  return item.name || item.categoryName || "";
}

async function load() {
  try {
    const result = await categoryApi.list();
    categories.value = Array.isArray(getResultData(result)) ? getResultData(result) : [];
  } catch (error) { props.toast(error.message); }
}

async function save() {
  if (!form.name.trim()) return props.toast(props.t("categoryNameRequired"));
  try {
    const body = { name: form.name.trim(), type: form.type };
    const result = form.id ? await categoryApi.update(form.id, body) : await categoryApi.create(body);
    props.toast(result.message || props.t("saveSuccess"));
    clear();
    await load();
  } catch (error) { props.toast(error.message); }
}

function clear() { Object.assign(form, { id: "", name: "", type: "expense" }); }
async function edit(item) {
  Object.assign(form, { id: item.id, name: rawNameOf(item), type: item.type || "expense" });
  formOpen.value = true;
  await nextTick();
  formPanel.value?.scrollIntoView({ behavior: "smooth", block: "start" });
  props.toast(props.t("editModeCategory"));
}
async function remove(item) {
  if (!confirm(props.t("confirmDeleteCategory"))) return;
  try { const result = await categoryApi.remove(item.id); props.toast(result.message || props.t("deleteSuccess")); await load(); }
  catch (error) { props.toast(error.message); }
}

onMounted(load);
defineExpose({ refresh: load });
</script>

<template>
  <section class="view-panel active">
    <div class="content-header"><div><h2>{{ t("categories") }}</h2><p>{{ t("categoriesHint") }}</p></div></div>

    <section ref="formPanel" class="panel mobile-collapsible" :class="{ collapsed: !formOpen }">
      <h3><button type="button" class="mobile-panel-toggle" @click="formOpen = !formOpen"><span>{{ t("categoryForm") }}</span><span class="mobile-panel-arrow">{{ formOpen ? "−" : "＋" }}</span></button></h3>
      <form class="form-grid mobile-collapsible-body" @submit.prevent="save">
        <input v-model="form.id" type="hidden">
        <label><span>{{ t("categoryName") }}</span><input v-model="form.name" type="text"></label>
        <label><span>{{ t("type") }}</span><select v-model="form.type"><option value="income">{{ t("income") }}</option><option value="expense">{{ t("expense") }}</option></select></label>
        <div class="form-actions"><button type="submit" class="primary">{{ t("save") }}</button><button type="button" @click="clear">{{ t("clear") }}</button></div>
      </form>
    </section>

    <section class="panel">
      <h3>{{ t("myCategories") }}</h3>
      <div class="table-wrap">
        <table>
          <thead><tr><th>ID</th><th>{{ t("categoryName") }}</th><th>{{ t("type") }}</th><th>{{ t("actions") }}</th></tr></thead>
          <tbody>
            <tr v-if="!categories.length"><td colspan="4" class="empty">{{ t("noData") }}</td></tr>
            <tr v-for="item in categories" :key="item.id">
              <td>{{ item.id }}</td><td>{{ nameOf(item) }} <span v-if="isDefaultCategory(item)" class="category-badge">{{ t("defaultCategoryBadge") }}</span></td><td>{{ typeText(item.type) }}</td>
              <td class="actions"><button type="button" @click="edit(item)">{{ t("edit") }}</button><button type="button" class="danger" @click="remove(item)">{{ t("remove") }}</button></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mobile-category-list">
        <div v-if="!categories.length" class="mobile-empty">{{ t("noData") }}</div>
        <MobileCategoryCard v-for="item in categories" :key="item.id" :category="item" :t="t" @edit="edit" @remove="remove" />
      </div>
    </section>
  </section>
</template>
