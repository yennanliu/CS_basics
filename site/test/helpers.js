// Shared jsdom bootstrap for the site unit tests.
//
// nav.js and site.js are browser scripts that read `document`, `localStorage`
// and `CustomEvent` off the global scope at call time (never at load time), so
// a test can swap in a fresh jsdom between cases and the already-required
// module picks it up.
const { JSDOM } = require('jsdom');

const GLOBALS = ['window', 'document', 'localStorage', 'CustomEvent', 'Event', 'navigator', 'self'];

/**
 * Installs a fresh jsdom as the global environment and returns it.
 * Pass `html` to control the document body.
 */
function setupDOM(html = '<div id="site-nav"></div>') {
  const dom = new JSDOM(`<!DOCTYPE html><html><head></head><body>${html}</body></html>`, {
    url: 'https://example.test/',
  });
  for (const key of GLOBALS) {
    global[key] = key === 'self' ? dom.window : dom.window[key];
  }
  return dom;
}

function teardownDOM() {
  for (const key of GLOBALS) delete global[key];
}

/** Dispatches a real click so listeners bound with addEventListener fire. */
function click(el) {
  el.dispatchEvent(new global.window.MouseEvent('click', { bubbles: true, cancelable: true }));
}

function keydown(key) {
  global.document.dispatchEvent(new global.window.KeyboardEvent('keydown', { key, bubbles: true }));
}

module.exports = { setupDOM, teardownDOM, click, keydown };
