package jasondecarvalho.snapshot.testing

import com.diffplug.selfie.Selfie.expectSelfie
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SnapshotTestingTest {

    @Test
    fun `Complicated logic`() {
        val response = app(Request(GET, "/snapshot-testing/complicated?a=1&b=1"))
        expectSelfie(response.body.toString()).toBe("")
        expectSelfie(response.status.code).toBe(400)
    }

    @ParameterizedTest
    @MethodSource("values")
    fun `complicated logic snapshots`(a: Int, b: Int) {
        val response = app(Request(GET, "/snapshot-testing/complicated?a=$a&b=$b"))
        expectSelfie(response.toString()).toMatchDisk("($a, $b)")
    }

    companion object {
        @JvmStatic
        fun values(): Stream<Arguments> =
            (1..100).flatMap {
                a -> (1..100).map {
                    b -> Arguments.of(a, b)
                }
            }.stream()
    }

}
