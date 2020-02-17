#### Generic enum 값 접근
Generic 방식으로 enum 클래스의 값을 차례로 열거할 수 있다.
```Kotlin
enum class RGB { RED, GREEN, BLUE }
inline fun <reified T : Enum<T>> printAllValues(){
    print(enumValues<T>().joinToString{
        it.name}
    )
}
```

#### Enum을 이용한 Factory Method 패턴
```java
public enum EnumFactoryMethod{
    RECTANGLE{ //EnumFactoryMethod 타입
        protected Shape createShape(){
            return new Rectangle();
        }
    },
    CIRCLE{
        protected Shape createShape(){  
            return new Circle();
        }
    };
    
    public Shape create(Color color){
        Shape shape = createShape();
        shape.setColor(color);
        return shape;
    }
    
    abstract protected Shape createShape();
    
    public static void main(String[] args){
        EnumFactoryMethod.RECTANGLE.create(Color.BLACK);
        // EnumFactoryMethod 타입이며 public static final 임
        // abstract method인 createShape()을 구현한 구체 Factory class
        // Singleton 방식 대신 사용(code 의 간편화)
        // EnumFactoryMethod를 extends한 RECTANGLE 타입 -> abstract protected Shape createShape()를 override(재정의)해야 한다.
        EnumFactoryMethod.CIRCLE.create(Color.WHITE);
    }
}
```

#### Enum의 활용법
C언어에서 enum은 단순히 상수형 변수 역할에 지나지 않는다. 하지만 Java에서는 매우 다른 특성을 지니고 있다.

enum Type{
    ADD, // public static final class ADD extends Type{}
    SUB; // public static final class SUB extends Type{}
}

enum은 추상 클래스이다. 그리고 그 하위에 선언된 각 열거형 변수는 실제로는 변수가 아니고 **enum 타입을 상속받은 하위 클래스**이다. 이 하위 클래스는 외부에 노출되어 있고, 생성할 필요가 없고, 런타임에 교체가 불가능하므로 public static final 타입이다.
(와 한방에 이해했다...)

enum은 기본적으로 추상 클래스이긴 하나, 다른 클래스로부터 상속을 받지는 못한다. 그러나 interface를 상속받을 수는 있음!

interface Interface {
    public void api();
}

enum Type implements Interface {
    ADD {
        public void api(){System.out.println("ADD api");}
    },
    SUB { 
        public void api(){System.out.println("SUB api");}
    };
}

이 인터페이스 상속 방법은 생각보다 강력하다. 이 특성을 이용해서 디자인 패턴에 나오는 수많은 패턴들을 enum을 통해 구현할 수 있음. enum이 public static final이라는 점은 Singleton과 유사한 특성을 지니고 있다. 이 강력한 특성 때문에 Singleton 패턴, Abstract Factory 패턴, Factory Methon 패턴, State 패턴, Strategy 패턴, Visitor 등 다양한 패턴의 enum 버전이 있다. 
그래서 enum을 이용해서 Singleton의 복잡한 구현을 대신하기도 한다.

추상 클래스라면 함수를 선언할 수도 있어야 한다. (**Enum 클래스는 추상 클래스다**) 

enum Type {
    ADD,
    SUB;
    public void api(){
        System.out.println("api()");
    }
}

추상 클래스이기 때문에 추상 메소드의 선언도 가능하다. interface를 상속받을 수 있다는 점에서도 이 점을 유추할 수 있음..(?)

enum Type{
    ADD{
        public void api(){System.out.println("ADD api");}
    },
    SUB{
        public void api(){System.out.println("SUB api");}
    };
    abstract public void api();
}

같은 맥락에서 static 메소드 역시 가능하다. 

public static int size(){return values().length;}

보통은 Type.values().length 를 통해 꺼내오기도 하나, 가독성 측면에서 Type.size()를 호출하는 것이 더 나아보인다.

클래스라면 생성자도 선언할 수 있어야 한다. 생성자 선언은 아래와 같다. 
enum Type{
    ADD(0),
    SUB(1);
    int value;
    private Type(int value){this.value = value;}
    public int value(){return value;}
}

enum 은 외부에서 생성이 불가능하기 때문에 생성자는 항상 private으로 선언해주어야 한다.

문자열로 enum을 알아낼 수 있다. enum이 클래스라는 것은 이미 알고 있다(enum 추상 클래스가 extends된 구체적 타입). 따라서 하위 타입(구체적 타입)도 모두 toString() 함수를 가지고 있다. 그 결과 값은 기본적으로 자신의 선언된 이름과 같음. ADD.toString()은 "ADD" 값이 결과값이다. 이런 특성은 매우 유용한데 특히 데이터베이스에 저장할 때 유용하다고 한다. DB 가독성 측면에서 문자열을 활용한 경우에 문자열을 입력받아 타입을 리턴하도록 하면 코드 상에서 문자열 대신 enum을 활용할 수 있으므로 코딩이 편리해진다. 

public static Type getTypeByString(String str){
    for(Type each : values()){
        if(each.toString().equals(str)) return each;
    }
    return null;
}

enum 이 제공하는 기본 함수로 enum의 순서를 알 수 있는 함수가 있다. 

public int ordinal();

근데 effective java에서 이건 뭐.. 웬만하면 쓰지 말라고 했었던 것 같다ㅣ 
Java에서 enum은 열거형의 특성과 클래스의 특성을 함께 가지고 있다는 장점이 있다. 특히 클래스의 특성인 toString()함수를 가지고 있다는 것만으로도 디버깅을 겁나 쉽게 할 수 있게 해준다. 그 밖에도 DB 연동, switch-case 문에 대한 활용, 인터페이스 상속을 활용한 디자인 패턴 등 다양한 곳에 활용할 수 있다.

