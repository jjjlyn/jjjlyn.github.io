object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val pikachuFactory = PikachuFactory()
        val pikachu : Pikachu = pikachuFactory.spawn("pikachu") as Pikachu

        val arg1 = "나는 바보다"
        val arg2 = "나는 바보다"
        println(arg1.hashCode())
        println(arg2.equals(arg1))

        println(arg1 === arg3)
    }
}