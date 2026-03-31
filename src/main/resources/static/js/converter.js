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
    const fromValue = document.getElementById('converter-value').value;
    const toValue = document.getElementById('converter-result').value;

    const fromUnit = document.getElementById('from-unit').value;
    const toUnit = document.getElementById('to-unit').value;

    const errorDiv = document.getElementById('converter-error');
    const valueInput = document.getElementById('converter-value');
    const resultInput = document.getElementById('converter-result');

    errorDiv.className = 'eq-result';
    errorDiv.innerHTML = '';

    let value, from, to, targetField;

    // Cas 1 : aucun champ rempli
    if (!fromValue && !toValue) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Enter a value in one field';
        return;
    }

    // Cas 2 : les deux champs remplis
    if (fromValue && toValue) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = ' Fill only one field';
        return;
    }

    // Cas normal : conversion dans le bon sens
    if (fromValue) {
        value = parseFloat(fromValue);
        from = fromUnit;
        to = toUnit;
        targetField = resultInput;
    } else {
        value = parseFloat(toValue);
        from = toUnit;
        to = fromUnit;
        targetField = valueInput;
    }

    if (isNaN(value)) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = ' Invalid number';
        return;
    }

    try {
        const response = await fetch('/api/convert', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ value, from, to })
        });

        const data = await response.json();

        if (response.ok && data.status === 'SUCCESS') {
            targetField.value = data.value;
            errorDiv.className = 'eq-result eq-success';
            errorDiv.innerHTML = `Conversion successful`;
        } else {
            errorDiv.className = 'eq-result eq-error';
            errorDiv.innerHTML =   (data.error || 'Conversion failed');
        }
    } catch (error) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML =  error.message;
    }
}

// Initialize units on page load
document.addEventListener('DOMContentLoaded', () => updateUnits('length'));