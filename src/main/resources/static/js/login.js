
        console.log("login script active");

        var modal = document.getElementById("loginiframe");
        modal.style.visibility = "hidden";
        if(document.getElementById("jsaAboutCarousel") != null) {
            var carousel = document.getElementById("jsaAboutCarousel");
        }
        var superwrapper = document.getElementById("superwrapper");
        var loginbutton = document.getElementById("loggainknapp");

        loginbutton.onclick = function (event2) {
        
            modal.style.visibility = "visible";
            document.body.style.background = "rgba(0,0,0,0.7)";
            superwrapper.style.filter = "blur(3px) brightness(50%)";
            if(document.getElementById("jsaAboutCarousel") != null) {
                carousel.style.filter = "brightness(20%)";
            }


            closeWindow(event2);
        }

        function closeWindow(event2) {
            
            event2.stopPropagation();
            window.onclick = function (event) {
               
                if (modal.style.visibility = "visible") {
               
                    modal.style.visibility = "hidden";
                    document.body.style.background = "none";
                    superwrapper.style.filter = "none";

                    if(document.getElementById("jsaAboutCarousel") != null) {
                        carousel.style.filter = "none";
                    }


               
                }
            }
            return false;
        }
