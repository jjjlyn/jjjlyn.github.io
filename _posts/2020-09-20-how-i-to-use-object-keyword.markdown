---
layout: post
title: [Kotlin] object 키워드로 싱글톤 구현해보기
date: 2020-09-20
comments: true
external-url:
tags: [kotlin]
---

최근 프로젝트에서 조회수, 상품 건수 등에서 단위가 큰 숫자(예: 백만 건, 천만 건)를
인스타그램 팔로워 수 혹은 유투브 조회수처럼 포맷팅하는 유틸 메소드를 추가하라는 요청이 있었다.
```
1,000 -> 1천
20,000 -> 2만
350,000 -> 35만
```

우선 숫자를 단위에 맞춰 포맷팅하는 메소드는 코틀린 Number 클래스의 확장함수로 구현하였다.
이는 어렵지 않게 ~~로직은 다소... 베껴서 ㅋㅋ;;~~ 완성했다.

```kotlin
// Number.kt
fun Number.formatCount(): String{

    val value = this.toLong()

    if(value == Long.MIN_VALUE){
        return (Long.MIN_VALUE + 1).formatCount()
    }
    if(value < 0){
        return "-" + (-value).formatCount()
    }
    if(value < 1000){
        return value.toString()
    }

    val e: MutableMap.MutableEntry<Long, String> = NumberUnit.suffixes.floorEntry(value) // 여기 부분
    val divideBy = e.key
    val suffix = e.value

    val truncated = value / (divideBy / 10)
    val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
}
```

문제는 상단 코드에서 `NumberUnit.suffixes`(함수 외부에 선언된 프로퍼티)를 호출하는데 이를 어떻게 구현할 것인가였다. 
숫자가 커지면 단위(천, 만, 억 등)도 숫자 범위에 맞춰 같이 바뀌어야 하는데,
단위는 변하지 않는 고정표현이기 때문에 매번 `Number.formatCount()` 메소드를 호출할 때마다 새로운 인스턴스를 생성하는 것은 불필요하다.
1. **숫자**와 이에 대응하는 **단위**를 Map<Long, String>을 생성하여 저장하고, 이를 객체 선언하여 싱글턴으로 구현
2. 최상위 프로퍼티를 선언하여 어디에서나 호출 가능하도록 구현
이 두가지 방법이 떠올랐다.

### 방법 1. 객체 선언
```kotlin
object NumberUnit {

    val suffixes: NavigableMap<Long, String> = TreeMap()

    init {
        initSuffixes()
    }

    private fun initSuffixes(){
        suffixes[1_000] = "천" // 편의상 하드코딩
        suffixes[1_000_0L] = "만"
        suffixes[100_000_000L] = "억"
        suffixes[1_000_000_000_000L] = "조"
        suffixes[10_000_000_000_000_000L] = "경"
    }
}
```

코틀린에서는 `object` 키워드 하나만으로 클래스를 선언하는 동시에 
그 클래스에 속한 단일 인스턴스를 생성하여 변수에 저장하는 작업을 할 수 있다.
객체 선언 내부에서 `init {}` (초기화 블록)을 선언할 수 있으므로 객체 접근 시점에 `init { initSuffixes() }`이 실행되어 Map에 데이터가 저장되도록 구현하였다.

코틀린에는 static 키워드가 없다. 대신에 패키지 수준의 최상위 함수(클래스 외부 함수 선언)나 객체 선언(object 키워드를 활용한 싱글턴 생성)을 이용하면 프로그램 종료 시까지 모든 객체에서 이를 참조할 수 있다.
보통 최상위 함수를 활용하는 편이 더 권장되고, 최상위 함수로 구현하기 어려울 때 차선책으로 객체 선언을 이용한다.

### 방법 2. 최상위 프로퍼티 선언
```kotlin
// Number.kt (확장함수가 선언된 코틀린 파일)
fun Number.formatCount(): String {
    // Do Something...

    val e: MutableMap.MutableEntry<Long, String> = unitSuffixes.floorEntry(value) // 최상위 프로퍼티 접근
}

val unitSuffixes: NavigableMap<Long, String> = TreeMap()
    get() {
        field[1_000] = "천"
        field[1_000_0L] = "만"
        field[100_000_000L] = "억"
        field[1_000_000_000_000L] = "조"
        field[10_000_000_000_000_000L] = "경"
        return field
    }
```

### 결론
이번 구현에서는 객체 선언을 채택하였다.`NumberUtil.formatCount()` 확장함수에서 함수 외부에 선언된 변수에 대한 직접 접근이 필요했기 때문이다. 그러나 `unitSuffixes`와 같은 최상위 프로퍼티는 흔히 사용되지 않을 뿐더러 Number.kt 파일 내부에 최상위 프로퍼티 하나가 덩그러니 구현되어 있는 것은 쌩뚱 맞아 보였다(다른 개발자가 보기에 이 프로퍼티의 용도가 무엇인지 한번에 파악하기 어려울 것 같았다). 차라리 용도가 명시적으로 드러나도록 싱글톤으로 만들어 재사용하는 편이 낫다고 판단했다. 어차피 자바로 변환 시 둘다 static으로 컴파일 되므로 성능 차이는 미미할 것으로 보인다.

[참조]
- Kotlin In Action (p.182-185)