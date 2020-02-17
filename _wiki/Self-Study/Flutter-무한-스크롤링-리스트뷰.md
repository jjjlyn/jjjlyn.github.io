사용자가 스크롤을 해서 무한대로 리스트를 보여주려면 ListView의 Builder 팩토리 생성자를 이용하면 된다. 상황에 따라서 목록의 뷰를 느리게 빌드할 수 있다. _buildSuggestions 메소드가 리스트 뷰의 row를 그려주는 기능인데 itemBuilder는 리스트뷰에 row 하나가 추가될 때마다 호출되는 콜백을 포함하고 있다. 

```dart
void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
    @override
    Widget build(BuildContext ctx){
        return new MaterialApp(
            home: ListDisplay(),
        );
    }
}

class ListDisplay extends StatelessWidget {
    @override
    Widget build(BuildContext ctx){
        return new Scaffold(
            appBar: new AppBar(title: Text("Dynamic Demo"),),
            body: ListView.builder(
                itemCount: litems.length,
                itemBuilder: (BuildContext ctx, int index){
                    return new Text(litems[index]);
            )
            ),
        );
    }
}
```

List<String> litems = [];
final TextEditingController eCtrl = new TextEditingController();

```javascript
function foo(b){
    var a = 10;
    return a + b + 11;
}

fuction bar(x) {
    var y = 3;
    return foo(x * y);
}

console.log(bar(7));
```

bar를 호출할 때, bar의 파라미터와 지역 변수를 포함하는 첫번째 프레임이 생성된다. bar가 foo를 호출하면 두번째 프레임이 만들어져 foo의 인수와 지역 변수가 들어있는 프레임이 첫 번째 프레임의 맨 위에 푸시된다. foo가 반환되면 최상위 프레임 요소는 bar의 호출 프레임만을 남겨둔 채로 스택을 빠져나온다. bar가 반환되면 스택은 비워진다. 

Heap
객체들은 힙 안에 할당된다. 힙은 구조화되지 않은 넓은 메모리 영역

Queue 
JavaScript 런타임은 처리할 메시지 목록인 메시지 대기열을 사용한다. 각 메시지에는 메시지를 처리하기 위해 호출되는 관련 함수가 있다. event loop 중 어떤 시점에서 런타임은 대기열에서 가장 오래된 메시지부터 처리하기 시작한다. 그렇게 하기 위해, 메시지는 큐에서 제거되고 해당 기능이 메시지를 입력 매개 변수로 호출한다. 함수를 호출하면 그 함수 사용을 위한 새로운 스택 프레임이 생성됨.

메인 메소드가 실행될 때 이벤트 루프가 태스크를 처리하는 과정이 있어야 데이터가 변화하는 것이다. UI를 빌딩할 때 약간의 버벅거림이 느껴지는 것도 이 처리 과정 때문이다. 
Like doing IO operation or bitmap manipulation is kind of CPU or Disk process, and it takes a lot of time inside the event loop(event queue에 대기하던 task 를 call stack 으로 호출하여 처리한다), so the next frame will not get updated and UI will be stuck for a while. 이벤트 루프에서 Call stack으로 호출된 task를 처리하는 동안 그 다음 frame은 작업이 완료되기 전까지 update될 수 없고, 일정 시간동안 멈춰있게 되는 것이다. 
Dart는 Single thread로 돌아가는 시스템이기 때문에, 강도높은 태스크는 메인 스레드에서 작업할 수 없도록 설계하는 것이 중요하다.
그런데 싱글 스레드라면서 메인 스레드에서 작업할 수 없으면 뭐 어디서 작업하라는거임?

Isolates in Flutter do not share memory. Interaction between diffent Isolates is made via messages in terms of communication

Create isolate in 2 ways:
Use compute fuction and it will handle all communication process for data sending and receiving. 
Write all logic to 'send and receive data' back-and-forth yourself.

Isolates are kind of TCP Socket.
We need to create a handshake mechanism for this. Because Isolate does not share memory and you need to use ports and messages to pass data back-and-forth. That is why we need to user some kind of handshake. 
TCP 소켓의 일종이며, 각각의 Isolate는 메모리를 공유하지 않는다. 우리는 데이터를 패스하기 위해 포트와 메시지가 필요할 것이다. 

There are some rules for creating Isolate
1. Isolates need to have non-static top-level function.
Static하지 않은 최상위 function을 갖고 있어야 한다.
1. If you use compute a function or Isolate.spawn function, for now, function must have one parameter.

