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
    let latestFraction = null; // Store the latest fraction result for SD button functionality
    const THEME_STORAGE_KEY = 'theme';
    let currentPage = 'calculator'; // Default page

    // ── DOM helpers ────────────────────────────────────────────────────────
    const arithmeticButtons =  document.querySelectorAll('button.digit, button.op, button.sct, #ans_button');
    const ans_button = document.getElementById('ans_button');
    const sd_button = document.getElementById('sd_button');
    const close_paren = document.getElementById('close-paren');
    let activeExpression = document.getElementById('std_expression'); // Start with standard expression active
    let activeResult = document.getElementById('std_result'); // Start with standard result active
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

    // ── Language and theme toggles ─────────────────────────────────────────────
    document.getElementById("lang-toggle").addEventListener("click", async () => {
        const current = getLanguage();
        const next = current === "en" ? "fr" : "en";

        setLanguage(next);
        await loadTranslations(next);
        updateContent();
    });

    function applyTheme(theme) {
        const isDark = theme === 'dark';
        document.documentElement.classList.toggle('dark-theme', isDark);

        const icon = document.getElementById('night-mode-icon');
        if (icon) {
            icon.textContent = isDark ? '☀️' : '🌙';
        }
    }

    function getInitialTheme() {
        const savedTheme = localStorage.getItem(THEME_STORAGE_KEY);
        if (savedTheme === 'dark' || savedTheme === 'light') {
            return savedTheme;
        }

        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            return 'dark';
        }

        return 'light';
    }

    function toggleNightMode() {
        const isDark = document.documentElement.classList.contains('dark-theme');
        const nextTheme = isDark ? 'light' : 'dark';
        localStorage.setItem(THEME_STORAGE_KEY, nextTheme);
        applyTheme(nextTheme);
    }

    applyTheme(getInitialTheme());


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
            } else if (btn.classList.contains('digit') || btn.classList.contains('op')) {
                expression_to_evaluate.push(value);
            } else if (btn.classList.contains('sct')) {
                switch (value) {
                    /*
                    case 'sqrt':
                        expression_to_evaluate.push('sqrt(');
                        parenthesis_stack.push(')');
                        break;
                        */

                    case '(':
                        expression_to_evaluate.push('(');
                        parenthesis_stack.push(')');
                        break;

                    case ')':
                        if (parenthesis_stack.length > 0) {
                            expression_to_evaluate.push(')');
                            parenthesis_stack.pop();
                        }
                        break;

                    case '!':
                        computeFactorial();
                        break;

                    default:
                        expression_to_evaluate.push(value);
                }
            }

            activeResult.textContent = expression_to_evaluate.length > 0 ? expression_to_evaluate.join('') + parenthesis_stack.join('') : '0';
            if (parenthesis_stack.length > 0) {
                close_paren.disabled = false;
            } else {
                close_paren.disabled = true;
            }

            // SD button only works on a result from the calculator. Disable it if the user starts entering a new expression.
            if (expression_to_evaluate.length > 0 || parenthesis_stack.length > 0) {
                sd_button.disabled = true;
            } else if (ans_button.disabled === false && getLastAnswer().includes('/')) { // Only re-enable SD if Ans is available, otherwise it would be confusing to have SD enabled but not do anything.
                latestFraction = getLastAnswer();
                sd_button.disabled = false;
            }
            return;
        })

    })

    /** Trigger the same action as clicking the button with the given value. */
    function clickButtonWithValue(value) {
        const button = Array.from(arithmeticButtons).find(btn => btn.value === value);
        if (button) {
            button.click();
            return true;
        }
        return false;
    }

    /** Listen to numpad key presses and reuse the existing button handlers. */
    document.addEventListener('keydown', (event) => {
        const target = event.target;
        if (target instanceof HTMLInputElement || target instanceof HTMLTextAreaElement || target?.isContentEditable) {
        }

        if (!event.code || !event.code.startsWith('Numpad')) {
            return;
        }

        switch (event.code) {
            case 'NumpadEnter':
                event.preventDefault();
                calculate();
                return;
            case 'NumpadAdd':
                event.preventDefault();
                clickButtonWithValue('+');
                return;
            case 'NumpadSubtract':
                event.preventDefault();
                clickButtonWithValue('-');
                return;
            case 'NumpadMultiply':
                event.preventDefault();
                clickButtonWithValue('×');
                return;
            case 'NumpadDivide':
                event.preventDefault();
                clickButtonWithValue('÷');
                return;
            case 'NumpadDecimal':
                event.preventDefault();
                clickButtonWithValue('.');
                return;
            default:
                if (/^Numpad\d$/.test(event.code)) {
                    event.preventDefault();
                    clickButtonWithValue(event.code.replace('Numpad', ''));
                }
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

    /**
     * Convert fraction strings to a stable display format.
     * Example: "5 1/3" -> "16/3".
     */
    function normalizeFractionDisplay(value) {
        const normalized = String(value).trim().replace(/\s+/g, ' ');
        const mixedMatch = normalized.match(/^(-?\d+)\s+(\d+)\/(\d+)$/);
        if (!mixedMatch) {
            return normalized;
        }

        const whole = Number(mixedMatch[1]);
        const remainder = Number(mixedMatch[2]);
        const denominator = Number(mixedMatch[3]);
        const sign = whole < 0 ? -1 : 1;
        const numerator = sign * (Math.abs(whole) * denominator + remainder);
        return `${numerator}/${denominator}`;
    }

    /**
     * Parse a displayed fraction ("a/b" or "w a/b") into numerator/denominator.
     */
    function parseDisplayedFraction(value) {
        const normalized = String(value).trim().replace(/\s+/g, ' ');

        const simpleMatch = normalized.match(/^(-?\d+)\/(\d+)$/);
        if (simpleMatch) {
            return {
                numerator: Number(simpleMatch[1]),
                denominator: Number(simpleMatch[2])
            };
        }

        const mixedMatch = normalized.match(/^(-?\d+)\s+(\d+)\/(\d+)$/);
        if (mixedMatch) {
            const whole = Number(mixedMatch[1]);
            const remainder = Number(mixedMatch[2]);
            const denominator = Number(mixedMatch[3]);
            const sign = whole < 0 ? -1 : 1;
            return {
                numerator: sign * (Math.abs(whole) * denominator + remainder),
                denominator
            };
        }

        return null;
    }

    /** Build an expression string using parser-friendly operator symbols. */
    function buildExpressionForApi() {
        let input = expression_to_evaluate.map(token => {
            switch (token) {
                case '×': return '*';
                case '÷': return '/';
                case '^': return '^';
                case '!': return '!';
                case 'mod': return '%';
                default: return token;
            }
        }).join('') + parenthesis_stack.join('');
        let result = input;
        return result;
    }

    // ── public API ─────────────────────────────────────────────────────────

    function clearDisplay() {
        activeExpression.textContent = '';
        activeResult.textContent = "0";
        expression_to_evaluate = [];
        parenthesis_stack = [];
        close_paren.disabled = true;
    }

    /** Send the expression to the backend and display the result. */
    async function calculate() {

        try {
            if (expression_to_evaluate.length === 0) {
                showError('Please enter an expression to calculate.');
                activeDisplay.textContent = '0';
                return;
            }

            const expression = buildExpressionForApi();
            const response = await fetch(`/api/calculate?expression=${encodeURIComponent(expression)}`);

            const data = await response.json();

            if (!response.ok) {
                showError(data.error ?? 'Failed to calculate expression. Please try again.');
                return;
            }

            const displayResult = normalizeFractionDisplay(formatApiResult(data.result));
            activeResult.textContent = displayResult;
            activeExpression.textContent = expression;
            addHistory(expression, displayResult);

            const isScientific = getCurrentPage() === 'scientific';
            ans_button.disabled = isScientific ? false : true;

            // Reset for next expression
            expression_to_evaluate = [];
            parenthesis_stack = [];
            if (displayResult.includes('/')) {
                console.log(`Storing latest fraction result for SD button: ${displayResult}`);
                latestFraction = displayResult;
                sd_button.disabled = false;
            } else {
                console.log('Latest result is not a fraction, disabling SD button.');
                latestFraction = null;
                sd_button.disabled = true;
            }


        } catch (err) {
            showError('Network error – is the server running?');
            console.error(err);
        }
    }

    /**
     * Delete the last entry in the expression, handling parentheses correctly, and update the display accordingly. If the expression becomes empty, reset the display to '0'.
     */
    function deleteLastEntry() {

        if (expression_to_evaluate.length > 0) {
            const lastToken = expression_to_evaluate.pop();

            if (lastToken === ')') {
                parenthesis_stack.push(')');
            } else if (lastToken[lastToken.length - 1] === '(') {
                parenthesis_stack.pop();
            }
            if (expression_to_evaluate.length === 0 && parenthesis_stack.length === 0) {
                activeResult.textContent = '0';
                sd_button.disabled = true;
            } else {
                activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
            }
        }
    }

    /**
     * Handle sign change by toggling a leading '-' in the expression or applying it to the last answer if the expression is empty. Update the display accordingly.
     */
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
        activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
    }

    /**
     * Handle percentage calculation by converting the last number in the expression to a percentage (dividing by 100) and updating the display accordingly.
     */
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

    /**
     * Retrieve the last answer from the history to support the "Ans" button functionality.
     * @returns {string} The last answer as a string, or '0' if no history is available.
     */
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

    /**
     * Clear the input field of a given element (e.g., when switching modes).
     * @param {*} thisInput 
     */
    function clearInput(thisInput) {
        if (thisInput) {
            thisInput.value = '';
        }
    }

    /**
     * Compute the factorial of the last number in the expression and update the display.
     * @returns {void}
     */
    function computeFactorial() {
        // Get the last number from the expression, meaning the last sequence of digits before an operator
        const lastNumberMatch = expression_to_evaluate.join('').match(/(\d+\.?\d*)$/);
        if (lastNumberMatch) {
            const lastNumber = parseInt(Math.ceil(lastNumberMatch[0]));

            if (lastNumber < 0) {
                showError('Factorial is not defined for negative numbers.');
                return;
            } else if (lastNumber > 15) { // Limit to 15! to prevent overflow and long computation times
                showError('Factorial is too large to compute (max 15!).');
                return;
            } else if (lastNumber === 0 || lastNumber === 1) {
                expression_to_evaluate.splice(-lastNumber.toString().length, lastNumber.toString().length, '1');
            } else {
                let factorial = 1;
                for (let i = 2; i <= lastNumber; i++) {
                    factorial *= i;
                }
                expression_to_evaluate.splice(-lastNumber.toString().length, lastNumber.toString().length, String(factorial));
            }
            activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
        } else {
            showError('Please enter a number before the factorial operator.');
        }

    }

    /**
     * Switch between standard and decimal formats by sending the current result to the backend for conversion. If the current result is a fraction, convert it to decimal; if it's a decimal, switch back to the latest fraction if available. Update the display accordingly.
     * @returns {Promise<void>}
     */
    async function switchStandardDecimal() {
        try {
            if (activeResult.textContent.includes('/')) {
                // We go from standard to decimal
                const fraction = parseDisplayedFraction(activeResult.textContent);
                if (!fraction || !Number.isFinite(fraction.numerator) || !Number.isFinite(fraction.denominator)) {
                    showError('Invalid fraction format.');
                    return;
                }

                const { numerator, denominator } = fraction;
                const reponse = await fetch(`/api/switchFormat`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'                    },
                    body: JSON.stringify({ numerator, denominator })
                });
                const data = await reponse.json();
                if (!reponse.ok) {
                    showError(data.error ?? 'Failed to switch to decimal format. Please try again.');
                    return;
                }
                activeResult.textContent = data.decimalValue;
                console.log(`Switching to decimal format with result: ${activeResult.textContent}`);

                return;
            } else if (activeResult.textContent.includes('.')) {
                // We go from decimal to standard
                activeResult.textContent = latestFraction ? latestFraction : activeResult.textContent;
                console.log(`Switching to standard format with result: ${activeResult.textContent}`);
                return;
            }
        } catch (err) {
            showError('Failed to switch between standard and decimal formats.');
            console.error(err);
        }
    }


    return { calculate, clearDisplay, clearInput, deleteLastEntry, changeSign, percentage, getLastAnswer, toggleNightMode, switchStandardDecimal };
})();

// Expose app globally for inline HTML onclick handlers.
window.app = app;
window.toggleNightMode = () => app.toggleNightMode();
