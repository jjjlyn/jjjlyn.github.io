---
layout: post
title: "[Android] 프로젝트가 끝났다(0) -- 중복된 코드를 없애보자"
date: 2020-05-27
comments: true
external-url:
tags: [kotlin]
---

#### [목차]
0. 중복된 코드를 없애보자
1. 상수값 관리 방식을 바꿔보자
2. 3개 이상의 매개변수를 받는 메서드
3. 하나의 메서드는 하나의 기능만 하는가?
4. else 예약어를 없애보자
5. Indent >= 2 인 메서드를 리팩토링 해보자
6. 일급 콜렉션 메서드를 만들자
7. 메서드 안에 선언된 로컬변수는 꼭 필요한 것일까?

#### 정리

구글 맵 API를 사용한 안드로이드 프로젝트이다. <br>
네트워크에서 받아온 위치정보가 있으면 `setMarkerRetrieved()`로 지도상에 마커를 표시한다. <br>
현재 위치정보를 표시해야 할 때는 `setMarkerCurrent()`를 사용한다. <br>
네트워크 및 현재 위치정보 모두 존재하지 않을 경우 `setMarkerDefault()`로 하드코딩된 좌표를 이용해 마커를 찍는다. <br>

문제점은 세 개의 메서드는 명칭만 다를 뿐, *'불러온 좌표를 구글맵에 마커로 찍는다'*로 모두 같은 기능을 수행하는 것. <br>
코드 역시 중복된다. 

```kotlin
    private fun setMarkerRetrieved(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
            .title(getAddress(latLng))
            .icon(
                BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
            )
       
        val marker = mGoogleMap?.addMarker(markerOptions)

        marker?.tag = "MARKER_RETRIEVED"
    }

    private fun setMarkerDefault(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(LatLng(latLng.latitude, latLng.longitude))
            .title("Default Place")
            .icon(
                BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
    
        val marker = mGoogleMap?.addMarker(markerOptions)

        marker?.tag = "MARKER_DEFAULT"
    }

    private fun setMarkerCurrent() {
        val markerOptions = MarkerOptions()
        markerOptions.position(
            LatLng(
                mCurrentLocation?.latitude ?: Constants.Map.DEFAULT_LOCATION.latitude,
                mCurrentLocation?.longitude ?: Constants.Map.DEFAULT_LOCATION.longitude
            )
        )
            .title(
                getAddress(
                    LatLng(
                        mCurrentLocation?.latitude ?: Constants.Map.DEFAULT_LOCATION.latitude,
                        mCurrentLocation?.longitude ?: Constants.Map.DEFAULT_LOCATION.longitude
                    )
                )
            )
            .icon(
                BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            )
      
        val marker = mGoogleMap?.addMarker(markerOptions)

        marker?.tag = "MARKER_CURRENT"
        mCurrentMarker = marker
    }
```

중복된 내용의 여러개 메서드는 하나로 통합한다. `RETRIEVED/CURRENT/DEFAULT`의 상태에 따라 메서드의 내용이 약간씩 달라질 수 있기 때문에 **Enum** 상수를 추가해 분기처리한다. 정수 혹은 문자열 열거 패턴을 사용하는 대신 **Enum**을 채택한다. <br>
**정수 열거 패턴**은 타입 안전을 보장하지 않고 가독성에도 좋지 않다. **문자열 열거 패턴**은 가독성은 괜찮으나 문자열 비교에서 성능 저하가 일어난다.

```kotlin
enum class MapStatus {
        RETRIEVED, CURRENT, DEFAULT
    }
```

```kotlin
private fun setMarker(latLng: LatLng, tag: MapStatus, markerColor: Float) {
        val title: String? = when (tag) {
            MapStatus.DEFAULT -> {
                "Default Place"
            }
            else -> {
                getAddress(latLng)
            }
        }

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
            .title(title)
            .icon(
                BitmapDescriptorFactory
                    .defaultMarker(markerColor)
            )
        val marker = mGoogleMap?.addMarker(markerOptions)

        marker?.tag = tag

        if (tag == MapStatus.CURRENT) {
            mCurrentMarker = marker
        }
    }
```

기존보다 간략해지긴 했다. 그러나 매개변수를 **3개**나 받게 된다. 인자를 줄이는 연습을 해보자. Enum 인자에 markerColor를 추가하니, `setMarker()`는 두 개의 인자를 받게 되었다.

```kotlin
 enum class MapStatus(val markerColor: Float) {
        RETRIEVED(BitmapDescriptorFactory.HUE_ROSE),
        CURRENT(BitmapDescriptorFactory.HUE_BLUE),
        DEFAULT(BitmapDescriptorFactory.HUE_GREEN);
    }   
```

```kotlin
private fun setMarker(latLng: LatLng, tag: MapStatus) {
        
        val title: String? = when (tag) {
            MapStatus.DEFAULT -> {
                "Default Place"
            }
            else -> {
                getAddress(latLng)
            }
        }

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
            .title(title)
            .icon(
                BitmapDescriptorFactory
                    .defaultMarker(tag.markerColor)
            )
        val marker = mGoogleMap?.addMarker(markerOptions)

        marker?.tag = tag

        if (tag == MapStatus.CURRENT) {
            mCurrentMarker = marker
        }
    }
```

#### 후기
- 난이도 최하 수준의 코드 정리 (리팩토링이라는 용어를 사용하기에 민망한 수준)
- 메서드는 단 하나의 기능만을 가지고 있어야 하나, `setMarker()`은 Marker의 title을 정하기, MarkerOptions 설정, Marker를 지도 상에 표시 등 적어도 3개의 기능을 지닌다. 간단한 덧셈에서도 연산 메서드와 반환 메서드 따로 분리를 해야 코드 재사용성이 높아진다고 들었다. 어떻게 기능 분리를 할 지 고민해봐야 할듯.




