package com.jeno.fantasyleague;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.google.common.collect.Maps;

public class Lala {

//	private static final String welcomeSlaapie = "" +
//			"_____/\\\\\\\\\\\\\\\\\\\\\\___        __/\\\\\\_____________        _____/\\\\\\\\\\\\\\\\\\____        _____/\\\\\\\\\\\\\\\\\\____        __/\\\\\\\\\\\\\\\\\\\\\\\\\\___        __/\\\\\\\\\\\\\\\\\\\\\\_        __/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_        \n" +
//			" ___/\\\\\\/////////\\\\\\_        _\\/\\\\\\_____________        ___/\\\\\\\\\\\\\\\\\\\\\\\\\\__        ___/\\\\\\\\\\\\\\\\\\\\\\\\\\__        _\\/\\\\\\/////////\\\\\\_        _\\/////\\\\\\///__        _\\/\\\\\\///////////__       \n" +
//			"  __\\//\\\\\\______\\///__        _\\/\\\\\\_____________        __/\\\\\\/////////\\\\\\_        __/\\\\\\/////////\\\\\\_        _\\/\\\\\\_______\\/\\\\\\_        _____\\/\\\\\\_____        _\\/\\\\\\_____________      \n" +
//			"   ___\\////\\\\\\_________        _\\/\\\\\\_____________        _\\/\\\\\\_______\\/\\\\\\_        _\\/\\\\\\_______\\/\\\\\\_        _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\/__        _____\\/\\\\\\_____        _\\/\\\\\\\\\\\\\\\\\\\\\\_____     \n" +
//			"    ______\\////\\\\\\______        _\\/\\\\\\_____________        _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_        _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_        _\\/\\\\\\/////////____        _____\\/\\\\\\_____        _\\/\\\\\\///////______    \n" +
//			"     _________\\////\\\\\\___        _\\/\\\\\\_____________        _\\/\\\\\\/////////\\\\\\_        _\\/\\\\\\/////////\\\\\\_        _\\/\\\\\\_____________        _____\\/\\\\\\_____        _\\/\\\\\\_____________   \n" +
//			"      __/\\\\\\______\\//\\\\\\__        _\\/\\\\\\_____________        _\\/\\\\\\_______\\/\\\\\\_        _\\/\\\\\\_______\\/\\\\\\_        _\\/\\\\\\_____________        _____\\/\\\\\\_____        _\\/\\\\\\_____________  \n" +
//			"       _\\///\\\\\\\\\\\\\\\\\\\\\\/___        _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_        _\\/\\\\\\_______\\/\\\\\\_        _\\/\\\\\\_______\\/\\\\\\_        _\\/\\\\\\_____________        __/\\\\\\\\\\\\\\\\\\\\\\_        _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_ \n" +
//			"        ___\\///////////_____        _\\///////////////__        _\\///________\\///__        _\\///________\\///__        _\\///______________        _\\///////////__        _\\///////////////__\n" +
//			"\n";
//
//	public static void main(String[] args) {
//		Map<String, Integer> scores = Maps.newHashMap();
//
//		while(true) {
//			int bound = 15;
//			System.out.println();
//			System.out.println();
//			System.out.println("-------- Guess the number ---------------");
//			System.out.println("De limiet is " + bound);
//			Random r = new Random();
//			int result = r.nextInt(15);
//
//			String name = askForName();
//			boolean passwordEntered = false;
//			boolean newNameChosen = false;
//			while(name != null && name.isEmpty() || nameIsInvalid(name, passwordEntered, newNameChosen)) {
//				name = askForName();
//				if ("ikzalzelfwelkiezen".equalsIgnoreCase(name)) {
//					passwordEntered = true;
//				} else if (passwordEntered) {
//					newNameChosen = true;
//				}
//			}
//
//			int turns = 0;
//			boolean correct = false;
//			while(!correct) {
//				String guess = askForNumber(turns);
//				turns++;
//				correct = parseGuess(guess, result, bound);
//				if (nameIsSlaapie(name, welcomeSlaapie)) {
//					result = r.nextInt(15);
//				}
//			}
//			System.out.println("Het duurde voor jou " + turns + " beurten.");
//			TurnResult turnResult = calculateTurnResult(turns, bound);
//			if (turnResult == null) {
//				System.err.println("WHAT THA FUCK");
//			} else {
//				System.out.println("Analyse leert dat: " + turnResult.getFeedback());
//			}
//
//			if (!scores.containsKey(name)) {
//				scores.put(name, turns);
//			} else if (scores.get(name) > turns) {
//				scores.put(name, turns);
//			}
//
//			System.out.println("Scoreboard: ");
//			prettyPrintMap(scores);
//			System.out.println();
//			System.out.println();
//		}
//	}
//
//	private static boolean nameIsInvalid(String name, boolean passwordEntered, boolean newNameChosen) {
//		if (passwordEntered && newNameChosen) {
//			return false;
//		}
//		if (passwordEntered && !newNameChosen) {
//			return true;
//		}
//		boolean nameIsSlaapie = nameIsSlaapie(name, welcomeSlaapie);
//		if (!nameIsSlaapie) {
//			System.err.println("Error: foutieve naam! Tip: probeer eens");
//			System.out.println(welcomeSlaapie);
//		}
//		return !nameIsSlaapie;
//	}
//
//	private static boolean nameIsSlaapie(String name, String welcomeSlaapie) {
//		return "slaapie".equalsIgnoreCase(name) || welcomeSlaapie.equals(name);
//	}
//
//	public static String askForName() {
//		Scanner myObj = new Scanner(System.in);
//		System.out.println("Geef jouw naam in: ");
//		return myObj.nextLine();
//	}
//
//	private static void prettyPrintMap(Map<String, Integer> map) {
//		map.entrySet().stream()
//				.sorted(Comparator.comparing(Map.Entry::getValue))
//				.forEach(score -> {
//			System.out.println(score.getKey() + " = " + score.getValue());
//		});
//	}
//
//	private static TurnResult calculateTurnResult(int turns, int bound) {
//		return Arrays.stream(TurnResult.values())
//				.filter(turnResult -> {
//					int lower = Math.round((((float) turnResult.getRange().getLower()) / 100) * bound);
//					int upper = Math.round((((float) turnResult.getRange().getUpper()) / 100) * bound);
//					return turns >= lower && turns <= upper;
//				})
//				.findFirst()
//				.orElse(null);
//	}
//
//	private static boolean parseGuess(String guess, int result, int bound) {
//		try {
//			int guessInt = Integer.parseInt(guess);
//			if (guessInt < 0 || guessInt > bound) {
//				System.err.println("ELABA MAKKER, blijf tss de grenzen :)");
//				return false;
//			} else {
//				if (guessInt > result) {
//					System.out.println("Ge zit te hoog");
//					return false;
//				} else if (guessInt < result) {
//					System.out.println("Ge zit te laag, te leeg e");
//					return false;
//				} else {
//					System.out.println("Goed zo! Het was inderdaad " + result);
//					return true;
//				}
//			}
//		} catch (NumberFormatException e) {
//			System.err.println("ELABA MAKKER, GE MOET WEL EEN NUMMER KIEZEN E VRIEND!");
//			return false;
//		}
//	}
//
//	public static String askForNumber(int turns) {
//		Scanner myObj = new Scanner(System.in);
//		System.out.println("Geef jouw gokje voor beurt " + turns + ": ");
//		return myObj.nextLine();
//	}
//
//	public enum TurnResult {
//		FEW_GUESSES(new Range(0, 15), "Super goed"),
//		AVG_GUESSES(new Range(15, 35), "Hmmmm, cva... I guess"),
//		A_LOT_OF_GUESSES(new Range(35, 100), "HAHAHAHAHAHAHA, gij suckt wel");
//
//		private final Range range;
//		private final String feedback;
//
//		TurnResult(Range range, String feedback) {
//			this.range = range;
//			this.feedback = feedback;
//		}
//
//		public Range getRange() {
//			return range;
//		}
//
//		public String getFeedback() {
//			return feedback;
//		}
//	}
//
//	public static class Range {
//		private final int lower;
//		private final int upper;
//
//		public Range(int lower, int upper) {
//			this.lower = lower;
//			this.upper = upper;
//		}
//
//		public int getLower() {
//			return lower;
//		}
//
//		public int getUpper() {
//			return upper;
//		}
//	}
}
