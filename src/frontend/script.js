const modeSelect = document.getElementById("mode")
const scenes = document.querySelectorAll(".scene")

modeSelect.addEventListener("change", () => {

    scenes.forEach(s => s.classList.remove("active"))

    document
        .getElementById(modeSelect.value)
        .classList.add("active")

})

const standardDisplay = document.querySelector("#standard .display")
const scientificDisplay = document.querySelector("#scientific .display")
const buttons = document.querySelectorAll(".grid button")

function getActiveDisplay() {
    if (modeSelect.value === "scientific" && scientificDisplay) {
        return scientificDisplay
    }

    return standardDisplay
}

function handleScientificButton(fn, activeDisplay) {
    switch (fn) {
        case "sin":
            break
        case "cos":
            break
        case "tan":
            break
        case "pi":
            break
        case "delete":
            break
        case "log":
            break
        case "ln":
            break
        case "factorial":
            break
        case "inv":
            break
        case "pow10":
            break
        case "exp":
            break
        case "leftParen":
            break
        case "rightParen":
            break
        case "modulo":
            break
        case "power2":
            break
        case "sqrt":
            break
        case "powerY":
            break
        default:
            break
    }
}

const CONVERSIONS = {
    "length": {
        "kilometers": 1000,
        "meters": 1,
        "centimeters": 0.01,
        "millimeters": 0.001,
        "inches": 0.0254,
        "feet": 0.3048,
        "yards": 0.9144,
        "miles": 1609.34
    },
    "weight": {
        "kilograms": 1000,
        "grams": 1,
        "milligrams": 0.001,
        "pounds": 453.592,
        "ounces": 28.3495,
        "tons": 907184.74
    },
    "temperature": {
        "celsius": {
            toKelvin: (c) => c + 273.15,
            toFahrenheit: (c) => (c * 9/5) + 32
        },
        "fahrenheit": {
            toCelsius: (f) => (f - 32) * 5/9,
            toKelvin: (f) => ((f - 32) * 5/9) + 273.15
        },
        "kelvin": {
            toCelsius: (k) => k - 273.15,
            toFahrenheit: (k) => ((k - 273.15) * 9/5) + 32
        }
    },
    "volume": {
        "liters": 1,
        "milliliters": 0.001,
        "gallons (US)": 3.78541,
        "quarts": 0.946353,
        "pints": 0.473176,
        "cups": 0.24,
        "cubic meters": 1000
    }
}

const conversionTypeButtons = document.querySelectorAll("#metric .smcont .grid button[data-fn]")
const fromUnitsSelect = document.getElementById("fromUnits")
const toUnitsSelect = document.getElementById("toUnits")
const fromValueInput = document.getElementById("fromValue")
const toValueInput = document.getElementById("toValue")
const swapMetricsButton = document.querySelector("#metric button[data-fn='switchMetrics']")

function getActiveConversionType() {
    const selectedButton = document.querySelector("#metric .smcont .grid button.selected")
    return selectedButton ? selectedButton.dataset.fn : "length"
}

function getUnitsForType(type) {
    return Object.keys(CONVERSIONS[type] || {})
}

function convertTemperature(value, fromUnit, toUnit) {
    if (fromUnit === toUnit) {
        return value
    }

    let celsius

    // Convert from the source unit to Celsius, then from Celsius to the target unit
    if (fromUnit === "celsius") {
        celsius = value
    } else if (fromUnit === "fahrenheit") {
        celsius = (value - 32) * 5 / 9
    } else if (fromUnit === "kelvin") {
        celsius = value - 273.15
    }

    if (toUnit === "celsius") {
        return celsius
    }
    if (toUnit === "fahrenheit") {
        return (celsius * 9 / 5) + 32
    }
    if (toUnit === "kelvin") {
        return celsius + 273.15
    }
}

function convertMetricValue(type, value, fromUnit, toUnit) {
    if (type === "temperature") {
        return convertTemperature(value, fromUnit, toUnit)
    }

    const units = CONVERSIONS[type] || {}
    const fromFactor = units[fromUnit]
    const toFactor = units[toUnit]

    if (typeof fromFactor !== "number" || typeof toFactor !== "number") {
        return NaN
    }

    return value * fromFactor / toFactor
}

