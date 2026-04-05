/**
 * Main navigation logic for the Calculator.
 */

function showPage(page, el) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
    document.getElementById('page-' + page).classList.add('active');
    el.classList.add('active');

    window.dispatchEvent(new CustomEvent('pageChanged', { detail: { page } }));
}

function selectCategory(cat, el) {
    document.querySelectorAll('.cat-tab').forEach(t => t.classList.remove('active'));
    el.classList.add('active');
    updateUnits(cat);
}

function initAccordions() {
    const accordions = Array.from(document.querySelectorAll('.accordion'));

    accordions.forEach(accordion => {
        const toggle = accordion.querySelector('.accordion-toggle');
        const panel = accordion.querySelector('.accordion-panel');

        if (!toggle || !panel) {
            return;
        }

        const syncState = () => {
            const isOpen = accordion.classList.contains('open');
            toggle.setAttribute('aria-expanded', String(isOpen));
            panel.hidden = !isOpen;
        };

        toggle.addEventListener('click', () => {
            const willOpen = !accordion.classList.contains('open');

            if (willOpen) {
                accordions.forEach(other => {
                    if (other !== accordion) {
                        other.classList.remove('open');
                        const otherToggle = other.querySelector('.accordion-toggle');
                        const otherPanel = other.querySelector('.accordion-panel');
                        if (otherToggle && otherPanel) {
                            otherToggle.setAttribute('aria-expanded', 'false');
                            otherPanel.hidden = true;
                        }
                    }
                });
            }

            accordion.classList.toggle('open');
            syncState();
        });

        syncState();
    });
}

function initCurrencyAutocomplete() {
    const currencies = window.currency_list;
    if (!currencies) {
        console.log('Currency list not available');
        return;
    }

    const items = Object.entries(currencies).map(([code, data]) => ({
        code,
        name: data.name,
        label: `${code} - ${data.name}`
    }));

    const setupField = (inputId, menuId) => {
        const input = document.getElementById(inputId);
        const menu = document.getElementById(menuId);
        if (!input || !menu) return;

        let activeIndex = -1;
        let currentItems = [];

        const closeMenu = () => {
            menu.hidden = true;
            menu.innerHTML = '';
            activeIndex = -1;
        };

        const normalizeInputValue = () => {
            const value = input.value.trim();
            if (!value) return;

            if (value.includes(' - ')) {
                input.value = value.split(' - ')[0].trim().toUpperCase();
                return;
            }

            input.value = value.toUpperCase();
        };

        const selectItem = (item) => {
            input.value = item.code;
            closeMenu();
        };

        const renderMenu = (filtered) => {
            currentItems = filtered.slice(0, 40);
            menu.innerHTML = '';

            if (currentItems.length === 0) {
                closeMenu();
                return;
            }

            currentItems.forEach((item, index) => {
                const option = document.createElement('div');
                option.className = 'currency-option';
                option.setAttribute('role', 'option');
                option.textContent = item.label;
                option.addEventListener('mousedown', (event) => {
                    event.preventDefault();
                    selectItem(item);
                });

                if (index === activeIndex) {
                    option.classList.add('active');
                }

                menu.appendChild(option);
            });

            menu.hidden = false;
        };

        const filterItems = () => {
            const query = input.value.trim().toLowerCase();
            activeIndex = -1;

            if (!query) {
                renderMenu(items);
                return;
            }

            const filtered = items.filter(item => (
                item.code.toLowerCase().startsWith(query) ||
                item.name.toLowerCase().includes(query)
            ));

            renderMenu(filtered);
        };

        input.addEventListener('focus', filterItems);
        input.addEventListener('input', filterItems);

        input.addEventListener('keydown', (event) => {
            if (menu.hidden || currentItems.length === 0) return;

            if (event.key === 'ArrowDown') {
                event.preventDefault();
                activeIndex = (activeIndex + 1) % currentItems.length;
                renderMenu(currentItems);
            } else if (event.key === 'ArrowUp') {
                event.preventDefault();
                activeIndex = activeIndex <= 0 ? currentItems.length - 1 : activeIndex - 1;
                renderMenu(currentItems);
            } else if (event.key === 'Enter' && activeIndex >= 0) {
                event.preventDefault();
                selectItem(currentItems[activeIndex]);
            } else if (event.key === 'Escape') {
                closeMenu();
            }
        });

        input.addEventListener('blur', () => {
            normalizeInputValue();
            setTimeout(closeMenu, 120);
        });

        document.addEventListener('click', (event) => {
            if (!menu.contains(event.target) && event.target !== input) {
                closeMenu();
            }
        });
    };

    setupField('from-currency', 'from-currency-menu');
    setupField('to-currency', 'to-currency-menu');
}

document.addEventListener('DOMContentLoaded', initAccordions);
document.addEventListener('DOMContentLoaded', () => {
    if (window.currency_list) {
        initCurrencyAutocomplete();
        console.log('Currency list already loaded');
        return;
    }

    window.addEventListener('currenciesLoaded', initCurrencyAutocomplete, { once: true });
});