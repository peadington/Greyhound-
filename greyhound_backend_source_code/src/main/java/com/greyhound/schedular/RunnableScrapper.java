package com.greyhound.schedular;

import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greyhound.utility.DBUtility;
import com.greyhound.utility.Utility;

public class RunnableScrapper implements Runnable {
	private static Logger logger = Logger.getLogger(RunnableScrapper.class);
	private final LocalDate sDate;

	public RunnableScrapper(LocalDate sDate) {
		this.sDate = sDate;
	}

	@Override
	public void run() {
		Utility utility = new Utility();
		DBUtility dbutility = new DBUtility();
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("THREAD STARTED FOR :-" + sDate);
			String[] tracks = { "Newcastle", "Sunderland", "Pelaw Grange", "Crayford", "Romford", "Nottingham",
					"Perry Barr", "Sheffield" };
			for (String track : tracks) {
				String url = "https://api.gbgb.org.uk/api/results?page=1&itemsPerPage=99999999999999999&track=" + track
						+ "&date=" + sDate + "&race_type=race";
				Document document = utility.getDocument(url, 0);
				JsonNode items = mapper.readTree(document.html().split("<body>")[1].split("</body>")[0].trim())
						.get("items");
				if (items.size() > 0) {
					utility.findDataFromJson(items, dbutility, mapper);
					utility.addRating(sDate, track, dbutility);
				} else {
					logger.info("Data not available for date:-" + sDate + " Track :-" + track);
				}
			}

		} catch (Exception e) {
			logger.error("Exception in scrapMeetingDetail THREAD :-" + e.getMessage());
		} finally {
			dbutility.closeConnection();
		}
	}

}
