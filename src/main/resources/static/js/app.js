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
    const THEME_STORAGE_KEY = 'theme';
    let currentPage = 'calculator'; // Default page

    // ── DOM helpers ────────────────────────────────────────────────────────
    const arithmeticButtons =  document.querySelectorAll('button.digit, button.op, button.sct, #ans_button');
    const ans_button = document.getElementById('ans_button');
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
                    case 'sqrt':
                        expression_to_evaluate.push('sqrt(');
                        parenthesis_stack.push(')');
                        break;

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

    /** Build an expression string using parser-friendly operator symbols. */
    function buildExpressionForApi() {
        let input = expression_to_evaluate.map(token => {
            switch (token) {
                case '×': return '*';
                case '÷': return '/';
                case '^': return '^';
                case '!': return '!';
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

            const displayResult = formatApiResult(data.result).replaceAll(/\s/g, '');     // We handle various result formats (to avoid showing [object Object])
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

            if (lastToken === ')') {
                parenthesis_stack.push(')');
            } else if (lastToken[lastToken.length - 1] === '(') {
                parenthesis_stack.pop();
            }
            if (expression_to_evaluate.length === 0 && parenthesis_stack.length === 0) {
                activeResult.textContent = '0';
            } else {
                activeResult.textContent = expression_to_evaluate.join('') + parenthesis_stack.join('');
            }
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

    function clearInput(thisInput) {
        if (thisInput) {
            thisInput.value = '';
        }
    }


    // - Unit convertor mode: Allows the  user to convert between different units (e.g., length, weight, temperature) by selecting the desired conversion type and inputting the value to be converted.

    return { calculate, clearDisplay, clearInput, deleteLastEntry, changeSign, percentage, getLastAnswer, toggleNightMode};
})();

// Expose app globally for inline HTML onclick handlers.
window.app = app;
window.toggleNightMode = () => app.toggleNightMode();
