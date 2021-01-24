---
layout: post
title: "[Android] 파라미터에 따라 불러오는 값이 다른 Livedata를 호출하는 방법"
date: 2020-05-19
comments: true
external-url:
tags: [kotlin]
---

<br>
Room Database로부터 Livedata를 호출하는 일은 자주 발생한다. View(Activity 혹은 Fragment)에서 변경되는 데이터<br>
(i.e. DatePickerDialog에서 선택한 날짜, Spinner에서 선택한 값 등)<br>
를 LiveData 호출하는 메서드의 매개변수로 전달하고 싶을 때가 있다. 

<!-- more -->

이럴 때 View에서 ViewModel에 선언된 메서드로 직접 파라미터 값을 넘기는 방법

```kotlin
class HistoryViewModel : ViewModel(){
    // You shouldn't do like this
    fun loadHistory(date: String) : LiveData<History> {
        return mHistoryDAO.loadHistory(date)
    }
}
```
은 지양하는 것이 좋다.

대신 ViewModel에서

<script src="https://gist.github.com/jjjlyn/a4aba9810a39243d4791d99956590a61.js"></script>

를 사용하자. 

**Transformation.switchMap**은 **parameter에 들어가는 Livedata value**가 변할 때마다 지속적으로 달라진 상태의 LiveData를 반환한다.
예시코드에서는 `mDate`로 선언된 Livedata value값이 변하면 (View 단에서 `mHistoryViewModel.setDate(mDate)`로 mDate.value를 바꿈), Transformation.switchMap이 mDate.value의 변화된 값을 감지한 후 우리가 반환하려는 Room DAO에 선언된 `mHistoryDAO.loadHistory()`에 mDate.value값을 전달하여 `LiveData<History>`를 반환한다. 

<script src="https://gist.github.com/jjjlyn/219b45f25050ff6580d7d368b82f3ad2.js"></script>

`date`는 View에서 선택한 DatePickerDialog의 값에 따라 달라지는 날짜라고 가정해보자. `date`를 선택한 후 ViewModel에 선언된 setDate()로 그 값을 넘겨주면 subscribeObservers()내의 loadHistory()가 Callback으로 실행된다.

<script src="https://gist.github.com/jjjlyn/05df4111bfcce4116d1a77ac3e95fd2a.js"></script>

예전에 View에서 ViewModel에 선언된 메소드에 그대로 파라미터를 넘겨주기 위해 View에서

```kotlin
private var mDate : String? = null
```

로 전역 선언한 후

```kotlin
mHistoryViewModel.loadHistory(mDate)
```

이런 식으로 호출했었는데, mDate에 들어간 값이 항상 불안정하여 원하는 Livedata가 반환되지 않는 상황이 발생한 경험이 있다.
나와 같이 삽질하는 사람들이 없길 바라며... &#128514;