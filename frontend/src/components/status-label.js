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
					border-radius: var(--lumo-border-radius);
				}

				.show-icon.success, .show-icon.error, .show-icon.warning {
					padding-left: var(--lumo-size-m);
				}

				.show-icon.error::before, .show-icon.success::before, .show-icon.warning::before {
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
                
                * > iron-icon {
                    margin-right: 10px;
                }

				.show-icon.success::before {
					content: var(--lumo-icons-checkmark);
				}

				.show-icon.error::before {
					content: var(--lumo-icons-error);
				}

				.show-icon.warning::before {
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
