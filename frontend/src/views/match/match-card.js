import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import styles from '../../../styles/statusLabel.css'
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class MatchCard extends PolymerElement {
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
					padding: var(--lumo-space-m);
				}

				.wrapper[theme~="intractable"] {
					background-color: var(--lumo-primary-color-10pct);
					box-shadow: 0 7px 9px var(--lumo-shade-50pct);
					border-bottom: 1px solid var(--lumo-shade-10pct);
					cursor: pointer;
					border-radius: var(--lumo-border-radius);
				}

				.wrapper[theme~="intractable"]:hover {
					background-color: var(--lumo-primary-color-50pct);
					box-shadow: 0 7px 9px var(--lumo-shade-10pct);
					color: var(--lumo-primary-contrast-color);
				}

				.info-wrapper, .prediction-wrapper {
					width: 100%;
					display: flex;
					flex-direction: row;
					align-items: flex-end;
					justify-content: space-between;
				}

				.info-wrapper > h4, .prediction-wrapper > h4 {
					margin: 0px;
				}

				.info-wrapper > .left, .info-wrapper > .right {
					width: 200px;
				}

				.info-wrapper > .right {
					text-align: end;
				}

				#match-wrapper {
					width: 100%;
					display: flex;
					align-items: center;
					justify-content: center;
				}

				#your-prediction-wrapper {
					margin-right: 70px;
				}

				/* Non smartphone */
				@media (min-width: 600px) {
					.content {
						max-width: 1200px;
					}
				}

				/* Smartphone */
				@media (max-width: 600px) {
					.info-wrapper, .prediction-wrapper {
						flex-direction: column;
						align-items: start;
					}

					.info-wrapper > .right, .prediction-wrapper > .right {
						flex-direction: column;
						text-align: start;
					}

					.info-wrapper > h4, .prediction-wrapper > h4 {
						font-size: var(--lumo-font-size-s);
					}

					.prediction-wrapper > .middle {
						margin: 10px 0px;
					}

					#your-prediction-wrapper {
						margin-right: 0px;
					}
				}

				@media (max-width: 400px) {
					#match-wrapper {
						flex-direction: column;
						align-items: start;
					}

					#match-wrapper .left {
						flex-direction: row-reverse;
						margin-left: 5px;
					}

					#match-wrapper .score {
						margin: 5px 70px;
					}
				}

			</style>
			<div id="content" class="content">
				<vaadin-vertical-layout id="wrapper" class="wrapper" on-click="handleClickDiv">
					<div class="info-wrapper">
						<h4 class="left">[[match.date]]</h4>
						<h4 class="middle">[[match.stage]]</h4>
						<h4 class="right">[[match.place]]</h4>
					</div>
					<div id="match-wrapper"></div>
					<div class="prediction-wrapper">
						<vaadin-button id="prediction-button" on-click="openPrediction" theme="primary">Fill in
							prediction
						</vaadin-button>
						<div id="your-prediction-wrapper" class="middle"></div>
						<h4 class="right">[[match.pointsGained]]</h4>
					</div>
				</vaadin-vertical-layout>
			</div>
        `;
    }

    static get is() {
        return 'match-card';
    }

    handleClickDiv() {
        this.$server.handleClick();
    }

    openPrediction(event) {
        this.$server.openPrediction();
        event.stopPropagation();
    }

}

window.customElements.define(MatchCard.is, MatchCard);
