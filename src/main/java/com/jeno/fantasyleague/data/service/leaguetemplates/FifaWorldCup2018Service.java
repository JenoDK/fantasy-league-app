package com.jeno.fantasyleague.data.service.leaguetemplates;

import com.jeno.fantasyleague.model.League;

public class FifaWorldCup2018Service implements LeagueTemplateService {

	@Override
	public void run(League newLeague) {
	}

	public enum Team {

		ARGENTINA("Argentina", "images/icons/country_icons/argentina.png"),
		AUSTRALIA("Australia", "images/icons/country_icons/australia.png"),
		BELGIUM("Belgium", "images/icons/country_icons/belgium.png"),
		BRAZIL("Brazil", "images/icons/country_icons/brazil.png"),
		COLOMBIA("Colombia", "images/icons/country_icons/colombia.png"),
		COSTA_RICA("Costa rica", "images/icons/country_icons/costa_rica.png"),
		CROATIA("Croatia", "images/icons/country_icons/croatia.png"),
		DENMARK("Denmark", "images/icons/country_icons/denmark.png"),
		EGYPT("Egypt", "images/icons/country_icons/egypt.png"),
		ENGLAND("England", "images/icons/country_icons/england.png"),
		FRANCE("France", "images/icons/country_icons/france.png"),
		GERMANY("Germany", "images/icons/country_icons/germany.png"),
		ICELAND("Iceland", "images/icons/country_icons/iceland.png"),
		IRAN("Iran", "images/icons/country_icons/iran.png"),
		JAPAN("Japan", "images/icons/country_icons/japan.png"),
		KOREA_REPUBLIC("Korea republic", "images/icons/country_icons/korea_republic.png"),
		MOROCCO("Morocco", "images/icons/country_icons/morocco.png"),
		MEXICO("Mexico", "images/icons/country_icons/mexico.png"),
		NIGERIA("Nigeria", "images/icons/country_icons/nigeria.png"),
		PANAMA("Panama", "images/icons/country_icons/panama.png"),
		PERU("Peru", "images/icons/country_icons/peru.png"),
		POLAND("Poland", "images/icons/country_icons/poland.png"),
		PORTUGAL("Portugal", "images/icons/country_icons/portugal.png"),
		RUSSIA("Russia", "images/icons/country_icons/russia.png"),
		SAUDI_ARABIA("Saudi arabia", "images/icons/country_icons/saudi_arabia.png"),
		SENEGAL("Senegal", "images/icons/country_icons/senegal.png"),
		SERBIA("Serbia", "images/icons/country_icons/serbia.png"),
		SPAIN("Spain", "images/icons/country_icons/spain.png"),
		SWEDEN("Sweden", "images/icons/country_icons/sweden.png"),
		SWITZERLAND("Switzerland", "images/icons/country_icons/switzerland.png"),
		TUNISIA("Tunisia", "images/icons/country_icons/tunisia.png"),
		URUGUAY("Uruguay", "images/icons/country_icons/uruguay.png");

		private String name;
		private String iconPath;

		Team(String name, String iconPath) {
			this.name = name;
			this.iconPath = iconPath;
		}

		public String getName() {
			return name;
		}

		public String getIconPath() {
			return iconPath;
		}

	}
}
