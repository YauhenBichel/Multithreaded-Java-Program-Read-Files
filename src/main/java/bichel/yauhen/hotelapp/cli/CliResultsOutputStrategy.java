package bichel.yauhen.hotelapp.cli;

import bichel.yauhen.hotelapp.StringUtils;
import bichel.yauhen.hotelapp.cli.enumeration.CliActionCommandEnum;
import bichel.yauhen.hotelapp.cli.exception.IncorrectCliQueryException;
import bichel.yauhen.hotelapp.model.Hotel;
import bichel.yauhen.hotelapp.model.Review;
import bichel.yauhen.hotelapp.Constants;
import bichel.yauhen.hotelapp.HotelReviewData;
import bichel.yauhen.hotelapp.cli.mapper.CliActionQueryMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.SortedMap;

/**
 * Strategy for printing results of data handling in console
 */
public class CliResultsOutputStrategy implements ResultsOutputStrategy {

    private static final Logger logger = LogManager.getLogger(CliResultsOutputStrategy.class);

    private final CliActionQueryMapper cliActionQueryMapper;
    private final HotelReviewData hotelReviewData;

    public CliResultsOutputStrategy(HotelReviewData hotelReviewData) {
        this.cliActionQueryMapper = new CliActionQueryMapper();
        this.hotelReviewData = hotelReviewData;
    }

    /**
     * Control CLI commands
     */
    @Override
    public void process() {
        Scanner scanner = new Scanner(System.in);
        Map<CliActionCommandEnum, String> keyValueCommandMap;
        while (true) {
            System.out.println(Constants.CLI_COMMANDS_QUERY);
            System.out.println(Constants.CLI_NOTE_EXIT);
            String query = scanner.nextLine();
            if (query.equals(Constants.CLI_EXIT_KEY)) {
                break;
            }

            try {
                keyValueCommandMap = cliActionQueryMapper.mapActionQueryToPairs(query);

                if (keyValueCommandMap.containsKey(CliActionCommandEnum.FIND_BY_HOTEL_ID)) {
                    runFindHotelByIdCommand(keyValueCommandMap);
                } else if (keyValueCommandMap.containsKey(CliActionCommandEnum.FIND_REVIEWS_BY_HOTEL_ID)) {
                    runFindReviewsByHotelIdCommand(keyValueCommandMap);
                } else if (keyValueCommandMap.containsKey(CliActionCommandEnum.FIND_WORD)) {
                    runFindWordCommand(keyValueCommandMap);
                }
            } catch (IncorrectCliQueryException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    private void runFindReviewsByHotelIdCommand(Map<CliActionCommandEnum, String> keyValueCommandMap) {
        String value = keyValueCommandMap.get(CliActionCommandEnum.FIND_REVIEWS_BY_HOTEL_ID);
        List<Review> reviews = hotelReviewData.findReviews(Integer.parseInt(value));

        if(reviews.isEmpty()) {
            System.out.println("Reviews are not found");
        }

        reviews.forEach(System.out::println);
    }

    private void runFindWordCommand(Map<CliActionCommandEnum, String> keyValueCommandMap) {
        String word = keyValueCommandMap.get(CliActionCommandEnum.FIND_WORD);
        SortedMap<Integer, List<Review>> freqReviewsMap = hotelReviewData.findWord(word);

        System.out.println("Word is " + word);
        if(freqReviewsMap == null) {
            System.out.println(String.format("'{}' is not found", word));
        } else {
            for (Map.Entry<Integer, List<Review>> entry : freqReviewsMap.entrySet()) {
                System.out.println("Frequency is " + entry.getKey());
                System.out.println("Amount of reviews is " + entry.getValue().size());
                System.out.println("\tReviews are ");
                StringBuilder sb = new StringBuilder();
                for (Review review : entry.getValue()) {
                    sb.append("\t").append(review.getId()).append("  date: " + review.getReviewSubmissionTime())
                            .append("\n");
                }
                System.out.print(sb);
            }
        }

    }

    private void runFindHotelByIdCommand(Map<CliActionCommandEnum, String> keyValueCommandMap) {
        String value = keyValueCommandMap.get(CliActionCommandEnum.FIND_BY_HOTEL_ID);

        if (!StringUtils.isInteger(value)) {
            System.out.println("Hotel Id is integer. Please try again...");
            return;
        }

        Optional<Hotel> optHotel = hotelReviewData.findHotel(value);
        if (optHotel.isPresent()) {
            System.out.println("Hotel: " + optHotel.get());
        } else {
            System.out.println("Hotel is not found");
        }
    }
}
