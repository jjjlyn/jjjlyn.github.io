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

### also(), takeIf(), takeUnless()
`also`는 `apply`와 유사하다. 리시버를 받고, 리시버에 어떤 행위를 하고, 리시버를 리턴한다. 차이는 `apply`안의 블록에서는 리시버를 `this`로 접근할 수 있는데 반해 `also`의 블록에서는 `it`으로 접근 가능하다는 것이다(원한다면 다른 이름을 줄 수도 있다). 이는 바깥 범위의 `this`를 감추고 싶지 않을 때 쓸모있다(?) ~~뭔 소리야 ㅠㅠ~~ 
```
fun Block.copy() = Block().also{
    it.content = this.content
}
```

`takeIf`는 단일 값에 대해서 `filter`와 유사함. 리시버가 조건을 충족하는지 검사하고 충족하면 리시버를 리턴, 그렇지 않으면 `null`을 리턴한다. 
```
val outDirFile = File(outputDir.path).takeIf{
    it.exists()} ?: return false
