import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';
import {GoogleCharts} from 'google-charts';

class ScoreChart extends PolymerElement {
    static get template() {
        // language=HTML
        return html`
			<style include="shared-styles">
				:host {
					display: block;
					width: 100%;
				}

				.content {
					display: block;
					width: 100%;
					margin-left: auto;
					margin-right: auto;
				}

				.wrapper {
					display: flex;
					flex-direction: column;
					width: 100%;
					padding: var(--lumo-space-s);
				}

				.wrapper {
					background-color: var(--lumo-tint-10pct);
					border-bottom: 1px solid var(--lumo-shade-10pct);
					border-radius: var(--lumo-border-radius);
				}

				#chart {
					width: 100%;
					height: 600px;
				}

				#chart svg > g:nth-child(2) > g:nth-child(2) > g:nth-child(1) rect, #chart svg > g:nth-child(2) > g:nth-child(2) > g:nth-child(3) rect {
					/*fill: transparent;*/
				}

				.google-visualization-tooltip {
                    position: absolute;
                }
                
                .chart-tooltip {
                    padding: 5px;
                    background-color: #8ab5ed;
                    border: #10151c 2px solid;
                    border-radius: 3px;
                }
                
                .chart-tooltip span {
					font-family: var(--lumo-font-family);
					font-size: var(--lumo-font-size-m);
					line-height: var(--lumo-line-height-s);
					color: var(--lumo-body-text-color);
					font-weight: bold;
                }

				/* Non smartphone */
				@media (min-width: 600px) {
					.content {
						max-width: 1200px;
					}
				}

				/* Smartphone */
				@media (max-width: 600px) {

				}
			</style>
			<div id="content" class="content">
				<div id="wrapper" class="wrapper">
					<div id="chart"></div>
				</div>
			</div>
        `;
    }

    static get is() {
        return 'score-chart';
    }

    ready() {
        super.ready();
        requestAnimationFrame(() => {
            GoogleCharts.load(() => this.clientIsReady(), {'packages':['corechart', 'bar', 'line']});
        });
    }

    clientIsReady() {
        this.$server.clientIsReady();
    }

