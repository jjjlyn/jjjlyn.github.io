빌더 패턴은 객체를 생성할 때 흔하게 사용하는 패턴이다. 
자바로 코딩할 때 다음과 같은 스타일로 객체를 생성하는 코드가 있다면, 빌더 패턴을 사용했다고 할 수 있다.

Member customer = Member.build()
    .name("홍길동")
    .age(30)
    .build();

- 이펙티브 자바의 빌더 패턴
    - GoF의 빌더 패턴보다 좀 더 코딩 위주의 활용법을 설명한다. 
        - 코드 읽기/유지보수가 편해지므로 빌더 패턴을 쓰라고 한다. 
    - GoF가 책을 썼을 때에는 상대적으로 덜 중요했던 객체 일관성, 변경 불가능성 등의 특징을 설명한다. 
- GoF의 빌더 패턴
    - 객체 생성 알고리즘과 조립 방법을 분리하는 것이 목적이다.

Effiective Java의 빌더 패턴
- 생성자 인자가 많을 때는 Builder 패턴 적용을 고려하라
이펙티브 자바에서 말하는 빌더 패턴은 객체 생성을 깔끔하고 유연하게 하기 위한 기법이다.
- 객체 생성을 깔끔하게 
    - 점층적 생성자 패턴 소개
    - 점층적 생성자 패턴의 대안으로 자바빈 패턴 소개
    - 자바빈 패턴의 대안으로 빌더 패턴 소개
- 객체 생성을 유연하게
    - 빌더 인터페이스를 작성하여 다양한 빌더를 사용하는 방법 소개

객체 생성을 깔끔하게

- 점층적 생성자 패턴
점층적 생성자 패턴을 만드는 방법은 다음과 같다.
1. 필수 인자를 받는 필수 생성자를 하나 만든다.
2. 1개의 선택적 인자를 받는 생성자를 추가한다.
3. 2개의 선택적 인자를 받는 생성자를 추가한다.
4. ...반복
5. 모든 선택적 인자를 다 받는 생성자를 추가한다.

public class Member {

    private final String name;
    private final String location;
    private final String hobby;

    public Member(String name){
        this(name, "출신지역 비공개", "취미 비공개");
    }

    public Member(String name, String location){
        this(name, location, "취미 비공개")
    }

    public Member(String name, String location, String hobby){
        this.name = name;
        this.location = location;
        this.hobby = hobby;
    }

장점: new Member("홍길동", "출신지역 비공개", "취미 비공개"); 같은 호출이 빈번하게 일어난다면, new Member("홍길동")로 대체할 수 있다.

단점
- 다른 생성자를 호출하는 생성자가 많으므로, 인자가 추가되는 일이 발생하면 코드를 수정하기 어렵다.
- 코드 가독성이 떨어진다. 특히 인자 수가 많을 때 호출 코드만 봐서는 의미를 알기 어렵다.

- 자바빈 패턴: 따라서 이에 대한 대안으로 자바빈 패턴을 소개한다.
이 패턴은 setter메서드를 이용해 생성 코드를 읽기 좋게 만드는 것이다.

NutrionFacts cocaCola = new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);

장점: 이제 각 인자의 의미를 파악하기 쉬워졌다.
복잡하게 여러 개의 생성자를 만들지 않아도 된다.

단점: 객체 일관성이 깨진다.
- 1회의 호출로 객체 생성이 끝나지 않았다.
- 즉 한번에 생성하지 않고 생성한 객체에 값을 떡칠하고 있다. 
- setter 메서드가 있으므로 변경 불가능(immutable) 클래스를 만들 수가 없다.
- 스레드 안정성을 확보하려면 점층적 생성자 패턴보다 많은 일을 해야 한다.

// Effective Java의 Builder Pattern
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;

    public static class Builder{
        private final int servingSize;
        private final int servings;

        private int calories = 0;
        private int fat = 0;

        public Builder calories(int val){
            calories = val;
            return this;
        }

        public Builder fat(int val){
            fat = val;
            return this;
        }

        public NutritionFacts build(){
            return new NutritionFacts(this)
        }
    }

    private NutritionFacts(Builder builder){
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
    }
}

위와 같이 하면 다음과 같이 객체를 생성할 수 있다.
NutritionFacts.Builder builder = new NutritionFacts.Builder(240, 8);
builder.calories(100)
    .fat(100);

NutritionFacts cocaCola = builder.build();

- 장점
각 의미가 어떤 의미인지 알기 쉽다. 
setter 메소드가 없으므로 변경 불가능 객체를 만들 수 있다. 
한 번에 객체를 생성하므로 객체 일관성이 깨지지 않는다.
build()함수가 잘못된 값이 입력되었는지 검증하게 할 수도 있다. 

- 객체 생성을 유연하게
빌더 패턴을 사용하면 하나의 빌더 객체로 여러 객체를 만드는 것도 가능하다.

public interface Builder<T> {
    public T build();
}



