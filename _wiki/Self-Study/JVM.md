JVM은 Java Application을 Class Loader를 통해 읽어 들여 자바 API와 함께 실행하는 것이다. Java와 OS사이에서 중개자 역할을 하여 Java API가 OS에 종속받지 않고 재사용 될 수 있도록 한다. JVM의 중요한 역할 중 또 하나는 메모리 관리(Garbage Collection)이다. JVM은 Stack 기반의 가상머신(물리적 머신과 유사한 머신을 소프트웨어로 구현한 것)이다. ARM 아키텍처같은 하드웨어는 register 기반으로 동작하는 데 비해, JVM은 stack 기반으로 동작한다. 

프로그램이 실행된다.
JVM은 OS로부터 이 프로그램이 필요로 하는 메모리를 할당받는다. 
자바 컴파일러(javac)가 자바 소스코드(.java)를 읽어들여 자바 바이트코드(.class)로 변환시킨다.
Class Loader를 통해 class 파일들을 JVM으로 로딩한다. 
로딩된 class파일들을 Execution engine을 통해 해석한다(JVM).
해석된 바이트코드는 Runtime Data Areas에 배치되어 실질적인 수행이 이루어진다.
이러한 실행 과정 속에서 JVM은 필요에 따라 Thread Synchronization과 같은 Garbage Collection 같은 관리 작업을 수행한다.

JVM은 Class Loader와 Execution engine으로 구성되어 있다. 
1. Class Loader
JVM 내로 .class파일을 로드(Loading)하고, 링크(Linking)을 통해 배치하는 작업을 한다. Runtime시에 동적으로 클래스를 Load한다. jar파일 내 저장된 클래스들을 JVM위에 Load하고 사용하지 않는 클래스들은 메모리에서 삭제한다. 자바는 컴파일 타임이 아니라 런타임에 참조한다. 즉 클래스를 처음으로 참조할 때, 해당 클래스를 Load하고 Link한다. 이 역할을 Class Loader가 수행한다.

2. Execution Engine(실행 엔진)
Class를 실행시키는 역할이다. Class Loader가 JVM 내의 Runtime Data Areas에 바이트 코드를 배치시키고, 이게 Execution Engine에 의해 실행된다. 자바 바이트코드는 기계보다는 인간이 보기 편한 형태로 기술된 것이다. 그래서 Execution Engine은 이와 같은 바이트코드를 실제로 JVM 내부에서 기계가 읽을 수 있는 형태로 변환한다. 이 때 두 가지 방법을 이용하는데,
    a. Interpreter
    Execution Engine은 자바 바이트 코드를 명령어 단위로 읽어서 실행한다. 이는 인터프리터 언어의 단점을 갖고 있음(한 줄 씩 수행하기 때문에 느리다).
    b. JIT(Just - In - Time)
    인터프리터 방식의 단점을 보완하기 위해 나온 컴파일러(바이트코드 -> 기계어 컴파일러). 인터프리터 방식으로 진행하다가, 적절한 시점에 바이트코드 전체를 컴파일하여 네이티브 코드로 변경하고, 이후에는 더 이상 인터프리팅하지 않고 네이티브 코드로 직접 실행하는 방법이다. 네이티브 코드는 cache에 보관하기 때문에 한번 컴파일된 코드는 빠르게 수행된다. 물론 JIT 컴파일러가 컴파일하는 과정은 바이트코드를 인터프리팅하는 것보다 오래걸리므로, 한 번만 실행되는 코드라면 컴파일 하지 않고 인터프리팅하는 것이 유리하다. 따라서 JIT컴파일러를 사용하는 JVM은 내부적으로 해당 메서드가 얼마나 자주 수행되는지 체크하고, 일정 정도를 넘을 때에만 '컴파일'을 수행한다.
    
