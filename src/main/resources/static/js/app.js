$(document).ready(function () {
    Chart.defaults.global.defaultFontFamily = "Sura";
    /*Use these five color variables, comes from our palette!*/
    var color = {
        mainBgColor: "#FFFFFF",
        mainColor: "#8FB8A0",
        mainContrastColor: "#87524F",
        mainColorLight: "#E0E0E0",
        mainColorDark: "#6E8C7B"
    };

    var colorRGB = {
        mainColor: "rgba(206,237,241,0.7)",
        mainContrastColor: "rgba(135,82,79,0.7)",
        mainColorLight: "rgba(224,224,224,0.7)",
        mainColorDark: "rgba(110,140,123,0.7)",
        mainColorDarker: "rgba(161,186,189,0.7)",
        mainColorDarkLighter: "rgba(135,173,152,0.7)"
    };

    var colorRGBDarker = {
        mainColor: "rgba(206,237,241)",
        mainColorDarkerShade: "rgba(92,118,102)",
        mainContrastColor: "rgba(135,82,79)",
        mainContrastColorLighter: "rgba(167,129,127)",
        mainColorLight: "rgba(123,123,123)",
        mainColorDark: "rgba(110,140,123)",
        mainColorDarker: "rgba(161,186,189)",
        mainColorDarkLighter: "rgba(135,173,152)"
    };

    var keywordInput = '';

    /**
     *  FlexGauge
     *  Version: 1.0
     *  Author: Jeff Millies
     *  Author URI:
     */

    (function ($) {
        var FlexGauge = function (o) {
            if (typeof o === 'object') {
                this._extendOptions(o, false);
                this._build();
            }
        };
        FlexGauge.prototype = {
            /**
             *  {String} Element that you would like to append to. ie '#idname', '.classname', 'div#idname', etc..
             */
            appendTo: 'body',
            /**
             *  {String} Id of Canvas already created or Id of canvas that will be created automatically
             */
            elementId: 'canvas',
            /**
             *  {String} Class of canvas created
             */
            elementClass: 'canvas',
            /**
             *  {Int} Canvas Width & Height
             */
            elementWidth: 200,
            elementHeight: 200,
            /**
             *  {Boolean|String} Generate Dial Value for the Gauge, true will use arcFillPercent or arcFillInt
             *  depending on provided values and specified dialUnits, string will use specified value
             */
            dialValue: false,
            /**
             *  {String} Class applied to div when dial is generated.
             */
            dialClass: 'fg-dial',
            /**
             *  {string: %|$| } Type of unit to use for the dial
             */
            dialUnit: '%',
            /**
             *  {string: before|after} Where the dial unit will be displayed
             */
            dialUnitPosition: 'after',
            /**
             *  {Boolean|String} Generate Label for the Gauge, true will use default "FlexGauge", string will use specified
             */
            dialLabel: false,
            /**
             *  {String} Class applied to div when label is generated.
             */
            dialLabelClass: 'fg-dial-label',
            /**
             *  {Int} Radius of the arc
             */
            arcSize: 85,
            /**
             *  {double} Starting and Ending location of the arc, End always needs to be larger
             *  arc(x, y, radius, startAngle, endAngle, anticlockwise)
             */
            arcAngleStart: 0.85,
            arcAngleEnd: 2.15,
            /**
             *  {double} Percentage the arc fills
             */
            arcFillPercent: .5,
            /**
             *  {Int} Starting and Ending values that are used to
             *  find a difference for amount of units
             *  ie: 60 (arcFillEnd) - 10 (arcFillStart) = 50
             */
            arcFillStart: null,
            arcFillEnd: null,
            /**
             *  {Int} Data used to find out what percentage of the
             *  arc to fill. arcFillInt can be populated by
             *  the difference of arcFillStart and arcFillEnd
             */
            arcFillInt: null,
            arcFillTotal: null,
            /**
             *  {Int} Color lightness: 0 - 255, 0 having no white added, 255 having all white and no color
             */
            arcBgColorLight: 80,
            /**
             *  {Int} Color saturation: 0 - 100, 0 having no color, 100 is full color
             */
            arcBgColorSat: 20,
            /**
             *  {Int} Size of the line marking the percentage
             */
            arcStrokeFg: 30,
            /**
             *  {Int} Size of the container holding the line
             */
            arcStrokeBg: 30,

            /**
             *  {string: hex} Color of the line marking the percentage
             */
            colorArcFg: '#5bc0de',
            /**
             *  {string: hex} Color of the container holding the line, default is using the Fg color and lightening it
             */
            colorArcBg: null,

            /**
             *  {String} Instead of providing a color or hex for the color, you can provide a class from the style
             *  sheet and specify what you would like to grab for the color in styleSrc
             */
            styleArcFg: null,
            styleArcBg: null,
            styleSrc: 'color',

            /**
             *  {Boolean} If set to false, then the graph will not be animated
             */
            animateEasing: true,
            /**
             *  {Int} Speed for the animation, 1 is fastest, higher the number, slower the animation
             */
            animateSpeed: 5,
            /**
             *  {Int} Math used in animation speed
             */
            animateNumerator: 12,
            animateDivisor: 15,

            /**
             *  {double} Placeholder for current percentage while animating
             */
            _animatePerc: 0.00,

            /**
             *  {Object} Placeholder for setInterval
             */
            _animateLoop: null,

            /**
             *  {Object} Placeholder for canvas
             */
            _canvas: null,

            /**
             *  {Object} Placeholder for canvas context
             */
            _ctx: null,

            update: function (o) {
                if (typeof o === 'object') {
                    var difference;

                    // if using int, convert to percent to check difference
                    if (typeof o.arcFillInt !== 'undefined' && o.arcFillInt == this.arcFillInt &&
                        typeof o.arcFillTotal !== 'undefined' && o.arcFillTotal == this.arcFillTotal) {
                        o.arcFillPercent = this.arcFillPercent;
                    } else if (typeof o.arcFillInt !== 'undefined' && typeof o.arcFillTotal !== 'undefined' &&
                        (o.arcFillInt != this.arcFillInt || o.arcFillTotal == this.arcFillTotal)) {
                        o.arcFillPercent = (o.arcFillInt / o.arcFillTotal);
                    } else if (typeof o.arcFillInt !== 'undefined' && typeof o.arcFillTotal === 'undefined' &&
                        (o.arcFillInt != this.arcFillInt)) {
                        o.arcFillPercent = (o.arcFillInt / this.arcFillTotal);
                    }

                    if (typeof o.arcFillPercent !== 'undefined') {
                        difference = Math.abs((this.arcFillPercent - o.arcFillPercent));
                    } else {
                        difference = this.arcFillPercent;
                    }

                    this._extendOptions(o, true);

                    clearInterval(this._animateLoop);

                    if (difference > 0) {
                        var that = this;
                        this._animateLoop = setInterval(function () {
                            return that._animate();
                        }, (this.animateSpeed * this.animateNumerator) / (difference * this.animateDivisor));
                    }
                }
            },

            _extendOptions: function (o, update) {
                var color = false;
                if (update)
                    color = this.colorArcFg;

                $.extend(this, o, true);

                if (typeof o.arcFillStart !== 'undefined' && typeof o.arcFillEnd !== 'undefined' && typeof o.arcFillTotal !== 'undefined') {
                    this.arcFillInt = (o.arcFillEnd - o.arcFillStart);
                }

                if (typeof o.arcFillPercent === 'undefined' && this.arcFillInt !== null && this.arcFillInt >= 0 && this.arcFillTotal !== null && this.arcFillTotal > 0) {
                    this.arcFillPercent = this.arcFillInt / this.arcFillTotal;
                }

                if (typeof o.elementId === 'undefined') {
                    this.elementId = 'fg-' + this.appendTo + '-canvas';
                }
                // supporting color if pass, changing to hex
                if (typeof o.colorArcFg !== 'undefined') {
                    this.colorArcFg = colorToHex(o.colorArcFg);
                }

                if (typeof o.colorArcBg !== 'undefined') {
                    this.colorArcBg = "#E0E0E0";
                }

                // only use the styleArcFg if colorArcFg wasn't specified in the options
                if (typeof o.styleArcFg !== 'undefined' && typeof o.colorArcFg === 'undefined') {
                    this.colorArcFg = getStyleRuleValue(this.styleSrc, this.styleArcFg);
                }

                if (typeof o.colorArcBg === 'undefined' && this.colorArcBg === null && this.colorArcFg !== null) {
                    this.colorArcBg = "#E0E0E0";
                }

                if (typeof this.colorArcBg !== null && (!update || colorToHex(this.colorArcFg) != colorToHex(color))) {
                    if (colorToHex(this.colorArcFg) != colorToHex(color))
                        this.colorArcBg = "#E0E0E0";

                    this.colorArcBg = "#E0E0E0";
                }

                if (typeof o.dialLabel === 'boolean' && o.dialLabel) {
                    this.dialLabel = keywordInput;
                }

            },

            _build: function () {
                if (document.getElementById(this.elementId) === null) {
                    $(this.appendTo).append('<canvas id="' + this.elementId + '" width="' + this.elementWidth + '" height="' + this.elementHeight / 2 + '"></canvas>');
                }

                this._canvas = document.getElementById(this.elementId);
                this._ctx = this._canvas.getContext("2d");

                this.arcAngleStart = this.arcAngleStart * Math.PI;
                this.arcAngleEnd = this.arcAngleEnd * Math.PI;
                if (this.animateEasing === false) {
                    this._animatePerc = this.arcFillPercent;
                }

                var that = this;
                this._animateLoop = setInterval(function () {
                    return that._animate();
                }, (this.animateSpeed * this.animateNumerator) / (this.arcFillPercent * this.animateDivisor));
            },

            _animate: function () {
                var animateInt = Math.round(this._animatePerc * 100);
                var arcInt = Math.round(this.arcFillPercent * 100);

                if (animateInt < arcInt)
                    animateInt++;
                else
                    animateInt--;

                this._animatePerc = (animateInt / 100);
                if (animateInt === arcInt) {
                    this.arcFillPercent = this._animatePerc;
                    clearInterval(this._animateLoop);
                    this._draw();
                }
                this._draw();
            },

            _draw: function () {
                //Clear the canvas everytime a chart is drawn
                this._ctx.clearRect(0, 0, this.elementWidth, this.elementHeight);

                //Background 360 degree arc
                this._ctx.beginPath();
                this._ctx.strokeStyle = this.colorArcBg;
                this._ctx.lineWidth = this.arcStrokeBg;
                this._ctx.arc(
                    this.elementWidth / 2,
                    this.elementHeight / 2,
                    this.arcSize,
                    this.arcAngleStart,
                    this.arcAngleEnd,
                    false
                );

                this._ctx.stroke();

                var newEnd = ((this.arcAngleEnd - this.arcAngleStart) * this._animatePerc) + this.arcAngleStart;

                this._ctx.beginPath();
                this._ctx.strokeStyle = this.colorArcFg;
                this._ctx.lineWidth = this.arcStrokeFg;
                this._ctx.arc(
                    this.elementWidth / 2,
                    this.elementHeight / 2,
                    this.arcSize,
                    this.arcAngleStart,
                    newEnd,
                    false
                );
                this._ctx.stroke();
                this._renderLabel();
            },

            _renderLabel: function () {
                if (this.dialValue) {
                    var dialVal;
                    var dial = $(this.appendTo).find('div.' + this.dialClass);
                    if (dial.length === 0) {
                        $(this.appendTo).append('<div class="' + this.dialClass + '"></div>');
                    }
                    dial = $(this.appendTo).find('div.' + this.dialClass);
                    if (typeof this.dialValue === 'boolean') {
                        this.dialLabel = keywordInput;
                        switch (this.dialUnit) {
                            case '%':
                                dialVal = Math.round(this._animatePerc * 100);
                                break;
                            default:
                                dialVal = Math.round(this.arcFillInt * (this._animatePerc / this.arcFillPercent));
                                break;
                        }
                        dialVal = (isNaN(dialVal) ? 0 : dialVal);
                        switch (this.dialUnitPosition) {
                            case 'before':
                                dialVal = this.dialUnit + dialVal;
                                break;
                            case 'after':
                                dialVal = dialVal + this.dialUnit;
                                break;
                        }
                    } else {
                        dialVal = this.dialValue;
                        this.dialLabel = keywordInput;
                    }
                    dial.html(dialVal)
                }
                if (this.dialLabel) {
                    var label = $(this.appendTo).find('div.' + this.dialLabelClass);
                    if (label.length === 0) {
                        $(this.appendTo).append('<div class="' + this.dialLabelClass + '"></div>');
                    }
                    label = $(this.appendTo).find('div.' + this.dialLabelClass);
                    label.html(this.dialLabel);
                }
            }
        };

        function shadeColor(col, amt, sat) {
            if (col[0] == "#") {
                col = col.slice(1);
            }

            var num = parseInt(col, 16);

            var r = (num >> 16) + amt;

            if (r > 255) r = 255;
            else if (r < 0) r = 0;

            var b = ((num >> 8) & 0x00FF) + amt;

            if (b > 255) b = 255;
            else if (b < 0) b = 0;

            var g = (num & 0x0000FF) + amt;

            if (g > 255) g = 255;
            else if (g < 0) g = 0;

            var gray = r * 0.3086 + g * 0.6094 + b * 0.0820;
            sat = (sat / 100);

            r = Math.round(r * sat + gray * (1 - sat));
            g = Math.round(g * sat + gray * (1 - sat));
            b = Math.round(b * sat + gray * (1 - sat));
            return "#" + (g | (b << 8) | (r << 16)).toString(16);
        }

        function getStyleRuleValue(style, selector) {
            $('body').append('<div id="getStyleRuleValue-' + selector + '"></div>');
            var element = $('#getStyleRuleValue-' + selector);
            element.addClass(selector);
            var color = element.css(style);
            var hex = colorToHex(color);
            element.remove();
            return hex;
        }

        function colorToHex(color) {
            if (color[0] != 'r')
                return color;

            var rgb = color.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
            return "#" +
                ("0" + parseInt(rgb[1], 10).toString(16)).slice(-2) +
                ("0" + parseInt(rgb[2], 10).toString(16)).slice(-2) +
                ("0" + parseInt(rgb[3], 10).toString(16)).slice(-2);
        }

        if (typeof define === 'function') {
            define('flex-gauge', ['jquery'], function ($) {
                return FlexGauge;
            });
        } else {
            window.FlexGauge = FlexGauge;
        }
    })(jQuery);


    var getColorBasedOnIndex = function (index) {
        var color = "";
        var counter = 0;
        var size = 0;
        for (var col in colorRGBDarker) {
            size++;
        }

        for (var col in colorRGBDarker) {
            if (index % size === counter) {
                return colorRGBDarker[col];
            }
            counter++;
        }
        return color;
    };

    function setFocusToTextBox() {
        $("#searchTweetInput").focus();
    }

    $('#searchTweetInput').on('input change', function () {
        if ($("#searchTweetInput").val() !== '') {
            $('#searchTweetButton').prop('disabled', false);
        }
        else {
            $('#searchTweetButton').prop('disabled', true);
        }
    });

    var toggleDisableTrackKeywordsButton = function (isDisabled) {
        $("#addKeyWordButton").prop('disabled', isDisabled);
    };



    $('#searchTweetInput').keypress(function (event) {
        if (event.which === 13 && $(this).val() !== '') {
            var searchInput = $(this).val();
            keywordInput = htmlEscape(searchInput);
            ajaxRequest(searchInput);
        }
    });

    $(".demoButton").on("click", function () {
        var searchInput = $(this).val();
        keywordInput = htmlEscape(searchInput);
        ajaxRequestForDemoPurposes(htmlEscape(searchInput));
    });

//Premium: add keyword to saved keywords
    $("#addKeyWordButton").on("click", function (e) {
        var searchInput = $("#addKeyWordButton").val();
        keywordInput = htmlEscape(searchInput);
        ajaxForSavingKeywords(searchInput);
    });

//Keyword buttons are set on document because all buttons are not created on page load
    $(document).on("click", ".keywordButton", function (e) {
        var searchInput = $(this).html();
        keywordInput = htmlEscape(searchInput);
        ajaxRequest(searchInput);
    });

    $(document).on("click", ".removeKeyword", function (e) {
        console.log("clicked remove button");
        var pos = $(this).closest("li").attr("data-pos");
        console.log("pos", pos);
        var keyword = $("#" + pos).val();
        console.log("keyword", keyword);
        ajaxForUpdatingKeywords(keyword);
    });

    $("#resetButton").on("click", function (e) {
        clearAll();
    });

    $("#searchTweetButton").on("click", function (e) {
        var searchInput = $("#searchTweetInput").val();
        keywordInput = htmlEscape(searchInput);
        ajaxRequest(htmlEscape(searchInput));
    });

    var ajaxRequestForDemoPurposes = function (searchInput) {
        var tweetObjects = {};
        $(document.body).css({'cursor': 'wait'});
        $.ajax({
            type: "POST",
            error: function () {
                $(document.body).css({'cursor': 'default'});
                console.log("error sending the data");
            },
            data: {
                searchInput: searchInput
            },
            url: "/searchForDemoTweets", //which is mapped to its partner function on our controller class
            success: function (result) {
                console.log("successfully inserted ", result);
                if (result.tweets === null) {
                    $(document.body).css({'cursor': 'default'});
                    clearAll();
                    keywordInput = "No tweets were found"; //To update the dialLabel
                    $("#scatterTitle").text("No tweets were found");
                    return;
                }
                $(document.body).css({'cursor': 'default'});
                tweetObjects = result;

                $("#scatterTitle").text("Latest opinions of: " + searchInput);

                var percentage = result.averageSentiment.toFixed(2);

                $("#gauge").find("h1").empty();
                gauge.dialLabel = true;
                gauge.dialValue = true;
                gauge.update(
                    {
                        arcFillPercent: percentage,
                        colorArcFg: getColor(percentage),
                    }
                );
                $("#numberOfTweets").text(tweetObjects.tweets.length);

                var numberOfPositiveTweets = 0;
                var numberOfNegativeTweets = 0;
                for (var j = 0; j < tweetObjects.tweets.length; j++) {
                    if (tweetObjects.tweets[j].sentimentScore > 0.5) {
                        numberOfPositiveTweets++;
                    } else {
                        numberOfNegativeTweets++;
                    }
                }

                $("#numberOfPosTweets").text(numberOfPositiveTweets);
                $("#numberOfNegTweets").text(numberOfNegativeTweets);

                $("#scatterChartContainer").empty();
                $("#scatterChartContainer").append(" <canvas id=\"scatterChart\"></canvas>");
                $("#barChartContainer").empty();
                $("#barChartContainer").append(" <canvas id=\"barChart\"></canvas>");
                $("#lineChartContainer").empty();
                $("#lineChartContainer").append(" <canvas id=\"lineChart\"></canvas>");


                if (searchInput in state.tweetsSearchedFor) {
                    state.tweetsSearchedFor[searchInput].tweets = result;
                } else {
                    state.tweetsSearchedFor[searchInput] = {tweets: result};
                }

                createPureStatistics(searchInput, tweetObjects);
                createScatterPlot(searchInput, result.tweets);
                createLineChart(searchInput, state);
            }
        });
        $("#searchTweetInput").val('');
    };

    var ajaxForSavingKeywords = function (searchInput) {
        $(document.body).css({'cursor': 'wait'});
        $.ajax({
            type: "POST",
            error: function () {
                $(document.body).css({'cursor': 'default'});
                console.log("error sending the data");
            },
            data: {
                searchInput: searchInput
            },
            url: "/saveKeywordToUser", //which is mapped to its partner function on our controller class
            success: function (savedKeywords) {
                console.log("successfully inserted ", savedKeywords);
                if (savedKeywords !== null) {
                    $(document.body).css({'cursor': 'default'});
                }
                $(document.body).css({'cursor': 'default'});
                updateKeywordsButtons(savedKeywords);
                updateOverview(savedKeywords);
            }
        });
    };

    var ajaxForUpdatingKeywords = function (keyword) {
        $(document.body).css({'cursor': 'wait'});
        $.ajax({
            type: "POST",
            error: function () {
                $(document.body).css({'cursor': 'default'});
                console.log("error sending the data");
            },
            data: {
                searchInput: keyword
            },
            url: "/updateKeywordToUser", //which is mapped to its partner function on our controller class
            success: function (savedKeywords) {
                console.log("successfully inserted ", savedKeywords);
                if (savedKeywords !== null) {
                    $(document.body).css({'cursor': 'default'});
                }
                $(document.body).css({'cursor': 'default'});
                updateKeywordsButtons(savedKeywords);
                updateOverview(savedKeywords);
            }
        });
    };

    var updateKeywordsButtons = function (savedKeywords) {
        keywordsArray = [];
        var listOfKeywords = [];
        var iterator = 0;
        $("#savedKeywords").empty();
        for (var i = 0; i < savedKeywords.length; i++) {
            listOfKeywords.push(savedKeywords[i]);
        }
        $("#scatterChartContainer").append(" <canvas id=\"scatterChart\"></canvas>");
        for (var j = 0; j < listOfKeywords.length; j++) {
            $("#savedKeywords").append(" <li data-pos=\"" + iterator + "\" >\n" +
                "                            <button id=\"" + iterator + "\" type=\"submit\" class=\"searchButton button keywordButton\" value=\"" + listOfKeywords[j] + "\">" + listOfKeywords[j] + "</button>\n" +
                "<button type=\"submit\" class=\"searchButton button removeKeyword\">X</button>" +

                "                        </li>");
            keywordsArray.push(listOfKeywords[j]);
            iterator++;
        }
    };

    var ajaxRequest = function (searchInput) {
        $('#searchTweetButton').prop('disabled', true);
        var tweetObjects = {};
        $(document.body).css({'cursor': 'wait'});
        $.ajax({
            type: "POST",
            error: function () {
                $(document.body).css({'cursor': 'default'});
                console.log("error sending the data");
            },
            data: {
                searchInput: searchInput
            },
            url: "/searchForTweets", //which is mapped to its partner function on our controller class
            success: function (result) {
                console.log("successfully inserted ", result);
                if (result.tweets === null) {
                    $(document.body).css({'cursor': 'default'});
                    clearAll();
                    keywordInput = "No tweets were found"; //To update the dialLabel
                    $("#scatterTitle").text("No tweets were found");
                    return;
                }
                toggleDisableTrackKeywordsButton(false);
                $(document.body).css({'cursor': 'default'});
                tweetObjects = result;

                $("#scatterTitle").text("Latest opinions of: " + searchInput);

                var percentage = result.averageSentiment.toFixed(2);

                $("#gauge").find("h1").empty();
                gauge.dialLabel = true;
                gauge.dialValue = true;
                gauge.update(
                    {
                        arcFillPercent: percentage,
                        colorArcFg: getColor(percentage)
                    }
                );
                $("#numberOfTweets").text(tweetObjects.tweets.length);

                var numberOfPositiveTweets = 0;
                var numberOfNegativeTweets = 0;
                for (var j = 0; j < tweetObjects.tweets.length; j++) {
                    if (tweetObjects.tweets[j].sentimentScore > 0.5) {
                        numberOfPositiveTweets++;
                    } else {
                        numberOfNegativeTweets++;
                    }
                }

                $("#numberOfPosTweets").text(numberOfPositiveTweets);
                $("#numberOfNegTweets").text(numberOfNegativeTweets);

                $("#scatterChartContainer").empty();
                $("#scatterChartContainer").append(" <canvas id=\"scatterChart\"></canvas>");
                $("#barChartContainer").empty();
                $("#barChartContainer").append(" <canvas id=\"barChart\"></canvas>");
                $("#lineChartContainer").empty();
                $("#lineChartContainer").append(" <canvas id=\"lineChart\"></canvas>");


                if (searchInput in state.tweetsSearchedFor) {
                    state.tweetsSearchedFor[searchInput].tweets = result;
                } else {
                    state.tweetsSearchedFor[searchInput] = {tweets: result};
                }

                updateAddKeywordButton(searchInput);
                createPureStatistics(searchInput, tweetObjects);
                createScatterPlot(searchInput, result.tweets);
                createLineChart(searchInput, state);
            }
        });
        $("#searchTweetInput").val('');
    };

    var updateAddKeywordButton = function (keyword) {
        $("#addKeyWordButton").val(keyword);
    };

    var clearAll = function () {
        toggleDisableTrackKeywordsButton(true);
        keywordInput = "-"; //To update the dialLabel
        $("#scatterTitle").text("Latest opinions of tweets");
        $("#numberOfTweets").text("?");
        $("#numberOfPosTweets").text("?");
        $("#numberOfNegTweets").text("?");
        updateAddKeywordButton("");
        $("#scatterChartContainer").empty();
        $("#scatterChartContainer").append(" <canvas id=\"scatterChart\"></canvas>");
        $("#barChartContainer").empty();
        $("#barChartContainer").append(" <canvas id=\"barChart\"></canvas>");
        $("#lineChartContainer").empty();
        $("#lineChartContainer").append(" <canvas id=\"lineChart\"></canvas>");
        clearPureStatistics();
        returnsCleanLineChart();
        returnsCleanScatter();
        state = {
            tweetsSearchedFor: {}
        };
        gauge.update(
            {
                dialValue: "-%",
                arcFillPercent: 0,
                colorArcFg: getColor(0)
            }
        );
    };

// //Creates a new gauge and appends it to the #demo-tag
    var gauge = new FlexGauge({
        appendTo: '#gauge',

        //Sizes of the canvas element
        elementWidth: 600,
        elementHeight: 500,
        elementId: 'gaugeCanvas',

        arcSize: 200,
        arcFillStart: 90,
        arcAngleStart: 1,
        arcAngleEnd: 2,

        arcStrokeFg: 80,
        arcStrokeBg: 80,

        colorArcFg: function () {
            //value from 0 to 1
            value = 0.5;

            var hue = ((1 - (Math.abs(value - 1))) * 120).toString(10);
            return ["hsl(", hue, ",65%,65%)"].join("");
        },

        dialValue: "-%",
        dialLabel: true
    });

    var getColor = function (value) {
        //value from 0 to 1
        var hue = ((1 - (Math.abs(value - 1))) * 120).toString(10);
        return ["hsl(", hue, ",65%,65%)"].join("");

    };

//Beginning of state object
    var state = {
        tweetsSearchedFor: {} //contains the result of all tweets searched for in this session
    };

    var keywordsArray = [];

//Scatterplot scripts below
    var createScatterPlot = function (searchQuery, tweets) {
        var dataPoints = [];
        var arrayOfTweets = tweets;
        var tweetsToBeAnalyzed = [];

        //om listan är längre än 100, gör bara 100 första
        if (arrayOfTweets.length > 100) {
            for (var k = 0; k < 100; k++) {
                tweetsToBeAnalyzed.push(arrayOfTweets[k]);
            }
        } else {
            tweetsToBeAnalyzed = arrayOfTweets;
        }

        for (var i = 1; i <= tweetsToBeAnalyzed.length; i++) {
            dataPoints.push({
                y: (tweetsToBeAnalyzed[i - 1].sentimentScore),
                x: i,
                createdAt: new Date(tweetsToBeAnalyzed[i - 1].createdAt).toLocaleString(),
                tweetText: tweetsToBeAnalyzed[i - 1].tweetText,
                sentimentScore: tweetsToBeAnalyzed[i - 1].sentimentScore.toFixed(2)
            });
        }

        var pointBackgroundColors = [];
        var ctx = document.getElementById('scatterChart').getContext('2d');
        var scatterChart = new Chart(ctx, {
            type: 'scatter',
            data: {
                datasets: [{
                    fill: false, //how to fill the area under the line
                    showLine: false,
                    pointStyle: "circle",
                    pointBackgroundColor: pointBackgroundColors,
                    pointBorderColor: color.mainColorDark,
                    pointHoverBackgroundColor: color.mainColorLight,
                    backgroundColor: color.mainBgColor,
                    borderColor: color.mainColorDark,
                    pointRadius: 4,
                    pointHoverRadius: 6,
                    pointHitRadius: 6,
                    data: dataPoints
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'linear',
                        position: 'bottom',
                        ticks: {
                            display: false,
                            min: 0,
                            max: 100,
                            stepSize: 10
                        },
                        gridLines: {
                            color: color.mainColorLight
                        },
                        scaleLabel: {
                            display: true,
                            labelString: "Tweets",
                            fontSize: 20
                        }
                    }],
                    yAxes: [{
                        gridLines: {
                            color: [color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight],
                            lineWidth: [1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1]
                        },
                        ticks: {
                            callback: function (value, index, values) {
                                if (index === 10) {
                                    return 'Negative';
                                }
                                if (index === 5) {
                                    return 'Neutral';
                                }
                                if (index === 0) {
                                    return 'Positive';
                                }
                                return "";
                            },
                            min: 0,
                            max: 1,
                            stepSize: 0.1
                        }
                    }]
                },
                tooltips: {
                    enabled: true,
                    caretSize: 0,
                    mode: "nearest",
                    backgroundColor: color.mainColor,
                    bodyFontFamily: "sans-serif",
                    bodyFontSize: 12,
                    bodyFontColor: "#000000",
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
                    display: false
                },
                legend: {
                    display: false
                },
                layout: {
                    padding: {
                        left: 20,
                        right: 50,
                        top: 0,
                        bottom: 0
                    }
                }
            }
        });

        for (i = 0; i < scatterChart.data.datasets[0].data.length; i++) {
            if (scatterChart.data.datasets[0].data[i].y > 0.5) {
                pointBackgroundColors.push("rgba(110,140,123,0.8)");
            } else {
                pointBackgroundColors.push("rgba(135,82,79,0.8)");
            }
        }
        scatterChart.update();
    };

    var returnsCleanScatter = function () {
        var ctx = document.getElementById('scatterChart').getContext('2d');
        var emptyScatter = new Chart(ctx, {
            type: 'scatter',
            data: {
                datasets: [{
                    fill: false, //how to fill the area under the line
                    showLine: false,
                    backgroundColor: color.mainBgColor,
                    borderColor: color.mainColorDark
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'linear',
                        position: 'bottom',
                        gridLines: {
                            color: color.mainColorLight
                        },
                        ticks: {
                            display: false,
                            min: 0,
                            max: 100,
                            stepSize: 10
                        },
                        scaleLabel: {
                            display: true,
                            labelString: "Tweets",
                            fontSize: 20
                        }
                    }],
                    yAxes: [{
                        gridLines: {
                            color: [color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight],
                            lineWidth: [1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1],
                        },
                        ticks: {
                            callback: function (value, index, values) {
                                if (index === 10) {
                                    return 'Negative';
                                }
                                if (index === 5) {
                                    return 'Neutral';
                                }
                                if (index === 0) {
                                    return 'Positive';
                                }
                                return "";
                            },
                            min: 0,
                            max: 1,
                            stepSize: 0.1
                        }
                    }]
                },
                title: {
                    display: false
                },
                legend: {
                    display: false
                },
                layout: {
                    padding: {
                        left: 20,
                        right: 50,
                        top: 0,
                        bottom: 0
                    }
                }
            }
        });
    };

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
    };

    var breakLabels = function (tooltipItem, data) {
        var label = data["datasets"][0]["data"][tooltipItem["index"]].tweetText;
        if (label.length <= maxTooltipLength) {
            return [label]
        }
        var words = label.split(' ');
        return wordsToArray(words);
    };

    var firstLabel = function (tooltipItem, data) {
        return breakLabels(tooltipItem, data)[0];
    };

    var otherLabels = function (tooltipItem, data) {
        return breakLabels(tooltipItem, data).slice(1);
    };

    function htmlEscape(str) {
        return str
            .replace(/&/g, '&amp;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');
    }

