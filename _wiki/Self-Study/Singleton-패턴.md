1. 가급적 쓰지 말자
Singleton으로 객체를 생성하는 경우, 이 객체는 '전역변수'와 같다. 
단 하나만의 객체가 생성되고, static함수(getInstance() 함수)를 통해 접근 가능하고, static 함수가 public이기 때문에, 전역 변수와 같은 특성을 지니게 된다. 그래서 마치 전역 변수처럼 사용되는 문제가 생긴다. 테스트 주도 개발이라는 책에는 싱글톤에 대해 이렇게 설명한다.

싱글톤은 전역 변수처럼 무분별하게 사용될 여지가 있고, 특히 Unit Test를 작성하기 어렵게 만든다. 전역 변수라서 이곳 저곳에서 접근 가능하고 static final 변수로 선언되기 때문에 대체가 불가능하다. 비슷한 문제가 enum, Holder 패턴에서도 발생한다. 하지만 Singleton 객체는 이들 패턴보다 훨씬 무질서하게 사용되는 경향이 있다. 

public class Singleton {
    private volatile static Singleton instance = null;
    
    public static Singleton getInstance(){
        if(instance == null){
            synchronized(Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    private Singleton(){}
    
    public static void main(String[] args){
        Singleton instance = Singleton.getInstance();
    }
}

// 이렇게 getInstance(){} 안에서 객체를 생성하는 경우가 있고, static으로 생성하는 방식도 있다. 

public class StaticSingleton{
    private static final StaticSingleton instance = new StaticSingleton();
    private StaticSingleton(){}
    
    public static StaticSingleton getInstance(){
        return instance;
    }
}

static으로 바로 생성하는 것과 차이점은 instance 변수 선언 시 Singleton 객체가 생성되지 않는다는 점. Singleton 객체가 매우 크고, 쓰일 가능성이 매우 낮은 경우에 보통 사용하는 방식. 객체의 생성은 getInstance() 함수가 호출될 때 이루어진다. 
이 경우 객체가 이미 생성되었는지 그렇지 않은지 (null 여부)를 체크함으로써 중복 생성을 방지할 필요가 있다. 이 때 멀티 쓰레드 상태에서 발생할 수 있는 레이싱 문제도 고려해 주어야 한다.
if(instance == null)을 체크하는 부분이 두 곳이 있다. 
첫번째는 일단 instance가 null이 아닐 경우 synchronized 블록 내부로 들어가는 것을 미리 차단한다. 이는 Singleton 객체를 얻기 위해 발생하는 부하를 줄여주는 역할을 한다. 

일단 null임이 확인된 이후에는 synchronized 블럭으로 넘어가게 된다. 이 때는 이 블럭이 단 하나의 쓰레드만 내부로 접근할 수 있도록 한다. (여기로 instance 변수가 들어가게 됨) 이후 instance 객체의 null 체크를 한번 더 한다. 이는 맨 처음 synchronized 블럭에 들어온 쓰레드를 위한 것이 아니고, 그 다음에 들어오는 경우를 위한 것이다. 

즉 두 쓰레드가 instance를 null로 판단헸고, 둘 다 synchronized 블럭으로 접근했다고 가정하자. 첫 번째 쓰레드는 일단 instance 객체를 생성하고 종료한다. 두번째 스레드가 두 번째 null 체크에 도달했을 때에는 이미 첫번째 스레드가 생성을 마치고 난 다음이다. 따라서 두번째 스레드는 이 null 체크에서 instance 객체가 null이 아니라고 판단하게 된다. 그리고 그 블록을 그냥 빠져 나온다. 이를 통해서 instance 객체가 중복 생성되는 것을 막을 수 있다. 

이 방법(synchronized lock)은 실제로는 거의 의미가 없다시피하다. 어쨌든 필요한 경우 사용해 보려면 정확한 구현 매커니즘을 이해하는 것이 도움이 된다. 그러나 싱글톤은 사용하지 않는 편이 웬만하면 좋다...
