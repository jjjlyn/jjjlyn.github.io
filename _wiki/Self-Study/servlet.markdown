# Servlet & JSPs

HttpRequest가 들어오면, servlet을 instantiate하거나 Request를 처리할 `스레드`를 확보(할당)해놔야 한다. 
- call the servlet's doPost(), doGet() method
- get the request and the response to the servlet
- manage `생명주기` of the servlet
When a request A, servlet's job is to take a client's request and send back a response. 
만약 Request가 중요한 정보를 들고 있다면 서블릿 코드는 이를 찾는 방법, 이용하는 방법을 들고 있어야 한다. 
그리고 Response를 보내는 방법을 들고 있어야 함.
A Servlet's REAL job is to handle GET and POST requests.
* 멱등성: GET처럼 같은 자원을 계속 요청해도 결과가 같음. 그러나 POST의 경우 새로운 데이터가 생성되는 것이기 때문에 멱등이 아니다.
GET, PUT, DELETE는 멱등성이 있다.

writes MyJSP.`jsp` is translated to MyJSP_jsp.`java` -> compiles to MyJSP_jsp.`class` -> loaded and initialized -> Servlet object MyJSP_jsp `Servlet`

- 웹 클라이언트(모질라, 사파리 등의 브라우저)
- 웹 서버(아파치 등)
connected through wires and wireless networks

-> 웹 서버가 하는 역할
아파치 등의 웹 서버는 클라이언트 요청을 가져와서 그에 따른 응답(자원)을 클라이언트에 되돌려주는 역할을 한다.
이 때 자원은 HTML 페이지, 이미지, 음원 등의 파일이 될 수 있다.
서버에는 physical machine(hardware)과 web server application(software)가 있다...

-> 웹 클라이언트가 하는 역할
모질라, 크롬 등의 브라우저는 서버와 communicate하는 방법을 알고 있다. 웹 클라이언트는 사용자가 서버에 특정 자원을 요청할 수 있도록 하며, 사용자에게 요청에 대한 결과를 보여주는 역할을 한다. 브라우저의 역할 중 가장 큰 부분을 차지하는 것은 `HTML 해석 후 사용자에게 웹 페이지를 보여줄 수 있도록 rendering`하는 것.

-> 클라이언트와 서버가 소통하는 방식
그렇다면 클라이언트와 서버는 어떻게 정보를 주고받는 것인가? They must share a common language. 웹 상에서 클라이언트와 서버는 반드시 HTTP 프로토콜을 사용하여 소통해야 하며, 브라우저는 HTML을 알고 있어야 한다. (HTTPRequest <-> HTTPResponse) 가끔씩 구식 브라우저는 신버전 HTML interpretation에서 난관을 겪는다.
- HTML은 브라우저가 어떤 식으로 웹 페이지에 컨텐츠를 배치해야 하는지 알려준다.
- HTTP는 클라이언트와 서버가 웹에서 소통하는 프로토콜(통신 규약)이다.
- 서버는 HTTP를 이용하여 HTML을 클라이언트에 전달한다.

[HTTP 프로토콜이란 무엇인가]
HTTP runs on top of TCP/IP. If you're not familiar with those networking protocols, here's the crash course: TCP is responsible for making sure that a file sent from one network node to another ends up as a complete file at the destination, even though the file is split into chunks when it is sent. IP is the underlying protocol that moves/routes the chunks (packets) from one host to another on their way to the destination. 
내가 주소창에 www.naver.com을 쳤을 때, DNS 서버에서 해당 도메인 이름(naver.com)에 매핑되는 IP 주소를 먼저 찾고, IP 주소를 얻어온 이후 이를 깔고 TCP/IP Connection을 거친 다음에 그 위에서 HTTPRequest/Response를 주고받는 것임. TCP 통신 규약은 네트워크 노드에서 그 다음 네트워크 노드로까지의 `파일 전송을 보장`하며, IP는 노드 간 전송(노드가 그 다음 노드를 찾아가야 할 때) 시 필요한 호스트 주소(?)라고 볼 수 있다.
클라이언트와 서버는 TCP/IP 통신을 깔고 HTTP 프로토콜(언어)로 커뮤니케이션하며, Server는 주로 HTML파일을 브라우저에 넘겨준다. 이를 해석하여 웹 페이지에 `렌더링` 하는 것이 클라이언트(브라우저)의 역할.
 
 * HTTPResponse can contain HTML
