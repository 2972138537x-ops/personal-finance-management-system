(function () {
    // 页面全局状态：保存登录信息、当前语言、分类、记录、分页和筛选条件
    // 画面全体の状態：ログイン情報、現在言語、カテゴリ、記録、ページング、絞り込み条件を保持する
    const state = {
        token: localStorage.getItem("pf.token") || "",
        user: readJson(localStorage.getItem("pf.user")) || null,
        lang: localStorage.getItem("pf.lang") || "zh",
        activeTab: "home",
        categories: [],
        records: [],
        homeRecentRecords: [],
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
        adminDetail: null,
        appearance: readJson(localStorage.getItem("pf.appearance")) || {
            theme: "light",
            incomeColor: "#0f8277",
            expenseColor: "#b42318",
            primaryColor: "#0f8277"
        }
    };

    const i18n = {
        zh: {
            appTitle: "个人财务管理系统",
            appSubTitle: "Spring Boot + MyBatis + MySQL",
            login: "登录",
            register: "注册",
            loginHint: "登录后即可管理收入、支出和统计数据。",
            registerHint: "创建账号后即可开始记录收支。",
            username: "用户名",
            password: "密码",
            showPassword: "显示密码",
            hidePassword: "隐藏密码",
            noAccount: "还没有账号？",
            goRegister: "创建账号",
            hasAccount: "已有账号？",
            goLogin: "返回登录",
            loggedIn: "已登录",
            role: "角色",
            refresh: "刷新",
            logout: "退出登录",
            home: "首页",
            homeHint: "查看本月收入、支出、结余和最近记录。",
            monthFilter: "月份筛选",
            profile: "个人中心",
            profileNav: "我的",
            profileHint: "管理账号信息、账号安全和个性化显示设置。",
            accountInfo: "账号信息",
            categories: "分类管理",
            categoriesHint: "管理常用的收入和支出分类。",
            records: "收支记录",
            recordsHint: "记录每一笔收入和支出，支持筛选、编辑和删除。",
            stats: "统计",
            statsHint: "按月份查看收支趋势和分类占比。",
            aiAssistant: "AI助手",
            aiAssistantHint: "向 AI 提问，获取记账建议、消费分析和节省建议。",
            aiQuestion: "你的问题",
            aiQuestionPlaceholder: "例如：请给我一些控制生活支出的建议",
            aiSend: "发送问题",
            aiAnswer: "AI 回答",
            aiDefaultAnswer: "在上方输入问题后，点击发送即可获得 AI 建议。",
            admin: "管理员",
            adminHint: "查询用户、查看详情、重置密码、删除用户。",
            incomeTotal: "收入合计",
            expenseTotal: "支出合计",
            balance: "结余",
            recentRecords: "最近记录",
            incomeCategoryStats: "收入分类统计",
            expenseCategoryStats: "支出分类统计",
            categoryForm: "添加 / 编辑分类",
            myCategories: "分类列表",
            recordForm: "添加 / 编辑记录",
            myRecords: "收支明细",
            filter: "筛选",
            allRecords: "全部类型",
            allCategories: "全部分类",
            incomeOnly: "只看收入",
            expenseOnly: "只看支出",
            startDate: "开始日期",
            endDate: "结束日期",
            queryByDate: "按日期筛选",
            monthlyStats: "月度统计",
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
            clear: "清空输入",
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
            selectUserHint: "点击“详情”后显示用户信息。",
            editModeCategory: "正在编辑分类，请在上方表单修改后保存。",
            editModeRecord: "正在编辑记录，请在上方表单修改后保存。",
            recordsUpdated: "记录列表已更新",
            refreshed: "当前页面已刷新",
            displaySettings: "显示与个性化",
            settingsHint: "设置主题模式、金额颜色和界面主题色。",
            theme: "主题模式",
            themeLight: "浅色模式",
            themeDark: "夜间模式",
            themeSystem: "跟随系统",
            incomeColor: "收入金额颜色",
            expenseColor: "支出金额颜色",
            primaryColor: "主题颜色",
            resetAppearance: "恢复默认个性化设置",
            appearanceSaved: "个性化设置已保存",
            accountSecurity: "账号安全",
            deleteAccount: "注销账号",
            deleteAccountHint: "注销账号会删除当前账号及相关数据，请谨慎操作。",
            deletePassword: "当前密码",
            deleteConfirmPassword: "再次输入当前密码",
            deletePasswordRequired: "注销账号前，请输入两遍当前密码",
            deletePasswordMismatch: "两次输入的密码不一致",
            deleteAccountConfirm1: "确定要注销当前账号 {username} 吗？这个操作不可恢复。",
            deleteAccountConfirmInput: "为了确认注销，请输入当前用户名：",
            deleteAccountCancelled: "用户名不一致，已取消注销",
            deleteAccountSuccess: "账号已注销",
            deleteAccountBackendMissing: "注销账号失败，请确认后端 DELETE /me 接口是否正常。",
            loginRequired: "登录已失效，请重新登录",
            requestFailed: "请求失败",
            usernamePasswordRequired: "请输入用户名和密码",
            loginSuccessNoToken: "登录成功，但后端没有返回 token",
            loginSuccess: "登录成功",
            registerSuccess: "注册成功，请登录",
            changePasswordRequired: "当前密码、新密码和确认新密码都必须填写",
            newPasswordMismatch: "两次输入的新密码不一致",
            changePasswordSuccess: "密码修改成功",
            yearInvalid: "年份必须大于0",
            monthInvalid: "月份必须是1到12",
            categoryNameRequired: "请输入分类名",
            saveSuccess: "保存成功",
            categoryRequired: "请选择分类",
            amountRequired: "请输入金额",
            dateRequired: "请选择日期",
            dateRangeRequired: "开始日期和结束日期必须同时填写",
            startDateAfterEndDate: "开始日期不能晚于结束日期",
            questionRequired: "请输入问题",
            sending: "发送中...",
            aiThinking: "AI 正在思考中，请稍等...",
            aiEmpty: "AI没有返回内容。",
            aiSuccess: "AI回复成功",
            usernameRequired: "请输入用户名",
            usernameNewPasswordRequired: "请输入用户名和新密码",
            resetPasswordSuccess: "密码已重置",
            confirmDeleteCategory: "确定删除这个分类吗？",
            confirmDeleteRecord: "确定删除这条记录吗？",
            confirmDeleteUser: "确定删除用户 {username} 吗？",
            deleteSuccess: "删除成功",
            confirmPasswordLength: "确认密码长度必须是6到12位",
            passwordLength: "密码长度必须是6到12位",
            oldPasswordWrong: "当前密码错误",
            passwordMismatchBackend: "两次密码不一致",
            accountDeleted: "账号已注销"
        },
        ja: {
            appTitle: "個人向け家計管理システム",
            appSubTitle: "Spring Boot + MyBatis + MySQL",
            login: "ログイン",
            register: "登録",
            loginHint: "ログインすると、収入・支出・統計データを管理できます。",
            registerHint: "アカウントを作成して、収支記録を始めます。",
            username: "ユーザー名",
            password: "パスワード",
            showPassword: "パスワードを表示",
            hidePassword: "パスワードを非表示",
            noAccount: "アカウントがありませんか？",
            goRegister: "アカウント作成",
            hasAccount: "すでにアカウントがありますか？",
            goLogin: "ログインに戻る",
            loggedIn: "ログイン済み",
            role: "ロール",
            refresh: "更新",
            logout: "ログアウト",
            home: "ホーム",
            homeHint: "指定した月の収入、支出、残高、最近の記録を表示します。",
            monthFilter: "年月で絞り込み",
            profile: "マイページ",
            profileNav: "マイページ",
            profileHint: "アカウント情報、セキュリティ、表示のカスタマイズを管理します。",
            accountInfo: "アカウント情報",
            categories: "カテゴリ管理",
            categoriesHint: "よく使う収入・支出カテゴリを管理します。",
            records: "収支記録",
            recordsHint: "収入・支出を記録し、絞り込み、編集、削除ができます。",
            stats: "統計",
            statsHint: "月別の収支状況とカテゴリ別の割合を確認できます。",
            aiAssistant: "AIアシスタント",
            aiAssistantHint: "AIに質問して、記録の付け方、支出分析、節約のアドバイスを受け取れます。",
            aiQuestion: "質問内容",
            aiQuestionPlaceholder: "例：生活費を抑えるためのアドバイスをください",
            aiSend: "質問を送信",
            aiAnswer: "AIの回答",
            aiDefaultAnswer: "上に質問を入力して送信すると、AIのアドバイスが表示されます。",
            admin: "管理者",
            adminHint: "ユーザー検索、詳細確認、パスワードリセット、削除を行います。",
            incomeTotal: "収入合計",
            expenseTotal: "支出合計",
            balance: "残高",
            recentRecords: "最近の記録",
            incomeCategoryStats: "収入カテゴリ統計",
            expenseCategoryStats: "支出カテゴリ統計",
            categoryForm: "カテゴリを追加 / 編集",
            myCategories: "カテゴリ一覧",
            recordForm: "記録を追加 / 編集",
            myRecords: "収支明細",
            filter: "絞り込み",
            allRecords: "すべての種別",
            allCategories: "すべてのカテゴリ",
            incomeOnly: "収入のみ",
            expenseOnly: "支出のみ",
            startDate: "開始日",
            endDate: "終了日",
            queryByDate: "日付で絞り込む",
            monthlyStats: "月次統計",
            userManagement: "ユーザー管理",
            userDetail: "ユーザー詳細",
            resetPassword: "パスワードリセット",
            newPassword: "新しいパスワード",
            oldPassword: "現在のパスワード",
            changePassword: "パスワード変更",
            confirmPassword: "新しいパスワード確認",
            queryOneUser: "単一ユーザー検索",
            queryAllUsers: "全ユーザー検索",
            category: "カテゴリ",
            categoryName: "カテゴリ名",
            type: "種別",
            amount: "金額",
            date: "日付",
            remark: "メモ",
            actions: "操作",
            year: "年",
            month: "月",
            query: "検索",
            save: "保存",
            clear: "入力をクリア",
            income: "収入",
            expense: "支出",
            edit: "編集",
            remove: "削除",
            detail: "詳細",
            noData: "データがありません",
            prevPage: "前のページ",
            nextPage: "次のページ",
            pageUnit: "ページ",
            totalPrefix: "全",
            totalSuffix: "件",
            selectUserHint: "「詳細」をクリックするとユーザー情報が表示されます。",
            editModeCategory: "カテゴリを編集中です。上のフォームを修正して保存してください。",
            editModeRecord: "記録を編集中です。上のフォームを修正して保存してください。",
            recordsUpdated: "記録一覧を更新しました",
            refreshed: "現在のページを更新しました",
            displaySettings: "表示とカスタマイズ",
            settingsHint: "テーマモード、金額の色、画面のテーマカラーを設定します。",
            theme: "テーマ",
            themeLight: "ライトモード",
            themeDark: "ダークモード",
            themeSystem: "システムに合わせる",
            incomeColor: "収入金額の色",
            expenseColor: "支出金額の色",
            primaryColor: "テーマカラー",
            resetAppearance: "表示設定を初期化",
            appearanceSaved: "個人設定を保存しました",
            accountSecurity: "アカウントセキュリティ",
            deleteAccount: "アカウント削除",
            deleteAccountHint: "アカウントを削除すると、現在のアカウントと関連データが削除されます。慎重に操作してください。",
            deletePassword: "現在のパスワード",
            deleteConfirmPassword: "現在のパスワードを再入力",
            deletePasswordRequired: "アカウントを削除するには、現在のパスワードを2回入力してください。",
            deletePasswordMismatch: "2回入力したパスワードが一致しません",
            deleteAccountConfirm1: "現在のアカウント {username} を削除しますか？この操作は元に戻せません。",
            deleteAccountConfirmInput: "削除を確認するため、現在のユーザー名を入力してください：",
            deleteAccountCancelled: "ユーザー名が一致しないため、アカウント削除をキャンセルしました。",
            deleteAccountSuccess: "アカウントを削除しました",
            deleteAccountBackendMissing: "アカウント削除に失敗しました。バックエンドの DELETE /me が正常に動作しているか確認してください。",
            loginRequired: "ログインの有効期限が切れました。もう一度ログインしてください",
            requestFailed: "リクエストに失敗しました",
            usernamePasswordRequired: "ユーザー名とパスワードを入力してください",
            loginSuccessNoToken: "ログインは成功しましたが、バックエンドから token が返されませんでした。",
            loginSuccess: "ログインしました",
            registerSuccess: "登録しました。ログインしてください",
            changePasswordRequired: "現在のパスワード、新しいパスワード、確認用パスワードを入力してください。",
            newPasswordMismatch: "新しいパスワードが一致しません",
            changePasswordSuccess: "パスワードを変更しました",
            yearInvalid: "年は0より大きい必要があります",
            monthInvalid: "月は1〜12で入力してください",
            categoryNameRequired: "カテゴリ名を入力してください",
            saveSuccess: "保存しました",
            categoryRequired: "カテゴリを選択してください",
            amountRequired: "金額を入力してください",
            dateRequired: "日付を選択してください",
            dateRangeRequired: "開始日と終了日を両方入力してください。",
            startDateAfterEndDate: "開始日は終了日より後にできません",
            questionRequired: "質問を入力してください",
            sending: "送信中...",
            aiThinking: "AIが回答を作成しています。少々お待ちください...",
            aiEmpty: "AIから内容が返されませんでした。",
            aiSuccess: "AIの回答を取得しました",
            usernameRequired: "ユーザー名を入力してください",
            usernameNewPasswordRequired: "ユーザー名と新しいパスワードを入力してください",
            resetPasswordSuccess: "パスワードをリセットしました",
            confirmDeleteCategory: "このカテゴリを削除しますか？",
            confirmDeleteRecord: "この記録を削除しますか？",
            confirmDeleteUser: "ユーザー {username} を削除しますか？",
            deleteSuccess: "削除しました",
            confirmPasswordLength: "確認用パスワードは6〜12文字で入力してください",
            passwordLength: "パスワードは6〜12文字で入力してください",
            oldPasswordWrong: "現在のパスワードが違います",
            passwordMismatchBackend: "2回入力したパスワードが一致しません",
            accountDeleted: "アカウントを削除しました"
        },
        en: {
            appTitle: "Personal Finance Management System",
            appSubTitle: "Spring Boot + MyBatis + MySQL",
            login: "Login",
            register: "Register",
            loginHint: "Log in to manage your income, expenses, and statistics.",
            registerHint: "Create an account and start recording your finances.",
            username: "Username",
            password: "Password",
            showPassword: "Show password",
            hidePassword: "Hide password",
            noAccount: "Don't have an account?",
            goRegister: "Create Account",
            hasAccount: "Already have an account?",
            goLogin: "Back to Login",
            loggedIn: "Logged in",
            role: "Role",
            refresh: "Refresh",
            logout: "Log out",
            home: "Home",
            homeHint: "View this month’s income, expenses, balance, and recent records.",
            monthFilter: "Filter by Month",
            profile: "My Page",
            profileNav: "Me",
            profileHint: "Manage account information, account security, and display personalization.",
            accountInfo: "Account Information",
            categories: "Category Management",
            categoriesHint: "Manage frequently used income and expense categories.",
            records: "Transaction Records",
            recordsHint: "Record each income and expense, with filtering, editing, and deletion.",
            stats: "Statistics",
            statsHint: "View monthly income and expense trends with category breakdowns.",
            aiAssistant: "AI Assistant",
            aiAssistantHint: "Ask AI for bookkeeping tips, spending analysis, and saving advice.",
            aiQuestion: "Your Question",
            aiQuestionPlaceholder: "Example: Please give me advice on reducing living expenses",
            aiSend: "Send Question",
            aiAnswer: "AI Response",
            aiDefaultAnswer: "Enter a question above and send it to get AI advice.",
            admin: "Admin",
            adminHint: "Search users, view details, reset passwords, and delete users.",
            incomeTotal: "Total Income",
            expenseTotal: "Total Expenses",
            balance: "Balance",
            recentRecords: "Recent Records",
            incomeCategoryStats: "Income by Category",
            expenseCategoryStats: "Expenses by Category",
            categoryForm: "Add / Edit Category",
            myCategories: "Category List",
            recordForm: "Add / Edit Entry",
            myRecords: "Transaction Details",
            filter: "Filter",
            allRecords: "All Types",
            allCategories: "All Categories",
            incomeOnly: "Income Only",
            expenseOnly: "Expenses Only",
            startDate: "Start Date",
            endDate: "End Date",
            queryByDate: "Apply Date Filter",
            monthlyStats: "Monthly Statistics",
            userManagement: "User Management",
            userDetail: "User Details",
            resetPassword: "Reset Password",
            newPassword: "New Password",
            oldPassword: "Current Password",
            changePassword: "Change Password",
            confirmPassword: "Confirm New Password",
            queryOneUser: "Search One User",
            queryAllUsers: "Search All Users",
            category: "Category",
            categoryName: "Category Name",
            type: "Type",
            amount: "Amount",
            date: "Date",
            remark: "Note",
            actions: "Actions",
            year: "Year",
            month: "Month",
            query: "Search",
            save: "Save",
            clear: "Clear Input",
            income: "Income",
            expense: "Expense",
            edit: "Edit",
            remove: "Delete",
            detail: "Details",
            noData: "No data available",
            prevPage: "Previous",
            nextPage: "Next",
            pageUnit: "page",
            totalPrefix: "Total",
            totalSuffix: "records",
            selectUserHint: "Click Details to view user information.",
            editModeCategory: "Editing category. Update the form above and save.",
            editModeRecord: "Editing entry. Update the form above and save.",
            recordsUpdated: "Record list updated",
            refreshed: "Current page refreshed",
            displaySettings: "Display & Personalization",
            settingsHint: "Set theme mode, amount colors, and interface theme color.",
            theme: "Theme",
            themeLight: "Light Mode",
            themeDark: "Dark Mode",
            themeSystem: "Follow System",
            incomeColor: "Income Amount Color",
            expenseColor: "Expense Amount Color",
            primaryColor: "Theme Color",
            resetAppearance: "Reset Personalization Settings",
            appearanceSaved: "Personalization settings saved",
            accountSecurity: "Account Security",
            deleteAccount: "Delete Account",
            deleteAccountHint: "Deleting your account will remove the current account and related data. Please proceed carefully.",
            deletePassword: "Current Password",
            deleteConfirmPassword: "Re-enter Current Password",
            deletePasswordRequired: "Please enter your current password twice to delete your account.",
            deletePasswordMismatch: "The two passwords do not match.",
            deleteAccountConfirm1: "Delete the current account {username}? This action cannot be undone.",
            deleteAccountConfirmInput: "To confirm deletion, enter your current username:",
            deleteAccountCancelled: "The username does not match. Account deletion was canceled.",
            deleteAccountSuccess: "Account deleted",
            deleteAccountBackendMissing: "Account deletion failed. Please check whether backend DELETE /me works correctly.",
            loginRequired: "Your login has expired. Please log in again.",
            requestFailed: "Request failed",
            usernamePasswordRequired: "Please enter username and password.",
            loginSuccessNoToken: "Login succeeded, but the backend did not return a token.",
            loginSuccess: "Logged in successfully",
            registerSuccess: "Registration successful. Please log in.",
            changePasswordRequired: "Please enter current password, new password, and confirmation password.",
            newPasswordMismatch: "The new passwords do not match.",
            changePasswordSuccess: "Password changed successfully",
            yearInvalid: "Year must be greater than 0.",
            monthInvalid: "Month must be between 1 and 12.",
            categoryNameRequired: "Please enter a category name.",
            saveSuccess: "Saved successfully",
            categoryRequired: "Please select a category.",
            amountRequired: "Please enter an amount.",
            dateRequired: "Please select a date.",
            dateRangeRequired: "Please enter both start date and end date.",
            startDateAfterEndDate: "Start date cannot be later than end date.",
            questionRequired: "Please enter a question.",
            sending: "Sending...",
            aiThinking: "AI is generating a response. Please wait...",
            aiEmpty: "AI returned no content.",
            aiSuccess: "AI response received",
            usernameRequired: "Please enter a username.",
            usernameNewPasswordRequired: "Please enter username and new password.",
            resetPasswordSuccess: "Password reset successfully",
            confirmDeleteCategory: "Delete this category?",
            confirmDeleteRecord: "Delete this record?",
            confirmDeleteUser: "Delete user {username}?",
            deleteSuccess: "Deleted successfully",
            confirmPasswordLength: "Confirmation password must be 6 to 12 characters.",
            passwordLength: "Password must be 6 to 12 characters.",
            oldPasswordWrong: "Current password is incorrect.",
            passwordMismatchBackend: "The two passwords do not match.",
            accountDeleted: "Account deleted"
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

    function applyBalanceClass(element, value) {
        if (!element) return;

        element.classList.remove("amount-income", "amount-expense");

        if (Number(value || 0) < 0) {
            element.classList.add("amount-expense");
        } else {
            element.classList.add("amount-income");
        }
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

    function normalizeHex(value, fallback) {
        const text = String(value || "").trim();

        if (/^#[0-9a-fA-F]{6}$/.test(text)) {
            return text;
        }

        return fallback;
    }

    function shadeHex(hex, percent) {
        const value = normalizeHex(hex, "#0f8277").slice(1);
        const number = parseInt(value, 16);
        const amount = Math.round(2.55 * percent);
        const r = Math.max(0, Math.min(255, (number >> 16) + amount));
        const g = Math.max(0, Math.min(255, ((number >> 8) & 0x00ff) + amount));
        const b = Math.max(0, Math.min(255, (number & 0x0000ff) + amount));

        return "#" + (0x1000000 + r * 0x10000 + g * 0x100 + b).toString(16).slice(1);
    }

    function saveAppearance() {
        localStorage.setItem("pf.appearance", JSON.stringify(state.appearance));
    }

    function getResolvedTheme() {
        if (state.appearance.theme === "system") {
            return window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
        }

        return state.appearance.theme || "light";
    }

    function applyAppearance() {
        const appearance = state.appearance || {};
        const incomeColor = normalizeHex(appearance.incomeColor, "#0f8277");
        const expenseColor = normalizeHex(appearance.expenseColor, "#b42318");
        const primaryColor = normalizeHex(appearance.primaryColor, "#0f8277");

        state.appearance = {
            theme: appearance.theme || "light",
            incomeColor: incomeColor,
            expenseColor: expenseColor,
            primaryColor: primaryColor
        };

        document.documentElement.dataset.theme = getResolvedTheme();
        document.documentElement.style.setProperty("--income", incomeColor);
        document.documentElement.style.setProperty("--expense", expenseColor);
        document.documentElement.style.setProperty("--primary", primaryColor);
        document.documentElement.style.setProperty("--primary-dark", shadeHex(primaryColor, -18));
        document.documentElement.style.setProperty("--soft", shadeHex(primaryColor, 86));

        if ($("themeSelect")) $("themeSelect").value = state.appearance.theme;
        if ($("incomeColorInput")) $("incomeColorInput").value = incomeColor;
        if ($("expenseColorInput")) $("expenseColorInput").value = expenseColor;
        if ($("primaryColorInput")) $("primaryColorInput").value = primaryColor;
    }

    function updateAppearanceFromInputs() {
        state.appearance = {
            theme: $("themeSelect") ? $("themeSelect").value : state.appearance.theme,
            incomeColor: $("incomeColorInput") ? $("incomeColorInput").value : state.appearance.incomeColor,
            expenseColor: $("expenseColorInput") ? $("expenseColorInput").value : state.appearance.expenseColor,
            primaryColor: $("primaryColorInput") ? $("primaryColorInput").value : state.appearance.primaryColor
        };

        applyAppearance();
        saveAppearance();
        toast(text("appearanceSaved"));
    }

    function resetAppearance() {
        state.appearance = {
            theme: "light",
            incomeColor: "#0f8277",
            expenseColor: "#b42318",
            primaryColor: "#0f8277"
        };

        applyAppearance();
        saveAppearance();
        toast(text("appearanceSaved"));
    }

    function localizeMessage(message) {
        const raw = String(message || "");

        if (!raw) return "";

        const exactMap = {
            "登录已失效，请重新登录": "loginRequired",
            "请求失败": "requestFailed",
            "请输入用户名和密码": "usernamePasswordRequired",
            "登录成功，但后端没有返回 token": "loginSuccessNoToken",
            "登录成功": "loginSuccess",
            "注册成功，请登录": "registerSuccess",
            "当前密码、新密码和确认新密码都必须填写": "changePasswordRequired",
            "两次输入的新密码不一致": "newPasswordMismatch",
            "密码修改成功": "changePasswordSuccess",
            "年份必须大于0": "yearInvalid",
            "月份必须是1到12": "monthInvalid",
            "请输入分类名": "categoryNameRequired",
            "保存成功": "saveSuccess",
            "请选择分类": "categoryRequired",
            "请输入金额": "amountRequired",
            "请选择日期": "dateRequired",
            "开始日期和结束日期必须同时填写": "dateRangeRequired",
            "开始日期不能晚于结束日期": "startDateAfterEndDate",
            "请输入问题": "questionRequired",
            "发送中...": "sending",
            "AI 正在思考中，请稍等...": "aiThinking",
            "AI没有返回内容。": "aiEmpty",
            "AI回复成功": "aiSuccess",
            "请输入用户名": "usernameRequired",
            "请输入用户名和新密码": "usernameNewPasswordRequired",
            "密码已重置": "resetPasswordSuccess",
            "确定删除这个分类吗？": "confirmDeleteCategory",
            "确定删除这条记录吗？": "confirmDeleteRecord",
            "删除成功": "deleteSuccess",
            "确认密码长度必须是6到12位": "confirmPasswordLength",
            "密码长度必须是6到12位": "passwordLength",
            "当前密码错误": "oldPasswordWrong",
            "两次密码不一致": "passwordMismatchBackend",
            "账号已注销": "accountDeleted"
        };

        if (exactMap[raw]) {
            return text(exactMap[raw]);
        }

        if (raw.includes("确认密码长度必须是6到12位")) return text("confirmPasswordLength");
        if (raw.includes("密码长度必须是6到12位")) return text("passwordLength");
        if (raw.includes("当前密码错误")) return text("oldPasswordWrong");
        if (raw.includes("两次密码不一致")) return text("passwordMismatchBackend");
        if (raw.includes("账号已注销")) return text("accountDeleted");

        if (raw.includes("确定删除用户")) {
            const username = raw.replace("确定删除用户", "").replace("吗？", "").trim();
            return text("confirmDeleteUser").replace("{username}", username);
        }

        return raw;
    }

    function confirmLocalized(message) {
        return confirm(localizeMessage(message));
    }

    function promptLocalized(message) {
        return prompt(localizeMessage(message));
    }

    function toast(message) {
        const el = $("toast");
        if (!el) return;

        el.textContent = localizeMessage(message);
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

    function isAuthExpiredMessage(message) {
        const value = String(message || "").toLowerCase();

        return value.includes("token") ||
            value.includes("未登录") ||
            value.includes("登录失效") ||
            value.includes("请先登录") ||
            value.includes("重新登录") ||
            value.includes("无效") ||
            value.includes("unauthorized") ||
            value.includes("forbidden") ||
            value.includes("login");
    }

    function handleAuthExpired(message) {
        state.token = "";
        state.user = null;
        state.adminDetail = null;
        state.categories = [];
        state.records = [];
        state.homeRecentRecords = [];

        saveAuth();
        showAuthView();
        showLoginForm();

        if ($("loginPassword")) $("loginPassword").value = "";

        toast(message || text("loginRequired"));
    }

    // 统一 API 请求函数：自动携带 token，并把后端 Result 错误转换成异常
    // 共通 API リクエスト関数：token を自動付与し、バックエンドの Result エラーを例外に変換する
    async function api(path, options) {
        const opts = options || {};
        const headers = {
            "Content-Type": "application/json"
        };

        if (state.token && opts.auth !== false) {
            // 登录后的接口需要 Authorization 请求头
            // ログイン後のAPIには Authorization ヘッダーが必要
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

        // 登录失效处理：例如别的地方重新登录导致 token 失效时，自动回到登录页
        // ログイン失効処理：別の場所で再ログインして token が無効になった場合、ログイン画面へ戻す
        if (opts.auth !== false && (response.status === 401 || response.status === 403)) {
            const message = getErrorMessage(result) || text("loginRequired");
            handleAuthExpired(message);
            throw new Error(message);
        }

        if (!response.ok) {
            throw new Error(getErrorMessage(result) || ("HTTP " + response.status));
        }

        if (result && result.success === false) {
            const message = result.message || text("requestFailed");

            if (opts.auth !== false && isAuthExpiredMessage(message)) {
                handleAuthExpired(message);
            }

            throw new Error(message);
        }

        if (result && result.code !== undefined && Number(result.code) >= 400) {
            const message = result.message || text("requestFailed");

            if (opts.auth !== false && (Number(result.code) === 401 || Number(result.code) === 403 || isAuthExpiredMessage(message))) {
                handleAuthExpired(message);
            }

            throw new Error(message);
        }

        return result;
    }

    function translatePage() {
        document.querySelectorAll("[data-i18n]").forEach(function (el) {
            const key = el.dataset.i18n;
            el.textContent = text(key);
        });

        document.querySelectorAll("[data-i18n-placeholder]").forEach(function (el) {
            const key = el.dataset.i18nPlaceholder;
            el.setAttribute("placeholder", text(key));
        });

        document.querySelectorAll(".lang-btn").forEach(function (btn) {
            btn.classList.toggle("active", btn.dataset.lang === state.lang);
        });

        if ($("languageSelect")) {
            $("languageSelect").value = state.lang;
        }

        document.documentElement.lang = state.lang === "zh" ? "zh-CN" : state.lang;

        setupTypeSelects();
        setupPasswordToggleButtons();
        fillRecordCategoryOptions();
        fillFilterCategoryOptions();
        renderCurrentUser();
        applyAppearance();

        if (state.categories.length > 0) renderCategoryTable();

        if (state.homeRecentRecords.length > 0) {
            renderHomeRecentRecords(state.homeRecentRecords);
        }

        if (state.activeTab === "records" && $("recordsBody")) {
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

    // 暴露给 HTML 的 select onchange 使用，避免手机端事件绑定失败
    // HTML の select onchange から呼べるように公開し、スマホでイベントが効かない問題を防ぐ
    window.setPfLanguage = setLanguage;

    function setupTypeSelects() {
        const selects = ["categoryType", "recordType"];

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


    // 初始化密码显示/隐藏按钮
    // パスワード表示/非表示ボタンを初期化する
    function setupPasswordToggleButtons() {
        document.querySelectorAll(".password-toggle").forEach(function (button) {
            const input = $(button.dataset.target);
            if (!input) return;

            const isVisible = input.type === "text";
            button.textContent = isVisible ? "🙈" : "👁";
            button.setAttribute("title", isVisible ? text("hidePassword") : text("showPassword"));
            button.setAttribute("aria-label", isVisible ? text("hidePassword") : text("showPassword"));
        });
    }

    // 切换密码显示/隐藏
    // パスワードの表示/非表示を切り替える
    function togglePasswordVisibility(button) {
        const input = $(button.dataset.target);
        if (!input) return;

        input.type = input.type === "password" ? "text" : "password";
        setupPasswordToggleButtons();
    }

    function switchAuthForm(target) {
        const loginForm = $("loginForm");
        const registerForm = $("registerForm");
        const loginTab = $("loginTab");
        const registerTab = $("registerTab");
        if (!loginForm || !registerForm || !loginTab || !registerTab) return;

        const showRegister = target === "register";
        const currentForm = showRegister ? loginForm : registerForm;
        const nextForm = showRegister ? registerForm : loginForm;

        if (!nextForm.classList.contains("hidden")) return;

        currentForm.classList.add("auth-form-leaving");

        setTimeout(function () {
            currentForm.classList.add("hidden");
            currentForm.classList.remove("auth-form-leaving");

            nextForm.classList.remove("hidden");
            nextForm.classList.add("auth-form-entering");

            requestAnimationFrame(function () {
                nextForm.classList.add("auth-form-entered");
            });

            setTimeout(function () {
                nextForm.classList.remove("auth-form-entering");
                nextForm.classList.remove("auth-form-entered");
            }, 320);
        }, 150);

        loginTab.classList.toggle("active", !showRegister);
        registerTab.classList.toggle("active", showRegister);
    }

    function showLoginForm() {
        switchAuthForm("login");
    }

    function showRegisterForm() {
        switchAuthForm("register");
    }

    function showAuthView() {
        $("authView").classList.remove("hidden");
        $("appView").classList.add("hidden");

        // 不写死 display，让 CSS 根据电脑端 / 手机端控制布局
        // display を固定せず、PC / スマホのレイアウトは CSS に任せる
        $("authView").style.display = "";
        $("appView").style.display = "none";
    }

    function showAppView() {
        $("authView").classList.add("hidden");
        $("appView").classList.remove("hidden");

        // 不写死 appView 的 display，避免手机端仍然保持电脑端两栏布局
        // appView の display を固定せず、スマホで2カラムになる問題を防ぐ
        $("authView").style.display = "none";
        $("appView").style.display = "";

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

        const form = event.currentTarget;
        if (form) form.classList.add("auth-submitting");

        const username = $("loginUsername").value.trim();
        const password = $("loginPassword").value;

        if (!username || !password) {
            toast(text("usernamePasswordRequired"));
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
                throw new Error(text("loginSuccessNoToken"));
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

            toast(text("loginSuccess"));
        } catch (error) {
            toast(error.message);
        } finally {
            if (form) form.classList.remove("auth-submitting");
        }
    }

    async function handleRegister(event) {
        event.preventDefault();

        const form = event.currentTarget;
        if (form) form.classList.add("auth-submitting");

        const username = $("registerUsername").value.trim();
        const password = $("registerPassword").value;

        if (!username || !password) {
            toast(text("usernamePasswordRequired"));
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
        } finally {
            if (form) form.classList.remove("auth-submitting");
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
            toast(text("changePasswordRequired"));
            return;
        }

        if (newPassword !== confirmPassword) {
            toast(text("newPasswordMismatch"));
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

            toast((result && result.message) || text("changePasswordSuccess"));

            $("meOldPassword").value = "";
            $("meNewPassword").value = "";
            $("meConfirmPassword").value = "";

            const passwordPanel = document.querySelector('[data-mobile-panel="changePasswordPanel"]');
            if (passwordPanel) passwordPanel.classList.add("collapsed");
        } catch (error) {
            toast(error.message);
        }
    }

    function clearLoginState() {
        state.token = "";
        state.user = null;
        state.adminDetail = null;
        state.categories = [];
        state.records = [];
        state.homeRecentRecords = [];

        saveAuth();
        showAuthView();
    }

    async function logout() {
        try {
            await api("/logout", {method: "POST"});
        } catch (e) {
            // 后端退出失败时，前端仍然清除登录状态
        }

        clearLoginState();
        toast(text("logout"));
    }

    async function deleteMyAccount(event) {
        if (event) event.preventDefault();

        const username = (state.user && state.user.username) ? state.user.username : "";
        const password = $("deletePassword") ? $("deletePassword").value : "";
        const confirmPassword = $("deleteConfirmPassword") ? $("deleteConfirmPassword").value : "";

        if (!username) {
            toast(text("login"));
            return;
        }

        if (!password || !confirmPassword) {
            toast(text("deletePasswordRequired"));
            return;
        }

        if (password !== confirmPassword) {
            toast(text("deletePasswordMismatch"));
            return;
        }

        const message = text("deleteAccountConfirm1").replace("{username}", username);
        if (!confirmLocalized(message)) return;

        try {
            // 普通用户自注销接口：后端 DeleteAccountRequest 需要 password 和 confirmPassword
            // 一般ユーザー自身のアカウント削除API：DeleteAccountRequest には password と confirmPassword が必要
            const result = await api("/me", {
                method: "DELETE",
                body: {
                    password: password,
                    confirmPassword: confirmPassword
                }
            });

            clearLoginState();

            if ($("deletePassword")) $("deletePassword").value = "";
            if ($("deleteConfirmPassword")) $("deleteConfirmPassword").value = "";

            toast((result && result.message) || text("deleteAccountSuccess"));
        } catch (error) {
            const messageText = String(error.message || "");

            if (messageText.includes("HTTP 404") || messageText.includes("HTTP 405")) {
                toast(text("deleteAccountBackendMissing"));
            } else {
                toast(messageText);
            }
        }
    }

    function getPanelBody(panel) {
        if (!panel) return null;

        return panel.querySelector(":scope > .collapsible-body") ||
            panel.querySelector(":scope > .mobile-collapsible-body") ||
            panel.querySelector(":scope > form.collapsible-body") ||
            panel.querySelector(":scope > form.mobile-collapsible-body");
    }

    function finishPanelMotion(body) {
        if (!body) return;

        body.style.overflow = "";
        body.style.willChange = "";
        body.style.transition = "";
    }

    function animatePanel(panel, shouldOpen) {
        const body = getPanelBody(panel);

        if (!body) {
            panel.classList.toggle("collapsed", !shouldOpen);
            return;
        }

        clearTimeout(body.motionTimer);
        body.style.overflow = "hidden";
        body.style.willChange = "height, opacity";

        if (shouldOpen) {
            panel.classList.remove("collapsed");

            body.style.transition = "none";
            body.style.height = "0px";
            body.style.opacity = "0";

            requestAnimationFrame(function () {
                body.style.transition = "height 300ms cubic-bezier(0.22, 1, 0.36, 1), opacity 180ms ease";
                body.style.height = body.scrollHeight + "px";
                body.style.opacity = "1";
            });

            body.motionTimer = setTimeout(function () {
                body.style.height = "auto";
                finishPanelMotion(body);
            }, 330);
        } else {
            body.style.transition = "none";
            body.style.height = body.scrollHeight + "px";
            body.style.opacity = "1";

            requestAnimationFrame(function () {
                body.style.transition = "height 260ms cubic-bezier(0.22, 1, 0.36, 1), opacity 140ms ease";
                body.style.height = "0px";
                body.style.opacity = "0";
            });

            body.motionTimer = setTimeout(function () {
                panel.classList.add("collapsed");
                body.style.height = "";
                body.style.opacity = "";
                finishPanelMotion(body);
            }, 290);
        }
    }

    function closeSiblingSecurityPanels(openingName) {
        if (openingName !== "changePasswordPanel" && openingName !== "deleteAccountPanel") return;

        ["changePasswordPanel", "deleteAccountPanel"].forEach(function (name) {
            if (name === openingName) return;

            const sibling = document.querySelector('[data-mobile-panel="' + name + '"]');
            if (sibling && !sibling.classList.contains("collapsed")) {
                animatePanel(sibling, false);
            }
        });
    }

    function gentlyRevealPanel(panel) {
        if (!panel || panel.classList.contains("collapsed")) return;

        const rect = panel.getBoundingClientRect();
        const safeTop = 92;

        if (rect.top < safeTop || rect.top > window.innerHeight * 0.72) {
            setTimeout(function () {
                panel.scrollIntoView({
                    behavior: "smooth",
                    block: "nearest"
                });
            }, 150);
        }
    }

    function toggleMobilePanel(name) {
        const panel = document.querySelector('[data-mobile-panel="' + name + '"]');
        if (!panel) return;

        const shouldOpen = panel.classList.contains("collapsed");

        if (shouldOpen) {
            closeSiblingSecurityPanels(name);
        }

        animatePanel(panel, shouldOpen);
        gentlyRevealPanel(panel);
    }

    function openMobilePanel(name) {
        const panel = document.querySelector('[data-mobile-panel="' + name + '"]');
        if (!panel) return;

        if (panel.classList.contains("collapsed")) {
            animatePanel(panel, true);
        }
    }

    function scrollToElement(id, offset) {
        const el = $(id);
        if (!el) return;

        const topOffset = typeof offset === "number" ? offset : 92;

        setTimeout(function () {
            const top = el.getBoundingClientRect().top + window.pageYOffset - topOffset;

            window.scrollTo({
                top: Math.max(0, top),
                behavior: "smooth"
            });
        }, 120);
    }

    function scrollToRecordsList() {
        scrollToElement("recordsListPanel");
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
            } else if (tab === "ai") {
                renderAiDefaultAnswer();
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

    function renderHomeRecentRecords(records) {
        const recentRows = (records || []).map(function (record) {
            return "<tr>" +
                "<td>" + escapeHtml(record.id) + "</td>" +
                "<td>" + escapeHtml(getCategoryName(record)) + "</td>" +
                "<td>" + renderType(record.type) + "</td>" +
                '<td class="amount-cell ' + (record.type === "income" ? "amount-income" : "amount-expense") + '">' + money(record.amount) + "</td>" +
                "<td>" + escapeHtml(record.recordDate || "") + "</td>" +
                "</tr>";
        });

        renderRows("recentRecordsBody", recentRows, 5);
    }

    async function loadHome() {
        const year = Number(state.homeYear || currentYear());
        const month = Number(state.homeMonth || currentMonth());

        const statsResult = await api("/transaction-stats/month?year=" + year + "&month=" + month);
        const stats = getResultData(statsResult) || {};

        $("homeIncome").textContent = money(stats.incomeTotal || stats.income || stats.totalIncome);
        $("homeExpense").textContent = money(stats.expenseTotal || stats.expense || stats.totalExpense);
        $("homeBalance").textContent = money(stats.balance);
        applyBalanceClass($("homeBalance"), stats.balance);

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

        state.homeRecentRecords = recentList;
        renderHomeRecentRecords(state.homeRecentRecords);

        await renderCategoryStats("income", year, month, "homeIncomeStats");
        await renderCategoryStats("expense", year, month, "homeExpenseStats");
    }

    async function handleHomeMonthQuery(event) {
        event.preventDefault();

        const year = Number($("homeYear").value);
        const month = Number($("homeMonth").value);

        if (!year || year <= 0) {
            toast(text("yearInvalid"));
            return;
        }

        if (!month || month < 1 || month > 12) {
            toast(text("monthInvalid"));
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
        renderMobileCategories();
    }

    // 手机端分类列表：不再挤在表格里，操作按钮固定在卡片下方
    // スマホ用カテゴリ一覧：表で詰め込まず、操作ボタンをカード下部に配置する
    function renderMobileCategories() {
        const box = $("mobileCategoriesList");
        if (!box) return;

        if (!state.categories || state.categories.length === 0) {
            box.innerHTML = '<div class="mobile-empty">' + text("noData") + "</div>";
            return;
        }

        box.innerHTML = state.categories.map(function (item) {
            const id = escapeHtml(item.id);
            const name = escapeHtml(item.name || item.categoryName || "");
            const typeText = renderType(item.type);

            return '<article class="mobile-category-card">' +
                '<div class="mobile-category-main">' +
                '<strong>' + name + '</strong>' +
                '<span>' + typeText + '</span>' +
                '</div>' +
                '<div class="mobile-category-actions">' +
                '<button type="button" data-action="edit-category" data-id="' + id + '">' + text("edit") + '</button>' +
                '<button type="button" class="danger" data-action="delete-category" data-id="' + id + '">' + text("remove") + '</button>' +
                '</div>' +
                '</article>';
        }).join("");

        if (window.observeMobileCards) window.observeMobileCards();
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

        const options = ['<option value="">' + text("allCategories") + "</option>"];

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
            toast(text("categoryNameRequired"));
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

            toast((result && result.message) || text("saveSuccess"));
            clearCategoryForm();
            await loadCategories();
            scrollToElement("mobileCategoriesList");
        } catch (error) {
            toast(error.message);
        }
    }

    function clearCategoryForm() {
        $("categoryId").value = "";
        $("categoryName").value = "";
        $("categoryType").value = "expense";
    }

    // 加载收支记录：把分页和筛选条件组装成查询参数，调用后端组合搜索接口
    // 収支記録読み込み：ページングと絞り込み条件をクエリパラメータにし、バックエンドの複合検索APIを呼ぶ
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
                '<td class="amount-cell ' + (record.type === "income" ? "amount-income" : "amount-expense") + '">' + money(record.amount) + "</td>" +
                "<td>" + escapeHtml(record.remark || "") + "</td>" +
                "<td>" + escapeHtml(record.recordDate || "") + "</td>" +
                "<td class=\"actions\">" +
                "<button type=\"button\" data-action=\"edit-record\" data-id=\"" + escapeHtml(record.id) + "\">" + text("edit") + "</button>" +
                "<button type=\"button\" class=\"danger\" data-action=\"delete-record\" data-id=\"" + escapeHtml(record.id) + "\">" + text("remove") + "</button>" +
                "</td>" +
                "</tr>";
        });

        renderRows("recordsBody", rows, 7);
        renderMobileRecords(records);
    }

    // 手机端记录列表：默认只显示关键信息，点击后展开备注和操作
    // スマホ用の記録一覧：通常は主要情報のみ表示し、タップでメモと操作を開く
    function renderMobileRecords(records) {
        const box = $("mobileRecordsList");
        if (!box) return;

        if (!records || records.length === 0) {
            box.innerHTML = '<div class="mobile-empty">' + text("noData") + "</div>";
            return;
        }

        box.innerHTML = records.map(function (record) {
            const id = escapeHtml(record.id);
            const categoryName = escapeHtml(getCategoryName(record));
            const typeText = renderType(record.type);
            const amountText = money(record.amount);
            const recordDate = escapeHtml(record.recordDate || "");
            const remark = escapeHtml(record.remark || "-");
            const typeClass = record.type === "income" ? "income amount-income" : "expense amount-expense";

            return '<article class="mobile-record-card">' +
                '<button type="button" class="mobile-record-main" data-action="toggle-mobile-record" data-id="' + id + '">' +
                '<span class="mobile-record-left">' +
                '<strong>' + categoryName + '</strong>' +
                '<em>' + typeText + ' · ' + recordDate + '</em>' +
                '</span>' +
                '<span class="mobile-record-amount ' + typeClass + '">' + amountText + '</span>' +
                '<span class="mobile-record-arrow">⌄</span>' +
                '</button>' +
                '<div class="mobile-record-detail hidden" id="mobileRecordDetail-' + id + '">' +
                '<div class="mobile-record-row"><span>ID</span><strong>' + id + '</strong></div>' +
                '<div class="mobile-record-row"><span>' + text("remark") + '</span><strong>' + remark + '</strong></div>' +
                '<div class="mobile-record-actions">' +
                '<button type="button" data-action="edit-record" data-id="' + id + '">' + text("edit") + '</button>' +
                '<button type="button" class="danger" data-action="delete-record" data-id="' + id + '">' + text("remove") + '</button>' +
                '</div>' +
                '</div>' +
                '</article>';
        }).join("");
    }

    // 渲染分页按钮：根据总条数和每页条数计算总页数
    // ページングボタン描画：総件数と1ページ件数から総ページ数を計算する
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

        $("recordPrevPage").addEventListener("click", async function (event) {
            flashPagerButton(event.currentTarget);
            if (state.recordPage <= 1) return;
            state.recordPage--;
            await loadRecords();
        });

        $("recordNextPage").addEventListener("click", async function (event) {
            flashPagerButton(event.currentTarget);
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
            toast(text("categoryRequired"));
            return;
        }

        if (!amount) {
            toast(text("amountRequired"));
            return;
        }

        if (!recordDate) {
            toast(text("dateRequired"));
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

            toast((result && result.message) || text("saveSuccess"));
            clearRecordForm();
            state.recordPage = 1;
            await loadRecords();
            scrollToRecordsList();
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
        scrollToRecordsList();
        toast(text("recordsUpdated"));
    }

    // 按日期范围和分类筛选记录：前端先做简单日期校验，再请求后端
    // 日付範囲とカテゴリで記録を絞り込む：フロントで簡単な日付チェックをしてからバックエンドへ送る
    async function filterRecordsByRange(event) {
        event.preventDefault();

        const start = $("startRecordDate").value;
        const end = $("endOfRecordDate").value;
        const categoryId = $("filterCategory") ? $("filterCategory").value : "";

        if ((start && !end) || (!start && end)) {
            toast(text("dateRangeRequired"));
            return;
        }

        if (start && end && start > end) {
            toast(text("startDateAfterEndDate"));
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
            applyBalanceClass($("statsBalance"), data.balance);

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

    function getCssColor(name, fallback) {
        const value = getComputedStyle(document.documentElement).getPropertyValue(name).trim();

        return value || fallback;
    }

    function getCanvasPointer(event, canvas) {
        const rect = canvas.getBoundingClientRect();
        const scaleX = canvas.width / rect.width;
        const scaleY = canvas.height / rect.height;

        return {
            x: (event.clientX - rect.left) * scaleX,
            y: (event.clientY - rect.top) * scaleY
        };
    }

    function getChartSliceIndex(canvas, point) {
        const state = canvas._chartState;
        if (!state) return -1;

        const dx = point.x - state.centerX;
        const dy = point.y - state.centerY;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < state.innerRadius || distance > state.radius + 16) {
            return -1;
        }

        let angle = Math.atan2(dy, dx);

        if (angle < -Math.PI / 2) {
            angle += Math.PI * 2;
        }

        return state.slices.findIndex(function (slice) {
            return angle >= slice.startAngle && angle <= slice.endAngle;
        });
    }

    function setLegendActive(canvas, activeIndex) {
        if (!canvas || !canvas.id) return;

        document.querySelectorAll('[data-chart-canvas="' + canvas.id + '"]').forEach(function (item) {
            item.classList.toggle("active", Number(item.dataset.index) === activeIndex);
        });
    }

    function drawPieChart(canvas, list, activeIndex) {
        if (!canvas || !list || list.length === 0) return;

        const ctx = canvas.getContext("2d");
        const width = canvas.width;
        const height = canvas.height;
        const centerX = width / 2;
        const centerY = height / 2;
        const radius = Math.min(width, height) / 2 - 18;
        const innerRadius = radius * 0.56;

        ctx.clearRect(0, 0, width, height);

        let total = 0;

        list.forEach(function (item) {
            total += Number(item.amount || 0);
        });

        if (total <= 0) return;

        let startAngle = -Math.PI / 2;
        const slices = [];

        list.forEach(function (item, index) {
            const value = Number(item.amount || 0);
            const angle = (value / total) * Math.PI * 2;
            const endAngle = startAngle + angle;
            const isActive = index === activeIndex;
            const midAngle = (startAngle + endAngle) / 2;
            const offset = isActive ? 8 : 0;
            const sliceCenterX = centerX + Math.cos(midAngle) * offset;
            const sliceCenterY = centerY + Math.sin(midAngle) * offset;
            const outerRadius = radius + (isActive ? 4 : 0);

            slices.push({
                startAngle: startAngle,
                endAngle: endAngle
            });

            ctx.save();

            if (isActive) {
                ctx.shadowColor = "rgba(16, 36, 42, 0.22)";
                ctx.shadowBlur = 12;
                ctx.shadowOffsetY = 4;
            }

            ctx.beginPath();
            ctx.arc(sliceCenterX, sliceCenterY, outerRadius, startAngle, endAngle);
            ctx.arc(sliceCenterX, sliceCenterY, innerRadius, endAngle, startAngle, true);
            ctx.closePath();

            const color = getChartColor(index);
            ctx.fillStyle = color;
            ctx.fill();

            if (isActive) {
                ctx.lineWidth = 1;
                ctx.strokeStyle = "rgba(255, 255, 255, 0.18)";
                ctx.stroke();
            }

            ctx.restore();

            startAngle = endAngle;
        });

        const centerFill = getCssColor("--panel", "#ffffff");
        const centerText = getCssColor("--text", "#10242a");

        ctx.save();
        ctx.beginPath();
        ctx.arc(centerX, centerY, innerRadius - 1, 0, Math.PI * 2);
        ctx.fillStyle = centerFill;
        ctx.fill();
        ctx.restore();

        ctx.fillStyle = centerText;
        ctx.textAlign = "center";
        ctx.textBaseline = "middle";

        if (typeof activeIndex === "number" && activeIndex >= 0 && list[activeIndex]) {
            const activeItem = list[activeIndex];
            const percent = total > 0 ? ((Number(activeItem.amount || 0) / total) * 100).toFixed(1) + "%" : "0%";

            ctx.font = "bold 15px Arial";
            ctx.fillText(percent, centerX, centerY - 8);

            ctx.font = "12px Arial";
            ctx.fillStyle = getCssColor("--muted", "#607276");
            ctx.fillText(String(activeItem.name || ""), centerX, centerY + 13);
        } else {
            ctx.font = "bold 16px Arial";
            ctx.fillText(money(total), centerX, centerY);
        }

        canvas._chartState = {
            list: list,
            total: total,
            centerX: centerX,
            centerY: centerY,
            radius: radius,
            innerRadius: innerRadius,
            slices: slices,
            activeIndex: activeIndex
        };
    }

    function attachChartInteractions(canvas, list) {
        if (!canvas || canvas._chartBound) return;

        canvas._chartBound = true;

        canvas.addEventListener("pointermove", function (event) {
            if (event.pointerType === "touch") return;

            const point = getCanvasPointer(event, canvas);
            const index = getChartSliceIndex(canvas, point);
            const state = canvas._chartState || {};

            if (index !== state.activeIndex) {
                drawPieChart(canvas, state.list || list, index);
                setLegendActive(canvas, index);
            }
        });

        canvas.addEventListener("pointerleave", function () {
            const state = canvas._chartState || {};
            drawPieChart(canvas, state.list || list, -1);
            setLegendActive(canvas, -1);
        });

        canvas.addEventListener("pointerdown", function (event) {
            const point = getCanvasPointer(event, canvas);
            const index = getChartSliceIndex(canvas, point);
            const state = canvas._chartState || {};
            const nextIndex = state.activeIndex === index ? -1 : index;

            drawPieChart(canvas, state.list || list, nextIndex);
            setLegendActive(canvas, nextIndex);
        });
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
                '<canvas id="' + canvasId + '" width="240" height="240"></canvas>' +
                '<div class="chart-legend">' +
                list.map(function (item, index) {
                    return '<div class="chart-legend-item" data-chart-canvas="' + canvasId + '" data-index="' + index + '">' +
                        '<span class="chart-dot" style="background:' + getChartColor(index) + '"></span>' +
                        '<span>' + escapeHtml(item.name) + '</span>' +
                        '<strong class="' + (type === "income" ? "amount-income" : "amount-expense") + '">' + money(item.amount) + '</strong>' +
                        '</div>';
                }).join("") +
                '</div>' +
                '</div>';

            const canvas = $(canvasId);

            drawPieChart(canvas, list, -1);
            attachChartInteractions(canvas, list);

            target.querySelectorAll(".chart-legend-item").forEach(function (item) {
                item.addEventListener("pointerenter", function () {
                    if (window.matchMedia && window.matchMedia("(hover: none)").matches) return;

                    const index = Number(item.dataset.index);
                    drawPieChart(canvas, list, index);
                    setLegendActive(canvas, index);
                });

                item.addEventListener("pointerleave", function () {
                    if (window.matchMedia && window.matchMedia("(hover: none)").matches) return;

                    drawPieChart(canvas, list, -1);
                    setLegendActive(canvas, -1);
                });

                item.addEventListener("pointerdown", function () {
                    const index = Number(item.dataset.index);
                    const state = canvas._chartState || {};
                    const nextIndex = state.activeIndex === index ? -1 : index;

                    drawPieChart(canvas, list, nextIndex);
                    setLegendActive(canvas, nextIndex);
                });
            });
        } catch (error) {
            target.innerHTML = '<div class="empty">' + escapeHtml(error.message) + '</div>';
        }
    }

    function renderAiDefaultAnswer() {
        const answerBox = $("aiAnswerBox");
        if (!answerBox) return;

        if (!answerBox.dataset.hasAnswer) {
            answerBox.textContent = text("aiDefaultAnswer");
        }
    }

    function renderAiAnswer(answer) {
        const answerBox = $("aiAnswerBox");
        if (!answerBox) return;

        answerBox.dataset.hasAnswer = "true";
        answerBox.innerHTML = escapeHtml(answer || "").replaceAll("\n", "<br>");
    }

    // AI 提问：把用户输入发送到 /ai/chat，后端再调用外部 AI 服务
    // AI 質問：ユーザー入力を /ai/chat に送り、バックエンド側で外部 AI サービスを呼び出す
    async function askAi(event) {
        event.preventDefault();

        const input = $("aiQuestionInput");
        const sendBtn = $("aiSendBtn");

        if (!input) return;

        const message = input.value.trim();

        if (!message) {
            toast(text("questionRequired"));
            return;
        }

        try {
            if (sendBtn) {
                // 调用 AI 时临时禁用按钮，避免重复提交
                // AI 呼び出し中はボタンを一時的に無効化し、重複送信を防ぐ
                sendBtn.disabled = true;
                sendBtn.textContent = "发送中...";
            }

            renderAiAnswer("AI 正在思考中，请稍等...");

            const result = await api("/ai/chat", {
                method: "POST",
                body: {
                    message: message
                }
            });

            const answer = getResultData(result);
            renderAiAnswer(answer || result.message || "AI没有返回内容。");
            toast(result.message || "AI回复成功");
        } catch (error) {
            renderAiAnswer(error.message);
            toast(error.message);
        } finally {
            if (sendBtn) {
                sendBtn.disabled = false;
                sendBtn.textContent = text("aiSend");
            }
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
            toast(text("usernameRequired"));
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
            toast(text("usernameNewPasswordRequired"));
            return;
        }

        try {
            const result = await api("/admin/users/" + encodeURIComponent(username) + "/password", {
                method: "PUT",
                body: {
                    newPassword: password
                }
            });

            toast((result && result.message) || text("resetPasswordSuccess"));
            $("adminResetPassword").value = "";
        } catch (error) {
            toast(error.message);
        }
    }

    const mobileLongPressState = {
        timer: null,
        button: null,
        detail: null,
        opened: false,
        suppressClick: false
    };

    function clearMobileLongPress() {
        clearTimeout(mobileLongPressState.timer);
        mobileLongPressState.timer = null;
    }

    function closeLongPressDetail() {
        if (!mobileLongPressState.opened) return;

        if (mobileLongPressState.button) {
            mobileLongPressState.button.classList.remove("open");
            mobileLongPressState.button.classList.remove("long-pressing");
        }

        if (mobileLongPressState.detail) {
            animateMobileDetail(mobileLongPressState.detail, false);
        }

        mobileLongPressState.opened = false;

        setTimeout(function () {
            mobileLongPressState.suppressClick = false;
            mobileLongPressState.button = null;
            mobileLongPressState.detail = null;
        }, 120);
    }

    function setupMobileRecordLongPress() {
        if (setupMobileRecordLongPress.done) return;
        setupMobileRecordLongPress.done = true;

        document.addEventListener("pointerdown", function (event) {
            const button = event.target.closest('[data-action="toggle-mobile-record"]');
            if (!button || event.pointerType === "mouse") return;

            const id = button.dataset.id;
            const detail = $("mobileRecordDetail-" + id);
            const card = button.closest(".mobile-record-card");
            if (!detail || !card) return;

            clearMobileLongPress();

            mobileLongPressState.button = button;
            mobileLongPressState.detail = detail;
            mobileLongPressState.opened = false;
            mobileLongPressState.suppressClick = false;

            mobileLongPressState.timer = setTimeout(function () {
                closeOtherMobileDetails(card);
                button.classList.add("open");
                button.classList.add("long-pressing");
                animateMobileDetail(detail, true);

                mobileLongPressState.opened = true;
                mobileLongPressState.suppressClick = true;
            }, 380);
        });

        document.addEventListener("pointerup", function (event) {
            if (event.pointerType === "mouse") return;

            clearMobileLongPress();
            closeLongPressDetail();
        });

        document.addEventListener("pointercancel", function () {
            clearMobileLongPress();
            closeLongPressDetail();
        });

        document.addEventListener("pointerleave", function (event) {
            if (event.pointerType === "mouse") return;

            clearMobileLongPress();
            closeLongPressDetail();
        });
    }

    async function handleActionClick(event) {
        const button = event.target.closest("[data-action]");
        if (!button) return;

        const action = button.dataset.action;
        const id = button.dataset.id;
        const username = button.dataset.username;

        if (action === "toggle-mobile-record") {
            const detail = $("mobileRecordDetail-" + id);
            const card = button.closest(".mobile-record-card");
            if (!detail || !card) return;

            if (mobileLongPressState.suppressClick) {
                event.preventDefault();
                event.stopPropagation();

                setTimeout(function () {
                    mobileLongPressState.suppressClick = false;
                }, 80);

                return;
            }

            const shouldOpen = detail.classList.contains("hidden");

            if (shouldOpen) {
                closeOtherMobileDetails(card);
                button.classList.add("open");
                animateMobileDetail(detail, true);
            } else {
                button.classList.remove("open");
                animateMobileDetail(detail, false);
            }

            return;
        }

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
                openMobilePanel("categoryForm");
                scrollToElement("categoryForm", 100);
                toast(text("editModeCategory"));
            }

            if (action === "delete-category") {
                if (!confirmLocalized(text("confirmDeleteCategory"))) return;

                const result = await api("/transaction-categories/" + encodeURIComponent(id), {
                    method: "DELETE"
                });

                toast((result && result.message) || text("deleteSuccess"));
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
                openMobilePanel("recordForm");
                scrollToElement("recordForm", 100);
                toast(text("editModeRecord"));
            }

            if (action === "delete-record") {
                if (!confirmLocalized(text("confirmDeleteRecord"))) return;

                const result = await api("/transaction-records/" + encodeURIComponent(id), {
                    method: "DELETE"
                });

                toast((result && result.message) || text("deleteSuccess"));

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
                if (!confirmLocalized(text("confirmDeleteUser").replace("{username}", username))) return;

                const result = await api("/admin/users/" + encodeURIComponent(username), {
                    method: "DELETE"
                });

                toast((result && result.message) || text("deleteSuccess"));
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
        if ($("languageSelect")) {
            $("languageSelect").addEventListener("change", function () {
                setLanguage($("languageSelect").value);
            });
        }

        document.querySelectorAll(".lang-btn").forEach(function (btn) {
            btn.addEventListener("click", function () {
                setLanguage(btn.dataset.lang);
            });
        });

        if ($("loginTab")) $("loginTab").addEventListener("click", showLoginForm);
        if ($("registerTab")) $("registerTab").addEventListener("click", showRegisterForm);
        if ($("goRegisterBtn")) $("goRegisterBtn").addEventListener("click", showRegisterForm);
        if ($("goLoginBtn")) $("goLoginBtn").addEventListener("click", showLoginForm);

        if ($("loginForm")) $("loginForm").addEventListener("submit", handleLogin);
        if ($("registerForm")) $("registerForm").addEventListener("submit", handleRegister);

        document.querySelectorAll(".password-toggle").forEach(function (button) {
            button.addEventListener("click", function () {
                togglePasswordVisibility(button);
            });
        });

        $("logoutBtn").addEventListener("click", logout);

        if ($("profileLogoutBtn")) {
            $("profileLogoutBtn").addEventListener("click", logout);
        }

        // 暴露给 HTML onclick 兜底使用
        // HTML onclick のフォールバック用
        window.logoutPf = logout;

        $("changePasswordForm").addEventListener("submit", changeMyPassword);

        ["themeSelect", "incomeColorInput", "expenseColorInput", "primaryColorInput"].forEach(function (id) {
            const input = $(id);
            if (!input) return;

            input.addEventListener("change", updateAppearanceFromInputs);
            input.addEventListener("input", function () {
                if (input.type === "color") updateAppearanceFromInputs();
            });
        });

        if ($("resetAppearanceBtn")) {
            $("resetAppearanceBtn").addEventListener("click", resetAppearance);
        }

        if ($("deleteAccountForm")) {
            $("deleteAccountForm").addEventListener("submit", deleteMyAccount);
        }

        if ($("deleteAccountBtn")) {
            $("deleteAccountBtn").addEventListener("click", deleteMyAccount);
        }

        $("refreshBtn").addEventListener("click", async function () {
            await switchTab(state.activeTab);
            toast(text("refreshed"));
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

        $("loadAllRecordsBtn").addEventListener("click", async function () {
            state.recordFilter.type = "";
            state.recordFilter.categoryId = "";
            state.recordFilter.startRecordDate = "";
            state.recordFilter.endOfRecordDate = "";

            if ($("filterCategory")) $("filterCategory").value = "";
            if ($("startRecordDate")) $("startRecordDate").value = "";
            if ($("endOfRecordDate")) $("endOfRecordDate").value = "";

            state.recordPage = 1;
            await loadRecords();
            scrollToRecordsList();
            toast(text("recordsUpdated"));
        });

        $("filterIncomeBtn").addEventListener("click", async function () {
            state.recordFilter.type = "income";
            state.recordPage = 1;
            await loadRecords();
            scrollToRecordsList();
            toast(text("recordsUpdated"));
        });

        $("filterExpenseBtn").addEventListener("click", async function () {
            state.recordFilter.type = "expense";
            state.recordPage = 1;
            await loadRecords();
            scrollToRecordsList();
            toast(text("recordsUpdated"));
        });

        $("rangeFilterForm").addEventListener("submit", filterRecordsByRange);

        $("homeMonthForm").addEventListener("submit", handleHomeMonthQuery);

        $("monthlyStatsForm").addEventListener("submit", loadStats);

        $("aiForm").addEventListener("submit", askAi);

        $("adminSearchForm").addEventListener("submit", searchAdminUser);
        $("adminLoadAllBtn").addEventListener("click", loadAdminUsers);
        $("adminResetForm").addEventListener("submit", resetAdminPassword);

        document.addEventListener("click", handleActionClick);
    }

    function setupSmoothInteractions() {
        if (setupSmoothInteractions.done) return;
        setupSmoothInteractions.done = true;

        // 轻量级点击反馈：让按钮像 App 一样有按下感
        // 軽量クリックフィードバック：ボタンにアプリらしい押下感を付ける
        document.addEventListener("pointerdown", function (event) {
            const target = event.target.closest("button, .mobile-record-main, .mobile-category-card, .summary-card, .panel");
            if (!target || target.disabled) return;

            target.classList.add("is-pressing");

            clearTimeout(target.pressTimer);
            target.pressTimer = setTimeout(function () {
                target.classList.remove("is-pressing");
            }, 180);
        });

        document.addEventListener("pointerup", clearPressingState);
        document.addEventListener("pointercancel", clearPressingState);
        document.addEventListener("pointerleave", clearPressingState);

        document.addEventListener("click", function (event) {
            const button = event.target.closest("button");
            if (!button || button.disabled) return;

            const rect = button.getBoundingClientRect();
            const ripple = document.createElement("span");
            const size = Math.max(rect.width, rect.height);
            const x = event.clientX - rect.left - size / 2;
            const y = event.clientY - rect.top - size / 2;

            ripple.className = "ui-ripple";
            ripple.style.width = size + "px";
            ripple.style.height = size + "px";
            ripple.style.left = x + "px";
            ripple.style.top = y + "px";

            button.appendChild(ripple);

            if (button.closest(".pagination")) {
                flashPagerButton(button);
            }

            clearButtonFocus(event.target);

            setTimeout(function () {
                ripple.remove();
                button.classList.remove("is-pressing");
            }, 220);
        });
    }

    function clearPressingState() {
        document.querySelectorAll(".is-pressing").forEach(function (el) {
            el.classList.remove("is-pressing");
            if (typeof el.blur === "function") el.blur();
        });
    }

    function setupChartTouchInteractions() {
        if (setupChartTouchInteractions.done) return;
        setupChartTouchInteractions.done = true;

        document.addEventListener("pointerdown", function (event) {
            const chartBox = event.target.closest ? event.target.closest(".chart-box") : null;
            const legendItem = event.target.closest ? event.target.closest(".chart-legend-item") : null;

            if (chartBox) {
                chartBox.classList.add("chart-touching");

                clearTimeout(chartBox.touchTimer);
                chartBox.touchTimer = setTimeout(function () {
                    chartBox.classList.remove("chart-touching");
                }, 140);
            }

            if (legendItem) {
                legendItem.classList.add("legend-touching");

                clearTimeout(legendItem.touchTimer);
                legendItem.touchTimer = setTimeout(function () {
                    legendItem.classList.remove("legend-touching");
                }, 140);
            }
        });

        document.addEventListener("pointerup", clearChartTouchState);
        document.addEventListener("pointercancel", clearChartTouchState);
    }

    function clearChartTouchState() {
        document.querySelectorAll(".chart-touching, .legend-touching").forEach(function (el) {
            el.classList.remove("chart-touching");
            el.classList.remove("legend-touching");
        });
    }

    function clearButtonFocus(target) {
        const button = target && target.closest ? target.closest("button") : null;

        if (button && typeof button.blur === "function") {
            setTimeout(function () {
                button.blur();
                button.classList.remove("is-pressing");
            }, 90);
        }
    }

    function forceClearTouchButtons() {
        document.querySelectorAll("button.is-pressing, button:focus").forEach(function (button) {
            button.classList.remove("is-pressing");

            if (typeof button.blur === "function") {
                button.blur();
            }
        });
    }

    function setupRecordScrollMotion() {
        // v28：不做滚动浮入动画，避免手机端列表抖动
        // v28：スクロール時の浮入アニメーションは使わず、リストの揺れを防ぐ
    }

    function animateMobileDetail(detail, shouldOpen) {
        if (!detail) return;

        clearTimeout(detail.motionTimer);
        detail.style.overflow = "hidden";
        detail.style.willChange = "height, opacity";

        if (shouldOpen) {
            detail.classList.remove("hidden");
            detail.classList.add("detail-opening");

            detail.style.transition = "none";
            detail.style.height = "0px";
            detail.style.opacity = "0";

            requestAnimationFrame(function () {
                detail.style.transition = "height 260ms cubic-bezier(0.22, 1, 0.36, 1), opacity 180ms ease";
                detail.style.height = detail.scrollHeight + "px";
                detail.style.opacity = "1";
            });

            detail.motionTimer = setTimeout(function () {
                detail.classList.remove("detail-opening");
                detail.style.height = "auto";
                detail.style.opacity = "";
                detail.style.overflow = "";
                detail.style.willChange = "";
                detail.style.transition = "";
            }, 290);
        } else {
            detail.classList.add("detail-closing");
            detail.style.transition = "none";
            detail.style.height = detail.scrollHeight + "px";
            detail.style.opacity = "1";

            requestAnimationFrame(function () {
                detail.style.transition = "height 220ms cubic-bezier(0.22, 1, 0.36, 1), opacity 150ms ease";
                detail.style.height = "0px";
                detail.style.opacity = "0";
            });

            detail.motionTimer = setTimeout(function () {
                detail.classList.add("hidden");
                detail.classList.remove("detail-closing");
                detail.style.height = "";
                detail.style.opacity = "";
                detail.style.overflow = "";
                detail.style.willChange = "";
                detail.style.transition = "";
            }, 250);
        }
    }

    function closeOtherMobileDetails(currentCard) {
        document.querySelectorAll(".mobile-record-card").forEach(function (card) {
            if (card === currentCard) return;

            const main = card.querySelector(".mobile-record-main.open");
            const detail = card.querySelector(".mobile-record-detail:not(.hidden)");

            if (main) main.classList.remove("open");
            if (detail) animateMobileDetail(detail, false);
        });
    }

    function flashPagerButton(button) {
        if (!button) return;

        button.classList.add("pager-tap");

        clearTimeout(button.pagerTapTimer);
        button.pagerTapTimer = setTimeout(function () {
            button.classList.remove("pager-tap");

            if (typeof button.blur === "function") {
                button.blur();
            }
        }, 180);
    }

    async function init() {
        applyAppearance();

        // pf-global-delegated-events-v10
        // 兜底事件：即使普通事件绑定失败，语言切换和退出按钮也能工作
        // フォールバックイベント：通常のイベント登録が失敗しても言語切替とログアウトが動く
        document.addEventListener("change", function (event) {
            if (event.target && event.target.id === "languageSelect") {
                setLanguage(event.target.value);
            }
        });

        document.addEventListener("click", function (event) {
            const authSwitchBtn = event.target.closest ? event.target.closest("#loginTab, #registerTab, #goRegisterBtn, #goLoginBtn") : null;
            if (authSwitchBtn) {
                event.preventDefault();

                if (authSwitchBtn.id === "registerTab" || authSwitchBtn.id === "goRegisterBtn") {
                    showRegisterForm();
                } else {
                    showLoginForm();
                }

                return;
            }

            const logoutBtn = event.target.closest ? event.target.closest("#profileLogoutBtn, #logoutBtn") : null;
            if (logoutBtn) {
                event.preventDefault();
                logout();
                return;
            }

            const deleteAccountBtn = event.target.closest ? event.target.closest("#deleteAccountBtn") : null;
            if (deleteAccountBtn) {
                event.preventDefault();
                deleteMyAccount();
                return;
            }

            const toggleBtn = event.target.closest ? event.target.closest("[data-mobile-toggle]") : null;
            if (toggleBtn) {
                event.preventDefault();
                toggleMobilePanel(toggleBtn.dataset.mobileToggle);
            }
        });

        window.setPfLanguage = setLanguage;
        window.logoutPf = logout;

        if (window.matchMedia) {
            window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", function () {
                if (state.appearance.theme === "system") applyAppearance();
            });
        }

        window.addEventListener("touchend", forceClearTouchButtons, {passive: true});
        window.addEventListener("pageshow", forceClearTouchButtons);
        setupMobileRecordLongPress();
        bindEvents();

        setupTypeSelects();
        setupPasswordToggleButtons();
        translatePage();

        $("recordDate").value = today();
        $("startRecordDate").value = today();
        $("endOfRecordDate").value = today();

        $("homeYear").value = currentYear();
        $("homeMonth").value = currentMonth();

        $("statsYear").value = currentYear();
        $("statsMonth").value = currentMonth();

        if (!state.token) {
            showAuthView();
            return;
        }

        showAppView();

        try {
            await loadMe();
            await switchTab("home");
        } catch (error) {
            handleAuthExpired(text("loginRequired"));
        }
    }

    document.addEventListener("DOMContentLoaded", init);
})();
