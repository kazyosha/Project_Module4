$(document).ready(function () {
    const currentLang = getCookie("Locale");
    renderOptionLang(currentLang);
    // lay cookie LOCALE
    $("#select-lang").change(function() {
        const lang = $(this).val();
        const url = new URL(window.location.href);
        url.searchParams.set("lang", lang);
        window.location.href = url.toString();

    })
})
function renderOptionLang(currentLang) {
    let html = "";
    html += `<option ${currentLang === 'en' ? 'selected' : ''} value="en">EN</option>
             <option ${currentLang === 'vi' ? 'selected' : ''} value="vi">VN</option>`;

    $("#select-lang").html(html);
}

function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';'); // Split all cookies into an array

    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') { // Remove leading spaces
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) === 0) { // Check if the cookie starts with the desired name
            return decodeURIComponent(c.substring(nameEQ.length, c.length)); // Return the decoded value
        }
    }
    return null; // Return null if the cookie is not found
}