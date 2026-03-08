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

console.log(display)
console.log(buttons.length)

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
        "ounces": 28.3495?
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

buttons.forEach(btn => {

    btn.addEventListener("click", () => {

        console.log(`Valeur: ${btn.innerText}`)

        const value = btn.innerText

        if(value === "="){
            console.log('Calcul en cours...')
            console.log(`Expression à évaluer: ${display[0].innerText}`)
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
