package com.jeno.fantasyleague.ui.main.views.league.singleleague.chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueMessage;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.broadcast.ChatBroadcaster;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("chat-box")
@JsModule("./src/components/chat-box.js")
public class ChatBox extends PolymerTemplate<TemplateModel> {

	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final League league;
	private final LeagueUser loggedInLeagueUser;
	Registration broadcasterRegistration;

	private boolean hasBeenOpened = false;
	private boolean open = false;
	private int amountOfUnreadMessages = 0;

	@Id("chat-box")
	private Div mainLayout;

	@Id("open-chat-box-button")
	private Button openChatboxButton;

	@Id("unread-notification")
	private Div unreadDiv;

	@Id("open-chat-box")
	private VerticalLayout chatLayout;

	@Id("open-chat-box-topbar")
	private HorizontalLayout topBar;

	@Id("open-chat-box-messages")
	private VerticalLayout messagesLayout;

	@Id("open-chat-box-send-messages")
	private HorizontalLayout sendMessageLayout;

	@Id("send-messages-textarea")
	private TextArea textArea;

	@Id("send-messages-button")
	private Button sendButton;

	public ChatBox(League league, LeagueUser loggedInLeagueUser, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		this.league = league;
		this.loggedInLeagueUser = loggedInLeagueUser;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;

		this.amountOfUnreadMessages = loggedInLeagueUser.getUnread_messages();

		initLayout();
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		UI ui = attachEvent.getUI();
		broadcasterRegistration = ChatBroadcaster.register(league.getId(), newMessage -> {
			try {
				ui.access(() -> {
					addMessage(newMessage);
				});
			} catch (UIDetachedException e) {
				removeChatSubscription();
			}
		});
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		removeChatSubscription();
	}

	private void removeChatSubscription() {
		broadcasterRegistration.remove();
		broadcasterRegistration = null;
	}

	private void addMessage(ChatMessage newMessage) {
		boolean isFromLoggedInUser = newMessage.getUser().getId().equals(singleLeagueServiceprovider.getLoggedInUser().getId());
		ChatMessageBlock block = new ChatMessageBlock(new ChatMessageBean(
				newMessage.getUser().getUsername(),
				newMessage.getMessage(),
				DateUtil.formatInUserTimezone(newMessage.getTimeSent()),
				isFromLoggedInUser
		), newMessage.getUser());
		messagesLayout.add(block);
		scrollToBottomOfMessages();
		if (!open && !isFromLoggedInUser) {
			amountOfUnreadMessages++;
			setMessagesUnread();
		}
	}

	private void setMessagesUnread() {
		if (amountOfUnreadMessages > 0) {
			unreadDiv.setVisible(true);
			unreadDiv.setText(String.valueOf(amountOfUnreadMessages));
		} else {
			unreadDiv.setVisible(false);
			unreadDiv.setText("");
		}
	}

	private void initLayout() {
		mainLayout.addClassName("chat-box");

		openChatboxButton.addClickListener(ignored -> openChatBox());
		initChatLayout();
		minimize();
	}

	public boolean isOpen() {
		return open;
	}

	@ClientCallable
	private void openChatBox() {
		if (!hasBeenOpened) {
			createTopBar();
			createMessagesLayout();
			createSendMessagesLayout();
			loadMessages();
		}
		open = true;
		amountOfUnreadMessages = 0;
		chatLayout.setVisible(true);
		openChatboxButton.setVisible(false);
		unreadDiv.setVisible(false);
		scrollToBottomOfMessages();
		singleLeagueServiceprovider.resetUnreadMessages(loggedInLeagueUser);
		hasBeenOpened = true;
	}

	private void loadMessages() {
		singleLeagueServiceprovider.getLeagueMessages(league).stream()
				.sorted(Comparator.comparing(LeagueMessage::getCreatedAt))
				.map(leagueMessage -> {
					User user = leagueMessage.getCreatedBy();
					boolean isFromLoggedInUser = user.getId().equals(singleLeagueServiceprovider.getLoggedInUser().getId());
					return new ChatMessageBlock(new ChatMessageBean(
							user.getUsername(),
							leagueMessage.getMessage(),
							DateUtil.formatInUserTimezone(LocalDateTime.ofInstant(leagueMessage.getCreatedAt(), ZoneId.systemDefault())),
							isFromLoggedInUser
					), user);
				})
				.forEach(messagesLayout::add);
	}

	private void scrollToBottomOfMessages() {
		getElement().callJsFunction("scrollToBottom");
	}

	@ClientCallable
	private void appendTextArea(String emoji) {
		textArea.setValue(textArea.getValue() + emoji);
	}

	@ClientCallable
	private void sendMessage(String message) {
		ChatBroadcaster.broadcastChatMessage(league.getId(), new ChatMessage(singleLeagueServiceprovider.getLoggedInUser(), message, LocalDateTime.now()));
		textArea.clear();
		singleLeagueServiceprovider.addMessage(message, league);
	}

	public void minimize() {
		chatLayout.setVisible(false);
		openChatboxButton.setVisible(true);
		open = false;
	}

	private void initChatLayout() {
		chatLayout.setHeight(600, Unit.PIXELS);
		chatLayout.setWidth(350, Unit.PIXELS);
		chatLayout.setPadding(false);
		chatLayout.setSpacing(false);
		chatLayout.setMargin(false);

		setMessagesUnread();
	}

	private void createSendMessagesLayout() {
		sendMessageLayout.setWidthFull();
		sendMessageLayout.setSpacing(false);
		sendMessageLayout.setAlignItems(FlexComponent.Alignment.END);
		sendMessageLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		sendMessageLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);

		openChatboxButton.addThemeVariants(ButtonVariant.LUMO_ICON);

		textArea.setMaxHeight("80px");
	}

	private void createMessagesLayout() {
		messagesLayout.setHeightFull();
		messagesLayout.setWidthFull();
		messagesLayout.setPadding(false);
	}

	private void createTopBar() {
		topBar.setWidthFull();
		topBar.setSpacing(false);
		topBar.setHeight(30, Unit.PIXELS);
		topBar.setAlignItems(FlexComponent.Alignment.END);
		topBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

		Label chatLabel = new Label("League Chat");
		chatLabel.setWidthFull();
		chatLabel.getStyle().set("margin-left", "5px");
		topBar.add(chatLabel);

		CustomButton minimize = new CustomButton(VaadinIcon.MINUS);
		minimize.addThemeName("large-for-mobile large tertiary");
		minimize.setIconAfterText(true);
		minimize.addClickListener(ignored -> minimize());
		topBar.add(minimize);
	}

}
