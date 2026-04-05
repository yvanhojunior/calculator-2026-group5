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

document.addEventListener('DOMContentLoaded', initAccordions);