var tweetObjects = {};


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
            tweetObjects = result;
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
            $("#scatterChartContainer").empty();
            $("#scatterChartContainer").append(" <canvas id=\"myChart\"></canvas>");
           createScatterPlot(searchInput, result.tweets);
        }
    });
});

// //Creates a new gauge and appends it to the #demo-tag
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


//changes cursor to show that something is loading while waiting for AJAX
$(document).ajaxStart(function() {
    $(document.body).css({'cursor' : 'wait'});
}).ajaxStop(function() {
    $(document.body).css({'cursor' : 'default'});
});


//Scatterplot scripts below
var createScatterPlot = function (searchQuery, tweets) {
    var dataPoints = [];
    console.log(dataPoints);
    var numberOfTweets = tweetObjects.tweets.length;
    if(numberOfTweets > 100){
        tweetObjects.tweets.splice(0,100);
    }
    for (var i = 1; i <= numberOfTweets; i++) {
        dataPoints.push({
            y: (tweetObjects.tweets[i - 1].sentimentScore),
            x: i,
            createdAt: new Date(tweetObjects.tweets[i - 1].createdAt).toLocaleString(),
            tweetText: tweetObjects.tweets[i - 1].tweetText,
            sentimentScore: tweetObjects.tweets[i - 1].sentimentScore.toFixed(2)
        });
    }

    var tickConfig = {};
    if (dataPoints.length < 50) {
        tickConfig = {
            min: 0,
            max: 50,
            stepSize: 5
        }
    } else {
        tickConfig = {
            min: 0,
            max: 100,
            stepSize: 10
        }
    }

    var pointBackgroundColors = [];
    var ctx = document.getElementById('myChart').getContext('2d');
    var scatterChart = new Chart(ctx, {
        type: 'scatter',
        data: {
            datasets: [{
                label: "You searched for: " + searchQuery,
                fill: false, //how to fill the area under the line
                showLine: false,
                pointStyle: "circle",
                pointBackgroundColor: pointBackgroundColors,
                pointBorderColor: "#6E8C7B",
                pointHoverBackgroundColor: "#FFFFFF",
                backgroundColor: "#FFFFFF",
                borderColor: "#6E8C7B",
                pointRadius: 8,
                pointHoverRadius: 10,
                data: dataPoints
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    type: 'linear',
                    position: 'bottom',
                    ticks: tickConfig,
                    scaleLabel: {
                        display: true,
                        labelString: "Tweet no",
                        fontSize: 20
                    }
                }],
                yAxes: [{
                    gridLines: {
                        color: ["rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "#8FB8A0", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)"],
                        lineWidth: [1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1],
                    },
                    ticks: {
                        min: 0,
                        max: 1,
                        stepSize: 0.1
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Sentiment Score",
                        fontSize: 20
                    }
                }],
            },
            tooltips: {
                enabled: true,
                caretSize: 0,
                mode: "nearest",
                backgroundColor: "#A7D6BB",
                titleFontFamily: "sans-serif",
                titleFontSize: 14,
                titleFontColor: "#6E8C7B",
                bodyFontFamily: "sans-serif",
                bodyFontSize: 12,
                bodyFontColor: "#6E8C7B",
                displayColors: false, //whether to display colored boxes in tooltip
                callbacks: {
                    label: firstLabel.bind(this),
                    afterLabel: otherLabels.bind(this),
                    footer: function (tooltipItem, data) {
                        return "SentScore: " + data["datasets"][0]["data"][tooltipItem[0]["index"]].sentimentScore;
                    },
                    afterFooter: function (tooltipItem, data) {
                        return "Posted: " + data["datasets"][0]["data"][tooltipItem[0]["index"]].createdAt;
                    }

                }
            },
            title: {
                display: true,
                text: "Scatterplot of Tweets' Sentiment Scores",
                fontSize: 24,
                fontFamily: "sans-serif"
            },
        }
    });

    for (i = 0; i < scatterChart.data.datasets[0].data.length; i++) {
        if (scatterChart.data.datasets[0].data[i].y > 0.5) {
            pointBackgroundColors.push("#90cd8a");
        } else {
            pointBackgroundColors.push("#f58368");
        }
    }
    scatterChart.update();
}

var cleanScatter = function () {
    var ctx = document.getElementById('myChart').getContext('2d');
    var emptyScatter = new Chart(ctx, {
        type: 'scatter',
        data: {
            datasets: [{
                label: "You searched for: ",
                fill: false, //how to fill the area under the line
                showLine: false,
                pointStyle: "circle",
                pointBorderColor: "#6E8C7B",
                pointHoverBackgroundColor: "#FFFFFF",
                backgroundColor: "#FFFFFF",
                borderColor: "#6E8C7B",
                pointRadius: 4,
                pointHoverRadius: 6
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    type: 'linear',
                    position: 'bottom',
                    ticks: {
                        min: 0,
                        max: 50,
                        stepSize: 5
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Tweet no",
                        fontSize: 20
                    }
                }],
                yAxes: [{
                    gridLines: {
                        color: ["rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "#8FB8A0", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)", "rgba(0, 0, 0, 0.1)"],
                        lineWidth: [1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1],
                    },
                    ticks: {
                        min: 0,
                        max: 1,
                        stepSize: 0.1
                    },
                    scaleLabel: {
                        display: true,
                        labelString: "Sentiment Score",
                        fontSize: 20
                    }
                }],
            },
            tooltips: {
                enabled: true,
                mode: "nearest",
                backgroundColor: "#A7D6BB",
                titleFontFamily: "sans-serif",
                titleFontSize: 14,
                titleFontColor: "#6E8C7B",
                bodyFontFamily: "sans-serif",
                bodyFontSize: 12,
                bodyFontColor: "#6E8C7B",
                displayColors: false, //whether to display colored boxes in tooltip
            },
            title: {
                display: true,
                text: "Scatterplot of Tweets' Sentiment Scores",
                fontSize: 24,
                fontFamily: "sans-serif"
            },
        }
    });
}

var maxTooltipLength = 50; //possibly refactor this global variable

var wordsToArray = function (words) {
    var lines = [];
    var str = '';
    words.forEach(function (word) {
        if ((str.length + word.length + 1) <= maxTooltipLength) {
            str += word + ' ';
        } else {
            lines.push(str);
            str = word + ' ';
        }
    });
    lines.push(str);
    return lines;
}

var breakLabels = function (tooltipItem, data) {
    var label = data["datasets"][0]["data"][tooltipItem["index"]].tweetText;
    if (label.length <= maxTooltipLength) {
        return [label]
    }
    var words = label.split(' ');
    return wordsToArray(words);
}

var firstLabel = function (tooltipItem, data) {
    return breakLabels(tooltipItem, data)[0];
}

var otherLabels = function (tooltipItem, data) {
    return breakLabels(tooltipItem, data).slice(1);
}

cleanScatter();


