[Linked List]
링크드 리스트는 가장 기본적인 자료구조다. 링크드 리스트는 많은 요소가 특정 순서로 그루핑되어 있거나, 연결되어 있는 구조이며, 데이터 컬렉션을 다루는데 효과적이다. 배열과 비슷한데, 배열에 비해 삽입/삭제가 용이한 장점이 있다. 
런타임에서 메모리가 할당되는 애플리케이션에 링크드 리스트가 자주 이용된다. (데이터 사이즈를 컴파일 타임에서 결정하지 못하는 애플리케이션의 경우)

- Singly-linked lists
하나의 포인터에 의해 요소가 연결됨. 이 구조는 처음 요소부터 마지막 요소까지 횡단할 수 있게 해준다. 노드가 포인터로 연결되어 있으니 포인터를 타고 처음부터 끝까지 횡단. (맨 앞에서 맨 뒤로)
- Doubly-linked lists
두 개의 포인터로 연결된 노드로 이루어진 구조. 앞 <-> 뒤 양쪽으로 횡단이 가능.
- Circular lists
마지막 요소에서 다음 노드로 연결되는 포인터가 NULL로 설정되지 않고, 맨 처음 요소에 연결된 구조. 순환 구조로 횡단할 수 있다.
- Mailing lists
이메일 어플리케이션에서 볼 수 있는 구조. (이해가 안됐음)
- Scrolled lists
GUI에서 자주 볼 수 있는 구조. 스크롤 리스트에서 아래에 위치하여 화면에 띄워지지 않은 아이템과 관련있다. 이런 '숨겨진' 데이터를 링크드 리스트로 연결하여 저장한다.
- Polynomials (다항식)
다항식은 프로그래밍 언어에 내재된 데이터 타입이 아니므로, 링크드 리스트로 연결하여 하나의 term(용어)로 관리하기에 적합하다.
- Memory management
OS의 중요한 역할이다. 운영체제는 프로세스를 시스템 상에서 돌아가게 하기 위해 메모리를 할당, 요구(reclaim)하는 결정권을 갖는다. 링크드 리스트는 사용가능한(잉여의) 메모리 portion(조각)을 찾아(keep track of) 연결하여 OS의 메모리 할당에 기여하는 역할을 한다.
- LISP
AI에 이용되는 프로그래밍 언어. (몰라도 될 것 같다)
- Linked allocation of files
외부 디스크 조각화를 제거하기 위해 사용되는 파일 정리? 할당? 방법으로 사용된다. 이는 순차적인 접근에만 효과적이며, 각각의 파일 블록은 그 다음 파일 블록으로 연결하기 위한 '포인터'를 갖고 있다.
- Other data structures
stacks, queues, sets, hash tables, graphs 등에 이용된다.

[Description of Linked List]
일반적으로는 `싱글 링크드 리스트`를 보통 `링크드 리스트`로 통칭한다. 하나의 요소(element)는 자체 data + next pointer(다음 element와 연결되는)를 갖고 있으며, 싱글 링크드 리스트는 Head(가장 첫번째 노드)에서부터 원하는 요소가 나올 때까지 순차적으로(sequential) 탐색한다. 뒤에서 앞으로 가는 것은 불가능한데, 하나의 element에는 next pointer만 있지 predecessor pointer는 구현되어 있지 않기 때문이다. 이것은 큰 단점은 아닌게, 이럴 때는 doubly-linked list나 circular list를 사용하면 된다. 
개념적으로 봤을 때 링크드 리스트는 노드(element)와 노드가 pointer로 연결 연결되어 연속적인 형태를 띠고 있는 것처럼 보일 수도 있겠지만, 이건 오해다. 노드 내부에 다음 노드에 접근할 수 있는 '다음 노드의 주소(포인터)'를 저장하고 있어서, 해당 주소에 언제나 접근할 수 있는 것일 뿐, 실제로는 메모리 상에서 scattered(분산된, 흩어진) 형태를 띠고 있다. 왜냐하면 이 노드들은 dynamically 할당되기 때문이다(C에서는 malloc을 이용한다). 링크드 리스트에서는 이 노드 내 next pointer를 특별히 잘 관리해야 할 필요성이 있는데, 한번 링크가 끊겨 버리면 해당 노드에서부터 줄줄이 이어지는 다음 노드들에 대한 연결고리가 아예 없어지기 때문이다. 

