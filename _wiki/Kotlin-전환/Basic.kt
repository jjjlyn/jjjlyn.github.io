fun main(args: Array<String>){
    val question = ""
    println("question: $question")
    

    val answer3: Int
    answer3 = 50
    // 초기화를 하지 않고, 변수를 생성하고 싶다면 타입을 명시해줘야 한다. 
    val answer2: Int = 50
    println("answer2: " + $answer2)

타입을 생략하면 코틀린 컴파일러의 Type Inference를 통해 알아서 타입을 지정해준다. 프로그래머가 직접 타입을 명시해줘도 상관없다. 4번째 줄처럼 타입을 생략하거나 9번째 줄처럼 Int라고 직접 타입을 선언해도 된다. 12번째 줄을 살펴보면, 컴파일러는 7.5e6을 보고 해당 변수(yearsToCompute)를 Double타입으로 지정한다. 

만약 16번째 줄처럼 초기화를 하지 않고 변수를 생성하고 싶다면 그 뒤에 반드시 타입을 명시해줘야 한다. 그리고 밑에서 해당 변수를 초기화해준다.         

val과 var을 사용하여 생성할 수 있다. 이 둘의 차이점은 mutable하느냐의 여부로 나뉜다.  
fun main(args: Array<String>){

모든 변수를 val로 선언하고 나중에 꼭 필요할 때만 var을 사용하는 것을 추천한다. 
val의 특징1. 
val은 한번만 초기화되는 것이 맞다. 컴파일러가 문맥을 살펴봤을 때 한번만 초기화되는 것이 맞다면 상황에 따라 val을 여러 값으로 초기화 할 수 있다.

fun main(args: Array<String>){
    val msg:String
    if(isSuccess()){
        msg = "Success"
    }else{
        msg = "Fail"
    }
    println("msg :" + $msg)
}

val은 변경 불가능한 것이 맞다. 하지만 참조가 가리키는 객체의 내부 값은 변경이 가능하다. 예를 들어 

fun main(args: Array<String>){
    val myArray = arrayListOf("Java")
    myArray.add("Python")
}

3번째 줄을 살펴보면 리스트의 경우 참조가 가리키는 객체의 내부 값은 변경이 가능하다는 사실을 알 수 있다. 

var의 특징
var은 값을 변경할 수 있다. 하지만 다른 타입의 값을 넣을 수는 없다. 처음에는 Int타입으로 값을 초기화 했으면, 그 다음에는 계속 Int 타입으로만 변경이 가능함. 만약 String으로 재정의하면 Type mismatch라는 error을 확인할 수 있을 것이다. 

fun main(args: Array<String>){
    var answer = 45 // Int형으로 초기화하였음
    answer = "no answer" // ==> Error: Type mismatch
   
굳이 타입을 바꾸고 싶다면 강제 형변환을 통해 가능(coerse)

@Getter
class Address{
    @SerializedName("province")
    val province: Province? = null 
}

null 변수
- 코틀린 언어는 null을 대입할 수 있는 변수와 대입할 수 없는 변수로 구분된다.
- null을 저장할 수 없는 변수에 null을 저장하면 "Null can not be a value of non-null type String"이라는 에러가 발생한다. 

? (nullable type)
null을 저장할 수 있는 자료형 타입을 의미한다. 
변수 선언시 자료형 타입 오른쪽에 ?기호를 써서 null값을 가질 수 있는 nullable type임을 표시해 준다.

null을 저장할 수 있는 자료형 타입
변수 선언시 자료형타입 오른쪽에 ?기호를 써서 null값을 가질 수 있는 nullable type임을 표시해준다. 널러블 타입을 사용할 경우 npe가 발생할 수 있음에 유의해야 한다. 

var data1:String = "Kotlin"
var data2:String? = "Kotlin"

nullable type 문자열 변수
null을 대입할 수 있는 변수
문자열 또는 null을 저장할 수 있음

변수에 null값을 대입하려면 타입에 ?기호를 이용하여 명시적으로 null이 될 수 있는 변수로 선언해야 한다. 
val | var 변수명:타입? = null
null을 저장할 수 없는 변수는 
val nonNullData1: String = null (x)
val nonNullData2: String = null (x)

null을 저장할 수 있는 변수
val nullableData1: String? = null (o)
val nullableData2: String? = null (o)
    
null 가능성
기본적으로 객체를 불변으로 보고 null값을 허용하지 않는다.
null값을 허용하려면 별도의 연산자가 필요하고 null을 허용한 자료형을 사용할 때도 별도의 연산자들을 사용하여 안전하게 호출해야 한다. 
코틀린에서 기본적으로 null값을 허용하지 않는다. 따라서 모든 객체는 생성과 동시에 값을 대입하여 초기화해야 한다. 

코틀린에서 null값을 허용하려면 자료형의 오른쪽에 ?기호를 붙여주면 된다.

lateinit 키워드를 사용한 늦은 초기화
안드로이드를 개발할 때는 초기화를 나중에 할 경우가 있다. 이때는 lateinit 키워드를 변수 선언 앞에 추가하면 된다. 안드로이드에서는 특정 타이밍에 객체를 초기화할 때 사용한다. 초기화를 잊는다면 잘못된 null값을 참조하여 앱이 종료될 수 있으니 주의해야 한다. 

lateinit var a:String

a = "Hello"
println(a)

lateinit은 다음 조건에서만 사용할 수 있다.
1) var 변수에서만 사용 가능하다.
2) null 값으로 초기화 할 수 없다.
3) 초기화 전에는 변수를 사용할 수 없다.
4) Int, Long, Double, Float 에는 사용할 수 없다. 

