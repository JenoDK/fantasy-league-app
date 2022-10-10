import {PolymerElement} from "@polymer/polymer";
import {html} from "@polymer/polymer/lib/utils/html-tag";
import emojiRegex from "emoji-regex";

class ChatMessageBox extends PolymerElement {
    static get template() {
        // language=HTML
        return html`
			<style include="shared-styles">

				:host {
					display: block;
					width: 100%;
				}
                
                .chat-message-main {
					display: flex;
					flex-direction: row;
                }
                
                .chat-message {
					background-color: var(--lumo-primary-color-50pct);
					color: var(--lumo-primary-contrast-color);
					border-radius: 8px;
                }
                
                .right {
                    margin-left: auto;
                    justify-content: flex-end;
                    max-width: 90%;
				}
                
                .info-header {
					display: flex;
					flex-direction: row;
					justify-content: space-between;
                    margin: 4px 8px 4px 8px;
                }
                
				.right .info-header .nickname {
					order: 99;
                    margin-left: 20px;
				}

				.left .info-header .nickname {
					margin-right: 20px;
				}
                
                .info-header > p {
                    margin: 0px;
					font-size: var(--lumo-font-size-xs);
					color: var(--lumo-secondary-text-color);
                }

                .iconLabel {
					display: flex;
					align-items: center;
                    height: auto;
					margin: 4px;
                }

				.right .iconLabel {
					float: right;
				}

				.left .iconLabel {
					float: left;
				}
                
                .message {
                    margin: 4px;
                }

				.message.emoji {
					font-size: 36px;
				}

				.right .message {
					margin-left: 8px;
				}

				.left .message {
					margin-right: 8px;
				}
                
			</style>

			<div class="chat-message-main">
                <div class$="chat-message {{_getUserIconClassName()}}">
                    <div class="info-header">
						<p class="nickname">[[chatMessage.username]]</p>
                        <p class="timestamp">[[chatMessage.timeSent]]</p>
                    </div>
                    <h4 id="user" class="iconLabel"></h4>
                    <p class$="message {{_isOnlyOneEmoji()}}">[[chatMessage.message]]</p>
                </div>
			</div>
        `;
    }

    _getUserIconClassName(){
        return this.chatMessage.isFromLoggedInUser ? "right" : "left";
    }

    _isOnlyOneEmoji() {
        const text = this.chatMessage.message;
        const regex = emojiRegex();
        const replaced = text.replaceAll(regex, '');
        if (replaced.length === 0) {
            const allEmojis = text.matchAll(regex);
            let iterationCount = 0;
            for (const match of allEmojis) {
                iterationCount++;
            }
            if (iterationCount <= 2) {
                return "emoji";
            }
        }
        return "";
    }

    static get is() {
        return 'chat-message-box';
    }

    ready() {
        super.ready()
        requestAnimationFrame(() => {
            this.clientIsReady();
        });
    }

    clientIsReady() {
        this.$server.clientIsReady();
    }
}

window.customElements.define(ChatMessageBox.is, ChatMessageBox);