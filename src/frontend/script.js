const modeSelect = document.getElementById("mode")
const scenes = document.querySelectorAll(".scene")

modeSelect.addEventListener("change", () => {

    scenes.forEach(s => s.classList.remove("active"))

    document
        .getElementById(modeSelect.value)
        .classList.add("active")

})

const display = document.getElementById("display")
const buttons = document.querySelectorAll(".grid button")

buttons.forEach(btn => {

    btn.addEventListener("click", () => {

        const value = btn.innerText

        if(value === "="){
            display.value = eval(display.value)
        }
        else{
            display.value += value
        }

    })

})
