const state = {
    mode: "login",
    dashboardView: "categories",
    csrfToken: "",
    token: localStorage.getItem("expenseTrackerToken") || "",
    user: JSON.parse(localStorage.getItem("expenseTrackerUser") || "null"),
    editingCategoryId: null,
    editingExpenseListId: null,
    categories: [],
    expenseLists: [],
};

const elements = {
    sessionPill: document.getElementById("session-pill"),
    authPanel: document.getElementById("auth-panel"),
    dashboardPanel: document.getElementById("dashboard-panel"),
    dashboardTabs: Array.from(document.querySelectorAll("[data-dashboard-view]")),
    categoriesView: document.getElementById("categories-view"),
    listsView: document.getElementById("lists-view"),
    authTitle: document.getElementById("auth-title"),
    authStatus: document.getElementById("auth-status"),
    categoryStatus: document.getElementById("category-status"),
    categoryList: document.getElementById("category-list"),
    expenseListForm: document.getElementById("expense-list-form"),
    expenseListStatus: document.getElementById("expense-list-status"),
    expenseListTotal: document.getElementById("expense-list-total"),
    expenseListList: document.getElementById("expense-list-list"),
    expenseCategorySelect: document.getElementById("expense-category-select"),
    expenseListSubmitButton: document.getElementById("expense-list-submit-button"),
    expenseListCancelButton: document.getElementById("expense-list-cancel-button"),
    loginForm: document.getElementById("login-form"),
    registerForm: document.getElementById("register-form"),
    categoryForm: document.getElementById("category-form"),
    categorySubmitButton: document.getElementById("category-submit-button"),
    categoryCancelButton: document.getElementById("category-cancel-button"),
    categoryNameInput: document.querySelector('#category-form input[name="categoryName"]'),
    logoutButton: document.getElementById("logout-button"),
    tabs: Array.from(document.querySelectorAll("[data-mode]")),
};

function setText(target, message) {
    target.textContent = message;
}

function resetForm(form) {
    if (form && typeof form.reset === "function") {
        form.reset();
    }
}

function setCategoryFormMode(editingCategoryId) {
    state.editingCategoryId = editingCategoryId;
    const isEditing = editingCategoryId !== null;

    elements.categorySubmitButton.textContent = isEditing ? "Save" : "Add";
    elements.categoryCancelButton.classList.toggle("hidden", !isEditing);
    setText(elements.categoryStatus, isEditing ? `Editing category #${editingCategoryId}.` : "");
    if (!isEditing) {
        elements.categoryNameInput.value = "";
    }
}

function setExpenseListFormMode(editingExpenseListId) {
    state.editingExpenseListId = editingExpenseListId;
    const isEditing = editingExpenseListId !== null;

    elements.expenseListSubmitButton.textContent = isEditing ? "Save list" : "Add list";
    elements.expenseListCancelButton.classList.toggle("hidden", !isEditing);
    if (!isEditing) {
        resetForm(elements.expenseListForm);
    }
}

function setDashboardView(view) {
    state.dashboardView = view;
    elements.dashboardTabs.forEach((tab) => tab.classList.toggle("active", tab.dataset.dashboardView === view));
    elements.categoriesView.classList.toggle("hidden", view !== "categories");
    elements.listsView.classList.toggle("hidden", view !== "lists");
    if (view === "lists") {
        renderCategoryOptions(state.categories);
        if (state.token) {
            loadExpenseLists();
        }
    }
}

function persistSession(response) {
    state.token = response.jwtToken || "";
    state.user = response.user || null;

    localStorage.setItem("expenseTrackerToken", state.token);
    localStorage.setItem("expenseTrackerUser", JSON.stringify(state.user));
}

function clearSession() {
    state.token = "";
    state.user = null;
    localStorage.removeItem("expenseTrackerToken");
    localStorage.removeItem("expenseTrackerUser");
}

function setView() {
    const isAuthed = Boolean(state.token);
    elements.sessionPill.textContent = isAuthed ? "Online" : "Offline";
    elements.authPanel.classList.toggle("hidden", isAuthed);
    elements.dashboardPanel.classList.toggle("hidden", !isAuthed);
    if (!isAuthed) {
        setCategoryFormMode(null);
        setExpenseListFormMode(null);
        setDashboardView("categories");
        renderCategories([]);
        renderExpenseLists([]);
        setText(elements.expenseListTotal, "");
        setText(elements.expenseListStatus, "");
    } else {
        setDashboardView(state.dashboardView || "categories");
    }
}

function setMode(mode) {
    state.mode = mode;
    elements.tabs.forEach((tab) => tab.classList.toggle("active", tab.dataset.mode === mode));
    elements.loginForm.classList.toggle("hidden", mode !== "login");
    elements.registerForm.classList.toggle("hidden", mode !== "register");
    elements.authTitle.textContent = mode === "login" ? "Sign in" : "Create account";
}

async function primeCsrf() {
    const response = await fetch("/csrf-token", { credentials: "include" });
    if (!response.ok) {
        return;
    }
    const data = await response.json();
    state.csrfToken = data.token || "";
}

