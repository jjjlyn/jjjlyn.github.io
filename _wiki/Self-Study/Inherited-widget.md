Widget, State, BuildContext, InheritedWidget
Stream(비동기처리), Provider(외부 모듈) => Inheritable Widget과 유사
StreamProvider
BLoC패턴(데이터 바인딩) => 패턴이기 때문에 flutter에 종속적이지는 않다. (React 등 다른 곳에서 쓰이기도 한다.)

위젯 : 시각적 구성 요소(레이아웃을 구성하는 요소)
위젯 트리: 위젯은 트리 구조로 구성된다
상위 위젯: 다른 위젯을 포함하는 위젯(부모 위젯)
하위 위젯: 상위 위젯에 포함된 위젯(자식 위젯)

빌드 컨텍스트: 빌드된 모든 위젯 트리 구조 내의 위젯 위치에 대한 참조. 한 위젯에만 속한다. 만약 위젯 A가 하위 위젯을 갖고 있다면, 위젯 A의 빌드 컨텍스트는 자식 빌드 컨텍스트의 상위 빌드 컨텍스트가 된다. 빌드 컨텍스트들은 서로 연결되어 있고, 빌드 컨텍스트 트리(부모 자식 관계)를 이루고 있다.

빌드 컨텍스트 가시성: 어떤 빌드컨텍스트는 자신의 빌드컨텍스트와 상위 빌드컨텍스트에서만 보임.
예를 들어 Scaffold>Center>Column>Text에서 context.ancestorWidgetOfExactType(Scaffold)는 Text 컨텍스트에서 트리 구조로 이동하여 첫번째 Scaffold를 반환한다.
상위 빌드컨텍스트에서 descendant 위젯을 찾을 수도 있지만 이렇게 하는 것은 별로 좋지 않다. 

위젯의 타입에는 두 가지가 있다 : StatefulWidget/ StatelessWidget
StatelessWidget: 빌드 타이밍에 부모로부터 받은 정보에만 의존하는 컴포넌트다. 


StatefulWidget: Flutter가 StatefulWidget을 만들 때, State 객체를 만든다. 이 개체는 해당 위젯의 모든 가변 상태가 유지되는 곳이다. 

state의 개념은 두 가지로 정의된다.
1. 위젯이 사용하는 데이터는 변경될 수 있다.
2. 위젯이 빌드될 때 데이터를 동시에 읽을 수 없다. (모든 state는 build 메서드가 호출될 때까지 설정되어야 한다.)
데이터 설정 -> 빌드
라이프 사이클
- createState()
- mounted == true
- initState()
- didChangeDependencies()
- build()
- didUpdateWidget()
- setState()
- deactivate()
- dispose()
- mounted == false

StatefulWidget과 State 클래스는왜 별도로 분리되었는가?
성능 때문이다. State 객체는 오래 유지되지만 StatefulWidgets 및 모든 Widget의 서브 클래스는 구성이 변경될 때마다 rebuild된다. Flutter에서 이를 rebuild하는 데 들어가는 비용이 매우 저렴하기 때문이다. 그러나 State가 변경될 때마다 rebuild된다면, 이는 비용이 클 것이다. 그렇기 때문에 둘을 별도로 분리해 놓은 것이다. State는 rebuild될 때마다 폐기되지 않기 때문에 이에 대한 비용을  절감할 수 있으며 프레임 별로 프레임이 rebuild될 때마다 state 속성이나 getter, setter등으로 value를 가져오거나 설정한다. State는 폐기되지 않으므로 데이터 변경에 대한 응답으로 필요할 때 언제나 위젯을 rebuild할 수 있다.

1. createState()
Flutter가 StatefulWidget을 빌드하도록 지시하면 즉시 createState()가 호출된다. 이 메서드는 반드시 존재해야 함!! 

