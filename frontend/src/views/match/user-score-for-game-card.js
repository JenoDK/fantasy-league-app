import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class UserScoreForGameCard extends PolymerElement {
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
					padding: var(--lumo-space-m);
				}

				.wrapper {
					background-color: var(--lumo-tint-10pct);
					border-bottom: 1px solid var(--lumo-shade-10pct);
					border-radius: var(--lumo-border-radius);
				}

				#top {
					width: 100%;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
				}

				#bottom {
					width: 100%;
				}

				#prediction {
					margin-bottom: 10px;
				}

				.wrapper h4 {
					margin: 0px;
				}

				.wrapper .iconLabel {
					display: flex;
					align-items: center;
					margin: 0px 0px 15px 0px;
				}

				.wrapper .iconLabel > img {
					margin-right: 10px;
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
					<div id="top">
						<div class="left">
							<h4 id="user" class="iconLabel"></h4>
						</div>
						<div class="right">
							<span id="stocksTeam1" class="iconLabel"></span>
							<span id="stocksTeam2" class="iconLabel"></span>
						</div>
					</div>
					<div id="bottom">
						<div id="prediction"></div>
						<h4 id="points"></h4>
					</div>
				</div>
			</div>
        `;
    }

    static get is() {
        return 'user-score-for-game-card';
    }

    kudo() {
        this.$server.kudo();
    }

}

window.customElements.define(UserScoreForGameCard.is, UserScoreForGameCard);
