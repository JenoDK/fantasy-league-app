import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class LeagueCard extends PolymerElement {
    static get template() {
        // language=HTML
        return html`
			<style include="shared-styles">
				:host {
					display: block;
				}

				.content {
					display: block;
					width: 100%;
					margin-left: auto;
					margin-right: auto;
				}

				.wrapper {
					background-color: var(--lumo-primary-color-10pct);
					box-shadow: 0 7px 9px var(--lumo-shade-50pct);
					border-bottom: 1px solid var(--lumo-shade-10pct);
					display: flex;
					padding: var(--lumo-space-l) var(--lumo-space-m);
					cursor: pointer;
				}

				.wrapper:hover {
					background-color: var(--lumo-primary-color-50pct);
					box-shadow: 0 7px 9px var(--lumo-shade-10pct);
					color: var(--lumo-primary-contrast-color);
				}

				.main {
					color: var(--lumo-secondary-text-color);
					margin-right: var(--lumo-space-s);
					font-weight: bold;
				}

				.info-wrapper {
					width: 100%;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
				}

				.minor_info {
					display: flex;
					flex-direction: column;
				}

				.name {
                    margin-top: 0;
					word-break: break-all;
					/* Non standard for WebKit */
					word-break: break-word;
					white-space: normal;
				}

				#leagueImageTag {
					max-height: 300px;
					object-fit: cover;
					width: 100%;
				}

				@media (min-width: 600px) {
					.wrapper {
						border-radius: var(--lumo-border-radius);
					}

					.content {
						max-width: 1200px;
					}
				}

				@media (max-width: 600px) {
					.info-wrapper {
						flex-direction: column;
					}
				}

				.top-3-grid {
					padding: var(--lumo-space-s) var(--lumo-space-s);
					border-radius: var(--lumo-border-radius);
					cursor: pointer;
					pointer-events: none;
				}
			</style>
			<div id="content" class="content">
				<vaadin-vertical-layout id="wrapper" class="wrapper">
                    <img id="leagueImageTag" />
					<div id="info" class="info-wrapper">
						<h2 id="leagueName" class="name"></h2>
						<div class="minor_info">
							<span id="adminName"></span>
							<span id="membersCount"></span>
						</div>
					</div>
				</vaadin-vertical-layout>
			</div>
        `;
    }

    static get is() {
        return 'league-card';
    }

    ready() {
        super.ready();
    }
}

window.customElements.define(LeagueCard.is, LeagueCard);
