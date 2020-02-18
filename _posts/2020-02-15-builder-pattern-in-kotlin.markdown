---
layout: post
title: 코틀린에 빌더 패턴을 적용하려면? (코틀린에는 사실 빌더 패턴이 필요없다 ㅠㅠ)
date: 2020-02-15
comments: true
external-url:
---


```kotlin
fun main() {
    val person = Person.Builder
        .setName("julia")
        .setAge(17)
        .setSexuality(1)
        .build()
        
        
    println("${person.name} , ${person.age}, ${person.sexuality}")
}

class Person private constructor(builder : Builder){
    private val name : String
    private val age : Int
    private val sexuality : Int // 0 : male, 1 : female
    
    init{
        this.name = builder.name
        this.age = builder.age
        this.sexuality = builder.sexuality
    }
    
    companion object Builder {
        private var name : String = ""
        private var age : Int = 0
        private var sexuality : Int = 0
        
        fun setName(value : String): Builder{
            this.name = value
            return this
        }
        
        fun setAge(value : Int) : Builder{
            this.age = value
            return this
        }
        
        fun setSexuality(value : Int) : Builder{
            this.sexuality = value
            return this
        }
        
        fun build() : Person {
            return Person(this)
        }
    }
}
```