HTTP는 어떤 컨텐츠를 응답으로 가져오든 맨 위에 항상 header를 붙인다. <html> 태그를 브라우저가 찾으면, 그때부터 rendering mode로 들어간다. `브라우저가 <image> 태그를 찾으면 이는 또 다른 HTTPRequest를 생성한다.`

그렇다면 Request는 무엇일까?
HTTP method name -> tells the server the kind of request that's being made

POST is a powerful request that is like a GET plus plus. With POST, you can request something and at the same time send form data to the server. 
물론 GET으로도 소량의 데이터를 전송할 수 있따. 그러나 이 방식은 사용하지 않기를 권한다. 
-> The total amount of characters in a GET is really limited. Input box에 long passage를 넣으면 GET이 작동을 안할 수 있다.
-> GET은 내가 전송하려는 데이터를 URL에 append한다. 그렇기 때문에 내가 전송하려는 데이터가 모두 노출된다. '?'은 경로(path)와 parameters(the extra data)를 구분한다.
-> 이 두 가지 이유 때문에 사용자는 POST를 사용하면 form submission을 bookmark 할 수 없다(?). You may or may not want users to be able to bookmark the resulting request from a form submission. 

[HTTP GET Request의 골격]
path + parameters added to the URL -> request line (전체가 request line이 된다.)
[HTTP POST Request의 골격]
서버로 전송되는 데이터는 message body (혹은 payload)에 실리며, 용량이 클 수 있음.
[HTTP Response의 골격 -- MIME type은 무엇일까]
HTTP response에는 header, body가 모두 있다. Header 정보는 어떤 프로토콜이 사용되었는지에 대한 명세, 요청이 성공적이었는지 아닌지 여부, 어떤 종류의 컨텐츠가 Body에 포함되었는지 등을 갖고 있다. Body는 HTML 등의 컨텐츠를 싣고 있음.

HTTP/1.1 200OK
-> The protocol version that the web server is using
-> The HTTP status code for the Response

Set-Cookie
-> 쿠키 설정
Content-Type: text/html
-> MIME type
브라우저가 어떤 정보를 받게 될 지를 알려주며, 브라우저는 이를 화면 상에 어떤 식으로 그릴지 알게 된다.
이는 HTTPRequest의 "Accept" header에 나열된 values와 연관되어 있음. (Accept에 나열된 종류 중 하나가 text/html)

[URL]
Every resource on the web has its own unique address, in the URL format.
`http://www.wickedlysmart.com:80/beeradvice/select/beer1.html`
-> http://
Protocol: 서버에게 어떤 프로토콜을 사용하고 있는지를 알린다.

-> www.wickedlysmart.com
Server: The unique name of 물리 서버. 고유한 IP address와 매핑된다. 

-> 80
Port: A single server supports many ports. A `server application` is identified by a port. 80 is the default one.

-> /beeradvice/select/
Path: 요청된 자원이 위치한 경로(서버 내에서). Early 웹 서버들은 유닉스 위에서 돌았는데, 이 유닉스 문법(directory hierarchies)이 아직 남아있는 것임

-> beer1.html
Resource: 요청된 컨텐츠의 명칭. HTML 페이지, 서블릿, 이미지, PDF 등 서버가 전달할 수 있는 모든 종류의 데이터가 될 수 있다. 이 항목이 없으면 대부분의 서버는 index.html을 default로 찾는다.

