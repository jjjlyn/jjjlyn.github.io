```javascript
setTimeout(function(){
    console.log('A')}, 0);

Promise.resolve().then(function(){
    console.log('B');
}).then(function(){
    console.log('C');
});
```

마이크로 태스크 큐는 일반 태스크(매크로 태스트) 큐보다 더 높은 우선순위를 갖는다. 즉 태스크 큐에 대기 중인 태스크가 있더라도 마이크로 태스크가 먼저 실행된다. 예를 들어 마이크로 태스크가 계속 실행될 경우 일반 태스크인 'UI 렌더링'이 지연되는 현상이 발생할 수도 있을 것이다.

마이크로 태스크를 사용하는 다른 API에는
- MutationObserver
- Node.js의 nextTick

자바스크립트는 단일 스레드 기반의 언어이다. 스레드가 하나라는 말은 곧, 동시에 하나의 작업만을 처리할 수 있다는 말이다. 하지만 실제로 자바스크립트가 실행되는 환경을 생각해보면 많은 작업이 동시에 처리되고 있는데, 예를 들어 웹 브라우저에서는 애니메이션 효과를 보여주면서 마우스 입력(둘다 매크로 태스크)을 받아서 처리하고, Node.js 기반의 웹 서버에서는 동시에 여러개의 HTTP 요청을 처리하기도 한다. 어떻게 스레드가 하나인데 이런 일이 가능할까? 이때 등장하는 개념이 바로 이벤트 루프. 자바스크립트는 이벤트 루프를 이용해서 비동기 방식으로 동시성을 지원한다. 

ECMAScript에는 이벤트 루프가 없다. 실제로 V8같은 자바스크립트 엔진은 단일 호출 스택(Call stack)을 사용하며, 요청이 들어올 때마다 해당 요청을 순차적으로 호출 스택에 담아 처리할 뿐이다. 그렇다면 비동기 요청은 어떻게 이루어지며, 동시성 처리는 누가 하는 것인가? 바로 이 JS 엔진을 구동하는 환경, 즉 브라우저나 Node.js가 담당한다.

```javascript
$('.btn').click(function(){ // Callback A
    try {
        $.getJSON('/api/members', function(res){ // Callback B
            // 에러 발생 코드
        });
    } catch(e){
        console.log('Error: ' + e.message);
    }
});
```

onClick이벤트로 인한 Callback A 함수가 매크로 태스크 큐에 들어간 후, 이벤트 루프에 의해 콜 스택에 들어가 실행되고 나면 try { } 블록이 execute되면서, $.getJSON ~~ 파트에서 오류가 발생하게 된다. XMLHttpRequest API(브라우저 API)를 통해 서버로 비동기 요청을 보낸 후 콜 스택에서 사라진다. 이후 서버에서 응답을 받은 브라우저는 콜백 B를 태스크 큐에 추가하고, B는 이벤트 루프에 의해 실행되어 호출 스택에 추가된다. 하지만 이때 A는 이미 호출 스택에서 비워진 상태이기 때문에, 호출 스택에는 B만 존재하게 된다. 즉 B와 A는 실행될 때와는 전혀 다른 독립적인 컨텍스트에서 실행되며, 그렇기 때문에 A 내부의 catch문의 영향을 받지 않게 된다.

```javascript
$('btn').click(function(){
    showWaitingMessage(); // UI 렌더링이므로 태스크 큐에 추가된다.
    longTakingProcess(); // 실행된다.
    hideWaitingMessage(); // 실행된다. 
    showResult(); // 실행된다. 
});
```

showWatingMessage의 함수 실행이 끝나고 렌더링 엔진이 렌더링 요청을 보내도 해당 요청은 태스크 큐에서 이미 실행중인 테스크가 끝나기를 기다리고 있기 때문에 대기하고 있다. 실행중인 태스크가 끝나는 시점은 호출 스택이 비워지는 시점인데, 그 때는 이미 showResult()까지 실행이 끝나 있을 것이고, 결국 렌더링이 진행되는 시점에는 hideWaitingMessage로 인해 로딩 메시지가 숨겨진 상태일 것이다. 
이를 방지하기 위해 코드를 개선하자면 이렇게 된다.

```javascript
$('btn').click(function(){
    showWaitingMessage(); 
    // 태스크 큐에 먼저 추가되고 그 이후 메소드들은 나중에 순서대로 추가된다. 
    setTimeout(function(){
        longTakingProcess();
        hideWaitingMessage();
        showResult();
    }, 0);
});
```

```javascript
var data = ajax("http://some.url.1"); // microtask queue에 등록된다.
console.log(data); // call stack에서 먼저 실행되고 사라진다.
// `data`는 ajax result 값을 받지 못한다. 
```

```javascript
ajax("http://some.url.1", function myCallbackFunction(data){
    console.log(data);
}); 
```

Ajax를 절대 동기적으로 실행하려고 해서는 안된다. Microtask queue에서 대기하고 있던 ajax의 콜백 함수가 콜 스택에 넘겨져서 실행하는 동안 이벤트 루프가 콜 스택에 멈춰있어, blocking 상태가 되어버리기 때문이다. 그런데 이건 비동기 호출에서도 이벤트 루프가 계속 도니까 똑같은거 아닌가?

