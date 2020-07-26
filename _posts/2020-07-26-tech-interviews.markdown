---
layout: post
title: "[Android] 얼떨결에 이직 준비 -- 기술면접"
date: 2020-07-26
comments: true
external-url:
tags: [java, kotlin]
---

> 경력이 거의 1년이 다 되어가는 시점이다. 지금까지의 업무 및 개인적인 스터디 활동이 구직 활동에 실질적인 도움이 되는지 알아보고 싶었다. 지원은 스타트업 자사 앱 서비스 업체 위주로 했다.

### 총 3번의 기술면접이 있었는데, 이 중 기억에 남는 민망한 질문은...
**Github 커밋 메시지 중복이 왜 그렇게 많은가?**

### 그 외 대답을 못했거나, 틀린 답변을 한 항목

1. 1부터 10000까지 1의 개수를 구하는 방법

2. HashMap을 구현

3. 디자인 패턴 아는 것 나열하고 각각 항목에 대한 기술

4. 이진 검색 트리에 대한 기술

5. 안드로이드 Parcelize와 Serialize의 차이

6. 안드로이드 Clean Architecture 설명

7. 안드로이드 Repository Pattern

면접관이 커밋 컨벤션, 커밋 기록 중복(ㅠㅠ), Repository 내용 사소한 것들을 전부 살펴볼 줄이야! 정말 민망했지만, 지적해 주신 내용에 대해서는 고치면 되는 것이다ㅎㅎ 면접을 안 봤다면 몰랐을 것들인데 덕분에 알게 된 것이니 긍정적으로 생각하자 :-) 

### 그 외 기술면접 질문 항목!!

포지션은 안드로이드로 지원했기 때문에 대부분 안드로이드에 관한 질문이었다. 그 외 Java, Kotlin, Computer Science 지식에 대한 질문도 있긴 했음.

1. 스레드 간 통신 방법
- Thread, Handler, Message Queue, Looper

2. 백그라운드에서 UI 업데이트

3. Application과 context란

4. Activity의 생명주기에 관해 설명하고, 각 항목 자세히 기술 (Fragment도 나올 수 있음)

5. ANR이란?

6. AsyncTask란?

7. 명시적 인텐트, 암시적 인텐트

8. 안드로이드 4대 컴포넌트 Broadcast Receiver, Content Provider, Activity, Service

9. Android MAT란?

10. FCM(GCM) 구동 원리

11. 리스트뷰에서 사용되는 뷰홀더(ViewHolder Pattern) 패턴에 대해 아는 만큼 설명하라.
(리사이클러뷰와 비교)

12. 인텐트란?

13. Inflation에 대해 설명하라.

14. RXJava에 대해 설명하라. 

15. 안드로이드의 태스크란?

16. Restful API란?

17. 안드로이드 메모리 구조

18. PendingIndent란?

19. 안드로이드에서 가비지 컬렉터

20. 안드로이드 메모리 누수 관리법 (경험이 있다면 곁들여서 설명)

21. 안드로이드에서 액티비티 간 통신 방법

22. 이미지 라이브러리 사용 경험 (Glide, Picasso 등 비교)

23. Jetpack Library 사용 경험

24. JSON과 XML 비교

25. 채팅 기능 구현 경험 여부

26. 커스텀 뷰 구현 방식

27. constraint layout 설명

28. MVVM ViewModel과 AAC ViewModel의 차이점

29. Room과 SharedPreference 비교

30. 빌드, 컴파일 설명

31. 컴파일 타임, 런타임 설명

32. 자바 다형성

33. 자바 인터페이스, abstract class 차이

34. kotlin data class, 프로퍼티 설명

35. 링크드 리스트, 어레이 리스트, 어레이 비교

36. 해시 맵 설명

37. 라이브러리와 프레임워크 차이

38. 비지니스 로직이란 무엇인가?

39. MVP Presenter와 MVVM ViewModel의 차이, ViewModel의 개념 (MVC와 MVI 포함)

40. 싱글톤 패턴 구현

41. 옵저버 패턴 기술

42. 싱글 액티비티 어플리케이션 구조란?

43. Vector VS Bitmap

44. XML 기반 레이아웃이 중요한 이유

45. volatile 이란?

46. Doze 모드 기술

47. Thread와 Process의 차이