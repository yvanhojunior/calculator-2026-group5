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