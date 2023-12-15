Writing Java multithreaded programs using ReentrantReadWriteLock and ExecutorService

## CLI arguments

## Results on my local machine
- MacBook Pro (Retina, 13-inch, Early 2015)
- Processor: 2.7 GHz Dual-Core Intel Core i5
- Memory: 8 GB 1867 MHz DDR3

```xml
<testcase classname="HotelDataTest" name="testOneHotel" time="0.827"></testcase>
<testcase classname="HotelDataTest" name="testConcurrentBuildLargerSetSeveralThreads" time="13.13"></testcase>
<testcase classname="HotelDataTest" name="testBuildLargerSetOneThread" time="10.474"></testcase>
<testcase classname="HotelDataTest" name="testThreeReviewsSameHotel" time="0.014"></testcase>
<testcase classname="HotelDataTest" name="testHotelDataConsistency" time="112.985"></testcase>
<testcase classname="HotelDataTest" name="testConcurrentBuildSmallSet" time="0.748"></testcase>
```
