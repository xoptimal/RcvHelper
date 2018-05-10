package com.xoptimal.revhelper.provider;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Freddie on 2016/11/14 0014 .
 * Description:
 */
public abstract class ExItemViewBinder<T> extends ItemViewBinder<T, BaseViewHolder> {

    public abstract int getLayoutId();

    @NonNull
    @Override
    protected BaseViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(getLayoutId(), parent, false);
        return new BaseViewHolder(root);
    }

    protected OnClickHandle onClickHandle;

    public ExItemViewBinder setOnClickHandle(OnClickHandle onClickHandle) {
        this.onClickHandle = onClickHandle;
        return this;
    }

    public interface OnClickHandle {
        void onHandle(int operation);
    }

}
