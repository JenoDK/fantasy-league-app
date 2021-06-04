import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class StockCard extends PolymerElement {
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
					flex-direction: row;
					justify-content: space-between;
					width: 100%;
					padding: var(--lumo-space-s);
				}

				.wrapper {
					background-color: var(--lumo-tint-10pct);
					border-bottom: 1px solid var(--lumo-shade-10pct);
					border-radius: var(--lumo-border-radius);
				}

				#buyStocks-button {
					margin-top: 10px;
				}

				.right h4, .right h5 {
					text-align: end;
				}

				h4, h5 {
					margin: 5px 0px 5px 0px;
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
					<div class="left">
						<div id="teamLayout"></div>
						<vaadin-button id="buyStocks-button" on-click="buyStocks" theme="primary"></vaadin-button>
					</div>
					<div class="right">
						<h4 id="pricePerStock"></h4>
						<h4 id="stocksPurchased"></h4>
						<h5 id="stocksLeft"></h5>
					</div>
				</div>
			</div>
        `;
    }

    static get is() {
        return 'stock-card';
    }

    buyStocks() {
        this.$server.buyStocks();
    }

}

window.customElements.define(StockCard.is, StockCard);
