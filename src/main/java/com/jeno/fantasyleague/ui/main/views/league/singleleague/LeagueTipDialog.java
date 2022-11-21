package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.HtmlLabel;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.subjects.BehaviorSubject;

public class LeagueTipDialog extends VerticalLayout {

	private final LeagueBean leagueBean;
	private final LeagueUser leagueUser;
	private BehaviorSubject<LeagueUser> hideTips = BehaviorSubject.create();
	private BehaviorSubject<LeagueUser> skipTip = BehaviorSubject.create();
	private PopupWindow popupWindow;

	public LeagueTipDialog(LeagueBean leagueBean, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		this.leagueBean = leagueBean;
		this.leagueUser = singleLeagueServiceprovider.getLoggedInLeagueUser(leagueBean.getLeague());

		initLayout();

		hideTips.subscribe(lu -> {
			close();
			singleLeagueServiceprovider.getLeagueUserRepository().saveAndFlush(lu);
		});
		skipTip.subscribe(lu -> singleLeagueServiceprovider.getLeagueUserRepository().saveAndFlush(lu));
	}

	private void initLayout() {
		setMargin(false);
		setSpacing(false);
		setPadding(false);
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.END);

		Label caption = new Label();
		caption.getStyle().set("vertical-align", "middle");
		caption.getStyle().set("font-weight", "bold");
		caption.getStyle().set("margin-left", "10px");
		caption.getStyle().set("color", "var(--lumo-tertiary-text-color)");

		HtmlLabel infoLabel = new HtmlLabel();
		infoLabel.getStyle().set("margin", "15px");
		infoLabel.getStyle().set("overflow-y", "auto");
		infoLabel.setHeightFull();

		changeStage(caption, infoLabel, leagueUser.getHelp_stage());

		CustomButton skip = new CustomButton(VaadinIcon.ARROW_RIGHT, "");
		skip.addThemeName("small-for-mobile small");
		skip.setIconAfterText(true);
		if (leagueUser.getHelp_stage().isLastStage()) {
			skip.setEnabled(false);
		}
		CustomButton previous = new CustomButton(VaadinIcon.ARROW_LEFT, "");
		previous.addThemeName("small-for-mobile small");
		if (leagueUser.getHelp_stage().isFirstStage()) {
			previous.setEnabled(false);
		}
		skip.addClickListener(ignored -> {
			leagueUser.getHelp_stage().getNextStage()
					.ifPresentOrElse(
							nextStage -> {
								leagueUser.setHelp_stage(nextStage);
								changeStage(caption, infoLabel, nextStage);
							},
							() -> skip.setEnabled(false)
					);
			if (leagueUser.getHelp_stage().getPreviousStage().isPresent()) {
				previous.setEnabled(true);
			}
			if (leagueUser.getHelp_stage().isLastStage()) {
				skip.setEnabled(false);
			}
			skipTip.onNext(leagueUser);
		});
		previous.addClickListener(ignored -> {
			leagueUser.getHelp_stage().getPreviousStage()
					.ifPresentOrElse(
							previousStage -> {
								leagueUser.setHelp_stage(previousStage);
								changeStage(caption, infoLabel, previousStage);
							},
							() -> skip.setEnabled(false)
					);
			if (leagueUser.getHelp_stage().getNextStage().isPresent()) {
				skip.setEnabled(true);
			}
			if (leagueUser.getHelp_stage().isFirstStage()) {
				previous.setEnabled(false);
			}
			skipTip.onNext(leagueUser);
		});
		CustomButton doNotShow = new CustomButton(VaadinIcon.CLOSE);
		doNotShow.addThemeName("small-for-mobile small tertiary");
		doNotShow.setIconAfterText(true);
		doNotShow.addClickListener(ignored -> {
			leagueUser.setShow_help(false);
			hideTips.onNext(leagueUser);
		});
		HorizontalLayout bottomBar = createBasicBar();
		bottomBar.setAlignItems(FlexComponent.Alignment.END);
		bottomBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

		HorizontalLayout topBar = createBasicBar();
		topBar.setAlignItems(FlexComponent.Alignment.END);
		topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		topBar.setPadding(false);
		topBar.add(caption, doNotShow);
		bottomBar.add(previous, skip);

		add(topBar, infoLabel, bottomBar);
	}

	private void changeStage(Label caption, HtmlLabel infoLabel, LeagueUser.HelpStage nextStage) {
		String infoText;
		if (LeagueUser.HelpStage.FILL_PREDICTIONS.equals(nextStage)) {
			infoText = Resources.getMessage(nextStage.getResourceKey(), DateUtil.formatInUserTimezone(leagueBean.getLeague().getLeague_starting_date()));
		} else {
			infoText = Resources.getMessage(nextStage.getResourceKey());
		}
		infoLabel.setText(infoText);
		caption.setText(Resources.getMessage(nextStage.getTitleResourceKey()));
	}

	private HorizontalLayout createBasicBar() {
		HorizontalLayout bottomBar = new HorizontalLayout();
		bottomBar.setSpacing(false);
		bottomBar.setHeight(null);
		bottomBar.setWidthFull();
		VaadinUtil.addStyles(bottomBar.getStyle(),
				"padding: var(--lumo-space-xs);\n");
		return bottomBar;
	}

	public void show() {
		VerticalLayout layout = this;
		popupWindow = new PopupWindow.Builder("TODO", popupWindow -> layout)
				.setType(PopupWindow.Type.NO_BUTTONS)
				.addExtraThemeNames("league-tip")
				.setWidth("100%")
				.setHeight("60%")
				.build();
		popupWindow.open();
	}

	public void close() {
		if (popupWindow != null) {
			popupWindow.close();
		}
	}
}
