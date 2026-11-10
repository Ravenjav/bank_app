
const API_URL = "/api/applications";

const table = document.getElementById("applicationsTable");
const createDialog = document.getElementById("createDialog");
const calculateDialog = document.getElementById("calculateDialog");
const createForm = document.getElementById("createForm");
const notification = document.getElementById("notification");
const createFormError = document.getElementById("createFormError");

let selectedApplicationId = null;

async function apiRequest(url, options = {}) {
    const response = await fetch(url, options);

    if (!response.ok) {
        const error = await response.json().catch(() => null);
        throw new Error(
            error?.message ?? `Ошибка HTTP ${response.status}`
        );
    }

    return response.json();
}

function showNotification(message, isError = false) {
    notification.textContent = message;
    notification.className =
        `notification ${isError ? "error" : "success"}`;
    notification.hidden = false;
}

async function loadApplications() {
    try {
        const applications = await apiRequest(API_URL);
        renderApplications(applications);
    } catch (error) {
        showNotification(error.message, true);
    }
}

function renderApplications(applications) {
    table.replaceChildren();

    if (applications.length === 0) {
        const row = table.insertRow();
        const cell = createCell(row, "Заявок пока нет");
        cell.colSpan = 6;
        return;
    }

    applications.forEach(app => {
        const row = document.createElement("tr");

        createCell(row, app.id);
        createCell(row, app.clientName);
        createCell(row, Number(app.amount).toLocaleString("ru-RU", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }));
        createCell(row, app.riskScore);
        createCell(row, formatDate(app.createdAt));

        const actionCell = document.createElement("td");

        if (app.riskScore === null) {
            const button = document.createElement("button");
            button.className = "btn btn-primary btn-small";
            button.textContent = "Рассчитать";
            button.addEventListener("click", () => {
                selectedApplicationId = app.id;
                document.getElementById("calculateMessage")
                    .textContent =
                    `Рассчитать рейтинг заявки №${app.id}?`;
                calculateDialog.showModal();
            });
            actionCell.appendChild(button);
        } else {
            actionCell.textContent = "Рассчитан";
        }

        row.appendChild(actionCell);
        table.appendChild(row);
    });
}

function formatDate(value) {
    return value
        ? new Date(value).toLocaleString("ru-RU", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit"
        })
        : "—";
}

async function createApplication(event) {
    event.preventDefault();

    createFormError.hidden = true;
    createFormError.textContent = "";

    if (!createForm.reportValidity()) {
        return;
    }

    const data = {
        clientId: Number(document.getElementById("clientId").value),
        amount: Number(document.getElementById("amount").value)
    };

    try {
        await apiRequest(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        createDialog.close();
        createForm.reset();

        showNotification("Заявка успешно создана");
        await loadApplications();
    } catch (error) {
        createFormError.textContent = error.message;
        createFormError.hidden = false;
    }
}

async function calculateScore() {
    if (selectedApplicationId === null) {
        return;
    }

    try {
        await apiRequest(
            `${API_URL}/${selectedApplicationId}/calculate`,
            { method: "POST" }
        );

        calculateDialog.close();
        selectedApplicationId = null;

        showNotification("Рейтинг успешно рассчитан");
        await loadApplications();
    } catch (error) {
        calculateDialog.close();
        showNotification(error.message, true);
    }
}

function createCell(row, value) {
    const cell = document.createElement("td");
    cell.textContent = value ?? "—";
    row.appendChild(cell);
    return cell;
}

document.getElementById("createButton")
    .addEventListener("click", () => createDialog.showModal());

document.getElementById("cancelCreate")
    .addEventListener("click", () => createDialog.close());

document.getElementById("cancelCalculate")
    .addEventListener("click", () => calculateDialog.close());

document.getElementById("refreshButton")
    .addEventListener("click", loadApplications);

document.getElementById("confirmCalculate")
    .addEventListener("click", calculateScore);

createForm.addEventListener("submit", createApplication);

loadApplications();
