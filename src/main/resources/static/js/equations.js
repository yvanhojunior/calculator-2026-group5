function showTab(tab, event) {
    // Afficher / cacher les sections
    document.getElementById('tab-calculator').style.display = tab === 'calculator' ? 'block' : 'none';
    document.getElementById('tab-equations').style.display  = tab === 'equations'  ? 'block' : 'none';
    document.getElementById('tab-converter').style.display  = tab === 'converter'  ? 'block' : 'none';

    // Gestion du bouton actif
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));

    if (event && event.target) {
        event.target.classList.add('active');
    }

    // Initialiser les unités quand on ouvre le converter
    if (tab === 'converter' && typeof updateUnits === 'function') {
        updateUnits();
    }
}

async function solveEquation() {
    const equation  = document.getElementById('equation').value;
    const resultDiv = document.getElementById('result-single');

    try {
        const response = await fetch('/api/solve', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ equation })
        });
        const data = await response.json();
        displayEquationResult(resultDiv, data);
    } catch (error) {
        resultDiv.className  = 'eq-result eq-error';
        resultDiv.innerHTML  = 'Error: ' + error.message;
    }
}

async function solveSystem() {
    const eq1       = document.getElementById('eq1').value;
    const eq2       = document.getElementById('eq2').value;
    const resultDiv = document.getElementById('result-system');

    try {
        const response = await fetch('/api/solve/system', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ equations: [eq1, eq2] })
        });
        const data = await response.json();
        displayEquationResult(resultDiv, data);
    } catch (error) {
        resultDiv.className = 'eq-result eq-error';
        resultDiv.innerHTML = 'Error: ' + error.message;
    }
}

function displayEquationResult(div, data) {
    if (data.type === 'UNIQUE') {
        div.className   = 'eq-result eq-success';
        const solutions = Object.entries(data.solutions)
            .map(([k, v]) => `${k} = ${v}`)
            .join(', ');
        div.innerHTML = 'Solution: ' + solutions;
    } else if (data.type === 'NO_SOLUTION') {
        div.className = 'eq-result eq-error';
        div.innerHTML = data.message;
    } else if (data.type === 'INFINITE_SOLUTIONS') {
        div.className = 'eq-result eq-warning';
        div.innerHTML = data.message;
    } else if (data.type === 'SYNTAX_ERROR') {
        div.className = 'eq-result eq-error';
        div.innerHTML = data.error;
    }
}