lazy는 값을 변경할 수 없는 val을 사용할 수 있다. 
val 선언 뒤에 by lazy 라는 블록에 초기화에 필요한 코드를 작성한다. 마지막 줄에는 초기화 할 값을 작성한다. str이 처음 호출될 때 초기화 블록의 코드가 실행된다. println() 메소드로 두번 호출하면 처음에만 "초기화"가 출력된다. 

val str:String by lazy{
    println("초기화")
    "hello"
}

println(str)
println(str)

lazy로 늦은 초기화를 하면 앱이 시작될 때 연산을 분산시킬 수 있어 빠른 실행에 도움이 된다. 

by lazy 는 val 에서만 사용할 수 있고 조건이 적기 때문에 lateinit 보다 편하게 사용할 수 있다. 

코틀린은 null 값을 가질 수 있는 변수를 타입? 로 선언한다. 이 변수는 null check 가 필요하다. 변수명!! 로 해당 변수가 null이 아니라고 명시할 수도 있다. 당연한 얘기지만 non-null로 선언된 변수는 nullable 변수를 맏아올 수 없다. 

@NonNull
var name:String = "yoon"

@Nullable
fun getMyName(name: String): String?{
    return name
}

@NonNull 
val mmMyname: String = getMyName(name)

val mmMyName2: String = (getMyName(name))!!

Kotlin의 기본 클래스를 정리한다. 
Kotlin은 별도의 클래스를 정의하지 않고 Util을 생성하기도 한다. 
이러한 방법 및 상속시에 사용 가능한 추상 클래스 등을 살펴보도록 하자. 

코틀린으로 클래스를 생성하고, 코틀린에서만 사용하게 되면 default 변수 정의가 가능하기 때문에 별도의 overloading을 정의하지 않아도 된다. 

fun setItems(val name: String, val age: Int = 0) {}

class ClassName {
    public ClassName(String name){
    }
}

위와 같이 별도의 함수로 생성자를 선언한다. 
코틀린에서는 다음과 같이 클래스 이름 끝에 ()을 포함하여 생성자를 선언해주게 된다. 
class ClassName(name: String){
}

