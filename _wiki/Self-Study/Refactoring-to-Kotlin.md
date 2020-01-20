> 안티패턴
```kotlin
lateinit var profile: Profile
fun init() {
    fetchProfile().subscribe{ profile ->
        this.profile = profile
    }
}
```
이렇게 `lateinit`를 남발하게 되면 통신 중이거나 통신 에러 시에 접속해 **UninitializedPropertyAccessException**이 발생한다. 

> 해결방법
lateinit을 선언하는 대신 nullable로 설정해 놓고 값을 못 받았을 시에 어떻게 처리할까를 고민하는 것이 좋다. 
```kotlin
var profile: Profile? = null
fun init() {
    fetchProfile().subscribe { profile ->
        this.profile = profile
    }
}
```
onCreate() / onCreateView()에서 초기화가 가능하다면 lateinit을 사용해도 된다. 그러나 onCreate()혹은 onCreateView() 이후에 값이 정해지면 Nullable로 설정하는 것이 좋다(혹은 멤버변수를 사용하는 것을 피하는 편이 좋다).

**isInitialized**의 추가로 값이 대입되었는지 여부를 확인할 수 있게 되었음(Kotlin 1.2)
```kotlin
lateinit var str: String

fun foo(){
    val before = ::str.isInitialized // false
    str = "hello"
    val after = ::str.isInitialized // true
}
```

그런데 isInitialized는 원래 테스트 코드에서 이용하려고 추가된 것이고, 이를 일상적으로 이용하면 더 이상 null이 안전하지 않으므로 프로덕션 코드에서는 사용하지 않는 편이 낫다고 하는데 ~~뭔 소리인지 모르겠다~~
(++ 생각한 이유: 안심하고 lateinit var을 남발할 시 isInitialized를 믿다가 NullPointException이 발생할 때 앱이 죽어버리기 때문)
