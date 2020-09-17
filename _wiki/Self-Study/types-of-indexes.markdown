메모리의 데이터베이스 버퍼 캐시
`버퍼 캐시`에는 자주 사용되는 테이블들이 캐싱되어 있는데 여기서 데이터가 있을 경우에는 바로 찾아 출력하고, 없을 경우에는 `하드 디스크`에 있는 데이터 파일에서 데이터를 찾기 시작한다.
인덱스를 사용한다면 이러한 과정을 거치지 않고 바로 `주소`를 통해 찾아간다. 
예를 들어 홍길동의 집에 택배를 배송하기 위해 대한민국 전국을 뒤져야 한다(full scan).
하지만 인덱스를 사용한다면 바로 배송지로 택배를 배송할 수 있다. 

인덱스의 자료구조는 SortedList로 저장되는 값을 항상 `정렬` 상태로 유지한다. 정렬되어 있으므로 SELECT 쿼리 속도가 빠르다(왜??). 디스크 탐색 속도가 빠르다(왔다 갔다 안해도 되니까). 그러나 INSERT, UPDATE, DELETE 쿼리는 정렬을 해야해서 쿼리 수행시간이 늘어난다. 

해당 테이블을 모두 읽고 인덱스를 만드는 동안 데이터가 변경되면 문제가 되므로 해당 데이터들이 변경되지 못하도록 조치한 후 메모리에 정렬하게 된다. 
전체 테이블 스캔 -> 정렬 -> Block 기록

일반적으로 인덱스의 종류는 B-TREE, BITMAP으로 나뉜다.
[B-TREE]
실제 테이블의 `주소`는 Leaf Block에 전부 들어있다. 
해당 데이터들에 대한 데이터들이 `Branch Block`과 `Root Block`에 들어 있다. 
특정 데이터를 찾아야 할 경우 Root Block에서 Branch Block을 찾고
그 다음 Leaf Block 정보를 찾아가서 해당 데이터의 row id를 찾아 데이터가 들어 있는 블록을 `메모리`로 복사해 온다. 
B-TREE 인덱스의 종류로는
UNIQUE INDEX, Non UNIQUE INDEX, Function Based INDEX, DESCENDING INDEX, 결합 인덱스(Composite INDEX -- 두 개 컬럼 이상 조합한 인덱스)등이 있다. 

이 중 결합 인덱스는 인덱스를 생성할 때 두 개 이상의 컬럼을 합쳐서 인덱스를 만드는 것이다. 예를 들어 성별과 이름을 이용하여 데이터를 찾는다고 가정하자. 테이블 데이터는 총 60건이 존재한다. 성별은 남자, 이름은 홍길동인 데이터를 찾아보자.
60명 -> '남자' -> 25명
25명 -> '홍길동' -> 2명

60명 -> '홍길동' -> 2명
2명 -> '남자' -> 2명

위의 상황처럼 인덱스의 사용 순서도 중요한 역할을 하게 된다.
B-Tree index speeds up data access because the storage engine doesn't have to scan the whole table to find the desired data. It starts at the `root node`, instead. The slots in the root node hold pointers to child nodes, and the storage engine follows these pointers. It finds the right pointer by looking at the values in the node pages, which define the upper and lower bounds of the values in the child nodes. 

B-TREE 형식의 인덱스는 주로 데이터 값의 종류가 많고 동일한 데이터가 적을 경우에 사용하는 인덱스다. 반면 BITMAP INDEX는 데이터 값의 종류가 적고 동일한 데이터가 많을 경우에 사용한다. 이름처럼 데이터가 어디 있다는 지도 정보를 BIT로 표시한다. 데이터가 있으면 '1', 없으면 '0'으로 표시한다. 
Map은 인덱스 컬럼 개수만큼 만들어진다.
예를 들어 회원 정보에 '지역'이라는 컬럼이 있다면
- 서울: 1 0 1 0 0
- 대전: 0 1 0 0 1
- 대구: 0 0 0 1 1
이런 식으로 MAP이 생성된다. 이 때 '울산'이라는 데이터가 추가된다면
BITMAP INDEX를 전부 수정해야하고 '울산'이라는 MAP을 추가해야 한다.
즉 B-TREE INDEX는 관련 블록만 변경하면 되지만 BITMAP INDEX는 모든 맵을 다 수정해야 한다는 큰 문제점이 있다. 그래서 주로 데이터 변경이 안되는 테이블과 값의 종류가 작은 컬럼에 생성하는 것이 유리하다. 