클래스 생성자의 원형은 다음과 같다. 

class ClassName constructor(name: String){
}

원래는 constructor 가 생성자를 나타낸다. constructor 를 매번 써주는 것도 코드량만 늘어나고 불필요한 사항이라서 생략이 가능한 형태로 제공을 하기 때문에 constructor은 생략할 수 있는 형태로 제공되며, 간단하게 class ClassName(name: String)의 형태로 선언이 가능하다. 

생성자 초기화
Java에서는 생성자에서 많은 일을 할 수 있다. 
다음과 같이 초기화를 하기도 한다.

class ClassName{
    private int[] age;
    public ClassName(){
        age = new int[10];
    }
}

코틀린의 constructor에서는 이러한 행동을 할 수 없기 때문에 init이라는 블럭이 별도로 제공된다. init 에서 다음과 같이 초기화를 해줄 수 있다. 

class ClassName(name: String){
    init{
        println("Initialized with value ${name}")
    }
}

init 블록을 사용하지 않고, 다음과 같이 upperName이라는 String변수에 생성자에서 넘겨받은 name을 toUpperCase하여 바로 대문자로 초기화할 수도 있다. 

class ClassName(name:String){
    val upperName = name.toUpperCase()
}

별도의 생성자 정의 없이 위와 같이 바로 초기화 가능한 형태로 사용할 수 있다. 추가로 val은 읽기 전용이고, var은 읽기 쓰기가 가능한 형태. 생성자에 val을 정의했다면 읽기만 가능하고 var로 정의했다면 읽기 쓰기가 가능한 형태다. Java에서는 final이다. 다음과 같은 형태로 생성자 정의가 가능하다.

class Person(val name, var age:Int){
}

1개 이상의 생성자
클래스 이름 선언과 동시에 생성자를 선언하는 코틀린에서도 1개 이상의 생성자 정의가 가능하다.

class Person{
    public Person(String name){
    }

    public Person(String name, int age){
        this(name);
    }
}

Kotlin에서는 constructor을 여러 개로 정의할 수 있는데 다음과 같은 형태로 생성자 정의가 가능하다. 

class Person(val name: String){
    constructor(name: String, age: Int) : this(name){
    }
}

첫 번째 생성자는 val name: String 만 초기화 할 수 있고 2번째는 name:String, age:Int를 가지는 생성자다. name은 중복적으로 사용되므로 기존 생성자로 넘겨주기 위해서 this(name)키워드를 사용하여 정의할 수 있다. 

class Person(val name: String){
    constructor(name: String, age: Int): this(name){
    }
}

생성자 private으로 사용하기
Java에서는 싱글톤을 사용하는 경우 private을 사용하여 생성자를 가린다. Kotlin에서도 private의 생성자를 만들 수 있는데 다음과 같다.

class PrivateConstructor private constructor(){
}

class ClassName {
}

ClassName className = new ClassName();

코틀린에서는 new를 쓰지 않고도 class사용이 가능하다. 
아래와 같이 해당 클래스의 생성자만 정의하면 바로 사용 가능하다.

class ClassName {
}
 val className = ClassName()

 public abstract class Base{
     public Base(int age){
     }
 }

 public class UseBase extends Base {
     public UseBase(int age){
         super(age);
     }
 }

 코틀린에서는 abstract와 interface에 대한 별도의 구분 없이 :로 구분함
open class Base(age: Int)
// open으로 생성한 추상 클래스를 다음과 같이 사용한다.

class UseBase(age: Int) : Base(age)

간단하게 위와 같이 :로 구분하여 상속을 구현하게 된다. 
Android에서 가장 많이 사용할 View 상속은 다음과 같이 처리할 수 있다. 

class MyView : View {
    constructor(ctx: Context) : super(ctx){
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs){
    }
}