3. Garbage collector(Runtime Data Area)
Garbage Collection을 수행하는 모듈(thread)이 있다: Runtime Data Area
이는 프로그램을 수행하기 위해 OS에서 할당받은 메모리 공간이다. 
    a. PC Register(Program Counter Register)
    Thread가 시작될 때 '생성'된다. 각 Thread마다 하나씩 존재한다. Thread가 어떤 부분을 명령으로 실행해야 할지에 대한 기록을 하는 부분으로, 현재 수행 중인 JVM 명령의 주소를 갖는다.
    b. JVM Stack Area
    프로그램 실행 과정에서 임시로 할당되었다가 메소드를 빠져나가면 바로 소멸되는 특성의 데이터를 저장하기 위한 영역이다. 각종 형태의 변수나 임시 데이터, thread나 method의 정보를 저장한다. method 호출 시마다 각각의 stack 프레임(그 method를 위한 공간)이 생성된다. method 수행이 끝나면 프레임 별로 삭제된다. method 안에서 사용되는 값들을 저장한다. 또 호출된 method의 매개변수, 지역변수, 리턴 값 및 연산 시 일어나는 값 등을 임시로 저장한다.
    c. Native Method Stack
    자바 프로그램이 컴파일되어 생성되는 바이트 코드가 아니라 실제 실행할 수 있는 기계어!!로 작성된 프로그램을 실행시키는 영역. Java Native Interface를 통해 바이트 코드로 전환하여 저장하게 된다. (이 부분은 솔직히 뭔 소린지 잘 모르겠다) 이 영역에서 C code를 실행시켜 커널에 접근할 수 있다고 한다... 일반 프로그램처럼 커널이 스택을 잡아 독자적으로 프로그램을 실행시킨다(?). Java만 사용하여 필요한 기능과 성능을 모두 만족시키기는 힘들다. 그래서 C나 C++언어로 작성된 프로그램을 Java에서 사용할 수 있도록 JDK에서 제공한다. 이것을 가능하게 해주는 툴 킷에는 Android NDK가 있다. 
    The Android NDK is a toolset that lets you implement parts of your app using native-code languages such as C and C++. For certain types of apps, this can help you reuse code libraries written in those languages.
    - Android NDK를 사용하여 얻을 수 있는 장점
        - 기존에 C로 만들어진 대규모 코드를 Java에서 다시 만들 필요없이 재사용이 가능하다.
        - 시스템 디바이스 접근과 Java의 성능을 넘어선 작업이 필요할 때 유용하다. 
        - 속도 및 성능을 향상 시킬 수 있다. 
        - 주로 영상처리, 게임, 신호처리, 물리 시뮬레이션에 사
    d. Method Area(Static area)
    Class 정보를 처음 메모리 공간에 올릴 때 초기화되는 대상을 저장하기 위한 메모리 공간. Java 프로그램은 main의 호출에서부터 계속된 method의 호출로 흐름을 이어나간다. 그래서 올라가게 되는 method의 바이트코드는 프로그램의 흐름을 구성하는 바이트코드가 된다. 대부분 인스턴스의 생성도 method 내에서 명령하고 호출한다. 사실상 컴파일된 바이트코드의 대부분이 method 바이트코드이기 때문에 거의 모든 바이트코드가 올라간다고 봐도 상관없다. 이 공간에 Runtime Constant Pool이라는 별도의 관리 영역이 존재한다. 상수 자료형을 저장하여 참조하고, 중복을 막는 역할을 수행한다.
    
    # Runtime Constant Pool에 올라가는 정보의 종류
        - Field Information: Class member 변수의 이름, 데이터 타입, 접근제어자 정보
        - Method Information: method의 이름, 리턴 타입, 매개 변수, 접근 제어자에 대한 정보
        - Type Information: Class인지 Interface인지의 여부 저장/ 타입의 속성, 전체 이름, super class의 전체 이름
    
    Method Area가 클래스 데이터를 위한 공간이라면 Heap 영역은 객체를 위한 공간이다. Heap과 마찬가지로 GC 관리 대상에 포함된다.
    
    e. Heap 영역
    객체를 저장하는 가상 메모리 공간이다. new 연산자로 생성된 객체와 배열을 저장한다. Heap은 세 부분으로 나뉜다.
        - Permanent Generation
        생성된 객체들의 정보의 '주소값'이 저장된 공간이다. Class Loader에 의해 Load되는 Class, Method 등에 대한 Meta 정보가 저장되는 영역이다. 동적으로 클래스가 로딩되는 경우에 사용된다. 
        - New/Young 영역
            * Eden : 객체들이 '최초'로 생성되는 공간
            * Survivor 0/1: Eden에서 참조되는 객체들이 저장되는 공간(?)
        - Old 영역
        New area에서 일정 시간 참조되고 있는, 살아남은 객체들이 저장되는 공간 Eden영역에 객체가 가득차게 되면 첫번째 GC(minor CG)가 발생한다. Eden영역에 있는 값들을 Survivor 1 영역에 복사하고, 이 영역을 제외한 나머지 영역의 객체를 삭제한다.
        인스턴스는 소멸 방법과 소멸 시점이 지역 변수(stack)와 다르기에 Heap이라는 별도 영역에 할당된다. 자바 가상머신은 매우 합리적으로 인스턴스를 소멸시킨다. 더 이상 인스턴스의 존재 이유가 없을 때 소멸시킨다. 
        
javac -> java파일을 .class 바이트 코드 파일로 변환해서 
jvm의 class loader를 이용하여 jvm으로 class 파일들을 load한다. 
이는 jvm 내 execution engine 에서 interpreter/ jit(just-in-time) 컴파일러로 (사람이 읽기 쉬운) 바이트코드를 기계어로 변환한다.
jvm의 runtime data area에 class가 loading되면 실시간으로 class가 호출될 때마다 runtime으로 기계어로 변환이 된다. 컴파일 타임이 아니라 런타임으로 이루어지는 것이다. 이 Runtime Data Area에 멀티 스레드 환경에서의 각 스레드의 PC Register, Stack, Native Method Stack과/ Heap영역/ Method Area(Static Data 영역)등의 요소가 있다.  
