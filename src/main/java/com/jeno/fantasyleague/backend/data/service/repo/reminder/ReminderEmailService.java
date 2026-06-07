package com.jeno.fantasyleague.backend.data.service.repo.reminder;

public interface ReminderEmailService {

	/**
	 * Reminds league members who still own zero stocks that the buying deadline (the league start) is approaching.
	 * Sends an escalating reminder ~48h and ~24h before the league starts.
	 */
	void sendStockReminders();

	/**
	 * Sends a daily digest to each member listing the matches starting in the next ~24h that they haven't predicted yet.
	 */
	void sendPredictionReminders();
}
