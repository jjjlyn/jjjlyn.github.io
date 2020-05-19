```dart
final StreamSubscription subscription = ctrl.stream.listen((data) => print('$data'));

ctrl.sink.add('my name');
// StreamController가 sink property로 접근 가능한 StreamSink라는 entrance를 사용한다. 

ctrl.sink.add(1234);
ctrl.sink.add({'a': 'element A', 'b': 'element B'});
ctrl.sink.add(123.45);

ctrl.close();
```

그런데 도대체 add로 데이터는 더 추가하는거지? Single-subscription에 대한 예시라고 한다. 솔직히 뭔 개소리인지 모르겠다.

StreamTransformer - Broadcast Stream의 예시
integer 값들만 전달하고 그 중 짝수만 print한다. 

```dart
void main(){
    final StreamController ctrl = StreamController();
    // Stream을 컨트롤 하는 것, Stream에 무언가를 삽입하려면 sink rpoperty로 접근 가능한 StreamSink라는 entrance를 사용해야 한다.

    final StreamSubscription subscription = ctrl.stream.listen((data) => print('$data'));
    // stream의 데이터 흐름을 구독할 때는 StreamSubscription 객체가 필요하다.

    ctrl.sink.add('my name');
    ctrl.sink.add(1234);
    ctrl.sink.add({'a': 'element A', 'b': 'element B'});
    ctrl.sink.add(123.45);
    // Stream에 데이터를 삽입할 때 sink property에 접근해서 집어넣어야 한다. 

// 이런건 GC에서 안 해주나?
    ctrl.close();
}
```

RxDart Package -> 이걸 알아야 Streams을 더 완성도 있게 알 수 있다. 
ReactiveX API Dart에서 사용할 수 있도록 구현된 패키지다. 
Dart
Stream / StreamController
Observable / Subject

RxDart는 기존 Dart Streams API를 extends하고 StreamController의 3가지 변형을 제공한다.

1) PublishSubject
PublishSubject는 일반적인 broadcast Stream Controller인데, 한 가지 예외 사항이 있다. 
stream은 Stream말고 Observable을 리턴한다. PublishSubject는 subscription 이후에 들어오는 events들만 listener에게 보낸다. 

StreamSubscription - Stream을 더 이상 listen하지 않으면 subscription cancel하기
StreamController - StreamController가 필요없으면 close
BehaviorSubject ReplaySubject PublishSubject 모두 controller다. 이는 Stream말고 같은 기능이지만 다른 명칭의 Observable을 리턴한다.

StreamBuilder는 StatefulWidget이다.
StreamBuilder는 Stream을 listen하고, data가 stream을 나갈 때마다 자동으로 builder callback을 호출해서 rebuild를 한다.

```dart
StreamBuilder<T>(
    key: // ...optional (the unique id of this widget),
    stream: // ...the stream to listen to,
    // 아니, 그러면 StreamSubscription은 필요없는가?
    initialData: // ...any initial data, in case the stream would initially be empty,
    builder: (BuildContext context, AsyncSnapshot<T> snapshop){
        if(snapshot.hasData){
            return // ... the Widget to be built base on snapshop.data
        }
        return // ...the Widget to be built if no data is available
    }
)
```

Stream을 사용해서 setState()를 빼고 구현해보자

```dart
import 'dart:async';
import 'package:flutter/material.dart';

class CounterPage extends StatefulWidget {
    @override
    _CounterPageState createState() => _CounterPageState();
}

class _CounterPageState extends State{

    int _counter = 0;
    final StreamController<int> _streamController = StreamController<int>(); // Stream의 흐름을 관리하는 StreamController

    @override
    void dispose(){
        _streamController.close(); // 사용이 끝났으면 reference를 끊어준다
        super.dispose();
    }

    @override
    Widget build(BuildContext context){
        return Scaffold(
            appBar: AppBar(title: TExt('Stream version of the counter app'));
            body: Center(
                child: StreamBuilder<int>(
                    stream: _streamController.stream, // observe할 stream을 구독
                    initialData: _counter, // 초기 데이터
                    builder: (BuildContext context, AsyncSnapshot<int> snapshot){
                        return Text('You hit me: ${snapshot.data} times');
                    } // data가 stream을 나갈 때마다 자동으로 builder callback을 호출해서 rebuild를 한다.
                )
            ),
            floatingActionButton: FloatingActionButton(
                child: const Icon(Icons.add),
                onPressed: (){
                    _streamController.sink.add(++_counter);
                } // 버튼을 누르면 counter를 증가시키고 이 값을 Stream에 sink를 사용해서 보낸다. Stream에 sink하는 행동은 StreamBuilder로 하여금 rebuild를 하고 counter를 refresh하게 한다. 
                // 모든 값은 Stream으로 전달되기 때문에 state는 따로 필요가 없어지게 된다. 원래 위젯의 생명 주기 상 setState()를 하면 dirtyState()로 가서 다시 rebuild하는데 여기서는 cleanState()에서 stream builder call back 함수 자체가 호출되는 것으로 구현되어 있기 때문에 그 후에는 자동 rebuild가 된다. 솔직히 약간 이해가 안 된다. 생명주기를 어기는 행위 아닌가? 이를 이용해서 구현되어 있는 걸텐데, 크게 보면.
                // 원래는 setState()를 사용할 때마다 전체 위젯은 rebuild되었다. 하지만 이제 only StreamBuilder만 rebuild하게 되었다. 이것도 이해가 잘 안 된다. 이런 식의 구현이 어찌 가능한지? StatefulWidget을 사용하는 이유는 무엇일까? 상태가 더 이상 필요없는데. 이유는 StreamController를 dispose()에서 release해주기 위해서이다.
            )
        );
    }
}
```

