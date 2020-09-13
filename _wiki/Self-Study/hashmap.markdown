HashTable 또한 Map 인터페이스를 구현하고 있기 때문에 HashMap과 HashTable이 제공하는 기능은 같다. 
다만 HashMap은 `보조 해시 함수`를 사용하여 보조 해시 함수를 사용하지 않는 HashTable에 비해 해시 충돌이 덜 발생할 수 있어 상대적으로 성능상 이점이 있다. 
여기서 보조 해시 함수는 도대체 무엇인가...?
HashMap과 HashTable을 정의한다면, '키에 대한 해시 값을 사용하여 값을 저장하고 조회하며, 키-값 쌍의 개수에 따라 동적으로 크기가 증가하는 associate array'라고 할 수 있다. 이 associate array를 지칭하는 다른 용어가 있는데, 대표적으로 Map, Dictionary, Symbol Table 등이다.

associate array를 지칭하기 위해 HashTable에서는 Dictionary라는 이름을 사용하고, HashMap에서는 그 명칭이 그대로 말하듯이 Map이라는 용어를 사용하고 있다. map은 원래 수학 함수에서의 대응 관계를 지칭하는 용어로, 경우에 따라서는 함수 자체를 의미한다. HashMap이라는 이름에서 알 수 있듯이, HashMap은 키 집합인 정의역과 값 집합인 공역의 대응에 해시 함수를 이용한다.

Boolean 같이 서로 구별되는 객체의 종류가 적거나, Integer, Long, Double같은 Number 객체는 객체가 나타내려는 `값 자체를 해시 값으로 이용`할 수 있기 때문에 완전한 해시 함수 대상으로 삼을 수 있다?
Java의 Object.hashCode가 int 반환값이므로 Long, Double을 완전한 해시 함수 대상으로 포함하는 것은 무리일 것이다. 
String, POJO(plain old java object)에 대해 완전한 해시 함수를 제작하는 것은 사실상 불가능하다. 적은 연산만으로 빠르게 동작할 수 있는 완전한 해시 함수가 있다 하더라도, 그것을 HashMap 에서 사용할 수 있는 것은 아니다. HashMap은 기본적으로 각 객체의 hashCode() 메서드가 반환하는 값을 사용하는데, 결과 자료형은 int다. 32비트 정수 자료형으로는 완전한 자료 해시 함수를 만들 수 없다. 논리적으로 생성 가능한 객체의 수가 2^32보다 많을 수 있으며, 또한 모든 HashMap 객체에서 O(1)을 보장하기 위해 랜덤 접근이 가능하게 하려면 원소가 2^32인 배열을 모든 HashMap이 가지고 있어야 하기 때문이다. 따라서 HashMap을 비롯한 많은 해시 함수를 이용하는 associative array 구현체에서는 메모리를 절약하기 위하여 실제 해시 함수의 표현 정수 범위보다 작은 M개의 원소가 있는 배열만을 사용한다. 따라서 다음과 같이 객체에 대한 해시 코드의 나머지 값을 해시 버킷 인덱스 값으로 사용한다. 
int index = X.hashCode() % M;
이 코드와 같은 방식을 사용하면, 서로 다른 해시 코드를 가지는 서로 다른 객체가 1/M의 확률로 같은 `해시 버킷`을 사용하게 된다. 이는 해시 함수가 얼마나 해시 충돌을 회피하도록 잘 구현되었느냐에 상관없이 발생할 수 있는 또 다른 종류의 해시 충돌이다. 이렇게 해시 충돌이 발생하더라도 키-값 쌍 데이터를 잘 저장하고 조회할 수 있게 하는 방식에는 대표적으로 두 가지가 있는데, 하나는 `Open Addressing`이고, 다른 하나는 `Separate Chaining`이다. 이 둘 외에도 해시 충돌을 해결하기 위한 다양한 자료구조가 있지만, 거의 모두 이 둘을 응용한 것이라고 볼 수 있다.
int index = X.hashCode() % M;
A, B, C, D 순서로 HashMap에 삽입될 때
0, 1, 2M + 1, M + 1이라고 할 때
Open Addressing은 데이터를 삽입하려는 해시 버킷이 이미 사용 중인 경우 "다른 해시 버킷에 해당 데이터를 삽입"하는 방식이다. 데이터를 저장/조회할 해시 버킷을 찾을 때에는 Linear Probing, Quadratic Probing 등의 방법을 사용한다. 
Separate Chaining에서 각 배열의 인자는 인덱스가 같은 해시 버킷을 연결한 "링크드 리스트"의 첫 부분이다. (링크드 리스트 이용)
둘다 Worst Case O(M)이다(O(1)에서 겹치는 인덱스 링크드 리스트를 탐색하는 시간 O(M)). 하지만 Open Addressing은 "연속된 공간에 데이터를 저장"하기 때문에 Separate Chaining에 비하여 성능이 좋다(단순히 빈 버킷에 일렬로 저장하기 때문). 하지만 배열의 크기가 커질수록 캐시 효율이라는 Open Addressing의 장점은 사라진다. 
Java HashMap에서 사용하는 방식은 Separate Channing이다. Open Addressing은 데이터를 삭제할 때 처리가 효율적이기 어려운데(그런데 링크드 리스트도 맨 처음 노드 빼고는 데이터 삭제가 어렵지 않나?), HashMap에서 remove() 호출은 빈번하기 때문이다. 게다가 HashMap에 저장된 키-값 쌍 개수가 일정 개수 이상으로 많아지면, 일반적으로 Open Addressing은 Separate Chaning보다 느리다. Open Addressing의 경우 해시 버킷을 채운 밀도가 높아질수록 Worst Case 발생 빈도가 더 높아지기 때문이다. 반면 Separate Chaining 방식의 경우 해시 충돌이 잘 발생하지 않도록 조정할 수 있다면 Worst Case에 가까운 일이 발생하는 것을 줄일 수 있다(여기서 보조 해시 함수를 이용한다).

