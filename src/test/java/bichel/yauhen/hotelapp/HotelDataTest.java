package bichel.yauhen.hotelapp;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HotelDataTest {

	private static final int RUNS = 10;
	private static final int THREADS = 3;


	@Test(timeout = TestUtils.TIMEOUT1)
	public void testOneHotel() {
		// No reviews, reading one hotel from hotels1.json.
		// Asks the program to output hotels to the file "studentOutput"
		// // Compares studentOutput with expectedOutputHotels1
		String testName = "testOneHotel";
		String[] args = {"-hotels", "input" + File.separator + "hotels" + File.separator + "hotels1.json",
				         "-output", "output" + File.separator + "studentOutputHotels1"};
		Path expected = Paths.get(TestUtils.OUTPUT_DIR + File.separator + "expectedOutputHotels1"); // instructor's output
		Path actual = Paths.get(args[3]);
		runTestAndCompare(args, actual, expected, testName);
	}


	@Test(timeout = TestUtils.TIMEOUT1)
	public void testThreeReviewsSameHotel() {
		// reading one hotel from hotels1.json.
		// reading three reviews from reviewsTiny folder
		// Asks the program to output hotel & review data to file "studentOutputHotel1Reviews3"
		// Compares studentOutputHotel1Reviews3 with expectedOutputHotel1Reviews3".
		String testName = "testThreeReviewsSameHotel";
		String[] args = {"-hotels", "input" + File.separator + "hotels" + File.separator + "hotels1.json",
				"-reviews", "input" + File.separator + "reviewsTiny",
				"-output", "output" + File.separator + "studentOutputHotel1Reviews3"};
		Path expected = Paths.get( "output" + File.separator  + "expectedOutputHotel1Reviews3"); // instructor's output
		Path actual = Paths.get(args[5]);
		runTestAndCompare(args, actual, expected, testName);
	}

	@Test(timeout = TestUtils.TIMEOUT)
	public void testConcurrentBuildSmallSet() {
		String testName = "testConcurrentBuildSmallSet";
		String[] args = {"-hotels", "input" + File.separator + "hotels" + File.separator + "hotels.json",
				"-reviews", "input" + File.separator + "reviews",
				"-output", "output" + File.separator + "studentOutputReviewsSet", "-threads", String.valueOf(THREADS)};
		Path expected = Paths.get( "output" + File.separator  + "expectedOutputReviewsSet"); // instructor's output
		Path actual = Paths.get(args[5]);
		runTestAndCompare(args, actual, expected, testName);
	}

	@Test(timeout = TestUtils.TIMEOUT)
	public void testConcurrentBuildLargerSetSeveralThreads() {
		String testName = "testConcurrentBuildLargerSetSeveralThreads";
		String[] args = {"-hotels", "input" + File.separator + "hotels" + File.separator + "hotels.json",
				"-reviews", "input" + File.separator + "reviewsLargeSet",
				"-output", "output" + File.separator + "studentOutputLargeSet", "-threads", String.valueOf(THREADS)};
		Path expected = Paths.get( "output" + File.separator  + "expectedOutputLargeSet"); // instructor's output
		Path actual = Paths.get(args[5]);
		runTestAndCompare(args, actual, expected, testName);

	}

	@Test(timeout = TestUtils.TIMEOUT)
	public void testBuildLargerSetOneThread() {
		String testName = "testBuildLargerSetOneThread";
		String[] args = {"-hotels", "input" + File.separator + "hotels" + File.separator + "hotels.json",
				"-reviews", "input" + File.separator + "reviewsLargeSet",
				"-output", "output" + File.separator + "studentOutputLargeSet", "-threads", "1"};
		Path expected = Paths.get( "output" + File.separator  + "expectedOutputLargeSet"); // instructor's output
		Path actual = Paths.get(args[5]);
		runTestAndCompare(args, actual, expected, testName);

	}

	/**
	 * Tests the hotel builder output multiple times, to make sure the
	 * results are always consistent.
	 */
	@Test(timeout = TestUtils.TIMEOUT * RUNS)
	public void testHotelDataConsistency() {

		for (int i = 0; i < RUNS; i++) {
			testConcurrentBuildLargerSetSeveralThreads();
		}
	}

	/**
	 * Helper method, used to run the program and compare intructor's output with student's output
	 * @param args
	 * @param actual
	 * @param expected
	 * @param testName
	 */
	private void runTestAndCompare(String[] args, Path actual, Path expected, String testName) {
		try {
			Files.deleteIfExists(actual);
		}
		catch (IOException e) {
			System.out.println("Could not delete old output file: " + e);
		}

		HotelSearch.main(args); // run HotelSearch with these arguments

		//  compare output files
		int count = 0;
		try {
			count = TestUtils.checkFiles(expected, actual); // compares two files line by line
		} catch (IOException e) {
			Assert.fail(String.format("%n" + "Test Case: %s%n" + " File check failed: %s%n", testName, e.getMessage()));
		}

		if (count <= 0) {
			Assert.fail(String.format("%n" + "Test Case: %s%n" + " Mismatched Line: %d%n", testName, -count));
		}
	}



}
