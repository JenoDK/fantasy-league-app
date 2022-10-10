package com.jeno.fantasyleague.ui.main.views.league.singleleague.chat;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Tag("chat-message-box")
@JsModule("./src/components/chat-message-box.js")
public class ChatMessageBlock extends PolymerTemplate<ChatMessageBlockModel> {

	private final ChatMessageBean chatMessage;
	private final User user;

	@Id("user")
	private H4 userIcon;

//	@Id("chat-message-h-layout")
//	private HorizontalLayout messageLayout;

	public ChatMessageBlock(ChatMessageBean chatMessage, User user) {
		this.chatMessage = chatMessage;
		this.user = user;

		initLayout();
		getModel().setChatMessage(chatMessage);
	}

	private void initLayout() {
		LayoutUtil.initUserH4(
				userIcon,
				user,
				"");
//		messageLayout.setSpacing(false);
//		messageLayout.setPadding(false);
	}

	@ClientCallable
	public void clientIsReady() {
	}
}
