package taxipark

import kotlin.math.ceil
import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter { trips.none { trip -> trip.driver.name == it.name } }
            .toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { p -> trips.count { trip -> trip.passengers.any { p.name == it.name } } >= minTrips }
        .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers.filter { p ->
        trips
            .count { trip -> trip.driver.name == driver.name && trip.passengers.any { it.name == p.name } } > 1
    }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { p ->
        trips.count { trip ->
            trip.discount != null &&
                    trip.passengers.any { it.name == p.name }
        } > trips.count { trip ->
            trip.discount == null &&
                    trip.passengers.any { it.name == p.name }
        }
    }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return if (trips.isNotEmpty()) {
        (0..1000 step 10).map { IntRange(it, it + 9) }
            .maxBy { range -> trips.map(Trip::duration).count { range.contains(it) } }
    } else {
        null
    }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    return if (trips.isNotEmpty()) {
        val oneFifth = (allDrivers.size * 0.2).toInt()
        val oneFifthIncome = trips.groupBy { it.driver.name }
            .map { it.key to it.value.map(Trip::cost).sum() }
            .sortedByDescending { it.second }
            .take(oneFifth).sumOf { it.second }
        oneFifthIncome / trips.sumOf { it.cost } >= 0.8
    } else {
        false
    }
}