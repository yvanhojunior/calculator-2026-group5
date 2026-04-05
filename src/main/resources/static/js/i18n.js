/** Internationalization (i18n) support for the calculator. 
 * Entierly made by ChatGPT
*/

let translations = {};
const defaultLang = "en";

function getLanguage() {
  return localStorage.getItem("lang") || defaultLang;
}

function setLanguage(lang) {
  localStorage.setItem("lang", lang);
}

async function loadTranslations(lang) {
  const res = await fetch(`/locales/${lang}.json`);
  translations = await res.json();
}

function t(key) {
  const value = key
    .split(".")
    .reduce((obj, part) => (obj && obj[part] !== undefined ? obj[part] : undefined), translations);
  return value || key;
}

function updateContent() {
  // Normal text handling
  document.querySelectorAll("[data-i18n]").forEach(el => {
    const key = el.dataset.i18n;
    el.textContent = t(key);
  });

  // Placeholder handling
  document.querySelectorAll("[data-i18n-placeholder]").forEach(el => {
    const key = el.dataset.i18nPlaceholder;
    const translated = t(key);
    el.placeholder = translated;
  });
}

async function initI18n() {
  const lang = getLanguage();
  await loadTranslations(lang);
  updateContent();
}