```java
transient Entry<K, V>[] table = (Entry<K, V>[]) EMPTY_TABLE;
// transient로 선언된 이유는 직렬화 할 때 전체 table 배열 자체를 직렬화 하는 것보다
// 키-값 쌍을 차례로 기록하는 것이 더 효율적이기 때문이다.

static class Entry<K, V> implements Map.Entry<K, V> {
    final K key;
    V value;
    Entry<K, V> next;
    int hash;

    Entry(int h, K k, V v, Entry<K, V> n){
        value = v;
        next = n;
        hash = h;
        key = k;
    }

    public final K getKey() { ... }
}

public V put(K key, V value) {
    if(table == EMPTY_TABLE){
        inflateTable(threshold);
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        // 해시 버킷에 있는 링크드 리스트를 순회한다.
        // 만약 같은 키가 이미 저장되어 있다면 교체한다.
        for(Entry<K, V> e = table[i]; e != null; e = e.next){
            Object k;
            if(e.hash == hash && ((k = e.key) == key || key.equals(k))){
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        // 삽입, 삭제 등으로 이 HashMap 객체가 몇 번이나 변경되었는지 관리하기 위한 코드다. 
        // ConcurrentModificationException을 발생시켜야 하는지 판단할 때 사용한다(?)
        modCount++;

        // 아직 해당 키-값 쌍 데이터가 삽입된 적이 없다면 새로 Entry를 생성한다.
        addEntry(hash, key, value, i);
        return null;
    }
}
```

