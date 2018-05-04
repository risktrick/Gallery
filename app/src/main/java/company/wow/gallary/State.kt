package company.wow.gallary

interface State<T> {
    fun restart() {}
    fun refresh() {}
    fun loadNewPage() {}
    fun release() {}
    fun newData(data: List<T>) {}
    fun fail(error: Throwable) {}
}