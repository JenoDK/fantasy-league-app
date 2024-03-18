package com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks;

import static com.jeno.fantasyleague.ui.main.views.league.singleleague.stocks.StocksBean.COSMETICAL_PRICE_MODIFIER;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.label.HtmlLabel;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.DataProvider;

public class StocksTab extends LazyTabComponent {

	private final boolean forAdminModule;
	private final User user;
	private League league;
	private SingleLeagueServiceProvider singleLeagueServiceprovider;
	private StocksGrid stocksGrid;
	private StatusLabel balanceLabel;

	private List<StocksBean> teamWeights;

	public StocksTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		this(league, singleLeagueServiceprovider, false, null);
	}
	public StocksTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, boolean forAdminModule, User user) {
		super();
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;
		this.forAdminModule = forAdminModule;
		this.user = user;

		setMargin(false);
		setPadding(false);
		setSizeFull();
		setAlignItems(Alignment.CENTER);

		fetchAndSetTeamWeights();
		stocksGrid = new StocksGrid(league, DataProvider.fromStream(teamWeights.stream()), this::validateStockChange, forAdminModule);

		initRightSide(league, singleLeagueServiceprovider);
		add(stocksGrid);
		HtmlLabel source = new HtmlLabel();
		source.getElement().getThemeList().add("small");
		source.setText("<i>Stock prices are decided based on scores found at <a href=\"https://kassiesa.net/uefa/data/method5/crank2024.html\" target=\"_blank\">UEFA Country Ranking 2024</a></i>");
		add(source);
	}

	public void fetchAndSetTeamWeights() {
		teamWeights = getContestantWeights().stream()
				.map(StocksBean::new)
				.sorted(Comparator.comparingDouble(StocksBean::getPowerIndex).reversed())
				.collect(Collectors.toList());
	}

	private List<ContestantWeight> getContestantWeights() {
		if (forAdminModule) {
			return singleLeagueServiceprovider.getContestantWeights(league, user);
		} else {
			return singleLeagueServiceprovider.getContestantWeights(league);
		}
	}

	public ValidationResult validateStockChange(StocksBean bean) {
		List<ContestantWeight> serversideStream = getContestantWeights();
		Stream<StocksBean> streamWithBeanReplaced = serversideStream.stream()
				.map(contestantWeight -> {
					if (bean.getContestantWeight().getId().equals(contestantWeight.getId())) {
						return bean;
					} else {
						return new StocksBean(contestantWeight);
					}
				});
		BigDecimal serversideWeightWithBeanReplacedToDistribute = getWeightToDistribute(streamWithBeanReplaced);
		BigDecimal clientSideWeightToDistribute = getWeightToDistribute();
		if (!serversideWeightWithBeanReplacedToDistribute.equals(clientSideWeightToDistribute)) {
			return ValidationResult.error("Server-side changes were detected, refresh and then you can try again");
		} else {
			if (bean.getStocksPurchased() > 10) {
				return ValidationResult.error("You cannot purchase more than 10 stocks per team.");
			}
			boolean exceedsLimit = clientSideWeightToDistribute.compareTo(BigDecimal.ZERO) < 0;
			boolean isInTime = DateUtil.nowIsBeforeUtcDateTime(league.getLeague_starting_date()) || forAdminModule;
			if (!isInTime) {
				return ValidationResult.error(Resources.getMessage("cannotPurchaseStock", DateUtil.formatInUserTimezone(league.getLeague_starting_date())));
			} else if (exceedsLimit) {
				return ValidationResult.error(Resources.getMessage("cannotExceedBalanceLimit"));
			}
		}
		return ValidationResult.ok();
	}

	public void initRightSide(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		balanceLabel = new StatusLabel(false);
		updateBalanceLabel();

		stocksGrid.stockChanged()
				.subscribe(bean -> {
						singleLeagueServiceprovider.getContestantWeightRepository().saveAndFlush(bean.setWeightAndContestantWeight());
						updateBalanceLabel();
					});

		Label infoLabel = new Label("Changes can be made until " + DateUtil.formatInUserTimezone(league.getLeague_starting_date()));

		add(balanceLabel);
		add(infoLabel);
	}

	private void updateBalanceLabel() {
		BigDecimal weightToDistribute = getWeightToDistribute();
		boolean exceedsLimit = weightToDistribute.compareTo(BigDecimal.ZERO) < 0;
		balanceLabel.setText("$" + getWeightToDistributeString(weightToDistribute));
		balanceLabel.addComponentAsFirst(VaadinIcon.WALLET.create());
		if (exceedsLimit) {
			balanceLabel.setErrorStyle();
		} else {
			balanceLabel.setSuccessStyle();
		}
	}

	public String getWeightToDistributeString(BigDecimal weightToDistribute) {
		return DecimalUtil.getTwoDecimalsThousandSeperator(weightToDistribute);
	}

	public BigDecimal getWeightToDistribute() {
		return getWeightToDistribute(teamWeights.stream());
	}

	private BigDecimal getWeightToDistribute(Stream<StocksBean> stream) {
		BigDecimal sumOfDistributedWeight = stream
				.map(StocksBean::getPricePayed)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return BigDecimal.valueOf(100).subtract(sumOfDistributedWeight).multiply(COSMETICAL_PRICE_MODIFIER);
	}

}
