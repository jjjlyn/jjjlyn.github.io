Factory Method 패턴: 객체의 생성과 관련된 패턴
Factory라는 것은 생산품을 생상하는 생산자의 의미로 사용되는 단어이며,
객체지향 언어에서는 객체를 생성하는 생산자를 의미한다. 

Method는 본래 Template Method 패턴에서 차용한 단어다. Factory Method 패턴에서는 객체의 생성 직후에 해야 할 일을 순서대로 정의한 메서드를 제공한다. 그리고 구체적인 생성은 구체 클래스들에게 위임한다. 

이익은 다음과 같다. 
1. 객체 생성 후 공통으로 할 일을 수행한다. 
   객체가 생성된 직후에 어떤 작업을 수행해야 하는 경우는 자주 나타난다. 특히 문제가 되는 경우는 객체가 이벤트를 수신하는 경우다. 만약 객체를 이벤트 수신자로 등록하는 코드를 생성자 내부에 넣어두게 되면 생성이 완료되기 이전에 이벤트를 받게 되어 오류가 발생하는 경우가 생긴다. 이런 일은 객체 생성 이후에 해줘야 하는데, Factory Method 패턴을 이용하면 이 문제를 해결할 수 있다.
2. 생성되는 객체의 구체적인 타입을 감춘다.
   Factory Method 패턴은 그 구조 상 생성된 객체의 타입을 구체적인 타입이 아니라 추상 타입(상위 타입)으로 리턴한다. 이를 통해서 객체를 사용하는 측에서는 구체적인 타입의 존재조차 모르도록 할 수 있다. 
3. 구체적인 Factory 클래스가 생성할 객체를 결정하도록 한다. 
구체 Factory 클래스는 생성 메소드 내부의 구현을 책임진다. 구체 생성 메소드 내부에서는 필요한 동작을 자유롭게 구현할 수 있다. 특히 인자를 받거나 상태에 따라서 생성할 객체를 바꿀수도 있다. 이렇게 하면 좀 더 다양한 기능을 수행하거나 수정에 용이한 구조를 만들 수도 있다. 

> 1. 의 경우를 구현
class Wheel{
    private Wheel(){}
    public static Wheel create(){
        Wheel wheel = new Wheel();
        wheel.init();
        return wheel;
    }
    public void init(){}
}

위의 클래스에서 create() 메소드가 이에 해당한다. create() 메소드는 객체가 생성된 직 후 해주어야 할 일(init() 메소드를 호출하는 일)을 수행한다. 어떤 작업은 객체의 생성자 내부에서 하지 못하는 일이 있을 수 있다. 그런 일들은 객체가 생성된 '이후'에 해주어야 하는데, 이런 Factory Method를 제공해주지 않으면 객체를 생성받은 쪽에서 그 일을 해야 한다. 만약 객체를 생성하는 곳이 '여러 곳'이라면 이런 작업을 여러 곳에서 해야 하고, 또 이를 수정해야 할 경우에는 이걸 또 여러 곳에서 수정해야 한다. 이런 문제를 방지할 수 있도록 해주는 것이 바로 Factory Method 이다.

Factory Method 패턴은 좀 더 많은 문제에 관여하는 패턴이다. 위는 Factory Method 패턴은 아님.

abstract class ShapeFactory{
    public final Shape create(Color color){
        Shape shape = createShape();
        shape.setColor(color);
        return shape;
    }
    
    abstract protected Shape createShape();
}

class RectangleFactory extends ShapeFactory {

    @Override
    protected Shape createShape(){
        return new Rectangle();
    }
}

class CircleFactory extends ShapeFactory {
    
    @Override
    protected Shape createShape(){
        return new Circle();
    }
}

interface Shape{
    public void setColor(Color color);
    public void draw();
}

class Rectangle implements Shape {
    Color color;
    public void setColor(Color color){
        this.color = color;
    }
    public void draw(){
        System.out.println("rect draw");
    }
}

