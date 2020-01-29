```java
private fun setVisibility(view: View, visibility: Int) {
            if (view == null) {
                return
            }
            if (view.visibility != visibility) {
                view.visibility = visibility
            }
        }
```
