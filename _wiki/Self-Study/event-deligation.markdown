<!-- Bad case -->
```javascript
const lis = document.querySelectorAll('li');
lis.forEach(li => {
    li.addEventListener('click', () => {
        li.classList.add('selected');
    });
});
```

<!-- Good case -->
<!-- 부모에 한꺼번에 event를 등록하는 것이 좋다 -->
```javascript
const ul = document.querySelector('ul');
ul.addEventListener('click', event => {
    if(event.target.tagName == 'LI') {
        event.target.classList.add('selected');
    }
});
```