//Line chart scripts below
    var createLineChart = function (searchInput) {

        var dataPoints = addDataPointsToLineChart();

        var dataSets = generateDatasetsFromLineChartDataPoints(dataPoints, searchInput);


        var ctx = document.getElementById('lineChart').getContext('2d');
        var lineChart = new Chart(ctx, {
            type: 'line',
            data: {
                datasets: dataSets
            },
            options: {
                tooltips: {
                    enabled: true,
                    caretSize: 0,
                    mode: "nearest",
                    backgroundColor: color.mainColor,
                    bodyFontFamily: "sans-serif",
                    bodyFontSize: 12,
                    bodyFontColor: "#000000",
                    displayColors: false, //whether to display colored boxes in tooltip
                    callbacks: {
                        title: function () {
                        },
                        label: function (tooltipItem, data) {
                            return data["datasets"][tooltipItem["datasetIndex"]]["data"][tooltipItem["index"]]["numberOfTweets"] + " tweet(s) analyzed this day";
                        },
                        footer: function (tooltipItem, data) {
                            return "SentScore: " + data["datasets"][tooltipItem[0]["datasetIndex"]]["data"][tooltipItem[0]["index"]]["y"];
                        },
                        afterFooter: function (tooltipItem, data) {
                            return data["datasets"][tooltipItem[0]["datasetIndex"]]["data"][tooltipItem[0]["index"]]["x"].format("ddd MMM D YYYY");
                        }
                    }
                },
                legend: {
                    display: true,
                    labels: {
                        fontFamily: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                    }
                },
                title: {
                    display: false
                },
                scales: {
                    xAxes: [{
                        type: "time",
                        time: {
                            displayFormats: {},
                            unit: "day"
                        },
                        gridLines: {
                            color: color.mainColorLight
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Date'
                        }
                    }],
                    yAxes: [{
                        gridLines: {
                            color: [color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight],
                            lineWidth: [1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1],
                        },
                        ticks: {
                            callback: function (value, index, values) {
                                if (index === 10) {
                                    return 'Negative';
                                }
                                if (index === 5) {
                                    return 'Neutral';
                                }
                                if (index === 0) {
                                    return 'Positive';
                                }
                                return "";
                            },
                            min: 0,
                            max: 1,
                            stepSize: 0.1
                        }
                    }]
                },
                layout: {
                    padding: {
                        left: 20,
                        right: 50,
                        top: 0,
                        bottom: 0
                    }
                }

            }
        });
        lineChart.update();
    };

