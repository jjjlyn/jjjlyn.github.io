#### Generic enum 값 접근
Generic 방식으로 enum 클래스의 값을 차례로 열거할 수 있다.
```Kotlin
enum class RGB { RED, GREEN, BLUE }
inline fun <reified T : Enum<T>> printAllValues(){
    print(enumValues<T>().joinToString{
        it.name}
    )
}
```
