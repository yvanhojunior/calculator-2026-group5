const units = {
    length: ["meters", "kilometers", "centimeters", "decimeters",
             "millimeters", "feet", "inches", "miles", "yards", "nauticalmiles"],
    weight: ["kilograms", "grams", "milligrams", "pounds", "ounces", "tonnes"],
    temperature: ["celsius", "fahrenheit", "kelvin"]
};

let currentCategory = 'length';

function updateUnits(category) {
    currentCategory = category || currentCategory;

    const fromSelect = document.getElementById('from-unit');
    const toSelect   = document.getElementById('to-unit');

    if (!fromSelect || !toSelect) return;

    fromSelect.innerHTML = '';
    toSelect.innerHTML   = '';

    units[currentCategory].forEach(unit => {
        fromSelect.innerHTML += `<option value="${unit}">${unit}</option>`;
        toSelect.innerHTML   += `<option value="${unit}">${unit}</option>`;
    });

    // Select different default units
    if (toSelect.options.length > 1) {
        toSelect.selectedIndex = 1;
    }

    // Clear results when category changes
    const errorDiv    = document.getElementById('converter-error');
    const resultInput = document.getElementById('converter-result');
    if (errorDiv)    { errorDiv.className = 'eq-result'; errorDiv.innerHTML = ''; }
    if (resultInput) { resultInput.value = ''; }
}

function switchUnits() {
    const fromSelect  = document.getElementById('from-unit');
    const toSelect    = document.getElementById('to-unit');
    const fromValue   = document.getElementById('converter-value').value;
    const toValue     = document.getElementById('converter-result').value;

    const tempFrom    = fromSelect.value;
    fromSelect.value  = toSelect.value;
    toSelect.value    = tempFrom;

    document.getElementById('converter-value').value  = toValue;
    document.getElementById('converter-result').value = fromValue;
}

async function convertUnits() {
    const value       = parseFloat(document.getElementById('converter-value').value);
    const from        = document.getElementById('from-unit').value;
    const to          = document.getElementById('to-unit').value;
    const errorDiv    = document.getElementById('converter-error');
    const resultInput = document.getElementById('converter-result');

    errorDiv.className = 'eq-result';
    errorDiv.innerHTML = '';
    resultInput.value  = '';

    if (isNaN(value)) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = '❌ Please enter a valid number';
        return;
    }

    try {
        const response = await fetch('/api/convert', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ value, from, to })
        });

        const data = await response.json();

        if (response.ok && data.status === 'SUCCESS') {
            resultInput.value  = data.value;
            errorDiv.className = 'eq-result eq-success';
            errorDiv.innerHTML = `✅ ${value} ${from} = ${data.value} ${to}`;
        } else {
            errorDiv.className = 'eq-result eq-error';
            errorDiv.innerHTML = '❌ ' + (data.error || 'Conversion failed');
        }
    } catch (error) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = '❌ Error: ' + error.message;
    }
}

// Initialize units on page load
document.addEventListener('DOMContentLoaded', () => updateUnits('length'));