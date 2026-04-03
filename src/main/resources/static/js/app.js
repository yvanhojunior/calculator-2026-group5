/**
 * Frontend logic for the web-based calculator GUI.
 *
 * The UI collects a list of numbers and an operation, sends them to the
 * REST endpoint POST /api/calculate, and displays the result.
 */
const app = (() => {
    // ── state ──────────────────────────────────────────────────────────────
    let expression_to_evaluate = [];
    let parenthesis_stack = [];

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
            parenthesis_stack = [];
            activeExpression.textContent = '';
            activeResult.textContent = '0';
        }
    });

    /**
     * Handle button clicks for digits, operators, and the equals sign.
     */

    arithmeticButtons.forEach(btn => {

        btn.addEventListener("click", () => {

            console.log(`Button clicked: ${btn.innerText}`)

            const value = btn.value

            if (btn.classList.contains('light')) {
                switch (btn.innerText) {
                    case "+/-":

                    break;
                    case "%":

                    break;
                    case "⌫":

                    break;
                    case "C":
                        break;
                    default:
                        console.warn(`Unhandled light button: ${btn.innerText}`);
                }

            } else {
                if (btn.classList.contains('digit') || btn.classList.contains('op')) {
                    expression_to_evaluate.push(value);
                }
                if (btn.classList.contains('sct')) {
                    // Handle scientific calculator buttons (e.g., sin, cos, etc.)
                    expression_to_evaluate.push(value+'('); // Assume scientific functions are followed by an opening parenthesis
                    parenthesis_stack.push(')'); // Assume scientific functions require parentheses
                }
                expression = expression_to_evaluate.join('') + parenthesis_stack.join('');
                activeResult.textContent = expression;
                // activeResult.textContent = activeResult.textContent === '0' ? value : activeResult.textContent + value;
            }
            
        })

    })

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
        return expression_to_evaluate.map(token => {
            switch (token) {
                case '×': return '*';
                case '÷': return '/';
                default: return token;
            }
        }).join('') + parenthesis_stack.join('');
    }

    // ── public API ─────────────────────────────────────────────────────────

    function clearDisplay() {
        activeExpression.textContent = '';
        activeResult.textContent = "0";
        expression_to_evaluate = [];
        parenthesis_stack = [];
    }

    /** Send the expression to the backend and display the result. */
    async function calculate() {
        /*
        if (expression_to_evaluate.length === 0) {
            return;
        }
        if (expression_to_evaluate.length < 2) {
            showError('Add at least two numbers to compute an expression.');
            return;
        }
        */

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
            expression_to_evaluate = [];
            parenthesis_stack = [];
        } catch (err) {
            showError('Network error – is the server running?');
            console.error(err);
        }
    }


    return { calculate, clearDisplay };
})();

// Expose app globally for inline HTML onclick handlers.
window.app = app;
