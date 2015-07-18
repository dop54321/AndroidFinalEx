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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;


public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final DisplayImageOptions options;
    List<GameCard> mGameCards;
    private ImageLoader imageLoader;
    private OnItemClickCallback myCallback;

    public GridRecyclerViewAdapter(List<GameCard> mGameCards, Context context) {
        super();
        this.mContext = context;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_pic)
                .showImageForEmptyUri(R.drawable.default_pic)
                .showImageOnFail(R.drawable.fail_load_image)
                        // .cacheInMemory(true)
                        // .cacheOnDisk(true)
                //.considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)

                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(1)
                .diskCacheExtraOptions(480, 320, null)
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2000000)) // You
                        // can pass your own memory cache implementation
                .build();
        ImageLoader.getInstance().init(config);
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
        String uri = null;
        Uri imageRef = imageItem.getImageRef();
        if (imageRef != null) {
            if (!fromDrawable(imageRef))
                uri = "file://" + Uri.decode(imageRef.getPath());
            else {
                uri = imageRef.toString();
            }
        }

        try {
            imageLoader.displayImage(uri, viewHolder.imgThumbnail, options);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean fromDrawable(Uri imageRef) {
//        Uri backSideCardUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getResources().getResourcePackageName(R.drawable.default_pic) +
//                '/' + mContext.getResources().getResourceTypeName(R.drawable.default_pic) +
//                '/' + mContext.getResources().getResourceEntryName(R.drawable.default_pic));

        Uri backSideCardUri = Uri.parse("drawable://" + R.drawable.default_pic);

        return imageRef.equals(backSideCardUri);
    }

    @Override
    public int getItemCount() {
        return mGameCards.size();
    }

    public List<GameCard> getmGameCards() {
        return mGameCards;
    }

    public void setMyCallback(OnItemClickCallback myCallback) {
        this.myCallback = myCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(GameCard clickedImage, View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;

        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cv);


            imgThumbnail = (SquareImageView) itemView.findViewById(R.id.img_thumbnail);
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

}
