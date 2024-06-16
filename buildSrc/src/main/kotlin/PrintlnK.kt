object println {
    fun red(content: String) {
        println("\u001B[31m" + content + "\u001B[0m")
    }
}
