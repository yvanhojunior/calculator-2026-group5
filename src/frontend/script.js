const modeSelect = document.getElementById("mode")
const scenes = document.querySelectorAll(".scene")

modeSelect.addEventListener("change", () => {

    scenes.forEach(s => s.classList.remove("active"))

    document
        .getElementById(modeSelect.value)
        .classList.add("active")

})

const display = document.querySelectorAll(".display")
const buttons = document.querySelectorAll(".grid button")

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

function getUnitsForType(type) {
    return Object.keys(CONVERSIONS[type] || {})
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

buttons.forEach(btn => {

    btn.addEventListener("click", () => {

        const value = btn.innerText

        if(value === "="){
            console.log('Calcul en cours...')
            console.log(`Expression à évaluer: ${display[0].innerText}`)
            // Make API call there
            // Exceptions to handle:
            // - Invalid expressions (e.g., "5++2", "3*/4")
            // - Division by zero (e.g., "10/0")
            // - Syntax errors (e.g., "2(3+4)", "5..2")
            
            // Replace the op symbols with their JavaScript equivalents
            display[0].innerText = display[0].innerText.replace(/×/g, "*").replace(/÷/g, "/").replace(/−/g, "-")
            console.log(display[0].innerText)
            display[0].innerText = eval(display[0].innerText)
        }
        else if (btn.classList.contains("digit") || btn.classList.contains("op") || value === "."){
            display[0].innerText = display[0].innerText !== "0" ? display[0].innerText + value : value
        }
        else if (btn.classList.contains("light")){
            switch (btn.dataset.fn) {
                case "clear":
                    display[0].innerText = "0"
                    break
                case "opposite":
                    display[0].innerText = parseFloat(display[0].innerText) * -1
                    break
                case "percent":
                    display[0].innerText = parseFloat(display[0].innerText) / 100
                    break
            }
        }

    })

})
