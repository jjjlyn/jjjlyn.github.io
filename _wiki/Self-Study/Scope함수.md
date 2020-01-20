### Scope 함수
scope 함수의 종류에는 let/ run/ also/ apply/ with 총 5개가 있다. with를 제외하면 대략적으로 다음과 같이 분류된다. 

반환값 = 자신.scope함수{
        리시버.method()
        결과
}

#### Scope 함수를 사용하는 곳 #1
null 관련 제어에 유용하다.
```kotlin
str?.let {
// str이 null이 아닐 경우
}

val r = str ?: run {
// str이 null인 경우
}
```

#### Scope 함수를 사용하는 곳 #2
초기화 처리를 정리하고 싶은 경우 사용하면 좋다. 
```kotlin
val intent = Intent().apply{
    putExtra("key", "value")
    putExtra("key", "value")
    putExtra("key", "value")
}
```

#### Scope 함수 안티패턴
apply범위 내에서 프로퍼티 접근 형식을 사용할 때
```kotlin
val button = Button(context)
button.text = "hello"

// 안티패턴 -> 이런 식으로 사용하면 안 된다. 
val button = Button(context).apply{
    text = "hello"
}
```
로컬 변수를 정의하면 접근 범위가 바뀐다. 

```kotlin
fun init(){
    var text = ""
    val button = Button(context).apply{
        text = "hello"
    }
    button.text // 는 hello가 되지 않는다.
}
```

#### 해결방법 #1
apply 범위 내에서는 프로퍼티 접근 형식을 사용하지 않고 일반 함수를 호출하는 방법
```kotlin
val button = Button(context).apply{
    setText("hello")
}
```
단 로컬 함수(setText와 이름이 동일한 함수)가 정의되어 있을 경우 문제가 발생할 수 있다. 

#### 해결방법 #2
this를 필수로 사용하는 방법
```
val button = Button(context).apply{
    this.text = "hello"
}
```

#### 해결방법 #3
```
val button = Button(context).also{
    it.text = "hello"
}
```
~~이건 솔직히 뭔지 모르겠다~~

