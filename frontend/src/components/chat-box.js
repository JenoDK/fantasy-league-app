import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import '../../styles/shared-styles.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';
import {Picker} from 'emoji-mart';

class ChatBox extends PolymerElement {
    static get template() {
        // language=HTML
        return html`
			<style include="shared-styles">
				:host {
					display: block;
					width: 100%;
				}

				.chat-box {
					position: absolute;
					bottom: 5px;
                    margin-left: -55px;
                    z-index: 195;
				}

				@media (max-width: 1300px) {
					.chat-box {
						bottom: 5px;
						margin-left: 5px;
                        left: 0px;
					}
				}

				@media (max-width: 800px) {
					.chat-box {
						bottom: 37px;
					}
				}

				#open-chat-box {
					background-color: var(--lumo-base-color);
					border: 2px solid var(--lumo-primary-color);
					border-radius: 8px;
				}

				#unread-notification {
					/* circle shape, size and position */
					position: absolute;
					right: -0.7em;
					top: -0.7em;
					min-width: 1.6em; /* or width, explained below. */
					height: 1.6em;
					border-radius: 0.8em; /* or 50%, explained below. */
					background-color: red;

					/* number size and position */
					display: flex;
					justify-content: center;
					align-items: center;
					font-size: 0.8em;
					color: white;
                }
                
				#open-chat-box-topbar {
					border-bottom: 1px solid var(--lumo-primary-color);
				}
                
                #open-chat-box-messages {
                    padding: 5px 10px 5px 10px;
                    overflow-y: scroll;
                    overflow-anchor: none;
                }

				#open-chat-box-send-messages {
					border-top: 1px solid var(--lumo-primary-color);
				}

				#send-messages-textarea {
					margin: 5px;
					width: 100%;
				}

				#send-messages-button {
					margin-right: 5px;
				}

				#choose-smiley-button {
					position: absolute;
					right: 110px;
				}

				em-emoji-picker {
					position: absolute;
					bottom: 75px;
					z-index: 2;
                }
			</style>

			<div id="chat-box" class="chat-box">
				<vaadin-button id="open-chat-box-button" on-click="openChatBox" theme="primary icon large">
					<iron-icon icon="vaadin:chat"></iron-icon>
					<div id="unread-notification" role="status"></div>
				</vaadin-button>
				<vaadin-vertical-layout id="open-chat-box" class="open">
					<vaadin-horizontal-layout id="open-chat-box-topbar" class="topbar">

					</vaadin-horizontal-layout>
					<vaadin-vertical-layout id="open-chat-box-messages" class="messages">

					</vaadin-vertical-layout>
					<vaadin-horizontal-layout id="open-chat-box-send-messages" class="send-messages">
						<vaadin-text-area id="send-messages-textarea" on-keydown="checkForEnter"></vaadin-text-area>
						<vaadin-button id="choose-smiley-button" theme="icon" aria-label="Choose smiley" on-click="openSmileyPicker" >
							<iron-icon icon="vaadin:smiley-o"></iron-icon>
						</vaadin-button>
						<vaadin-button id="send-messages-button" theme="primary" on-click="sendMessage">Send</vaadin-button>
					</vaadin-horizontal-layout>
				</vaadin-vertical-layout>
			</div>
        `;
    }

    static get is() {
        return 'chat-box';
    }

    ready() {
        super.ready();
    }

    checkForEnter(e) {
        // check if 'enter' was pressed
        if (e.keyCode === 13 && !e.altKey && !e.shiftKey) {
            e.preventDefault();
            this.sendMessage();
        }
    }

    sendMessage() {
        const textArea = this.root.getElementById("send-messages-textarea");
        if (textArea.value) {
            this.$server.sendMessage(textArea.value);
            textArea.clear();
            this.closeSmileyPicker();
        }
    }

    scrollToBottom() {
        const messagesElement = this.root.getElementById("open-chat-box-messages");
        messagesElement.scroll({ top: messagesElement.scrollHeight, behavior: 'auto' });
    }

    closeSmileyPicker() {
        const existingSmileyPickers = this.root.getElementById("emoji-picker");
        if (existingSmileyPickers) {
            existingSmileyPickers.remove();
        }
        this.root.getElementById("send-messages-textarea").focus();
    }

    openSmileyPicker() {
        const existingSmileyPickers = this.root.getElementById("emoji-picker");
        if (existingSmileyPickers) {
            existingSmileyPickers.remove();
        } else {
            const serverObj = this.$server;
            requestAnimationFrame(() => {
                const pickerOptions = {onEmojiSelect: emoji => {
                        serverObj.appendTextArea(emoji.native);
                        this.closeSmileyPicker();
                    }};
                const picker = new Picker(pickerOptions)
                picker.id = "emoji-picker";
                const openChatBox = this.root.getElementById('open-chat-box');
                openChatBox.appendChild(picker);
            });
        }
    }

    openChatBox() {
        this.$server.openChatBox();
    }

}

window.customElements.define(ChatBox.is, ChatBox);
