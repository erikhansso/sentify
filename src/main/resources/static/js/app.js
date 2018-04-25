$("#searchButton").on("click", function (e) {
    var searchInput = $("#searchTweetInput").val();

    console.log(searchInput);

    $.ajax({
        type: "POST",
        error: function () {
            console.log("error sending the data");
        },
        data: {
            searchInput: searchInput
        },
        url: "/searchForTweets", //which is mapped to its partner function on our controller class
        success: function (result) {
            console.log("successfully inserted ", result);
            //for (var i = 0; i < result.length; i++) {
            //    $("#output").append("<p>" + result[i] + "</p>");
            //}
            var averageSentiment = result.averageSentiment;
        }
    });
});

//Creates a new gauge and appends it to the #demo-tag
var gauge = new FlexGauge({
    appendTo: '#demo',

    //Sizes of the canvas element
    elementWidth: 800,
    elementHeight: 800,

    arcSize: 200,
    arcFillStart: 90,
    arcAngleStart: 1,
    arcAngleEnd: 2,

    arcStrokeFg: 80,
    arcStrokeBg: 80,
});