함수 Overriding
함수를 Overriding 하기 위해서 java에서는 abstract 또는 interface 를 사용한다. abstract 는 extends 를 사용하여 상속을 구현하고, interface는 implements를 이용하여 상속을 구현한다. 

abstract class AbstractBase{
    abstract void onCreate();

    final void onResume(){
    }

    interface Base{
        void onStart();
    }

    public Use extends AbstractBase implements Base {

        @Override
        void onCreate(){}

        @Override
        void onStart(){}
    }
}

위와 같이 상속을 구현한다. final에 대해서는 아래쪽에서 설명하고 우선 코틀린에서는 interface와 abstract를 :로 처리한다고 하였다.

다음과 같이 open이라는 키워드를 함께 사용하면 다음과 같다. 
interface BaseItem{
    fun onStart()
}

open class Base{
    open fun v(){}
    fun nv() {}
}

class AbstractBase() : Base(), BaseItem{
    // Base의 open 키워드를 통해 상속을 구현
    override fun v(){}
    override fun onStart(){}
}

kotlin에서 사용하게 되는 open 키워드는 다음과 같다. 
java에서는 상속의 재정의를 방지하기 위해 final을 사용한다. 
kotlin에서는 반대로 상속의 재정의를 허용하기 위해 open을 사용한다. 
java에서는 final을 통해서 상속의 재정의를 막지만 kotlin에서는 open을 이용하여 함수의 재정의를 할 수 있도록 허용한다. 
open 클래스에 open 함수가 있다면, 이는 상속을 받아 재정의가 가능한 형태가 되는 것. 그래서 다음과 같이 open class를 구현하게 되면 v()는 재정의가 가능하고 nv()는 재정의가 불가능한 형태가 만들어진다. 

open class Base{
    open fun v(){
        print("ABC")
    }
    fun nv(){}
}

open은 변수에서도 사용가능한데 다음과 같다. 
open class Foo{
    open val x: Int get{}

    class Bar(override val x: Int) : Foo() {
    }
}

위의 코드를 예제로 작성하면 다음과 같고 실행하면 12의 결과를 얻을 수 있다.
fun main(args: Array<String>){
    print(C(12).x)
}

open class A {
    open val x: Int = 0
}

// final로 지정된 것으로 x = 0으로 초기화되어 고정
// 그런데 재정의가 가능한 것 아닌가?

class C(override val x: Int) : A(){
}
// parameter: override val x: Int => open 변수인 x를 재정의
// A class를 extends하기 때문에 저렇게 표현됨

open class A{
    open fun f(){print("A")} // 재정의 가능
    fun a(){print("a")}
}

interface B {
    fun f(){print("B")}
    fun b(){print("b")}
}
// interface memebers are 'open' by default

class C(): A(), B{
    // The compiler requires f() to be overriden:
    override fun f(){
        super<A>.f()
        super<B>.f()
    }
}

open의 클래스 A의 f()함수와 B interface의 f()함수가 다중으로 상속되는 상황이다. 그래서 super<Base>의 형태로 함수를 각각 불러올 수 있다. class C()에서 보듯 f()함수를 정의한 부분을 살펴볼 수 있다. 

super<A>.f()는 A.f()를 하는 것과 동일
super<B>.f()는 B.f()를 하는 것과 동일

함수 f()가 open class A의 f()도 상속받았고, interface B의 f()도 함께 상속이 된 상태라서 가능한 rule이다. 

추가로 Java8 이전 버전을 공부한 사람들은 interface에서 함수의 정의가 가능함을 의아해 할 수 있다. java 8 virtual extension methods의 자료를 참고하면 되겠다. 

I've been following the evolution of the Java 8 Lambda expressions project for a while now, and I'm really thrilled by its current state of progress. The latest "easy-to-understand" presentation I've found is this one:

I was wondering whether it was also considered to introduce "final" extension methods as opposed to "default" ones. For example:

