import { request } from "./request.js";

export const aiApi = {
  chat(message) {
    return request("/ai/chat", { method: "POST", body: { message } });
  }
};