인터넷 웹(HTTP) 서버 `소프트웨어`는 기본적으로 포트 80번에서 돌아간다. 
Using one server app per port, a server can have up to 65536 different server apps running. 포트는 서버 하드웨어 위에서 돌아가는 서버 어플리케이션에 대한 논리적인(물리적인게 아닌) 연결(?)이라고 볼 수 있다. 하나의 서버 어플리케이션(HTTP, HTTPS, POP3, FTP, Telnet, SMTP, Time 등)은 각각 하나의 포트를 이용한다. 포트는 0부터 65535까지 쓸 수 있고, 0부터 1023까지는 잘 알려진 서버 어플리케이션이 사용하고 있기 때문에(i.e. FTP, Telnet, SMTP...) custom web server application을 연결하고 싶으면 그 이후 포트를 사용해야 할 것(사실 난 이게 뭔 소린지를 아직 잘 모르겠다.) 10.10.5.2:8075은 무엇을 의미하는가? 
포트 번호가 없으면 서버는 클라이언트가 어떤 웹 어플리케이션을 이용하고 싶은지 알 턱이 없음. 그리고 각각의 서버 어플리케이션은 고유한 `프로토콜`을 가지고 있기 때문에, 포트 번호라는 식별자가 없으면 문제가 생긴다. 포트 번호를 설정할 때 어떤 프로토콜을 이용할 것인지도 설정해주나? 그렇다면 10.10.5.2:8075에서 10.10.5.2 호스트 서버의 8075번을 열어줬을 때 HTTP 프로토콜로 통신하도록 사전 세팅해 놨겠지. 그렇기 때문ㅁ에 8075번 포트가 어떤 프로토콜을 사용하는지 알 수 있는 것이다. HTTP프로토콜 대신 다른 프로토콜을 사용할 때 웹 브라우저에서는 접근 못함(웹 브라우저는 서버와 HTTP 프로토콜로만 통신하기 때문에 규약이 안 맞으면 아무것도 할 수 없다.)
예를 들어 웹 브라우저가 HTTP를 프로토콜로 사용하는 웹 서버 대신 110포트를 사용하는 POP3 메일 서버에 landed했다고 가정하자. 이 메일 서버는 HTTPRequest를 파싱하는 방법을 모를 것이다. 또한 POP3 서버는 응답으로 HTML 페이지를 리턴해주는 방법도 모른다. 
내가 만약에 웹 어플리케이션을 회사 네트워크에서 돌린다면, 시스템 관리자에 가서 어떤 포트 번호가 이미 사용되고 있는지를 파악해야 한다. 예를 들어 시스템 관리자는 3000 이하로는 포트 번호를 사용하지 말라고 알려줄 수 있다. 

[아파치 웹 사이트 디렉토리 구조]
- Apache_home
- **htdocs** : 아파치 서버는 htdocs라는 디렉토리를 서버 웹 어플리케이션의 root(최상단)으로 인식한다.
- `skiingAdvice` `beerAdvice` : two **applications** on this server
- beerAdvice > `select` `checkout` : the folders for the beerAdvice app's two actions

[웹 서버는 정적 웹 페이지를 제공한다]
정적 페이지는 just sits there in a directory. 아파치 서버가 디렉토리 상에 그대로 있는 정적 페이지를 찾아내 클라이언트에 제공한다. 모든 클라이언트는 같은 정적 페이지를 보게 된다.
Web server machine > Web server application > Do NOT ask me to do anything to the page. These pages go straight to the client just exactly as they were put on the server.
그런데 만약 동적 페이지를 보고 싶다면?
웹 서버 머신 안에는 웹 서버 어플리케이션(정적 페이지를 클라이언트에 제공)과 **또 다른 종류의 어플리케이션**이 있다. 
동적 페이지(just-in-time pages)는 Request가 있기 전에는 미리 존재하지 않는다. 데이터를 DB 혹은 File로부터 읽어들이거나 쓰는 작업은 웹 서버에 의존할 수 없음. 
`웹 서버는 정적 페이지만을 제공`한다. 그러나 웹 서버(아파치)와 소통하는 `helper application`은 동적인 페이지를 빌드할 수 있다. 동적인 페이지 빌드가 완료되면 웹 서버가 이를 받아서 클라이언트에 돌려준다.
폼 데이터를 제출(Submit)했을 때 이를 파일 혹은 DB에 저장하거나, 이를 받아서 Response 페이지로 만드는 것은 이 `helper application`의 도움을 필요로 한다. `helper application`이 웹 서버에 request를 하면, 웹 서버는 `helper application`이 필요로 하는 parameters를 넘겨주고, `helper application`은 Response를 만든다. 이를 다시 웹 서버에서 받아와(이미 정적 페이지로 바뀜) 클라이언트에 넘겨준다.

