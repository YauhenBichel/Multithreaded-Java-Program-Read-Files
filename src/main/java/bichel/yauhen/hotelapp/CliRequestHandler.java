package bichel.yauhen.hotelapp;

import bichel.yauhen.hotelapp.cli.enumeration.CliPathQueryKeyEnum;
import bichel.yauhen.hotelapp.model.Hotel;
import bichel.yauhen.hotelapp.cli.CliResultsOutputStrategy;
import bichel.yauhen.hotelapp.cli.FileResultsOutputStrategy;
import bichel.yauhen.hotelapp.cli.ResultsOutputStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class for command line interface
*/
public class CliRequestHandler {

    private static final Logger logger = LogManager.getLogger(CliRequestHandler.class);

    private final HotelReviewData hotelReviewData;
    private final HotelsProcessor hotelsProcessor;
    private final ReviewsProcessor reviewsProcessor;
    private final Map<CliPathQueryKeyEnum, String> keyValuePathMap;

    public CliRequestHandler(HotelsProcessor hotelsProcessor,
                             ReviewsProcessor reviewsProcessor,
                             HotelReviewData hotelReviewData,
                             Map<CliPathQueryKeyEnum, String> keyValuePathMap) {
        this.hotelReviewData = hotelReviewData;
        this.hotelsProcessor = hotelsProcessor;
        this.reviewsProcessor = reviewsProcessor;
        this.keyValuePathMap = keyValuePathMap;
    }

    /**
     * Starts the app
     * The user should be able to type find,
     * findReviews and findWord in the console and get relevant results.
     * The data should NOT be sorted during the search.
     */
    public void run() {
        loadHotelsAndTheirReviews(keyValuePathMap);

        ResultsOutputStrategy outputStrategy;
        if(keyValuePathMap.containsKey(CliPathQueryKeyEnum.OUTPUT)) {
            logger.info("Print results to files");
            outputStrategy = new FileResultsOutputStrategy(keyValuePathMap, hotelReviewData);
        } else {
            hotelReviewData.createInvertedIndexMap();
            logger.info("Print results to console");
            outputStrategy = new CliResultsOutputStrategy(hotelReviewData);
        }

        outputStrategy.process();
    }

    private void loadHotelsAndTheirReviews(Map<CliPathQueryKeyEnum, String> keyValuePathMap) {
        String hotelFile = keyValuePathMap.get(CliPathQueryKeyEnum.HOTELS);

        List<Hotel> hotels = hotelsProcessor.parseHotels(hotelFile);
        for (Hotel hotel : hotels) {
            hotelReviewData.addHotel(hotel);
        }

        int threads = Integer.parseInt(keyValuePathMap.getOrDefault(CliPathQueryKeyEnum.THREADS, "1"));
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        if(keyValuePathMap.containsKey(CliPathQueryKeyEnum.REVIEWS)) {
            String reviewDir = keyValuePathMap.get(CliPathQueryKeyEnum.REVIEWS);
            reviewsProcessor.parseReviews(reviewDir, executor, hotelReviewData);
        }
    }
}
