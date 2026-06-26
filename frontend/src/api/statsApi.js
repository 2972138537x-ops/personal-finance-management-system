import { request } from "./request.js";

export const statsApi = {
  month(year, month) {
    return request(`/transaction-stats/month?year=${year}&month=${month}`);
  },
  category(type, year, month) {
    return request(`/transaction-stats/category/${encodeURIComponent(type)}?year=${year}&month=${month}`);
  }
};