function formatConvertedValue(value) {
    if (!Number.isFinite(value)) {
        return ""
    }

    return Number(value.toPrecision(12)).toString()
}

function updateMetricConversion() {
    if (!fromUnitsSelect || !toUnitsSelect || !fromValueInput || !toValueInput) {
        return
    }

    const rawValue = fromValueInput.value.trim().replace(",", ".")

    if (rawValue === "") {
        toValueInput.value = ""
        return
    }

    const numericValue = Number(rawValue)

    if (!Number.isFinite(numericValue)) {
        toValueInput.value = ""
        return
    }

    const type = getActiveConversionType()
    const convertedValue = convertMetricValue(type, numericValue, fromUnitsSelect.value, toUnitsSelect.value)

    toValueInput.value = formatConvertedValue(convertedValue)
}

function populateMetricUnitSelects(type) {
    if (!fromUnitsSelect || !toUnitsSelect) {
        return
    }

    const units = getUnitsForType(type)

    fromUnitsSelect.innerHTML = ""
    toUnitsSelect.innerHTML = ""

    units.forEach((unit) => {
        fromUnitsSelect.add(new Option(unit, unit))
        toUnitsSelect.add(new Option(unit, unit))
    })

    if (units.length > 1) {
        toUnitsSelect.selectedIndex = 1
    }

    updateMetricConversion()
}

function setActiveConversionType(button) {
    conversionTypeButtons.forEach((btn) => btn.classList.remove("selected"))
    button.classList.add("selected")
    populateMetricUnitSelects(button.dataset.fn)
}

conversionTypeButtons.forEach((button) => {
    button.addEventListener("click", () => {
        setActiveConversionType(button)
    })
})

const initiallySelectedButton = document.querySelector("#metric .smcont .grid button.selected")

if (initiallySelectedButton) {
    setActiveConversionType(initiallySelectedButton)
}

if (swapMetricsButton && fromUnitsSelect && toUnitsSelect) {
    swapMetricsButton.addEventListener("click", () => {
        const fromUnit = fromUnitsSelect.value
        fromUnitsSelect.value = toUnitsSelect.value
        toUnitsSelect.value = fromUnit
        updateMetricConversion()
    })
}

if (fromValueInput) {
    fromValueInput.addEventListener("input", updateMetricConversion)
}

if (fromUnitsSelect) {
    fromUnitsSelect.addEventListener("change", updateMetricConversion)
}

if (toUnitsSelect) {
    toUnitsSelect.addEventListener("change", updateMetricConversion)
}

buttons.forEach(btn => {

    btn.addEventListener("click", () => {
        const activeDisplay = getActiveDisplay()

        if (!activeDisplay) {
            return
        }

        const value = btn.innerText

        if(value === "="){
            console.log('Calcul en cours...')
            console.log(`Expression à evaluer: ${activeDisplay.innerText}`)
            // Make API call there
            // Exceptions to handle:
            // - Invalid expressions (e.g., "5++2", "3*/4")
            // - Division by zero (e.g., "10/0")
            // - Syntax errors (e.g., "2(3+4)", "5..2")
            
            // Replace the op symbols with their JavaScript equivalents
            activeDisplay.innerText = activeDisplay.innerText.replace(/×/g, "*").replace(/÷/g, "/").replace(/−/g, "-")
            console.log(activeDisplay.innerText)
            activeDisplay.innerText = eval(activeDisplay.innerText)
        }
        else if (btn.classList.contains("digit") || btn.classList.contains("op") || value === "."){
            activeDisplay.innerText = activeDisplay.innerText !== "0" ? activeDisplay.innerText + value : value
        }
        else if (btn.classList.contains("sct")) {
            handleScientificButton(btn.dataset.fn, activeDisplay)
        }
        else if (btn.classList.contains("light")){
            switch (btn.dataset.fn) {
                case "clear":
                    activeDisplay.innerText = "0"
                    break
                case "opposite":
                    activeDisplay.innerText = parseFloat(activeDisplay.innerText) * -1
                    break
                case "percent":
                    activeDisplay.innerText = parseFloat(activeDisplay.innerText) / 100
                    break
            }
        }

    })

})