//For every result in state object create an array of datapoints
    var addDataPointsToLineChart = function () {
        var dataPointsArray = [];
        var maxNumberOfDates = 10;

        //Array with objects containing searchinput and tweets
        var allTweetsSearchedFor = generateArrayFromHashmap(state.tweetsSearchedFor);


        for (var i = 0; i < allTweetsSearchedFor.length; i++) {
            var datesToBeAnalyzed = allTweetsSearchedFor[i]["tweets"]["tweets"]["avgSentimentGroupedByDate"];

            var dataPoints = [];
            if (datesToBeAnalyzed.length < maxNumberOfDates) {
                for (var j = 0; j < datesToBeAnalyzed.length; j++) {
                    dataPoints.push({
                        x: moment(datesToBeAnalyzed[j].date, "YYYY-MM-DD"),
                        y: datesToBeAnalyzed[j].avgSentScore.toFixed(2),
                        numberOfTweets: datesToBeAnalyzed[j].numberOfTweetsThisDay
                    });
                }
            } else {
                for (var k = 0; k < maxNumberOfDates; k++) {
                    dataPoints.push({
                        x: moment(datesToBeAnalyzed[j].date, "YYYY-MM-DD"),
                        y: datesToBeAnalyzed[j].avgSentScore.toFixed(2),
                        numberOfTweets: datesToBeAnalyzed[j].numberOfTweetsThisDay
                    });
                }
            }

            dataPointsArray.push({
                dataPoints: dataPoints,
                searchQuery: allTweetsSearchedFor[i]["searchInput"]
            });
        }
        return dataPointsArray;
    };

    var generateArrayFromHashmap = function (tweetmap) {
        var newArray = [];
        for (var key in tweetmap) {
            newArray.push({
                searchInput: key,
                tweets: tweetmap[key]
            });
        }
        return newArray;
    };

    var generateDatasetsFromLineChartDataPoints = function (dataPointsArray) {

        var dataset = [];
        //Generates a dataset from every datapoints-array
        for (var i = 0; i < dataPointsArray.length; i++) {
            dataset.push({
                label: [dataPointsArray[i]["searchQuery"]],
                data: dataPointsArray[i]["dataPoints"],
                lineTension: 0,
                fill: false,
                backgroundColor: getColorBasedOnIndex(i),
                borderColor: getColorBasedOnIndex(i),
                pointBackgroundColor: getColorBasedOnIndex(i),
                pointBorderColor: getColorBasedOnIndex(i)
            });
        }


        return dataset;
    };

    var returnsCleanLineChart = function () {
        var ctx = document.getElementById('lineChart').getContext('2d');
        var lineChart = new Chart(ctx, {
            type: 'line',
            data: {},
            options: {
                legend: {display: true},
                title: {
                    display: false
                },
                scales: {
                    xAxes: [{
                        type: "time",
                        distribution: "linear",
                        time: {
                            displayFormats: {},
                            unit: "day"
                        },
                        gridLines: {
                            color: color.mainColorLight
                        },
                        ticks: {
                            min: 0,
                            max: 100,
                            stepSize: 10
                        }
                    }],
                    yAxes: [{
                        gridLines: {
                            color: [color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight, color.mainColorLight],
                            lineWidth: [1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1]
                        },
                        ticks: {
                            callback: function (value, index, values) {
                                if (index === 10) {
                                    return 'Negative';
                                }
                                if (index === 5) {
                                    return 'Neutral';
                                }
                                if (index === 0) {
                                    return 'Positive';
                                }
                                return "";
                            },
                            min: 0,
                            max: 1,
                            stepSize: 0.1
                        }
                    }]
                },
                layout: {
                    padding: {
                        left: 20,
                        right: 50,
                        top: 0,
                        bottom: 0
                    }
                }

            }
        });
    };

    $("#panelOne").click(function () {
        element = $("has-tip").index($("#chartHelper"));
        content = "To calculate the average sentiment the sentiment scores for all tweets are summed up and divided by the " +
            "number of tweets";
        $(".tooltip").eq(element).html(content);
    });

    $("#panelTwo").click(function () {
        element = $("has-tip").index($("#chartHelper"));
        content = "Tweets are plotted based on their individual sentiment scores. Mouse over a point to see the content of " +
            "the tweet and when it was posted";
        $(".tooltip").eq(element).html(content);
    });

    $("#panelThree").click(function () {
        element = $("has-tip").index($("#chartHelper"));
        content = "Compare the average sentiment related to different keywords";
        $(".tooltip").eq(element).html(content);
    });

    $("#panelFour").click(function () {
        element = $("has-tip").index($("#chartHelper"));
        content = "Shows the average sentiment related to certain keywords over time. Click the colored field just above the chart" +
            " to hide and reveal a line. Mouse over a point to see how many tweets matching that keyword were found that day";
        $(".tooltip").eq(element).html(content);
    });

    $("#panelFive").click(function () {
        element = $("has-tip").index($("#chartHelper"));
        content = "Shows statistics about the current chosen keyword";
        $(".tooltip").eq(element).html(content);
    });


    var updateOverview = function (listOfKeywords) {
        $("#numberOfKeywordsTracked").text("You are tracking " + listOfKeywords.length + " keyword(s).");
        $("#keywordHighestAvgSS").text(" has the highest average SentScore with ");
        $("#keywordLowestAvgSS").text(" has the lowest average SentScore with ");
        $("#totalTweetsForAllKW").text("Total number of tweets analyzed for your keywords combined: ");
    };

    var createPureStatistics = function (searchInput, tweetObjects) {
        $("#tableKeyword").text(searchInput);
        $("#tableNumTweets").text(tweetObjects.tweets.length);
        $("#tableAvgSentiment").text(tweetObjects.averageSentiment.toFixed(5));
        $("#tableSD").text(tweetObjects.standardDeviation.toFixed(5));
        $("#tableMedian").text(tweetObjects.median.toFixed(5));
        $("#tableTimeSpan").text(tweetObjects.avgSentimentGroupedByDate[0].date.slice(0, 10) + " to " +
            tweetObjects.avgSentimentGroupedByDate[tweetObjects.avgSentimentGroupedByDate.length - 1].date.slice(0, 10));
    };

    var clearPureStatistics = function () {
        $("#tableKeyword").empty();
        $("#tableNumTweets").empty();
        $("#tableAvgSentiment").empty();
        $("#tableSD").empty();
        $("#tableMedian").empty();
        $("#tableTimeSpan").empty();
    };

    toggleDisableTrackKeywordsButton(true);
    returnsCleanScatter();
    returnsCleanLineChart();
});