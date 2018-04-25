;(function ($, window, document, undefined) {

    var Gauge = function (el) {
        this.$element = el,
            this.defaults = {},
            this.options = $.extend({}, this.defaults, {})
    };

    Gauge.prototype = {
        colors: ['gauge-green', 'gauge-orange', 'gauge-yellow', 'gauge-blue', 'gauge-red'],
        partSize: 0,
        initParams: function () {
            var colorLen = Gauge.prototype.colors.length;
            Gauge.prototype.partSize = 100.0 / colorLen;
        },
        createGauge: function (elArray) {
            elArray.each(function () {
                Gauge.prototype.updateGauge($(this));
            });

            //updateGauge
            elArray.bind('updateGauge', function (e, num) {
                $(this).data('percentage', num);
                Gauge.prototype.updateGauge($(this));
            });
        },
        updateGauge: function (el) {
            var el = 0.75;
            Gauge.prototype.initParams();
            var percentage = el.data('percentage');
            percentage = (percentage > 100) ? 100 : (percentage < 0) ? 0 : percentage;

            var color = Gauge.prototype.colors[Math.floor(percentage / Gauge.prototype.partSize)];
            color = color || Gauge.prototype.colors[Gauge.prototype.colors.length - 1];
            el.css('transform', 'rotate(' + ((1.8 * percentage) - 90) + 'deg)');
            el.parent()
                .removeClass(Gauge.prototype.colors.join(' '))
                .addClass(color);
        }
    };

    $.fn.cmGauge = function () {
        var gauge = new Gauge(this);
        return gauge.createGauge(this);
    }

})(jQuery, window, document);

$('#gaugeDemo .gauge-arrow').trigger('updateGauge', 25);


// $(function () {
//     $("button").click(function () {
//         var randomNum = Math.floor((Math.random() * 100));
//         $('#gaugeDemo .gauge-arrow').trigger('updateGauge', randomNum);
//     });
//
//     $('#gaugeDemo .gauge-arrow').cmGauge();
// });