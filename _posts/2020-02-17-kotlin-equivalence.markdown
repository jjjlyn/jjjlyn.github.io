---
layout: post
title:  "레퍼런스 타입에서의 자바 equals()는 코틀린 == 과 같다(동등비교)"
comments: true
date:   2020-02-17
---

## JAVA
자바는 primitive type을 비교할 때는 `==`를 사용하여 value 속성이 **동등** 한지 비교할 수 있다. 

```java
public static void main(String[] args){
    int a = 10;
    int b = 10;

    System.out.println(a == b); // value의 동등비교 (내용비교)
}
```

참조 타입인 경우(Call by Reference)에서는 연산자 `==` 가 주소값을 비교하는 것에 사용된다.  

> 주소값을 hashCode와 혼동하지 말자. 그리고 자바(혹은 코틀린)에서 힙 내에 저장된 객체의 주소를 알 수 있는 방법도 없다.
> - 주소값: 메모리 내 객체가 저장된 위치
> - hashCode: HashMap 등에서 객체를 구분하기 위한 정수값

## KOTLIN
코틀린에서도 primitive type을 비교할 때는  `==` 연산자가 기본이다.
그러나 reference type을 비교할 때는 `==` 가 자바의 `==`(주소값 비교)로 동작하지 않는다. 

코틀린에서 `==` 는 내부적으로 `equals()`를 호출한다. (`==` 연산자 호출을 `equals()` 호출로 컴파일 한다)
자바에서는 동등비교에 관해서는 `equals()`를 호출해야 했으나, 코틀린은 `==`가 `equals()`로 컴파일 되기 때문에 굳이 `equals()`를 쓰지 않고도 동등성 비교를 할 수 있어서 코드가 간결해지는 결과를 가져온다. (물론 `!=` 도 `equals()`호출로 컴파일 된다.) 

```kotlin
a == b

// 는 아래처럼 컴파일 된다.

a?.equals(b) ?: (b == null)

/**
 a가 null일 경우, ?: 뒤에 (b == null)과 비교. 
 a가 null이 아닌 경우, a.equals(b)를 호출
 a가 null이라면 b가 null일 때에만 비로소 true일 수 있다 
 이렇게 컴파일이 이루어진다
 */
```
그러니까 참조 타입의 주소값을 비교한답시고 `==` 을 호출하면, 내부적으로 `equals()`가 호출되어 동등 비교(value 비교)를 해 버린다. 

### 결론
- 코틀린의 `==`는 **동등 비교(내용 비교)** 로 자바의 `equals()` 연산과 같다.
- 코틀린에서는 reference type 주소 비교에서는 `===` 연산자를 이용한다. 이는 자바의 참조타입에서 `==` 와 같은 역할을 한다.
- 참고로 `===`는 primitive type에서는 당연히 `==`와 동일하게 작동한다. 

&nbsp;
&nbsp;
&nbsp;
&nbsp;

#### 출처
- Kotlin In Action (p.316-317)
- <a href="https://ko.wikipedia.org/wiki/%EC%96%B5%EC%9D%8C_%EB%B6%80%ED%98%B8" target="_blank" rel="noopener" style="">https://okky.kr/article/596050</a>
