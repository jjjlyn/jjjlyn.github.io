### Intent
Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

The Observer pattern describes how to establish these relationships. The ky objects in this pattern are subject and observer. A subject may have any number of dependent observers. All observers are notified whenever the subjects undergoes a change in state. In response, each observer will query the subject to synchronzie its state with the subject's state. 
This kind of interaction is also known as publish-subscribe. The subject is the publisher of notifications. Subject가 변화의 주체(여기에 다 depend해서 subject를 observe한다). The subject is the publisher of notifications. It sends out these notifications without having to know who its observers are(subject는 변화를 publish해주긴 하는데, 얘를 구독하는 observers가 누군지 알 필요가 없다) Any number of observers can subscribe to receive notifications. 
- Subject 
  knows its observers. Any number of Observer objects may observe a subject.(구독자가 몇 명인지 알고 있음)
  provides an interface for attaching and detaching Observer objects.
- Observer
  defines an updating interface for objects that should be notified of changes in a subject. (notify() interface를 일단 정의한다는 뜻인 것 같다)
- ConcreteSubject
  stores state of interest to ConcreteObserver objects. (특정 Observer object가 특정 파트의 UI를 바꾸고 싶어할 것 아님? 그 상태를 저장한다는 것 같음)
  자기 상태가 바뀔 때마다 구독자(observers)에게 notification을 준다.
- ConcreteObserver
  maintains a reference to a ConcreteSubject object. (ConcreteSubject에 대한 참조를 유지한다.)
  stores state that should stay consistent with the subject's. (자기가 구독하는 Subject의 상태와 동기화되도록 상태를 저장한다.)
  implements the Observer updating interface to keep its state consistent with the subject's (여기서 Observer의 defined interface를 implement한다는 뜻인듯)
  
Collaborations
- ConcreteSubjects notifies its observers whenever a change occurs that could make its observers' state in consistent with its own.
- After being informed of a change in the concrete subject, a ConcreteObserver object may query the subject for information. ConcreteObserver uses this info to reconcile its state with that of the subject.

[[Factory-Method-패턴]]
[[Singleton-패턴]]
[[Observer-패턴]]
[[Factory-Method-패턴]]
[[practice]]

팩토리 패턴과 빌더 패턴과 탬플릿 메소드 패턴과 ... 도대체 각각의 차이가 무엇인가?
