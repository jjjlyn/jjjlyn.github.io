* 표준 라이브러리: `String.toIntOrNull(): Int?`, `String.toDoubleOrNull(): Double?` 등 잘못된 숫자에 대해 Exception 발생없이 문자열을 숫자로 변환할 수 있는 확장을 String에 추가했다. 
`Int.toString()`, `String.toInt()`, `String.toIntOrNull()`과 같은 정수 변환 함수도 `radix` 파라미터를 가진 오버로딩한 함수를 갖는다(?)