Reactive Programming이 무엇인가? 비동기 데이터 stream으로 프로그래밍하는 것을 의미한댄다. 즉 event(e.g. tap), 변수 변경 등이 data stream으로 trigger 혹은 전달된다.

```dart
@override
Widget build(BuildContext context){
    RefreshController _controller = RefreshController();
    final MainBloc bloc = BlocProvider.of<MainBloc>(context); 
    // StreamController의 replacement인가? 100%그럴 삘이다.
    bloc.homeEventStream.listen((event){
        if(labelId == event.labelId){
            _controller.sendBack(false, event.status);
        }
    });

    if(isHomeInit){
        isHomeInit = false;
        Observable.just(1).delay(Duration(milliseconds: 500)).listen(_){
            bloc.onRefresh(lable: labelId);
            bloc.getHotRecItem();
            bloc.getVersion(); // 일단 여기까지는 뭔 소린지 모르겠다. Observable을 listen하는 건 알겠는데 뭐 어쩌라고
        });
    }

    return StreamBuilder( // 이 StreamBuilder는 뭐고 아래 StreamBuilder는 뭐임? 
        stream: bloc.bannerStream, // bloc은 백퍼센트 컨트롤러일 것이다 : 배너 정보를 받아올 것이다
        builder: // 이건 콜백 함수를 계속 호출해서 rebuild()시킨다.
        (BuildContext context, AsyncSnapshot<List<BannerModel>> snapshot){ // 이건 BannerModel의 리스트를 observing하는 StreamBuilder (제네릭을 잘 보면 된다)
            return RefreshScaffold(
                lableId: lableId,
                loadStatus: Utils.getLoadStatus(snapshot.hasError, snapshot.data), // 이렇게 두 가지의 status가 있다
                controller: _controller, // refresh controller
                enabledPullUp: false, // 이건 뭐냐?
                onRefresh: ({bool isReload}){
                    return bloc.onRefresh(lableId: lableId); // 이것도 뭔 개소린지 모르겠다
                },
                child: ListView(
                    children: <Widget>[
                        StreamBuilder(
                            stream: bloc.recItemStream, // recItem을 받아올 것이다
                            builder: (BuildContext context, AsyncSnapshot<ComModel> snapshot){
                                ComModel model = bloc.hotRecModel;
                                if(model == null){
                                    return Container(
                                        height: 0.0,
                                    );
                                }
                                int status = Utils.getUpdateStatus(model.version);
                                return HeaderItem(
                                    titleColor: Colors.redAccent,
                                    title: status == 0 ? model.content : model.title,
                                    extra: status == 0 ? 'Go' : "",
                                    onTap(){
                                        if(status == 0){
                                            NavigatorUtil.pushPage(
                                                context, RecHotPage(title: model.content),
                                                pageName: model.content);
                                            ) else {
                                                NavigatorUtil.launchInBrowser(model.utl, title: model.title);
                                            }
                                        },
                                    }
                                );
                            }),
                        buildBanner(context, snapshot.data),
                        StreamBuilder(
                            stream: bloc.recRepoStream,
                            builder: (BuildContext context,
                                AsyncSnapshot<List<ReposModel>> snapshot){
                                    return buildRepos(context, snapshot.data);
                                }),
                        StreamBuilder(
                            stream: bloc.recWxArticleStream,
                            builder: (BuildContext context, AsyncSnapshot<List<ReposModel>> snapshot){
                                return buildWxArticle(context, snapshot.data);
                            }),
                    ]
                ),
            );
        });
}
```

