# Recursion

[Factorial Function]
One way to describe repetition within a computer program is the use of loops, such as Java's while-loop and for-loop constructs. An entirely different way to achieve repetition is through a process known as recursion.
Recursion is a technique by which a method makes one or more calls to itself during execution.

```java
public static int factorial(int n) throws IllegalArgumentException {
    if(n < 0)
        throw new IllegalArgumentException();
    else if(n == 0)
        return 1;
    else 
        return n*factorial(n-1);
}
```
Repetition is achieved through repeated recursive invocations of the method.
The process is `finite` because each time the method is invoked, its argument is smaller by one, and when a base case is reached, no further recursive calls are made.
자바에서 메서드가 호출될 때 activation record (혹은 activation frame)이라는 구조가 형성된다. 이 프레임 안에는 메서드의 parameter, local variables, info abouth which command in the body of the method is currently executing(현재 실행되고 있는 코드 정보) 등이 저장된다. 메서드 실행 중 메서드 내에서 또 다른 메서드(nested method)의 호출이 일어나면, 앞선 호출이 일단 중지된다. 이 메서드의 프레임은 the place in the source code at which the flow of control should continue upon return of the nested call(nested call 이 return되었을 때 다시 코드가 실행되어야 하는 지점)을 저장한다. 이런 절차는 일반적인 메서드 내 메서드 호출 뿐 아니라, 재귀적 호출에서도 이루어진다. 여기서 중요한 포인트는, 각 active call 당 따로 따로 프레임이 형성된다는 것이다.

[Drawing an English Ruler]
The length of the tick designating a whole inch : major tick length
Between the marks for whole inches, the ruler contains a series of minor ticks
As the size of the interval decreases by half, the tick length decreases by one.
(a) a 2-inch ruler with major tick length 4
(b) a 1-inch ruler with with major tick length 4
(c) a 3-inch ruler with major tick length 3

A Recursive Approcach to Ruler Drawing
다양한 크기의 같은 모양을 가진 프랙탈 패턴. Consider the rule with major tick length 5 shown in Figure 5.2(b). Ignoring the lines containing 0 and 1, let us consider how to draw the sequence of ticks lying between these lines. The central tick (at 1/2 inch) has length 4. Observe that the two patterns of tick above and below this central tick are identical, and each has a central tick of length 3. 전체 길이의 1/2가 되는 지점의 길이가 4인 막대를 기준으로, 위 아래가 동일한 패턴을 이루고 있다. 또한 이 패턴 한 블록의 가운데 막대 길이는 3이다. 

In general, an interval with a central tick length L >= 1 is composed of:
- An interval with a central tick length L-1 (앞 패턴)
- A single tick of length L (중간)
- An interval with a central tick length L-1 (뒤 패턴)
Although it is possible to draw such a ruler using an iterative process, the task is considerably easier to accomplish with recursion. 루프를 돌려서 해결하는 방법도 있으나, 재귀를 써서 해결하면 한결 간단히 구현할 수 있다.
The main method,`drawRuler` manages the construction of the entire ruler. Its arguments specify the total number of inches in the ruler and the major tick length. The utility method, `drawLine`, draws a single tick with a specified number of dashes(----) (and an optional integer label that is printed to the right of the tick -> 1, 2 등 자에 숫자 표식).
The interesting work is done by the recursive `drawInterval` method. This method draws the sequence of minor ticks within some interval, based upon the length of the interval's central tick(중간의 tick에 dash가 몇갠가를 기준으로 그린다). We rely on the intuition shown at the top of this page, and with a base case(수렴해야 할 곳) when L = 0 that draws nothing. For L >= 1, the `first` and `last`steps are performed by recursively calling `drawInterval(L-1)`. The middle step is performed by calling method `drawLine(L)`.

```java
public static void drawRuler(int nInches, int majorLength){
    drawLine(majorLength, 0);
    for(int j = 1; j <= nInches; j++){
        drawInterval(majorLength -1);
        drawLine(majorLength, j);
    }
}

private static void drawInterval(int centralLength){
    if(centralLength >= 1){
        drawInterval(centralLength - 1);
        drawLine(centralLength);
        drawInterval(centralLength - 1);
    }
}

private static void drawLine(int tickLength, int tickLabel){
    for(int j = 0; j < tickLength; j++){
        System.out.print("-");
    }
    if(tickLabel >= 0){
        System.out.print(" " + tickLabel);
    }
    System.out.print("\n");
}
```

**재귀를 쉽게 생각하는 법** 
n과 (n-1)의 규칙을 찾는다.
1. 러시안 인형(똑같은 인형이 계속 나오는 형식)을 생각한다.
2. (n - 1) -> n 으로 갈 때 (n - 1)에서 나오는 패턴이 n에 동일하게 들어 있는지, 이 패턴이 n에서는 어떤 식으로 쓰여 있는지를 살펴봐야 한다.
3. English Ruler
4. Binary Search
배열이 sorted되어 있을 경우 target은 적어도 가장 작은 인덱스와 가장 큰 인덱스의 사이(포함)에 있을 것이다. 이 target value를 sorted array의 가장 작은 값(인덱스 0)과 가장 큰 값(n-1)을 1/2으로 나눈 값과 비교한다. 이는 업다운 술게임의 원리와 비슷하다. 
- target == 중간값일 경우 탐색이 종료된다.
- target < 중간값일 경우, 중간값 앞에 있는 배열에서의 중간값과 target을 또 비교(재귀)
(from index 0 to mid-1 의 중간값)
- target > 중간값일 경우, 중간값 뒤 배열에서의 중간값과 target을 또 비교(재귀)
(from mid+1 to n-1 의 중간값)

단, 가장 작은 인덱스의 값이 가장 큰 인덱스의 값보다 클 경우 탐색은 실패한다.
Linear search(선형 탐색)이 O(n)인데 반해, 이진 탐색은 O(log n)으로 훨씬 효율적이다. 만약 n이 1billion, log n은 겨우 30.

[구현]
```java
// 이건 먼저 보지 말고 내가 직접 구현해보자.
```