    drawHBarChart() {
        var chartData = this.scoresPerUser.map(function(x) {
            return [x.name].concat(x.scores).concat(x.dataRoles);
        });
        this.dataRoles
            .filter(dr => dr.role === 'tooltip')
            .forEach(dr => dr.p = {html: true});
        var chartH = this.chartHeaders.concat(this.dataRoles);
        chartData.splice(0, 0, chartH);
        var data = GoogleCharts.api.visualization.arrayToDataTable(chartData);
        var options = {
            chartArea: {
                width: '95%',
                height: '80%',
                left: 50,
                top: 20,
                backgroundColor: 'transparent'
            },
            backgroundColor: {fill: 'transparent'},
            bar: {groupWidth: "60%"},
            legend: { position: "none" },
            vAxis: {
                viewWindow: {
                    max:this.highestScore,
                    min:0
                },
                baselineColor: 'transparent',
                gridlines: {
                    color: 'transparent'
                },
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
            hAxis: {
                title: "",
                baselineColor: 'transparent',
                gridlines: {
                    color: 'transparent'
                },
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
            tooltip: { isHtml: true },
        };

        var chart = new GoogleCharts.api.visualization.ColumnChart(this.root.getElementById('chart'));
        var positionUserWithProfilePicture = new Map(this.scoresPerUser.filter(obj => obj.hasUserProfilePicture).map(obj => [obj.position, obj.userId]));
        var colors = this.colors;

        var observer = new MutationObserver(setBorderRadius);
        GoogleCharts.api.visualization.events.addListener(chart, 'ready', function () {
            setBorderRadius();
            observer.observe(chart.container, {
                childList: true,
                subtree: true
            });
            Array.prototype.forEach.call(chart.container.getElementsByTagName('rect'), function (rect, index) {
                var color = rect.getAttribute('fill');
                if (color != null) {
                    color = color.toLowerCase();
                }
                if (colors.includes(color)) {
                    var xPos = parseFloat(rect.getAttribute('x'));
                    var yPos = parseFloat(rect.getAttribute('y'));
                    var imageSize = 50;
                    let x = (xPos + (rect.getAttribute('width') / 2)) - (imageSize / 2);
                    var y = yPos - (imageSize / 2);
                    // If there's no score then show the icon above the line
                    if (rect.getAttribute('height') < 1) {
                        y = yPos - imageSize - 3;
                    }
                    var image = rect.parentElement.appendChild(document.createElementNS('http://www.w3.org/2000/svg', 'image'));
                    var profilePictureLink = '';
                    if (positionUserWithProfilePicture.has(index - 1)) {
                        profilePictureLink = '/profileImage?userPk=' + positionUserWithProfilePicture.get(index - 1);
                    } else {
                        profilePictureLink = "/images/default_profile_picture.png";
                    }
                    image.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', profilePictureLink);
                    image.setAttribute('x', x);
                    image.setAttribute('y', y);
                    image.setAttribute("preserveAspectRatio", "none");
                    image.setAttribute('class', 'profileImage');
                    image.setAttribute('width', imageSize);
                    image.setAttribute('height', imageSize);
                }
            });
        });

        function setBorderRadius() {
            Array.prototype.forEach.call(chart.container.getElementsByTagName('rect'), function (rect, index) {
                var color = rect.getAttribute('fill');
                if (color != null) {
                    color = color.toLowerCase();
                }
                if (parseFloat(rect.getAttribute('x')) > 0 && colors.includes(color)) {
                    rect.setAttribute('rx', '0.25em');
                    rect.setAttribute('ry', '0.25em');
                }
            });
        }

        chart.draw(data, options);
    }
    
    drawCountryFlagChart() {

        var chartData = this.scoresPerUser.map(function(x) {
            return [x.name].concat(x.scores);
        });
        chartData.splice(0, 0, this.chartHeaders);
        var data = GoogleCharts.api.visualization.arrayToDataTable(chartData);
        var seriesConfig = this.seriesData.map(function(x) {
            return {color: x.color};
        });

        var options = {
            backgroundColor: {fill: 'transparent'},
            isStacked: true,
            legend: {position: 'none'},
            series: seriesConfig,
            chartArea: {
                width: '95%',
                height: '80%',
                left: 50,
                top: 20,
                backgroundColor: 'transparent'
            },
            vAxis: {
                showTextEvery: 4,
                baselineColor: 'transparent',
                gridlines: {
                    color: 'transparent'
                },
                viewWindowMode:'explicit',
                viewWindow: {
                    max:this.highestScore,
                    min:0
                },
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
            hAxis: {
                baselineColor: 'transparent',
                gridlines: {
                    color: 'transparent'
                },
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
        };

        var chart = new GoogleCharts.api.visualization.ColumnChart(this.root.getElementById('chart'));
        var colors = this.colors;
        var iconColorsMap = new Map(this.iconColors.map(obj => [obj.color, obj.iconPath]));

        GoogleCharts.api.visualization.events.addListener(chart, 'ready', function () {

            var observer = new MutationObserver(function () {
                var svgArray = chart.container.getElementsByTagName('svg');
                if (svgArray.length > 0) {
                    svgArray[0].setAttribute('xmlns', 'http://www.w3.org/2000/svg');
                }
                var chartWidth = chart.container.offsetWidth;
                var chartHeight = chart.container.offsetHeight;
                var defs = chart.container.getElementsByTagName('defs')[0];
                Array.prototype.forEach.call(chart.container.getElementsByTagName('rect'), function (rect) {
                    var color = rect.getAttribute('fill');
                    if (color != null) {
                        color = color.toUpperCase();
                    }
                    var iconPath = iconColorsMap.get(color);
                    if (typeof iconPath !== 'undefined') {
                        var x = rect.getAttribute('x');
                        var y = rect.getAttribute('y');
                        var width = rect.getAttribute('width');
                        var height = rect.getAttribute('height');
                        var id = x + y;
                        addPattern(id, x, y, width, height, chartWidth, chartHeight, iconPath, defs);
                        rect.setAttribute('fill', 'url(#' + id + ')');
                    }
                });
            });
            observer.observe(chart.container, {
                childList: true,
                subtree: true
            });

            function addPattern(id, x, y, width, height, chartWidth, chartHeight, imagePath, defs) {
                var pattern = document.createElementNS('http://www.w3.org/2000/svg', 'pattern');
                pattern.setAttribute('id', id);
                pattern.setAttribute('patternUnits', 'userSpaceOnUse');
                pattern.setAttribute('width', chartWidth);
                pattern.setAttribute('height', chartHeight);

                var image = document.createElementNS('http://www.w3.org/2000/svg', 'image');
                image.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', imagePath);
                image.setAttribute('x', x);
                image.setAttribute('y', y);
                image.setAttribute("preserveAspectRatio", "none");
                image.setAttribute('width', width);
                image.setAttribute('height', height);

                pattern.appendChild(image);
                defs.appendChild(pattern);
            }

        });

        chart.draw(data, options);
    }

    drawLineChart() {

        var chartData = this.scoresPerDate.map(function(x) {
            return [x.date].concat(x.scores);
        });
        chartData.splice(0, 0, this.chartHeaders);
        var data = GoogleCharts.api.visualization.arrayToDataTable(chartData);

        var options = {
            backgroundColor: {fill: 'transparent'},
            chartArea: {
                width: '95%',
                height: '80%',
                left: 50,
                top: 20,
                backgroundColor: 'transparent'
            },
            legend: {
                position: 'bottom',
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
            pointSize: 6,
            vAxis: {
                showTextEvery: 4,
                baselineColor: 'transparent',
                gridlines: {
                    color: 'transparent'
                },
                viewWindowMode:'explicit',
                viewWindow: {
                    max:this.highestScore,
                    min:0
                },
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
            hAxis: {
                baselineColor: 'transparent',
                gridlines: {
                    color: 'transparent'
                },
                textStyle: {
                    color: '#EBF3FF',
                    fontName: '-apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"',
                    fontSize: 16,
                    bold: true
                }
            },
        };

        var chart = new GoogleCharts.api.visualization.LineChart(this.root.getElementById('chart'));

        GoogleCharts.api.visualization.events.addListener(chart, 'ready', function () {

            var observer = new MutationObserver(function () {
            });
            observer.observe(chart.container, {
                childList: true,
                subtree: true
            });

        });

        chart.draw(data,options);
    }

}

window.customElements.define(ScoreChart.is, ScoreChart);
