#### Gitflow가 무엇인가?
Vincent Driessen이 제안한 git의 workflow 디자인에 기반한 브랜칭 모델이다. 좀 더 자세히 말하자면 Vincent는 master, develop, feature, release, hotfix 5개 종류의 branch로 나눠서 관리한다고 했다. 

- feature
어떤 새로운 기능이 추가되어야 할 때 사용되는 branch다. 개발이 완료되면 parent인 develop branch로 merge된다. master branch에는 직접적으로 접근할 수 없다는 점을 기억하면 된다. 

- develop
develop branch는 product로 release를 할 준비가 된 가장 안정적인 branch라고 보면 된다. (즉, master로 merge하기 전) 개발된 모든 feature가 develop에 merge 된다. 

- release
develop branch에서 어느 정도 feature를 merge하고 이쯤에서 release해야겠다 싶을 때 생성하는 branch다. 당연히 develop branch에서 release branch로 merge하고, release가 완료되면, 다음 release cycle(?)을 진행한다. 

- hotfix
release된 product에서 발생한 버그같은 것을 수정할 때 사용되는 branch다. 수정이 완료된 후에는 수정사항을 반영하기 위해 master와 develop branch에 모두 merge된다. 

- master
가장 core가 되는 branch는 master와 develop이다. 그 중 master는 product로 release하는 branch다. 모든 변경사항이 결국은 master로 merge되어야 한다. 

### Gitflow 설치 및 적용
git이 설치된 상태에서 git flow 명령어를 치면 동작한다. 
> git이 설치되어 있다면 굳이 git flow를 따로 설치할 필요 없음.


#### # 새 기능(feature) 시작
```git
git flow feature start [Branch_Name]
```
develop branch에 기반한 새 branch를 만들고, 그 branch로 전환한다.

#### # 기능 완료
새 기능의 개발이 완료되면 아래 명령어를 수행한다.
```git
git flow feature finish [Branch_Name]
```
feature branch를 develop branch에 merge하고, 해당 feature branch를 삭제하고 develop branch로 돌아간다. 

#### # 기능 게시(publish)
다른 팀원들과 공동으로 개발 중이라면, remote에 게시하여 다른 사람들이 사용할 수 잇게 한다.
git flow feature publish [Branch_Name]

#### # 게시된 기능 가져오기
다른 사람이 게시한 기능을 가져온다.
git flow feature pull origin [Branch_Name]

### Release
새로운 제품 출시를 준비하면서, 버그 수정이나 메타 데이터를 준비하는 과정이다.

#### # Release 시작
develop branch에서 release branch를 생성한다.
```git
git flow release start <version>
```
그런데, 위와 같이 release branch를 생성하게 되면 문제점이 하나 있다. local 저장소에만 branch를 생성한 것이기 때문에 다른 팀원들의 release commit을 반영할 수 없다. 따라서 다른 사람들의 commit도 반영하고 싶다면 아래와 같이 release branch를 생성하자. (정확히는 게시한다는 것이 맞다)
```git
git flow release publish <version> 
```

#### # 릴리즈 완료
릴리즈가 완료되면 release version으로 tag를 달고, master branch에 merge한다. 추가적으로 release 변경사항을 develop branch에 반영(merge)하고, release branch는 삭제한다.
```git
git flow release finish <version>
```

* 태그를 push하는 것도 잊지 말 것

### Hotfix
현재 출시된 제품에 문제가 생겨서 즉시 대응해야 할 때 필요하다. master branch에서 hotfix branch를 따서 시작한다.
#### # 핫픽스 시작
```git
git flow hotfix start <version> [BASENAME]
```
핫픽스를 시작할 때, 옵션으로 'BASENAME'으로 시작점을 지정할 수 있다고 한다.
#### # 핫픽스 완료
핫픽스를 완료하면 develop branch와 master branch로 merge된다.
```git
git flow hotfix finish <version>
```
