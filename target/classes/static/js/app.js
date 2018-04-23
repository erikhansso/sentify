$("#searchButton").on("click", function (e) {
    var searchInput = $("#searchTweetInput").val();

    console.log(searchInput);

    $.ajax({
        type: "POST",
        error: function () {
            console.log("error sending the data");
            render([]);
        },
        data: {
            searchInput: searchInput
        },
        url: "/searchForTweets", //which is mapped to its partner function on our controller class
        success: function (result) {
            console.log("successfully inserted ", result);
            $("#output").append("<p>"+result+"</p>");
        }
    });
});