Java 8에서는 데이터의 개수가 많아지면, Separate Chaining에서 링크드 리스트 대신 '트리'를 사용한다. Birthday Problem? 하튼 일부 해시 버킷 몇 개에 데이터가 집중될 수 있다. 그래서 데이터의 개수가 일정 이상일 때는 링크드 리스트 대신 트리를 사용하는 것이 성능상 이점이 있다. 링크드 리스트를 사용할 것인가 트리를 사용할 것인가의 기준은 '하나의 해시 버킷에 할당된 키-값 쌍의 개수'이다. Java 8 HashMap에서는 '상수 형태(THRESHOLD)'로 기준을 정하고 있다. 즉 하나의 해시 버킷에 8개의 키-값 쌍이 모이면 링크드 리스트를 트리로 변경한다. 만약 해당 버킷에 있는 데이터를 삭제하여 개수가 6개에 이르면 다시 링크드 리스트로 변경한다. 트리는 링크드 리스트보다 메모리 사용량이 많고, 데이터의 개수가 적을 때 트리와 링크드 리스트의 Worst Case 수행 시간 차이 비교는 의미가 없기 때문이다. 8과 6으로 2이상의 차이를 둔 것은, 만약 차이가 1이라면 어떤 한 키-값 쌍이 반복되어 삽입/삭제되는 경우 불필요하게 트리와 링크드 리스트를 변경하는 일이 반복되어 성능 저하가 발생할 수 있기 때문이다. 

```java
static final int TREEIFY_THRESHOLD = 8;
static final int UNTREEIFY_THRESHOLD = 6;
```

Java 8 HashMap에서는 Entry 클래스 대신 'Node 클래스'를 사용한다. Node 클래스 자체는 사실상 Java 7의 Entry 클래스와 내용이 같지만, 링크드 대신 트리를 사용할 수 있도록 '하위 클래스 TreeNode'가 구현된 것이 Java 7 HashMap과 다르다.
이때 사용하는 트리가 Red-Black Tree인데, Java Collections Framework의 TreeMap과 구현이 거의 같다. 트리 순회 시 사용하는 대소 판단 기준은 해시 함수 값이다. 해시 값을 대소 판단 기준으로 사용하면 Total Ordering에 문제가 생기는데, Java 8 HashMap에서는 이를 tieBreakOrder()로 해결한다.

나는 Red-Black Tree는 들어만 봤지 뭔지는 모르겠다.

```java
transient Node<K, V>[] table;

static class Node<K, V> implements Map.Entry<K, V> {

}

// 하위 클래스
// LinkedHashMap.Entry는 HashMap.Node를 상속한 클래스다.
// 따라서 TreeNode 객체를 table 배열에 저장할 수 있다.
static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V> {


}

```

[해시 버킷 동적 확장]
해시 버킷의 개수가 적다면 메모리 사용을 아낄 수 있지만 해시 충돌로 인해 성능상 손실이 발생한다. 그래서 HashMap은 키-값 쌍 데이터 개수가 일정 개수 이상이 되면, 해시 버킷의 개수를 '두 배'로 늘린다. 이렇게 해시 버킷 개수를 늘리면 값도 작아져, 해시 충돌로 인한 성능 손실 문제를 어느 정도 해결할 수 있다. 해시 버킷 개수의 기본값은 16이고, 데이터의 개수가 임계점에 이를 때마다 해시 버킷의 크기를 두 배씩 증가시킨다. 버킷의 최대 개수는 2^30이다. 그런데 이렇게 버킷 개수가 두 배로 증가할 때마다, 모든 키-값 데이터를 읽어 새로운 Separate Chaining을 구성해야 하는 문제가 있다. HashMap 생성자의 인자로 '초기 해시 버킷 개수를 지정'할 수 있으므로, 해당 HashMap 객체에 저장될 데이터의 개수가 어느 정도인지 예측 가능한 경우에는 이를 생성자의 인자로 지정하면 불필요하게 Separate Chaining을 재구성하지 않게 할 수 있다.

