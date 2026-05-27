const state = {
    mode: "login",
    csrfToken: "",
    token: localStorage.getItem("expenseTrackerToken") || "",
    user: JSON.parse(localStorage.getItem("expenseTrackerUser") || "null"),
};

const elements = {
    sessionPill: document.getElementById("session-pill"),
    authPanel: document.getElementById("auth-panel"),
    dashboardPanel: document.getElementById("dashboard-panel"),
    authTitle: document.getElementById("auth-title"),
    authStatus: document.getElementById("auth-status"),
    categoryStatus: document.getElementById("category-status"),
    categoryList: document.getElementById("category-list"),
    loginForm: document.getElementById("login-form"),
    registerForm: document.getElementById("register-form"),
    categoryForm: document.getElementById("category-form"),
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
        renderCategories([]);
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
    if (!items.length) {
        elements.categoryList.innerHTML = '<li class="empty">No categories yet.</li>';
        return;
    }

    elements.categoryList.innerHTML = items
        .map(
            (item) => `
                <li>
                    <span class="category-name">${escapeHtml(item.categoryName)}</span>
                    <span class="message">#${item.id}</span>
                </li>
            `
        )
        .join("");
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
    setText(elements.categoryStatus, "Saving...");

    const payload = Object.fromEntries(new FormData(form).entries());

    try {
        await request("/categories/add", {
            method: "POST",
            body: JSON.stringify(payload),
        });
        resetForm(form);
        await loadCategories();
        setText(elements.categoryStatus, "Category added.");
    } catch (error) {
        setText(elements.categoryStatus, error.message);
    }
}

elements.tabs.forEach((tab) => {
    tab.addEventListener("click", () => setMode(tab.dataset.mode));
});

elements.loginForm.addEventListener("submit", handleLogin);
elements.registerForm.addEventListener("submit", handleRegister);
elements.categoryForm.addEventListener("submit", handleCategorySubmit);
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
    }
})();