function buildHeaders(method) {
    const headers = {
        Accept: "application/json",
    };

    if (method !== "GET") {
        headers["Content-Type"] = "application/json";
        if (state.csrfToken) {
            headers["X-XSRF-TOKEN"] = state.csrfToken;
        }
    }

    if (state.token) {
        headers.Authorization = `Bearer ${state.token}`;
    }

    return headers;
}

async function request(url, options = {}) {
    const method = options.method || "GET";
    const response = await fetch(url, {
        credentials: "include",
        ...options,
        method,
        headers: {
            ...buildHeaders(method),
            ...(options.headers || {}),
        },
    });

    if (response.status === 401) {
        throw new Error("Unauthorized");
    }

    if (!response.ok) {
        const body = await response.text();
        throw new Error(body || `Request failed with status ${response.status}`);
    }

    const contentType = response.headers.get("content-type") || "";
    return contentType.includes("application/json") ? response.json() : response.text();
}

function renderCategories(items) {
    state.categories = items;
    renderCategoryOptions(items);

    if (!items.length) {
        elements.categoryList.innerHTML = '<li class="empty">No categories yet.</li>';
        return;
    }

    elements.categoryList.innerHTML = items
        .map(
            (item) => `
                <li class="category-row">
                    <div class="category-copy">
                        <span class="category-name">${escapeHtml(item.categoryName)}</span>
                    </div>
                    <div class="category-actions">
                        <button type="button" class="ghost-button secondary edit-category-button" data-category-id="${item.id}" data-category-name="${escapeHtml(item.categoryName)}">Edit</button>
                        <button type="button" class="ghost-button danger delete-category-button" data-category-id="${item.id}" data-category-name="${escapeHtml(item.categoryName)}">Delete</button>
                    </div>
                </li>
            `
        )
        .join("");

    Array.from(document.querySelectorAll(".edit-category-button")).forEach((button) => {
        button.addEventListener("click", () => {
            const categoryId = Number(button.dataset.categoryId);
            const categoryName = button.dataset.categoryName || "";
            elements.categoryNameInput.value = categoryName;
            setCategoryFormMode(categoryId);
            elements.categoryNameInput.focus();
        });
    });

    Array.from(document.querySelectorAll(".delete-category-button")).forEach((button) => {
        button.addEventListener("click", async () => {
            const categoryId = Number(button.dataset.categoryId);
            const categoryName = button.dataset.categoryName || "this category";
            const confirmed = window.confirm(`Delete ${categoryName}?`);
            if (!confirmed) {
                return;
            }

            try {
                await request(`/categories/${categoryId}`, { method: "DELETE" });
                if (state.editingCategoryId === categoryId) {
                    resetForm(elements.categoryForm);
                    setCategoryFormMode(null);
                }
                await loadCategories();
                setText(elements.categoryStatus, "Category deleted.");
            } catch (error) {
                setText(elements.categoryStatus, error.message);
            }
        });
    });
}

function renderCategoryOptions(items) {
    const options = ['<option value="">Select category</option>']
        .concat(
            items.map(
                (item) =>
                    `<option value="${item.id}">${escapeHtml(item.categoryName)}</option>`
            )
        )
        .join("");

    elements.expenseCategorySelect.innerHTML = options;
}

function renderExpenseLists(items) {
    state.expenseLists = items;

    if (!items.length) {
        elements.expenseListList.innerHTML = '<li class="empty">No expense lists yet.</li>';
        return;
    }

    elements.expenseListList.innerHTML = items
        .map(
            (item) => `
                <li class="expense-row" data-expense-list-id="${item.id}">
                    <div class="expense-copy">
                        <span class="expense-name">${escapeHtml(item.listName)}</span>
                        <span class="expense-category">${escapeHtml(item.category || "")}</span>
                    </div>
                    <div class="expense-actions">
                        <span class="expense-amount">${escapeHtml(formatAmount(item.amount))}</span>
                        <button type="button" class="ghost-button secondary edit-expense-list-button"
                            data-expense-list-id="${item.id}"
                            data-expense-list-name="${escapeHtml(item.listName)}"
                            data-expense-list-amount="${escapeHtml(item.amount)}"
                            data-expense-list-category-id="${item.categoryId}"
                        >Edit</button>
                    </div>
                </li>
            `
        )
        .join("");

    Array.from(document.querySelectorAll(".edit-expense-list-button")).forEach((button) => {
        button.addEventListener("click", () => {
            const expenseListId = Number(button.dataset.expenseListId);
            const expenseListName = button.dataset.expenseListName || "";
            const expenseListAmount = button.dataset.expenseListAmount || "";
            const expenseListCategoryId = button.dataset.expenseListCategoryId || "";

            elements.expenseListForm.elements.listName.value = expenseListName;
            elements.expenseListForm.elements.amount.value = expenseListAmount;
            elements.expenseListForm.elements.categoryId.value = expenseListCategoryId;
            setExpenseListFormMode(expenseListId);
            elements.expenseListForm.elements.listName.focus();
        });
    });
}

function formatAmount(value) {
    const numericValue = Number(value);
    return Number.isFinite(numericValue) ? numericValue.toFixed(2) : String(value);
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}

