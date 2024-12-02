package jasondecarvalho.snapshot.testing.domain

fun entryPoint(a: Int, b: Int): Int {
    if(a + 10 > b) {
        return 30
    }
    if(b + 10 < a) {
        return 20
    }
    if(a + b > 40) {
        return 10
    }
    if(a - b < 10) {
        return 40
    }
    return 0
}