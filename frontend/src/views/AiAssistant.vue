<script setup>
import { ref, watch } from "vue";
import { aiApi } from "@/api/aiApi.js";
import { getResultData } from "@/utils/format.js";

const props = defineProps({ t: Function, toast: Function });
const question = ref("");
const answer = ref("");
const hasAnswer = ref(false);
const sending = ref(false);

watch(() => props.t("aiDefaultAnswer"), () => {
  if (!hasAnswer.value) answer.value = props.t("aiDefaultAnswer");
}, { immediate: true });

async function send() {
  const message = question.value.trim();
  if (!message) return props.toast(props.t("questionRequired"));
  sending.value = true;
  answer.value = props.t("aiThinking");
  hasAnswer.value = true;
  try {
    const result = await aiApi.chat(message);
    answer.value = getResultData(result) || result.message || props.t("aiEmpty");
    props.toast(result.message || props.t("aiSuccess"));
  } catch (error) {
    answer.value = error.message;
    props.toast(error.message);
  } finally {
    sending.value = false;
  }
}
</script>

<template>
  <section class="view-panel active">
    <div class="content-header"><div><h2>{{ t("aiAssistant") }}</h2><p>{{ t("aiAssistantHint") }}</p></div></div>
    <section class="panel">
      <h3>{{ t("aiQuestion") }}</h3>
      <form class="form-grid" @submit.prevent="send">
        <label class="ai-question-label"><span>{{ t("aiQuestion") }}</span><textarea v-model="question" class="ai-question-input" :placeholder="t('aiQuestionPlaceholder')"></textarea></label>
        <div class="form-actions"><button type="submit" class="primary" :disabled="sending">{{ sending ? t("sending") : t("aiSend") }}</button><button type="button" @click="question = ''">{{ t("clear") }}</button></div>
      </form>
    </section>
    <section class="panel"><h3>{{ t("aiAnswer") }}</h3><div class="ai-answer">{{ answer }}</div></section>
  </section>
</template>