async function loadCategories() {
    if (!state.token) {
        renderCategories([]);
        return;
    }

    try {
        const categories = await request("/categories/all");
        renderCategories(categories);
        setText(elements.categoryStatus, `Loaded ${categories.length} categories.`);
        if (!elements.listsView.classList.contains("hidden")) {
            renderCategoryOptions(categories);
        }
    } catch (error) {
        if (error.message === "Unauthorized") {
            clearSession();
            setView();
            setText(elements.authStatus, "Session expired. Sign in again.");
            return;
        }
        setText(elements.categoryStatus, error.message);
    }
}

async function loadExpenseLists() {
    if (!state.token) {
        renderExpenseLists([]);
        elements.expenseListTotal.innerHTML = "";
        return;
    }

    try {
        const overview = await request("/expense-lists/all");
        renderExpenseLists(overview.expenseLists || []);
        elements.expenseListTotal.innerHTML = `
            <span>Total Amount</span>
            <span>${escapeHtml(formatAmount(overview.totalAmount || 0))}</span>
        `;
        setText(elements.expenseListStatus, "");
    } catch (error) {
        if (error.message === "Unauthorized") {
            clearSession();
            setView();
            setText(elements.authStatus, "Session expired. Sign in again.");
            return;
        }
        setText(elements.expenseListStatus, error.message);
    }
}

async function handleExpenseListSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const isEditing = state.editingExpenseListId !== null;
    setText(elements.expenseListStatus, "Saving...");

    const payload = Object.fromEntries(new FormData(form).entries());
    payload.amount = Number(payload.amount);
    payload.categoryId = Number(payload.categoryId);

    try {
        if (!isEditing) {
            await request("/expense-lists/add", {
                method: "POST",
                body: JSON.stringify(payload),
            });
            setText(elements.expenseListStatus, "Expense list created successfully.");
        } else {
            await request(`/expense-lists/${state.editingExpenseListId}`, {
                method: "PUT",
                body: JSON.stringify(payload),
            });
            setText(elements.expenseListStatus, "Expense list updated.");
        }
        resetForm(form);
        setExpenseListFormMode(null);
        await loadExpenseLists();
    } catch (error) {
        setText(elements.expenseListStatus, error.message);
    }
}

async function handleLogin(event) {
    event.preventDefault();
    const form = event.currentTarget;
    setText(elements.authStatus, "Signing in...");

    const payload = Object.fromEntries(new FormData(form).entries());

    try {
        const response = await request("/auth/login", {
            method: "POST",
            body: JSON.stringify(payload),
        });
        persistSession(response);
        setView();
        setText(elements.authStatus, response.message || "Signed in.");
        resetForm(form);
        await loadCategories();
        await loadExpenseLists();
    } catch (error) {
        setText(elements.authStatus, error.message);
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const form = event.currentTarget;
    setText(elements.authStatus, "Creating account...");

    const payload = Object.fromEntries(new FormData(form).entries());

    try {
        const response = await request("/auth/register", {
            method: "POST",
            body: JSON.stringify(payload),
        });
        setMode("login");
        setText(elements.authStatus, response.message || "Account created.");
        resetForm(form);
    } catch (error) {
        setText(elements.authStatus, error.message);
    }
}

async function handleCategorySubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const successMessage = state.editingCategoryId === null ? "Category added." : "Category updated.";
    setText(elements.categoryStatus, "Saving...");

    const payload = Object.fromEntries(new FormData(form).entries());

    try {
        if (state.editingCategoryId === null) {
            await request("/categories/add", {
                method: "POST",
                body: JSON.stringify(payload),
            });
        } else {
            await request(`/categories/${state.editingCategoryId}`, {
                method: "PUT",
                body: JSON.stringify(payload),
            });
        }
        resetForm(form);
        setCategoryFormMode(null);
        await loadCategories();
        setText(elements.categoryStatus, successMessage);
    } catch (error) {
        setText(elements.categoryStatus, error.message);
    }
}

elements.tabs.forEach((tab) => {
    tab.addEventListener("click", () => setMode(tab.dataset.mode));
});

elements.dashboardTabs.forEach((tab) => {
    tab.addEventListener("click", () => setDashboardView(tab.dataset.dashboardView));
});

elements.loginForm.addEventListener("submit", handleLogin);
elements.registerForm.addEventListener("submit", handleRegister);
elements.categoryForm.addEventListener("submit", handleCategorySubmit);
elements.expenseListForm.addEventListener("submit", handleExpenseListSubmit);
elements.categoryCancelButton.addEventListener("click", () => {
    resetForm(elements.categoryForm);
    setCategoryFormMode(null);
});
elements.expenseListCancelButton.addEventListener("click", () => {
    resetForm(elements.expenseListForm);
    setExpenseListFormMode(null);
});
elements.logoutButton.addEventListener("click", () => {
    clearSession();
    setView();
    setText(elements.authStatus, "Logged out.");
});

(async () => {
    await primeCsrf();
    setMode(state.mode);
    setView();

    if (state.token) {
        await loadCategories();
        await loadExpenseLists();
    }
})();
