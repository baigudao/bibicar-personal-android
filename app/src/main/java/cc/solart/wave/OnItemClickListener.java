package cc.solart.wave;

import android.support.v7.widget.RecyclerView;

/**
 * Created by jackie on 2017/9/9 13:59.
 * QQ : 971060378
 * Used as : OnItemClickListener
 */
public abstract class OnItemClickListener {
    public void onItemLongClick(RecyclerView.ViewHolder vh,int position){}
    abstract public void onItemClick(RecyclerView.ViewHolder vh,int position);
}