B-TREE 구조에서 리프 노드는 링크드 리스트로 서로 연결되어 있고, 저장된 키는 `정렬`되어 있어서 `순차 처리`가 용이하다. 그렇기 때문에 `범위`를 검색하는 데 용이하다. 테이블에서는 처음부터 끝까지 모든 레코드를 읽어야 완전한 결과 집합을 얻을 수 있지만, 인덱스는 키-컬럼 순으로 정렬되어 있기 때문에 `특정 위치에서 검색을 시작`해서 `검색 조건이 일치하지 않는 값을 만나는 순간 검색을 멈출 수 있다`. 이것을 인덱스 범위 스캔이라고 한다. 범위 스캔에는 두 개의 키가 필요한데, 범위의 양 끝을 표현하는 하위 키와 상위 키가 그것이다. 인덱스 범위 스캔은 두 단계로 진행된다. 첫 번째 단계에서는 `루트`에서부터 트리를 순회하여 `리프 노드`에서 하위 키를 찾는다. 두 번째 단계에서는 첫 번째 단계에서 찾은 키에서부터 상위 키까지 순차적으로 레코드를 읽어 처리한다. 상위 키가 현재 노드에서 발견되지 않으면 다음 노드를 읽어 상위 키를 가진 노드까지 검색을 계속해 나간다. 상위 키까지 순차 검색이 끝나면 전체 범위 검색이 완료된다. 

Leaf pages는 다른 페이지로 갈 수 있는 포인터 대신에 indexed data로 갈 수 잇는 포인터를 가지고 있다. 이 책에서는 node page(root ~ leaf를 중개하는)를 1 depth로만 나타냈지만 이는 더 깊어질 수 있고, depends on how big the table is.
B-Trees store the indexed columns in order!! 
그래서 범위 탐색에 적절하다.
"everyone whose name begins with I through K" is efficient
```sql
CREATE TABLE People(
    last_name varchar(50) not null,
    first_name varchar(50) not null,
    dob date not null,
    gender enum('m', 'f') not null,
    key(last_name, first_name, dob)
);
```

인덱스는 last_name, first_name, dob columns 컬럼을 모든 row에 갖고 있다. Let's illustrate how the index arranges the data it stores.
Notice that the index sorts the values according to the order of the columns given in the index in the CREATE TABLE statement. 

B-Tree indexes are useful only if the lookup uses a leftmost prefix of the index. B-Tree 인덱스는 항상 정렬되어 있기 때문에, 이는 looksup과 ORDER BY에 쓰인다. 즉 ORDER BY 쿼리를 쓸 때 인덱스가 있으면 will be helpful

단점
- Not start from the leftmost side of the indexed columns 에는 not useful
For example, this index won't help you find all people named Bill or all people born on a certain date, because those columns are not leftmost in the index. Likewise, you can't use the index to find people whose last names `ends with a particular letter`.
- You can't skip columns in the index. If you don't specify a value for the first_name column, MySQL can use only the first column of the index. 키를 세 개(컬럼) 설정했으면 하나라도 스킵하면 안 된다. 만약 first_name이 스킵됐다면, last_name만을 인덱스로 삼아버린다.
- The storage engine can't optimize accesses with any columns to the right of the first range condition. 범위 검색 조건의 우측에 있는 컬럼은 인덱스로 삼지를 못한다.
```sql
WHERE last_name = 'Smith' AND first_name LIKE 'J%' AND dob = '1976-12-23';
```
인덱스는 앞에서부터 두 컬럼만 인덱스로 사용할 것이다. 왜냐하면 LIKE is a range condition. For a column that has a limited number of values, you can often work around this by specifying equality conditions instead of range conditions.

You might need to create indexes with the same columns in different orders to satisfy your queries.

[HASH INDEX]
해쉬 테이블에 build되는 해쉬 인덱스. 테이블 내 `모든 컬럼`을 인덱스로 삼을 때 유용하다.
B-Tree만큼 범용적이지는 않다. 동등 비교 검색(해시가 뭐 그렇지)에는 최적화 돼 있지만 `범위`를 검색한다거나 `정렬된 결과`를 가져오는 목적으로는 사용할 수 없다. 
InnoDB의 버퍼 풀에서 빠른 레코드 검색을 위한 Adaptive Hash Index로 사용되기도 한다. When InnoDB notices that some index values are being accessed very frequently, it builds a hash index for them in memroy on top of B-Tree indexes. This gives its B-Tree indexes some properties of hash indexes, such as very fast hashed lookups. This process is completely automatic, and you can't control or configure it, although you can disable the adaptive hash index together. 

