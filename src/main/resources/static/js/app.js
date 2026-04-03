/**
 * Frontend logic for the web-based calculator GUI.
 *
 * The UI collects a list of numbers and an operation, sends them to the
 * REST endpoint POST /api/calculate, and displays the result.
 */
const app = (() => {
    // ── state ──────────────────────────────────────────────────────────────
    let numbers   = [];
    let operators = [];
    let pendingOperation = null;

    // ── DOM helpers ────────────────────────────────────────────────────────
    const arithmeticButtons =  document.querySelectorAll('button.digit, button.op, button.sct, button.light');
    const nightModeToggle = document.getElementById('night-mode-toggle');
    const languageToggle = document.getElementById('language-toggle');
    let activeExpression = document.getElementById('std_expression'); // Start with standard expression active
    let activeResult = document.getElementById('std_result'); // Start with standard result active
    let activeDisplay = document.getElementById('std_display'); // Start with standard display active
    let activeHistory = document.getElementById('std_history'); // Start with standard history active
    let activeError = document.getElementById('std_error'); // Start with standard error active

    /** Show an error message and clear it after 3 s. */
    function showError(msg) {
        if (!activeError) {
            return;
        }
        activeError.textContent = msg;
        setTimeout(() => {
            if (activeError) {
                activeError.textContent = '';
            }
        }, 3000);
    }

    /** Synchronize active references based on the current page. */
    function syncActiveRefs(pageName) {
        const isScientific = pageName === 'scientific';

        activeDisplay = isScientific
            ? document.getElementById('sct_display')
            : document.getElementById('std_display');

        activeExpression = isScientific
            ? document.getElementById('sct_expression')
            : document.getElementById('std_expression');

        activeResult = isScientific
            ? document.getElementById('sct_result')
            : document.getElementById('std_result');

        activeHistory = isScientific
            ? document.getElementById('sct_history')
            : document.getElementById('std_history');
        activeError = isScientific
            ? document.getElementById('sct_error')
            : document.getElementById('std_error');
    }

    // Initialize active references based on the default page
    syncActiveRefs('calculator');

    // Update active references when the page changes
    window.addEventListener('pageChanged', (event) => {
        const page = event.detail?.page;
        if (page === 'calculator' || page === 'scientific') {
            syncActiveRefs(page);
            expression_to_evaluate = [];
            activeExpression.textContent = '';
            activeResult.textContent = '0';
        }
    });


    /** Append an entry to the history list. */
    function addHistory(expression, result) {
        const li = document.createElement('li');
        li.innerHTML =
            `<span class="hist-expr">${expression}</span>` +
            `<span class="hist-result">= ${result}</span>`;
        if (activeHistory) {
            activeHistory.prepend(li);
        }
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

    /** Build an expression string using parser-friendly operator symbols. */
    function buildExpressionForApi() {
        const opSymbols = { plus: '+', minus: '-', times: '*', divides: '/' };
        let expression = String(numbers[0]);

        for (let i = 0; i < operators.length; i += 1) {
            expression += `${opSymbols[operators[i]]}${numbers[i + 1]}`;
        }

        return expression;
    }

    // ── public API ─────────────────────────────────────────────────────────

    /** Add the current input value to the numbers list. */
    function addNumber() {
        const raw = numberInput.value.trim();
        if (raw === '' || !/^-?\d+$/.test(raw)) {
            showError('Please enter a valid integer (no decimals).');
            return;
        }

        if (numbers.length > 0) {
            if (!pendingOperation) {
                showError('Select an operator before adding another number.');
                return;
            }
            operators.push(pendingOperation);
        }

        numbers.push(parseInt(raw, 10));
        pendingOperation = null;
        highlightOp(null);
        numberInput.value = '';
        numberInput.focus();
        updateDisplay();
    }

    /** Set (or replace) the next arithmetic operation. */
    function setOperation(op) {
        if (!numbers.length) {
            showError('Add a number first.');
            return;
        }

        pendingOperation = op;
        highlightOp(op);
        updateDisplay();
    }

    /** Send the expression to the backend and display the result. */
    async function calculate() {
        if (numbers.length === 0) {
            showError('Add at least one number first.');
            return;
        }
        if (numbers.length < 2) {
            showError('Add at least two numbers to compute an expression.');
            return;
        }
        if (pendingOperation) {
            showError('Add the next number to complete the expression.');
            return;
        }

        try {
            const expression = buildExpressionForApi();
            const response = await fetch(`/api/calculate?expression=${encodeURIComponent(expression)}`);

            const data = await response.json();

            if (!response.ok) {
                showError(data.error ?? 'Failed to calculate expression. Please try again.');
                return;
            }

            const displayResult = formatApiResult(data.result);     // We handle various result formats (to avoid showing [object Object])
            activeResult.textContent = displayResult;
            activeExpression.textContent = expression;
            addHistory(expression, displayResult);

            // Reset for next expression
            numbers   = [];
            operators = [];
            pendingOperation = null;
            highlightOp(null);
        } catch (err) {
            showError('Network error – is the server running?');
            console.error(err);
        }
    }

    /** Clear the current expression and reset the UI. */
    function clear() {
        numbers   = [];
        operators = [];
        pendingOperation = null;
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

// Expose app globally for inline HTML onclick handlers.
window.app = app;
