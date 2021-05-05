import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class NotificationCard extends PolymerElement {
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
					padding: 0 var(--lumo-space-m);
					border-radius: var(--lumo-border-radius);
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
				<vaadin-horizontal-layout id="wrapper" class="wrapper" >
					<h4 id="notificationMsg" class="name"></h4>
                    <vaadin-button id="prediction-button" on-click="acceptNotification" theme="primary">Accept</vaadin-button>
                    <vaadin-button id="prediction-button" on-click="declineNotification" theme="primary">Decline</vaadin-button>
				</vaadin-horizontal-layout>
			</div>
        `;
    }

    static get is() {
        return 'notification-card';
    }

    acceptNotification() {
        this.$server.acceptNotification();
    }

    declineNotification() {
        this.$server.declineNotification();
    }

}

window.customElements.define(NotificationCard.is, NotificationCard);