Streams와 Listeners 구조를 갖게 된다. 
asynchronous 하게 된다.
어디서 어떤 일이(evnet, change of variable...) 발생하면 Stream으로 전달된다.
예를 들어서 application을 사용하는 도중에 버튼을 클릭하는 event가 발생하면 해당 event가 stream으로 전달된다. widget이 무언가를 stream으로 보내면 widget은 다음과 같은 정보들을 알 필요가 없다.
- 다음에 무슨 일이 일어날지
- 누가 이 정보를 사용할지
- 어디에 언제 이 정보가 사용될지

The BLoC Pattern
BLoC Pattern은 구글의 Paolo Soares와 Cong Hui에 의해 디자인되었고 DartConf 2018 때 이 것을 처음으로 선보였다. 

BLoC은 Business Logic Component를 의미한다. 
Business Logic은 다음과 같아야 한다.
- be moved to one or several BLoCs
- Presentation Layer에서 가능한 제거되어야 한다. 다른 말로 하면 UI components는 UI만 고려해야 하고 business logic에 관련된 것들은 신경쓰지 말아야 한다.
- rely on exclusive use of Streams for both input(Sink) and output(Stream) 
전적으로 Streams에 의존한다(input과 output에 대해서)
- platform에 독립적이어야 한다. 
BLoC pattern은 원래 platform(웹 앱, 모바일 앱, 백엔드)에 독립적으로 같은 코드를 재사용하기 위해 고안되었다. 

BLoC은 Streams를 이용한다.
- Widget은 events를 Sinks를 통해 BLoC에 보낸다.
- Widget은 BLoC의 streams로 notify를 받는다.
- BLoC에 구현된 business logic은 그 어느 것도 신경쓰지 않는다. 

Business Logic을 UI로부터 분리했기 때문에 Business Logic을 변경해도 application에 minimal한 영향을 주고, vice versa
BLoC을 test하는 것도 쉬움
이 패턴을 가능하게 하는 것은 역시 Stream이다.

```dart

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
    @override
    Widget build(BuildContext context){
        return new MaterialApp(
            title: 'Streams Demo',
            theme: ThemeData(
                primarySwatch: Colors.blue,
            ),
            home: BlocProvider<IncrementBloc>(
                bloc: IncrementBloc(),
                child: CounterPage(),
            )
        );
    }
}

class CounterPage extends StatelessWidget{
    @override
    Widget build(BuildContext context){
        final IncrementBloc bloc = BlocProvider.of<IncrementBloc>(context);

        return Scaffold(
            appBar: AppBar(title: Text('Stream version of the Counter App')),
            body: Center(
                child: StreamBuilder<int>(
                    stream: bloc.outCounter,
                    initialData: 0,
                    builder: (BuildContext context, AsyncSnapshot<int> snapshot){
                        return Text('You hit me: ${snapshot.data} times');
                    }
                ),
            ),
            floatingActionButton: FloatingActionButton(
                child: const Icon(Icons.add),
                onPressed: (){
                    bloc.incrementCounter.add(null);
                },
            )
        );
    }
}


class IncrementBloc implements BlocBase {
    int _counter;

    StreamController<int> _counterController = StreamController<int>();
    StreamSink<int> get _inAdd => _counterController.sink;
    Stream<int> get outCounter => _counterContoller.stream;

    StreamController _actionController = StreamController();
    StreamSink get incrementCounter => _actionController.sink;

    IncrementBloc(){
        _counter = 0;
        _actionController.stream.listen(_handleLogic);
    }

    void dispose(){
        _actionController.close();
        _counterController.close();
    }

    void _handleLogic(data){
        _counter = _counter + 1;
        _inAdd.add(_counter);
    }
}

```

global singletone으로 할 수 있지만 추천하는 방법이 아니다. Dart에는 class destructor가 없어서 resource를 release 하는 것이 힘듦
local instance BLoC의 로컬 인스턴스를 인스턴스화해서 사용할 수 있다. dispose()메소드의 이점을 활용하기 위해 StatefulWidget을 사용하는 경우에는 이 방법이 적절하고 많이 사용하는 것 같다.
ancestor.BLoC에 접근하는 가장 일반적인 방법은 StatefulWidget으로 구현된 ancestor Widget을 사용하는 것이다. 

