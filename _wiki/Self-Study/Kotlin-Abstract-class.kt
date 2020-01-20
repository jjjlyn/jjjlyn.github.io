자바에서는 익명 클래스가 자주 사용된다. 
abstract class Person(var name: String, var age: Int){
    abstract fun printMe()
}

코틀린에서도 이처럼 익명 클래스라는 문법을 제공한다. 
해당 클래스를 상속받아서 사용한다고 가정해보자.
자바처럼 익명 클래스로 만들고 싶다면 어떻게 해야할까?

fun main(args: Array<String>){
    var jiharu: Person = object:Person("jiharu", 26){
        override fun printMe(){
            println(this.name + "is my Name.")
            println(this.age + "years old.")
        }
    }
    jiharu.printMe()
}