[CGI program VS Servlets]
자바에 국한되지 않은 용어를 사용하자면, Web Server Helper Application은 "CGI program" 이라고 통칭한다. 동적인 웹 페이지를 빌드해주는 기능을 말한다. 
(이 CGI 프로그램은 대부분 Perl 언어로 작성되었음)
하튼 자바 내에서 이 동적 페이지를 생성해주는 프로그램은 서블릿이다. 서블릿은 Java로 작성된다. Perl 언어로 작성된 CGI 프로그램은 서버에 부하를 발생시킨다고 한다. 
서블릿은 계속 loaded 된 채로 client requests for a Servlet resource are handled as `separate threads` of a single running Servlet. There's no overhead of starting the JVM, loading the class. JVM에서 돌아가며, 한 서블릿 내에서는 클라이언트의 요청 당 하나의 스레드가 생성된다. Thread per Request)
Servlet can be a J2EE client.
A Servlet running in a J2EE web container can participate in security and transactions right along with enterprise beans. (이건 뭔 소린지 잘 모르겠다.)

```java
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Ch1Servlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
                    throws IOException {
        
        PrintWriter out = response.getWriter();
        java.util.dDate today = new java.util.Date();
        // HTML embedded in a Java program.
        // This is how you create a dynamic web page in a servlet
        // You have to print the whole thing to an output stream
        // it's really part of the HTTPResponse stream that you're printing to
        out.println("<html>" + 
                "<body>" +
                "<h1 align=center>HF\'s Chapter1 Servlet</h1>" +
                "<br>" +
                today +
                "</body>" +
                "</html>");
    }
}
```

- One Deployment Descriptor (DD) per web application
- DD can declare `many servlets`
그런데 이런 식으로 서블릿의 out.println()에 HTML을 일일이 박아넣는 것은 성가신 일이다. (This is one of the worse parts of servlets)
에디터에서 HTML을 작성해서 println()에 한꺼번에 박아넣을 순 없나?
Java code를 HTML에 박아넣을 수도 있잖아(Instead of putting HTML inside a Java class) --> JSP!!

[정리]
- HTTP는 웹에서 사용하는 네트워크 프로토콜이며, TCP/IP 계층 위에서 돌아간다.
- HTTP는 Request/Response 모델을 사용한다.
- 서버 응답 메시지가 HTML 페이지라면, HTML은 HTTP응답에 박힌다.
- HTTPRequest는 Request URL, HTTP method(GET/POST), parameter data등으로 구성된다.
- HTTPResponse는 상태 코드, content-type(MIME type), content of response를 포함한다.
- Every resource on the web has its own unique address in this format. 
- 웹 서버는 정적 HTML 페이지만을 제공한다. 동적 페이지가 필요하다면 helper application이 필요하다. 이를 일반적으로 CGI라고 한다. 
- 서블릿의 out.println()에 HTML을 박아 넣는 것은 성가시다. 이를 JSP가 HTML에 자바 코드를 삽입하는 방식으로 해결한다.

[서블릿도 도움이 필요하다]
Client Request가 들어오면, 누군가가 서블릿을 initiate하고, 적어도 Request에 대한 스레드를 생성해야 한다. 또한 서블릿의 doPost()와 doGet() 메서드를 호출해야 한다. Servlet의 생명주기도 관리해야 하며, 서블릿에게 클라이언트의 Request를 전달해주고, 서블릿이 만든 응답을 꺼내가는 일도 해야 한다. 이를 `Web Container`라는 놈이 한다. 대표적으로는 `Tomcat`이 있다.
How your web application runs in the Container?
A first look at the structure of a web app using the MVC design pattern!

- For each of the HTTP methods(GET, POST, HEAD 등)은 HTTP 프로토콜의 역할과 기능을 설명한다.
HttpServlet method corresponds to the HTTP method.
- Event sequence of the servlet life cycle
    -> Class loading
    -> Instantation
    -> init method is called
    -> service method is called
    -> distroy method is called
- Construct the file and directory structure of a Web Application contains `static content`, `JSP pages`, `servlet classes`, `JAR files`, `Java class files`, `deployment descriptor(web.xml)`, `tag libraies`

서블릿에는 main() method가 없다. 그래서 Container라 불리는 또 다른 Java application의 통제 대상(?)이 된다. 
웹 서버(아파치)에 Servlet에 대한 Request가 들어왔을 때(<-> plain old static HTML page 요청), 서버는 서블릿 자체에 이 Request를 넘겨주는 게 아니라, 서블릿이 실행되는(올라가는) Container에 넘겨준다. 그 후 Container가 서블릿에 HTTP Request를 넘겨주는 것이다. 서블릿의 doGet(), doPost()를 호출하는 것은 바로 이 Container다.

