// Get the modalTerms
var modalTerms = document.getElementById('termsSheet');

// Get the button that opens the modalTerms
var termsBtn = document.getElementById("termsButton");

// Get the <spanCloseTerms> element that closes the modalTerms
var spanCloseTerms = document.getElementsByClassName("closeTerms")[0];

// When the user clicks on the button, open the modalTerms
termsBtn.addEventListener('click', termsBtnClick, true);
 function termsBtnClick() {
    modalTerms.style.display = "block";
}

// When the user clicks on <span> (x), close the modalTerms
spanCloseTerms.onclick = function() {

    modalTerms.style.display = "none";
}

// When the user clicks anywhere outside of the modalTerms, close it
window.onclick = function(event) {

    if (event.target == modalTerms) {
        modalTerms.style.display = "none";
    }
}