import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";

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
      "/login": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/register": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/logout": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/me": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/users": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/transaction-categories": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/transaction-records": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/transaction-stats": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/ai": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/admin": {
        target: "http://localhost:8080",
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: "../src/main/resources/static",
    assetsDir: "assets",
    emptyOutDir: false
  }
});