[Container(i.e. Tomcat)의 역할]
- Container는 Web server와 Servlet의 소통을 용이하게 하는 중간 매개체
서버 소켓을 직접 만들 필요도 없고
포트에 귀 기울일 필요 없고
스트림을 만들 필요도 없다. 
Container는 Web server와 자신의 프로토콜을 알고 있으며, 서블릿은 아파치 웹 서버와 웹 애플리케이션의 API를 걱정할 필요가 없다. 
개발자는 서블릿 코드에서 돌아가는 비지니스 로직만 생각하면 된다.
- Container는 서블릿의 생명주기를 총괄한다. 
서블릿의 class loading, 생성, 초기화, 메서드 호출, 가비지 컬렉션 등을 주관. 그래서 서블릿의 생명주기도 걱정할 필요가 없다.
- Multithreading Support
Container는 서블릿에 도달하는 Request에 대한 자바 스레드를 자동으로 생성해 준다. 서블릿이 클라이언트 요청에 대한 HTTP Service method 실행을 완료하면 스레드는 자동으로 소멸된다. 이건 반드시 스레드 안정성을 보장하는 것은 아니지만, 뭐 암튼.
- Declarative Security
컨테이너와 함께 web.xml(Deployment descriptor)을 이용하여 Security 환경설정을 손쉽게 할 수 있다(서블릿 안에 직접 하드코딩하는 대신).
- JSP Support
Translating JSP code into real Java

[How the Container handles a request]
1. 사용자가 URL을 클릭 (link that has a URL to a servlet instead of a static page)
2. Tomcat Container가 클라이언트 요청을 '포착'하고, 이가 서블릿에 대한 요청이라는 것을 감지한다. Container은 두 가지 객체를 생성하는데 HttpServletResponse, HttpServletRequest가 그것이다.
3. Container는 요청 URL을 기반으로 correct 서블릿을 찾는다. 그 후 이 요청에 대한 스레드를 생성하고 서블릿에 할당한다. 이후 Request Object / Response Object (HttpServletRequest Object, HttpServletResponse Object)을 서블릿의 스레드에 넘겨준다. 
4. Container는 서블릿의 service() 메서드를 호출하고, 어떤 호출 타입이냐에 따라 service() 메서드는 doGet()이나 doPost() 메서드를 호출한다. 
5. doGet()을 실행한다고 가정하면, 이 doGet()이 HttpServletResponse 객체에 들어갈 동적 페이지와 각종 stuffs를 생성한다. Container는 아직까지 HttpServletResponse 객체에 대한 reference(주소값)을 들고 있다는 것을 기억할 것.
6. 스레드가 종료되면 Container는 HttpServletResponse 객체를 HTTP response로 바꾸고, 클라이언트에 보낸다. 그 후 두 객체를 제거.

[Container는 어떻게 서블릿을 찾는가]
Deployment Descriptor은 Container이 서블릿과 JSP를 어떻게 실행해야 하는지에 대해 명세해놓은 것이다. 
-> Mapping URLs to actual servlets
<servlet> tells the container which class files belong to a particular web application
    <servlet-name> The end-user NEVER sees this name
    <servlet-class> fully-qualified name of the class
</servlet>

<servlet-mapping> what the container uses at runtime when a request comes in, to ask, "which servlet should I invoke for this requested URL?"
    <servlet-name>
    <url-pattern> This is what the client sees to get to the servlet. But it's a made-up name that is NOT the name of the actual servlet class. Possible to use wildcards in this element.
</servlet-mapping>

-> Also can include security roles, error pages, tag libraries, inital configuration information in DD

With MVC, the business logic is not only separate from the presentation, it doesn't even know that there IS a presentation.
MVC의 본질은 비지니스 로직과 프레젠테이션을 분리하는 것이긴 하지만, 이 둘 사이에 중개자(?)를 끼워넣어 비지니스 로직이 뷰의 존재를 모르도록 해야 한다. 이로써 비지니스 로직이 재활용 가능한 독립적인 자바 클래스가 된다.
그러므로 서블릿에 비지니스 로직을 한 무더기로 짜놓고, out.println()으로 서블릿 내에서 HTML 코드를 작성하지 않는 대신 JSP를 포워딩하는 것은 비지니스 로직과 뷰를 분리한 작업이라고 볼 수 있긴 하지만, 여전히 서블릿이 뷰의 존재를 알고 있기 때문에(뷰에 대한 커넥션이 온전히 드러나기 때문) 진정한 MVC라고 보기에는 어렵다.

