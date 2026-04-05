async function loadCurrencyList() {
    if (window.currency_list) {
        return window.currency_list;
    }

    const response = await fetch('/conversion_list.json');
    if (!response.ok) {
        throw new Error('Unable to load conversion list');
    }

    const data = await response.json();
    const currencies = data.currencies || [];
    const byCode = {};
    const lastUpdated = data.last_updated;
    const nextUpdate = data.next_update;

    let current_time = Math.floor(Date.now() / 1000);   // Current time in seconds
    if (current_time < nextUpdate && current_time >= lastUpdated) {
        for (const currency of currencies) {
            byCode[currency.code] = {
                name: currency.name,
                rate: currency.rate,
                symbol: currency.symbol
            };
        }
    } else if (current_time >= nextUpdate) {
        // Data is outdated, need to make a API call
    }

    window.currency_list = byCode;
    window.dispatchEvent(new CustomEvent('currenciesLoaded'));
    return byCode;
}

function switchCurrencies() {
    const fromInput = document.getElementById('from-currency');
    const toInput = document.getElementById('to-currency');
    if (!fromInput || !toInput) return;

    const temp = fromInput.value;
    fromInput.value = toInput.value;
    toInput.value = temp;
}

async function convertCurrency() {
    const amountInput = document.getElementById('currency-amount'); // Input
    const fromInput = document.getElementById('from-currency'); // Select
    const toInput = document.getElementById('to-currency'); // Select

    const errorDiv = document.getElementById('currency-error');
    const resultDiv = document.getElementById('currency-result');
    const resultValue = document.getElementById('currency-result-value');
    const resultDisplay = document.getElementById('currency-result-display');

    if (!amountInput || !fromInput || !toInput || !errorDiv || !resultDiv) {
        return;
    }

    let amount;
    let from;
    let to;

    errorDiv.className = 'eq-result';
    errorDiv.innerHTML = '';

    if (!amountInput.value.trim() || !fromInput.value.trim() || !toInput.value.trim()) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Please fill in all fields';
        return;
    }
    if (isNaN(amountInput.value.trim())) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Invalid amount';
        return;
    }

    amount = parseFloat(amountInput.value.trim());
    from = fromInput.value.trim().toUpperCase();
    to = toInput.value.trim().toUpperCase();

    console.log('Converting', amount, from, 'to', to);

    const currencyList = await loadCurrencyList();

    console.log('Loaded currency list:', currencyList);

    if (!currencyList[from] || !currencyList[to]) {
        console.log('Invalid currency code:', from, to);
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Invalid currency code';
        return;
    }

    const fromRate = currencyList[from].rate;
    const toRate = currencyList[to].rate;
    const convertedAmount = (amount / fromRate) * toRate;

    console.log('Converted amount:', convertedAmount);
    console.log('From symbol:', currencyList[from].symbol, 'To symbol:', currencyList[to].symbol);

    const formattedValue = `${currencyList[to].symbol || ''}${convertedAmount.toFixed(4)}`;
    if (resultValue) {
        resultValue.value = formattedValue;
    }
    if (resultDisplay) {
        resultDisplay.textContent = formattedValue;
    }
    resultDiv.className = 'eq-result eq-success';
    if (!resultDisplay) {
        resultDiv.innerHTML = `Converted amount: ${formattedValue}`;
    }
    resultDiv.hidden = false;
}

document.addEventListener('DOMContentLoaded', () => {
    loadCurrencyList().catch(() => {
        const errorDiv = document.getElementById('currency-error');
        if (errorDiv) {
            errorDiv.className = 'eq-result eq-error';
            errorDiv.innerHTML = 'Failed to load local currency list';
        }
    });
});