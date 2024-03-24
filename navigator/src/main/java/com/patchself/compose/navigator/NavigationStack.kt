package com.patchself.compose.navigator

internal class NavigationStack {
    var currentIndex = -1
        private set
    private val stack = arrayListOf<Content>()


    fun set(items: Array<Content>) {
        stack.clear()
        currentIndex = items.size - 1
        stack.addAll(items)
    }

    fun push(page: Content, setAsCurrent: Boolean) {
        stack.add(page)
        if (setAsCurrent) {
            currentIndex++
        }
    }

    fun indexOf(item: Content): Int {
        var index = 0
        val it = stack.iterator()
        while (it.hasNext()) {
            if (item == it.next()) {
                return index
            }
            index++
        }
        return -1
    }

    fun isEmpty() = stack.isEmpty()

    fun size() = stack.size

    fun get(index: Int) = if (index < 0 || index >= stack.size) null else stack[index]

    fun getAll() = stack

    fun getCurrent() =
        if (currentIndex == -1 || currentIndex >= stack.size) null else stack[currentIndex]

    fun getPrevious() = get(currentIndex - 1)

    fun remove(index: Int): Content? {
        return if (index < 0 || index >= stack.size || index == currentIndex) {
            null
        } else if (index >= currentIndex) {
            stack.removeAt(index)
        } else {
            currentIndex--
            stack.removeAt(index)
        }
    }

    fun removeLast(): Content? {
        if (stack.isEmpty()) {
            return null
        }
        val page = stack.removeAt(currentIndex)
        currentIndex--
        return page
    }

    fun resetTo(controller: Content){
        for (index in stack.size - 1 downTo 0) {
            if (stack.getOrNull(index) == controller){
                return
            }
            currentIndex--
            destory(index)
        }
    }

    fun destory(index: Int): Content? {
        val page = remove(index)
        return page
    }


    fun clear() {
        if (stack.size > 0) {
            val it = stack.iterator()
            while (it.hasNext()) {
                val page = it.next()
            }
            stack.clear()
            currentIndex = -1
        }
    }

    fun reset(saveFirst: Boolean) {
        val last = removeLast()!!
        if (!saveFirst) {
            clear()
        } else {
            val first = stack[0]
            stack.clear()
            stack.add(first)
        }
        push(last, true)
    }

    fun resetSilently(initial: Content) {
        stack.clear()
        stack.add(initial)
        currentIndex = 0
    }
}

