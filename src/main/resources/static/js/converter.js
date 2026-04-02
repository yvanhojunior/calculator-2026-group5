const units = {
    length: ["meters", "kilometers", "centimeters", "decimeters",
        "millimeters", "feet", "inches", "miles", "yards", "nauticalmiles"],
    weight:["kilograms", "grams", "milligrams", "pounds", "ounces", "tonnes"],
    temperature: ["celsius", "fahrenheit", "kelvin"],
    area: ["square_meter", "square_kilometer", "square_centimeter", "square_millimeter","square_mile", "square_yard", "square_foot", "square_inch"],
    volume: ["liter", "milliliter", "cubic_meter", "cubic_centimeter", "cubic_millimeter", "gallon", "quart", "pint", "cup", "fluid_ounce"],
    speed: ["meter_per_second", "kilometer_per_hour", "mile_per_hour", "feet_per_second", "knot"]
};

let currentCategory = 'length';

/**
 * Format a unit name for display.
 * Example: square_meter -> Square Meter
 */
function formatUnitLabel(unit) {
    return unit
        .replaceAll('_', ' ')
        .replace(/\b\w/g, char => char.toUpperCase());
}

/**
 * Update the unit dropdowns according to the selected category.
 */
function updateUnits(category) {
    currentCategory = category || currentCategory;

    const fromSelect = document.getElementById('from-unit');
    const toSelect = document.getElementById('to-unit');

    if (!fromSelect || !toSelect) return;
    if (!units[currentCategory]) return;

    fromSelect.innerHTML = '';
    toSelect.innerHTML = '';

    units[currentCategory].forEach(unit => {
        const fromOption = document.createElement('option');
        fromOption.value = unit;
        fromOption.textContent = formatUnitLabel(unit);
        fromSelect.appendChild(fromOption);

        const toOption = document.createElement('option');
        toOption.value = unit;
        toOption.textContent = formatUnitLabel(unit);
        toSelect.appendChild(toOption);
    });

    if (toSelect.options.length > 1) {
        toSelect.selectedIndex = 1;
    }

    // Clear fields and messages when category changes
    const errorDiv = document.getElementById('converter-error');
    const valueInput = document.getElementById('converter-value');
    const resultInput = document.getElementById('converter-result');

    if (errorDiv) {
        errorDiv.className = 'eq-result';
        errorDiv.innerHTML = '';
    }

    if (valueInput) valueInput.value = '';
    if (resultInput) resultInput.value = '';
}

/**
 * Handle category selection from category buttons.
 */
function selectCategory(category, button) {
    currentCategory = category;
    updateUnits(category);

    document.querySelectorAll('.cat-tab').forEach(tab => {
        tab.classList.remove('active');
    });

    if (button) {
        button.classList.add('active');
    }
}

/**
 * Swap units and values.
 */
function switchUnits() {
    const fromSelect = document.getElementById('from-unit');
    const toSelect = document.getElementById('to-unit');
    const valueInput = document.getElementById('converter-value');
    const resultInput = document.getElementById('converter-result');

    if (!fromSelect || !toSelect || !valueInput || !resultInput) return;

    const tempUnit = fromSelect.value;
    fromSelect.value = toSelect.value;
    toSelect.value = tempUnit;

    const tempValue = valueInput.value;
    valueInput.value = resultInput.value;
    resultInput.value = tempValue;
}

/**
 * Convert units in both directions depending on the filled field.
 */
async function convertUnits() {
    const fromValue = document.getElementById('converter-value').value.trim();
    const toValue = document.getElementById('converter-result').value.trim();

    const fromUnit = document.getElementById('from-unit').value;
    const toUnit = document.getElementById('to-unit').value;

    const errorDiv = document.getElementById('converter-error');
    const valueInput = document.getElementById('converter-value');
    const resultInput = document.getElementById('converter-result');

    errorDiv.className = 'eq-result';
    errorDiv.innerHTML = '';

    let value;
    let from;
    let to;
    let targetField;

    // No field filled
    if (!fromValue && !toValue) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Enter a value in one field';
        return;
    }

    // Both fields filled
    if (fromValue && toValue) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Fill only one field';
        return;
    }

    // Determine conversion direction
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
        errorDiv.innerHTML = 'Invalid number';
        return;
    }

    // Same unit: no API call needed
    if (from === to) {
        targetField.value = value;
        errorDiv.className = 'eq-result eq-success';
        errorDiv.innerHTML = 'Conversion successful';
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
            errorDiv.innerHTML = 'Conversion successful';
        } else {
            errorDiv.className = 'eq-result eq-error';
            errorDiv.innerHTML = data.error || 'Conversion failed';
        }
    } catch (error) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = error.message;
    }
}

/**
 * Initialize converter on page load.
 */
document.addEventListener('DOMContentLoaded', () => {
    updateUnits('length');
});