```java
void resize(int newCapacity){
    Entry[] oldTable = table;
    int oldCapacity = oldTable.langth;

    if(oldCapacity == MAXIMUM_CAPACITY){
        threshold = Integer.MAX_VALUE;
        return;
    }

    Entry[] newTable = new Entry[newCapacity];

    // 새 해시 버킷을 생성한 다음 기존의 모든 키-값 데이터들을 새 해시 버킷에 저장한다.
    transfer(newTable, initHashSeedAsNeeded(newCapacity));
    table = newTable;
    threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
}

void transfer(Entry[] newTable, boolean rehash){
    int newCapacity = newTable.length;

    // 모든 해시 버킷을 순회한다(bin을 탐색)
    for (Entry<K, V> e : table){
        while(null != e){
            // 해당 bin(버킷)의 링크드 리스트를 탐색한다.
            // e(는 링크드 리스트로 구현되어 있다)에 연결된 다음 노드를 불러온다.
            Entry<K, V> next = e.next;
            // 해싱의 대상이라면
            if(rehash){
                // 해당 버킷의 해시값을 null로 초기화하고 e.key와 비교한다. 
                // 같으면 0 다르면 e.key에 hash함수를 적용하여 새로운 해시값을 입힌다.
                // 사실 무슨 소리인지 모르겠다. 다시 해시값을 새롭게 박으라는 의미인 듯 하다.
                // linked list를 순회하면서 각 노드의 해시값을 모두 변경해야 하기 때문인 듯.
                (e.hash = null) == e.key ? 0 : hash(e.key);
            }
            // 해시 버킷 개수가 변경되었기 때문에 테이블(버킷)의 idx 값을 다시 계산해야 한다.
            int i = indexFor(e.hash, newCapacity);
            // e는 링크드 리스트로 구현되어 있으니까 e에 줄줄이 딸린 노드들에게도
            // 동일한 테이블의 인덱스 값을 부여해야 한다. 
            // 나머지값으로 인덱스를 부여하니까 버킷의 크기가 커지면
            // 자연히 인덱스의 값도 변경된다.
            e.next = newTable[i]; 
            // 이 짓을 해당 버킷의 링크드 리스트가 끝날 때까지 반복한다.
            e = next;
        }
    }
}
```

임계점에 이르면 항상 해시 버킷 크기를 두 배로 확장하기 때문에, N개의 데이터를 삽입했을 때의 키-값 쌍 접근 횟수는 2.5N이 된다.
즉 기본 생성자로 생성한 HashMap을 이용하여 많은 양의 데이터를 삽입할 때에는, 해시 버킷 개수를 지정한 것보다 약 2.5배 많이 키-값 쌍 데이터에 접근해야 한다. 이는 곧 수행 시간이 2.5배 길어지는 것이고, 성능을 높이려면 HashMap 객체를 생성할 때 적정한 해시 버킷 개수를 지정해야 한다.

그런데 이렇게 해시 버킷의 크기를 두 배로 확장하는 것에는 결정적인 문제가 있다. 해시 버킷의 개수 M이 2^a이 되기 때문에, index = X.hashCode() % M을 계산할 때 X.hashCode()의 하위 a개의 비트만 사용하게 되는 것이다. 즉 해시 함수가 32비트 영역을 고르게 사용하도록 만들었다 하더라도 해시 값을 2의 제곱수로 나누면 해시 충돌이 쉽게 발생할 수 있다.
(M == 2^a)

[보조 해시 함수]
index = X.hashCode() % M을 계산할 때 사용하는 M값은 '소수'일 때 index값 분포가 가장 균등할 수 있다. 그러나 'M값이 소수가 아니기 때문에' 별도의 '보조 해시 함수'를 이용하여 index 값 분포가 가급적 균등할 수 있도록 해야 한다. 

보조 해시 함수(supplement hash function)의 목적은 '키'의 해시 값을 변형하여, 해시 충돌 가능성을 줄이는 것이다. 