```javascript
function now() {
    return 21;
}

function later() {
    answer = answer * 2; // 2
    console.log("Meaning of life:", answer);
}

var answer = now(); // 1

setTimeout(later, 1000); // 3
```

간혹 console.log()를 I/O 처리하여 콜 스택에서 바로 처리하지 않고 태스크 큐로 보내, 콜 스택이 모두 비워진 후 console.log()를 호출하는 브라우저가 있다. 이는 console.log()가 자바스크립트 명세가 아니기 때문에 브라우저마다 defer한 방식이 서로 다르기 때문이다. 
```javascript
var a = {
    index : 1;
}

console.log(a);

a.index++;
```
이럴 경우 우리가 원래 기대하는 바는 콘솔에 { index : 1 } 이 출력되는 것이지만, 실제로는 a.index++; 까지 모두 실행된 후 마지막으로 처리되어 { index : 2 } 가 찍혀 나올 수도 있다는 것이다.
JS program에서 ajax 통신을 요청할 경우(request to fetch some data from server), 자바스크립트 엔진이 호스팅 환경(웹 브라우저나 Node.js 서버)에 "함수 실행을 임시적으로 suspend 할 테니, network 요청이 끝난 후 반환된 data가 있다면, callback 함수를 call stack으로 넣어줘라"라고 알린다. 

```javascript
var eventLoop = [];
var event;

while(true){
    if(eventLoop.length > 0){
        event = eventLoop.shift();

        try {
            event();
        }
        catch (err) {
            reportError(err);
        }
    }
}
```

화면에서 화면으로 넘어갈 때 onClick()으로 넘어간다. 이 때 onClick() 이벤트에 의해 콜백이 걸리는데, 이게 CallStack에 넘어가서 실행되고 있는 중이라 가정하자. 중간에 ajax로 인하여 서버에 HTTP Response가 걸리고, 그 후 결과 콜백이 microTask queue에 등록된다. 그리고 실행되고 있던 블록의 로직이 계속 실행된다. 블록의 로직이 모두 수행되면, 렌더링 요소라든지 등등 반영될 사항들은 모두 반영이 되고, call stack이 비워진다. 화면 렌더링 변경 요소가 있으면 렌더링 콜백에서 이를 먼저 수행하여 화면에 반영하고, microTask queue로 넘어가서 등록된 콜백을 콜 스택으로 옮겨 수행하게 된다. 이 콜백을 수행하는 중에 렌더링 요소가 또 있으면 이것도 콜 스택이 모두 비워지게 되는 동시에 이벤트 루프가 화면 렌더링 콜백 쪽으로 가서 painting을 해준다. 그 후 microTask queue -> macroTask queue -> call stack을 빙글빙글 도는 식임.

```javascript
$('.btn').click(function() {
    showWaitingMessage();
    longTakingProcess();
    hideWaitingMessage();
    showResult();
});
```

그럼 버튼을 클릭하면 showWaitingMessage()가 실행되고, 이는 렌더링 큐에서 반영되기만을 기다리고 있음. scripting language는 절차적이기 때문에 반영요소는 일단 보류하고, longTakingProcess -> hideWaitingMessage -> showResult 가 쭉 실행된다. 콜 스택이 비워진 후 렌더링 큐에 들어가서 콜백을 실행하려고 보니 이미 렌더링 요소가 show -> hide로 변경된 상태가 된다.

```javascript
 function onclickItemGroup(grpItemCd) {
        document.marketpriceForm.method = "GET"; // 1
        $("#grpItemCd").val(grpItemCd); // 2
        $("#ajaxPage").html("");
        
        // 애니메이션 렌더링 요소가 있다고 가정 // 3 (렌더링 큐에 등록된다) --> 콜스택이 비워진 후 이벤트 루프가 가장 먼저 반영함(렌더링 콜백)
        // 로딩 애니메이션이라면 로딩이 이루어짐
        
        // ajax 요청 (http response가 서버로 전달) 4 (Microtask 큐에 등록된다) --> 그 다음 비동기 통신이 이루어진다
        $.ajax({
            type: "POST",
            url: "<c:url value='/pblc/mrktprc/pblcmrktprc_ajax.do?lang=${param.lang}' />",
            data: $("#marketpriceForm").serialize(),
            dataType: "html",
            success: function (json) {
                $("#ajaxPage").html(json);
            },
            error: function (xhr, status, error) {
                alert(error);
                $("#areaIdValidYn").val("N");
                $("#areaIdValidYnResultMsgSpan").html("Not Valid");
            },
            complete: function () {
            }
        });
         
        // 5 onClick 이벤트로 인해 macroTask queue에 콜백이 등록된다. 
        // --> 마지막으로 eventLoop가 macroTask queue에서 이벤트 콜백을 하나 꺼내와 실행시킨다.
        <c:if test="${sessionScope.sessionVO.sessionRole eq 'ROLE002' && sessionScope.sessionVO.sessionExploitApproveCnt > 0}">
        onclickItemGroup_exploitservice(grpItemCd); 
        </c:if>
    }
```