```dart
import 'package:flutter/material.dart';

typedef BlocBuilder<T> = T Function();
typedef BlocDisposer<T> = Function(T);

abstract class BlocBase {
    void dispose();
}

class BlocProvider<T extends BlocBase> extends StatefulWidget {
    BlocProvider({
        Key key,
        @required this.child,
        @required this.blocBuilder,
        this.blocDispose,
    }) : super(key: key);
    // 생성자

    final Widget child;
    final BlocBuilder<T> blocBuilder;
    final BlocDisposer<T> blocDispose;

    @override
    _BlocProviderState<T> createState() => _BlocProviderState<T>();

    static T of<T extends BlocBase>(BuildContext context){
        _BlocProviderInherited<T> provider = context.getElementForInheritedWidgetOfExactType<_BlocProviderInherited<T>>()?.widget;

        return provider?.bloc;
    }
}
```

BLoC에 관련된 Article을 보면 Provider를 InheritedWidget으로 구현한 것을 볼 수 있다. 하지만 위 코드에서는 State Class 안에 구현을 했다. InheritedWidget은 내부에 dispose 메소드를 제공하지 않는다. 하지만 사용되지 않는 resource를 release하는 것은 필요한 일이고 이렇게 하는 것이 좋은 연습이다. 
그렇기 때문에,
InheritedWidget을 StatefulWidget안에 구현한 것은
오로지 BLoC에 의해 할당된 resources들을 release하기 위해서이다.

```
class Bloc with Validators{
    final _email = BehaviorSubject<String>(); // StreamController를 Subject라고 한다.
    final _password = BehaviorSubject<String>();

    Stream<String> get email => _email.stream.transform(validateEmail);
    Stream<String> get password => _password.stream.transform(validatePassword);
    Stream<bool> get submitValid =>
        Observable.combineLatest2(email, password, (e, p) => true);

    Function(String) get changeEmail =>  _email.sink.add;
    Function(String) get changePassword => _password.sink.add;

    submit(){
        final validEmail = _email.value;
        final validPassword = _password.value;
        print(validEmail);
        print(validPassword);
    }

    dispose(){
        _email.close();
        _password.close();
    }
}
```

BLoC 패턴 내에서 로직 및 데이터 처리 부분이 들어간다.
여기서는 단순하게 입력된 데이터에 대한 validation 체크 및 최종 화면 출력만 있다. 


UI 쪽 소스
1. final bloc = Provider.of(context);
2. 입력에 대한 유효성 체크
```dart
return StreamBuilder(
    stream: bloc.email,
    builder: (context, snapshot){
        return TextField(
            onChanged: bloc.changeEmail,
            keyboardType: TextInputType.emailAddress,
            decoration: InputDecoration(
                labelText: 'Email',
                hintText: 'you@example.com',
                errorText: snapshot.error,
            )
        );
    }
);
```

StreamBuilder 객체에 stream 속성을 통하여 BLoC에 해당 controller을 연결
onChanged 이벤트 속성으로 데이터가 변경될 때마다 bloc.changeEmail (_email.sink.add)을 호출 -> validation 체크
errorText: snapshot.error -> Error Text 처리
snapshot 객체는 Stream을 통해서 연결된 AsyncSnapshot 객체로 bloc 객체 내부에서 어떤 처리에 대한 데이터가 담기는 부분이다. 

```dart
final validateEmail = StreamTransformer<String, String>.fromHandlers(handleData: (email, sink){
    if(email.contains('@')){
        sink.add(email);
    } else {
        sink.addError("Enter a valid Email");
    }
});
```

submit 버튼 처리 부분을 보면 역시 snapshot 객체를 통해서 데이터를 확인하고 관련 처리를 한다. bloc.submit()으로 실제 해당 함수 내에서 로그인을 처리하는 로직이 포함된다.

```dart
Widget submitButton(Bloc bloc){
    return StreamBuilder(
        stream: bloc.submitValid,
        builder: (context, snapshot){
            return RaisedButton(
                color: Colors.blue,
                child: Text('submit'),
                onPressed: snapshot.hasData ? () {
                    bloc.submit();
                }
                : null,
            )
        }
    )
}
```

