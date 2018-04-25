$("#searchButton").on("click", function (e) {
    var searchInput = $("#searchTweetInput").val();

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
            $("#output").empty();
            $("#gauge").find("h1").empty();
            console.log("successfully inserted ", result);
            // for (var i = 0; i < result.tweets.length; i++) {
            //     $("#output").append("<p>" + result.tweets[i].tweetText + "</p>");
            // }
            gauge.update(
                {
                    arcFillPercent: result.averageSentiment
                }
            );
            $("#gauge").append("<h1>" + result.averageSentiment + "</h1>");

        }
    });
});

//Creates a new gauge and appends it to the #demo-tag
var gauge = new FlexGauge({
    appendTo: '#gauge',

    //Sizes of the canvas element
    elementWidth: 600,
    elementHeight: 500,

    arcSize: 200,
    arcFillStart: 90,
    arcAngleStart: 1,
    arcAngleEnd: 2,

    arcStrokeFg: 80,
    arcStrokeBg: 80,

    dialValue: true,
    dialLabel: true
});