```java
final int hash(Object k){
    int h = hashSeed;
    if (0 != h && k instanceof String){
        return sun.misc.Hashing.stringHash32((String) k);
    }
    h ^= k.hashCode();
    // 해시 버킷의 개수가 2^a이기 때문에 해시 값의 a비트 값만을
    // 해시 버킷의 인덱스로 사용한다. 따라서 상위 비트 값이
    // 해시 버킷의 인덱스 값을 결정할 때 반영될 수 있도록
    // shift 연산과 XOR 연산을 사용하여, 원래의 해시 값이 a비트 내에서
    // 최대한 값이 겹치지 않고 구별되게 한다.
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h  >>> 7) ^ (h >>> 4);
}
```

Java 8 HashMap 보조 해시 함수는 상위 16비트 값을 XOR 연산하는 매우 단순한 형태의 보조 해시 함수를 사용한다. 이유로는 두 가지가 있는데, 첫 번째에서는 Java 8에서는 해시 충돌이 많이 발생하면 링크드 리스트 대신 트리를 사용하므로 해시 충돌 시 발생할 수 있는 성능 문제가 완화되었기 때문이다. 두 번째로는 최근의 해시 함수는 균등 분포가 잘 되게 만들어지는 경향이 많아, Java 7까지 사용했던 보조 해시 함수의 효과가 크지 않기 때문이다. 두 번째 이유가 좀 더 결정적 원인이 되어 Java 8에서는 보조 해시 함수의 구현을 변경하였다.

[String 객체에 대한 해시 함수]
String 객체에 대한 해시 함수 수행 시간은 문자열 길이에 비례한다. 

```java
public int hashCode(){
    int hash = 0;
    int skip = Math.max(1, length() / 8); 
    // 문자열의 길이가 16을 넘으면 최소 하나의 문자를 건너가면서 해시 함수를 계산한다.
    for (int i = 0; i < length(); i += skip){
        hash = s[i] + (37 * hash);
    }   
    return hash;
}
```

이 방식은 심각한 문제를 야기한다. 웹상의 URL은 길이가 수십 글자에 이르면서 '앞 부분은 동일하게 구성'되는 경우가 많다. 이 경우 서로 다른 URL의 해시 값이 같아지는 빈도가 매우 높아질 수 있다는 문제가 있다. 따라서 이런 방식은 곧 폐기되었다.

```java
public int hashCode() {
    int h = hash;

    if(h == 0 && value.length > 0){
        char val[] = value;

        for(int i = 0; i < value.length; i++){
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
}
```

**String 객체 해시 함수에서 31을 사용하는 이유는, 31이 소수이며 또한 어떤 수에 31을 곱하는 것은 빠르게 계산할 수 있기 때문이다. 31N = 32N - N인데, 32는 2^5이니, 어떤 수에 대한 32를 곱한 값은 shift 연산으로 쉽게 구현할 수 있다. 따라서 N에 31을 곱한 값은 (N << 5) - N과 같다. 31을 곱한 연산은 이렇게 최적화된 머신 코드로 생성할 수 있기 때문에, String 클래스에서 해시 값을 계산할 때에는 31을 승수로 사용한다.**

[결론]
Java HashMap에서는 해시 충돌을 방지하기 위하여 Separate Chaining과 보조 해시 함수를 사용한다는 것, Java 8에서는 Separate Chaining에서 링크드 리스트 대신 트리를 사용하기도 한다는 것, 그리고 String 클래스의 hashCode() 메서드에서 31을 승수로 사용하는 이유는 성능 향상 도모를 위한 것이라고 정리할 수 있다.

웹 애플리케이션 서버의 경우에는 HTTPRequest가 생성될 때마다, 여러 개의 HashMap이 생성된다. 수많은 HashMap 객체가 1초도 안 되는 시간에 생성되고 또 GC의 대상이 된다. 컴퓨터 메모리 크기가 보편적으로 증가하게 됨에 따라, 메모리 중심적인 에플리케이션 제작도 늘었다. 따라서 갈수록 HashMap에 더 많은 데이터를 저장하고 있다고 할 수 있다.