```dart
class MyHomePage extends StatefulWidget{
    @override
    _MyHomePageState createState() => new _MyHomePageState();
}
```
2. mounted is true
createState()가 state클래스를 생성하면 buildContext는 state에 할당된다. BuildContext는 위젯이 배치된 '위젯 트리의 위치'가 어디인지를 단순화한 표식(?)이라고 생각하면 된다. 모든 위젯은 bool형식의 this.mounted 속성을 가지고 있다. buildContext가 할당되면 true를 리턴한다. 위젯이 unmounted 상태일 때 setState를 호출하면 error가 발생한다.
3. initState()
위젯이 생성될 때 처음으로 호출되는 메서드. initState()는 오직 한 번만 호출된다. 또한 반드시 super.initState()를 호출해야 한다. 
initState()에서 실행되면 좋은 것들
- 생성된 위젯 인스턴스의 BuildContext에 의존적인 것들의 데이터를 초기화
- 동일 위젯트리 내 '부모 위젯'에 의존하는 속성 초기화
- Stream 구독, 알림변경, 위젯의 데이터를 변경할 수 있는 다른 객체 핸들링(?)
4. didChangeDependencies()
didChangeDependencies() 메서드는 위젯이 최초 생성될 때 initState 다음에 바로 호출된다. 또한 위젯이 의존하는 데이터의 객체가 호출될 때마다 호출된다. 예를 들면 업데이트되는 위젯을 상속한 경우, 공식 문서 또한 상속한 위젯이 업데이트 될 때 네트워크 호출(또는 다른 비용이 큰 액션)이 필요한 경우 유용하다.
5. build()
이 메서드는 자주 호출됨. 필수이며 @override 대상이고 반드시 Widget을 return한다. Flutter의 모든 gui는 padding, center조차도 child, children을 가진 위젯이다.
6. didUpdateWidget()
didUpdateWidget()는 부모 위젯이 변경되어 이 위젯을 재구성해야 하는 경우. 이 경우 initState()에서처럼 데이터를 다시 초기화해야 한다.
build() 메서드가 Stream이나 변경 가능한 데이터에 의존적인 경우, 이전 객체에서 구독을 취소하고 didUpdateWidget() 에서 새로운 인스턴스에 다시 구독해야 한다고 한다...
Flutter는 항상 이 메서드 수행 후에 build() 메서드를 호출하므로 setState() 이후 모든 추가 호출은 불필요함. 

Android에서 뷰는 화면에 나타나는 모든 것의 기반이다. 버튼, 툴바, 입력창 등 모든 것이 뷰다. Flutter에서는 위젯이 뷰와 유사하다. 위젯이 안드로이드 뷰와 정확하게 일치하는 것은 아니지만, Flutter를 익힐 때 위젯이 UI를 선언하고 구성하는 방식이라고 이해할 수 있다. 
위젯은 뷰와 조금 차이가 있음. 먼저 생명주기가 다르다. 위젯은 변경 불가능하며, 변경이 필요할 때까지만 존재한다. 위젯 혹은 위젯의 상태가 변경되면, Flutter는 위젯 인스턴스의 새로운 트리를 rebuild한다. 반면 안드로이드의 뷰는 한번만 그려지고, invalidate가 호출되기 전까지 다시 그리지는 않는다. 
Flutter의 위젯은 불변하기 때문에 가볍다. 위젯 그 자체가 뷰가 아니기 때문에 어떤 것도 직접 그리지 않고, 대신 UI에 대한 설명이며, 내부적으로 이미 influate된 실제 뷰 객체 UI의 semantics에 불과함. 
Flutter는 material component library를 포함한다. 위젯은 이 디자인 가이드라인을 따르고 있다. material design은 모든 플랫폼에 최적화된 유연한 디자인 시스템이다.
그러나 Flutter는 모든 디자인 언어를 적용할 수 있을만큼 유연하고 표현력이 우수하다. 예를 들어 iOS에서는 cupertino 위젯을 적용하여 애플의 iOS 디자인 언어와 유사한 인터페이스를 만들 수 있다. 
안드로이드에서는 뷰를 직접 수정하여 변경사항을 적용한다. 하지만  Flutter에서 위젯은 불변이기 때문에 직접 변경할 수 없고, 대신 위젯의 state를 변경할 수 있다. 
