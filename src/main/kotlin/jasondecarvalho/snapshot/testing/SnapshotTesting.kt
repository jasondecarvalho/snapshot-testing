package jasondecarvalho.snapshot.testing

import jasondecarvalho.snapshot.testing.domain.entryPoint
import jasondecarvalho.snapshot.testing.models.HandlebarsViewModel
import org.http4k.core.*
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.viewModel

val app: HttpHandler = routes(

    "/snapshot-testing/json" bind GET to {
        Response(OK)
                .with(CONTENT_TYPE of APPLICATION_JSON)
                .body(
                """
                    {
                      "id": "cus_NffrFeUfNV2Hib",
                      "object": "customer",
                      "address": null,
                      "balance": 0,
                      "created": 1680893993,
                      "currency": null,
                      "default_source": null,
                      "delinquent": false,
                      "description": null,
                      "discount": null,
                      "email": "jennyrosen@example.com",
                      "invoice_prefix": "0759376C",
                      "invoice_settings": {
                        "custom_fields": null,
                        "default_payment_method": null,
                        "footer": null,
                        "rendering_options": null
                      },
                      "livemode": false,
                      "metadata": {
                        "order_id": "6735"
                      },
                      "name": "Jenny Rosen",
                      "next_invoice_sequence": 1,
                      "phone": null,
                      "preferred_locales": [],
                      "shipping": null,
                      "tax_exempt": "none",
                      "test_clock": null
                    }
                """.trimIndent())
    },

    "/snapshot-testing/html" bind GET to {
        val renderer = HandlebarsTemplates().CachingClasspath()
        val view = Body.viewModel(renderer, TEXT_HTML).toLens()
        val viewModel = HandlebarsViewModel("Hello there!")
        Response(OK).with(view of viewModel)
    },

    "/snapshot-testing/complicated" bind GET to { request: Request ->
        val a = Query.int().required("a")(request)
        val b = Query.int().required("b")(request)

        if (a < 50) {
            Response(BAD_REQUEST)
        } else {
            Response(OK).body(entryPoint(a, b).toString())
        }
    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(Undertow(9000)).start()

    println("Server started on " + server.port())
}