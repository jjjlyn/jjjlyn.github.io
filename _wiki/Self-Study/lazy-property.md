Kotlin에는 늦은 초기화를 위한 방법 2개를 제공한다.

```kotlin
class SampleMainActivity {
    private val sampleAdapter: SampleAdapter = 
        SampleAdapter(ImageLoaderAdapterViewModel(this@SampleMainActivity, 3))
}
```

이런 식으로 class 전역에 값을 생성하면 클래스가 **만들어지는 시점**에 변수를 함께 초기화한다. class 생성과 동시에 변수가 초기화되면, 재 접근시 빠르게 접근이 가능하여 이득을 볼 수 있다(?)

하지만 sampleAdapter (위에서) 변수를 꼭 사용하는 것이 아니라면 오히려 **메모리 손해**를 볼 수 있다. 필수 요건이 아닌 경우라면 **늦은 초기화**가 필요하다.

```kotlin
class SampleActivity{
    private var sampleAdapter: SampleAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_main)
        
        sampleAdapter = SampleAdapter(ImageLoaderAdapterViewModel(this@SampleMainActivity, 3)
    }
}
```

Kotlin에서는 늦은 초기화 시 null을 명시해야 하는데, **꼭 null이 필요하지는 않다.** Java라면 무조건 null에 대한 접근이 가능하여, 언제든 null로 명시할 수 있지만 kotlin의 경우 null은 필요한 경우에만 명시해야 한다.

***
특히 Java와 함께 사용하거나, null을 통해 명시가 필요한 경우
***

```kotlin
class SampleMainActivity {
    private var sampleAdapter: SampleAdapter? = null // 코틀린에서 전역변수는 초기화가 필요하다
    
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.id.R.layout.activity_sample_main)
        
        sampleAdapter = SampleAdapter(ImageLoaderAdapterViewModel(this@SampleMainActivity, 3)
        
        sampleAdapter?.addItem()
        sampleAdapter?.notifyDataSetChanged()
    }
}
```
안전할 수는 있는데 저 변수는 null일 필요가 없으며, 코틀린에서 전역변수는 초기화가 필요하다. 그러다보니 늦은 초기화 2개를 제공한다.
1. **<u>Late-Initialized Properties</u>**
2. **<u>lazy</u>**

### **Late-Initialized Properties**
```java
public class SampleMainActivity extends AppCompatActivity {
    private SampleAdapter sampleAdapter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
        
        sampleAdapter = new SampleAdapter(new ImageLoaderAdapterViewModel(this, 3));
    }
}
```

sampleAdapter 변수는 onCreate에서 초기화를 하고 있어 이미 늦은 초기화에 해당하며, 언제나 null을 가질 수 있지만 굳이 null을 명시할 필요가 없는 코드이다. 

이걸 코틀린의 Late-Initialized Properties를 통해 해결하려고 하면, 

```kotlin
class SampleMainActivity : AppCompatActivity(){
    private lateinit var sampleAdapter : SampleAdapter
    
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_main)
        
        sampleAdapter = SampleAdapter(ImageLoaderAdapterViewModel(this@SampleMainActivity, 3))
        
        sampleAdapter.addItem()
        sampleAdapter.notifyDataSetChanged()
    }
}
```

### **lateinit 조건**
lateinit은 변수를 부르기 전에 초기화 시켜야 하는데 아래와 같은 조건을 가지고 있다. 

* var(mutable)에서만 사용 가능
* var이기 때문에 언제든 초기화 변경 가능
* null을 통한 초기화 불가
* 초기화 전에는 변수 접근 불가
* 변수에 대한 setter/getter properties가 불가
* lateinit은 모든 변수가 가능하지 않고, primitive type에서는 활용 불가

***

### 내가 알고 싶어하는 **lazy properties**
lateinit은 필요한 경우 언제든 초기화가 가능한 properties
이번에는 생성 후 값을 변경할 수 없는 val(immutable) 정의
lazy 초기화는 기존 val 변수 선언에 _by lazy_ 를 추가함으로 lazy{}에 생성과 동시에 값을 초기화하는 방법을 사용한다. 

```kotlin
val sampleAdapter: SampleAdapter by lazy {
    SampleAdapter(ImageLoaderAdapterViewModel(this, 3))
}
```

lazy는 lateinit과는 반대로 상대적으로 편하게 사용이 가능하고, 실수할 일도 줄어든다.
* 호출 시점에 by lazy 정의에 의해 초기화를 진행한다.
* val(immutable)에서만 사용 가능
* val이므로 값을 교체하는 것은 불가능
* 초기화를 위해서는 함수명이라도 한번 적어줘야 한다(?)
* lazy를 사용하는 경우 기본 Synchronized로 동작한다(?!!)

### **lazy 로그를 통한 초기화 확인하기**

