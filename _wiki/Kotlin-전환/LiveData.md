직렬화(Serialize)

- 자바 시스템 내부에서 사용되는 Object 또는 Data를 외부 자바 시스템에서 사용할 수 있도록 byte 형태로 데이터를 변환하는 기술
- JVM의 메모리에 상주(힙 또는 스택)되어 있는 객체 데이터를 바이트 형태로 변환하는 기술

역직렬화(Deserialize)
- byte로 변환된 Data를 원래대로 Object나 Data로 변환하는 기술을 역직렬화(Deserialize)라고 부른다. 
- 직렬화된 바이트 형태의 데이터를 객체로 변환해서 JVM으로 상주시키는 형태

public static void main(String[] args){
    Member member = new Member("김배민", "deliveryKim@baemin.com", 25);
    byte[] serializedMember;
    
    try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
        try(ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(member);
            serializedMember = baos.toByteArray();
        }
    }
    
    System.out.println(Base64.getEncoder().encodeToString(serializedMember));
}


역직렬화
자바 직렬화 대상 객체는 동일한 serialVersionUID를 가지고 있어야 한다.
private static final long serialVersionUID = 1L;
직렬화 대상이 된 객체의 클래스가 클래스 패스에 존재해야 하며, import되어 있어야 한다.

java.io.ObjectInputStream을 사용하여 역직렬화를 진행한다.

public static void main(String[] args){
    String base64Member = "...생략";
    byte[] serializedMember = Base64.getDecoder().decode(base64Member);
    try(ByteArrayInputStream bais = new ByteArrayInputStream()){
        try(ObjectInputStream ois = new ObjectInputStream(bais)){
            Object objectMember = ois.readObject();
            Member member = (Member) objectMember;
            System.out.println(member);
        }
    }
}

직렬화 방법에는 세 가지 포맷이 크게 존재한다. 
표형태의 다량의 데이터를 직렬화하는 CSV
구조적인 데이터는 XML, JSON 형태

CSV
- 데이터를 표현하는 가장 많이 사용하는 방법 중 하나로 콤마를 기준으로 데이터를 구분하는 방법이다.

복잡한 데이터 구조의 클래스 객체라도 직렬화 기본 조건만 지키면 큰 작업 없이 바로 직렬화, 역직렬화가 가능하다. 
데이터 타입이 자동으로 맞춰지기 때문에 관련 부분에 큰 신경을 쓰지 않아도 된다. 

역직렬화 시 클래스 구조 변경 문제
멤버 클래스에 속성을 추가하고 이미 직렬화한 데이터(기존 클래스의 속성을 가진)를 역직렬화 시키면 java.io.InvalidClassException이 발생한다. 
위에서 언급했던 것처럼 직렬화하는 시스템과 역직렬화하는 시스템이 다른 경우에 발생하는 문제
각 시스템에서 사용하고 있는 모델의 버전 차이가 발생했을 때 생기는 문제

이를 해결하기 위해서는 모델 버전간의 호환성을 유지하기 위해 SUID(serialVersionUID)를 정의해야 한다. 
Default는 클래스의 기본 해쉬값을 사용한다.

String -> StringBuilder
int -> long으로 변경해도 역직렬화에서 Exception이 발생한다. 
자바 직렬화는 상당히 타입에 엄격하다.
멤버 변수가 빠지게 도니다면 Exception 대신 null이 들어간다.

아주 간단한 객체의 내용도 2배 이상의 차이를 확인할 수 있다. 
일반적인 메모리 기반의 cache에서는 데이터를 저장할 수 있는 용량에 한계가 있기 때문에 json과 같은 경량화된 방법으로 직렬화하자.

public boolean add(E e){
    this.add(this.size(), e);
    return true;
}

MAX_ARRAY_SIZE

public boolean contains(Object o){
    Iterator<E> it = this.iterator();
    
    if(o == null){
        while(it.hasNext()){
            if(it.next() == null){
                return true;
            }
        }
    } else {
        while(it.hasNext()){
            if(o.equals(it.next())){
                return true;
            }
        }
    }
    
    return false;
}

// toArray()

public boolean remove(Object o){
    Iterator<E> it = this.iterator();
    
    if(o == null){
        while(it.hasNext()){
            if(it.next() == null){
                it.remove();
                return true;
            }
        }
    } else {
        while(it.hasNext()){
            if(o.equals(it.next()){
                it.remove();
                return true;
            }
        }
    }
    
    return false;
}

public boolean remove(Object o){
    Iterator<E> it = this.iterator();
    
    if(o == null){
        while(it.hasNext()){
            if(it.next() == null){
                it.remove();
                return true;
            }
        }
    } else {
        while(it.hasNext()){
            if(o.equals(it.next())){
                it.remove();
                return true;
            }
        }
    }
    
    return false;
}

public boolean containsAll(Collection<?> c){
    Iterator var2 = c.iterator();
    
    Object e;
    
    do {
        if(!var2.hasNext()){
            return true;
        }
        
        e = var2.next();
    } while(this.contains(e));
    
    return false;
}

public boolean addAll(Collection<? extends E> c){
    boolean modified = false;
    Iterator var3 = c.iterator();
    
    while(var3.hasNext()){
        E e = var3.next();
        if(this.add(e)){
            modified = true;
        }
    }
    
    return modified;
}

public boolean addAll(Collection<? extends E> c){
    boolean modified = false;
    Iterator var3 = c.iterator();
    
    while(var3.hasNext()){
        E e = var3.next();
        if(this.add(e)){
            modified = true;
        }
    }
    
    return modified;
}

public boolean removeAll(Collection<?> c){
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator it = this.iterator();
    
    while(it.hasNext()){
        if(c.contains(it.next())){
            it.remove();
            modified = true;
        }
    }
    
    return modified;
}

The >>> operator is the unsigned right bit-shift operator in Java.
It effectively divides the operand by 2 to the power of the 

Let's average 1 and Integer.MAX_VALUE
We can do the math easily
1 + Integer.MAX_VALUE /2
>>>
The difference between >> and >>> would only show up when shifting negative numbers. The >> operators shifts a 1 bit into the most significant bit if it was a 1, and the >>> shifts in a 0 regardless.
most significant bit 
    
































