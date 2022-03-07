package controller.ig

import constants.IG_ENDPOINT
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import model.IGResponse
import org.koin.ktor.ext.inject

const val NO_IGS_RETURNED = "No IGs returned from igController. List size '0'."

inline fun <R> executeAndMeasureTimeMillis(block: () -> R): Pair<R, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    return result to (System.currentTimeMillis() - start)
}

fun Route.igModule() {

    val igController by inject<IgController>()

    get(IG_ENDPOINT) {
        val logger = call.application.environment.log

        val (igsFromRegistry, registryTime )= executeAndMeasureTimeMillis { igController.listIgsFromRegistry() }

        println("${registryTime} ms for package registry")

        val (igsFromSimplifier, simplifierTime) = executeAndMeasureTimeMillis { igController.listIgsFromSimplifier() }

        println("${simplifierTime} ms for simplifier")

        val packageInfo  = (igsFromRegistry + igsFromSimplifier).toMutableList()

        println("Registry IGs: ${igsFromRegistry.size} Simplifier IGs: ${igsFromSimplifier.size} Total:${packageInfo.size}")

        if (packageInfo.size == 0) {
            logger.debug(NO_IGS_RETURNED)
            call.respond(HttpStatusCode.InternalServerError, NO_IGS_RETURNED)
        } else {
            val response = IGResponse(packageInfo = packageInfo)
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