interface A {
    void a();
    void b() default { System.out.println("b")};
    void c() final {System.out.println("c")};
}

When implementing the above interface A, 
- MUST also implement a()
- MAY implement/override b()
- Cannot override c()

API designers can create convenience methods more easily without risking client code "illegally" overriding default implementations. That's one of the main purposes of "final".

A class could inherit multiple colliding final method implementations in the case of "diamond interface inheritance". This could lead to new compilation errors in existing code. I guess this lack of backwards compatibility is the biggest drawback. 

Abstract class
Java에서 사용하는 추상 클래스의 정의는 다음과 같다. 

public abstract class Base {

    public Base(String name){
        update(name);
    }

    protected abstract void updateName(String name);
}

public class UseName extends Base{
    public UseName(String name){
        super(name);
    }

    @Override
    protected void updateName(String name){

    }
}

위와 같은 정의를 사용한다. kotlin에서도 기본 abstract 는 동일하게 구현된다.

abstract class BaseUse(name: String){

    init {
        updateName(name)
    }

    protected abstract fun updateName(name: String)
}

class UseName(name: String) : BaseUse(name){
    override fun updateName(name: String){
    }
}

추가로 kotlin의 open class를 추가하여 다음과 같은 확장도 가능하다.

open class Base{
    open fun f(){}
}

abstract class AbstractBase : Base(){
    override abstract fun f()
}

open 클래스의 f()함수는 override가 가능한 형태다. 이를 AbstractBase에서 상속받고 abstract으로 확장할 수 있다????????????????왜 굳이

open class Base{
    open fun f(){}
}

abstract class AbstractBase : Base(){
    override abstract fun f()
}

class ApplicateBase() : AbstractBase(){
    override fun f(){}
}

Java의 static 메소드 사용하기
java에서는 함수 내에 static 을 선언하여 외부에서 접근하게 된다. 

public class ClassName {

    private ClassName(){}

    public static ClassName getInstance(){
        return new ClassName();
    }
}

ClassName className = ClassName.getInstance();

위와 같은 getInstance의 형태로 사용하게 된다. 
kotlin에서는 companion 키워드를 사용하여 구현하게 된다.

class ClassName private constructor(){

    companion object {
        fun getInstance() = ClassName()
    }
}

val className = ClassName().getInstance()

ClassName className = ClassName.Companion.getInstance();

Sealed Classes
kotlin의 마지막 내용인 Sealed Classes 의 내용이다. 
열거 형태로 자기 자신을 return가능하고, 다음과 같이 class와 object에 자기 자신을 return하는 클래스 형태를 제공한다.
대락 다음을 실행하면 eval이라는 함수에 Expr을 세팅한다. when으로 동작하는데 Expr의 Sum클래스를 초기화한다. 초기화시에는 2개의 Expr을 사용하고, 이를 +하는 함수다. 실제로는 erpr.number을 가져와서 처리하는 예제다.

fun main(args: Array<String>){
    print(eval(Expr.Sum(Expr.Const(12.44), Expr.Const(12.33))))
}

sealed class Expr {
    class Const(val number: Double) : Expr()
    class Sum(val e1: Expr, val e2: Expr) : Expr()
    object NotANumber : Expr()
}

fun eval(expr: Expr): Double = when(expr){
    is Expr.Const -> expr.number
    is Expr.Sum -> eval(expr.e1) + eval(expr.e2)
    Expr.NotANumber -> Double.NaN
}

Lambda식
Lambda 식은 쉽게 말해 function에 function을 전달하고 이를 call하게 하는 것.

FloatingActionButton fab = findViewById(R.id.fab);

fab.setOnClickListener(v -> {
}
);

View 내부의 setOnClickListener에는 아래와 같은 코드를 담고 있다. 

public interface OnClickListener {
    void onClick(view v);
}

public void setOnClickListener(@Nullable OnClickListener 1){
    if(!isClickable()){
        setClickable(true);
    }

    getListenerInfo().mOnClickListener = 1;
}

