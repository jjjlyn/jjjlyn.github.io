---
layout: post
title: "[Flutter] Silver App Bar의 Dynamic한 Expanded Height"
date: 2020-03-18
comments: true
external-url:
tags: [dart, flutter]
---
<br>
<span style="font-weight:bold; font-size:3em; font-family: Georgia;">F</span>lutter에도 Android의 <span style="color:#fc054f; ">Collapse Toolbar Layout</span>과 비슷한 기능을 하는 위젯이 있다. <span style="color:#fc054f; ">Silver App Bar</span>가 되시겠다.
안드로이드에서는 `wrap_content`로 레이아웃의 `weight`, `height`을 주면 그 사이즈만큼 유동적으로 늘어나거나 줄어든다.<br><br>
안드로이드에서 <span style="color:#fc054f; ">Collapse Toolbar Layout</span>을 쓰면 이렇다. (안드로이드가 주가 아니니 xml의 앞과 뒤 생략)
```xml
<com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:fitsSystemWindows="true"
    app:layout_scrollFlags="scroll|exitUntilCollapsed"
    app:contentScrim="@color/colorPrimary">

    <FrameLayout
        android:fitsSystemWindows="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <androidx.appcompat.widget.AppCompatImageView
            android:fitsSystemWindows="true"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="centerCrop"/>

        <androidx.appcompat.widget.LinearLayoutCompat
            android:paddingTop="?attr/actionBarSize"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <com.google.android.material.textview.MaterialTextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>

            <com.google.android.material.textview.MaterialTextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>

        </androidx.appcompat.widget.LinearLayoutCompat>

    </FrameLayout>

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/tb_header"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        app:layout_collapseMode="pin"
        android:background="@android:color/transparent"/>
</com.google.android.material.appbar.SubtitleCollapsingToolbarLayout>
```

<span style="color:#fc054f">Collapsed - Expanded</span> 상태를 왔다갔다하는 `Collapsing Toolbar`의 뒷 배경 화면을 이미지 뷰로 깔고,
그 <span style="color:#fc054f">Expanded</span> 상태의 툴바 background 위에 특정 데이터를 뿌려주는 것이 목적이었다. 위 사례에서는 텍스트 뷰를 `LinearLayout`에 따라 `Column`으로 배치한 것이다(세로 방향).<br><br>
이 때 텍스트 뷰에 바인딩되는 데이터가 길수도 있고 짧을 수도 있다. 안드로이드 xml에서는 이런 레이아웃에 **부모 레이아웃**(텍스트 뷰들을 감싸고 있는 최상단 레이아웃)의 높이를 `layout_height="wrap_content"`로 주면 **자식 레이아웃의 높이**에 따라 **부모 레이아웃의 높이**가 정해진다. 어떤 데이터를 텍스트 뷰에 뿌려주느냐의 여부에 관계없이 깔끔하게 레이아웃 사이즈를 동적으로 처리할 수 있는 것이다. <br><br>
그래서 당연히 Flutter의 <span style="color:#fc054f;">SilverAppBar</span>에서도 **자식 위젯의 높이에 따른 부모 위젯의 유동적인 높이 처리**가 가능할 줄 알았다. 이번 결과물은 ~~편법~~으로 보일수도 있지만, 일단 원하는대로 나오긴 했으니 **#성공적...**이다. 그러나 불필요한 비용이 들어가는 방법이다.<br><br>
참고로 Flutter 오픈 카톡방에 문의해 봤고, 모개발자분이 `ScrollController`의 `offset`을 이용해서 처리하라고 조언해 주셨다. 그 방법으로도 강구해봤으나 해결하지 못했다. &nbsp; &nbsp;  *&#128073; `Offset` 값을 이용하여 해결하신 분 있으시면 알려주세요 ㅠㅠ*<br><br>

### 먼저 결과물 

<p align="middle">{% include youtube-screen.html id="OBqLJaCQUCA" %}</p>