```dart
class CleanRobot extends Robot{
    CleanRobot(): super.create();

    @override
    String getName(){
        return "Clean";
    }

    @override
    String command(){
        print("clean a room");
        return "clean a room";
    }
}

class WarRobot extends Robot{
    WarRobot(): super.create();

    @override
    String getName(){
        return "War";
    }

    @override
    String command(){
        print("declare war");
        return "declare war";
    }
}

abstract class Robot {

    Robot.create();
    
    factory Robot(RobotType type){
        switch(type){
            case RobotType.Clean:
                return CleanRobot();
            case RobotType.War:
                return WarRobot();
            }
        }

    String getName();
    String command();

    enum RobotType{
        Clean, War
    }
}


Map<String, int> map = Map<String, int>();
map.putIfAbsent("height", () => 175);
map.putIfAbsent("height", () => 180);

print(map["height"]); // 175 출력

map["height"] = 190;
print(map["height"]); // 190 출력
```
다트에서는 어떻게 병렬 작업을 할까?
다트는 싱글 스레드이고 여러 스레드를 만들 수 없다. 
다트에서는 Isolate를 만들어서 병렬 처리를 한다. 
스레드 안에 Isolate가 여럿 있는 것을 상상하면 된다.
Isolate는 분리된 작업 단위이다. Isolate는 각각의 메모리 힙이 있다.
Lock을 걸 수 없어서 경쟁 상태나 데드락이 발생하지 않는다. 

가장 기본이 되는 Isolate는 main isolate이다. 다트 런타임에 의해 만들어진다.
main isolate는 필요에 따라 isolate를 만들어 쓴다. 

```dart
void main(){
    Isolate.spawn(sendMessage, 'Hello');
    Isolate.spawn(sentMessage, 'Greetings');
    Isolate.spawn(sendMessage, 'Welcome');
}

void sendMessage(var message){
    print('This is a ${message}');
}
```

main을 실행하면 3개의 isolate가 만들어진다. 
각각의 isolate는 순서대로 실행되지는 않는다.

자바는 오래된 언어이다 보니 비동기 처리가 기본적으로 지원되지는 않는다. 
비동기는 retrofit을 쓰잖아...왜 안된다는겨
Future, Async, Await
RxJava는 난이도가 좀 있는 편이라 처음엔 쓰기 어렵지 ㅠㅠ
다트의 비동기 처리는 간편하다. Future객체를 쓴다.

```java
CompletableFuture.runAsync(() ->  {
    System.out.println("Run async in CompletableFuture");
});

Observable.just(1,2,3,4,5)
          .subscribeOn(Schedulers.io())
          .subscribe(
              numbers -> {
                  System.out.println(numbers);
              }
          );
```

-   Future는 바로 끝나지 않는 작업을 할 때 쓰인다. 일반적인 함수는 결과를 리턴하지만 비동기 함수는 future를 리턴한다.
- await는 비동기 처리가 끝날 때까지 기다린다는 의미다. 다른 작업을 진행하지 않는다. Future의 작업을 완료하고 다음 작업을 할 때 쓰인다.
- async는 비동기 처리를 하겠다는 선언이다.

```dart
void main(){
    first();
    second();
    third();
}

// First
// Third
// Second 가 나오겄지... await으로 비동기가 끝날 때까지 기다리라는 선언을 안했으니까 

void first(){
    print("First");
}

void second() async{
    Future.delayed(Duration(seconds: 2), (){
        print("Second");
    });
}

void third(){
    print("Third");
}
```

await을 써야 Future가 완료할 때까지 나머지 부분이 기다린다. 
그렇다면 중간에 멈추지 않고 Future값을 처리하려면 어떻게 해야할까?

```dart // 비동기 파일 읽기

void main(){
    print("Before");
    readFileAsync().then((data) => print(data));
    print("After");
}
Future readFirleAsync() async {
    return await File("file.json").readAsString();
}
/*  Before
    After
    {
        "name": "hochul",
        "age" : 33,
    }
*/
```

반면 await을 쓰면 파일 읽기를 완료할 때까지 다른 작업을 하지 않는다.

```dart
void main(){
    print("Before");
    print(await readFileAsync());
    print("After");
}
```

await을 쓰면 간단한 비동기처리를 할 수 있지만 메인 함수에서 쓰는 것은 주의하도록 해야 한다. 

Isolate vs Future
- Isolate: 병렬 작업을 할 수 있게 해준다. 멀티 스레드와 유사하며, 여러 Isolate를 만들어서 작업할 수 있게 해준다.
- Future: 비동기 처리를 하지만 보통 한 스레드(원래 한 스레드이긴 하다만...)에서 작업이 이루어진다.

Request Options
The Options class descriobes the http request infomration and configuation.
Each Dio instance has a base config for all request maked vy itself, and we can override the base config with
options when make a single request.
The BaseOptions