// resources/js/dashboard.js

document.addEventListener("DOMContentLoaded", function() {
    const boardBoxes = document.querySelectorAll(".board-box");

    boardBoxes.forEach(box => {
        box.addEventListener("click", function() {
            const targetPage = box.getAttribute("data-target");
            if (targetPage) {
                window.location.href = targetPage;
            }
        });
    });
});