<span style="color:#fc054f;">Silver App Bar</span> 위젯은 `expandedHeight: [int value]`파라미터를 **초기화** 해 주어야, App Bar가 `Expanded`일 때 설정해 준 높이값만큼 펼쳐진다. 이걸 설정해주지 않으면 App Bar가 펼쳐지지 않는다. 그러나 내가 원했던 건 초기부터 설정된 *static*한 높이가 아니라 <span style="color:#fc054f;">Silver App Bar</span>의 자식위젯인 `FlexibleSpaceBar`의 높이에 따라 유동적으로 변하는 높이였다. <span style="color:#fc054f;">Silver App Bar</span>는 자식 위젯이 그려지기 전에 먼저 그려지기 때문에 자식 위젯(여기서는 `FlexibleSpaceBar`)의 크기가 얼마가 될지 미리 계산해서 `expandedHeight`값으로 지정할 수 없다. `FlexibleSpaceBar` 위젯 안에 속하는 **자식 위젯의 높이를 미리 알 수 있는 방법**이 없을까? 고민하다가 찾아낸 방법이 바로 **전체화면을 `Stack`으로 놓고, 첫 번째 스택에 `FlexibleSpaceBar` 내부에 속하는 위젯을 먼저 할당하는 것**이다. `Stack`의 첫 번째 자식 위젯 렌더링이 완료되면, 그 다음 위젯의 렌더링이 실행될 것인데 이 때 이미 우리는 첫 번째 자식 위젯의 크기를 가져올 수 있다. 그러므로 두 번째 자식 위젯으로는 원래 화면에 띄워져야 할 <span style="color:#fc054f;">Silver App Bar</span>가 포함된 위젯을 배치시킨다. <br><br>
`Stack`에 할당된 첫 번째 자식 위젯은 `Container`의 높이만 구하면 필요없는 녀석이므로 `Visiblity` 혹은 `OffStage`로 감싼다. `bool _isVisible`과 같은 변수로 <span style="color:#fc054f">flag</span>를 하나 선언하여 높이를 구하기 전에는 *true*, 높이를 구한 이후는 `Stack`의 두 번째 자식 위젯이 렌더링 된 후에도 계속 밑에 쌓여서 보이면 안되므로 *false*로 상태값을 변경하여 사라지게 한다. 물론 중복되는 `Container`를 렌더링했다 사라지게 했다 하는 식으로 높이 계산을 하기 때문에 쓸데없는 비용이 들어갈 것이다. 
```dart
@override
  Widget build(BuildContext context) {

    return Stack(
      overflow: Overflow.visible,
      children: <Widget>[
          Visibility(
              visible: _isVisible; // Container의 높이 총 합을 구했으면 false로 값을 바꿔 위젯이 보이지 않게 해야한다.
              child: Material(
                  child: Container(...), 
                  /* flexibleSpace 내부 위젯을 할당해서 SilverAppBar가 렌더링 되기 전에 
                  미리 높이의 총 합을 구하려 한다. 
                  (Container의 높이값을 expandedHeight에 할당하기 위함) */
              )
          )
          ),
          SafeArea(
              child: Material(
                  child: CustomScrollView(
                      silvers: <Widget>[
                          SilverAppBar(
                              expandedHeight: [int value],
                              flexibleSpace: FlexibleSpaceBar(
                                  Container(...), // 이 Container를 Stack의 첫 자식 위젯에 할당한다.
                              )
                          ), // 
                          SilverFillRemaining(...),
                      ]
                  ),
          ),
        ],
    );
  }
```

### 압권의 편법
그렇다면 어떤 식으로 높이를 계산하는가? 독자들은 당혹스럽겠지만 `GlobalKey`를 이용하였다. 그것도 무려 3개나 선언하였다. 아마추어틱하게 `Container > Column` 에 속하는 Text 자식 위젯의 크기를 각각 구하기 위해 `final stickyKey = GlobalKey()` 이런 식으로 객체 3개를 생성하였다.<br><br>
> <span style="font-size: 2em; font-weight: bold">Why?</span><br>
> &nbsp; &nbsp;   Container나 Column 전체에 GlobalKey를 하나 선언하게 될 경우 전체 스크린 높이(double.infinity)를 return한다. 이를 방지하기 위해서는 Column에 선언된 자식 위젯 별로 GlobalKey를 각각 하나씩 부여할 수밖에 없었다.