```java
public void add(GameEntry e){
    int newScore = e.getScore();
    if(numEntries < board.length || newScore > board[numEntries -1].getScore()){
        if(numEntries < board.length){
            numEntries++;
        }

        int j = numEntries - 1;
        while(j > 0 && board[j-1].getScore() < newScore){
            board[j] = board[j-1];
            j--;
        }
        board[j] = e;
    }
}
```

- 전체 numEntries가 board.length보다 작을 경우
- 새로 들어오는 newScore가 numEntries의 맨 마지막 점수보다 클 경우
1. 전체 numEntries가 board.length보다 작을 경우 numEntries를 1 증가시킨다.
2. j는 배열에 담긴 인덱스 중 맨 끝에 있으며, 이는 최종 연산이 이루어진 이후에 새로 들어온 score보다 하나 앞서 있게 된다. 
```java
int j = numEntries - 1;
while(j > 0 && board[j-1].getScore() < newScore){
    board[j] = board[j-1];
    j--;
}

// In order to remove and return the game entryat index i in our array, we must first save e in a temporary variable. 
private void remove(int i){
    GameEntry e = Board[i];
    while(numEntries > i+1){
        Board[i] = Board[i+1];
        i++;
    }

    Board[numEntries -1] = null;
    numEntries--;
}

public GameEntry remove(int i) throws IndexOutOfBoundsException {
    if(i < 0 || i >= numEntries){
        throw new IndexOutOfBoundsException("Invalid index: " + i);
    }
    GameEntry temp = board[i]; // save the object to be removed
    for(int j = i; j < numEntries -1; j++){ 
        // count up from i (numEntries should be removed by 1 because board[i] will be removed)
        board[j] = board[j+1];
    }
    board[numEntries -1] = null; // 마지막 numEntries -1 였던 곳은 null로 바꾼다. null out the old last score
    numEntries--; // numEntries를 요소 하나가 빠져나갔으니 -1 해준다. 
    return temp;
}

private void add(int newScore){
    if(numEntries < board.length || board[numEntries-1].getScore() < newScore){
        if(numEntries < board.length){
            numEntries++;
        }

        int j = numEntries - 1;
        while(j > 0 && board[j-1].getScore() < newScore){  
            board[j] = board[j-1];
            j--;
        }
        board[j] = newScore;
    }
}
```

Inserting a new element at the beginning of a singly linked list. Note that we set the next pointer of the new node before we reassign variable head to it. 

```java
public class SinglyLinkedList<E> {
    private static class Node<E>{
        private E element;
        private Node<E> next;

        public Node(E e, Node<E> n){
            element = e;
            next = n;
        }

        public E getElement(){
            return element;
        }

        public Node<E> getNext(){
            return next;
        }

        public void setNext(Node<E> n){
            next = n;
        }
    }

    // size
    private int size = 0;
    // head
    private Node<E> head = null;
    // tail
    private Node<E> tail = null;
    // isEmpty
    private boolean isEmpty(){
        return size == 0;
    }
    // first
    private E first(){
        if(isEmpty()){
            return null;
        }
        return head.getElement();
    }
    // last
    private E last(){
        if(isEmpty()){
            return null;
        }
        return tail.getElement();
    }
    // addFirst
    private void addFirst(E e){
        head = new Node<E>(e, head);
        if(isEmpty()){
            tail = head;
        }
        size++;
    }
    // addLast
    private void addLast(E e){
        Node<E> newest = new Node<>(e, null);
        if(isEmpty()){
            head = newest;
        }
        else {
            tail.setNext(newest);
        }
        tail = newest;
        size++;
    }
    // removeFirst
    private E removeFirst(){
        if(isEmpty()){
            return null;
        }
        E answer = head.getElement();
        head = head.getNext();
        size--;
        if(isEmpty()){
            tail = null;
        }
        return answer;
    }
}
```