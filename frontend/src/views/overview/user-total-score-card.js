import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class UserTotalScoreCard extends PolymerElement {
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

				.wrapper .iconLabel {
					display: flex;
					align-items: center;
					margin: 0px 0px 15px 0px;
					word-wrap: break-word;
					white-space: normal;
				}

				.wrapper .iconLabel > img {
					margin-right: 10px;
				}

				#top {
					width: 100%;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
				}

				.wrapper h4 {
					margin: 0px;
				}

				.wrapper .smallScore {
					font-size: var(--lumo-font-size-xs);
				}
                
                .left {
                    max-width: 20%;
                }

				.right {
					display: flex;
					flex-direction: column;
				}

				.right span, .right h4 {
					text-align: end;
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
							<span id="groupScore" class="smallScore"></span>
							<span id="eighthFinals" class="smallScore"></span>
							<span id="quarterFinals" class="smallScore"></span>
							<span id="semiFinals" class="smallScore"></span>
							<span id="finals" class="smallScore"></span>
							<h4 id="total"></h4>
						</div>
					</div>
				</div>
			</div>
        `;
    }

    static get is() {
        return 'user-total-score-card';
    }

    kudo() {
        this.$server.kudo();
    }

}

window.customElements.define(UserTotalScoreCard.is, UserTotalScoreCard);
