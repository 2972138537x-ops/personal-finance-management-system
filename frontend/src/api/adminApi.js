import { request } from "./request.js";

export const adminApi = {
  users() {
    return request("/admin/users");
  },
  detail(username) {
    return request("/admin/users/" + encodeURIComponent(username));
  },
  resetPassword(username, newPassword) {
    return request("/admin/users/" + encodeURIComponent(username) + "/password", { method: "PUT", body: { newPassword } });
  },
  remove(username) {
    return request("/admin/users/" + encodeURIComponent(username), { method: "DELETE" });
  }
};
