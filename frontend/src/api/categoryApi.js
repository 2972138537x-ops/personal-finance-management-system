import { request } from "./request.js";

export const categoryApi = {
  list() {
    return request("/transaction-categories");
  },
  create(body) {
    return request("/transaction-categories", { method: "POST", body });
  },
  update(id, body) {
    return request("/transaction-categories/" + encodeURIComponent(id), { method: "PUT", body });
  },
  remove(id) {
    return request("/transaction-categories/" + encodeURIComponent(id), { method: "DELETE" });
  }
};
