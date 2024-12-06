import * as Schedule from './schedule/eventHandler.js';

document.addEventListener('DOMContentLoaded', function () {
  const $main = document.getElementById("main");
  Schedule.init($main);
});
