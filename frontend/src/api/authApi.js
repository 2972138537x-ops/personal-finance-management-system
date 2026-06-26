import { request } from "./request.js";

export const authApi = {
  login(username, password) {
    return request("/login", {
      method: "POST",
      auth: false,
      body: {
        username: username,
        password: password
      }
    });
  },

  register(username, password) {
    return request("/users", {
      method: "POST",
      auth: false,
      body: {
        username: username,
        password: password
      }
    });
  },

  logout() {
    return request("/logout", { method: "POST" });
  },

  me() {
    return request("/me");
  },

  changePassword(oldPassword, newPassword) {
    return request("/me/password", {
      method: "PUT",
      body: {
        oldPassword: oldPassword,
        newPassword: newPassword
      }
    });
  },

  deleteMe(password, confirmPassword) {
    return request("/me", {
      method: "DELETE",
      body: {
        password: password,
        confirmPassword: confirmPassword
      }
    });
  }
};
