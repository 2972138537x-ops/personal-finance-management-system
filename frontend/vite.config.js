import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";

const backend = "http://localhost:8080";

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url))
    }
  },
  server: {
    port: 5173,
    proxy: {
      "/login": { target: backend, changeOrigin: true },
      "/logout": { target: backend, changeOrigin: true },
      "/users": { target: backend, changeOrigin: true },
      "/me": { target: backend, changeOrigin: true },
      "/transaction-categories": { target: backend, changeOrigin: true },
      "/transaction-records": { target: backend, changeOrigin: true },
      "/transaction-stats": { target: backend, changeOrigin: true },
      "/ai": { target: backend, changeOrigin: true },
      "/admin": { target: backend, changeOrigin: true }
    }
  },
  build: {
    outDir: "../src/main/resources/static",
    assetsDir: "assets",
    emptyOutDir: true
  }
});
