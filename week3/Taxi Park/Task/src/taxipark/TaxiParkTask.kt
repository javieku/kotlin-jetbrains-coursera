package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter {
            driver -> trips.none { trip -> trip.driver.name == driver.name } }.toSet()


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter {
            passenger -> trips.count { trip -> trip.passengers.contains(passenger) } >= minTrips }
                    .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        allPassengers.filter { passenger ->
            trips.count { trip -> trip.passengers.contains(passenger) && trip.driver == driver } > 1
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        allPassengers.filter {
            passenger ->
            trips.count { trip -> trip.passengers.contains(passenger) && trip.discount != null } >
            trips.count { trip -> trip.passengers.contains(passenger) && trip.discount == null}}
                .toSet()


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange?  {

    if (trips.isNullOrEmpty())
        return null

    val frequencies = trips.map{ trip -> IntRange( 10 * (trip.duration/10), 10 * (trip.duration/10) + 9)}
                           .groupingBy { range -> range }.eachCount()

    return frequencies.maxBy { freq -> freq.value }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {

    if (trips.isEmpty())
        return false

    val income = trips.map { trip -> trip.cost }.sum()
    val driversReport = trips.groupBy { trip -> trip.driver }
                             .map { (driver, tripList) ->
                                 driver to tripList.sumByDouble { it.cost }
                             }
                             .toList()
                             .sortedByDescending { (driver, contribution) -> contribution }

    val profitableDrivers = Math.round(allDrivers.size * 0.2).toInt()

    val incomeFromProfitableDrivers = driversReport.subList(0, profitableDrivers)
                                                    .sumByDouble { (driver, contribution) -> contribution }

    return incomeFromProfitableDrivers >= income * 0.8
};