package jasondecarvalho.snapshot.testing

import com.diffplug.selfie.Camera
import com.diffplug.selfie.Lens
import com.diffplug.selfie.Selfie.expectSelfie
import com.diffplug.selfie.Snapshot
import com.diffplug.selfie.SnapshotValue
import com.fasterxml.jackson.databind.ObjectMapper
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.lens.contentType
import org.junit.jupiter.api.Test

class ExampleJsonSnapshotTest {

    @Test
    fun `check json response`() {
        val response = app(Request(GET, "/snapshot-testing/json"))
        expectSelfie(response, responseCamera.withLens(prettyPrintJson)).toMatchDisk()
            .facets("status", "content-type").toBe("""╔═ [status] ═╗
200 OK
╔═ [content-type] ═╗
ContentType(value=application/json, directives=[(charset, utf-8)])""")
    }

    private val responseCamera: Camera<Response> = Camera { response ->
        Snapshot.of(response.body.toString())
            .plusFacet("status", response.status.toString())
            .plusFacet("content-type", response.contentType().toString())
    }

    private val prettyPrintJson: Lens = Lens { snapshot: Snapshot ->
        val subject: String = snapshot.subject.valueString()
        snapshot.plusOrReplace("", prettyString(subject))
    }

    private fun prettyString(json: String): SnapshotValue {
        return SnapshotValue.of(ObjectMapper().readTree(json).toPrettyString())
    }

}