해시 인덱스는 트리 형태의 구조가 아니라 검색하고자 하는 값을 주면 해시 함수를 거쳐서 찾고자 하는 키값이 포함된 `해시 버킷`을 알아낼 수 있다. 그리고 그 버킷 하나만 읽어서 비교해보면 실제 레코드가 저장된 위치를 바로 알 수 있다. 그래서 트리 내에서 여러 노드를 읽어서 레코드 주소를 알아낼 수 있는 B-Tree보다 상당히 빨리 결과를 낼 수 있음. 
[키 값 + 레코드 주소값] => 고정된 크기의 저장공간을 `노드`라고 한다.
해시 인덱스에서는 이를 `버킷`이라고 한다. ㄹㅇ? 아닐텐데...
또한 해시 인덱스는 해시 함수의 결과(키 값을 해시 함수 돌린 integer 값)만을 저장하므로 키 컬럼의 값이 아무리 길어도 실제 해시 인덱스에 저장되는 값은 4~8바이트 수준으로 줄어든다. 해시 인덱스에서 가장 중요한 것은 역시 `해시 함수`. 해시 함수 결과 값의 범위가 넓으면 그만큼 버킷이 많이 필요해져서 공간의 낭비가 커지고, 값의 범위가 너무 작으면 해시 충돌 빈도수가 높아져 인덱스의 장점이 사라진다. 해시 알고리즘은 DBMS에서는 주로 검색을 위한 인덱스와 테이블의 파티셔닝을 위해 사용된다. 

Notice that the slots are ordered, but the rows are not. Now, when we execute this query:
mysql> SELECT lname FROM testhash WHERE fname='Peter';

MySQL will calculate the hash of 'Peter' and use that to look up the pointer in the index. Because f('Peter') = 8784, MySQL will look in the index for 8784 and find the pointer to row 3. The final is to compare the value in row 3 to 'Peter', to make sure it's the right row.
Because the indexes themseleves store only short hash values, hash indexes are very compact. As a result, lookups are usually lightning fast. However, hash indexes have some limitations:
- the index contains only hash codes and row pointers rather than values themselves, MySQL can't use the values in the index to avoid reading the rows. (MySQL은 꼭 행까지 읽어줘야 한다.) Fortunately, accessing the in-memory rows is very fast, so this doesn't usually degrade performance(그러나 메모리 안의 행에 접근하는 것은 매우 빠른 속도로 이루어지기 때문에 성능 저하 원인은 아니다).
- MySQL can't use hash indexes for sorting because they don't store rows in sorted order. (행의 value를 정렬해주지 않음)
- Hash indexes don't support partial key matching. They compute the hash from the `entire indexed value`. That is, if you have an index on (A, B) and your query's WHERE clause refers only to A --> the index won't help.
- Hash indexes support only `equality comparisons` that use the =, IN(), and <=> operators (note that <> and <=> are not the same operator). They can't speed up range queries, such as WHERE price > 100.
- 해시 충돌만 없으면 hash idx 접근 방식은 매우 빠르다. (여기서 해시 충돌이란 multiple values with the same hash) 해시 충돌이 발생하면, the storage engine must follow each row pointer in the linked list and compare their values to the lookup value to find the right row(s). 같은 해시 버킷에 위치한 링크드 리스트를 순회하여 right row를 찾아야 한다.
- Some index maintenance operations can be slow if there are many hash collisions. For example, if you create a hash index on a column with a very low selectivity(many hash collisions) and then delete a row from the table, finding the pointer from the index to that row might be expensive(링크드 리스트 순회 비용). The storage engine will ahve to examine each row in that hash key's linked list to find and remove the reference to the one row you deleted.

These limitations make hash indexes useful only in special cases. However, when they match the application's needs, they can improve performance dramatically. 

Building your own hash indexes. If your storage engine doesn't support hash indexes, you can emulate them yourself in a manner similar to that InnoDB uses. This will give you access to some of the desirable properties of hash indexes, such as a very small index size for very long keys.
The idea is simple: 의사 해시 인덱스를 B-Tree 인덱스 위에 만든다. 실제 해시 인덱스와 완벽하게 같진 않을 것인데, 이는 여전히 B-Tree index lookups(탐색)을 사용하기 때문이다. 그러나 key의 hash value를 탐색에 활용한다(실제 키 대신에). All you need to do is specify the `hash function` manually in the query's WHERE clause. 해시 함수만 잘 짜놓고 해시 코드를 추출하여 B-Tree 인덱스 위에 만들어 놓으면, 이 해시 코드를 이용하여 탐색하게 되는 것.

[Benefits of Indexes]
디스크에서 조회해 온 값 중 인덱스로 선언한 컬럼과 ORDER BY로 선언한 컬럼이 같을 경우, LIMIT(0, 10)이 걸렸다고 가정하자. 디스크에서 조회한 전체 값 중 총 10건만 메모리(Temporary Memory)로 복사되어 올라온다. 