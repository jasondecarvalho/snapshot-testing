package jasondecarvalho.snapshot.testing.domain

import com.diffplug.selfie.Selfie.expectSelfie
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ComplicatedExistingLogicTest {

    @ParameterizedTest
    @MethodSource("input")
    fun `complicated logic examples`(a: Int, b: Int) {
        val actual = entryPoint(a, b)
        expectSelfie(actual.toString()).toMatchDisk("($a, $b)")
    }

    companion object {
        @JvmStatic
        fun input(): Stream<Arguments> {
            return (1..100).flatMap {
                a -> (1..100).map {
                    b -> Arguments.of(a, b)
            }}.stream()
        }
    }

}