어떠한 click event가 발생하면 이를 내가 등록한 Listener에 전달해주기를 원하는 단순한 형태이다. 

Java 7에서는 람다식이 없으니 new View.OnClickListener를 정의해야 하지만
Java 8 이상이나 Kotlin에서는 아래와 같이 단순하게 정의할 수 있음.

fab.setOnClickListener{}
SAM(Single Abstract Method) 정의에 의해 동작하는 코틀린
SAM은 interface에 1개의 Methods만 있을 경우 동작하며 이러한 정의는 위에서 본 setOnClickListener와 OnClickListener interface정의 와 같이 Java파일에서만 존재해야 SAM이 동작한다. Kotlin에서는 Higher-Order Function이 제공되고 있으므로 중요치 않으며 처음 설계부터 위와 같이 동작하도록 만들어두었다. 

private int findMax(ArrayList<Integer> list){
    int max = -1;
    for(Integer integer: list){
        if(integer > max){
            max = integer;
         }
        return max;
    }
}

private fun findMax(list: ArrayList<Int>) = list.maxBy{ it } ?:0

selector: (T) -> R

Kotlin에는 High-Order Functions이 제공된다.
Higher-Order Functions는 함수를 변수로 넘겨주거나 이를 반환하는 것을 말한다.

fun<T> lock(lock: Lock, body: () -> T) : T {
    lock.lock()
    try{
        return body()
    }
    finally { 
        lock.unlock()
    }
}
lock 함수에 포함된 파라미터 중 body: () ->는 Higher-Order Functions에 해당한다. 
- body : 파라미터 이름
- () -> T : ()파라미터가 없는 메소드를 정의하였고, -> T는 return타입을 정의한다. 여기서는 Generics T를 사용하였기에 T를 Generics T를 리턴한다.
body 변수는 파라미터가 없고, T를 리턴하는 메소드를 넘겨주도록 정의했다.
위에 정의한 lock 메소드를 호출할 때는 아래와 같이 사용할 수 있는데, 여기서는 toBeSynchronized()를 그대로 넘겨준다. 이때 ::두개로 함수를 직접 가리킬 수 있어서 ::toBeSynchronized로 지정한다.

fun toBeSynchronized() = sharedResource.operation()
val result = lock(lock, ::toBeSynchronized)

이런 형태를 통해 실제 body()를 호출하면 toBeSynchronized()메소드가 호출되는 형태다. 

이러한 형태는 kotlin의 Standard.kt 레퍼런스 클래스에서 흔하게 확인할 수 있는데, 아래는 let 확장 함수다. 

@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R = block(this)
let 안에 있는 block: (T) -> R을 구분해ㅓㅅ 보면
block: 파라미터 이름
(T) -> R 파라미터로 T를 받고, 이를 R로 리턴하는 함수를 정의
결국 block은 (this)의 Generics 타입을 넘겨주고 결과는 Generics R을 리턴하는 것

파라미터 정의 - 없을 경우 
Higher-Order Functions를 사용할 때는 ()안에 파라미터를 N개 정의할 수 있다. 그냥 함수와 동일함. 그렇기에 1개일 때와 N개일 때와 처리하는 방법이 다르다. 

fun print(body: () -> String){
    println(body())
}

@Test
fun test(){
    print({
        "리턴되는 값을 정의"
    })
}
 
위와 같이 파라미터가 없다면 print = {}를 정의한다. 그리고 -> String을 정의했다. lambdas식에서는 마지막 줄이 return이기 때문에 리턴되는 값을 정의 가 return 되며 println()에 의해 리턴되는 값을 정의 가 출력된다. 

이번에는 파라미터가 하나
fun print(body: (String) -> String){
    println(body("이걸 더해서"))
}

@Test
fun test(){
    print({ a -> 
        "$a 리턴되는 값을 정의"
    })
}


