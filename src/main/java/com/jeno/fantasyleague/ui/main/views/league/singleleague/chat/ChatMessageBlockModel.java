package com.jeno.fantasyleague.ui.main.views.league.singleleague.chat;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface ChatMessageBlockModel extends TemplateModel {
	void setChatMessage(ChatMessageBean chatMessage);
	ChatMessageBean getChatMessage();
}
