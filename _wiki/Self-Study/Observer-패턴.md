내가 평소 안드로이드 개발할 때 많이 쓰는 OnClickListener가 옵저버 패턴의 용례다. 

class Button {

    public void onClick(){
        mListener.onClick(this);
    }
    
    public void setOnClickListener(OnClickListener listener){
        this.mListener = listener;
    }
    
    public interface OnClickListener{
        void onClick(Button button);
    }
    
    private OnClickListener mListener;
}

public static void main(String[] args){
    
    Button button = new Button();
    button.setOnClickListener(new OnClickListener(){
        @Override
        void onClick(Button button){
            println(button + "is clicked");
        }
    });
}

이 방식이 옵저버 패턴이라고 한다.
옵저버 패턴은 Observable(관찰당하는 대상)과 Observer(관찰자)가 서로 긴밀한 연관관계를 이루어야 한다. 
이는 Observable 객체에 addObserver(new Observer)를 등록하는 것으로 구현될 수 있다. Observalbe에 Observer가 등록되어야 Observable이 변화할 때마다 그 변화를 Observer가 감지할 수 있게 된다. 여기서 Observer는 update()라는 메소드를 갖고 있는 인터페이스다.
