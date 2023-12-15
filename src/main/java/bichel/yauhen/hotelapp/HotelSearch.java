package bichel.yauhen.hotelapp;

import bichel.yauhen.hotelapp.cli.enumeration.CliPathQueryKeyEnum;

import java.util.HashMap;
import java.util.Map;

public class HotelSearch {
    /**
        cli arguments:
        -hotels input/hotels/hotels.json -reviews input/reviews -threads 3 -output output/actualOutput
        -hotels input/hotels/hotels.json -reviews input/reviewsLargeSet -output output/studentOutputLargeSet -threads 1
        -hotels hotelFile -reviews reviewsDirectory -threads numThreads -output filepath
     **/
    public static void main(String[] args) {
        Map<CliPathQueryKeyEnum, String> keyValuePathMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            keyValuePathMap.put(CliPathQueryKeyEnum.enumByValue(args[i]), args[i + 1]);
        }

        CliRequestHandler cliRequestHandler = new CliRequestHandler(
                new HotelsProcessor(),
                new ReviewsProcessor(),
                new ThreadSafeHotelReviewData(),
                keyValuePathMap);
        cliRequestHandler.run();
    }
}
