package computation

fun <T : Comparable<T>> insertionSort(data: Array<T>): Array<T> =
    (if (data.size > 1) insertionIteration(1, data)
    else data).also { println("sorted array") }

tailrec fun <T : Comparable<T>> insertionIteration(index: Int, array: Array<T>): Array<T> =
    if (index == array.size) array
    else {
        val toBeMoved = array[index]
        val indexToInsert = getIndexToInsert(index, array)
        val moved = moveAll(indexToInsert - 1, array, index - 1)
        moved[indexToInsert] = toBeMoved
        insertionIteration(index + 1, moved)
    }

fun <T : Comparable<T>> getIndexToInsert(index: Int, array: Array<T>): Int {

    tailrec fun <T : Comparable<T>> getIndexToInsertInternal(valueToInsert: T, indexBefore: Int, array: Array<T>): Int =
        when {
            indexBefore < 0 -> 0
//            array[indexBefore] <= valueToInsert -> indexBefore + 1
            array[indexBefore] <= valueToInsert -> indexBefore + 1
            else -> getIndexToInsertInternal(valueToInsert, indexBefore - 1, array)
        }

    return getIndexToInsertInternal(array[index], index - 1, array)
}

fun <T : Comparable<T>> moveByOneIndexUp(atIndex: Int, array: Array<T>): Array<T> = array.apply {
    array[atIndex + 1] = array[atIndex]
}

tailrec fun <T : Comparable<T>> moveAll(untilIndex: Int, array: Array<T>, currentIndex: Int): Array<T> =
    if (currentIndex == untilIndex) array
    else moveAll(
        untilIndex = untilIndex,
        array = moveByOneIndexUp(currentIndex, array),
        currentIndex = currentIndex - 1
    )