파라미터가 1개인 경우에는 it로 대체가 가능하며 아래와 같이 정의할 수 있다. 
fun print(body: (String) -> String){
    println(body("이걸 더해서"))
}

@Test
fun test(){
    print({
        "$it 리턴되는 값을 정의"
    })
}

파라미터가 2개 이상인 경우 - 이 경우도 람다식으로 정의되어, a/b변수는 다음을 가리키고 있다.

fun print(body: (String, String) -> String) {
    println(body("이걸 더해서", "이것도 더해서"))
}

@Test
fun test(){
    print({a, b ->
        "$a $b 리턴되는 값을 정의"})
    }

사용하는 쪽 코드를 좀 더 lambdas로 줄여보자
Higher-Order Functions을 lambdas식을 통해 줄여보겠다. 

@Test
fun test(){
    print({ a, b -> 
        "$a $b 리턴되는 값을 정의"
    })
}

위의 코드에서 print뒤에 있는 ()를 단순히 제거하면 아래와 같다. if문을 연상할 수 있음

@Test
fun test(){
    print{ a, b ->
        "$a $b 리턴되는 값을 정의"
    }
}

Higher-Order Functions를 한개 이상 사용할 경우
fun print(body: (String) -> String, body2: (String) -> String){
    println(body("이걸 더해서"))
}

@Test
fun test(){
    print({ a ->
        "$a 리턴되는 값을 정의"
    }, {
        "BBBB"
    })
}

Higher-Order Functions 정의가 N개라면, 마지막의 Higher-Order Functions은()안에 포함하지 않고, ()밖에 위치시킬 수 있음. 

fun print(body: (String) -> String, body2: (String) -> String){
    println(body("이걸 더해서"))
}

@Test
fun test(){
    print({ a -> "$a 리턴되는 값을 정의"}) {"BBBB"}
}

다른 메소드 참조
이번에는 직접 구현하는 것이 아니라 다른 메소드를 참조하게 만들 수 있다. 

fun print(body: (String, String) -> String){
    println(body("이걸 더해서", "이것도 더해서"))
}

fun sample(a: String, b: String) = "$a $b 리턴되는 값을 정의"

@Test
fun test(){
    print{a, b ->
        sample(a, b)
    }
}

::를 이용해서 부르려는 함수의 파라미터가 서로 같아야 한다 만약 파라미터가 하나라도 다르면 코드 오류가 발생한다. 
아래 코드에서는 print에 String, String 2개의 파라미터를 body에 넘겨주고 있다. 그래서 호출하는 sample도 String, String 2개의 파라미터가 존재해야 하며 String, Int식으로 접근할 수 없다.

fun print(body: (Int, Int) -> Int){
    println(body(1,2))
}

@Test
fun test(){
    print(::sample)
}

fun sum(a: Int, b: Int) = a + b
fun minus(a: Int, b: Int) = a - b
fun multiply(a: Int, b: Int) = a * b
fun division(a: Int, b: Int) = a / b

@Test
fun test(){
    print(::sum)
    print(::minus)
    print(::multiply)
    print(::division)
}

Higher-Order Functions를 디컴파일 해보면?
bytecode를 디컴파일 해보면 결국 Function1 메소드로 값을 넘기는 것을 확인해 볼 수 있다. 

public final call ExampleUnitText{
    public final void print(@NotNull Function1 body){
        Intrinsics.checkParameterIsNotNull(body, "body");
        Objecct var2 = body.invoke("이걸 더해서");
        System.out.println(var2);
    }

class Sample {
    val higherOrder : () -> Unit = {
        println("Higher order functions")
    }


class Sample {
    private lateinit var higherOrder : (String) -> Unit

    @Test
    fun test(){
        higherOrder = {
            println("$it print higherOrder")
        }

        higherOrder("print message")
    }
}

lateinit var onItemClick : (Int) -> Unit





