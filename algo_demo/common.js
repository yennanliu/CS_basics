// Theme toggle
(function() {
  var saved = localStorage.getItem('algo-theme');
  if (saved === 'dark' || (!saved && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
    document.documentElement.setAttribute('data-theme', 'dark');
  }
})();

function initThemeToggle() {
  var btn = document.getElementById('theme-toggle');
  if (!btn) return;
  var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  btn.textContent = isDark ? '\u2600\uFE0F' : '\uD83C\uDF19';
  btn.addEventListener('click', function() {
    var dark = document.documentElement.getAttribute('data-theme') === 'dark';
    if (dark) {
      document.documentElement.removeAttribute('data-theme');
      localStorage.setItem('algo-theme', 'light');
      btn.textContent = '\uD83C\uDF19';
    } else {
      document.documentElement.setAttribute('data-theme', 'dark');
      localStorage.setItem('algo-theme', 'dark');
      btn.textContent = '\u2600\uFE0F';
    }
  });
}

// Logging helper
function createLogger(containerId) {
  var el = document.getElementById(containerId);
  return {
    clear: function() { el.innerHTML = ''; },
    log: function(msg, cls) {
      var div = document.createElement('div');
      div.className = 'step' + (cls ? ' ' + cls : '');
      div.innerHTML = msg;
      el.appendChild(div);
      el.scrollTop = el.scrollHeight;
    }
  };
}

// Sleep for animation
function sleep(ms) { return new Promise(function(r) { setTimeout(r, ms); }); }

// Shuffle array
function shuffle(arr) {
  for (var i = arr.length - 1; i > 0; i--) {
    var j = Math.floor(Math.random() * (i + 1));
    var tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
  }
  return arr;
}

// Generate random array
function randomArray(n, max) {
  var arr = [];
  for (var i = 0; i < n; i++) arr.push(Math.floor(Math.random() * max) + 1);
  return arr;
}

// Get CSS variable value
function cssVar(name) {
  return getComputedStyle(document.documentElement).getPropertyValue(name).trim();
}

document.addEventListener('DOMContentLoaded', initThemeToggle);
