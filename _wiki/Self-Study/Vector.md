### Vector
Vector 는 List와 비슷한데, 차이점은 Vector가 동기처리가 된다는 점.
Multi-thread 프로그래밍에서 JVM의 인스턴스 변수는 여러 thread가 동시에 접근할 수 있으므로 일관성을 유지하기 위해 동기처리가 필요하다. 

Vector는 동기처리가 되어 있어서 이를 효율적으로 관리할 수 있다. 

List 인터페이스를 구현한 클래스
- Vector
- ArrayList
- LinkedList

Set 인터페이스를 구현한 클래스
- HashSet

Map 인터페이스를 구현한 클래스
- HashMap, HashTable
- HashTable도 Vector와 같이 옛날 코드이며, 동기화가 되어 있다. 
