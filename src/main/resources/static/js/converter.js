const units = {
    length: {"meters": "Meters (m)", "kilometers": "Kilometers (km)", "centimeters": "Centimeters (cm)", "decimeters": "Decimeters (dm)",
        "millimeters": "Millimeters (mm)", "feet": "Feet (ft)", "inches": "Inches (in)", "miles": "Miles (mi)", "yards": "Yards (yd)", "nauticalmiles": "Nautical Miles (nmi)"},
    weight: {"kilograms": "Kilograms (kg)", "grams": "Grams (g)", "milligrams": "Milligrams (mg)", "pounds": "Pounds (lb)", "ounces": "Ounces (oz)", "tonnes": "Tonnes (t)"},
    temperature: {"celsius": "Celsius (°C)", "fahrenheit": "Fahrenheit (°F)", "kelvin": "Kelvin (K)"},
    area: {"square_meter": "Square Meters (m²)", "square_kilometer": "Square Kilometers (km²)", "square_centimeter": "Square Centimeters (cm²)", "square_millimeter": "Square Millimeters (mm²)","square_mile": "Square Miles (mi²)","square_yard":"Square Yards (yd²)","square_foot":"Square Feet (ft²)","square_inch":"Square Inches (in²)"},
    volume: {"liter":	"Liter (L)","milliliter":"Milliliter (mL)","cubic_meter":"Cubic Meters (m³)","cubic_centimeter":"Cubic Centimeters (cm³)","cubic_millimeter":"Cubic Millimeters (mm³)","gallon":"Gallons (gal)","quart":"Quarts (qt)","pint":"Pints (pt)","cup":"Cups","fluid_ounce":"Fluid Ounces (fl oz)"},
    speed: {"meter_per_second":"Meters per Second (m/s)","kilometer_per_hour":"Kilometers per Hour (km/h)","mile_per_hour":"Miles per Hour (mph)","feet_per_second":"Feet per Second (ft/s)","knot":"Knots"}
};

let currentCategory = 'length';

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

    Object.keys(units[currentCategory]).forEach(unit => {
        const fromOption = document.createElement('option');
        fromOption.value = unit;
        fromOption.textContent = units[currentCategory][unit];
        fromSelect.appendChild(fromOption);

        const toOption = document.createElement('option');
        toOption.value = unit;
        toOption.textContent = units[currentCategory][unit];
        toSelect.appendChild(toOption);
    });

    if (toSelect.options.length > 1) {
        toSelect.selectedIndex = 1;
    }

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

    if (!fromValue) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Enter a value in the field';
        return;
    } else {
        value = parseFloat(fromValue);
        from = fromUnit;
        to = toUnit;
        targetField = resultInput;
    }

    if (isNaN(value)) {
        errorDiv.className = 'eq-result eq-error';
        errorDiv.innerHTML = 'Invalid number';
        return;
    }

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