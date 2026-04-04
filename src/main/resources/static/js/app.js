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
    const ans_button = document.getElementById('ans_button');
    let currentPage = 'calculator'; // Default page
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
        window.currentPage = page;
        if (page === 'calculator' || page === 'scientific') {
            syncActiveRefs(page);
            expression_to_evaluate = [];
            parenthesis_stack = [];
            activeExpression.textContent = '';
            activeResult.textContent = '0';
        }
    });

    function getCurrentPage() {
        return window.currentPage;
    }

    ans_button.disabled = true;

    /**
     * Handle button clicks for digits, operators, and the equals sign.
     */

    arithmeticButtons.forEach(btn => {

        btn.addEventListener("click", () => {

            console.log(`Button clicked: ${btn.innerText}`)

            const value = btn.value

            if (btn.id === 'ans_button') {
                const lastAnswer = app.getLastAnswer();
                expression_to_evaluate.push(lastAnswer);
            }

            if (btn.classList.contains('digit') || btn.classList.contains('op')) {
                expression_to_evaluate.push(value);
            }
            if (btn.classList.contains('sct')) {
                if (!btn.hasAttribute('data-fn')) {
                    expression_to_evaluate.push(value);
                } else {
                    expression_to_evaluate.push(value+'(');
                    parenthesis_stack.push(')');
                }
            }
            activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
            return;
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
        result = expression_to_evaluate.map(token => {
            switch (token) {
                case '×': return '*';
                case '÷': return '/';
                default: return token;
            }
        }).join('') + parenthesis_stack.join('');
        if (result.startsWith('*') || result.startsWith('/') || result.startsWith('+')) {
            result = getLastAnswer() + result; // Prepend last answer to handle expressions starting with an operator
        }
        if (result.startsWith('-')) {
            result = '0' + result; // Prepend '0' to handle negative numbers at the start of the expression
        }
        return result;
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

        try {
            if (expression_to_evaluate.length === 0) {
                showError('Please enter an expression to calculate.');
                activeResult.textContent = '0';
                return;
            }

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

            const isScientific = getCurrentPage() === 'scientific';
            ans_button.disabled = isScientific ? false : true;

            // Reset for next expression
            expression_to_evaluate = [];
            parenthesis_stack = [];
        } catch (err) {
            showError('Network error – is the server running?');
            console.error(err);
        }
    }

    function deleteLastEntry() {
        if (expression_to_evaluate.length > 0) {
            const lastToken = expression_to_evaluate.pop();
            if (lastToken[lastToken.length - 1] === '(') {  // If the last token ends with '(', we also need to remove the corresponding ')' from the stack
                parenthesis_stack.pop();
            }
            if (expression_to_evaluate.length === 0 && parenthesis_stack.length === 0) {
                activeResult.textContent = '0';
            } else {
                activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('')
            };
        }
    }

    function changeSign() {
        // Handle sign change (e.g., toggle between positive and negative)
        if (expression_to_evaluate.length === 0) {
            expression_to_evaluate.push('-');
            expression_to_evaluate.push(getLastAnswer());
        } else {
            if (expression_to_evaluate[0] === '-' && expression_to_evaluate.length > 1) {
                expression_to_evaluate = expression_to_evaluate.slice(1); // Remove leading '-' if present
            } else {
                expression_to_evaluate.unshift('-'); // Add leading '-' if not present
            }
        }
        console.log(`Expression after sign change: ${expression_to_evaluate.join('')}`);
        activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
    }

    function percentage() {
        // Handle percentage calculation (e.g., convert current expression to a percentage)
        if (expression_to_evaluate.length > 0) {
            // Get the last number from the expression, meaning the last sequence of digits before an operator
            const lastNumberMatch = expression_to_evaluate.join('').match(/(\d+\.?\d*)$/);
            if (lastNumberMatch) {
                const lastNumber = lastNumberMatch[0];
                const percentageValue = parseFloat(lastNumber) / 100;
                expression_to_evaluate.splice(-lastNumber.length, lastNumber.length, String(percentageValue));
                activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
            }
        }
    }

    function getLastAnswer() {
        if (activeHistory && activeHistory.firstChild) {
            const lastResult = activeHistory.firstChild.querySelector('.hist-result');
            if (lastResult) {
                const value = lastResult.textContent.replace('= ', '');
                return value;
            }
        }
        return '0';
    }


    // - Unit convertor mode: Allows the  user to convert between different units (e.g., length, weight, temperature) by selecting the desired conversion type and inputting the value to be converted.

    return { calculate, clearDisplay, deleteLastEntry, changeSign, percentage, getLastAnswer};
})();

// Expose app globally for inline HTML onclick handlers.
window.app = app;
