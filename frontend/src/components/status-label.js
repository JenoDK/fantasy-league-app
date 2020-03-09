import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class StatusLabel extends PolymerElement {
    static get template() {
        // language=HTML
        return html`
			<style include="shared-styles">
				.success, .error, .warning {
					padding: var(--lumo-space-s);
					padding-left: var(--lumo-size-m);
					border-radius: var(--lumo-border-radius);
					margin-top: var(--lumo-space-m);
					margin-bottom: var(--lumo-space-s);
				}

				.error::before, .success::before, .warning::before {
					font-family: lumo-icons;
					font-size: var(--lumo-icon-size-m);
					width: var(--lumo-size-m);
					height: 1em;
					line-height: 1;
					text-align: center;
					margin-left: calc(var(--lumo-size-m) * -0.95);
				}

				.success {
					background-color: var(--lumo-success-color-10pct);
					color: var(--lumo-success-text-color);
				}

				.error {
					background-color: var(--lumo-error-color-10pct);
					color: var(--lumo-error-text-color);
				}

				.warning {
					background-color: var(--lumo-error-color-10pct);
					color: var(--lumo-primary-text-color);
				}

				.success::before {
					content: var(--lumo-icons-checkmark);
				}

				.error::before {
					content: var(--lumo-icons-error);
				}

				.warning::before {
					content: var(--lumo-icons-error);
				}
			</style>

			<label id="label"></label>
        `;
    }

    static get is() {
        return 'status-label';
    }

}

window.customElements.define(StatusLabel.is, StatusLabel);
