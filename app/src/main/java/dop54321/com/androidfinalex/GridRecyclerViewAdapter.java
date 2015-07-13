package dop54321.com.androidfinalex;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;


public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    List<GameCard> mGameCards;
    private ImageLoader imageLoader;
    private final DisplayImageOptions options;

    private OnItemClickCallback myCallback;

    public GridRecyclerViewAdapter(List<GameCard> mGameCards,Context context) {
        super();
        this.mContext=context;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_pic)
                .showImageForEmptyUri(R.drawable.default_pic)
                .showImageOnFail(R.drawable.default_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();




        this.mGameCards = mGameCards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        GameCard imageItem = mGameCards.get(i);
        String uri=null;
        Uri imageRef = imageItem.getImageRef();
        if (imageRef != null) {

            uri="file://" +Uri.decode(imageRef.getPath());
        }

        try {
            imageLoader.displayImage(uri, viewHolder.imgThumbnail,options);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mGameCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;

        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);

            cardView= (CardView) itemView.findViewById(R.id.cv);


            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myCallback != null) {
                        myCallback.onItemClicked(mGameCards.get(getAdapterPosition()), v, getAdapterPosition());
                    }
                }
            });
        }
    }
    public interface OnItemClickCallback{
        void onItemClicked(GameCard clickedImage, View view, int position);
    }

    public List<GameCard> getmGameCards() {
        return mGameCards;
    }

    public void setMyCallback(OnItemClickCallback myCallback) {
        this.myCallback = myCallback;
    }

}