```dart
external static Future<Isolate> spawn<T>(
    void entryPoint(T message), T message,
    {bool paused: false,
    bool errorsAreFatal,
    SendPort onExit,
    SendPort onError,
    @Since("2.3") String debugName});
```

Check entryPoint(T message) method call, It takes T message and you are required to have a single parameter function.

1. Isolates take up around ~2mb per Isolate and it's lightweight so use it carefully. Isolate memory size and allocation <= 이걸 알아야 함
2. Every message pass from Isolates requires message size * 2 memory because it will be two copies on system, since Isolate do not share memory, so if you have 1GB of file and want to pass from one Isolate to second, it will take 2GB of memory on device(RAM)
3. Isolates and Ports can be alive even though you have completed your work, or all statements are executed from the method. So it's up to us to kill isolates and close the ports.

Import isolate package, and call startIsolate() function. We will start by creating ReceivePort which is used to convey a message back and forth. The thing to remember ReceivePort Stream can only be used 'one time', so if we send data one time, and taking data from receivePort.first now, when we send data again and try to get data from receivePort.first -> it will not work and throw an error with the statement. The most common form of Stream can listen only once at a time. If you try to add multiple listeners, it also throw an exception. So we will use receivePort.first to get SendPort back and forth, and if we want to use just one ReceivePort for better optimization(여러개를 생성하지 않고 하나의 포트만 생성하는 것이 최적화에 좋다), we need to use listen to methods from that class.

1. Create Receive Port Object
2. Spawn Isolate (Create Isolate) with the send port(this is kind of like a port like a socket 8080 port where we launch our socket, and others can access it by calling website ...:8080 and server will give the response to that request on that port)
3. In Isolate we need to use a 'handshake', What I mean by this is we have 2 threads and we need to communicate messages, so we need to have some stream where we listen for both sides of events, and so we will pass sendPorts first to establish where we need to send the message. 
4. So after creating Isolate by spawn, we will wait for Isolate to send its SendPort, to which will ping for message.
5. At the side of Isolate, we will again create ReceiverPort and send a message to the SendPort we have passed 
   
6. Remember to use receiverPort.first only one time and we will get the SendPort. 
7. Now once we get all ports, It's like socket open channel we will send messages back and forth.

```dart
startIsolate() async{
    ReceivePort receivePort = ReceivePort();
    var isolate = await Isolate.spawn(calculateFunction,
    receivePort.sendPort);
    
    SendPort sendPort = await receivePort.first;
   }
   
   calculateFunction(SendPort sendPort) async{
    var calculateFunctionReceivePort = ReceivePort();
    sendPort.send(calculateFunctionReceivePort.sendPort);
    
    await for(var msg in calculateFunctionReceivePort){
        var data = msg[0];
        SendPort replyTo = msg[1];
        replyTo.send("$data from Isolate");
    }
}

Isolate를 만들어서 병렬 처리를 해야 한다. 스레드 안에 Isolate가 여러개 있는 것을 상상하면 된다. Isolate는 분리된 작업 단위로서, 각각의 메모리 힙이 있다. 락을 걸 수 없기 때문에 경쟁 상태나 데드락이 발생하지 않는다. 가장 기본이 되는 Isolate는 main isolate. 다트 런타임에 의해 만들어진다. main isolate는 필요에 따라 Isolate(spawn)을 만들어 쓴다. isolate을 만드는 것을 spawn(자식을 낳다)라고 한다. 

```dart
void main(){
    Isolate.spawn(sendMessage, 'Hello');
    Isolate.spawn(sendMessage, 'Greetings');
}

void sendMessage(var message){
    print('This is a ${message}');
}
```

main을 실행하면 3개의 Isolate가 만들어진다. 그러나 각각의 Isolate는 순서대로 실행되지는 않는다. 

- Future: 바로 끝나지 않는 작업을 할 때 쓰인다. 일반적인 함수는 결과를 리턴하는데, 비동기 함수는 Future를 리턴한다.
- await: 비동기 처리가 끝날 때까지 기다린다는 뜻이다. 다른 작업을 진행하지 않는다. Future의 작업을 완료하고 다음 작업을 할 때 쓰인다.
- async: 비동기 처리를 하겠다는 선언이다. await는 async와 항상 함께 쓰인다. 
