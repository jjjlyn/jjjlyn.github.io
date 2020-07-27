[Event Loop in Javascript]

자바스크립트 엔진은 Single Thread 안에 Stack, 3가지의 Queue, Rendering으로 구성되어 있다.
자바스크립트는 기본적으로 Single Threaded로 동작하는 언어이며, Multi-Threaded처럼 보일 수 있는 이유는 브라우저가 제공하는 각종 WEB API 덕분이다.
부장님이 "스레드는 메소드 단위로 실행된다"라고 했던 게 이제서야 이해가 된다. 
Stack에는 메소드가 LIFO(마지막으로 들어온 것이 먼저 나간다)방식으로 돌아가는 Stack에 쌓이고, 이 Stack의 메소드가 모두 실행되어 비워지게 되면, 그제서야 이벤트 루프가 Stack공간에 멈춰있다 다시 빙글빙글 돌기 시작한다. Rendering Callback에 대기한 콜백을 모두 실행하고, MicroTask Queue의 콜백도 마찬가지로 모두 실행하고, 실행 도중 새로운 콜백 메소드가 추가되면 추가된 콜백까지 모두 실행한다. 그렇기 때문에 MicroTask Queue의 콜백이 계속 쌓이도록 Promise 재귀를 하는 등 코딩을 하면, 브라우저가 멈춰 있는 엿같은 User Experience를 선사하게 된다. 그 다음에는 일반적인 Task Queue에서 Callback 단 하나만 Stack에 가져와 실행하고 다시 루프를 뺑뺑이 돈다. setTimeout등에서 등록하는 콜백함수는 일반 Task Queue에 등록되며, Promise에 등록되는 콜백은 Microstack Queue에 등록된다. setTimeout 같은 API는 일반 Task Queue에 등록되기 때문에 재귀호출헤도, Event Loop가 Queue에서 콜백 하나씩만 꺼내와 Stack에 넣기 때문에, 브라우저가 멈추는 현상은 발생하지 않는다.
가끔 Rendering (rendering callback(화면에 반영할 만한 UI 요소 적용) + layout + paint => composition) 쪽으로 Event Loop가 도는데, 왜냐하면 사용자 눈에 애니메이션이 움직이는 것처럼 보이려면 60fps(frame per seconds)를 충족시켜야 하기 때문에 주기적으로 Rendering 쪽으로 이벤트 루프를 돌려 GUI rendering을 해주는 것이다.
어차피 스택에 등록된 콜백은 시작~종료를 '보장'하기 때문에(이벤트 루프가 콜백 블록이 실행 완료할 때까지 기다려준다는 뜻), 콜백 블록 안에서 GUI 요소를 어떤 순서로 바꿔준다 하더라도, 이미 변경 요소는 콜백 블록 종료 시점에서 모두 반영이 되고, 콜백 종료 후 Rendering Callback으로 Event Loop가 가서 이 변경 요소를 모두 Noti 시키기 때문에, 콜백 블록 내부에서 렌더링을 어떻게 할 것인가 순서 등은 사실 별로 중요하지 않다.

A pending promise can either be fulfilled with a value, or rejected with a reason (error). When either of these options happens, the associated handlers queued up by a promise's then method are called. then 에 선언된 handlers 가 job queue에 등록된다. If the promise has already been fulfilled or rejected when a corresponding handler is attached, the handler will be called, so thener is no race condition between an asynchronous operation completing and its handlers being attached. Promises in JavaScript represent processes that are already happening, which can be chained with callback functions. A promise is said to be settled if it is either fulfilled or rejected, but not pending. You will also hear the term resolved used with promises -- this means that the promise is settled or 
"locked in" to match the state of another promise. States and fates contains more details about promise terminology.
The methods promise.then(), promise.catch(), and promise.finally() are used to associate further action with a promise that becomes settled. These methods also return a newly generated promise object, which can optionally be used for chaining; for example, like this:

```javascript
const myPromise = 
    (new Promise(myExecutorFunc))
    .then(handleFulfilledA, handleRejectedA)
    .then(handleFulfilledB, handleRejectedB)
    .then(handleFulfilledC, handleRejectedC);
```

In the absense of an immediate need, it is simpler to leave out error handling until a final .catch() statement. The signatures of these two functions are simple, they accept a single parameter of any type. These function are written by you, the programmer. The termination condition of these functions determines the "settled" state of the next promise in the chain. Any termination other than a throw creates a "resolved" state, while terminating with a throw creates a "rejected" state.

When a .then() lacks the appropriate function, processing simply continues to the next link of the chain. Therefore, a chain can safely omit every handleRejection until the final .catch(). Simply, .catch() is really just a .then() without a slot for handleFulfilled.
The promises of a chain are nested like Russian dolls, but get popped like the top of a stack. 
The first promise in the chain is most deeply nested and is the first to pop. 
// Nested 되어 있는 형태처럼 보이지만 실제로는 각각의 Callback 함수로 job queue에 등록되고, 차후에 Call Stack으로 넘어가 pop 된다. (LIFO)

(promise D, (promise C, (promise B, (promise A))))
The return causes a promise to be popped, but the nextValue promise is pushed into its place. For the nesting shown above, suppose the .then() associated with promise B returns a nextValue of promise X. 

A promise can participate in more than one nesting. For the following code, the transition of promiseA into a "settled" state will cause both instances of .then() to be invoked.

```javascript
const promiseA = new Promise(myExecutorFunc);
const promiseB = promiseA.then(handleFulfilled1, handleRejected1);
const promiseC = promiseA.then(handleFulfilled2, handleRejected2);
```

In that case the action (if appropriate) will be performed at the first asynchronous opportunity. Note that promises are guranteed to be asynchronous. Therefore, an action for an already "settled" promise will occur only after the stack has cleared and a clock-tick has passed. The effect is much like that of setTimeout(action, 10).

Promise.reject(reason)
Promise.resolve(value)
-> Both of these return a new promise object.

If the value is a thenable(i.e. has a then method), the returned promise will "follow" that thenable, adopting its eventual state; otherwise the return promise will be fulfilled with the value.
Generally, if you don't know if a value is a promise or not, Promise.resolve(value) it instread and work with the return value as a promise.




