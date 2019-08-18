package com.kandktech.ezivizi.colorSlider;

public interface ColorObservable {

    void subscribe(ColorObserver observer);

    void unsubscribe(ColorObserver observer);

    int getColor();
}