- Controller 컴포넌트
Takes user input from the request(서블릿으로 들어온 리퀘스트) and figures out what it means to the model. 
Tells the model to update itself, and makes the new model state available for the view(the JSP).
User가 입력한 정보를 Request로부터 꺼내서 이게 Model에게 의미하는 바가 무엇인지 알아낸다. 모델에 이 정보를 업데이트하라고 알려주는 동시에, 이 새로운 모델 상태를 뷰가 새로 업데이트 할 수 있도록 한다. 그래서 Controller에 구현된 메소드가 받는 파라미터에 HttpServeletRequest가 있었던 거야...ㅜㅜ 서블릿으로부터 요청 정보를 받아오려고. 나는 이 사실을 왜 이제야 알았을까????
- View 컴포넌트
Responsible for the presentation. It gets the `state of the model` from the controller. It's also the part that gets the user input that goes back to the Controller.
- Model
DB 접근 컴포넌트
Holds the real `business logic` and the `state`. 
It knows the rules for getting and updating the state.
It's the only part of the system that talks to the DB!

1. The client makes a request for the form.html page
2. The Container retrieves the form.html page
3. The Container returns the page to the browser, where the user answers, the questions on the form...
4. The browser sends the request data to the container
5. The Container finds the correct servlet based on the URL, and passes the request to the servlet
6. The servlet calls the BeerExpert for help
7. The expert class returns an answer, which the servlet adds to the request object
8. The servlet forwards the request to the JSP
9. The JSP gets the answer from the request object
10. The JSP generates a page for the Container
11. The Container returns the page to the user

[개발 환경 설정]
- MyProjects > beerV1 > etc, lib, src, classes, web
    - etc > web.xml
    - lib : put `3rd party` JAR files
    - src : All of your Java code lives under the src directory
        - src > com > example > web, model 
            Separate the `controller` components from the `model` components
    - classes > com > example
        derived when you compile your Java classes
    - web : Your `static` and `dynamic` view components go here

Deploying a web app involves both Container-specific rules(Tomcat rules) and requirements of the Servlets and JSP specifications.
- tomcat > webapps > Beer-v1 : This directory name also represents the "context root" which Tomcat uses when resolving URLs.
아래 디렉토리 구조는 Container vendor에 관계없이 모두 동일하다.
- WEB-INF > classes, lib, web.xml
    - classes > com > example > web, model

[The HTML for the initial form page]
```html
<form method = "POST"
    action = "SelectBeer.do">
```
HTML은 서블릿을 SelectBeer.do로 칭하는 줄 안다. (논리적 이름)
SelectBeer.do는 실제 파일 명은 아니다. 클라이언트는 이 이름을 URL에 붙여 Request를 보내는 것. 실제로 클라이언트는 서블릿 클래스 파일에 직접 접근하는 것이 아니라, HTML 페이지를 만들어서 HTML 페이지 내의 링크나 action을 타고 서블릿 파일에 접근하게 된다.
여기서 `web.xml`을 사용하여 클라이언트 요청 URL에 적힌 논리적 이름의 서블릿 클래스가 실제로 어떤 클래스인지 매핑을 한다.
그런데 `.do`는 뭐임? Just a simply part of the logical name...

[Deploying and testing the opening page]
Use Tomcat as both the web Server and the web Container
혹은 Apache + configured with a Web Container(Tomcat)

`http`://`10.10.5.2:8075`/`pblc`/`news/list.do`
- Host: 10.10.5.2:8075
- The web app context root: pblc
- The logical resource name: news/list.do
HTML에서는 웹 앱의 이름을 붙이지 않는다. 브라우저에서 URL을 요청할 때 붙여줌. 
Tomcat을 restart하지 않고서는 DD가 deploy되지 않는다.
In MVC, the model tends to be the "back-end" of the application.
In most cases it's just plain old Java code, with no knowledge of the fact that
it might be called by servlets.
The model shouldn't be tied down to being used by only a single web app,
so it should be in its own utility packages.