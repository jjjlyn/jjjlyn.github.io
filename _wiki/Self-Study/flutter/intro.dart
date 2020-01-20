Flutter는 React로부터 영감을 얻어 출시된 위젯으로 최신 프레임워크를 사용함.
가장 핵심은 UI를 **위젯**이라는 단위로 그려내는 것(build)하는 것이다.
위젯은 현재 환경설정(configuration)과 상태(state)를 고려하여 view가 어떻게 보여야 할지를 결정한다.
When a widget's state changes, the widget rebuilds its description, which the framework diffs against the previous description in order to determine the minimal changes needed in the underlying render tree to transition from one state to the next.
위젯 상태가 변화할 경우, 위젯은 다시 rebuild된다. 프레임워크가 
- Text
- Row, Column
- Stack
- Container

