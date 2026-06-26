import { request } from "./request.js";
import { buildQuery } from "@/utils/format.js";

export const recordApi = {
  search(params) {
    return request("/transaction-records/search?" + buildQuery(params));
  },
  create(body) {
    return request("/transaction-records", { method: "POST", body });
  },
  update(id, body) {
    return request("/transaction-records/" + encodeURIComponent(id), { method: "PUT", body });
  },
  remove(id) {
    return request("/transaction-records/" + encodeURIComponent(id), { method: "DELETE" });
  }
};