여기서 각각 return된 `stickyKey 1,2,3`를 더하고 `Container` 전체에 준 `padding` 혹은 `margin`을 모두 더하면 **데이터의 변화에 따라 유동적으로 바뀌는 높이**를 구할 수 있다. 이 높이를 전역변수로 지정한 후 `Stack`의 두 번째 위젯에서 <span style="color:#fc054f;">Silver App Bar</span>의 `expandedHeight` 값으로 넘겨주면 된다.<br>
```dart
GlobalKey stickyKey = GlobalKey();
GlobalKey stickyKey2 = GlobalKey();
GlobalKey stickyKey3 = GlobalKey();
```

앞에서 언급한 Container 위젯은 이렇게 구성되어 있다.
```dart
Container(
      padding: EdgeInsets.only(
          left: Dimens.gap_dp20,
          bottom: Dimens.gap_dp20,
          top: kToolbarHeight),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            '나는 정말 길다 나는 두 줄도 될 수 있다 나는 정말 길다 유동적인 길이를 보여주는 예시이다 아아아',
            key: stickyKey, // 높이 1
            style: TextStyle(
                color: Colors.white, 
                fontSize: Dimens.font_sp14),
          ),
          Text(
              '길다 길다 길다 짧을수도 있다 너가 어떤 데이터를 가져오느냐에 따라 높이가 달라져야 한다',
              key: stickyKey2, // 높이 2
              style: TextStyle(
                  color: Colors.white,
                  fontSize: Dimens.font_sp30,
                  letterSpacing: -0.4)),
          Text(
              '노잼',
              key: stickyKey3, // 높이 3
              style: TextStyle(
                  color: Colors.white, 
                  fontSize: Dimens.font_sp14)),
        ],
      ),
    ),
```            

참고로 `Material`로 `Container`를 감싸주지 않고 그대로 `Container`만 넘겨주면 당연히 **두 번째 위젯의 `FlexibleSpaceBar`의 자식 Container 높이와 다르게 return** 된다. 이건 너무 당연한가?
```dart
Visibility(
    Material(
        child : Container（...)
    )
)
```

다소 지저분하지만 **높이**를 구하는 코드이다. `Global Key`들이 구해지는대로 바로 그 값 자체를 더해서 `dynamicTotalHeight`으로 return 하면 사용자 경험이 정말 좋지 않아진다. 그러므로 **배열**에 담아서 return...
```dart
double _dynamicTotalHeight;
List<double> _childWidgetHeights = List();

@override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback(_getDynamicTotalHeight);
  }

  double _getChildWidgetHeight(GlobalKey key) {
    RenderBox renderBox = key.currentContext.findRenderObject();
    print(renderBox.size.height);
    return renderBox.size.height;
  }

  _getDynamicTotalHeight(_) {
    _dynamicTotalHeight = 0;

    _childWidgetHeights.add(_getChildWidgetHeight(stickyKey));
    _childWidgetHeights.add(_getChildWidgetHeight(stickyKey2));
    _childWidgetHeights.add(_getChildWidgetHeight(stickyKey3));

    for (double height in _childWidgetHeights) {
      _dynamicTotalHeight = height + _dynamicTotalHeight;
    }

    _dynamicTotalHeight = _dynamicTotalHeight + kToolbarHeight + Dimens.gap_dp20;

    return _dynamicTotalHeight;
  }
```  

#### 참고
<a href="https://gist.github.com/jjjlyn/82bc323c23682d9581d83f6861db2cc5" target="_blank" rel="noopener" style="">[전체코드 보기]</a>
