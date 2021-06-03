package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LeagueTipDialog extends VerticalLayout {

	private final LeagueBean leagueBean;
	private final LeagueUser leagueUser;
	private BehaviorSubject<LeagueUser> hideTips = BehaviorSubject.create();
	private BehaviorSubject<LeagueUser> skipTip = BehaviorSubject.create();
	private PopupWindow popupWindow;

	public LeagueTipDialog(LeagueBean leagueBean) {
		this.leagueBean = leagueBean;
		this.leagueUser = leagueBean.getLoggedInLeagueUser();

		initLayout();
	}

	private void initLayout() {
		setMargin(false);
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.END);

		StatusLabel infoLabel = new StatusLabel();
		infoLabel.setText(Resources.getMessage(leagueUser.getHelp_stage().getResourceKey()));
		infoLabel.setSizeFull();
		CustomButton skip = new CustomButton("Next tip", VaadinIcon.ARROW_RIGHT.create());
		skip.setIconAfterText(true);
		if (leagueUser.getHelp_stage().isLastStage()) {
			skip.setEnabled(false);
		}
		CustomButton previous = new CustomButton("Previous tip", VaadinIcon.ARROW_LEFT.create());
		if (leagueUser.getHelp_stage().isFirstStage()) {
			previous.setEnabled(false);
		}
		skip.addClickListener(ignored -> {
			leagueUser.getHelp_stage().getNextStage()
					.ifPresentOrElse(
							nextStage -> {
								leagueUser.setHelp_stage(nextStage);
								infoLabel.setText(Resources.getMessage(nextStage.getResourceKey()));
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
								infoLabel.setText(Resources.getMessage(previousStage.getResourceKey()));
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
		CustomButton doNotShow = new CustomButton("Hide tips", VaadinIcon.CLOSE.create());
		doNotShow.setIconAfterText(true);
		doNotShow.addClickListener(ignored -> {
			leagueUser.setShow_help(false);
			hideTips.onNext(leagueUser);
		});
		HorizontalLayout bottomBar = new HorizontalLayout();
		bottomBar.setSpacing(false);
		bottomBar.setPadding(false);
		bottomBar.setHeight(null);
		bottomBar.setWidthFull();
		VaadinUtil.addStyles(bottomBar.getStyle(),
				"padding: var(--lumo-space-xs);\n");
		bottomBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		bottomBar.setAlignItems(FlexComponent.Alignment.END);
		bottomBar.add(previous, skip, doNotShow);
		add(infoLabel, bottomBar);
	}

	public Observable<LeagueUser> hideTips() {
		return hideTips;
	}

	public Observable<LeagueUser> skipTip() {
		return skipTip;
	}

	public void show() {
		VerticalLayout layout = this;
		popupWindow = new PopupWindow.Builder("TODO", popupWindow -> layout)
				.setType(PopupWindow.Type.NO_BUTTONS)
				.setWidth("50%")
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
