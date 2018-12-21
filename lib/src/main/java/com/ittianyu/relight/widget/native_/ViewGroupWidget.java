package com.ittianyu.relight.widget.native_;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.view.ViewGroup;

import com.ittianyu.relight.view.AndroidRender;
import com.ittianyu.relight.widget.Widget;
import com.ittianyu.relight.widget.stateful.StatefulWidget;
import com.ittianyu.relight.widget.stateless.StatelessWidget;

import java.util.LinkedList;
import java.util.List;

public abstract class ViewGroupWidget<V extends ViewGroup, T extends ViewGroupWidget> extends BaseAndroidWidget<V, T> {
    protected List<Widget> children = new LinkedList<>();

    public ViewGroupWidget(Context context, Lifecycle lifecycle, Widget... children) {
        super(context, lifecycle);

        // add children
        if (null == children)
            children = new Widget[0];

        addChildren(false, true, children);
    }

    @Override
    protected void initProps() {
    }

    @Override
    public void updateView(V view) {
        super.updateView(view);
        for (Widget widget : children) {
            if (widget instanceof AndroidRender) {
                ((AndroidRender) widget).updateView(widget.render());
            } else if (widget instanceof StatefulWidget) {
                ((StatefulWidget) widget).setState(null);
            } else if (widget instanceof StatelessWidget) {
                ((StatelessWidget) widget).update(widget);
            }
        }
    }

    /**
     * call when add view which was removed
     */
    public void updateChildrenProps() {
        for (Widget widget : children) {
            if (widget instanceof BaseAndroidWidget) {
                ((BaseAndroidWidget) widget).updateProps(widget.render());
            } else if (widget instanceof StatelessWidget) {
                ((StatelessWidget) widget).updateProps(widget);
            } else if (widget instanceof StatefulWidget) {
                ((StatefulWidget) widget).updateProps(widget);
            }
        }
    }

    public T addChild(Widget widget) {
        return addChild(widget, true);
    }

    public T addChild(Widget widget, boolean updateProps) {
        children.add(widget);
        view.addView(widget.render());
        if (updateProps) {
            updateChildrenProps();
            updateProps(view);
        }
        return self();
    }

    public T addChildren(Widget... children) {
        return addChildren(true, children);
    }

    public T addChildren(boolean updateProps, Widget... children) {
        return addChildren(updateProps, updateProps, children);
    }

    public T addChildren(boolean updateProps, boolean updateChildrenProps, Widget... children) {
        for (Widget widget : children) {
            addChild(widget, false);
        }
        if (updateChildrenProps) {
            updateChildrenProps();
        }
        if (updateProps) {
            updateProps(view);
        }
        return self();
    }

    public T removeChild(Widget widget) {
        children.remove(weight);
        view.removeView(widget.render());
        return self();
    }

    public T removeAllChildren() {
        for (Widget widget : children) {
            view.removeView(widget.render());
        }
        children.clear();
        return self();
    }

}
