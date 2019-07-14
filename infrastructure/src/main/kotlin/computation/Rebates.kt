package computation

import Offer

fun Offer.getRebate() = generateSequence { Offer("245", "trudna") }.take(10_000_000).toList().sortedBy { it.name }