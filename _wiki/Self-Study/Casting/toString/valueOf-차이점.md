1. Casting
    - Object가 String 타입을 필요로 한다는 것을 의미한다.
    - 따라서 Object가 실제로 String 문자열이여야 변환된다.

    - e.g. int value = 432;
    (String) value -> ClassCastException을 발생시킨다.
    Object value = "foo";
    (String) value -> 문자열 "foo"가 된다.
2. toString
    - Object (Wrapper Class[Integer, Character, Double ...]) 데이터를 String 문자열로 바꿔준다.
    - toString()은 메소드이므로 기본 자료형은 사용할 수 없음
    - e.g. int primiType = 3;
    String str = primiType.toString(); -> 기본 자료형은 메소드를 사용할 수 없으므로 에러가 발생한다.
    String str = (new Integer(42)).toString() -> str은 문자열 "42"가 된다.
    Object nullValue = null;
    String str = nullValue.toString() -> NullPointException
3. valueOf
    - 어떠한 값을 넣어도 모두 String 문자열로 변환할 수 있다.
    - e.g. Object nullValue = null;
    String.valueOf(nullValue) => "null"이 된다.

Casting / valueOf() 성능 비교
    - strTest1 = "" + 432;
    - strTest2 = String.valueOf(432); -> 더 빠르다.
    - strTest1의 경우 빈 문자열에 String을 +로 연산하면 컴파일러에서 데이터 판단 작업이 발생한다.

