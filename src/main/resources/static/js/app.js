/**
 * Frontend logic for the web-based calculator GUI.
 *
 * The UI collects a list of numbers and an operation, sends them to the
 * REST endpoint POST /api/calculate, and displays the result.
 */
const app = (() => {
    // ── state ──────────────────────────────────────────────────────────────
    let numbers   = [];
    let operation = null;

    // ── DOM helpers ────────────────────────────────────────────────────────
    const expressionEl = document.getElementById('expression');
    const resultEl     = document.getElementById('result');
    const errorEl      = document.getElementById('error');
    const historyEl    = document.getElementById('history');
    const numberInput  = document.getElementById('numberInput');

    /** Show an error message and clear it after 3 s. */
    function showError(msg) {
        errorEl.textContent = msg;
        setTimeout(() => { errorEl.textContent = ''; }, 3000);
    }

    /** Update the expression display based on current numbers and operation. */
    function updateDisplay() {
        const opSymbols = { plus: '+', minus: '−', times: '×', divides: '÷' };
        const sep = operation ? ` ${opSymbols[operation]} ` : ' , ';
        expressionEl.textContent = numbers.length ? numbers.join(sep) : '';
    }

    /** Highlight the active operation button. */
    function highlightOp(op) {
        document.querySelectorAll('.btn-op').forEach(btn => btn.classList.remove('active'));
        if (op) {
            const opNames = { plus: '+', minus: '−', times: '×', divides: '÷' };
            document.querySelectorAll('.btn-op').forEach(btn => {
                if (btn.textContent.trim() === opNames[op]) {
                    btn.classList.add('active');
                }
            });
        }
    }

    /** Append an entry to the history list. */
    function addHistory(expression, result) {
        const li = document.createElement('li');
        li.innerHTML =
            `<span class="hist-expr">${expression}</span>` +
            `<span class="hist-result">= ${result}</span>`;
        historyEl.prepend(li);
    }
    
    /** Normalize API result payload to a user-friendly string. */
    function formatApiResult(result) {
        if (result === null || result === undefined) {
            return '';
        }

        if (typeof result === 'object') {
            if ('value' in result) {
                return String(result.value);
            }

            try {
                return JSON.stringify(result);
            } catch {
                return String(result);
            }
        }

        return String(result);
    }

    // ── public API ─────────────────────────────────────────────────────────

    /** Add the current input value to the numbers list. */
    function addNumber() {
        const raw = numberInput.value.trim();
        if (raw === '' || !/^-?\d+$/.test(raw)) {
            showError('Please enter a valid integer (no decimals).');
            return;
        }
        numbers.push(parseInt(raw, 10));
        numberInput.value = '';
        numberInput.focus();
        updateDisplay();
    }

    /** Set the arithmetic operation. */
    function setOperation(op) {
        operation = op;
        highlightOp(op);
        updateDisplay();
    }

    /** Send the expression to the backend and display the result. */
    async function calculate() {
        if (numbers.length === 0) {
            showError('Add at least one number first.');
            return;
        }
        if (!operation) {
            showError('Select an operation first.');
            return;
        }
        if (numbers.length < 2) {
            showError('Add at least two numbers to compute an expression.');
            return;
        }

        try {
            const response = await fetch('/api/calculate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ operation, numbers }),
            });

            const data = await response.json();

            if (!response.ok) {
                showError(data.error ?? 'Failed to calculate expression. Please try again.');
                return;
            }

            const displayResult = formatApiResult(data.result);     // We handle various result formats (to avoid showing [object Object])
            resultEl.textContent = displayResult;
            expressionEl.textContent = data.expression;
            addHistory(data.expression, displayResult);

            // Reset for next expression
            numbers   = [];
            operation = null;
            highlightOp(null);
        } catch (err) {
            showError('Network error – is the server running?');
            console.error(err);
        }
    }

    /** Clear the current expression and reset the UI. */
    function clear() {
        numbers   = [];
        operation = null;
        numberInput.value     = '';
        expressionEl.textContent = '';
        resultEl.textContent  = '0';
        errorEl.textContent   = '';
        highlightOp(null);
    }

    // Allow pressing Enter in the number input to add the number
    numberInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') addNumber();
    });

    return { addNumber, setOperation, calculate, clear };
})();
