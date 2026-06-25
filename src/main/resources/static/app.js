(function () {
    const state = {
        token: localStorage.getItem("pf.token") || "",
        user: readJson(localStorage.getItem("pf.user")) || null,
        lang: localStorage.getItem("pf.lang") || "zh",
        activeTab: "home",
        categories: [],
        records: [],
        recordPage: 1,
        recordSize: 10,
        recordTotal: 0,
        recordFilter: {
            type: "",
            categoryId: "",
            startRecordDate: "",
            endOfRecordDate: ""
        },
        homeYear: currentYear(),
        homeMonth: currentMonth(),
        adminDetail: null
    };

    const i18n = {
        zh: {
            appTitle: "个人收支管理系统",
            appSubTitle: "Spring Boot + MyBatis + MySQL",
            login: "登录",
            register: "注册",
            loginHint: "登录后进入收支管理系统。",
            registerHint: "创建普通用户账号。",
            username: "用户名",
            password: "密码",
            noAccount: "还没有账号？",
            goRegister: "去注册",
            hasAccount: "已有账号？",
            goLogin: "去登录",
            loggedIn: "已登录",
            role: "角色",
            refresh: "刷新",
            logout: "退出",
            home: "首页",
            homeHint: "查看指定月份的收入、支出、结余和最近记录。",
            profile: "个人信息",
            profileHint: "查看当前登录用户信息并修改登录密码。",
            accountInfo: "账号信息",
            categories: "分类管理",
            categoriesHint: "管理收入和支出分类。",
            records: "收支记录",
            recordsHint: "新增、修改和删除收支记录。",
            stats: "统计",
            statsHint: "按月份查看收入、支出、结余和分类统计。",
            admin: "管理员",
            adminHint: "查询用户、查看详情、重置密码、删除用户。",
            incomeTotal: "收入合计",
            expenseTotal: "支出合计",
            balance: "结余",
            recentRecords: "最近记录",
            incomeCategoryStats: "收入分类统计",
            expenseCategoryStats: "支出分类统计",
            categoryForm: "新增 / 编辑分类",
            myCategories: "分类列表",
            recordForm: "新增 / 编辑记录",
            myRecords: "记录列表",
            filter: "筛选",
            allRecords: "全部记录",
            incomeOnly: "只看收入",
            expenseOnly: "只看支出",
            startDate: "开始日期",
            endDate: "结束日期",
            queryByDate: "按日期查询",
            monthlyStats: "月度统计",
            typeTotal: "类型总额",
            userManagement: "用户管理",
            userDetail: "用户详情",
            resetPassword: "重置密码",
            newPassword: "新密码",
            oldPassword: "当前密码",
            changePassword: "修改密码",
            confirmPassword: "确认新密码",
            queryOneUser: "查询单个用户",
            queryAllUsers: "查询全部用户",
            category: "分类",
            categoryName: "分类名",
            type: "类型",
            amount: "金额",
            date: "日期",
            remark: "备注",
            actions: "操作",
            year: "年份",
            month: "月份",
            query: "查询",
            save: "保存",
            clear: "清空",
            income: "收入",
            expense: "支出",
            edit: "编辑",
            remove: "删除",
            detail: "详情",
            noData: "暂无数据",
            prevPage: "上一页",
            nextPage: "下一页",
            pageUnit: "页",
            totalPrefix: "共",
            totalSuffix: "条",
            selectUserHint: "点击“详情”后显示用户信息。"
        },
        ja: {
            appTitle: "個人収支管理システム",
            appSubTitle: "Spring Boot + MyBatis + MySQL",
            login: "ログイン",
            register: "登録",
            loginHint: "ログイン後、収支管理システムに入ります。",
            registerHint: "一般ユーザーアカウントを作成します。",
            username: "ユーザー名",
            password: "パスワード",
            noAccount: "アカウントがありませんか？",
            goRegister: "登録へ",
            hasAccount: "すでにアカウントがありますか？",
            goLogin: "ログインへ",
            loggedIn: "ログイン中",
            role: "権限",
            refresh: "更新",
            logout: "ログアウト",
            home: "ホーム",
            homeHint: "指定した月の収入、支出、残高、最近の記録を表示します。",
            profile: "個人情報",
            profileHint: "ログイン中のユーザー情報を確認し、パスワードを変更します。",
            accountInfo: "アカウント情報",
            categories: "カテゴリ管理",
            categoriesHint: "収入・支出カテゴリを管理します。",
            records: "収支記録",
            recordsHint: "収支記録を追加・編集・削除します。",
            stats: "統計",
            statsHint: "月別に収入、支出、残高、カテゴリ統計を確認します。",
            admin: "管理者",
            adminHint: "ユーザー検索、詳細確認、パスワード変更、削除を行います。",
            incomeTotal: "収入合計",
            expenseTotal: "支出合計",
            balance: "残高",
            recentRecords: "最近の記録",
            incomeCategoryStats: "収入カテゴリ統計",
            expenseCategoryStats: "支出カテゴリ統計",
            categoryForm: "カテゴリの追加 / 編集",
            myCategories: "カテゴリ一覧",
            recordForm: "記録の追加 / 編集",
            myRecords: "記録一覧",
            filter: "絞り込み",
            allRecords: "すべての記録",
            incomeOnly: "収入のみ",
            expenseOnly: "支出のみ",
            startDate: "開始日",
            endDate: "終了日",
            queryByDate: "日付で検索",
            monthlyStats: "月次統計",
            typeTotal: "タイプ別合計",
            userManagement: "ユーザー管理",
            userDetail: "ユーザー詳細",
            resetPassword: "パスワード変更",
            newPassword: "新しいパスワード",
            oldPassword: "現在のパスワード",
            changePassword: "パスワード変更",
            confirmPassword: "新しいパスワード確認",
            queryOneUser: "単一ユーザー検索",
            queryAllUsers: "全ユーザー検索",
            category: "カテゴリ",
            categoryName: "カテゴリ名",
            type: "タイプ",
            amount: "金額",
            date: "日付",
            remark: "メモ",
            actions: "操作",
            year: "年",
            month: "月",
            query: "検索",
            save: "保存",
            clear: "クリア",
            income: "収入",
            expense: "支出",
            edit: "編集",
            remove: "削除",
            detail: "詳細",
            noData: "データなし",
            prevPage: "前のページ",
            nextPage: "次のページ",
            pageUnit: "ページ",
            totalPrefix: "全",
            totalSuffix: "件",
            selectUserHint: "「詳細」をクリックするとユーザー情報が表示されます。"
        },
        en: {
            appTitle: "Personal Finance System",
            appSubTitle: "Spring Boot + MyBatis + MySQL",
            login: "Login",
            register: "Register",
            loginHint: "Log in to enter the finance management system.",
            registerHint: "Create a normal user account.",
            username: "Username",
            password: "Password",
            noAccount: "No account?",
            goRegister: "Register",
            hasAccount: "Already have an account?",
            goLogin: "Login",
            loggedIn: "Logged in",
            role: "Role",
            refresh: "Refresh",
            logout: "Logout",
            home: "Home",
            homeHint: "View income, expense, balance, and recent records for the selected month.",
            profile: "Profile",
            profileHint: "View your account information and change your password.",
            accountInfo: "Account Info",
            categories: "Categories",
            categoriesHint: "Manage income and expense categories.",
            records: "Records",
            recordsHint: "Add, edit, and delete transaction records.",
            stats: "Stats",
            statsHint: "View monthly income, expense, balance, and category stats.",
            admin: "Admin",
            adminHint: "Search users, view details, reset passwords, and delete users.",
            incomeTotal: "Income Total",
            expenseTotal: "Expense Total",
            balance: "Balance",
            recentRecords: "Recent Records",
            incomeCategoryStats: "Income Category Stats",
            expenseCategoryStats: "Expense Category Stats",
            categoryForm: "Add / Edit Category",
            myCategories: "Category List",
            recordForm: "Add / Edit Record",
            myRecords: "Record List",
            filter: "Filter",
            allRecords: "All Records",
            incomeOnly: "Income Only",
            expenseOnly: "Expense Only",
            startDate: "Start Date",
            endDate: "End Date",
            queryByDate: "Query by Date",
            monthlyStats: "Monthly Stats",
            typeTotal: "Type Total",
            userManagement: "User Management",
            userDetail: "User Detail",
            resetPassword: "Reset Password",
            newPassword: "New Password",
            oldPassword: "Current Password",
            changePassword: "Change Password",
            confirmPassword: "Confirm New Password",
            queryOneUser: "Query One User",
            queryAllUsers: "Query All Users",
            category: "Category",
            categoryName: "Category Name",
            type: "Type",
            amount: "Amount",
            date: "Date",
            remark: "Remark",
            actions: "Actions",
            year: "Year",
            month: "Month",
            query: "Query",
            save: "Save",
            clear: "Clear",
            income: "Income",
            expense: "Expense",
            edit: "Edit",
            remove: "Delete",
            detail: "Detail",
            noData: "No Data",
            prevPage: "Previous",
            nextPage: "Next",
            pageUnit: "page",
            totalPrefix: "Total",
            totalSuffix: "records",
            selectUserHint: "Click Detail to view user information."
        }
    };

    function $(id) {
        return document.getElementById(id);
    }

    function text(key) {
        return (i18n[state.lang] && i18n[state.lang][key]) || i18n.zh[key] || key;
    }

    function readJson(value) {
        try {
            return value ? JSON.parse(value) : null;
        } catch (e) {
            return null;
        }
    }

    function today() {
        return new Date().toISOString().slice(0, 10);
    }

    function currentYear() {
        return new Date().getFullYear();
    }

    function currentMonth() {
        return new Date().getMonth() + 1;
    }

    function money(value) {
        return Number(value || 0).toFixed(2);
    }

    function escapeHtml(value) {
        return String(value === undefined || value === null ? "" : value)
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;");
    }

    function toast(message) {
        const el = $("toast");
        if (!el) return;

        el.textContent = message || "";
        el.classList.add("show");

        clearTimeout(toast.timer);
        toast.timer = setTimeout(function () {
            el.classList.remove("show");
        }, 2300);
    }

    function saveAuth() {
        if (state.token) {
            localStorage.setItem("pf.token", state.token);
        } else {
            localStorage.removeItem("pf.token");
        }

        if (state.user) {
            localStorage.setItem("pf.user", JSON.stringify(state.user));
        } else {
            localStorage.removeItem("pf.user");
        }
    }

    function getResultData(result) {
        if (!result) return null;
        if (Object.prototype.hasOwnProperty.call(result, "data")) return result.data;
        return result;
    }

    function getErrorMessage(result) {
        if (!result) return "";
        return result.message || result.error || result.msg || "";
    }

    async function api(path, options) {
        const opts = options || {};
        const headers = {
            "Content-Type": "application/json"
        };

        if (state.token && opts.auth !== false) {
            headers.Authorization = state.token;
        }

        const response = await fetch(path, {
            method: opts.method || "GET",
            headers: headers,
            body: opts.body === undefined ? undefined : JSON.stringify(opts.body)
        });

        const result = await response.json().catch(function () {
            return null;
        });

        if (!response.ok) {
            throw new Error(getErrorMessage(result) || ("HTTP " + response.status));
        }

        if (result && result.success === false) {
            throw new Error(result.message || "请求失败");
        }

        if (result && result.code !== undefined && Number(result.code) >= 400) {
            throw new Error(result.message || "请求失败");
        }

        return result;
    }

    function translatePage() {
        document.querySelectorAll("[data-i18n]").forEach(function (el) {
            const key = el.dataset.i18n;
            el.textContent = text(key);
        });

        document.querySelectorAll(".lang-btn").forEach(function (btn) {
            btn.classList.toggle("active", btn.dataset.lang === state.lang);
        });

        document.documentElement.lang = state.lang === "zh" ? "zh-CN" : state.lang;

        setupTypeSelects();
        fillRecordCategoryOptions();
        renderCurrentUser();

        if (state.categories.length > 0) renderCategoryTable();

        if (state.records.length > 0) {
            renderRecordTable(state.records);
            renderRecordPagination();
        }

        if (state.adminDetail) {
            renderAdminDetail(state.adminDetail);
        } else if ($("adminDetail")) {
            $("adminDetail").textContent = text("selectUserHint");
        }
    }

    function setLanguage(lang) {
        state.lang = lang;
        localStorage.setItem("pf.lang", lang);
        translatePage();
    }

    function setupTypeSelects() {
        const selects = ["categoryType", "recordType", "typeTotalType"];

        selects.forEach(function (id) {
            const select = $(id);
            if (!select) return;

            const oldValue = select.value || "expense";
            select.innerHTML =
                '<option value="income">' + text("income") + "</option>" +
                '<option value="expense">' + text("expense") + "</option>";
            select.value = oldValue;
        });
    }

    function showLoginForm() {
        $("loginForm").classList.remove("hidden");
        $("registerForm").classList.add("hidden");
        $("loginTab").classList.add("active");
        $("registerTab").classList.remove("active");
    }

    function showRegisterForm() {
        $("loginForm").classList.add("hidden");
        $("registerForm").classList.remove("hidden");
        $("loginTab").classList.remove("active");
        $("registerTab").classList.add("active");
    }

    function showAuthView() {
        $("authView").classList.remove("hidden");
        $("appView").classList.add("hidden");

        $("authView").style.display = "flex";
        $("appView").style.display = "none";
    }

    function showAppView() {
        $("authView").classList.add("hidden");
        $("appView").classList.remove("hidden");

        $("authView").style.display = "none";
        $("appView").style.display = "grid";

        renderCurrentUser();
    }

    function renderCurrentUser() {
        const user = state.user || {};
        const username = user.username || "-";
        const role = String(user.role || "USER").toUpperCase();

        if ($("currentUsername")) $("currentUsername").textContent = username;
        if ($("currentRole")) $("currentRole").textContent = role;
        if ($("profileUsername")) $("profileUsername").textContent = username;
        if ($("profileRole")) $("profileRole").textContent = role;

        if ($("navAdmin")) {
            if (role === "ADMIN") {
                $("navAdmin").classList.remove("hidden");
            } else {
                $("navAdmin").classList.add("hidden");

                if (state.activeTab === "admin") {
                    switchTab("home");
                }
            }
        }
    }

    async function handleLogin(event) {
        event.preventDefault();

        const username = $("loginUsername").value.trim();
        const password = $("loginPassword").value;

        if (!username || !password) {
            toast("请输入用户名和密码");
            return;
        }

        try {
            const result = await api("/login", {
                method: "POST",
                auth: false,
                body: {
                    username: username,
                    password: password
                }
            });

            const data = getResultData(result);
            let token = "";

            if (typeof data === "string") {
                token = data;
            } else if (data) {
                token = data.token || data.authorization || data.Authorization || "";
            }

            if (!token && result) {
                token = result.token || "";
            }

            if (!token) {
                throw new Error("登录成功，但后端没有返回 token");
            }

            state.token = token;
            state.user = {
                username: username,
                role: "USER"
            };

            saveAuth();
            showAppView();

            await loadMe().catch(function () {
            });
            await switchTab("home");

            toast("登录成功");
        } catch (error) {
            toast(error.message);
        }
    }

    async function handleRegister(event) {
        event.preventDefault();

        const username = $("registerUsername").value.trim();
        const password = $("registerPassword").value;

        if (!username || !password) {
            toast("请输入用户名和密码");
            return;
        }

        try {
            const result = await api("/users", {
                method: "POST",
                auth: false,
                body: {
                    username: username,
                    password: password
                }
            });

            toast(result.message || "注册成功，请登录");

            $("loginUsername").value = username;
            $("loginPassword").value = "";
            $("registerPassword").value = "";
            showLoginForm();
        } catch (error) {
            toast(error.message);
        }
    }

    async function loadMe() {
        const result = await api("/me");
        const data = getResultData(result);

        if (data) {
            state.user = Object.assign({}, state.user || {}, data);
            saveAuth();
            renderCurrentUser();
        }
    }

    async function changeMyPassword(event) {
        event.preventDefault();

        const oldPassword = $("meOldPassword").value;
        const newPassword = $("meNewPassword").value;
        const confirmPassword = $("meConfirmPassword").value;

        if (!oldPassword || !newPassword || !confirmPassword) {
            toast("当前密码、新密码和确认新密码都必须填写");
            return;
        }

        if (newPassword !== confirmPassword) {
            toast("两次输入的新密码不一致");
            return;
        }

        try {
            const result = await api("/me/password", {
                method: "PUT",
                body: {
                    oldPassword: oldPassword,
                    newPassword: newPassword
                }
            });

            toast(result.message || "密码修改成功");

            $("meOldPassword").value = "";
            $("meNewPassword").value = "";
            $("meConfirmPassword").value = "";
        } catch (error) {
            toast(error.message);
        }
    }

    async function logout() {
        try {
            await api("/logout", {method: "POST"});
        } catch (e) {
            // 后端退出失败时，前端仍然清除登录状态
        }

        state.token = "";
        state.user = null;
        state.adminDetail = null;
        saveAuth();
        showAuthView();
        toast("已退出登录");
    }

    async function switchTab(tab) {
        state.activeTab = tab;

        document.querySelectorAll(".nav-btn").forEach(function (btn) {
            btn.classList.toggle("active", btn.dataset.tab === tab);
        });

        document.querySelectorAll(".view-panel").forEach(function (panel) {
            panel.classList.toggle("active", panel.id === tab + "Panel");
        });

        try {
            if (tab === "home") {
                await loadHome();
            } else if (tab === "profile") {
                renderCurrentUser();
            } else if (tab === "categories") {
                await loadCategories();
            } else if (tab === "records") {
                await loadCategories();
                await loadRecords();
            } else if (tab === "stats") {
                await loadStats();
            } else if (tab === "admin") {
                await loadAdminUsers();
            }
        } catch (error) {
            toast(error.message);
        }
    }

    function renderRows(tbodyId, rows, colspan) {
        const tbody = $(tbodyId);
        if (!tbody) return;

        if (!rows || rows.length === 0) {
            tbody.innerHTML = '<tr><td colspan="' + colspan + '" class="empty">' + text("noData") + "</td></tr>";
            return;
        }

        tbody.innerHTML = rows.join("");
    }

    function renderType(type) {
        if (type === "income") return text("income");
        if (type === "expense") return text("expense");
        return escapeHtml(type || "");
    }

    function getCategoryName(record) {
        if (record.categoryName) return record.categoryName;

        const categoryId = record.categoryId;
        const category = state.categories.find(function (item) {
            return String(item.id) === String(categoryId);
        });

        return category ? (category.name || category.categoryName || "-") : "-";
    }

    async function loadHome() {
        const year = Number(state.homeYear || currentYear());
        const month = Number(state.homeMonth || currentMonth());

        const statsResult = await api("/transaction-stats/month?year=" + year + "&month=" + month);
        const stats = getResultData(statsResult) || {};

        $("homeIncome").textContent = money(stats.incomeTotal || stats.income || stats.totalIncome);
        $("homeExpense").textContent = money(stats.expenseTotal || stats.expense || stats.totalExpense);
        $("homeBalance").textContent = money(stats.balance);

        await loadCategories();

        const startDate = year + "-" + String(month).padStart(2, "0") + "-01";
        const endDate = new Date(year, month, 0).toISOString().slice(0, 10);

        const params = new URLSearchParams();
        params.set("page", 1);
        params.set("size", 5);
        params.set("startRecordDate", startDate);
        params.set("endOfRecordDate", endDate);

        const recentResult = await api("/transaction-records/search?" + params.toString());
        const recentData = getResultData(recentResult) || {};
        const recentList = Array.isArray(recentData.list) ? recentData.list : [];

        const recentRows = recentList.map(function (record) {
            return "<tr>" +
                "<td>" + escapeHtml(record.id) + "</td>" +
                "<td>" + escapeHtml(getCategoryName(record)) + "</td>" +
                "<td>" + renderType(record.type) + "</td>" +
                "<td>" + money(record.amount) + "</td>" +
                "<td>" + escapeHtml(record.recordDate || "") + "</td>" +
                "</tr>";
        });

        renderRows("recentRecordsBody", recentRows, 5);

        await renderCategoryStats("income", year, month, "homeIncomeStats");
        await renderCategoryStats("expense", year, month, "homeExpenseStats");
    }

    async function handleHomeMonthQuery(event) {
        event.preventDefault();

        const year = Number($("homeYear").value);
        const month = Number($("homeMonth").value);

        if (!year || year <= 0) {
            toast("年份必须大于0");
            return;
        }

        if (!month || month < 1 || month > 12) {
            toast("月份必须是1到12");
            return;
        }

        state.homeYear = year;
        state.homeMonth = month;

        await loadHome();
    }

    async function loadCategories() {
        const result = await api("/transaction-categories");
        const data = getResultData(result);

        state.categories = Array.isArray(data) ? data : [];

        renderCategoryTable();
        fillRecordCategoryOptions();
        fillFilterCategoryOptions();
    }

    function renderCategoryTable() {
        const rows = state.categories.map(function (item) {
            const name = item.name || item.categoryName || "";

            return "<tr>" +
                "<td>" + escapeHtml(item.id) + "</td>" +
                "<td>" + escapeHtml(name) + "</td>" +
                "<td>" + renderType(item.type) + "</td>" +
                "<td class=\"actions\">" +
                "<button type=\"button\" data-action=\"edit-category\" data-id=\"" + escapeHtml(item.id) + "\">" + text("edit") + "</button>" +
                "<button type=\"button\" class=\"danger\" data-action=\"delete-category\" data-id=\"" + escapeHtml(item.id) + "\">" + text("remove") + "</button>" +
                "</td>" +
                "</tr>";
        });

        renderRows("categoriesBody", rows, 4);
    }

    function fillRecordCategoryOptions() {
        const select = $("recordCategory");
        if (!select) return;

        const currentType = $("recordType") ? $("recordType").value || "expense" : "expense";

        const filtered = state.categories.filter(function (item) {
            return !item.type || item.type === currentType;
        });

        if (filtered.length === 0) {
            select.innerHTML = '<option value="">' + text("noData") + "</option>";
            return;
        }

        select.innerHTML = filtered.map(function (item) {
            const name = item.name || item.categoryName || "";
            return '<option value="' + escapeHtml(item.id) + '">' + escapeHtml(name) + "</option>";
        }).join("");
    }

    function fillFilterCategoryOptions() {
        const select = $("filterCategory");
        if (!select) return;

        const oldValue = select.value;

        const options = ['<option value="">全部分类</option>'];

        state.categories.forEach(function (item) {
            const name = item.name || item.categoryName || "";
            options.push(
                '<option value="' + escapeHtml(item.id) + '">' +
                escapeHtml(name) +
                " / " +
                renderType(item.type) +
                "</option>"
            );
        });

        select.innerHTML = options.join("");
        select.value = oldValue;
    }

    async function saveCategory(event) {
        event.preventDefault();

        const id = $("categoryId").value;
        const name = $("categoryName").value.trim();
        const type = $("categoryType").value;

        if (!name) {
            toast("请输入分类名");
            return;
        }

        const body = {
            name: name,
            type: type
        };

        try {
            const result = id
                ? await api("/transaction-categories/" + encodeURIComponent(id), {
                    method: "PUT",
                    body: body
                })
                : await api("/transaction-categories", {
                    method: "POST",
                    body: body
                });

            toast(result.message || "保存成功");
            clearCategoryForm();
            await loadCategories();
        } catch (error) {
            toast(error.message);
        }
    }

    function clearCategoryForm() {
        $("categoryId").value = "";
        $("categoryName").value = "";
        $("categoryType").value = "expense";
    }

    async function loadRecords(renderTable) {
        const params = new URLSearchParams();

        params.set("page", state.recordPage);
        params.set("size", state.recordSize);

        if (state.recordFilter.type) {
            params.set("type", state.recordFilter.type);
        }

        if (state.recordFilter.categoryId) {
            params.set("categoryId", state.recordFilter.categoryId);
        }

        if (state.recordFilter.startRecordDate && state.recordFilter.endOfRecordDate) {
            params.set("startRecordDate", state.recordFilter.startRecordDate);
            params.set("endOfRecordDate", state.recordFilter.endOfRecordDate);
        }

        const result = await api("/transaction-records/search?" + params.toString());
        const data = getResultData(result) || {};

        state.records = Array.isArray(data.list) ? data.list : [];
        state.recordTotal = Number(data.total || 0);
        state.recordPage = Number(data.page || state.recordPage);
        state.recordSize = Number(data.size || state.recordSize);

        if (renderTable !== false) {
            renderRecordTable(state.records);
            renderRecordPagination();
        }
    }

    function renderRecordTable(records) {
        const rows = records.map(function (record) {
            return "<tr>" +
                "<td>" + escapeHtml(record.id) + "</td>" +
                "<td>" + escapeHtml(getCategoryName(record)) + "</td>" +
                "<td>" + renderType(record.type) + "</td>" +
                "<td>" + money(record.amount) + "</td>" +
                "<td>" + escapeHtml(record.remark || "") + "</td>" +
                "<td>" + escapeHtml(record.recordDate || "") + "</td>" +
                "<td class=\"actions\">" +
                "<button type=\"button\" data-action=\"edit-record\" data-id=\"" + escapeHtml(record.id) + "\">" + text("edit") + "</button>" +
                "<button type=\"button\" class=\"danger\" data-action=\"delete-record\" data-id=\"" + escapeHtml(record.id) + "\">" + text("remove") + "</button>" +
                "</td>" +
                "</tr>";
        });

        renderRows("recordsBody", rows, 7);
    }

    function renderRecordPagination() {
        const box = $("recordPagination");
        if (!box) return;

        const totalPage = Math.max(1, Math.ceil(state.recordTotal / state.recordSize));

        box.innerHTML =
            '<button type="button" id="recordPrevPage" ' + (state.recordPage <= 1 ? "disabled" : "") + ">" +
            text("prevPage") +
            "</button>" +
            '<span class="pagination-info">' +
            state.recordPage +
            " / " +
            totalPage +
            " " +
            text("pageUnit") +
            "，" +
            text("totalPrefix") +
            " " +
            state.recordTotal +
            " " +
            text("totalSuffix") +
            "</span>" +
            '<button type="button" id="recordNextPage" ' + (state.recordPage >= totalPage ? "disabled" : "") + ">" +
            text("nextPage") +
            "</button>";

        $("recordPrevPage").addEventListener("click", async function () {
            if (state.recordPage <= 1) return;
            state.recordPage--;
            await loadRecords();
        });

        $("recordNextPage").addEventListener("click", async function () {
            if (state.recordPage >= totalPage) return;
            state.recordPage++;
            await loadRecords();
        });
    }

    async function saveRecord(event) {
        event.preventDefault();

        const id = $("recordId").value;
        const type = $("recordType").value;
        const categoryId = $("recordCategory").value;
        const amount = $("recordAmount").value;
        const recordDate = $("recordDate").value;
        const remark = $("recordRemark").value.trim();

        if (!categoryId) {
            toast("请选择分类");
            return;
        }

        if (!amount) {
            toast("请输入金额");
            return;
        }

        if (!recordDate) {
            toast("请选择日期");
            return;
        }

        const body = {
            categoryId: Number(categoryId),
            type: type,
            amount: Number(amount),
            recordDate: recordDate,
            remark: remark
        };

        try {
            const result = id
                ? await api("/transaction-records/" + encodeURIComponent(id), {
                    method: "PUT",
                    body: body
                })
                : await api("/transaction-records", {
                    method: "POST",
                    body: body
                });

            toast(result.message || "保存成功");
            clearRecordForm();
            state.recordPage = 1;
            await loadRecords();
        } catch (error) {
            toast(error.message);
        }
    }

    function clearRecordForm() {
        $("recordId").value = "";
        $("recordType").value = "expense";
        $("recordAmount").value = "";
        $("recordRemark").value = "";
        $("recordDate").value = today();
        fillRecordCategoryOptions();
    }

    async function filterRecordsByType(type) {
        state.recordFilter.type = type;
        state.recordPage = 1;
        await loadRecords();
    }

    async function filterRecordsByRange(event) {
        event.preventDefault();

        const start = $("startRecordDate").value;
        const end = $("endOfRecordDate").value;
        const categoryId = $("filterCategory") ? $("filterCategory").value : "";

        if ((start && !end) || (!start && end)) {
            toast("开始日期和结束日期必须同时填写");
            return;
        }

        if (start && end && start > end) {
            toast("开始日期不能晚于结束日期");
            return;
        }

        state.recordFilter.startRecordDate = start;
        state.recordFilter.endOfRecordDate = end;
        state.recordFilter.categoryId = categoryId;

        if (categoryId) {
            const selectedCategory = state.categories.find(function (item) {
                return String(item.id) === String(categoryId);
            });

            if (selectedCategory) {
                state.recordFilter.type = selectedCategory.type || "";
            }
        }

        state.recordPage = 1;
        await loadRecords();
    }

    async function loadStats(event) {
        if (event) event.preventDefault();

        const year = Number($("statsYear").value || currentYear());
        const month = Number($("statsMonth").value || currentMonth());

        try {
            const result = await api("/transaction-stats/month?year=" + year + "&month=" + month);
            const data = getResultData(result) || {};

            $("statsIncome").textContent = money(data.incomeTotal || data.income || data.totalIncome);
            $("statsExpense").textContent = money(data.expenseTotal || data.expense || data.totalExpense);
            $("statsBalance").textContent = money(data.balance);

            await renderCategoryStats("income", year, month, "incomeCategoryStats");
            await renderCategoryStats("expense", year, month, "expenseCategoryStats");
        } catch (error) {
            toast(error.message);
        }
    }

    function getChartColor(index) {
        const colors = [
            "#0f8277",
            "#38a169",
            "#3182ce",
            "#d69e2e",
            "#e53e3e",
            "#805ad5",
            "#dd6b20",
            "#319795"
        ];

        return colors[index % colors.length];
    }

    function drawPieChart(canvas, list) {
        if (!canvas || !list || list.length === 0) return;

        const ctx = canvas.getContext("2d");
        const width = canvas.width;
        const height = canvas.height;
        const centerX = width / 2;
        const centerY = height / 2;
        const radius = Math.min(width, height) / 2 - 12;

        ctx.clearRect(0, 0, width, height);

        let total = 0;

        list.forEach(function (item) {
            total += Number(item.amount || 0);
        });

        if (total <= 0) return;

        let startAngle = -Math.PI / 2;

        list.forEach(function (item, index) {
            const value = Number(item.amount || 0);
            const angle = (value / total) * Math.PI * 2;
            const endAngle = startAngle + angle;

            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.arc(centerX, centerY, radius, startAngle, endAngle);
            ctx.closePath();
            ctx.fillStyle = getChartColor(index);
            ctx.fill();

            startAngle = endAngle;
        });

        ctx.beginPath();
        ctx.arc(centerX, centerY, radius * 0.55, 0, Math.PI * 2);
        ctx.fillStyle = "#ffffff";
        ctx.fill();

        ctx.fillStyle = "#10242a";
        ctx.font = "bold 16px Arial";
        ctx.textAlign = "center";
        ctx.textBaseline = "middle";
        ctx.fillText(money(total), centerX, centerY);
    }

    async function renderCategoryStats(type, year, month, targetId) {
        const target = $(targetId);
        if (!target) return;

        try {
            const result = await api("/transaction-stats/category/" + encodeURIComponent(type) + "?year=" + year + "&month=" + month);
            const data = getResultData(result);
            const rawList = Array.isArray(data) ? data : [];

            const list = rawList.map(function (item) {
                return {
                    name: item.categoryName || item.name || item.category || "-",
                    amount: Number(item.totalAmount || item.amount || item.total || 0)
                };
            }).filter(function (item) {
                return item.amount > 0;
            });

            if (list.length === 0) {
                target.innerHTML = '<div class="empty">' + text("noData") + "</div>";
                return;
            }

            const canvasId = targetId + "Chart";

            target.innerHTML =
                '<div class="chart-box">' +
                '<canvas id="' + canvasId + '" width="220" height="220"></canvas>' +
                '<div class="chart-legend">' +
                list.map(function (item, index) {
                    return '<div class="chart-legend-item">' +
                        '<span class="chart-dot" style="background:' + getChartColor(index) + '"></span>' +
                        '<span>' + escapeHtml(item.name) + '</span>' +
                        '<strong>' + money(item.amount) + '</strong>' +
                        '</div>';
                }).join("") +
                '</div>' +
                '</div>';

            drawPieChart($(canvasId), list);
        } catch (error) {
            target.innerHTML = '<div class="empty">' + escapeHtml(error.message) + '</div>';
        }
    }

    async function loadTypeTotal(event) {
        event.preventDefault();

        const type = $("typeTotalType").value;
        const year = Number($("typeTotalYear").value || currentYear());
        const month = Number($("typeTotalMonth").value || currentMonth());

        try {
            const result = await api("/transaction-stats/type-total?type=" + encodeURIComponent(type) + "&year=" + year + "&month=" + month);
            const data = getResultData(result);

            $("typeTotalAmount").textContent = money(data);
        } catch (error) {
            toast(error.message);
        }
    }

    async function loadAdminUsers() {
        const result = await api("/admin/users");
        const data = getResultData(result);
        const users = Array.isArray(data) ? data : [];

        renderAdminUsers(users);
    }

    function renderAdminUsers(users) {
        const rows = users.map(function (user) {
            return "<tr>" +
                "<td>" + escapeHtml(user.username) + "</td>" +
                "<td>" + escapeHtml(user.role || "") + "</td>" +
                "<td class=\"actions\">" +
                "<button type=\"button\" data-action=\"admin-detail\" data-username=\"" + escapeHtml(user.username) + "\">" + text("detail") + "</button>" +
                "<button type=\"button\" data-action=\"admin-reset\" data-username=\"" + escapeHtml(user.username) + "\">" + text("resetPassword") + "</button>" +
                "<button type=\"button\" class=\"danger\" data-action=\"admin-delete\" data-username=\"" + escapeHtml(user.username) + "\">" + text("remove") + "</button>" +
                "</td>" +
                "</tr>";
        });

        renderRows("adminUsersBody", rows, 3);
    }

    function renderAdminDetail(user) {
        state.adminDetail = user;

        const box = $("adminDetail");
        if (!box) return;

        if (!user) {
            box.textContent = text("selectUserHint");
            return;
        }

        box.innerHTML =
            '<div class="detail-row">' +
            '<span>' + text("username") + '</span>' +
            '<strong>' + escapeHtml(user.username || "-") + '</strong>' +
            '</div>' +
            '<div class="detail-row">' +
            '<span>' + text("role") + '</span>' +
            '<strong>' + escapeHtml(user.role || "-") + '</strong>' +
            '</div>';
    }

    async function searchAdminUser(event) {
        event.preventDefault();

        const username = $("adminSearchUsername").value.trim();

        if (!username) {
            toast("请输入用户名");
            return;
        }

        try {
            const result = await api("/admin/users/" + encodeURIComponent(username));
            const data = getResultData(result);

            renderAdminDetail(data);
            renderAdminUsers(data ? [data] : []);
        } catch (error) {
            toast(error.message);
        }
    }

    async function resetAdminPassword(event) {
        event.preventDefault();

        const username = $("adminResetUsername").value.trim();
        const password = $("adminResetPassword").value;

        if (!username || !password) {
            toast("请输入用户名和新密码");
            return;
        }

        try {
            const result = await api("/admin/users/" + encodeURIComponent(username) + "/password", {
                method: "PUT",
                body: {
                    newPassword: password
                }
            });

            toast(result.message || "密码已重置");
            $("adminResetPassword").value = "";
        } catch (error) {
            toast(error.message);
        }
    }

    async function handleActionClick(event) {
        const button = event.target.closest("[data-action]");
        if (!button) return;

        const action = button.dataset.action;
        const id = button.dataset.id;
        const username = button.dataset.username;

        try {
            if (action === "edit-category") {
                const category = state.categories.find(function (item) {
                    return String(item.id) === String(id);
                });

                if (!category) return;

                $("categoryId").value = category.id;
                $("categoryName").value = category.name || category.categoryName || "";
                $("categoryType").value = category.type || "expense";

                await switchTab("categories");
            }

            if (action === "delete-category") {
                if (!confirm("确定删除这个分类吗？")) return;

                const result = await api("/transaction-categories/" + encodeURIComponent(id), {
                    method: "DELETE"
                });

                toast(result.message || "删除成功");
                await loadCategories();
            }

            if (action === "edit-record") {
                const record = state.records.find(function (item) {
                    return String(item.id) === String(id);
                });

                if (!record) return;

                $("recordId").value = record.id;
                $("recordType").value = record.type || "expense";

                fillRecordCategoryOptions();

                if (record.categoryId) {
                    $("recordCategory").value = record.categoryId;
                } else if (record.categoryName) {
                    const matchedCategory = state.categories.find(function (item) {
                        return item.name === record.categoryName && item.type === record.type;
                    });

                    if (matchedCategory) {
                        $("recordCategory").value = matchedCategory.id;
                    }
                }

                $("recordAmount").value = record.amount || "";
                $("recordRemark").value = record.remark || "";
                $("recordDate").value = record.recordDate || today();

                await switchTab("records");
            }

            if (action === "delete-record") {
                if (!confirm("确定删除这条记录吗？")) return;

                const result = await api("/transaction-records/" + encodeURIComponent(id), {
                    method: "DELETE"
                });

                toast(result.message || "删除成功");

                if (state.records.length === 1 && state.recordPage > 1) {
                    state.recordPage--;
                }

                await loadRecords();
            }

            if (action === "admin-detail") {
                const result = await api("/admin/users/" + encodeURIComponent(username));
                const data = getResultData(result);
                renderAdminDetail(data);
            }

            if (action === "admin-reset") {
                $("adminResetUsername").value = username;
                $("adminResetPassword").focus();
            }

            if (action === "admin-delete") {
                if (!confirm("确定删除用户 " + username + " 吗？")) return;

                const result = await api("/admin/users/" + encodeURIComponent(username), {
                    method: "DELETE"
                });

                toast(result.message || "删除成功");
                await loadAdminUsers();

                if (state.adminDetail && state.adminDetail.username === username) {
                    renderAdminDetail(null);
                }
            }
        } catch (error) {
            toast(error.message);
        }
    }

    function bindEvents() {
        document.querySelectorAll(".lang-btn").forEach(function (btn) {
            btn.addEventListener("click", function () {
                setLanguage(btn.dataset.lang);
            });
        });

        $("loginTab").addEventListener("click", showLoginForm);
        $("registerTab").addEventListener("click", showRegisterForm);
        $("goRegisterBtn").addEventListener("click", showRegisterForm);
        $("goLoginBtn").addEventListener("click", showLoginForm);

        $("loginForm").addEventListener("submit", handleLogin);
        $("registerForm").addEventListener("submit", handleRegister);

        $("logoutBtn").addEventListener("click", logout);
        $("changePasswordForm").addEventListener("submit", changeMyPassword);

        $("refreshBtn").addEventListener("click", function () {
            switchTab(state.activeTab);
        });

        document.querySelectorAll(".nav-btn").forEach(function (btn) {
            btn.addEventListener("click", function () {
                switchTab(btn.dataset.tab);
            });
        });

        $("categoryForm").addEventListener("submit", saveCategory);
        $("clearCategoryBtn").addEventListener("click", clearCategoryForm);

        $("recordForm").addEventListener("submit", saveRecord);
        $("clearRecordBtn").addEventListener("click", clearRecordForm);

        $("recordType").addEventListener("change", fillRecordCategoryOptions);

        $("loadAllRecordsBtn").addEventListener("click", function () {
            state.recordFilter.type = "";
            state.recordFilter.categoryId = "";
            state.recordFilter.startRecordDate = "";
            state.recordFilter.endOfRecordDate = "";

            if ($("filterCategory")) $("filterCategory").value = "";
            if ($("startRecordDate")) $("startRecordDate").value = "";
            if ($("endOfRecordDate")) $("endOfRecordDate").value = "";

            state.recordPage = 1;
            loadRecords();
        });

        $("filterIncomeBtn").addEventListener("click", function () {
            state.recordFilter.type = "income";
            state.recordPage = 1;
            loadRecords();
        });

        $("filterExpenseBtn").addEventListener("click", function () {
            state.recordFilter.type = "expense";
            state.recordPage = 1;
            loadRecords();
        });

        $("rangeFilterForm").addEventListener("submit", filterRecordsByRange);

        $("homeMonthForm").addEventListener("submit", handleHomeMonthQuery);

        $("monthlyStatsForm").addEventListener("submit", loadStats);
        $("typeTotalForm").addEventListener("submit", loadTypeTotal);

        $("adminSearchForm").addEventListener("submit", searchAdminUser);
        $("adminLoadAllBtn").addEventListener("click", loadAdminUsers);
        $("adminResetForm").addEventListener("submit", resetAdminPassword);

        document.addEventListener("click", handleActionClick);
    }

    async function init() {
        bindEvents();

        setupTypeSelects();
        translatePage();

        $("recordDate").value = today();
        $("startRecordDate").value = today();
        $("endOfRecordDate").value = today();

        $("homeYear").value = currentYear();
        $("homeMonth").value = currentMonth();

        $("statsYear").value = currentYear();
        $("statsMonth").value = currentMonth();
        $("typeTotalYear").value = currentYear();
        $("typeTotalMonth").value = currentMonth();

        if (!state.token) {
            showAuthView();
            return;
        }

        showAppView();

        try {
            await loadMe();
            await switchTab("home");
        } catch (error) {
            state.token = "";
            state.user = null;
            saveAuth();
            showAuthView();
            toast("登录已失效，请重新登录");
        }
    }

    document.addEventListener("DOMContentLoaded", init);
})();