class Circle implements Shape {
    Color color;
    public void setColor(Color color){
        this.color = color;
    }
    public void draw(){
        System.out.println("circle draw");
    }
}

abstract class ShapeFactory {
    public final Shape create(Color color){
        Shape shape = createShape();
        shape.setColor(color);
        return shape;
    }
    
    abstract Shape createShape();
}

class RectangleFactory extends ShapeFactory {
    @Override
    protected Shape createShape(){
        return new Rectangle();
    }
}

interface Shape {
    public void setColor(Color color);
    public void draw();
}

class Rectangle implements Shape {
    Color color;
    public void setColor(Color color){
        this.color = color;
    }
    public void draw(){
        System.out.println("circle draw");
    }
}

최초에 나오는 ShapeFactory가 Factory Method 패턴의 기본 클래스다.

abstract class ShapeFactory {
    
    public final Shape create(Color color){
        Shape shape = createShape();
        shape.setColor(color);
        return shape;
    }
    
    abstract protected Shape createShape();
}

여기서 create() 메소드가 생성 메소드 역할을 담당. 이 메소드는 구체 메소드이므로 ShapeFactory 를 상속받은 클래스들은 모두 이를 이용할 수 있다. 또한 'final' 메소드이므로 재정의가 불가능하다. Shape을 return타입으로 사용하기 때문에 ShapeFactory 를 사용하는 곳에서는 리턴되는 Shape의 구체적인 타입(예제에서는 Rectangle인지 Circle인지를)을 알 수 없다.
ShapeFactory는 createShape()이라는 추상 메소드를 선언하고 있다. ShapeFactory의 구체 클래스들은 이 메소드를 통해서 어떤 객체를 생성할지 결정한다. create()메소드와 createShape() 메소드의 관계는 Template Method 패턴과 같다??
createShape() 메소드는 protected로 선언되어 있어서 외부 클래스에서는 접근이 불가하다. 즉 create() 메소드만을 이용하도록 해서 객체 생성 이후 수행할 작업이 완료될 것을 강제하는 것이다. createShape()를 구현하는 구체 Factory 클래스(ShapeFactory를 extends한 클래스)들에서는 다양한 형태로 이를 구현할 수 있다. createShape()메소드가 만약 매개변수를 받는다면, 매개변수를 조건으로 하여 구체적으로 생성할 Shape의 종류를 바꿀 수도 있고, createShape() 메소드 내부에서 가지고 있는 정보들을 활용하여 생성 조건을 변경할 수도 있음. 

Factory Method 패턴에서 문제가 될 부분은 구체 Factory들이 여러번 생성될 필요가 없다는 점이다. 따라서 Singleton 패턴을 이용해야 할 경우가 있는데 이렇게 되면 패턴이 복잡해지는 문제점이 있다. 이 문제는 Enum 타입을 통해 Factory Method 패턴을 구현함으로써 해결할 수 있다...

Enum Factory Method 패턴은 자바에서만 가능하다. 기본적으로 Factory Method 패턴과 마찬가지로 객체의 생성을 담당하는 메소드를 구현하는 패턴이다. 이와 함께 Factory Method를 구현한 객체를 생성하기 위해 Singleton을 사용해야 하는 문제점을 Enum의 특성을 이용하여 해결한다. 

public enum EnumFactoryMethod {
    RECTANGLE{
        protected Shape createShape(){return new Rectangle();}
    },
    CIRCLE{
        protected Shape createShape(){return new Circle();}
    };
    
    public Shape create(Color color){   
        Shape shape = createShape();
        shape.setColor(color);
        return shape;
    }
    
    abstract protected Shape createShape();
    
    public static void main(String[] args){
        EnumFactoryMethod.RECTANGLE.create(Color.black);
        EnumFactoryMethod.CIRCLE.create(Color.white);
    }
}

Enum 타입 자체가 public static final 이기 때문에 생성을 위임 받은 객체에 대한 중복 생성이 불가하고, Singleton을 굳이 구현하지 않아도 단일한 객체만 생성됨이 보장된다. 

